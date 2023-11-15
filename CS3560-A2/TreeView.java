import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

// Manages the tree display of users and groups
public class TreeView  {

    // Initizlation
    private Map<String, EntryText> entries;
    private JTree tree;
    private DefaultMutableTreeNode root;

    public TreeView() {
        this.entries = new HashMap<>();
        this.root = new DefaultMutableTreeNode("Root");
        this.tree = new JTree(root);
        this.tree.setCellRenderer(new CustomTreeCellRenderer());
        this.tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                doMouseClicked(me);
            }
        });
    }

    // Gets user based on node in the tree
    public User getSelectedUser() {
        TreePath selectedPath = tree.getSelectionPath();
        if (selectedPath != null) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
            if (selectedNode.getUserObject() instanceof User) {
                return (User) selectedNode.getUserObject();
            }
        }
        return null; 
    }
    

    // For when mouse clicks happen on the tree nodes
    private void doMouseClicked(MouseEvent me) {
        TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
        if (tp != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
            if (node.getUserObject() instanceof EntryText) {
                EntryText entry = (EntryText) node.getUserObject();
                if (entry instanceof User) {
                    openUserView((User) entry);
                }
            }
        }
    }

    // Method to add new entry to the tree
    public void addEntry(EntryText entry) {
        if (entry == null || entries.containsKey(entry.getDisplayName())) {
            return;
        }
        entries.put(entry.getDisplayName(), entry);
        insertNodeIntoTree(entry);
    }

    // Inserts new node in the tree
    private void insertNodeIntoTree(EntryText entry) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(entry);
        DefaultMutableTreeNode parentNode = findParentNodeForEntry(entry);
        parentNode.add(node);
        ((DefaultTreeModel) tree.getModel()).reload(parentNode);
    }

    // Method to find the parent node
    private DefaultMutableTreeNode findParentNodeForEntry(EntryText entry) {
        return root;
    }

    // Gets the tree 
    public JTree getTree() {
        return tree;
    }

    // Open the view of the user based on name
    public void openUserView(User user) {
        UserView userWindow = new UserView(user);
        userWindow.setVisible(true);
    }

    // Method to find user based on name
    public User findUser(String username) {
        EntryText entry = entries.get(username);
        return (entry instanceof User) ? (User) entry : null;
    }

    // Count of user instances
    public int getTotalUsers() {
        return (int) entries.values().stream().filter(e -> e instanceof User).count();
    }

    // Count of group instances
    public int getTotalGroups() {
        return (int) entries.values().stream().filter(e -> e instanceof Group).count();
    }
}
