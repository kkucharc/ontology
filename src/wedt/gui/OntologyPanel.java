package wedt.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import wedt.Main;

@SuppressWarnings("serial")
public class OntologyPanel extends JPanel
{
	//title
	private JLabel titleLabel;
		
	//tags, ontologies, and edit
	private JLabel ontologiesLabel;
	private JList<String> ontologiesList;
	private JScrollPane ontologiesListScroll;
	private JButton ontologieLoadButton;
	private JButton ontologieAddButton;
	private JButton ontologieRemoveButton;
		
	public OntologyPanel(boolean b)
	{
		super(b);
		        
    	titleLabel = new JLabel("Edit ontologies");
    	
    	ontologiesLabel = new JLabel("Ontologies");
    	ontologiesList = new JList<String>(new DefaultListModel<String>());
    	ontologiesListScroll = new JScrollPane(ontologiesList);
    	
    	ontologieLoadButton = new JButton("load");
    	ontologieAddButton = new JButton("select");
    	ontologieRemoveButton = new JButton("remove");

    	add(titleLabel);
    	add(ontologiesListScroll);
    	add(ontologieLoadButton);
    	add(ontologieAddButton);
    	add(ontologieRemoveButton);
    	add(ontologiesLabel);
    	
        setLayout(null);
    	
        titleLabel.setBounds(30, 15, 300, 30);
        
    	ontologiesLabel.setBounds(30, 150, 180, 30);
    	ontologiesListScroll.setBounds(30, 180, 180, 360);
    	
    	ontologieLoadButton.setBounds(270, 180, 90, 30);
    	ontologieAddButton.setBounds(270, 225, 90, 30);
    	ontologieRemoveButton.setBounds(270, 270, 90, 30);

    	JSeparator separator1 = new JSeparator(JSeparator.HORIZONTAL);
    	separator1.setBounds(15, 125, 1000, 10);
    	add(separator1);
   	
    	
    	titleLabel.setFont(new Font("Verdana", Font.BOLD, 20));
                
        ontologiesLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        
        
        this.addComponentListener(new ComponentAdapter() 
        {
            public void componentShown( ComponentEvent e ){
            	((DefaultListModel<String>) ontologiesList.getModel()).clear();
                for (String ontology : Main.getOntologyCollection().keySet()) {
                    ((DefaultListModel<String>) ontologiesList.getModel()).addElement(ontology);
                }
            }
        });
        
        ontologieAddButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
            	Main.setDefaultOntology(ontologiesList.getSelectedValue());
            }
        });
        
        
        ontologieRemoveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
            	int selectedIndex = ontologiesList.getSelectedIndex();
            	String selectedKey = ontologiesList.getSelectedValue();
            	if (-1 != selectedIndex)
            	{
        	        Main.removeOntology(selectedKey);
            		((DefaultListModel<String>) ontologiesList.getModel()).remove(selectedIndex);
            	}
            }
        });
	}
}