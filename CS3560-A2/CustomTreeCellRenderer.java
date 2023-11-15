import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTree;

// Goal is to customize the look in the tree view
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {

    // Sets up renderer to make changes to the text, groups will be in bold, users will be not in bold
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean expanded, boolean leaf,
                                                  int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object userObject = node.getUserObject();

        // Checks if it is an instance of Group or User, this allows us to differentiate between users and groups 
        if (userObject instanceof Group) {
            // If group apply the bold font and make text bigger 
            this.setFont(new Font("Dialog", Font.BOLD, 14));
        } else if (userObject instanceof User) {
            // If User name, then keep things simple
            this.setFont(new Font("Dialog", Font.PLAIN, 12));
        }

        return this;
    }
}
