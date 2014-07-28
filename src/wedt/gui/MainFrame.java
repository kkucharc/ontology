package wedt.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class MainFrame extends JFrame
{
 
    public MainFrame()
    {
        super("Baza przepisów");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1080, 720);
        setVisible(true);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        AddRecipePanel addRecipe = new AddRecipePanel(false);
        tabbedPane.addTab("Add recipes", null, addRecipe, null);
        
        BrowseRecipesPanel browsePane = new BrowseRecipesPanel(false);
        tabbedPane.addTab("Browse recipes", null, browsePane, null);
        
        OntologyPanel ontologyPane = new OntologyPanel(false);
        tabbedPane.addTab("Ontology edition", null, ontologyPane, null);
        
        LoadDatabasePanel dataPane = new LoadDatabasePanel(false);
        tabbedPane.addTab("Data", null, dataPane, null);
        
        add(tabbedPane);
    }
}
