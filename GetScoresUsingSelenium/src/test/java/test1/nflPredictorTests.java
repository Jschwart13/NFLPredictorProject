package test1;

import org.testng.annotations.Test;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class nflPredictorTests {

    @Test
    public void selectYourTeams() throws Exception {

        NFLPredictor nflPredictor = new NFLPredictor();
        List listOfNFLTeams = nflPredictor.getListOfTeamsFromSchedule();
        JComboBox pickATeam = new JComboBox(listOfNFLTeams.toArray());
        Object[] message = {
                "team", pickATeam,
        };
        JOptionPane.showConfirmDialog(null, message, "inputs", JOptionPane.OK_CANCEL_OPTION);

        String whatTeam = pickATeam.getSelectedItem().toString();

        List listOfOpponents = nflPredictor.getOpponentGivenTeamName(whatTeam);

        for (int i=0; i<17; i++) {
            int weekNumber = i+1;
            if (listOfOpponents.get(i).toString().equals("BYE")) {
                int byeWeek = i + 1;
                JOptionPane.showMessageDialog(null, whatTeam + " have a bye on week " + byeWeek);
                NFLPredictor.insertWinOrLossIntoDatabaseGivenTeamAndWeek(weekNumber,44,44, whatTeam, null);
            }else{
                String opponentTeamName = listOfOpponents.get(i).toString().substring(2);
                Object[] options1 = {
                        "Lose :(", "WIN!!!!!!!!!"
                };

                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(2, 2)); //2 rows, 4 columns
                panel.add(new JLabel(whatTeam));
                panel.add(new JLabel("          score :"));
                JTextField teamScoreInput = new JTextField(2);
                teamScoreInput.setText(Integer.toString(NFLPredictor.getPreviousPredictedScoreForTeam(weekNumber, whatTeam)));
                panel.add(teamScoreInput);
                panel.add(new JLabel(listOfOpponents.get(i).toString()));
                panel.add(new JLabel("          score:"));
                JTextField opponentScoreInput = new JTextField(2);
                opponentScoreInput.setText(Integer.toString(NFLPredictor.getPreviousOpponentPredictedScoreForTeam(weekNumber, whatTeam)));
                panel.add(opponentScoreInput);

                int result = JOptionPane.showOptionDialog(null, panel, whatTeam + " Week " + weekNumber,
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options1, null);

                String teamScorePredictionString = teamScoreInput.getText();
                int teamScorePrediction = Integer.parseInt(teamScorePredictionString);
                String opponentScorePredictionString = opponentScoreInput.getText();
                int opponentScorePrediction = Integer.parseInt(opponentScorePredictionString);

                // store your scores in the DB
                NFLPredictor.insertScoreIntoDatabaseGivenTeamAndWeek(weekNumber, teamScorePrediction, opponentScorePrediction, whatTeam, opponentTeamName);

                // no = win, yes = lose
                if (result == JOptionPane.NO_OPTION){
                    NFLPredictor.insertWinOrLossIntoDatabaseGivenTeamAndWeek(weekNumber,1,0, whatTeam, opponentTeamName);
                }else if (result == JOptionPane.YES_OPTION){
                    NFLPredictor.insertWinOrLossIntoDatabaseGivenTeamAndWeek(weekNumber,0,1, whatTeam, opponentTeamName);
                }
            }
        }
    }
}

