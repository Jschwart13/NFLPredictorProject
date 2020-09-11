package test1;

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

    public static ArrayList getListOfTeamsFromNFL() throws Exception {
        ArrayList<String> list = new ArrayList<String>();

        // Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Open a connection
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

        // Execute a query
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT teamName FROM TestNFL.listOfTeamsBrokenUp ORDER BY teamName ASC");

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

    public static ArrayList getListOfTeamsIHaventPickedYet() throws Exception {
        ArrayList<String> list = new ArrayList<String>();

        // Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Open a connection
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

        // Execute a query
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT DISTINCT teamName FROM TestNFL.results where predictedResult is NULL ORDER BY teamName ASC");

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

    public static ArrayList getListOfTeamsFromSection(String divisionOrConference) throws Exception {
        ArrayList<String> list = new ArrayList<String>();

        // Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Open a connection
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

        // Execute a query
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT teamName FROM TestNFL.listOfTeamsBrokenUp WHERE " + divisionOrConference + " = 1 ORDER BY teamName ASC");

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

    public static int getPreviousPredictedScoreForTeam(int week, String team) throws Exception{

        // Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Open a connection
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

        // Execute a query
        Statement updateYourTeam = conn.createStatement();
        ResultSet rs = updateYourTeam.executeQuery("SELECT predictedScore from TestNFL.results WHERE teamName = '" + team + "' and week = '" + week + "'");

        rs.next();
        int score = rs.getInt("predictedScore");

        // Clean-up environment
        rs.close();
        updateYourTeam.close();
        conn.close();

        return score;
    }

    public static int getPreviousOpponentPredictedScoreForTeam(int week, String team) throws Exception{

        // Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Open a connection
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

        // Execute a query
        Statement updateYourTeam = conn.createStatement();
        ResultSet rs = updateYourTeam.executeQuery("SELECT predictedOpponentScore from TestNFL.results WHERE teamName = '" + team + "' and week = '" + week + "'");

        rs.next();
        int score = rs.getInt("predictedOpponentScore");

        // Clean-up environment
        rs.close();
        updateYourTeam.close();
        conn.close();

        return score;
    }

    public static void insertScoreIntoDatabaseGivenTeamAndWeek(int week, int predictedScore, int predictedOpponentScore, String team, String opponentTeam) throws Exception {

        // Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Open a connection
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

        // Execute a query
        Statement updateYourTeam = conn.createStatement();
        updateYourTeam.executeUpdate("UPDATE TestNFL.results SET predictedScore = " + predictedScore + ", predictedOpponentScore = " + predictedOpponentScore + " WHERE teamName = '" + team + "' and week = '" + week + "'");

        Statement updateOpponentTeam = conn.createStatement();
        updateOpponentTeam.executeUpdate("UPDATE TestNFL.results SET predictedScore = " + predictedOpponentScore + ", predictedOpponentScore = " + predictedScore + " WHERE teamName = '" + opponentTeam + "' and week = '" + week + "'");

        // Clean-up environment
        updateYourTeam.close();
        updateOpponentTeam.close();
        conn.close();
    }

    public static void insertWinOrLossIntoDatabaseGivenTeamAndWeek(int week, int winOrLoss, int opponentWinOrLoss, String team, String opponentTeam) throws Exception {

        // Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Open a connection
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

        // Execute a query
        Statement updateYourTeam = conn.createStatement();
        updateYourTeam.executeUpdate("UPDATE TestNFL.results SET predictedResult = " + winOrLoss + " WHERE teamName = '" + team + "' and week = '" + week + "'");

        Statement updateOpponentTeam = conn.createStatement();
        updateOpponentTeam.executeUpdate("UPDATE TestNFL.results SET predictedResult = " + opponentWinOrLoss + " WHERE teamName = '" + opponentTeam + "' and week = '" + week + "'");

        // Clean-up environment
        updateYourTeam.close();
        updateOpponentTeam.close();
        conn.close();
    }

    public static int getCountOfWinsOrLosses(String team, int winOrLoss) throws Exception {
        // Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Open a connection
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

        // Execute a query
        Statement updateYourTeam = conn.createStatement();
        ResultSet rs = updateYourTeam.executeQuery("SELECT count(*) from TestNFL.results WHERE teamName = '" + team + "' and predictedResult = " + winOrLoss);

        rs.next();
        int count = rs.getInt("count(*)");

        // Clean-up environment
        rs.close();
        updateYourTeam.close();
        conn.close();

        return count;
    }
}
