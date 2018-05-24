


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.JTextComponent;
import static javax.swing.JOptionPane.*;

/**
 * @desc A singleton database access class for MySQL
 * Developed by Khalil Abdeljawad
 * 
 */
 
 
public final class MySQL {

    String host = "localhost";
    //String url = "jdbc:mysql://localhost:3306/";
    String db = "db";
    String driver = "com.mysql.jdbc.Driver";
    String user = "root";//"giga_test";
    String password = "root";


    public static Connection conn;
    private Statement statement;
    public static MySQL dbcon;
    private String fields[] = null;
    private String table = null;

    public MySQL() {

        try {
            Class.forName(driver).newInstance();
/*
            try {
                FileReader file = new FileReader("conf.txt");
                BufferedReader is = new BufferedReader((file));
                String inputLine;
                while ((inputLine = is.readLine()) != null) {
                    host = inputLine;
                    System.out.println(inputLine);
                    break;
                }
                is.close();
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }
*/
            this.conn = (Connection) DriverManager.getConnection("jdbc:mysql://" + host + "/" + db + "?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true", user, password);

            this.print(new String[]{"","",""});
// 	    this.update("set character_set_server='utf8'");
//            this.update("set names 'utf8'");
        } catch (Exception sqle) {
            sqle.printStackTrace();
        }
    }

    public MySQL(String host, String user, String password, String db) {

        this.host = host;
        this.user = user;
        this.password = password;
        this.db = db;

        try {
            Class.forName(driver).newInstance();

            this.conn = (Connection) DriverManager.getConnection("jdbc:mysql://" + host + "/" + db + "?useUnicode=true&characterEncoding=UTF-8", user, password);
            //System.out.println(this.conn.toString());
            dbcon = this;
// 	    this.update("set character_set_server='utf8'");
//            this.update("set names 'utf8'");
        } catch (Exception sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     *
     * @return MysqlConnect Database connection object
     */
    public static synchronized MySQL Connect() {
        
         if (dbcon == null) {
            dbcon = new MySQL();
        }
         
        
       
        
        if (dbcon == null) {
            dbcon = new MySQL();
        }

        return dbcon;

    }

    public void setTable(String table) {

        try {
            if (table != null) {
                this.table = table;
            }
        } catch (Exception ex) {
            System.err.print("Error in MySQL.setTable  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");

        }
    }

    private void testTable() throws Exception {

        if (table == null) {
            throw new Exception("MySQL.table is null, assign name of one of the database tables to MySQL.table, use MySQL.setTable()");
        }

    }

    /**
     *
     * @param query String The query to be executed
     * @return a ResultSet object containing the results or null if not
     * available
     * @throws SQLException
     */
    public ResultSet query(String query) {
        try {
            statement = MySQL.conn.createStatement();
            ResultSet res = statement.executeQuery(query);
            return res;
        } catch (SQLException ex) {
            System.out.print("Error in MySQL.query  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.out.println(ex.getMessage());
            System.out.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");

            //Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param query String The query to be executed
     * @return a ResultSet object containing the results or null if not
     * available
     * @throws SQLException
     */
    public boolean execute(String query) {
        try {
            statement = dbcon.conn.createStatement();
            statement.executeUpdate(query);
            return true;

        } catch (SQLException ex) {
            System.err.print("Error in MySQL.execute method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + ": ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");
return false;
            //Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param query String The query to be executed
     * @return a 2d String array containing the results or null if not available
     */
    public String[][] select(String query) {
        //System.out.println(query);
        try {
            String[][] sarr = null;

            statement = dbcon.conn.createStatement();
            ResultSet res = statement.executeQuery(query);
            int rows = 0, cols = 0;
            while (res.next()) {
                rows++;
                cols = res.getMetaData().getColumnCount();

            }

            fields = new String[cols];
            for (int i = 0; i < cols; i++) {
                fields[i] = res.getMetaData().getColumnName(i + 1);
            }
            sarr = new String[rows][cols];
            res.beforeFirst();
            int i = 0;
            while (res.next()) {
                for (int j = 0; j < cols; j++) {
                    sarr[i][j] = res.getString(j + 1);
                }
                i++;
            }
            return sarr;
        } catch (SQLException ex) {
            System.err.print("Error in MySQL.select  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");
            //Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }




    //JDBC Result set to Java HashMap

    public List<Map<String, String>> selectListMap(String query) {
        List<Map<String, String>> data = null;
        try {

            Statement stmt = dbcon.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            List<String> columns = new ArrayList<String>(rsmd.getColumnCount());
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                columns.add(rsmd.getColumnName(i));
            }
            data = new ArrayList<Map<String, String>>();
            while (rs.next()) {
                Map<String, String> row = new HashMap<String, String>(columns.size());
                for (String col : columns) {
                    row.put(col, rs.getString(col));
                }
                data.add(row);
            }
            System.out.println(data);
            rs.close();
            stmt.close();
            //con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return data;
    }


    public HashMap<String, String> selectRowMap(String query) {

        HashMap<String, String> row = null;

        try {

            Statement stmt = dbcon.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            List<String> columns = new ArrayList<String>(rsmd.getColumnCount());
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                columns.add(rsmd.getColumnName(i));
            }

            rs.next();


                row = new HashMap<>(columns.size());
                for (String col : columns) {
                    row.put(col, rs.getString(col));
                }



            rs.close();
            stmt.close();
            //con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return row;
    }
    /**
     *
     * @param query String The query to be executed
     * @return a 2d String array containing the results or null if not available
     */
    public Object[][] selectObjs(String query) {
        System.out.println(query);
        try {
            Object[][] sarr = null;

            statement = dbcon.conn.createStatement();
            ResultSet res = statement.executeQuery(query);
            int rows = 0, cols = 0;
            while (res.next()) {
                rows++;
                cols = res.getMetaData().getColumnCount();

            }

            fields = new String[cols];
            for (int i = 0; i < cols; i++) {
                fields[i] = res.getMetaData().getColumnName(i + 1);
            }
            sarr = new Object[rows][cols];
            res.beforeFirst();
            int i = 0;
            while (res.next()) {
                for (int j = 0; j < cols; j++) {
                    sarr[i][j] = res.getObject(j + 1);
                }
                i++;
            }
            return sarr;
        } catch (SQLException ex) {
            System.err.print("Error in MySQL.select  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");
            //Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * 
     * @param query
     * @param idCol
     * @param nameCol
     * @return the result of query in map
     */
    public Map<String, String> selectMap(String query, int idCol, int nameCol){
        String[][] res = this.select(query);
       // print(res);
        
        if(res.length<1){
            System.out.println("Error in query at selectMap");
            return null;
        }
                    
        Map<String, String> map = new HashMap<>();
        
        for (int i = 0; i < res.length; i++) {
            
            map.put(res[i][nameCol], res[i][idCol]);
            
        }
        
        return map;
    }
    
    
    public String[][] select(String query, int limit) {
        try {
            statement = dbcon.conn.createStatement();
            ResultSet res = statement.executeQuery(query + "LIMIT" + limit);
            int rows = 0, cols = 0;
            while (res.next()) {
                rows++;
                cols = res.getMetaData().getColumnCount();

            }
            String[][] sarr = new String[rows][cols];
            res.beforeFirst();
            int i = 0;
            while (res.next()) {
                for (int j = 0; j < cols; j++) {
                    sarr[i][j] = res.getString(j + 1);
                }
                i++;
            }
            return sarr;
        } catch (SQLException ex) {
            System.err.print("Error in MySQL.select  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");
            //Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String[][] select(String query, int limit_begin, int limit_end) {
        try {
            statement = MySQL.conn.createStatement();
            ResultSet res = statement.executeQuery(query + "LIMIT" + limit_begin + ", " + limit_end);
            int rows = 0, cols = 0;
            while (res.next()) {
                rows++;
                cols = res.getMetaData().getColumnCount();

            }
            String[][] sarr = new String[rows][cols];
            res.beforeFirst();
            int i = 0;
            while (res.next()) {
                for (int j = 0; j < cols; j++) {
                    sarr[i][j] = res.getString(j + 1);
                }
                i++;
            }
            return sarr;
        } catch (SQLException ex) {
            System.err.print("Error in MySQL.select  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");
            //Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Select all from table (which is stored in mysql.table)
     *
     * @return
     */
    public String[][] select() {
        try {
            testTable();
            statement = MySQL.conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM " + this.table + " LIMIT 100");
            int rows = 0, cols = 0;
            while (res.next()) {
                rows++;
                cols = res.getMetaData().getColumnCount();

            }
            String[][] sarr = new String[rows][cols];
            res.beforeFirst();
            int i = 0;
            while (res.next()) {
                for (int j = 0; j < cols; j++) {
                    sarr[i][j] = res.getString(j + 1);
                }
                i++;
            }
            return sarr;
        } catch (SQLException ex) {
            System.err.print("Error in MySQL.select  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");
            //Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String[][] select(int limit_begin, int limit_end) {
        try {
            statement = dbcon.conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM " + this.table + " LIMIT" + limit_begin + ", " + limit_end);
            int rows = 0, cols = 0;
            while (res.next()) {
                rows++;
                cols = res.getMetaData().getColumnCount();

            }
            String[][] sarr = new String[rows][cols];
            res.beforeFirst();
            int i = 0;
            while (res.next()) {
                for (int j = 0; j < cols; j++) {
                    sarr[i][j] = res.getString(j + 1);
                }
                i++;
            }
            return sarr;
        } catch (SQLException ex) {
            System.err.print("Error in MySQL.select  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");
            //Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String[][] select(int limit) {
        try {
            statement = dbcon.conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM " + this.table + " LIMIT" + limit);
            int rows = 0, cols = 0;
            while (res.next()) {
                rows++;
                cols = res.getMetaData().getColumnCount();

            }
            String[][] sarr = new String[rows][cols];
            res.beforeFirst();
            int i = 0;
            while (res.next()) {
                for (int j = 0; j < cols; j++) {
                    sarr[i][j] = res.getString(j + 1);
                }
                i++;
            }
            return sarr;
        } catch (SQLException ex) {
            System.err.print("Error in MySQL.select  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");
            //Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String selectField(String query) {
        //System.out.println(query);
        try {
            statement = dbcon.conn.createStatement();
            ResultSet res = statement.executeQuery(query);
            int rows = 0, cols = 0;
            while (res.next()) {
                return res.getString(1);
            }
        } catch (SQLException ex) {
            System.err.print("Error in MySQL.selectField  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");
            //Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String[] selectRow(String query) {
        //System.out.println(query);
        try {
            statement = dbcon.conn.createStatement();
            ResultSet res = statement.executeQuery(query );
            int rows = 0, cols = 0;
            while (res.next()) {

                cols = res.getMetaData().getColumnCount();

            }

            if (cols == 0) {
                return null;
            }
            String[] sarr = new String[cols];

            res.beforeFirst();
            int i = 0;
            while (res.next()) {
                for (int j = 0; j < cols; j++) {
                    sarr[j] = res.getString(j + 1);
                }

                return sarr;
            }
            return sarr;
        } catch (SQLException ex) {
            System.err.print("Error in MySQL.selectField  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");

            //Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * @desc Method to insert data to a table
     * @param query String The Insert query
     * @return boolean
     * @throws SQLException
     */
    public boolean insert(String query) {

        try {
            statement = dbcon.conn.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            System.err.print("Error in MySQL.insert  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");
            System.out.println(query);
            //System.out.println("In MySQL.java:insert()\n" + ex.getMessage());
            return false;
        }

    }

    public boolean insert(String fields, String values) {

        String query = "INSERT INTO " + this.table + "(" + fields + ") values (" + values + ")";
        try {
            statement = dbcon.conn.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            System.err.print("Error in MySQL.insert  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");
            return false;
        }

    }

    public boolean insert(String... values) {
        String vals = linkValues(values, "'");
        String[] fields = this.getFieldsNames(this.table);
        String query = "INSERT INTO " + this.table + "(" + linkValues(fields, "`") + ") values (" + vals + ")";
        try {
            statement = dbcon.conn.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            System.err.print("Error in MySQL.insert  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");
            System.out.println(query);
            return false;
        }

    }

    
    public boolean insert(JTextComponent... jtexts) {
        
        String[] values = new String[jtexts.length];
        int index = 0;
        for (JTextComponent jtext : jtexts) {
            values[index++] = jtext.getText();
        }
        String vals = linkValues(values, "'");
        String[] fields = this.getFieldsNames(this.table);
        String query = "INSERT INTO " + this.table + "(" + linkValues(fields, "`") + ") values (" + vals + ")";
        try {
            statement = dbcon.conn.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            System.err.print("Error in MySQL.insert  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " at line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + "\nFrom ");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");
            //System.out.println(query);
            return false;
        }

    }

    
    /**
     *
     * @param query
     * @return
     */
    public boolean update(String query) {
        //System.out.println(query);
        try {
            statement = MySQL.conn.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            ////Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println(query);
            System.out.println("In MySQL.java:update()");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");

        }
        return false;
    }

    /**
     * 
     * @param where : Condition in SQL query (where = `id=value`)
     * @param textFields: text fields from swing
     * @return boolean state depending on updating success
     */
    public boolean update(String where, JTextComponent ... textFields) {
        
       // String vals = linkValues(JTextToStrArr(textFields), "'");
        String[] fields = this.getFieldsNames(this.table);
        String query = "update " + this.table + " set " + linkFiledsAndValues(fields, getJTexts(textFields)) + " WHERE "+where;
        //System.out.println(query);

        try {
            statement = dbcon.conn.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            ////Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println(query);
            System.out.println("In MySQL.java:update()");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");

        }
        return false;
    }

    
    /**
     * 
     * @param where : Condition in SQL query (where => [id=value])
     * @param fields_values: fields to be update and their values (update table where [field1 = value1, field2 = value2 ])
     * @return boolean state depending on updating success
     */
    public boolean update(String where, String ... fields_values) {
        
        String[] fields = this.getFieldsNames(this.table);
        String query = "update " + this.table + " set " + linkFiledsAndValues(fields, fields_values) + " WHERE "+where;
        //System.out.println(query);

        try {
            statement = dbcon.conn.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            ////Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println(query);
            System.out.println("In MySQL.java:update()");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");

        }
        return false;
        
        /*
        String fields = linkValues(fields_values);
        //String[] fields = this.getFieldsNames(this.table);
        //String query = "INSERT INTO " + this.table + "(" + linkValues(fields, "`") + ") values (" + vals + ")";
        String query = "update " + this.table + " set " + fields + " WHERE "+where;
        //System.out.println(query);

        try {
            statement = dbcon.conn.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            ////Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println(query);
            System.out.println("In MySQL.java:update()");
            System.err.println(ex.getMessage());
            System.err.println("From " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + "  method in file " + Thread.currentThread().getStackTrace()[1].getFileName() + " " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " ");

        }
        return false;
        */
    }
    
    
    
    
    public void close() {
        try {
            this.conn.close();

        } catch (SQLException ex) {

            System.err.println("Failed to Close MySQL Connection.");
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @return 
     */
    public String[] getFieldsNames(String table) {
        
        this.table = table;
        
            
        try {

            return (this.getCol(this.select("desc " + table), 0));
        } catch (Exception ex) {
        }
        return null;
    }

    
    public String[] getTables(){
        
        String tables[] = this.getCol(this.select("show tables"), 0);
        
        return tables;
        
    }
    
    /**
     * 
     * 
     * @return 
     */
    public String[] getFieldsNames() {
        
        
            
        try {

            return (this.getCol(this.select("desc " + table), 0));
        } catch (Exception ex) {
        }
        return null;
    }
    
    
    // Utilities Methods:
    
    /**
     * Print out String[]
     * @param star 
     */
    public void print(String[] star) {
        for (String str : star) {
            System.out.print(str + " ");
        }
        System.out.println();
    }

    /**+
     * Print out the String[][]
     * @param star 
     */
    public void print(String[][] star) {
        for (String col : fields) {
            System.out.printf("%20s", col);
        }
        System.out.println();
        for (String[] starr : star) {
            for (String str : starr) {
                System.out.printf("%20s", str);
                // System.out.print(str + "");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * To combine two String[][]s one by one
     * @param fields: String[]
     * @param values: String[]
     * @return String: a series from files = values [field1 = value1, filed2 = value2 ...]
     */
    public String linkFiledsAndValues(String[] fields, String[] values ){
        
        if(fields.length!=values.length){
            System.err.println("Error: Size of fields != size of values.");
            return null;
        }
        String ret = "";
        
        for (int i = 0; i < fields.length; i++) {
            ret+= fields[i]+" = '"+values[i]+"'";
            if(i!=fields.length-1)ret+=",";
        }
        return ret;
    }
    
    /**
     * Link String[] elements with chosen string 
     * @param values: String[]
     * @param c: String
     * @return String: str = c+values[0]+c, c+values[1]+c, ...
     */
    public String linkValues(String[] values, String c) {

        String str = "";
        for (String st : values) {
            str += c + st + c + ",";
        }

        //System.out.println(str.length() + str);
        str = str.substring(0, str.length() - 1);
        return str;
    }
    
    /**
     * Link String array elements with ','
     * @param values: String[]
     * @return values[0],values[1],...
     */
    public String linkValues(String[] values) {

        String str = "";
        for (String st : values) {
            str +=  st + ",";
        }

        //System.out.println(str.length() + str);
        str = str.substring(0, str.length() - 1);
        return str;
    }

    
    /**
     * Choose a 1 column from String[][]
     * @param values: String[][]: contains a table(cols & rows)
     * @param col: int : the number of the column in values
     * @return String[] retart 
     */
    public String[] getCol(String[][] values, int col) {
        String[] retar = new String[values.length];
        int index = 0;
        for (String[] starr : values) {
            retar[index++] = starr[col];

        }
        return retar;
    }
    
    
    
    
    
    
    /**
     * 
     * @param textFields: the text fields in the gui frame
     * @return String[] star: contains the texts in textFiles; 
     */
    String[] getJTexts(JTextComponent[] textFields){
        if(textFields.length<1) return null;
        
        String[] star = new String[textFields.length];
        int index = 0;
        for(JTextComponent textField: textFields){
            star[index++] = textField.getText();
        }
        return star;
    }
    
    /**
     * Sets values[] in the textFields sequentially
     * @param values: String[]
     * @param textFields: JTextComponents
     * @return true
     * 
     */
    public boolean setJTexts(String[] values, JTextComponent ... textFields){
        if(textFields.length<1 || textFields.length!=values.length) return false;
        
        String[] star = new String[textFields.length];
        int index = 0;
        for(JTextComponent textField: textFields){
            textField.setText(values[index++]);
            //star[index++] = textField.getTex);
        }
        return true;
    }
    
    
    /* see it deeply
    public static void genereateTables(String packageName){
        
        
         // Create Tables.java file: 
         
        String directory = new File("").getAbsolutePath()+"/src/"+packageName;
       
        
        
      //  System.out.println();
        try {
            
            BufferedWriter bf = new BufferedWriter(new FileWriter(directory+"Tables.java"));
            bf.write("\n"+new  ly.ltnet.FinancailArchiveSystem.adminInterface.Main().getClass().getPackage()+";\n");
            bf.write("public class Tables{\n");
            
            String[] tables = dbcon.getTables();
            for (String table : tables) {
                bf.write("\n\tpublic static final String "+table.toUpperCase()+" = \""+table+"\";");
            }
            
            
            bf.write("\n}");
            
            bf.close();
        } catch (IOException ex) {
            showMessageDialog(null, ex.getMessage());
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    

    
    public static void genereateTablesFields(String packageName){
        
        
         // Create Tables.java file: 
         
        String directory = new File("").getAbsolutePath()+"/src/"+packageName;
       
        
        
      //  System.out.println();
        try {
            
            BufferedWriter bf = new BufferedWriter(new FileWriter(directory+"Fields.java"));
            bf.write("\n"+new  ly.ltnet.FinancailArchiveSystem.adminInterface.Main().getClass().getPackage()+";\n");
            bf.write("public class Tables{\n");
            
            String[] tables = dbcon.getTables();
            for (String table : tables) {
                bf.write("\n\tpublic static final String "+table.toUpperCase()+" = \""+table+"\";");
            }
            
            
            bf.write("\n}");
            
            bf.close();
        } catch (IOException ex) {
            showMessageDialog(null, ex.getMessage());
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    */

}
