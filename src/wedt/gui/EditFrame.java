package wedt.gui;

import wedt.Main;
import wedt.utils.Recipe;

import java.awt.Dialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class EditFrame extends JFrame
{
        private Recipe oldRecipe;
	//title
	private JLabel titleLabel;
	
	//name and source url
	private JLabel recipeNameLabel;
	private JTextField recipeNameInput;
	private JLabel urlLabel;
	private JTextField urlInput;
	
	//tags, ingredients, and edit
	private JLabel ingredientsLabel;
	private JList<String> ingredientsList;
	private JScrollPane ingredientsListScroll;
	private JTextField ingredientInput;
	private JButton ingredientAddButton;
	private JButton ingredientRemoveButton;
	private JLabel tagsLabel;
	private JList<String> tagsList;
	private JScrollPane tagsListScroll;
	private JTextField tagInput;
	private JButton tagAddButton;
	private JButton tagRemoveButton;
	
	//commit recipe
	private JButton okButton;
	private JButton cancelButton;
	
	private int selectedIndex;
	
	JFrame thisFrame;
		
	public EditFrame(Recipe recipe, final int selectedIndex, final BrowseRecipesPanel panel)
	{
		super("Edit recipe");
		thisFrame = this;
	oldRecipe = recipe;
	this.selectedIndex = selectedIndex;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(0, 0, 1080, 720);
        setVisible(true);
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
		        
    	titleLabel = new JLabel("Edit recipe");
    	
    	recipeNameLabel = new JLabel("Recipe name");
    	recipeNameInput = new JTextField();
    	urlLabel = new JLabel("Recipe link");
    	urlInput = new JTextField();

    	ingredientsLabel = new JLabel("Ingredients");
    	ingredientsList = new JList<String>(new DefaultListModel<String>());
    	ingredientsListScroll = new JScrollPane(ingredientsList);
    	
    	ingredientInput = new JTextField();
    	ingredientAddButton = new JButton("add");
    	ingredientRemoveButton = new JButton("remove");

    	tagsLabel = new JLabel("Tags");
    	tagsList = new JList<String>(new DefaultListModel<String>());
    	tagsListScroll = new JScrollPane(tagsList);
    	
    	tagInput = new JTextField();
    	tagAddButton = new JButton("add");
    	tagRemoveButton = new JButton("remove");
    	
    	okButton = new JButton("add recipe");
    	cancelButton = new JButton("cancel");

    	add(titleLabel);
    	add(recipeNameLabel);
    	add(recipeNameInput);
    	add(urlLabel);
    	add(urlInput);
    	add(ingredientsListScroll);
    	add(ingredientInput);
    	add(ingredientAddButton);
    	add(ingredientRemoveButton);
    	add(tagsListScroll);
    	add(tagInput);
    	add(tagAddButton);
    	add(tagRemoveButton);
    	add(okButton);
    	add(cancelButton);
    	add(tagsLabel);
    	add(ingredientsLabel);
    	
        setLayout(null);
    	
        titleLabel.setBounds(30, 15, 300, 30);
        
    	recipeNameLabel.setBounds(30, 75, 100, 30);
    	recipeNameInput.setBounds(140, 75, 225, 30);
    	urlLabel.setBounds(500, 75, 100, 30);
    	urlInput.setBounds(600, 75, 225, 30);

    	ingredientsLabel.setBounds(30, 150, 180, 30);
    	ingredientsListScroll.setBounds(30, 180, 180, 360);
    	
    	ingredientInput.setBounds(270, 180, 90, 30);
    	ingredientAddButton.setBounds(270, 225, 90, 30);
    	ingredientRemoveButton.setBounds(270, 270, 90, 30);

    	tagsLabel.setBounds(435, 150, 180, 30);
    	tagsListScroll.setBounds(435, 180, 180, 360);
    	
    	tagInput.setBounds(660, 180, 90, 30);
    	tagAddButton.setBounds(660, 225, 90, 30);
    	tagRemoveButton.setBounds(660, 270, 90, 30);
    	
    	okButton.setBounds(30, 600, 120, 30);
    	cancelButton.setBounds(180, 600, 120, 30);
    	
    	JSeparator separator1 = new JSeparator(JSeparator.HORIZONTAL);
    	separator1.setBounds(15, 125, 1000, 10);
    	add(separator1);
    	JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);
    	separator2.setBounds(15, 570, 1000, 10);
    	add(separator2);
    	
        //set title components
    	titleLabel.setFont(new Font("Verdana", Font.BOLD, 20));
    	
    	//name and url
    	urlInput.setEditable(false);
        
        //ingredients
        ingredientsLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        
        //ingredients edit
        ingredientAddButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
            	String keyword = ingredientInput.getText();
            	if (null != keyword)
            	{
            		((DefaultListModel<String>) ingredientsList.getModel()).addElement(keyword);
            	}
            }
        });
        ingredientRemoveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
            	int selectedIndex = ingredientsList.getSelectedIndex();
            	if (-1 != selectedIndex)
            	{
            		((DefaultListModel<String>) ingredientsList.getModel()).remove(selectedIndex);
            	}
            }
        });
        
        //tags
        tagsLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        
        //tags edit
        tagAddButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
            	String keyword = null;
            	if (null != (keyword = tagInput.getText()))
            	{
            		((DefaultListModel<String>) tagsList.getModel()).addElement(keyword);
            	}
            }
        });
        tagRemoveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
            	int selectedIndex = tagsList.getSelectedIndex();
            	if (-1 != selectedIndex)
            	{
            		((DefaultListModel<String>) tagsList.getModel()).remove(selectedIndex);
            	}
            }
        });
        
        //setup commit panel and buttons
        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                
            	ArrayList<String> tags = new ArrayList<String>();
            	ArrayList<String> ingredients = new ArrayList<String>();
            	for (int i = 0; i < tagsList.getModel().getSize(); i++)
            	{
            		tags.add(tagsList.getModel().getElementAt(i));
            	}
            	
            	for (int i = 0; i < ingredientsList.getModel().getSize(); i++)
            	{
            		ingredients.add(ingredientsList.getModel().getElementAt(i));
            	}
            	
            	Recipe recipe = new Recipe(recipeNameInput.getText(),tags, ingredients, urlInput.getText());
            	
            	Main.getDefaultOntology().editRecipe(oldRecipe, recipe);
            	panel.updateSelection(recipe, selectedIndex);
            	thisFrame.dispose();

            }
        });
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
            	thisFrame.dispose();
            }
        });
        
        fillDataFromRecipeObject(recipe);
	}

    private void fillDataFromRecipeObject(Recipe recipe) {
        recipeNameInput.setText(recipe.getRecipeName());
        urlInput.setText(recipe.getUrl());

        ((DefaultListModel<String>) ingredientsList.getModel()).clear();
        for (String ingredient : recipe.getIngredients()) {
            ((DefaultListModel<String>) ingredientsList.getModel()).addElement(ingredient);
        }

        ((DefaultListModel<String>) tagsList.getModel()).clear();
        for (String tag : recipe.getTags()) {
            ((DefaultListModel<String>) tagsList.getModel()).addElement(tag);
        }


    }
}
