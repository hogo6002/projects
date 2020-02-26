package Data;

import java.util.Objects;

public class Item {
    private int id;
    private String name;
    private int stock;

    public Item(int id, String name, int stock) {
        this.id = id;
        this.name = name;
        if (stock >= 0) {
            this.stock = stock;
        } else {
            this.stock = 0;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStock(int stock) {
        if (stock >= 0) {
            this.stock = stock;
        } else {
            this.stock = 0;
        }
    }

    public boolean lowStock() {
        return stock < 10;
    }

    public void changeStock(int number) {
        stock += number;
        if (stock < 0) {
            stock = 0;
        }
    }

    @Override
    public String toString() {
        return "产品编号： " + id + "，产品名： " + name + "，剩余数量： " + stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
