import java.io.Serializable;

/**
 * Base class for anything which can be found in a Room.
 *
 * @author JF
 * @serial exclude
 */
public abstract class Thing implements Serializable {
    /*
     * Short name of the Thing. Note: Will be used to identify the Thing in
     * commands. Note: This will be saved, so varying information should not
     * be stored here.
     */
    private String shortDescription;

    /*
     * More detailed description of the Thing. Note: This will be saved, so
     * varying information should not be stored here.
     */
    private String longDescription;

    /**
     * Note: each, \r, \n and semi-colon in the parameter 
     *     will be replaced by *    
     *
     * @param shortDescription A short name or description for the Thing
     * @param longDescription  A more detailed description for the Thing
     */
    public Thing(String shortDescription, String longDescription) {
        this.shortDescription=replaceDescription(shortDescription);
        this.longDescription=replaceDescription(longDescription);
    }

    /**
     * Allows subclasses to read the raw longDescription value.
     *
     * @return the raw shortDesc value
     */
    protected String getShort() {
        return shortDescription;
    }

    /**
     * Allows subclasses to read the raw longDescription value.
     *
     * @return the raw longDesc value
     */
    protected String getLong() {
        return longDescription;
    }

    /*
     * Replace characters in description strings
     * It is very important that this is private so it will be resolved
     * correctly during construction
     */
    private String replaceDescription(String description) {
        return description.replace('\n', '*')
                .replace(';', '*')
                .replace('\r', '*');
    }

    /** Change the short description for the Thing.
    * Note: each, \r, \n and semi-colon in the parameter 
    *     will be replaced by *        
    * @param shortDescription A short name or description for the Thing
    */
    protected void setShort(String shortDescription) {
        this.shortDescription = replaceDescription(shortDescription);
    }

    /** Change the long description for the Thing.
    * Note: each, \r, \n and semi-colon in the parameter 
    *     will be replaced by *    
    * @param longDescription A detailed description for the Thing
    */    
    protected void setLong(String longDescription) {
        this.longDescription = replaceDescription(longDescription);
    }

    /**
     * Get long description of the Thing (possibly with extra info at subclass'
     * discretion)..
     * <br />Note: not to be used for saving or encoding due
     *         possible extra info being included.
     *
     * @return Long description 
     */
    public String getDescription() {
        return longDescription;
    }

    /**
     * Get short description of the Thing.
     *
     * @return short description Note: This name is used to represent the Thing
     *         in text and to choose it in dialogs.
     */
    public String getShortDescription() {
        return shortDescription;
    }
    
    /** Get a representation of the object suitable for saving.
    @return A single line encoding enough information to identify 
    *    the type and recreate it.
    */
    public abstract String repr();
}
