/**
 * Anything which can fight.
 *
 * @author JF
 */
public interface Mob {
    /**
     * Fight another mob. This mob gets first hit.
     *
     * @param mob the target
     */
    void fight(Mob mob);

    /**
     * Does this Mob want to fight mob?
     *
     * @param mob possible target
     * @return true if we want to fight that Mob
     */
    boolean wantsToFight(Mob mob);

    /**
     * Is this Mob alive?
     *
     * @return true if the Mob's health &gt; 0
     */
    boolean isAlive();

    /**
     * Set this Mob's status.
     * Note: setting a Mob back to alive will set its
     *              health back to its max health. Setting a Mob with
     *              &gt;0 health to false will set its health to 0.     
     *
     * @param alive life status 
     */
    void setAlive(boolean alive);

    /**
     * How much damage could this mob do in one hit?
     *
     * @return damage amount
     */
    int getDamage();

    /**
     * Attempt to damage this Mob.
     *
     * @param amount Amount of damage
     */
    void takeDamage(int amount);
}
