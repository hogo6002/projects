package Data;


import java.util.Date;

public class Transaction {
    private int id;
    private EasyDate date;
    private int itemId;
    private int adminId;
    private int changeNum;
    private String comment;
    private String itemName;
    private String adminName;
    private String userName;

    public Transaction(int id, EasyDate date, int itemId, int adminId,
                       int changeNum) {
        this.id = id;
        this.date = date;
        this.itemId = itemId;
        this.adminId = adminId;
        this.changeNum = changeNum;
        this.comment = "";
        itemName = "编号: " + itemId;
        adminName = "编号: " + adminId;
    }

    public Transaction(int id, EasyDate date, int itemId, int adminId,
                       int changeNum,
                       String comment) {
        this.id = id;
        this.date = date;
        this.itemId = itemId;
        this.adminId = adminId;
        this.changeNum = changeNum;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EasyDate getDate() {
        return date;
    }

    public void setDate(EasyDate date) {
        this.date = date;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(int changeNum) {
        this.changeNum = changeNum;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setItemName(String name) {
        itemName = name;
    }

    public void setAdminName(String name) {
        adminName = name;
    }

    public void setNames(String itemName, String adminName) {
        setItemName(itemName);
        setAdminName(adminName);
    }

    @Override
    public String toString() {
        String change = changeNum >= 0 ? "增加" : "减少";
        return "交易编号： " + id + "， 交易时间： " + date + "， "+ itemName + change  +
                Math.abs(changeNum) + "个， 处理人： " + adminName + "， 备注： " + comment;
    }
}
