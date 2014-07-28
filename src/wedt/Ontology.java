package wedt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import wedt.utils.Recipe;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.ResourceUtils;

public class Ontology {
    private Model model;

    private Property urlProperty;
    private Property tagProperty;
    private Property ingredientProperty;
    private Property decribeProperty;
    private Property nameProperty;
    private Property nameIngredientProperty;
    private Property nameTagProperty;
    private Property recipeProperty;

    String tagURIprefix = "http://tag/";
    String ingredientURIprefix = "http://ingredient/";
    String recipeURIprefix = "http://recipe/";
    String foodURI = "http://food";

    public Ontology() {
        // prepare ontology
        model = ModelFactory.createDefaultModel();
        createProperties();

    }

    /** KREOWANIE ONTOLOGII Z BAZY */

    public void createProperties() {
        urlProperty = model.createProperty(foodURI, "/url");
        tagProperty = model.createProperty(foodURI, "/tag");
        ingredientProperty = model.createProperty(foodURI, "/contain");
        decribeProperty = model.createProperty(foodURI, "/describe");
        nameProperty = model.createProperty(foodURI, "/name-recipe");
        nameIngredientProperty = model.createProperty(foodURI,
                "/name-ingredient");
        nameTagProperty = model.createProperty(foodURI, "/name-tag");
        recipeProperty = model.createProperty(foodURI, "/recipe");
    }

    public void setRecipes(List<Recipe> recipes, String tag) {
        Resource rtag;
        if (tag.equals("")) {
            rtag = model.createResource(tagURIprefix + "None");
        } else {
            rtag = model.createResource(tagURIprefix + tag);
        }
        rtag.addProperty(nameTagProperty, tag);

        Resource rrecipe;
        for (Recipe recipe : recipes) {
            rrecipe = model.createResource(recipeURIprefix
                    + recipe.getRecipeName());
            rrecipe.addProperty(tagProperty, rtag);
            rrecipe.addProperty(nameProperty, recipe.getRecipeName());
            rrecipe.addProperty(urlProperty, recipe.getUrl());
            rtag.addProperty(recipeProperty, rrecipe);
            rtag.addProperty(decribeProperty, rrecipe);
            setIngreToRecipe(recipe.getIngredients(), recipe.getRecipeName());

        }

    }

    public void setRecipe(Recipe recipe) {
        Resource rrecipe = model.createResource(recipeURIprefix
                + recipe.getRecipeName());

        Resource rtag = null;

        if (recipe.getTags().isEmpty()) {

            rtag = model.getResource(tagURIprefix + "None");
            if (rtag == null) {
                rtag = model.createResource(tagURIprefix + "None");
            }
        }

        for (String tag : recipe.getTags()) {
            
            rtag = model.getResource(tagURIprefix + tag);
            if (rtag == null) {
                
                rtag = model.createResource(tagURIprefix + tag);
            }
            rtag.addProperty(nameTagProperty, tag);
            rrecipe.addProperty(tagProperty, rtag);
        }
        rrecipe.addProperty(tagProperty, rtag);
        rrecipe.addProperty(urlProperty, recipe.getUrl());
        rtag.addProperty(recipeProperty, rrecipe);
        rrecipe.addProperty(nameProperty, recipe.getRecipeName());
        rrecipe.addProperty(urlProperty, recipe.getUrl());
        rtag.addProperty(decribeProperty, rrecipe);

        setIngreToRecipe(recipe.getIngredients(), recipe.getRecipeName());

    }

    public void setTag(String tag) {
        Resource rtag = model.createResource(tagURIprefix + tag);
        if (rtag == null) {
            rtag = model.createResource(tagURIprefix + tag);
            rtag.addProperty(nameTagProperty, tag);
        }
    }

    public void setIngreToRecipe(Collection<String> collection, String recipe) {
        Resource rrecipe = model.getResource(recipeURIprefix + recipe);

        Resource ingredientResource;
        String i;
        for (String ingredient : collection) {
            i = ingredient.replace("[", "").replace("]", "");

            ingredientResource = model.createResource(ingredientURIprefix + i);
            ingredientResource.addProperty(nameIngredientProperty, i);

            ingredientResource.addProperty(recipeProperty, rrecipe);
            rrecipe.addProperty(ingredientProperty, ingredientResource);
        }

    }

    /** PRZESZUKIWANIE ONTOLOGII */

    public List<Recipe> getAllRecipes() {

        NodeIterator recipes = model.listObjectsOfProperty(nameProperty);
        List<Recipe> recipesList = new ArrayList<Recipe>();
        while (recipes.hasNext()) {
            recipesList.add(findRecipeByString(recipes.nextNode().toString()));
        }

        return recipesList;

    }

    public Recipe findRecipeByString(String recipe) {

        Resource recipeResource = model.getResource(recipeURIprefix + recipe);

        if (recipeResource == null)
            return null;

        return findRecipeByResource(recipeResource);

    }

    private Recipe findRecipeByResource(Resource recipe) {
        List<String> tagsList = new ArrayList<String>();
        List<String> ingredientsList = new ArrayList<String>();

        String name = recipe.getProperty(nameProperty).getString();
        String url = recipe.getProperty(urlProperty).getString();
        StmtIterator ingredients = recipe.listProperties(ingredientProperty);
        while (ingredients.hasNext()) {
            ingredientsList.add(ingredients.nextStatement().getResource()
                    .getProperty(nameIngredientProperty).getString());
        }

        StmtIterator tags = recipe.listProperties(tagProperty);
        while (tags.hasNext()) {
            Statement a = tags.nextStatement().getResource()
                    .getProperty(nameTagProperty);
            if (a != null) {
                tagsList.add(a.getString());
            }
        }
        Recipe r = new Recipe(name, tagsList, ingredientsList, url);
        return r;
    }

    public List<Recipe> findRecipeByTag(String tag) {
        final Resource rtag = model.getResource(tagURIprefix + tag);

        StmtIterator recipes = rtag.listProperties(decribeProperty);

        Resource recipe;
        List<Recipe> recipesList = new ArrayList<Recipe>();

        while (recipes.hasNext()) {

            recipe = recipes.nextStatement().getResource();
            recipesList.add(findRecipeByResource(recipe));
        }
        return recipesList;
    }

    public List<Recipe> findRecipeByIngredients(String ingredient) {
        final Resource ingredientResource = model
                .getResource(ingredientURIprefix + ingredient);
        Resource recipe;
        List<Recipe> recipesList = new ArrayList<Recipe>();

        if (model.contains(ingredientResource, nameIngredientProperty,
                ingredient)) {
            StmtIterator recipes = ingredientResource
                    .listProperties(recipeProperty);

            while (recipes.hasNext()) {

                recipe = recipes.nextStatement().getResource();
                recipesList.add(findRecipeByResource(recipe));
            }
        }
        return recipesList;
    }

    public List<Recipe> findRecipeByTagsIngredients(String tag,
            String ingredient) {
        Resource recipe;
        List<Resource> recipesWithTagList = new ArrayList<Resource>();
        List<Resource> recipesWitIngredientsList = new ArrayList<Resource>();
        List<Recipe> recipesList = new ArrayList<Recipe>();

        final Resource tagResource = model.getResource(tagURIprefix + tag);
        final Resource ingredientResource = model
                .getResource(ingredientURIprefix + ingredient);

        StmtIterator recipesWithTag = tagResource
                .listProperties(decribeProperty);
        StmtIterator recipesWithIngredients = ingredientResource
                .listProperties(recipeProperty);

        /* lista zasobow przepisow z danymi tagami */
        while (recipesWithTag.hasNext()) {
            recipesWithTagList
                    .add(recipesWithTag.nextStatement().getResource());

        }

        /* lista zasobow przepisow z danymi ingredientami */
        while (recipesWithIngredients.hasNext()) {
            recipesWitIngredientsList.add(recipesWithIngredients
                    .nextStatement().getResource());
        }

        /* iloczyn obu parametrow */
        if (!recipesWithTagList.isEmpty()
                && !recipesWitIngredientsList.isEmpty()) {
            for (Resource recipeT : recipesWithTagList) {
                for (Resource recipeI : recipesWitIngredientsList) {
                    if (recipeI
                            .getProperty(nameProperty)
                            .getString()
                            .equals(recipeT.getProperty(nameProperty)
                                    .getString())) {
                        recipesList.add(findRecipeByResource(recipeI));
                    }

                }
            }
        }

        return recipesList;
    }

    public List<Recipe> findRecipes(List<String> tags,
            List<String> ingredients, String text) {
        List<Recipe> recipeList = new ArrayList<Recipe>();
        if (text.equals("")) {
            if (!"".equals(text)) {
                recipeList.add(findRecipeByString(text));
            } else if (tags != null && ingredients != null) {

                for (String tag : tags) {
                    for (String ingredient : ingredients) {
                        List<Recipe> recipes = findRecipeByTagsIngredients(tag,
                                ingredient);
                        if (recipes.isEmpty()) {
                            recipeList.clear();
                            break;
                        } else {
                            recipeList.addAll(findRecipeByTagsIngredients(tag,
                                    ingredient));
                        }

                    }

                }
            } else if (tags != null) {
                for (String tag : tags) {
                    List<Recipe> recipes = findRecipeByTag(tag);
                    if (recipes.isEmpty()) {
                        recipeList.clear();
                        break;
                    } else {
                        recipeList.addAll(recipes);
                    }

                }
            } else if (ingredients != null) {
                for (String ingredient : ingredients) {
                    List<Recipe> recipes = findRecipeByIngredients(ingredient);
                    if (recipes.isEmpty()) {
                        recipeList.clear();
                        break;
                    } else {
                        recipeList.addAll(recipes);
                    }
                }
            } else {
                recipeList.addAll(getAllRecipes());
            }
        }
        HashSet<Recipe> hs = new HashSet<Recipe>();
        hs.addAll(recipeList);
        recipeList.clear();
        recipeList.addAll(hs);
        return recipeList; // To change body of created methods use File |
                           // Settings | File Templates.
    }

    public void editRecipe(Recipe oldRecipe, Recipe recipe) {
        removeRecipe(oldRecipe);
        setRecipe(recipe);
    }

    public void removeRecipe(Recipe recipe) {
        Model model_old = model;
        NodeIterator tags = model.listObjectsOfProperty(nameTagProperty);

        HashSet<String> tagList = new HashSet<String>();
        while (tags.hasNext()) {
            String tag = tags.nextNode().toString();
            tagList.add(tag);
        }
        List<Recipe> recipes = getAllRecipes();
        recipes.remove(recipe);

        List<Recipe> recipesByTag = new ArrayList<Recipe>();
        
        
        model = ModelFactory.createDefaultModel();
        createProperties();
        

        for (String tag : tagList) {
            for (Recipe tagInRecipe : recipes) {

                if (tagInRecipe.getTags().contains(tag)) {
                    recipesByTag.add(tagInRecipe);
                }
            }
            if (!recipesByTag.isEmpty()) {
                setRecipes(recipesByTag, tag);
                recipesByTag.clear();
            }
        }
    }

    /** OPERACJE NA PLIKACH */
    public boolean readFile(String inputFileName) {

        InputStream in = FileManager.get().open(inputFileName + ".rdf");
        if (in == null) {
            return false;
        }

        // read the RDF/XML file
        model.read(in, "RDF/XML");
        return true;
    }

    public void writeFile(String inputFileName) {

        try {
            File file = new File(inputFileName + ".rdf");
            model.write(new FileOutputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void print() {
        model.write(System.out, "Turtle");
    }

}