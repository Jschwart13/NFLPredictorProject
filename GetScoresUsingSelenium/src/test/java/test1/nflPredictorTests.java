package test1;

import org.testng.annotations.Test;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class nflPredictorTests {

    @Test
    public void selectYourTeams() throws Exception {

        NFLPredictor ctb = new NFLPredictor();
        List listOfNFLTeams = ctb.pickYourTeam();
        JComboBox pickATeam = new JComboBox(listOfNFLTeams.toArray());
        Object[] message = {
                "team", pickATeam,
        };
        JOptionPane.showConfirmDialog(null, message, "inputs", JOptionPane.OK_CANCEL_OPTION);

        String whatTeam = pickATeam.getSelectedItem().toString();

        List listOfOpponents = ctb.OpponentsGivenTeam(whatTeam);

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
                JTextField textField = new JTextField(2);
                panel.add(textField);
                panel.add(new JLabel(listOfOpponents.get(i).toString()));
                panel.add(new JLabel("          score:"));
                JTextField textField2 = new JTextField(2);
                panel.add(textField2);

                int result = JOptionPane.showOptionDialog(null, panel, "Pick Your Winner",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options1, null);
            }
        }
    }
}

