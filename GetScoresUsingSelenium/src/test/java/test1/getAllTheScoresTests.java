package test1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import javax.swing.*;
import java.util.*;
import java.util.List;

public class getAllTheScoresTests {

    @Test
    public void getAllTheScoresAndPrintThemOut() throws Exception {

        System.setProperty("webdriver.chrome.driver", "chromedriver");
        ChromeDriver driver;

        GetAllTheScores getAllTheScores = new GetAllTheScores();
        List listOfNFLTeams = getAllTheScores.getListOfNFLTeams();

        JTextField pickAYear = new JTextField();
        JTextField pickAWeek = new JTextField();
        JComboBox pickATeam = new JComboBox(listOfNFLTeams.toArray());

        Object[] message = {
                "year (example: 2007):", pickAYear,
                "week (example: 17):", pickAWeek,
                "team (example: New York Giants)", pickATeam
        };
        JOptionPane.showConfirmDialog(null, message, "inputs", JOptionPane.OK_CANCEL_OPTION);

        String stringYear = pickAYear.getText();
        int whatYear = Integer.parseInt(stringYear);
        String stringWeek = pickAWeek.getText();
        int whatWeek = Integer.parseInt(stringWeek);
        String whatTeam = pickATeam.getSelectedItem().toString();
        int season = (whatYear + 1);

        HashMap<String, String> teamScores = new HashMap<String, String>();
        HashMap<String, String> awayOpponent = new HashMap<String, String>();
        HashMap<String, String> homeOpponent = new HashMap<String, String>();

        driver = new ChromeDriver();

        for (int weekNumber = whatWeek; weekNumber < whatWeek+1; weekNumber++) {

            driver.navigate().to("https://www.pro-football-reference.com/years/" + whatYear +"/week_" + weekNumber + ".htm");
            System.out.println("Scores for week " + weekNumber + " of the " + whatYear + "-" + season + " season:\n");

            List<WebElement> numberOfGamesPlayed = driver.findElements(By.cssSelector("#content > div.game_summaries > div"));
            int numberOfGames = numberOfGamesPlayed.size();

            for (int gameNumber = 1; gameNumber < numberOfGames + 1; gameNumber++) {
                WebElement awayTeam = driver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div[" + gameNumber + "]/table[1]/tbody/tr[2]/td[1]/a"));
                WebElement awayScore = driver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div[" + gameNumber + "]/table[1]/tbody/tr[2]/td[2]"));

                WebElement homeTeam = driver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div[" + gameNumber + "]/table[1]/tbody/tr[3]/td[1]/a"));
                WebElement homeScore = driver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div[" + gameNumber + "]/table[1]/tbody/tr[3]/td[2]"));

                teamScores.put(awayTeam.getText(), awayScore.getText());
                teamScores.put(homeTeam.getText(), homeScore.getText());
                awayOpponent.put(awayTeam.getText(), homeTeam.getText());
                homeOpponent.put(homeTeam.getText(), awayTeam.getText());
            }
        }

        if (teamScores.get(whatTeam) == null){
            System.out.println(whatTeam + " either did not play this week or did not even exist...");
        } else {
            String opponentTeam = awayOpponent.get(whatTeam);
            if (opponentTeam == null) {
                opponentTeam = homeOpponent.get(whatTeam);
            }
            System.out.println("The " + whatTeam + " played against the " + opponentTeam);
            System.out.println(whatTeam + " scored " + teamScores.get(whatTeam));
            System.out.println(opponentTeam + " scored " + teamScores.get(opponentTeam));

            int scoreInIntFormForYourTeam = Integer.parseInt(teamScores.get(whatTeam));
            int scoreInIntFormForOpponentTeam = Integer.parseInt(teamScores.get(opponentTeam));

            if (scoreInIntFormForYourTeam > scoreInIntFormForOpponentTeam){
                System.out.println("The " + whatTeam + " won. Nice.");
            }else if (scoreInIntFormForYourTeam < scoreInIntFormForOpponentTeam){
                System.out.println("The " + whatTeam + " lost. Darn.");
            }else{
                System.out.println(" Must have been a tie.");
            }
        }
    }
}
