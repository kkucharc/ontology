package wedt.utils;

/**
 * Created with IntelliJ IDEA.
 * Date: 31.05.14
 * Time: 17:20
 */
public class ClassifiedIngredient {
    private final String tag;
    private final String ingredient;
    private final Double confidence;

    public ClassifiedIngredient(String tag, String ingredient, Double confidence) {
        this.tag = tag;
        this.ingredient = ingredient;
        this.confidence = confidence;
    }

    public String getTag() {
        return tag;
    }

    public String getIngredient() {
        return ingredient;
    }

    public Double getConfidence() {
        return confidence;
    }
}
