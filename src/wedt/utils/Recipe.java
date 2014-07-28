package wedt.utils;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * Date: 31.05.14
 * Time: 17:42
 */
public class Recipe {
    private final Collection<String> tags;
    private final Collection<String> ingredients;
    private final String recipeName;
    private final String url;

    public Recipe(String recipeName, Collection<String> tags, Collection<String> ingredients, String url) {
        this.tags = tags;
        this.ingredients = ingredients;
        this.recipeName = recipeName;
        this.url = url;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public Collection<String> getIngredients() {
        return ingredients;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (ingredients != null ? !ingredients.equals(recipe.ingredients) : recipe.ingredients != null) return false;
        if (recipeName != null ? !recipeName.equals(recipe.recipeName) : recipe.recipeName != null) return false;
        if (tags != null ? !tags.equals(recipe.tags) : recipe.tags != null) return false;
        if (url != null ? !url.equals(recipe.url) : recipe.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tags != null ? tags.hashCode() : 0;
        result = 31 * result + (ingredients != null ? ingredients.hashCode() : 0);
        result = 31 * result + (recipeName != null ? recipeName.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
