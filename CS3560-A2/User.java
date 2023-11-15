import java.util.*;

public class User extends EntryText implements Observable, Visitable {

    // Initilization, keeps count, has set of Observers user is following and is being followed by
    private static final Set<String> allUserIds = Collections.synchronizedSet(new HashSet<>());
    private static int totalUsers;
    private static int totalTweets;
    private static int totalPositiveTweets;
    private String userID;
    private Set<Observer> followers = new HashSet<>();
    private Set<Observer> following = new HashSet<>();
    private int userTweets;
    private int positiveTweets;
    private UserView window;

    // Makes sure user is not empty or duplicate, if mot then create user and set their count to 0
    public User(String id) {
        if (id == null || id.trim().isEmpty() || allUserIds.contains(id)) {
            throw new IllegalArgumentException("Invalid or duplicate user ID.");
        }
        this.userID = id;
        totalUsers++;
        allUserIds.add(id);
        this.userTweets = 0;
        this.positiveTweets = 0;
    }

    // Amount of users that are followed by this user
    public Set<Observer> getFollowing() {
        return Collections.unmodifiableSet(following);
    }

    // Amount of users that are following this user
    public Set<Observer> getFollowers() {
        return Collections.unmodifiableSet(followers);
    }

    // Get number of users
    public static int getTotalUsers() {
        return totalUsers;
    }

    // Get number of tweets
    public static int getTotalTweets() {
        return totalTweets;
    }

    // Get percentage of positive tweets 
    public static double getTotalPositiveTweetsPercentage() {
        return totalTweets == 0 ? 0.0 : (double) totalPositiveTweets / totalTweets * 100;
    }

    // Checks if user exists
    public static boolean userExists(String userID) {
        return allUserIds.contains(userID);
    }

    // Checks for duplicates
    public static boolean contains(String userId) {
        return allUserIds.contains(userId);
    }

    // Receives messages
    @Override
    public void receive(String message) {
        if (window != null) {
            window.receive(message);
        }
    }

    // Overrides so the actual name of the user is added to the tree view
    @Override
    public String toString() {
        return userID;
    }

    public void setWindow(UserView window) {
        this.window = window;
    }

    // Method to post a tweet by the user
    public void post(String tweet) {
    for (Observer follower : followers) {
        follower.receive(this.getDisplayName() + ": " + tweet);
    }
    // News feed is updated if user window is open
    if (this.window != null) {
        this.window.receive(this.getDisplayName() + ": " + tweet);
    }
    countTweet(tweet); 
    userTweets++;      
    totalTweets++;     
    }

    // Postive tweets that are kept track of
    private void countTweet(String tweet) {
        if (tweet.toLowerCase().contains("good") || tweet.toLowerCase().contains("great")) {
            positiveTweets++;
            totalPositiveTweets++;
        }
    }

    @Override
    public String getDisplayName() {
        return userID;
    }

    @Override
    public int accept(Visitor visitor) {
        return visitor.visit(this);
    }

    // Add follower to followers list of user
    @Override
    public void updateFollower(Observer follower) {
        followers.add(follower);
    }

    // Add the user being followed to following list
    @Override
    public void follow(Observer followee) {
        if (followee instanceof User) {
            following.add(followee);
            ((User) followee).updateFollower(this);
        }
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public boolean isUser() {
        return true;
    }
}
