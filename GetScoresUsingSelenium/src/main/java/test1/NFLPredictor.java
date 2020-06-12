package test1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class NFLPredictor {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "my-secret-pw";

    public static ArrayList getListOfTeamsFromSchedule() throws Exception {
        ArrayList<String> list = new ArrayList<String>();

        // Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Open a connection
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

        // Execute a query
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT teamName FROM TestNFL.nflSchedule ORDER BY teamName ASC");

        // Extract data from result set
        while(rs.next()){
            String teamName = rs.getString("teamName");
            list.add(teamName);
        }
        // Clean-up environment
        rs.close();
        stmt.close();
        conn.close();

        return list;
    }

    public static ArrayList getOpponentGivenTeamName(String team) throws Exception{
        ArrayList<String> list = new ArrayList<String>();
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

        for (int i=0; i<17; i++){
        // Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Open a connection
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // Execute a query
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT teamName, " + week.get(i) + " from TestNFL.nflSchedule where teamName = '" + team + "'");

            // Extract data from result set
            while (rs.next()) {
                String opponent = rs.getString(week.get(i));
                list.add(opponent);
            }

            // Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }
        return list;
    }

    public static void insertScoreIntoDatabaseGivenTeamAndWeek(int weekNumberMinusOne, int predictedScore, int predictedOpponentScore, String team, String opponentTeam) throws Exception {
        ArrayList<String> list = new ArrayList<String>();
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

        // Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Open a connection
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

        // Execute a query
        Statement updateYourTeam = conn.createStatement();
        updateYourTeam.executeUpdate("UPDATE TestNFL." + week.get(weekNumberMinusOne) + " SET predictedTeamScore = " + predictedScore + ", predictedOpponentScore = " + predictedOpponentScore + " WHERE teamName = '" + team + "'");

        Statement updateOpponentTeam = conn.createStatement();
        updateOpponentTeam.executeUpdate("UPDATE TestNFL." + week.get(weekNumberMinusOne) + " SET predictedTeamScore = " + predictedOpponentScore + ", predictedOpponentScore = " + predictedScore + " WHERE teamName = '" + opponentTeam + "'");

        // Clean-up environment
        updateYourTeam.close();
        updateOpponentTeam.close();
        conn.close();
    }
}
