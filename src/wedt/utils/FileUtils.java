package wedt.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 02.06.14
 * Time: 22:45
 */
public class FileUtils {
    private static final int RECIPE_NAME_INDEX = 0;
    private static final int TAGS_INDEX = 1;
    private static final int INGREDIENTS_INDEX = 2;
    private static final int URL_INDEX = 3;


    public List<Recipe> readFromReader(BufferedReader reader) throws IOException {
        List<Recipe> result = new LinkedList<>();

        String line = null;

        while ((line = reader.readLine()) != null) {
             result.add(parseRecipe(line));
        }

        return result;
    }

    private Recipe parseRecipe(String line) {
        String[] sections = line.split("\\|");

        String name = sections[RECIPE_NAME_INDEX].trim();
        String[] tagArray = sections[TAGS_INDEX].split(",");
        String[] ingredientArray = sections[INGREDIENTS_INDEX].split(",");
        String url = sections[URL_INDEX].trim();

        List<String> tags = cleanAndTrim(tagArray);
        List<String> ingredients = cleanAndTrim(ingredientArray);

        return new Recipe(name, tags, ingredients, url);
    }

    private List<String> cleanAndTrim(String[] itemArray) {
        List<String> items = new LinkedList<>();
        for (String item : itemArray) {
            item = item.trim();
            if (!"".equals(item)) {
                 items.add(item);
            }
        }
        return items;
    }


}
