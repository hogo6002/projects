/**
 * Anything which can be picked up/carried by a player.
 *
 * @author JF
 */
public interface Lootable {
    /**
     * Returns the value of the item.
     *
     * @return the value
     */
    double getValue();

    /**
     * Is looter able to pick up this object?
     *
     * @param looter Object try to collect
     * @return true if looter is allowed to pick up the object
     */
    boolean canLoot(Thing looter);
}
