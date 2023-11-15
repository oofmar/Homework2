import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminControlPanel implements ActionListener {
    // Singleton instance
    private static final AdminControlPanel INSTANCE = new AdminControlPanel();

    // User interface components
    private JFrame frame;
    private JTextField userIDField;
    private JTextField groupIDField;
    private JButton addUserButton;
    private JButton addGroupButton;
    private JButton openUserViewButton;
    private JButton showUserTotalButton;
    private JButton showGroupTotalButton;
    private JButton showMessagesTotalButton;
    private JButton showPositivePercentageButton;
    private TreeView treeView;

    // Constructor for singleton pattern
    private AdminControlPanel() {
        initializeComponents();
    }

    // Get the singleton instance
    public static AdminControlPanel getInstance() {
        return INSTANCE;
    }

    // Initializes components for the user interface
    private void initializeComponents() {
        frame = new JFrame("MiniTwitter Admin Control Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400); 

        treeView = new TreeView();

        // Text fields
        userIDField = new JTextField();
        groupIDField = new JTextField();
        userIDField.setPreferredSize(new Dimension(150, 20));
        groupIDField.setPreferredSize(new Dimension(150, 20));

        // Colors I want to use
        Color lightBlue = new Color(173, 216, 230); 
        Color darkBlue = new Color(0, 105, 148); 

        // Set background to light blue
        frame.getContentPane().setBackground(lightBlue);

        // Buttons that are created with their text and color
        addUserButton = createButton("Add User", Color.WHITE,darkBlue );
        addGroupButton = createButton("Add Group", Color.WHITE,darkBlue);
        openUserViewButton = createButton("Open User View", Color.WHITE,darkBlue);
        showUserTotalButton = createButton("Show User Total", Color.WHITE,darkBlue);
        showGroupTotalButton = createButton("Show Group Total", Color.WHITE,darkBlue);
        showMessagesTotalButton = createButton("Show Message Total", Color.WHITE,darkBlue);
        showPositivePercentageButton = createButton("Show Positive Percentage", Color.WHITE,darkBlue);

        // Set the color for these components
        userIDField.setBackground(lightBlue);
        userIDField.setForeground(darkBlue);
        groupIDField.setBackground(lightBlue);
        groupIDField.setForeground(darkBlue);

        treeView.getTree().setBackground(lightBlue);

        // Panel on the right hand side 
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(createSplitPane("User ID:", userIDField, addUserButton));
        rightPanel.add(createSplitPane("Group ID:", groupIDField, addGroupButton));
        rightPanel.add(openUserViewButton);
        rightPanel.add(showUserTotalButton);
        rightPanel.add(showGroupTotalButton);
        rightPanel.add(showMessagesTotalButton);
        rightPanel.add(showPositivePercentageButton);

        // Split treeview and right hand panel
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            new JScrollPane(treeView.getTree()),
            rightPanel
        );
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(250);

        frame.add(splitPane);
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);

        rightPanel.setBackground(lightBlue); 
        frame.setTitle("MiniTwitter Admin Control Panel"); 
        UIManager.put("Label.foreground", Color.WHITE); 

        frame.add(splitPane);
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }

    // Method to create button
    private JButton createButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setActionCommand(text);
        button.addActionListener(this);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        return button;
    }
    
    public TreeView getTreeView() {
        return treeView;
    }

    private JPanel createSplitPane(String labelText, JTextField textField, JButton button) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(labelText));
        panel.add(textField);
        panel.add(button);
        return panel;
    }

    // Calls method based on which button was pressed by the user
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("Add User".equals(command)) {
            addUserAction();
        } else if ("Add Group".equals(command)) {
            addGroupAction();
        } else if ("Open User View".equals(command)) {
            openSelectedUserView();
        } else if ("Show User Total".equals(command)) {
            showUserTotalAction();
        } else if ("Show Group Total".equals(command)) {
            showGroupTotalAction();
        } else if ("Show Message Total".equals(command)) {
            showMessageTotalAction();
        } else if ("Show Positive Percentage".equals(command)) {
            showPositivePercentageAction();
        }
    }
    
    // Opens user view
    private void openSelectedUserView() {
        // User is selected and their user view is opened
        User selectedUser = treeView.getSelectedUser();
        if (selectedUser != null) {
            treeView.openUserView(selectedUser);
        } else {
            // Handle the case where no user is selected or the selection is not a user
            showMessage("No user selected or selected entry is not a user.", "Open User View Error");
        }
    }

    // Adds user based on what is inputted into the text field
    private void addUserAction() {
        String userIdText = userIDField.getText().trim();
        if (!userIdText.isEmpty() && !User.userExists(userIdText)) {
            User user = new User(userIdText);
            treeView.addEntry(user);
        } else {
            showMessage("User ID is empty or already exists.", "Add User Error");
        }
    }

    // Adds group based on what is inputted into the text field
    private void addGroupAction() {
        String groupIdText = groupIDField.getText().trim();
        if (!groupIdText.isEmpty() && !Group.groupExists(groupIdText)) {
            Group group = new Group(groupIdText);
            treeView.addEntry(group);
        } else {
            showMessage("Group ID is empty or already exists.", "Add Group Error");
        }
    }

    // Shows total number of users
    private void showUserTotalAction() {
        int totalUsers = treeView.getTotalUsers();
        showMessage("There are " + totalUsers + " users in the system.", "Total Users");
    }

    // Shows total number of groups
    private void showGroupTotalAction() {
        int totalGroups = treeView.getTotalGroups();
        showMessage("There are " + totalGroups + " groups in the system.", "Total Groups");
    }

    // Shows total number of messages
    private void showMessageTotalAction() {
        int totalMessages = User.getTotalTweets();
        showMessage("Total number of messages (tweets) is: " + totalMessages, "Total Messages");
    }

    // Shows percentage of postive tweets to all tweets
    private void showPositivePercentageAction() {
        double positivePercentage = User.getTotalPositiveTweetsPercentage();
        showMessage("The percentage of positive messages is: " + positivePercentage + "%", "Positive Message Percentage");
    }

    // Shows information about the app when called for 
    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
