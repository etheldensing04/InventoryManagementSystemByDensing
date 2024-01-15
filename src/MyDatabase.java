import java.awt.Component;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class MyDatabase {
    private final String url = "jdbc:mysql://localhost:3306/inventory?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private final String username = "root";
    private final String password = "";
    
    private final String urlOnline = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12660721";
    private final String usernameOnline = "sql12660721";
    private final String passwordOnline = "UDY6pRSzzE";  
  
    protected static Connection connection;
    protected static Statement statement;
    protected static PreparedStatement prepare;   
    
    public static final String inventoryTable = "inventorytable";
    public static final String[] inventoryColumns = {"ProductID","Category","ProductName",
        "Description","Quantity","RetailPrice","DateOfPurchase"};
    
    public static final String categoryTable = "categorytable";
    public static final String[] categoryColumns = {"categoryID","categoryName","dateCreated"};
    
    public static final String usersTable = "userstable";
    public static final String[] usersColumns = {"userId","firstname","lastname","email","username","password",
        "birthdate","gender","profileImgPath"};
    
    public MyDatabase(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
            //this.connection = DriverManager.getConnection(urlOnline, usernameOnline, passwordOnline);
            this.statement = connection.createStatement();
        }catch(Exception e){
          
        }        
        
        System.out.println("");
    }
    
    public boolean isDatabaseConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Inventory Database Operation
    public DefaultTableModel DisplayInventoryData(){
        String query = "SELECT * FROM " + inventoryTable;

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Product ID","Category","Product Name","Description","Quantity","Retail Price","Date of Purchase"});
        try {
            ResultSet result = statement.executeQuery(query);
            ResultSetMetaData metaData = result.getMetaData();
            int numberOfColumns = metaData.getColumnCount();

            while (result.next()) {
                Object[] rowData = new Object[numberOfColumns];
                for (int i = 1; i <= numberOfColumns; i++) {
                    rowData[i - 1] = result.getObject(i);
                }
                model.addRow(rowData);
            }

            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }
    
    public void addInventoryValue(String[] values){
        String query = "INSERT INTO "+inventoryTable+
                " ("+inventoryColumns[1]+","+inventoryColumns[2]+","+inventoryColumns[3]+
                ","+inventoryColumns[4]+","+inventoryColumns[5]+","+inventoryColumns[6]+") VALUES (?,?,?,?,?,?)";
        
        try {
            prepare = connection.prepareStatement(query);
            prepare.setObject(1, values[0]);
            prepare.setObject(2, values[1]);
            prepare.setObject(3, values[2]); 
            prepare.setObject(4, values[3]); 
            prepare.setObject(5, values[4]); 
            prepare.setObject(6, values[5]);

            prepare.executeUpdate();
            prepare.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void EditInventoryValue(Object newVal, int colIdx, Object ID, Component parentComponent){
        String query = "UPDATE "+inventoryTable+" SET "+inventoryColumns[colIdx]+" = ? WHERE ("+inventoryColumns[0]+" = ?)";
        
        try{
            if(colIdx != 6){
                prepare = connection.prepareStatement(query);
                prepare.setObject(1, newVal);
                prepare.setObject(2, ID);
            }else{
                java.sql.Date sqlDate = java.sql.Date.valueOf(newVal.toString());
                prepare = connection.prepareStatement(query);
                prepare.setObject(1, sqlDate);
                prepare.setObject(2, ID);
                               
            }
            
            prepare.executeUpdate();
            prepare.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(parentComponent, "Something went wrong!");
        }
    }
    
    public void DeleteInventoryRecord(Object ID, Component parentComponent){
        String query = "DELETE FROM "+inventoryTable+" WHERE "+inventoryColumns[0]+" = ?";
        
        try{
            prepare = connection.prepareStatement(query);
            prepare.setObject(1, ID);
            
            prepare.executeUpdate();
            prepare.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(parentComponent, "Something went wrong!");
        }
    }
    
    //Category Database Operation
    public DefaultTableModel DisplayCategoryData(){
        String query = "SELECT * FROM " + categoryTable;

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Category ID","Category Name","Date Added"});
        try {
            ResultSet result = statement.executeQuery(query);
            ResultSetMetaData metaData = result.getMetaData();
            int numberOfColumns = metaData.getColumnCount();

            while (result.next()) {
                Object[] rowData = new Object[numberOfColumns];
                for (int i = 1; i <= numberOfColumns; i++) {
                    rowData[i - 1] = result.getObject(i);
                }
                model.addRow(rowData);
            }

            result.close();
        } catch (SQLException e) {
        
        }
        return model;
    }
    
    public void addCategoryValue(String[] values, Component parentComponent){
        String query = "INSERT INTO "+categoryTable+" ("+categoryColumns[1]+","+categoryColumns[2]+") VALUES (?,?)";
       
        try{
            prepare = connection.prepareStatement(query);
            prepare.setObject(1, values[0]);
            prepare.setObject(2, values[1]);
            
            prepare.executeUpdate();
            prepare.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(parentComponent, "Something went wrong!");
        }
    }
    
    public void EditCategoryValue(Object newVal, int colIdx, Object ID,Component parentComponent){
        String query = "UPDATE "+categoryTable+" SET "+categoryColumns[colIdx]+" = ? WHERE ("+categoryColumns[0]+" = ?)";
        
        try{
            if(colIdx != 3){
                prepare = connection.prepareStatement(query);
                prepare.setObject(1, newVal);
                prepare.setObject(2, ID);
            }else{
                java.sql.Date sqlDate = java.sql.Date.valueOf(newVal.toString());
                prepare = connection.prepareStatement(query);
                prepare.setObject(1, sqlDate);
                prepare.setObject(2, ID);
                               
            }
            
            prepare.executeUpdate();
            prepare.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(parentComponent, "Something went wrong!");
        }
    }
    
    public void DeleteCategoryRecord(Object ID,Component parentComponent){
        String query = "DELETE FROM "+categoryTable+" WHERE "+categoryColumns[0]+" = ?";
        
        try{
            prepare = connection.prepareStatement(query);
            prepare.setObject(1, ID);
            
            prepare.executeUpdate();
            prepare.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(parentComponent, "Something went wrong!");
        }
    }
    
    public String[] AddElementToComboBox(){
        String query = "SELECT " + categoryColumns[1] + " FROM " + categoryTable;
        
        List<String> getVal = new ArrayList<>();
        
        try {
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                getVal.add(result.getString(categoryColumns[1]));
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return getVal.toArray(new String[0]);
    }    
    
    //-----------------------------------------------------------------------------//
    
    public boolean isValueExists(String valueToCheck, String column,String table){
        boolean exist = false;
        String query = "SELECT * FROM "+table+" WHERE "+column+" = LOWER(?)";
        try{
            prepare = connection.prepareStatement(query);
            prepare.setString(1, valueToCheck.toLowerCase());
            
            ResultSet result = prepare.executeQuery();
            
            if(result.next()){
                exist = true;
            }
            
            prepare.close();
            result.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        return exist;
    }

    public void loadInventoryData(javax.swing.JTable table, String search) {
        try{
            String query = "SELECT * FROM "+inventoryTable+" WHERE "
                    + String.join(" LIKE ? OR ", inventoryColumns) + " LIKE ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (int i = 1; i <= inventoryColumns.length; i++) {
                    preparedStatement.setString(i, "%" + search + "%");
                }

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
                    model.setRowCount(0);

                    while (resultSet.next()) {
                        Object[] row = new Object[inventoryColumns.length];
                        for (int i = 0; i < inventoryColumns.length; i++) {
                            row[i] = resultSet.getObject(inventoryColumns[i]);
                        }
                        model.addRow(row);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void loadCategoryData(javax.swing.JTable table, String search) {
        try{
            String query = "SELECT * FROM " + categoryTable + " WHERE "
                    + String.join(" LIKE ? OR ", categoryColumns) + " LIKE ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (int i = 1; i <= categoryColumns.length; i++) {
                    preparedStatement.setString(i, "%" + search + "%");
                }

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
                    model.setRowCount(0);

                    while (resultSet.next()) {
                        Object[] row = new Object[categoryColumns.length];
                        for (int i = 0; i < categoryColumns.length; i++) {
                            row[i] = resultSet.getObject(categoryColumns[i]);
                        }
                        model.addRow(row);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int countProducts(){
        String query = "SELECT COUNT(*) FROM " + inventoryTable;
        int count = 0;
        try {
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
               count = result.getInt(1);
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    public static boolean isUserExist(String u_n, String u_p) {
        boolean userExists = false;
        String query = "SELECT * FROM "+ usersTable;
        
        try{

            prepare = connection.prepareStatement(query);
            ResultSet result = prepare.executeQuery();

            while (result.next()) {
                String username = result.getString(usersColumns[4]);
                String password = result.getString(usersColumns[5]);

                if (u_n.equals(username) && u_p.equals(password)) {
                    userExists = true;
                    break;
                }
            }

            prepare.close();
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userExists;
    }
    
    public static String getImagePath(int _id){
        String query = "SELECT "+usersColumns[8]+" FROM "+usersTable+" WHERE "
                + usersColumns[0] + " = ?";

        try{
            prepare = connection.prepareStatement(query);
            prepare.setInt(1, _id);

            ResultSet r_t = prepare.executeQuery();

            if(r_t.next()){
                return r_t.getString(usersColumns[8]);
            }

            prepare.close();
            r_t.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
}
