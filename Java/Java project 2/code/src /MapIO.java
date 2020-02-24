import java.io.*;
import java.util.*;

/** Static routines to save and load {@link Room Room}s
* @author JF
*/
public class MapIO
{
    /** Write rooms to a new file (using Java serialisation)
    * @param root Start room to explore from
    * @param filename Filename to write to
    * @return true if successful
    */
    public static boolean serializeMap(Room root, String filename) {
        try {
            FileOutputStream fs = new FileOutputStream(filename);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(root);
            os.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    /** Read serialised Rooms from a file 
    * @param filename Filename to read Rooms from
    * @return start Room or null on failure
    */
    public static Room deserializeMap(String filename) {
        try{
            FileInputStream ifs = new FileInputStream(filename);
            ObjectInputStream is = new ObjectInputStream(ifs);
            Room t = (Room)is.readObject();
            is.close();
            return t;
        } catch (IOException ex) {
            return null;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    /** Write Rooms to a new file (using encoded String form)
    *
    *
    * @param root Start room
    * @param filename Filename to write to
    * @return true if successful
    * @require There is exactly one player object anywhere in the 
         map(appearing exactly once).    
    * @detail. The structure of a file is:
    *   <ol><li>The number of rooms in the file</li>
    *       <li>Room descriptions</li>
    *       <li>Room exits</li>
    *       <li>Room contents</li>
    *   </ol>
    * Rooms are always listed in the same order with 
    *        the start room first.<br />
    * Note that this format does not preserver player inventory.<br />
    * <br />
    * The file format is as follows:<br />
    * <code>number_of_rooms</code><br />
    * <code>description_of_room_0</code><br />
    * <code>description_of_room_1</code><br />
    * <code>description_of_room_2</code><br />
    * <code>... (more descriptions)</code><br />
    * <code>number_of_exits_from_room_0</code><br />
    * <code>number_0_0 name_0_0</code><br />
    * <code>number_0_1 name_0_1</code><br />
    * <code>... (more room_0)</code><br />    
    * <code>number_of_exits_from_room_1</code><br />
    * <code>number_1_0 name_1_0</code><br />
    * <code>number_1_1 name_1_1</code><br />
    * <code>... (more room_1)</code><br />    
    * <code>number_of_exits_from_room_2</code><br />
    * <code>number_2_0 name_2_0</code><br />
    * <code>... (more room_2)</code><br />        
    * <code>... (more rooms)</code><br /> 
    * <code>number_items_in_room_0</code><br />
    * <code>repr_for_item_0_0</code><br />
    * <code>repr_for_item_0_1</code><br />
    * <code>... (more items in room 0)</code><br />
    * <code>number_items_in_room_0</code><br />
    * <code>repr_for_item_0_0</code><br />
    * <code>repr_for_item_0_1</code><br />
    * <code>... (more items in room 0)</code><br />
    * <code>... (more rooms)</code><br />  
    * <br />
    *  Note: even if a entry would have zero items, 
    *     it still needs to be there.   <br /> 
    * <br /> As an example:<br />
    * <code>    Room r1=new Room("#1");<br />
    Room r2=new Room("#2");<br />
    r2.enter(new Critter("frog", "a frog", 1, 5));<br />
    r1.enter(new Explorer("doris", "a doris"));<br />
    try {<br />
    &nbsp;&nbsp;&nbsp;&nbsp;r1.addExit("North", r2);<br />
    &nbsp;&nbsp;&nbsp;&nbsp;r2.addExit("South", r1);<br />
    } catch (ExitExistsException ee) {<br />
    } catch (NullRoomException nr) {<br />
    }<br />
    MapIO.saveMap(r1, "mymap");<br /></code>
    * <br />Would produce a file containing:<br />
    * <code>2<br />
#1<br />
#2<br />
1<br />
1 North<br />
1<br />
0 South<br />
1<br />
E;10;doris;a doris<br />
1<br />
C;1.0;5;frog;a frog<br />
</code>
    */
    public static boolean saveMap(Room root, String filename) {
        // There are two tasks here:
        // 1: To have a sequence of Rooms (with root at the start
        // 2: To be able to lookup a room's position in the sequence
        // 
        // There are more efficient ways to do this.
        // First step put all the rooms in a list with the root first
        
        // We could try to come up with an ordering using BoundsMapper
        // Code included below
        /*
        List<Room> rooms = new LinkedList<Room>();        
        rooms.add(root);
        BoundsMapper bm=new BoundsMapper(root);
        bm.walk();
        
        for (Map.Entry<Room, Pair> e : bm.coords.entrySet()) {
            if (e.getKey() != root) {
                rooms.add(e.getKey());
            }
        }
        */
            // But we'd like repeatable ordering, which hashing Room
            // objects does not give us. So We'll use a custom MapWalker
        SeqWalker sw=new SeqWalker(root);
        sw.walk();
        List<Room> rooms = sw.seq;
            // now we want to be able to find Rooms quickly
        Map<Room, Integer> idm = new HashMap<Room, Integer>();
        int count = 0;
        for (Room r : rooms) {
            idm.put(r, count++);
        }
            // now we can look up any Room's position quickly
        StringBuilder sb = new StringBuilder();
            // write the ID cap to the file 
        sb.append(idm.size());
        sb.append('\n');       
            // output all of the rooms in sequence
        for (Room r : rooms) {
            sb.append(r.getDescription());
            sb.append('\n');
        }

          //     then link them up
        for (Room r : rooms) {
            Map<String, Room> m = r.getExits();
            sb.append(m.size());
            sb.append('\n');             
            for (Map.Entry<String, Room> entry : m.entrySet()) {
                sb.append(idm.get(entry.getValue()));
                sb.append(" ");
                sb.append(entry.getKey());
                sb.append('\n');        
            }
        }
          //     then fill in the objects
        for (Room r : rooms) {
            List<Thing> l = r.getContents();
            int size = l.size();
            sb.append(l.size());
            sb.append('\n');
            for (Thing t : l) {
                sb.append(t.repr());
                sb.append('\n');
            }
        }
        try {
            FileWriter fw = new FileWriter(filename);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException ioe) {
            return false;
        }
        return true;    
    }    
    
    /** Decode a String into a Thing. (Need to be able to decode, 
    *     Treasure, Critter, Explorer)
    * @param encoded String to decode
    * @param root start room for the map
    * @return Decoded Thing or null on failure. (null arguments or 
              incorrectly encoded input)
    */
    public static Thing decodeThing(String encoded, Room root) {
        if ((encoded == null) || (root == null)) {
            return null;
        }
        switch (encoded.charAt(0)) {
        case '$': return Treasure.decode(encoded);
        case 'C': return Critter.decode(encoded);
        case 'E': return Explorer.decode(encoded);
        }
    
        return null;
    }
    
    /** Read information from a file created with saveMap
    * @param filename Filename to read from
    * @return null if unsucessful. If successful, an array of two Objects. 
           [0] being the Player object (if found) and 
           [1] being the start room. 
    * @detail. Do not add the player to the room they appear in, the caller 
           will be responsible for placing the player in the start room.
    */
    public static Object[] loadMap(String filename) {
        Player player = null;

        try {
            BufferedReader bf = new BufferedReader(
                    new FileReader(filename));
            String line = bf.readLine();
            int idcap = Integer.parseInt(line);
            Room[] rooms = new Room[idcap];
            for (int i = 0; i < idcap; ++i) {
                line = bf.readLine();
                if (line == null) {
                    return null;
                }
                rooms[i] = new Room(line);
            }
            for (int i = 0; i < idcap; ++i) {  // for each room set up exits
                line = bf.readLine();
                int exitcount=Integer.parseInt(line);
                for (int j=0; j < exitcount; ++j) {
                    line = bf.readLine();
                    if (line == null) {
                        return null;
                    }
                    int pos = line.indexOf(' ');
                    if (pos < 0) {
                        return null;
                    }
                    int target = Integer.parseInt(line.substring(0,pos));
                    String exname = line.substring(pos+1);
                    try {
                        rooms[i].addExit(exname, rooms[target]);
                    } catch (ExitExistsException e) {
                        return null;
                    } catch (NullRoomException e) {
                        return null;
                    }
                }
            }
            for (int i = 0;i<idcap;++i) {
                line = bf.readLine();
                int itemcount = Integer.parseInt(line);
                for (int j = 0; j < itemcount; ++j) {
                    line = bf.readLine();
                    if (line == null) {
                        return null;
                    }                    
                    Thing t = decodeThing(line, rooms[0]);
                    if (t == null) {
                        return null;
                    }
                    if (t instanceof Player) { // we don't add 
                        player = (Player)t;      // players to rooms
                    } else {
                        rooms[i].enter(t);
                    }
                }
            }
            Object[] res = new Object[2];                        
            res[0] = player;
            res[1] = rooms[0];
            return res;
        } catch (IOException ex) {
            return null;
        } catch (IndexOutOfBoundsException ex) {
            return null;
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
    
    /** 
    * Used to find a sequence of reachable rooms
    * @author JF
    */
    static class SeqWalker extends MapWalker 
    {
        private int count;
        public List<Room> seq;
        public SeqWalker(Room start) {
            super(start);
            count = 0;
            seq = new LinkedList<Room>();
        }
    
        @Override
        public void visit(Room r) {
            seq.add(r);
        }        
    }    

}
