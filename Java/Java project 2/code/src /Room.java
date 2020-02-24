import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Building block for the map. Contains {@link Thing Thing}s Note: all exit labels are case sensitive.
 *
 * @author JF
 * @serial exclude
 */
public class Room implements Serializable {

    // Description for this Room
    private String description;
    // Exits from this Room. Strings are names of the exits
    private Map<String, Room> exits;
    // Things in this Room
    private List<Thing> contents;

    /*
     * Replace characters in description strings
     * It is very important that this is private so it will be resolved
     * correctly during construction
     */    
    private void replaceDescription(String description) {
        this.description = description.replace('\n', '*')
                .replace('\r', '*');
    }

    /**
     * @param description Description for the room Note: each \r \n 
     *                    in description will be replaced with a `*`.
     */
    public Room(String description) {
        replaceDescription(description);
        exits = new TreeMap<String, Room>();
        contents = new LinkedList<Thing>();
    }

    /**
     * A description of the room.
     *
     * @return Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Change Room description Note: each  \n or \r  in the new 
     * description will be replaced with a `*`.
     *
     * @param description new Description
     */
    public void setDescription(String description) {
        replaceDescription(description);
    }

    /**
     * What exits are there from this Room?
     *
     * @return Non-modifiable map of names to Rooms
     */
    public Map<String, Room> getExits() {
        return Collections.unmodifiableMap(this.exits);
    }

    /**
     * What Things are in this Room?
     *
     * @return Non-modifiable List of Things in the Room
     */
    public List<Thing> getContents() {
        return Collections.unmodifiableList(this.contents);
    }

    /**
     * Add a new exit to this Room.
     *
     * @param name   Name of the exit
     * @param target Room the exit goes to
     * @throws ExitExistsException if the room already has an exit of that name
     * @throws NullRoomException   if target is null
     */
    public void addExit(String name, Room target) throws ExitExistsException,
            NullRoomException {
        if (exits.containsKey(name)) {
            throw new ExitExistsException();
        }
        if (target == null) {
            throw new NullRoomException();
        }
        exits.put(name, target);
    }

    /**
     * Remove an exit from this Room Note: silently fails if exit does not
     * exist.
     *
     * @param name Name of exit to remove
     */
    public void removeExit(String name) {
        exits.remove(name);
    }

    /**
     * Add Thing to this Room.
     *
     * @param item Thing to add
     */
    public void enter(Thing item) {
        if (!contents.contains(item)) {
            contents.add(item);
        }
    }

    /**
     * Remove item from Room. Note: will fail if item is not in the Room or if
     * something wants to fight item.
     *
     * @param item Thing to remove
     * @return true if removal was successful
     */
    public boolean leave(Thing item) {
        if (!contents.contains(item)) {
            return false;
        }
        boolean trap = false;
        if (item instanceof Mob) {
            for (Thing i : contents) {
                if ((i instanceof Mob) && (i != item)) {
                    if (((Mob) i).wantsToFight((Mob) item)) {
                        trap = true;
                        break;
                    }
                }
            }
        }
        if (trap) {
            return false;
        }
        contents.remove(item);
        return true;
    }

    /** Connects two rooms both ways.
    * Note: either both exits are created or neither are.  
    * @param room1 First room 
    * @param room2 Second room
    * @param label1 Name of exit which goes from room1 to room2
    * @param label2 Name of exit which goes from room2 to room1
    * @throws ExitExistsException if one or more of the exit labels are in use
    * @throws NullRoomException if either room1 or room2 is null
    * @throws NullPointerException if either label is null
    */
    public static void makeExitPair(Room room1, Room room2, 
            String label1, String label2) 
            throws ExitExistsException, NullRoomException {
        if ((room1 == null) || (room2 == null)) {
            throw new NullRoomException();
        }
        if ((label1 == null) || (label2 == null)) {
            throw new NullPointerException();
        }
        room1.addExit(label1, room2); // any exception here will throw out
        try {
            room2.addExit(label2, room1);
        } catch (ExitExistsException e) {
            room1.removeExit(label1);
            throw e;
        }
    }
}
