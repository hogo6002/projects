import java.util.*;

/** Iterator over all reachable {@link Room Room}s
* @author JF
*/
public class MapWalker
{
    /** Rooms we have already processed */
    private Set<Room> visited;
    /** Current todo list */
    private Deque<Room> toVisit;
    private Room start;
    
    /**
    * Choose start room but <B>Do not start the walk process.</B>
    * <br /><b>Joel will not test this constructor with a null parameter</b>.
    * @param start Room to begin exploring from
    */
    public MapWalker(Room start) {
        visited = new HashSet<Room>();
        this.start = start;
    }

    /**
     * Called by walk --- clears any state from previous walks.
     * Subclasses which @Override this method must call super.reset()
     * internally to ensure that parent state is cleared as well.
    */
    protected void reset() {
        visited = new HashSet<Room>();
        toVisit = new LinkedList<Room>();    
    }

    /** 
    * Visit all reachable rooms and call visit().
    * Note that code related to tracking which rooms have been visited
    * should be included here and not in visit().
	* Call reset() at the beginning of this routine.
    */
    public void walk() {
        reset();
        toVisit.add(start);    
        while (! toVisit.isEmpty()) {
            Room r = toVisit.removeFirst();
            if (!visited.contains(r)) {
                visited.add(r);
                for (Room e : r.getExits().values()) {
                    toVisit.add(e);
                }
                visit(r);
            }
        }
    }

    /**
    * @param room Room to query
    * @return true if room has been processed
    */
    public boolean hasVisited(Room room) {
        if (room == null) {
            return false;
        }
        return visited.contains(room);
    }

    /** process a room
    * override to customise behaviour
    * @param room Room to deal with
    */
    protected void visit(Room room) {
    }

}
