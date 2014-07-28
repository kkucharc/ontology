package wedt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wedt.gui.MainFrame;
import wedt.utils.BbcRecipeReader;
import wedt.utils.Classification;
import wedt.utils.ConnectionUtils;
import wedt.utils.Recipe;

public class Main {

    private static Ontology defaultOntology;
    private static Map<String, Ontology> ontologyCollection;

    public static void main(String[] args) {
        try {
            initDb();
            ontologyCollection = new HashMap<String, Ontology>();
            setDefaultOntology(new Ontology());
            MainFrame mainFrame = new MainFrame();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void createOntology() {
        defaultOntology = new Ontology();
        // ontology.readFile("ontologia");
        readDB(defaultOntology);

        defaultOntology.getAllRecipes();
        // ont.createReasoner();
        defaultOntology.findRecipes(null, null,
                "Turkish and Middle Eastern recipes");

        defaultOntology.findRecipeByString("Scandinavian rye bread");

        defaultOntology.removeRecipe(new Recipe("Banana tart tatin", null,
                null, null));
        // ontology.writeFile("ontologia");

    }

    /**
     * Reads database and fullfill ontology
     * 
     * @param ontology
     */
    public static void readDB(Ontology ontology) {
        for (String tag : getTags()) {
            ontology.setRecipes(getRecipes(tag), tag);
        }

    }

    private static void initDb() throws Exception {
        Class.forName("org.h2.Driver");
        if (!hasData()) {
            createTable();
            loadData();
        }
    }

    private static void loadData() throws SQLException {
        Classification classification = new Classification();
        BbcRecipeReader recipeReader = new BbcRecipeReader();
        classification.updateClassification(recipeReader
                .extractFromSite("http://www.bbc.co.uk/food/cuisines/african"));
        classification
                .updateClassification(recipeReader
                        .extractFromSite("http://www.bbc.co.uk/food/cuisines/american"));
        classification.updateClassification(recipeReader
                .extractFromSite("http://www.bbc.co.uk/food/cuisines/british"));
        classification
                .updateClassification(recipeReader
                        .extractFromSite("http://www.bbc.co.uk/food/cuisines/caribbean"));
        classification.updateClassification(recipeReader
                .extractFromSite("http://www.bbc.co.uk/food/cuisines/chinese"));
        classification
                .updateClassification(recipeReader
                        .extractFromSite("http://www.bbc.co.uk/food/cuisines/east_european"));
        classification.updateClassification(recipeReader
                .extractFromSite("http://www.bbc.co.uk/food/cuisines/french"));
        classification.updateClassification(recipeReader
                .extractFromSite("http://www.bbc.co.uk/food/cuisines/greek"));
        classification.updateClassification(recipeReader
                .extractFromSite("http://www.bbc.co.uk/food/cuisines/indian"));
        classification.updateClassification(recipeReader
                .extractFromSite("http://www.bbc.co.uk/food/cuisines/irish"));
        classification.updateClassification(recipeReader
                .extractFromSite("http://www.bbc.co.uk/food/cuisines/italian"));
        classification
                .updateClassification(recipeReader
                        .extractFromSite("http://www.bbc.co.uk/food/cuisines/japanese"));
        classification.updateClassification(recipeReader
                .extractFromSite("http://www.bbc.co.uk/food/cuisines/mexican"));
        classification.updateClassification(recipeReader
                .extractFromSite("http://www.bbc.co.uk/food/cuisines/nordic"));
        classification
                .updateClassification(recipeReader
                        .extractFromSite("http://www.bbc.co.uk/food/cuisines/north_african"));
        classification
                .updateClassification(recipeReader
                        .extractFromSite("http://www.bbc.co.uk/food/cuisines/portuguese"));
        classification
                .updateClassification(recipeReader
                        .extractFromSite("http://www.bbc.co.uk/food/cuisines/south_american"));
        classification.updateClassification(recipeReader
                .extractFromSite("http://www.bbc.co.uk/food/cuisines/spanish"));
        classification
                .updateClassification(recipeReader
                        .extractFromSite("http://www.bbc.co.uk/food/cuisines/thai_and_south-east_asian"));
        classification
                .updateClassification(recipeReader
                        .extractFromSite("http://www.bbc.co.uk/food/cuisines/turkish_and_middle_eastern"));
    }

    private static void createTable() throws SQLException {
        Connection connection = ConnectionUtils.getConnection();
        connection.prepareCall(
                "CREATE TABLE IF NOT EXISTS TAGGED_INGREDIENTS ("
                        + "TAG VARCHAR(255)," + "INGREDIENT VARCHAR(255),"
                        + "RECIPE_NAME VARCHAR(255),"
                        + "RECIPE_URL VARCHAR(255)," + "CONFIDENCE NUMBER"
                        + ")").execute();
    }

    private static boolean hasData() throws SQLException {
        ResultSet resultSet = null;
        try {
            Connection connection = ConnectionUtils.getConnection();
            resultSet = connection.prepareStatement(
                    "SELECT COUNT(*) FROM TAGGED_INGREDIENTS").executeQuery();
        } catch (SQLException e) {
            return false;
        }
        resultSet.next();
        return resultSet.getInt(1) > 0;
    }

    private static List<String> getTags() {
        List<String> tags = new ArrayList<String>();
        ResultSet resultSet = null;
        try {
            Connection connection = ConnectionUtils.getConnection();
            resultSet = connection.prepareStatement(
                    "SELECT DISTINCT TAG FROM TAGGED_INGREDIENTS")
                    .executeQuery();
        } catch (SQLException e) {
            System.err.println("Nie wyciagnalem danych z bazy");
        }

        try {
            while (resultSet.next()) {
                tags.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tags;
    }

    private static List<String> getIngredients() {
        List<String> ingredients = new ArrayList<String>();
        ResultSet resultSet = null;
        try {
            Connection connection = ConnectionUtils.getConnection();
            PreparedStatement ps = connection
                    .prepareStatement("SELECT INGREDIENT FROM TAGGED_INGREDIENTS");
            resultSet = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Nie wyciagnalem danych z bazy");
            return null;
        }

        if (!resultSet.equals(null)) {
            try {
                while (resultSet.next()) {
                    ingredients.add(resultSet.getString(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ingredients;
    }

    private static List<String> getIngredients(String tag) {
        List<String> ingredients = new ArrayList<String>();
        ResultSet resultSet = null;
        try {
            Connection connection = ConnectionUtils.getConnection();
            PreparedStatement ps = connection
                    .prepareStatement("SELECT INGREDIENT FROM TAGGED_INGREDIENTS WHERE TAG = ?");
            ps.setString(1, tag);
            resultSet = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Nie wyciagnalem danych z bazy");
            return null;
        }

        if (!resultSet.equals(null)) {
            try {
                while (resultSet.next()) {
                    ingredients.add(resultSet.getString(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ingredients;
    }

    private static List<Recipe> getRecipes(String tag) {
        List<Recipe> recipe = new ArrayList<Recipe>();
        ResultSet resultSet = null;
        Connection connection;
        try {
            connection = ConnectionUtils.getConnection();
            PreparedStatement ps = connection
                    .prepareStatement("SELECT RECIPE_NAME, RECIPE_URL FROM TAGGED_INGREDIENTS WHERE TAG = ?");
            ps.setString(1, tag);
            resultSet = ps.executeQuery();

            if (!resultSet.equals(null)) {
                try {
                    while (resultSet.next()) {
                        ResultSet resultSet2 = null;
                        Collection<String> ingred = new ArrayList<String>();
                        try {
                            PreparedStatement ps2 = connection
                                    .prepareStatement("SELECT INGREDIENT FROM TAGGED_INGREDIENTS WHERE RECIPE_NAME = ?");
                            ps2.setString(1, resultSet.getString(1));
                            resultSet2 = ps2.executeQuery();

                            if (!resultSet2.equals(null)) {
                                try {
                                    while (resultSet2.next()) {
                                        ingred.add(resultSet2.getString(1));
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (SQLException e) {
                            System.err.println("Nie wyciagnalem danych z bazy");
                            return null;
                        }

                        recipe.add(new Recipe(resultSet.getString(1), null,
                                ingred, resultSet.getString(2)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } catch (SQLException e) {
            System.err.println("Nie wyciagnalem danych z bazy");
            return null;
        }

        return recipe;
    }

    public static Ontology getDefaultOntology() {
        return defaultOntology;
    }

    public static void setDefaultOntology(String ontology) {
        if (ontology == null || ontology.equals(""))
            defaultOntology = null;
        else
            defaultOntology = ontologyCollection.get(ontology);
    }

    public static void setDefaultOntology(Ontology ontology) {
        defaultOntology = ontology;
    }

    public static Map<String, Ontology> getOntologyCollection() {
        return ontologyCollection;
    }

    public static void addOntology(String ontologyName, Ontology ontology) {
        ontologyCollection.put(ontologyName, ontology);

    }

    /**
     * Creating ontology from file
     * 
     * @param path
     * @return ontology
     */
    public static Ontology createOntologyFile(String path) {
        Ontology ontology = new Ontology();
        ontology.readFile(path);
        addOntology(path, ontology);
        setDefaultOntology(ontology);
        return ontology;
    }

    /**
     * Creating ontology from database
     * 
     * @param path
     * @return
     */
    public static Ontology createOntologyDB() {
        Ontology ontology = new Ontology();
        readDB(ontology);
        addOntology("data base ontology", ontology);
        setDefaultOntology(ontology);
        return ontology;
    }

    public static void saveOntology(String path) {
        defaultOntology.writeFile(path);
        Ontology ontology = defaultOntology;
        addOntology(path, ontology);
        setDefaultOntology(ontology);
    }

    public static void removeOntology(String ontology) {
        ontologyCollection.remove(ontology);
        setDefaultOntology("");
    }
}
