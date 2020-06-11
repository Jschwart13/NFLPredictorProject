package test1;

import java.sql.*;
import java.util.ArrayList;

public class GetAllTheScores {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "my-secret-pw";

    public static ArrayList getListOfNFLTeams() throws Exception{

        Connection conn = null;
        Statement stmt = null;
        String teamName = null;
        ArrayList<String> list = new ArrayList<String>();

        // Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Open a connection
        conn = DriverManager.getConnection(DB_URL,USER,PASS);

        // Execute a query
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT teamName FROM TestNFL.acceptableTeams ORDER BY teamName ASC");

        // Extract data from result set
        while(rs.next()){
            teamName = rs.getString("teamName");
            list.add(teamName);
        }
        // Clean-up environment
        rs.close();
        stmt.close();
        conn.close();

        return list;
    }

}

