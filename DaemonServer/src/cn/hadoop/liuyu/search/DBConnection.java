package cn.hadoop.liuyu.search;

import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.ResultSet;  
import java.sql.Statement;  
import java.util.ArrayList;  

/**
 * @function 连接mysql数据库，对表进行查询操作
 * @author liuyu
 *
 */
public class DBConnection {  
      
    /** 
     * 驱动类名称 
     */  
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";  
      
    /** 
     * 数据库连接字符串 
     */  
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/searchServerDB";  
      
    /** 
     * 数据库用户名 
     */  
    private static final String USER_NAME = "root";  
      
    /** 
     * 数据库密码 
     */  
    private static final String PASSWORD = "12035318";  
      
    /** 
     * 数据库连接类 
     */  
    private static Connection conn;  
      
    /** 
     * 数据库操作类 
     */  
    private static Statement stmt;  
      
      
      
    // 加载驱动  
    static{  
        try {  
            Class.forName(DRIVER_CLASS);  
        } catch (Exception e) {  
            System.out.println("加载驱动错误");  
            System.out.println(e.getMessage());  
        }  
    }  
      
    // 取得连接  
    private static Connection getConnection(){  
          
        try {  
            conn = DriverManager.getConnection(DATABASE_URL, USER_NAME, PASSWORD);  
        } catch (Exception e) {  
            System.out.println("取得连接错误");  
            System.out.println(e.getMessage());  
        }  
        return conn;  
    }  
         
    //查询数据库并返回list
    public ArrayList<String> getList(String sql){  
        ArrayList<String> list = null;  
          
        // 取得数据库操作对象  
        try {   
            stmt = getConnection().createStatement();  
        } catch (Exception e) {  
            System.out.println("statement取得错误");  
            System.out.println(e.getMessage());  
            return null;  
        }  
          
        try {  
        	list = new ArrayList<String>(); 
            // 查询数据库对象,返回记录集(结果集)  
            ResultSet rs = stmt.executeQuery(sql);  
              
            // 循环记录集，查看每一行每一列的记录  
            while (rs.next()) {  
                String keyword = rs.getString(1);  
                list.add(keyword);  
            }  
              
        } catch (Exception e) {  
            System.out.println(e.getMessage());  
        }  
        return list;  
    }  
}  
