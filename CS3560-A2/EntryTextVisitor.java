public class EntryTextVisitor implements Visitor {

    
    @Override
    public int visit(User user) {
        return 1;
    }

    
    @Override
    public int visit(Group group) {
        return 1;
    }
}
