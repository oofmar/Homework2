public abstract class EntryText {
    
    public boolean addEntry(EntryText newEntry) {
        throw new UnsupportedOperationException("Cannot add Entry");
    }

    // Gets the display name 
    public abstract String getDisplayName();

    // Assumes it is not a group
    public boolean isGroup() {
        return false;
    }

    // Assumes it is not a user
    public boolean isUser() {
        return false;
    }
}
