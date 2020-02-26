public class Product {
    private String name;
    private int stock;

    public Product(String name, int stock) {
        this.name = name;
        this.stock = stock;
    }

    public Product(String name) {
        this.name = name;
        stock = 0;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public void changeStock(int stock) {
        this.stock += stock;
    }
}
