import java.util.HashSet;
import java.util.Set;

public class Group extends EntryText implements Visitable {

    //Initialization, to hold objects from SysEntry like groups and users and to prevent duplicates 
    private Set<EntryText> entries;
    private static int totalGroups = 0;
    private static Set<String> groupNames = new HashSet<>();
    private String name;
    private long creationTime;

    // First checks if group already exists, if it does throw exception, if not then create the group and increment
    public Group(String name) {
        if (groupExists(name)) {
            throw new IllegalArgumentException("A group with this name already exists.");
        }
        this.name = name;
        this.entries = new HashSet<>();
        totalGroups++;
        groupNames.add(name);
        this.creationTime = System.currentTimeMillis();
    }

    
    public void add(EntryText entry) {
        entries.add(entry);
    }

    
    public static int getTotalGroups() {
        return totalGroups;
    }

    public static boolean groupExists(String groupId) {
        return groupNames.contains(groupId);
    }


    // Overrides to show actual name of the group in tree
    @Override
    public String getDisplayName() {
        return name;
    }
    // Overrides so actual group name is added
    @Override
    public String toString() {
        return name;
    }

    // From visitable interface, pass to the visit method 
    @Override
    public int accept(Visitor visitor) {
        return visitor.visit(this);
    }

    // Overrides to make sure the entry is a group 
    @Override
    public boolean isGroup() {
        return true;
    }

    // Overrides to make sure the entry is not a user
    @Override
    public boolean isUser() {
        return false;
    }

    // Get creation time
    public long getCreationTime() {
        return creationTime;
    }
}
