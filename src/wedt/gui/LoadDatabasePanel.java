package wedt.gui;

import wedt.Main;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class LoadDatabasePanel extends JPanel
{
	private JLabel titleLabel;
	
	private JButton openDatabase;
	private JButton openOntologyDatabase;
	private JButton saveOntologyDatabase;
	
	public LoadDatabasePanel(boolean b)
	{
		super(b);
		
		titleLabel = new JLabel("Load recipe data");
			
		openDatabase = new JButton("load database");
		openOntologyDatabase = new JButton("load ontology");
		saveOntologyDatabase = new JButton("save ontology");
		
		
		add(titleLabel);
		add(openDatabase);
		add(openOntologyDatabase);
		add(saveOntologyDatabase);
		
		setLayout(null);
		
		titleLabel.setBounds(30, 15, 240, 30);
		openDatabase.setBounds(90, 300, 150, 30);
		openOntologyDatabase.setBounds(90, 360, 150, 30);
		saveOntologyDatabase.setBounds(90, 420, 150, 30);
		
    	JSeparator separator1 = new JSeparator(JSeparator.HORIZONTAL);
    	separator1.setBounds(15, 125, 1000, 10);
    	add(separator1);
		
		titleLabel.setFont(new Font("Verdana", Font.BOLD, 20));
		
		openDatabase.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
            	Main.createOntologyDB();
            	JOptionPane.showMessageDialog(getParent(), "Database loaded");
            }
        });
		
		openOntologyDatabase.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                String path = JOptionPane.showInputDialog(getParent(), "file path");
                if (!"".equals(path)) {
                    if (path.endsWith(".rdf")) {
                        path = path.substring(0, path.length()-4);
                    }
                    if (Main.createOntologyFile(path)==null){
                        JOptionPane.showMessageDialog(getParent(), "File not found");
                    } else {
                        JOptionPane.showMessageDialog(getParent(), "File loaded");
                    }
                }
            }
        });
		
		
                saveOntologyDatabase.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                String path = JOptionPane.showInputDialog(getParent(), "file path");
                if (!"".equals(path)) {
                    if (path.endsWith(".rdf")) {
                        path = path.substring(0, path.length()-4);
                    }
                    
                    Main.saveOntology(path);
                    
                    
                        JOptionPane.showMessageDialog(getParent(), "File saved");
                    
                    
                }
            }
        });		
	}
}
