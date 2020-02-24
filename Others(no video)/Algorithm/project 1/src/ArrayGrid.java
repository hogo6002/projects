/**
 * A 2D grid implemented as an array.
 * Each (x,y) coordinate can hold a single item of type <T>.
 *
 * @param <T> The type of element held in the data structure
 */
public class ArrayGrid<T> implements Grid<T> {
	private T[][] data;
	private int width;
	private int height;

	/**
	 * Constructs a new ArrayGrid object with a given width and height.
	 *
	 * @param width The width of the grid
	 * @param height The height of the grid
     * @throws IllegalArgumentException If the width or height is less than or equal to zero
	 */
	public ArrayGrid(int width, int height) throws IllegalArgumentException {
	    // TODO: implement the constructor
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException();
		}
		this.width = width;
		this.height = height;
		this.data = (T[][]) new Object[width][height];
	}

	// TODO: implement all of Grid's methods here


	/**
	 * Add an element at a fixed position, overriding any existing element there.
	 *
	 * @param x       The x-coordinate of the element's position
	 * @param y       The y-coordinate of the element's position
	 * @param element The element to be added at the indicated position
	 * @throws IllegalArgumentException If the x or y value is out of the grid's maximum bounds
	 */
	@Override
	public void add(int x, int y, T element) throws IllegalArgumentException {
		if (x < 0 || y < 0|| x >= width || y >= height) {
			throw new IllegalArgumentException();
		}
		data[x][y] = element;
	}

	/**
	 * Returns the element at the indicated position.
	 *
	 * @param x The x-coordinate of the element to retrieve
	 * @param y The y-coordinate of the element to retrieve
	 * @return The element at this position, or null is no elements exist
	 * @throws IndexOutOfBoundsException If the x or y value is out of the grid's maximum bounds
	 */
	@Override
	public T get(int x, int y) throws IndexOutOfBoundsException {
		if (x < 0 || y < 0|| x >= width || y >= height) {
			throw new IndexOutOfBoundsException();
		}
		if (data[x][y] == null) {
			return null;
		}
		return data[x][y];
	}

	/**
	 * Removes the element at the indicated position.
	 *
	 * @param x The x-coordinate of the element to remove
	 * @param y The y-coordinate of the element to remove
	 * @return true if an element was successfully removed, false if no element exists at (x, y)
	 * @throws IndexOutOfBoundsException If the x or y value is out of the grid's maximum bounds
	 */
	@Override
	public boolean remove(int x, int y) throws IndexOutOfBoundsException {
		if (x < 0 || y < 0|| x >= width || y >= height) {
			throw new IndexOutOfBoundsException();
		}
		if (data[x][y] == null) {
			return false;
		} else {
			data[x][y] = null;
			return true;
		}
	}

	/**
	 * Removes all elements stored in the grid.
	 */
	@Override
	public void clear() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				data[i][j] = null;
			}
		}
	}

	/**
	 * Changes the size of the grid.
	 * Existing elements should remain at the same (x, y) coordinate
	 * If a resizing operation has invalid dimensions or causes an element to be lost,
	 * the grid should remain unmodified and an IllegalArgumentException thrown
	 *
	 * @param newWidth  The width of the grid after resizing
	 * @param newHeight The height of the grid after resizing
	 * @throws IllegalArgumentException if the width or height is less than or equal to zero, or
	 *                                  if an element would be lost after this resizing operation
	 */
	@Override
	public void resize(int newWidth, int newHeight) throws IllegalArgumentException {
		if (newHeight < 1 || newWidth < 1) {
			throw new IllegalArgumentException();
		}
		for (int i = newWidth; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (data[i][j] != null) {
					throw new IllegalArgumentException();
				}
			}
		}
		for (int i = 0; i < width; i++) {
			for (int j = newHeight; j < height; j++) {
				if (data[i][j] != null) {
					throw new IllegalArgumentException();
				}
			}
		}
		T[][] newData = (T[][]) new Object[newWidth][newHeight];
		int minWid = newWidth > width ? width : newWidth;
		int minHei = newHeight > height ? height : newHeight;
		for (int i = 0; i < minWid; i++) {
			for (int j = 0; j < minHei; j++) {
				newData[i][j] = data[i][j];
			}
		}
		width = newWidth;
		height = newHeight;
		data = newData;

	}
}