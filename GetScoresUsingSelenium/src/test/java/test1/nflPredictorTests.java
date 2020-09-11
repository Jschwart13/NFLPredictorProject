package test1;

import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;
import java.util.List;

public class nflPredictorTests {

    @Test
    public void selectYourTeams() throws Exception {

        List listOfNFLTeamsIHaventPickedYet = NFLPredictor.getListOfTeamsIHaventPickedYet();
        JComboBox pickATeam = new JComboBox(listOfNFLTeamsIHaventPickedYet.toArray());

        Object[] message = {
                "team", pickATeam,
        };
        int choice = JOptionPane.showConfirmDialog(null, message, "inputs", JOptionPane.OK_CANCEL_OPTION);
        if (choice == JOptionPane.CANCEL_OPTION){
            System.exit(0);
        }

        String whatTeam = pickATeam.getSelectedItem().toString();
        List listOfOpponents = NFLPredictor.getOpponentGivenTeamName(whatTeam);

        for (int i=0; i<17; i++) {
            int weekNumber = i+1;
            if (listOfOpponents.get(i).toString().equals("BYE")) {
                int byeWeek = i + 1;
                JOptionPane.showMessageDialog(null, whatTeam + " have a bye on week " + byeWeek);
                NFLPredictor.insertWinOrLossIntoDatabaseGivenTeamAndWeek(weekNumber,44,44, whatTeam, null);
            }else{
                String opponentTeamName = listOfOpponents.get(i).toString().substring(2);
                Object[] options1 = {
                        "WIN!!!!!!!!!", "Lose :(", "Cancel"
                };

                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(2, 2));
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
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options1, null);

                String teamScorePredictionString = teamScoreInput.getText();
                int teamScorePrediction = Integer.parseInt(teamScorePredictionString);
                String opponentScorePredictionString = opponentScoreInput.getText();
                int opponentScorePrediction = Integer.parseInt(opponentScorePredictionString);

                // store your scores in the DB
                NFLPredictor.insertScoreIntoDatabaseGivenTeamAndWeek(weekNumber, teamScorePrediction, opponentScorePrediction, whatTeam, opponentTeamName);

                // no = win, yes = lose
                if (result == JOptionPane.NO_OPTION) {
                    NFLPredictor.insertWinOrLossIntoDatabaseGivenTeamAndWeek(weekNumber, 0, 1, whatTeam, opponentTeamName);
                } else if (result == JOptionPane.YES_OPTION) {
                    NFLPredictor.insertWinOrLossIntoDatabaseGivenTeamAndWeek(weekNumber, 1, 0, whatTeam, opponentTeamName);
                } else if (result == JOptionPane.CANCEL_OPTION) {
                    System.exit(0);
                }
            }
        }
        System.out.println("wins: " + NFLPredictor.getCountOfWinsOrLosses(whatTeam, 1));
        System.out.println("loses: " + NFLPredictor.getCountOfWinsOrLosses(whatTeam, 0));
    }

    @Test
    public void seeYourPredictedRecords() throws Exception {

        JTabbedPane tabbedPane = new JTabbedPane();

        // images:
        String nfl = "https://upload.wikimedia.org/wikipedia/en/thumb/a/a2/National_Football_League_logo.svg/1280px-National_Football_League_logo.svg.png";
        String nfc = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/National_Football_Conference_logo.svg/2560px-National_Football_Conference_logo.svg.png";
        String afc = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7a/American_Football_Conference_logo.svg/2560px-American_Football_Conference_logo.svg.png";

        // nfl
        JTable nflTable = predictedRecord("nfl", 32);

        tabbedPane.addTab("NFL", null, new JScrollPane(nflTable),
                "NFL");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        BufferedImage img = ImageIO.read(new URL(nfl));
        ImageIcon icon = new ImageIcon(img);
        tabbedPane.setIconAt(0, icon);

        // nfc
        JTable nfcTable = predictedRecord("nfc", 16);

        tabbedPane.addTab("NFC", null, new JScrollPane(nfcTable),
                "NFC");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_1);
        img = ImageIO.read(new URL(nfc));
        icon = new ImageIcon(img);
        tabbedPane.setIconAt(1, icon);

        // afc
        JTable afcTable = predictedRecord("afc", 16);

        tabbedPane.addTab("AFC", null, new JScrollPane(afcTable),
                "AFC");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_1);
        img = ImageIO.read(new URL(afc));
        icon = new ImageIcon(img);
        tabbedPane.setIconAt(2, icon);

        // nfcNorth
        JTable nfcNorthTable = predictedRecord("nfcNorth", 4);

        tabbedPane.addTab("NFC North", null, new JScrollPane(nfcNorthTable),
                "NFC North");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_1);

        // nfcEast
        JTable nfcEastTable = predictedRecord("nfcEast", 4);

        tabbedPane.addTab("NFC East", null, new JScrollPane(nfcEastTable),
                "NFC East");
        tabbedPane.setMnemonicAt(4, KeyEvent.VK_1);

        // nfcSouth
        JTable nfcSouthTable = predictedRecord("nfcSouth", 4);

        tabbedPane.addTab("NFC South", null, new JScrollPane(nfcSouthTable),
                "NFC South");
        tabbedPane.setMnemonicAt(5, KeyEvent.VK_1);

        // nfcWest
        JTable nfcWestTable = predictedRecord("nfcWest", 4);

        tabbedPane.addTab("NFC West", null, new JScrollPane(nfcWestTable),
                "NFC West");
        tabbedPane.setMnemonicAt(6, KeyEvent.VK_1);

        // afcNorth
        JTable afcNorthTable = predictedRecord("afcNorth", 4);

        tabbedPane.addTab("AFC North", null, new JScrollPane(afcNorthTable),
                "AFC North");
        tabbedPane.setMnemonicAt(7, KeyEvent.VK_1);

        // afcEast
        JTable afcEastTable = predictedRecord("afcEast", 4);

        tabbedPane.addTab("AFC East", null, new JScrollPane(afcEastTable),
                "AFC East");
        tabbedPane.setMnemonicAt(8, KeyEvent.VK_1);

        // afcSouth
        JTable afcSouthTable = predictedRecord("afcSouth", 4);

        tabbedPane.addTab("AFC South", null, new JScrollPane(afcSouthTable),
                "AFC South");
        tabbedPane.setMnemonicAt(9, KeyEvent.VK_1);

        // afcWest
        JTable afcWestTable = predictedRecord("afcWest", 4);

        tabbedPane.addTab("AFC West", null, new JScrollPane(afcWestTable),
                "AFC West");
        tabbedPane.setMnemonicAt(10, KeyEvent.VK_1);

        tabbedPane.setBackgroundAt(0, Color.GRAY);
        tabbedPane.setBackgroundAt(1, Color.BLUE);
        tabbedPane.setBackgroundAt(2, Color.RED);

        // ok button
        Object[] option = {
                "Congrats You Have Predicted These Outcomes"
        };

        JOptionPane.showOptionDialog(null, tabbedPane, "Predicted Wins & Losses",
                JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, option, null);
    }

    public JTable predictedRecord(String section, int numberOfTeams) throws Exception{


        List predictedWinsInList = new ArrayList();
        int predictedWins;
        List predictedLossesInList = new ArrayList();
        int predictedLosses;

        List listOfTeams;

        if (section.equals("nfl")){
            listOfTeams = NFLPredictor.getListOfTeamsFromNFL();
        }else {
            listOfTeams = NFLPredictor.getListOfTeamsFromSection(section);
        }

        Object[][] rowData = {};
        String[] columnNames = {"Team", "Wins","Losses"};

        DefaultTableModel tabModel;
        tabModel = new DefaultTableModel(rowData, columnNames);

        for (int i = 0; i < numberOfTeams; i++) {
            predictedWins = NFLPredictor.getCountOfWinsOrLosses(listOfTeams.get(i).toString(), 1);
            predictedWinsInList.add(predictedWins);
            predictedLosses = NFLPredictor.getCountOfWinsOrLosses(listOfTeams.get(i).toString(), 0);
            predictedLossesInList.add(predictedLosses);
            tabModel.addRow(new Object[]{listOfTeams.get(i).toString(), predictedWinsInList.get(i), predictedLossesInList.get(i)});
        }


        JTable table = new JTable(tabModel);
        table.setAutoCreateRowSorter(true);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150);
        columnModel.getColumn(1).setPreferredWidth(20);
        columnModel.getColumn(2).setPreferredWidth(20);

        return table;
    }
}

