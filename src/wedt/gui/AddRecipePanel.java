package wedt.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import wedt.IngredientExtractor;
import wedt.Ingredient;
import wedt.Main;
import wedt.utils.Recipe;

@SuppressWarnings("serial")
public class AddRecipePanel extends JPanel
{
	//title
	private JLabel titleLabel;
	
	//fetch url
	private JLabel urlInputLabel;
	private JTextField urlInput;
	private JButton urlFetchButton;
	
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
	private JLabel recpieNameLabel;
	private JTextField recipeNameInput;
	
	//commit recipe
	private JButton commitRecipeButton;
	private JButton cancelRecipeButton;
	
		
	public AddRecipePanel(boolean b)
	{
		super(b);
		        
    	titleLabel = new JLabel("Get recipe from URL");
    	
    	urlInputLabel = new JLabel("URL:");
    	urlInput = new JTextField();
    	urlFetchButton = new JButton("get recipe");

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
    	
    	recpieNameLabel = new JLabel("Recipe name");
    	recipeNameInput = new JTextField();
    	
    	commitRecipeButton = new JButton("add recipe");
    	cancelRecipeButton = new JButton("cancel");

    	add(titleLabel);
    	add(urlInputLabel);
    	add(urlInput);
    	add(urlFetchButton);
    	add(ingredientsListScroll);
    	add(ingredientInput);
    	add(ingredientAddButton);
    	add(ingredientRemoveButton);
    	add(tagsListScroll);
    	add(tagInput);
    	add(tagAddButton);
    	add(tagRemoveButton);
    	add(commitRecipeButton);
    	add(cancelRecipeButton);
    	add(recpieNameLabel);
    	add(recipeNameInput);
    	add(tagsLabel);
    	add(ingredientsLabel);
    	
        setLayout(null);
    	
        titleLabel.setBounds(30, 15, 300, 30);
        
    	urlInputLabel.setBounds(30, 75, 60, 30);
    	urlInput.setBounds(75, 75, 225, 30);
    	urlFetchButton.setBounds(300, 75, 120, 30);

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

    	recpieNameLabel.setBounds(805, 305, 180, 30);
    	recipeNameInput.setBounds(805, 340, 180, 30);
    	
    	commitRecipeButton.setBounds(30, 600, 120, 30);
    	cancelRecipeButton.setBounds(180, 600, 120, 30);
    	
    	JSeparator separator1 = new JSeparator(JSeparator.HORIZONTAL);
    	separator1.setBounds(15, 125, 1000, 10);
    	add(separator1);
    	JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);
    	separator2.setBounds(15, 570, 1000, 10);
    	add(separator2);
    	
        //set title components
    	titleLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        
        //set fetch url components
    	
        urlFetchButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
            	List<Ingredient> keywords = null;
				try
				{
					keywords = IngredientExtractor.extractIngredientsFromURL(urlInput.getText());
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
		        ((DefaultListModel<String>) ingredientsList.getModel()).clear();
				for (Ingredient k : keywords)
				{
			        ((DefaultListModel<String>) ingredientsList.getModel()).addElement(k.getStem());
				}				
				
				//TODO get tags and fill tags list
            }
        });
        
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
        commitRecipeButton.addActionListener(new ActionListener()
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
                Recipe recipe = new Recipe(recipeNameInput.getText(), tags, ingredients, urlInput.getText());
                Main.getDefaultOntology().setRecipe(recipe);
            }
        });
        cancelRecipeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
            	((DefaultListModel<String>) ingredientsList.getModel()).clear();
            	((DefaultListModel<String>) tagsList.getModel()).clear();
            	tagInput.setText("");
            	ingredientInput.setText("");
            	urlInput.setText("");
            }
        });
	}
}