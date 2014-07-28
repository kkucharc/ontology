package wedt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.*;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public final class IngredientExtractor
{

    public static Version LUCENE_VERSION = Version.LUCENE_48;
    private static ArrayList<String> stemWhitelist;
    private static StanfordCoreNLP pipeline;
    
    
    private final static IngredientExtractor instance = new IngredientExtractor();
    
    public static IngredientExtractor getInstance()
    {
        return instance;
    }
 
    private IngredientExtractor()
    {
    	Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        pipeline = new StanfordCoreNLP(props);
        
    	stemWhitelist = new ArrayList<String>();
    	loadWhitelist("whitelist.txt");
    }

    public static String lemmatize(String documentText)
    {
        List<String> lemmas = new LinkedList<String>();

        Annotation document = new Annotation(documentText);

        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences)
        {
            for (CoreLabel token: sentence.get(TokensAnnotation.class))
            {
                lemmas.add(token.get(LemmaAnnotation.class));
            }
        }

        return lemmas.toString();
    }
    
    public void loadWhitelist(String filename)
    {
    	try
    	{
	    	BufferedReader br = new BufferedReader(new FileReader(filename));
	    	String line;
	    	while ((line = br.readLine()) != null)
	    	{
	    	   stemWhitelist.add(lemmatize(line));
	    	   //System.out.println(stemmize(line));
	    	}
	    	br.close();
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }

    @SuppressWarnings("resource")
	private static String stemmize(String term) throws IOException
    {
        TokenStream tokenStream = new ClassicTokenizer(LUCENE_VERSION, new StringReader(term));
        tokenStream = new PorterStemFilter(tokenStream);
 
        Set<String> stems = new HashSet<String>();
        CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken())
        {
            stems.add(token.toString());
        }
 
        if (stems.size() != 1)
        {
            return null;
        }
 
        String stem = stems.iterator().next();
 
        if (!stem.matches("[\\w-]+"))
        {
            return null;
        }
 
        return stem;
    }
 
    private static <T> T find(Collection<T> collection, T example)
    {
        for (T element : collection)
        {
            if (element.equals(example))
            {
                return element;
            }
        }
        collection.add(example);
        return example;
    }

    @SuppressWarnings("resource")
	public static List<Ingredient> extractIngredientsFromURL(String url) throws IOException
    {
        String input = "";
        String inputLine;
            
		try
		{
			URL oracle = new URL(url);
	        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
	
	        while ((inputLine = in.readLine()) != null)
	        {
	        	input += inputLine;
	        }
	        in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    	
		input = Jsoup.clean(input, Whitelist.simpleText());
		input = Jsoup.parse(input).text();
		input = input.replaceAll("[0-9]","");
		input = input.replaceAll("-"," ");
		input = input.replaceAll("-+", "-0");
		input = input.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
		input = input.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");
		input = input.toLowerCase();
		
		int end = input.indexOf("prepa");
		if (-1 != end)
		{
			input = input.substring(0, end);				
		}
		
		end = input.indexOf("direc");
		if (-1 != end)
		{
			input = input.substring(0, end);					
		}			
		
		end = input.indexOf("method");
		if (-1 != end)
		{
			input = input.substring(0, end);					
		}
		
		int start = input.indexOf("ingred");
		if (-1 != start)
		{
			input = input.substring(start);
		}
				
		String array[] = input.split(" ");
		input = "";
		for (String word: array)
		{
			if (word.length() > 3)
			{
				input += " " + word;
			}
		}
		
        TokenStream tokenStream = new ClassicTokenizer(LUCENE_VERSION, new StringReader(input));
        tokenStream = new LowerCaseFilter(LUCENE_VERSION, tokenStream);
        tokenStream = new ClassicFilter(tokenStream);
        tokenStream = new ASCIIFoldingFilter(tokenStream);
        tokenStream = new StopFilter(LUCENE_VERSION, tokenStream, EnglishAnalyzer.getDefaultStopSet());
 
        List<Ingredient> ingredients = new LinkedList<Ingredient>();
        CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
 
        tokenStream.reset();
        while (tokenStream.incrementToken())
        {
            String term = token.toString();
            //String stem = term;
            //String stem = stemmize(term);
            String stem = lemmatize(term);
            
            if (stem != null)
            {
                Ingredient keyword = find(ingredients, new Ingredient(stem.replaceAll("-0", "-")));
                keyword.add(term.replaceAll("-0", "-"));
            }
        }
 
        Collections.sort(ingredients);
 
        List<Ingredient> finalIngredients = new LinkedList<Ingredient>();
        List<Ingredient> unknownIngredients = new LinkedList<Ingredient>();
        
        int count = 0;
        for (Ingredient ingredient: ingredients)
        {	
        	if (stemWhitelist.contains(ingredient.getStem()))
        	{
            	System.out.println("white " + ingredient.toString());
        		finalIngredients.add(ingredient);
        		count++;
        	}
        	else
        	{
            	System.out.println("unknown " + ingredient.toString());
        		unknownIngredients.add(ingredient);
        	}
        }
        
        for (Ingredient ingredient: unknownIngredients)
        {        	
        	if (7 < count || 3 < ingredient.getFrequency())
        	{
        		break;
        	}
        	else
        	{
        		finalIngredients.add(ingredient);
        		count++;
        	}
        }
        
        return finalIngredients;
    }
}