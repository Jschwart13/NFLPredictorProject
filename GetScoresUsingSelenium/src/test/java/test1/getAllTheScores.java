package test1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.util.List;

public class getAllTheScores {

    @Test
    public void getAllTheScoresAndPrintThemOut(){

        System.setProperty("webdriver.chrome.driver", "chromedriver");
        ChromeDriver driver;
        driver = new ChromeDriver();

        for (int weekNumber = 1; weekNumber < 2; weekNumber++) {

            driver.navigate().to("https://www.pro-football-reference.com/years/2019/week_" + weekNumber + ".htm");

            System.out.println("Scores for week" + weekNumber + "\n\n");

            List<WebElement> numberOfGamesPlayed = driver.findElements(By.cssSelector("#content > div.game_summaries > div"));
            int numberOfGames = numberOfGamesPlayed.size();

            for (int gameNumber = 1; gameNumber < numberOfGames + 1; gameNumber++) {
                WebElement getGame1Team1Name = driver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div[" + gameNumber + "]/table[1]/tbody/tr[2]/td[1]/a"));
                WebElement getGame1Team1Score = driver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div[" + gameNumber + "]/table[1]/tbody/tr[2]/td[2]"));
                System.out.println(getGame1Team1Name.getText());
                System.out.println(getGame1Team1Score.getText());

                WebElement getGame1Team2Name = driver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div[" + gameNumber + "]/table[1]/tbody/tr[3]/td[1]/a"));
                WebElement getGame1Team2Score = driver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div[" + gameNumber + "]/table[1]/tbody/tr[3]/td[2]"));
                System.out.println(getGame1Team2Name.getText());
                System.out.println(getGame1Team2Score.getText());
            }

            System.out.println("\n\n");
        }

        driver.close();
    }
}
