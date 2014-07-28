package wedt.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import wedt.Ingredient;
import wedt.IngredientExtractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BbcRecipeReader {
    private final IngredientExtractor ingredientExtractor;

    public BbcRecipeReader() {
        this.ingredientExtractor = IngredientExtractor.getInstance();
    }

    public List<Recipe> extractFromSite(String siteUrl) {
        String cuisine = null;
        List<String> tags = new ArrayList<String>();
        List<Recipe> recipeList = new LinkedList<Recipe>();
        try {
            Document doc = Jsoup.connect(siteUrl).get();
            Element section = doc.getElementById("subcolumn-1");
            cuisine = section.getElementsByTag("h2").text();

            Elements sectionNames = section.getElementsByTag("h3");
            for (Element e : sectionNames) {
                tags.add(e.text());
            }

            Elements sectionRecipes = section.select("div.accordion");
            int i = 0;
            for (Element e : sectionRecipes) {
                String currentTag = tags.get(i);
                Elements recipes = e.select("ul li h4 a");
                for (Element recipe : recipes) {
                    String link = "http://bbc.co.uk" + recipe.attr("href");
                    String name = recipe.text();
                    List<String> ingredients = new LinkedList<>();
                    for (Ingredient ingredient : ingredientExtractor.extractIngredientsFromURL(link)) {
                        ingredients.add(ingredient.getStem());
                    }
                    Recipe r = new Recipe(name, Arrays.asList(cuisine, currentTag), ingredients, link);
                    recipeList.add(r);
                }
                ++i;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recipeList;
    }
}