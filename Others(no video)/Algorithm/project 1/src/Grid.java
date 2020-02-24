/**
 * A 2D grid data structure that holds items in a positional relationship to each other.
 * Each (x,y) coordinate can hold a single item of type <T>.
 *
 * @author Henry O'Brien (adapted from Richard Thomas)
 *
 * @param <T> The type of element held in the data structure
 */
public interface Grid<T> {

	/**
	 * Add an element at a fixed position, overriding any existing element there.
	 *
	 * @param x The x-coordinate of the element's position
	 * @param y The y-coordinate of the element's position
	 * @param element The element to be added at the indicated position
	 * @throws IllegalArgumentException If the x or y value is out of the grid's maximum bounds
	 */
	public void add(int x, int y, T element) throws IllegalArgumentException;

	/**
	 * Returns the element at the indicated position.
	 *
	 * @param x The x-coordinate of the element to retrieve
	 * @param y The y-coordinate of the element to retrieve
	 * @return The element at this position, or null is no elements exist
	 * @throws IndexOutOfBoundsException If the x or y value is out of the grid's maximum bounds
	 */
	public T get(int x, int y) throws IndexOutOfBoundsException;

	/**
	 * Removes the element at the indicated position.
	 *
	 * @param x The x-coordinate of the element to remove
	 * @param y The y-coordinate of the element to remove
	 * @return true if an element was successfully removed, false if no element exists at (x, y)
	 * @throws IndexOutOfBoundsException If the x or y value is out of the grid's maximum bounds
	 */
	public boolean remove(int x, int y) throws IndexOutOfBoundsException;

	/**
	 * Removes all elements stored in the grid.
	 */
	public void clear();

    /**
     * Changes the size of the grid.
     * Existing elements should remain at the same (x, y) coordinate
     * If a resizing operation has invalid dimensions or causes an element to be lost,
     *  the grid should remain unmodified and an IllegalArgumentException thrown
     *
     * @param newWidth The width of the grid after resizing
     * @param newHeight The height of the grid after resizing
     * @throws IllegalArgumentException if the width or height is less than or equal to zero, or
     *                                  if an element would be lost after this resizing operation
     */
    public void resize(int newWidth, int newHeight) throws IllegalArgumentException;
}