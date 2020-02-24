import java.util.Collections;
import java.util.List;
import java.util.LinkedList;


/**
 * A player object.
 *
 * @author JF
 * @serial exclude
 */
public abstract class Player extends Thing implements Mob {
    // Current health
    private int health;
    // Starting health
    private static final int MAX_HEALTH = 10;
    // Our inventory
    private List<Thing> contents;

    /**
     * A player with default health.
     * @param shortDescription A short name or description for the Player
     * @param longDescription A more detailed description for the Player
     * @see Thing#Thing
     */
    public Player(String shortDescription, String longDescription) {
        super(shortDescription, longDescription);
        health = MAX_HEALTH;
        contents = new LinkedList<Thing>();
    }

    /**
     * A player with a defined health.
     * @param shortDescription A short name or description for the Player
     * @param longDescription A more detailed description for the Player
     * @param health The starting health for the Player
     * @see Thing#Thing
     */
    public Player(String shortDescription, String longDescription, int health) {
        super(shortDescription, longDescription);
        this.health = health;
        contents = new LinkedList<Thing>();
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
    }

    @Override
    public void fight(Mob mob) {
        while (this.isAlive() && mob.isAlive()) {
            int d = getDamage();
            mob.takeDamage(d);
            if (mob.isAlive()) {
                d = mob.getDamage();
                this.takeDamage(d);
            }
        }
    }

    @Override
    public boolean wantsToFight(Mob mob) {
        return false;
    }

    /**
     * Gets the health of the Player.
     * @return health of Player
     */
    public int getHealth() {
        return health;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void setAlive(boolean alive) {
        health = (alive ? MAX_HEALTH : 0);
    }

    /**
     * Put thing into player's inventory.
     *
     * @param thing Thing to add to inventory
     */
    public void add(Thing thing) {
        contents.add(thing);
    }

    /**
     * What is in the player's inventory.
     *
     * @return Things in the inventory
     */
    public List<Thing> getContents() {
        return Collections.unmodifiableList(this.contents);
    }

    /**
     * Remove Thing from inventory. Fails silently if item isn't present.
     *
     * @param thing Thing to remove
     */
    public void drop(Thing thing) {
        contents.remove(thing); // silently fails if not there
    }

    /**
     * Remove Thing from inventory.
     *
     * @param name Name of the thing to remove
     * @return Thing removed or null if not found
     */
    public Thing drop(String name) {
        int index = 0;
        for (Thing t : contents) {
            if (t.getShortDescription().equals(name)) {
                break;
            }
            index++;
        }
        if (index < contents.size()) {
            return contents.remove(index);
        }
        return null;
    }
}
