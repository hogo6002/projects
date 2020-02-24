/**
 * A Player who doesn't modify the map.
 *
 * @author JF
 * @serial exclude
 */
public class Explorer extends Player {
    /**
     * Copy details (but not inventory) from another Player.
     *
     * @param player Player to copy from
     */
    public Explorer(Player player) {
        super(player.getShort(), player.getLong(), player.getHealth());
    }

    /**
     * Create an Explorer with default health.
     * @param shortDescription A short name or description for the Explorer
     * @param longDescription A more detailed description for the Explorer
     */
    public Explorer(String shortDescription, String longDescription) {
        super(shortDescription, longDescription);
    }

    /**
     * Create an Explorer with a set health.
     * @param shortDescription A short name or description for the Explorer
     * @param longDescription A more detailed description for the Explorer
     * @param health Starting health for the Explorer.
     */
    public Explorer(String shortDescription, String longDescription, 
            int health) {
        super(shortDescription, longDescription, health);
    }    
    
    /**
    * Get encoded representation.
    * @return <B>E</B>;<i>H</i>;<i>S</i>;<i>L</i> where H=health,
        S=raw short description, L=raw long description 
        <br />(eg "E;2;doris;There were ** chars but they were replaced")
    */
    @Override
    public String repr() {
            // does not save inventory
        return "E;" + getHealth() + ';' + getShort() + ';' + getLong(); 
    }

    @Override
    public String getDescription() {
        return getLong() + (isAlive()
                ? " with " + getHealth() + " health" : "(fainted)");
    }
    
    /** 
    * @return 1
    * @inheritDoc
    */
    @Override
    public int getDamage() {
        return 1;
    }
    
    /** Factory to create an Explorer from a String
    * @param encoded repr() form of the object
    * @return decoded Object or null for failure. Failures include:  
               null parameters, empty input or improperly encoded input. 
    */    
    public static Explorer decode(String encoded) {
        if (encoded == null) {
            return null;
        }
        String[] toks=encoded.split(";");
        if (toks.length != 4) {
            return null;
        }
        try {
            int health = Integer.parseInt(toks[1]);
            return new Explorer(toks[2],toks[3],health);
        } catch (NumberFormatException nfe) {
            return null;
        }    
    }    
}
