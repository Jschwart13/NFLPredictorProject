package test1;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NFLPredictor {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "my-secret-pw";

    public static ArrayList pickYourTeam() throws Exception {

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

        ResultSet rs = stmt.executeQuery("SELECT teamName FROM TestNFL.nflSchedule ORDER BY teamName ASC");

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

    public static ArrayList OpponentsGivenTeam(String team) throws Exception{
        Connection conn = null;
        Statement stmt = null;
        String opponent = null;
        ArrayList<String> week = new ArrayList<String>();

        week.add("weekOne");
        week.add("weekTwo");
        week.add("weekThree");
        week.add("weekFour");
        week.add("weekFive");
        week.add("weekSix");
        week.add("weekSeven");
        week.add("weekEight");
        week.add("weekNine");
        week.add("weekTen");
        week.add("weekEleven");
        week.add("weekTwelve");
        week.add("weekThirteen");
        week.add("weekFourteen");
        week.add("weekFifteen");
        week.add("weekSixteen");
        week.add("weekSeventeen");

        ArrayList<String> list = new ArrayList<String>();

        for (int i=0; i<17; i++){
        // Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Open a connection
        conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // Execute a query
            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT teamName, " + week.get(i) + " from TestNFL.nflSchedule where teamName = '" + team + "'");

            // Extract data from result set
            while (rs.next()) {
                opponent = rs.getString(week.get(i));
                list.add(opponent);
            }

            // Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }
        return list;
    }
}
