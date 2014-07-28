package wedt.utils;

import wedt.IngredientExtractor;

import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 31.05.14
 * Time: 17:17
 */
public class Classification {

    private FileUtils fileUtils;

    public Classification() {
        this.fileUtils = new FileUtils();
    }

    public void readFromFile(String filename) {
        try (FileReader fileReader = new FileReader(filename)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<Recipe> recipes = fileUtils.readFromReader(bufferedReader);
            updateClassification(recipes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ClassifiedIngredient> getCharacteristicIngredients(String tag) {
        List<ClassifiedIngredient> result = new LinkedList<>();

        try (Connection connection = ConnectionUtils.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement("SELECT " +
                    "TAG, INGREDIENT, CONFIDENCE " +
                    "FROM TAGGED_INGREDIENTS " +
                    "WHERE TAG = ? " +
                    "ORDER BY CONFIDENCE DESC");
            statement.setString(1, tag);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ClassifiedIngredient item = new ClassifiedIngredient(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getDouble(3)
                );
                result.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updateClassification(List<Recipe> data) {
        if (data != null && !data.isEmpty()) {
            insertData(data);
            updateConfidenceValues();
        }

    }

    private void updateConfidenceValues() {
        try (Connection connection = ConnectionUtils.getConnection()) {
            //plz test me
            CallableStatement statement = connection.prepareCall("" +
                    "UPDATE TAGGED_INGREDIENTS TI " +
                    "SET CONFIDENCE = 0");
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertData(List<Recipe> data) {
        try (Connection connection = ConnectionUtils.getConnection()) {
            for (Recipe item : data) {
                for (String tag : item.getTags()) {
                    for (String ingredient : item.getIngredients()) {
                        CallableStatement statement = connection.prepareCall("INSERT INTO TAGGED_INGREDIENTS" +
                                "(TAG, INGREDIENT, RECIPE_NAME, RECIPE_URL)" +
                                " VALUES(?,?,?,?)");
                        statement.setString(1, tag);
                        statement.setString(2, ingredient);
                        statement.setString(3, item.getRecipeName());
                        statement.setString(4, item.getUrl());
                        statement.execute();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
