package Data;

public class Admin {
    private int id;
    private String name;

    public Admin(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "管理员{ " +
                "管理员编号 = " + id +
                ", 管理员姓名 = '" + name + '\'' +
                '}';
    }
}
