package ca.laurentian.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCUtil {
    public static String driverClass = null;
    public static String url = null;
    public static String name = null;
    public static String password = null;

    static {
        try {
            // 1. create a Properties instance
            Properties properties = new Properties();
            // 2. use classloader to read the data in src folder(this time we can not use ServletContext)
            // InputStream is = JDBCUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
            // this is not JavaEE environment anymore, so we should use different way to get resource file
            InputStream is = new FileInputStream("jdbc.properties");
            // 3. load InputStream into "properties"
            properties.load(is);
            // 4. read properties
            driverClass = properties.getProperty("driverClass");
            url = properties.getProperty("url");
            name = properties.getProperty("name");
            password = properties.getProperty("password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets Connection instance "conn".
     *
     * @return
     */
    public static Connection getConn() {
        Connection conn = null;
        try {
            // before calling the getConnection method of DriverManager,
            // make sure that the corresponding Driver class has been loaded into the JVM,
            // and the initialization of the class has been completed.
            // driverClass = com.mysql.jdbc.Driver
            // when a Driver class is loaded, it should create an instance of itself and register it with the DriverManager
            Class.forName(driverClass);
            // creat a conn need 3 parameters
            // param1: protocol://database ip
            // param2: username
            // param3: password
            conn = DriverManager.getConnection(url, name, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Releases resource (Statement, ResultSet and Connection).
     *
     * @param conn
     * @param st
     * @param rs
     */
    public static void release(Connection conn, Statement st, ResultSet rs) {
        closeRs(rs);
        closeSt(st);
        closeConn(conn);
    }

    /**
     * Releases resource (Statement and Connection).
     *
     * @param conn
     * @param st
     */
    public static void release(Connection conn, Statement st) {
        closeSt(st);
        closeConn(conn);
    }

    /*
    "conn" is an object stands for the connection with specific databases.
     */
    private static void closeConn(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn = null;
        }
    }

    /*
    Statement is an important class for Java to perform database operations.
    It is used to send SQL statements to the database on the basis of established database connections.
     */
    private static void closeSt(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            st = null;
        }
    }

    /*
    JDBC provides us with the ResultSet interface to specialize in processing query result sets.
    In fact, the data queried is not in the ResultSet, but still in the database.
    The next () method in the ResultSet is similar to a pointer, pointing to the result of the query, and then traversing continuously.
    So this requires that the "conn" cannot be disconnected.
    Gets ResultSet instance via Statement instance: ResultSet executeQuery(String sql) throws SQLException.
     */
    private static void closeRs(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs = null;
        }
    }
}
