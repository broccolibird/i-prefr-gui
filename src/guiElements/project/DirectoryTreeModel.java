package guiElements.project;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class DirectoryTreeModel extends DefaultTreeModel {  
        
        private DefaultMutableTreeNode root;  
          
        public DirectoryTreeModel(File root) {  
            super( new DefaultMutableTreeNode( root ) );  
            this.root = (DefaultMutableTreeNode)getRoot();  
            File[] f = File.listRoots();  
            for ( int i = 0; i < f.length; i++ ) {  
                DefaultMutableTreeNode fileRoot = new DefaultMutableTreeNode( f[i] );  
                fileRoot.add( new DefaultMutableTreeNode() );  
                this.root.add( fileRoot );  
            }  
        }  
}