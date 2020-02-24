/**
 * A non-player Lootable Mob.
 *
 * @author JF
 * @serial exclude
 */
public class Critter extends Thing implements Lootable, Mob {
    // What is the value of this Mob once looted?
    private double value;
    // Current health of this Mob.
    private int health;
    // Default health (used if alive is set back to true).
    private int MAX_HEALTH = 10;

    /**
     * Critter - A non-player Lootable Mob.
     * @param shortDescription Name or short description for this Mob
     * @param longDescription  Longer description for this Mob
     * @param value     Worth of Mob when looted
     * @param health    starting health of Mob
     */
    public Critter(String shortDescription, String longDescription,
                   double value, int health) {
        super(shortDescription, longDescription);
        this.value = value;
        this.health = health;
    }

    /**
     * Long desctription of Mob.
     *
     * @return Long description, immediately followed by "(fainted)" iff the
     *         critter is not alive.
     */
    @Override
    public String getDescription() {
        return isAlive() ? getLong() : getLong() + "(fainted)";
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
    }

    /**
     * @return 2
     * @inheritDoc
     */
    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    public double getValue() {
        return value;
    }

    /**
     * @return true iff looter is a Player and your health is zero
     * @inheritDoc
     */
    @Override
    public boolean canLoot(Thing looter) {
        return (looter instanceof Player) && (health == 0);
    }

    /**
     * @inheritDoc
     * @see Mob#fight(Mob)
     */
    @Override
    public void fight(Mob mob) {
        while (this.isAlive() && mob.isAlive()) {
            int damage = getDamage();
            mob.takeDamage(damage);
            if (mob.isAlive()) {
                damage = mob.getDamage();
                this.takeDamage(damage);
            }
        }
    }

    /**
     * Returns whether the mob is an Explorer.
     * @return true if mob is an Explorer
     */
    @Override
    public boolean wantsToFight(Mob mob) {
        return mob instanceof Explorer;
    }

    /**
     * Is the current Mob health above zero?
     */
    @Override
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * If true, set health to MAX_HEALTH.
     * @see Critter#Critter(String, String, double, int)
     */
    @Override
    public void setAlive(boolean alive) {
        health = (alive ? MAX_HEALTH : 0);
    }

    /**
     * Returns the current health.
     * @return current health
     */
    public int getHealth() {
        return health;
    }
    
    /**
    * Get encoded representation.
    * Note: when representing doubles, use String.format("%.5f", value)
    * @return <B>C</B>;<i>V</i>;<i>H</i>;<i>S</i>;<i>L</i> where V=value, H=health,
        S=raw short description, L=raw long description
        <br />(eg "C;14.50000;2;cat;a cat")
    */
    @Override
    public String repr() {
        return "C;" + String.format("%.5f", value) + ';' + health+';' 
                + getShort() + ';' + getLong();
    }    
    
    /** Factory to create Critter from a String
    * @param encoded repr() form of the object
    * @return decoded Object or null for failure. Failures include:  
               null parameters, empty input or improperly encoded input.
    */    
    public static Critter decode(String encoded) {
        if (encoded == null) {
            return null;
        }
        String[] toks = encoded.split(";");
        if (toks.length != 5) {
            return null;
        }
        try {
            double val = Double.parseDouble(toks[1]);
            int health = Integer.parseInt(toks[2]);
            return new Critter(toks[3],toks[4],val, health);
        } catch (NumberFormatException nfe) {
            return null;
        }    
    }
}
