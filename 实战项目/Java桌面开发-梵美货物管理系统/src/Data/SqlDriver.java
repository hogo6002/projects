package Data;

import java.sql.*;

class SqlDriver {
    private static Connection myConn;
    public final static String NOTFOUND = "NOT FOUND";
    private static Statement myStmt;

    //5935b81f9595
    public static void setUp() {
        try {
            myConn = DriverManager.getConnection("jdbc:mysql" +
                    "://148.72.203.146:3306/fanmei?useUnicode=true&characterEncoding=UTF-8", "holly", "gongwen0147");
            myStmt = myConn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String selectAllItems() {
        String info = "";
        setUp();
        try {
            ResultSet myRs = myStmt.executeQuery("select * from Item");

            while (myRs.next()) {
                info += myRs.getString("id") + ":" + myRs.getString("name") + ":"
                        + myRs.getString("stock") + "||";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

    public static String selectItem(int id) {
        String info = "";
        setUp();
        try {
            ResultSet myRs = myStmt.executeQuery("select * from Item where " +
                    "id = " + id + "");

            if (myRs.next()) {
                info += myRs.getString("id") + ":" + myRs.getString("name") + ":"
                        + myRs.getString("stock");
            } else {
                info = NOTFOUND;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

    public static boolean updateName(int id, String newName) {
        setUp();
        try {
            ResultSet myRs = myStmt.executeQuery("select * from Item where " +
                    "id = " + id + "");
            if (!myRs.next()) {
                return false;
            }
            int updateRs = myStmt.executeUpdate("UPDATE Item SET name = '" + newName + "' Where id = " + id + "");
            if (updateRs != 1) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean updateStock(int id, int stock) {
        setUp();
        try {
            ResultSet myRs = myStmt.executeQuery("select * from Item where " +
                    "id = " + id + "");
            if (!myRs.next()) {
                return false;
            }
            stock += Integer.parseInt(myRs.getString("stock"));
            if (stock < 0) {
                stock = 0;
            }
            int updateRs =
                    myStmt.executeUpdate("UPDATE Item SET stock = " + stock +
                            " Where id = " + id + "");
            if (updateRs != 1) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String selectAllTrans() {
        String info = "";
        setUp();
        try {
            ResultSet myRs = myStmt.executeQuery("select * from Transaction");

            while (myRs.next()) {
                info += myRs.getString("id") + "^" + myRs.getString("date") + "^"
                        + myRs.getString("item_id") + "^" + myRs.getString(
                                "admin_id") + "^" + myRs.getString(
                        "change_number") + "^" + myRs.getString(
                        "comment") + "||";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

    public static String selectAllAdmins() {
        String info = "";
        setUp();
        try {
            ResultSet myRs = myStmt.executeQuery("select * from Admin");

            while (myRs.next()) {
                info += myRs.getString("id") + ":" + myRs.getString("name") + "||";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

    public static boolean insertTrans(EasyDate date, int itemId, int adminId,
                                      int changeNum, String comment) {
        setUp();
        try {
            int updateRs =
                    myStmt.executeUpdate("insert INTO Transaction VALUES " +
                            "(NULL, '" + date + "', " + itemId + ", " + adminId + ", " + changeNum + ", '" + comment +
                                    "')");
            if (updateRs != 1) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean insertItem(String name, int stock) {
        setUp();
        try {
            int updateRs =
                    myStmt.executeUpdate("insert INTO Item VALUES " +
                            "(NULL, '" + name
                            + "', " + stock + ")");
            if (updateRs != 1) {
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
