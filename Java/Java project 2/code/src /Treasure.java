/**
 * Lootable object which doesn't fight.
 *
 * @author JF
 * @serial exclude
 */
public class Treasure extends Thing implements Lootable {
    // Worth of this treasure.
    private double value;

    /**
     * Make a treasure.
     * Note: Character replacement rules from Thing apply
     * @param shortDescription short name for this item. (This will also be
     *                         used as the long description)
     * @param value worth of this item
     */
    public Treasure(String shortDescription, double value) {
        super(shortDescription, shortDescription);
        this.value = value;
    }

    /**
     * Gets the worth of the item.
     * @return the worth of this item
     */
    @Override
    public double getValue() {
        return value;
    }

    /**
     * Can the looter loot me.
     * @return true if looter is an instance of Player; else false
     * @inheritDoc
     */
    @Override
    public boolean canLoot(Thing looter) {
        return looter instanceof Player;
    }

    /**
    * Get encoded representation.
    * Note: when representing doubles, use String.format("%.5f", value)
    * @return <B>$</B>;<i>V</i>;<i>S</i> where V=value, S=raw short description
    <br />(eg "$;14.50000;box")
    */
    @Override
    public String repr() {
        return "$;"+String.format("%.5f", value)+";"+getShort();
    }
    
    /** Factory to create Treasure from a String
    * @param encoded repr() form of the object
    * @return decoded Object or null for failure.  Failures include:  
               null parameters, empty input or improperly encoded input.
    */
    public static Treasure decode(String encoded) {
        if (encoded == null) {
            return null;
        }
        String[] toks = encoded.split(";");
        if (toks.length != 3) {
            return null;
        }
        try {
            double val = Double.parseDouble(toks[1]);
            return new Treasure(toks[2], val);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
}
