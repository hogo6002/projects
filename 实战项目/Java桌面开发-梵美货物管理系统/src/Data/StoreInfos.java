package Data;

import java.util.*;
import java.util.List;

import static Data.SqlDriver.*;

public class StoreInfos {

    private List<Item> allItems;
    private List<Transaction> allTrans;
    private Map<Integer, List<Transaction>> itemTrans;
    private List<Admin> allAdmins;

    public StoreInfos() {
        setUpAllItems();
        setUpAllAdmins();
        setUpAllTrans();
        pairTrans();
    }

    public void setUpAllItems() {
        allItems = new ArrayList<>();
        String info = selectAllItems();
        String[] parseInfo = info.split("\\|\\|");
        if (info.equals("")) {
            return;
        }
        for(int i = 0; i < parseInfo.length; i++) {
            info = parseInfo[i];
            Item item = parseItem(info);
            allItems.add(item);
        }
    }

    public Item getItem(int id) {
        for (Item item : allItems) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public List<Item> getAllItems() {
        return allItems;
    }

    public boolean changeStock(int id, int stock) {
        Item item = getItem(id);
        if (updateStock(id, stock)) {
            item.changeStock(stock);
            refresh();
            return true;
        }
        return false;
    }

    public Item parseItem (String info) {
        int id = Integer.parseInt(info.split(":")[0]);
        String name = info.split(":")[1];
        int stock = Integer.parseInt(info.split(":")[2]);
        return new Item(id, name, stock);
    }

    public Transaction parseTran (String info) {
        int id = Integer.parseInt(info.split("\\^")[0]);
        String dateInfo = info.split("\\^")[1];
        int year = Integer.parseInt(dateInfo.split("-")[0]);
        int month = Integer.parseInt(dateInfo.split("-")[1]);
        int day = Integer.parseInt(dateInfo.split("-")[2]);
        EasyDate date = new EasyDate(year, month, day);
        int itemId = Integer.parseInt(info.split("\\^")[2]);
        int adminId = Integer.parseInt(info.split("\\^")[3]);
        int changeNum = Integer.parseInt(info.split("\\^")[4]);
        if (info.split("\\^").length > 5) {
            String comment = info.split("\\^")[5];
            return new Transaction(id, date, itemId, adminId, changeNum,
                    comment);
        }
        return new Transaction(id, date, itemId, adminId, changeNum);
    }

    public void setUpAllTrans() {
        allTrans = new ArrayList<>();
        String info = selectAllTrans();
        if (info.equals("")) {
            return;
        }
        String[] parseInfo = info.split("\\|\\|");

        for(int i = 0; i < parseInfo.length; i++) {
            info = parseInfo[i];
            Transaction transaction = parseTran(info);
            transaction.setItemName(getItem(transaction.getItemId()).getName());
            transaction.setAdminName(getAdmin(transaction.getAdminId()).getName());
            allTrans.add(transaction);
        }
    }

    public List<Transaction> getAllTrans() {
        return allTrans;
    }

    public Transaction getTran(int id) {
        for (Transaction Transaction : allTrans) {
            if (Transaction.getId() == id) {
                return Transaction;
            }
        }
        return null;
    }

    public void pairTrans() {
        itemTrans = new HashMap<>();
        for (Item item : allItems) {
            List<Transaction> trans = new ArrayList<>();
            for (Transaction transaction : allTrans) {
                if (transaction.getItemId() == item.getId()) {
                    trans.add(transaction);
                }
            }
            itemTrans.put(item.getId(), trans);
        }
    }

    public List<Transaction> getItemTrans(int itemId) {
        return itemTrans.get(itemId);
    }

    public Admin parseAdmin(String info) {
        int id = Integer.parseInt(info.split(":")[0]);
        String name = info.split(":")[1];
        return new Admin(id, name);
    }

    public void setUpAllAdmins() {
        allAdmins = new ArrayList<>();
        String info = selectAllAdmins();
        String[] parseInfo = info.split("\\|\\|");

        for(int i = 0; i < parseInfo.length; i++) {
            info = parseInfo[i];
            Admin admin = parseAdmin(info);
            allAdmins.add(admin);
        }
    }

    public Admin getAdmin(int id) {
        for (Admin admin : allAdmins) {
            if (admin.getId() == id) {
                return admin;
            }
        }
        return null;
    }

    public List<Admin> getAllAdmins() {
        return allAdmins;
    }

    public boolean createTrans(EasyDate date, int itemId, int adminId,
                            int changeNum, String comment) {
        if(insertTrans(date, itemId, adminId, changeNum, comment)) {
            return changeStock(itemId, changeNum);
        }
        return false;
    }

    public boolean addItem(String name, int stock) {
        boolean flag = insertItem(name, stock);
        refreshItem();
        return flag;
    }

    public void refresh() {
//        setUpAllItems();
//        setUpAllAdmins();
        setUpAllTrans();
        pairTrans();
    }

    public void refreshItem() {
        setUpAllItems();
//        setUpAllAdmins();
//        setUpAllTrans();
//        pairTrans();
    }


}
