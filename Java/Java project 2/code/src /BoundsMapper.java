import java.util.*;

/** Find the bounding box for the overall map.
* @author JF
*/
public class BoundsMapper extends MapWalker {
  /** Map Rooms to coordinates*/
  public Map<Room, Pair> coords;
  
  /** Minimum x coordinate for rooms (root has x=0) */
  public int xMin;
  
  /** Maximum x coordinate for rooms (root has x=0) */  
  public int xMax;
  
  /** Minimum y coordinate for rooms (root has y=0) */  
  public int yMin;
  
  /** Maximum y coordinate for rooms (root has y=0) */  
  public int yMax;  
  
  public BoundsMapper(Room root) {
    super(root);
    coords = new HashMap<Room, Pair>();
    xMin = xMax = yMin = yMax = 0;    
  }
  
  /** Assign room coordinates relative to a neighbour.
  * <br />If room has no known neighbours, give it coordinate (0,0).<br /> 
  *    If your "North" neighbour has coordinates (x,y), then 
  *    your coodinates should be (x, y-1).<br />
  *    If your "East" neighbour has coordinates (x,y), then <br />
  *    your coordinates should be (x+1, y). <br />
  *    (Similar for South and West). 
  *
  * <br />Check for known coordinates in order: North, South, East, West.
  * @param room Room to assign coordinates to
  * @require All exits are labelled
  *    one of {"North", "South", "East", "West"}
  * 
  */
  protected void visit(Room room) {
    Map<String, Room> exits = room.getExits();
    Pair n = null;
    if (hasVisited(exits.get("North"))) {
        Pair p = coords.get(exits.get("North"));
        n = new Pair(p.x,p.y + 1);  // remember screen coords flipped
    } else if (hasVisited(exits.get("South"))) {
        Pair p = coords.get(exits.get("South"));
        n = new Pair(p.x,p.y - 1);  // remember screen coords flipped
    } else if (hasVisited(exits.get("East"))) {
        Pair p = coords.get(exits.get("East"));
        n = new Pair(p.x - 1,p.y);
    } else if (hasVisited(exits.get("West"))) {
        Pair p = coords.get(exits.get("West"));
        n = new Pair(p.x + 1,p.y);
    } else {
        // can't be sure where we are, assume 0, 0
        n = new Pair(0,0);
    }
    coords.put(room, n);
    if (n.x < xMin) {
      xMin = n.x;
    } else if (n.x > xMax) {
      xMax = n.x;
    }
    if (n.y < yMin) {
      yMin = n.y;
    } else if (n.y > yMax) {
      yMax = n.y;
    }
  }

  @Override 
  public void reset() {
    super.reset();
    coords = new HashMap<Room, Pair>();
    xMin = xMax = yMin = yMax = 0;    
  }
}
