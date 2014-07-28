package wedt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Ingredient implements Comparable<Ingredient>
{ 
    private String stem;
    private Integer frequency;
    private Set<String> terms;

    public Ingredient(String stem)
    {
        this.stem = stem;
        terms = new HashSet<String>();
        frequency = 0;
    }
 
    public void add(String term)
    {
        terms.add(term);
        frequency++;
    }
 
    public String getStem()
    {
        return stem;
    }
 
    public Integer getFrequency()
    {
        return frequency;
    }

    public Set<String> getTerms()
    {
        return terms;
    }
 
    @Override
    public int compareTo(Ingredient o)
    {
        return o.frequency.compareTo(frequency);
    }
 
    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof Ingredient && obj.hashCode() == hashCode();
    }
 
    @Override
    public int hashCode()
    {
        return Arrays.hashCode(new Object[] { stem });
    }
 
    @Override
    public String toString()
    {
        return stem + " x" + frequency;
    } 
}