package wedt.gui;

import wedt.Main;
import wedt.utils.Recipe;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class BrowseRecipesPanel extends JPanel {
    private JLabel titleLabel;

    private JLabel filterLabel;

    private JLabel tagsLabel;
    private JTextField tagsInput;

    private JLabel ingredientsLabel;
    private JTextField ingredientsInput;

    private JLabel recipeNameLabel;
    private JTextField recipeNameInput;

    private JButton searchButton;

    private JLabel resultsLabel;
    private JList<String> resultsList;
    private JScrollPane resultsListScroll;
    private JButton editButton;
    private JButton removeButton;
    private List<Recipe> recipeList;
    
    private BrowseRecipesPanel panel;

    public BrowseRecipesPanel(boolean b) {
        super(b);
        
        panel = this;

        titleLabel = new JLabel("Browse recipes");

        filterLabel = new JLabel("Filter by:");

        tagsLabel = new JLabel("Tags");
        tagsInput = new JTextField();

        ingredientsLabel = new JLabel("Ingredients");
        ingredientsInput = new JTextField();

        recipeNameLabel = new JLabel("Recipe name");
        recipeNameInput = new JTextField();

        searchButton = new JButton("search");

        resultsLabel = new JLabel("Results");
        resultsList = new JList<String>(new DefaultListModel<String>());
        resultsListScroll = new JScrollPane(resultsList);

        editButton = new JButton("edit");
        removeButton = new JButton("remove");

        add(titleLabel);
        add(filterLabel);
        add(tagsLabel);
        add(tagsInput);
        add(ingredientsLabel);
        add(ingredientsInput);
        add(recipeNameLabel);
        add(recipeNameInput);
        add(searchButton);
        add(resultsLabel);
        add(resultsListScroll);
        add(editButton);
        add(removeButton);

        setLayout(null);

        titleLabel.setBounds(30, 15, 240, 30);
        filterLabel.setBounds(30, 90, 120, 30);
        tagsLabel.setBounds(30, 135, 120, 30);
        tagsInput.setBounds(30, 180, 180, 30);
        ingredientsLabel.setBounds(30, 255, 120, 30);
        ingredientsInput.setBounds(30, 300, 180, 30);
        recipeNameLabel.setBounds(30, 375, 120, 30);
        recipeNameInput.setBounds(30, 410, 180, 30);
        searchButton.setBounds(30, 455, 90, 30);
        resultsLabel.setBounds(400, 90, 120, 30);
        resultsListScroll.setBounds(400, 150, 550, 450);
        editButton.setBounds(400, 615, 90, 30);
        removeButton.setBounds(500, 615, 90, 30);

        JSeparator separator1 = new JSeparator(JSeparator.VERTICAL);
        separator1.setBounds(350, 90, 10, 540);
        add(separator1);

        titleLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        filterLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        resultsLabel.setFont(new Font("Verdana", Font.BOLD, 16));

        this.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                if (Main.getDefaultOntology() != null)
                    setResults(Main.getDefaultOntology().getAllRecipes());
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String t = tagsInput.getText();
                List<String> tags = "".equals(t) ? null : Arrays.asList(t
                        .split(","));
                String i = ingredientsInput.getText();
                List<String> ingredients = "".equals(i) ? null : Arrays
                        .asList(i.split(","));
                setResults(Main.getDefaultOntology().findRecipes(tags,
                        ingredients, recipeNameInput.getText()));
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                EditFrame edit = new EditFrame(recipeList.get(resultsList
                        .getSelectedIndex()), resultsList.getSelectedIndex(), panel);
            }
        });

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selectedIndex = resultsList.getSelectedIndex();
                if (-1 != selectedIndex) {
                	Recipe recipe = recipeList.get(selectedIndex);
                	((DefaultListModel<String>) resultsList.getModel()).remove(selectedIndex);
                    Main.getDefaultOntology().removeRecipe(recipe);
                }
            }
        });
    }

    private void setResults(List<Recipe> recipes) {
        this.recipeList = recipes;
        ((DefaultListModel<String>) resultsList.getModel()).clear();
        if (recipeList != null) {
            if (!recipeList.isEmpty()){
                for (Recipe recipe : recipes) {
                    ((DefaultListModel<String>) resultsList.getModel())
                            .addElement(recipeToString(recipe));
                }
            }
        }
    }
    
    public void updateSelection(Recipe recipe, int position) {
        String recipeString = recipeToString(recipe);
        ((DefaultListModel<String>) resultsList.getModel()).set(position, recipeString);
    }

    private String recipeToString(Recipe recipe) {
        StringBuffer sb = new StringBuffer();
        sb.append(recipe.getRecipeName()).append(" (").append(recipe.getUrl())
                .append(") [ ");
        for (String tag : recipe.getTags()) {
            sb.append(tag).append(" ");
        }
        sb.append("]: ");
        for (String ingredient : recipe.getIngredients()) {
            sb.append(ingredient).append(",");
        }
        return sb.toString();
    }
}
