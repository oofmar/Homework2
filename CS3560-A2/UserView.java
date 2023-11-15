import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserView extends JFrame implements ActionListener {

    // User Interface components
    private JTextField textField;
    private JTextField tweetField;
    private JList<String> followingList;
    private JList<String> newsFeedList;
    private DefaultListModel<String> followingModel;
    private DefaultListModel<String> newsFeedModel;
    private User currentUser;

    // Sets up the window that will be for the user view
    public UserView(User user) {
        currentUser = user;
        currentUser.setWindow(this);
        initializeComponents();
        setTitle(user.getDisplayName() + "'s User Interface");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(500, 700); 
        setVisible(true);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Panel at the top that will allow current user to follow already eixsting user
        JPanel followPanel = new JPanel(new FlowLayout());
        textField = new JTextField(20);
        JButton followButton = createButton("Follow User", "follow");
        followPanel.add(textField);
        followPanel.add(followButton);

        // Following list that the user is able to view
        followingModel = new DefaultListModel<>();
        followingModel.addElement("Currently Following");
        followingList = new JList<>(followingModel);
        JScrollPane followingScrollPane = new JScrollPane(followingList);

        // Panel at the bottom that allows user to enter a tweet
        JPanel tweetPanel = new JPanel(new FlowLayout());
        tweetField = new JTextField(20);
        JButton postButton = createButton("Post Tweet", "post");
        tweetPanel.add(tweetField);
        tweetPanel.add(postButton);

        // News feed list that the user is able to view
        newsFeedModel = new DefaultListModel<>();
        newsFeedModel.addElement("News Feed");
        newsFeedList = new JList<>(newsFeedModel);
        JScrollPane newsFeedScrollPane = new JScrollPane(newsFeedList);

        // Adds the components
        add(followPanel, BorderLayout.NORTH);
        add(followingScrollPane, BorderLayout.WEST);
        add(tweetPanel, BorderLayout.SOUTH);
        add(newsFeedScrollPane, BorderLayout.CENTER);
    }

    // Allows for the buttons to be created 
    private JButton createButton(String text, String actionCommand) {
        JButton button = new JButton(text);
        button.setActionCommand(actionCommand);
        button.addActionListener(this);
        return button;
    }

    // Does action based on what button is pressed 
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("follow".equals(command)) {
            followUser();
        } else if ("post".equals(command)) {
            postTweet();
        }
    }

    // Method to follow a user
    private void followUser() {
        String userToFollow = textField.getText().trim();
    if (isValidFollow(userToFollow)) {
        followingModel.addElement(userToFollow); 
        User userToFollowObject = AdminControlPanel.getInstance().getTreeView().findUser(userToFollow);
        if (userToFollowObject != null) {
            currentUser.follow(userToFollowObject); 
        }
        textField.setText(""); 
    }
    }

    // Makes sure the follow button is allowed by not letting user follow nobody and themselves
    private boolean isValidFollow(String userToFollow) {
    return !userToFollow.equals(currentUser.getDisplayName()) &&
    User.contains(userToFollow) &&
    !currentUser.getFollowing().contains(userToFollow);

    }

    // Method to post tweet 
    private void postTweet() {
        String tweet = tweetField.getText();
    if (!tweet.isEmpty()) {
        currentUser.post(tweet);
        tweetField.setText(""); 
    }
    }

    // Displays tweet 
    public void receive(String tweet) {
        newsFeedModel.addElement(tweet);
    }
}
