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
            if (listOfOpponents.get(i).toString().equals("BYE")) {
                int byeWeek = i + 1;
                JOptionPane.showMessageDialog(null, whatTeam + " have a bye on week " + byeWeek);
            }else{
                Object[] options1 = {
                        "Lose :(", "WIN!!!!!!!!!"
                };

                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(2, 2)); //2 rows, 4 columns
                panel.add(new JLabel(whatTeam));
                panel.add(new JLabel("          score :"));
                JTextField teamScoreInput = new JTextField(2);
                teamScoreInput.setText("get text from db");
                //TODO get score if already in DB ^
                panel.add(teamScoreInput);
                panel.add(new JLabel(listOfOpponents.get(i).toString()));
                JTextField opponentScoreInput = new JTextField(2);
                panel.add(new JLabel("          score:"));
                opponentScoreInput.setText("get text from db op");
                //TODO get score if already in DB ^
                panel.add(opponentScoreInput);

                int result = JOptionPane.showOptionDialog(null, panel, "Pick Your Winner",
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options1, null);

                String opponentTeamName = listOfOpponents.get(i).toString().substring(2);

                String teamScorePredictionString = teamScoreInput.getText();
                int teamScorePrediction = Integer.parseInt(teamScorePredictionString);
                String opponentScorePredictionString = opponentScoreInput.getText();
                int opponentScorePrediction = Integer.parseInt(opponentScorePredictionString);
//                System.out.println(teamScorePrediction);
//                // TODO store your team score in DB
//                System.out.println(opponentScorePrediction);
//                // TODO store your opponent team score in DB

                NFLPredictor.insertScoreIntoDatabaseGivenTeamAndWeek(i, teamScorePrediction, opponentScorePrediction, whatTeam, opponentTeamName);

                //no = win, yes = lose
                if (result == JOptionPane.NO_OPTION){
                    System.out.println("yay win");
                    // TODO enter into DB
                }else if (result == JOptionPane.YES_OPTION){
                    System.out.println("boo lose");
                    // TODO enter into DB
                }
            }
        }
    }
}

