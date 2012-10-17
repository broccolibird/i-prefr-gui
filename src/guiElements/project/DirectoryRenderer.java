package guiElements.project;

import java.awt.Component;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class DirectoryRenderer extends DefaultTreeCellRenderer {  
        
		public DirectoryRenderer() {
			super();
			
			// all nodes are folders, make new leaves look like closed folder icon
			leafIcon = closedIcon;
		}
		
        public Component getTreeCellRendererComponent( JTree tree, Object value,  
                boolean sel, boolean expanded, boolean leaf, int row,   
                boolean hasFocus ) {  
           
        	JLabel l = (JLabel)super.getTreeCellRendererComponent( tree, value, sel,  
        								expanded, leaf, row, hasFocus);  
            Object userObject = ((DefaultMutableTreeNode)value).getUserObject(); 
            
            if ( userObject instanceof File ) {  
                String name = ((File)userObject).getName();  
                if ( name.trim().equals( "" ) ) {  
                    l.setText( ((File)userObject).toString() );  
                }  
                else {  
                    l.setText( ((File)userObject).getName() );  
                }  
            }  
            return l;   
        }  
    }