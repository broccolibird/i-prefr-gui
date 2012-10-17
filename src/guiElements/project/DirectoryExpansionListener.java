package guiElements.project;

import java.io.File;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class DirectoryExpansionListener implements TreeWillExpandListener {  
    public void treeWillCollapse( TreeExpansionEvent e ) {  
        Object node = e.getPath().getLastPathComponent();  
        if ( node instanceof DefaultMutableTreeNode ) {  
            ((DefaultMutableTreeNode)node).removeAllChildren();  
            ((DefaultMutableTreeNode)node).add( new DefaultMutableTreeNode() );  
        }  
    }  
      
    public void treeWillExpand( TreeExpansionEvent e ) {  
        Object node = e.getPath().getLastPathComponent();  
        if ( node instanceof DefaultMutableTreeNode ) {  
            Object userObj = ((DefaultMutableTreeNode)node).getUserObject();  
            if ( userObj instanceof File ) {  
                ((DefaultMutableTreeNode)node).removeAllChildren();  
                File f = (File)userObj;  
                try {  
                    File[] childFile = f.listFiles();  
                    for ( int i = 0; i < childFile.length; i++ ) {  
                        if ( childFile[i].isDirectory() ) {  
                            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode( childFile[i] );  
                            childNode.add( new DefaultMutableTreeNode() );  
                            ((DefaultMutableTreeNode)node).add( childNode );  
                        }  
                    }  
                }  
                catch ( Exception x ) {  
                    // Problem reading directory...  
                    x.printStackTrace();  
                }  
            }  
        }  
    }  
}