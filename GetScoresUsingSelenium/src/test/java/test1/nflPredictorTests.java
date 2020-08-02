package test1;

import org.testng.annotations.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class nflPredictorTests {

    @Test
    public void selectYourTeams() throws Exception {

        List listOfNFLTeams = NFLPredictor.getListOfTeamsFromNFL();
        JComboBox pickATeam = new JComboBox(listOfNFLTeams.toArray());

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
                if (result == JOptionPane.NO_OPTION){
                    NFLPredictor.insertWinOrLossIntoDatabaseGivenTeamAndWeek(weekNumber,0,1, whatTeam, opponentTeamName);
                    System.out.println("lose");
                }else if (result == JOptionPane.YES_OPTION){
                    NFLPredictor.insertWinOrLossIntoDatabaseGivenTeamAndWeek(weekNumber,1,0, whatTeam, opponentTeamName);
                }else if (result == JOptionPane.CANCEL_OPTION){
                    System.exit(0);
                }
            }
        }
        System.out.println("wins: " + NFLPredictor.getCountOfWinsOrLosses(whatTeam, 1));
        System.out.println("loses: " + NFLPredictor.getCountOfWinsOrLosses(whatTeam, 0));
    }

    @Test
    public void seeYourPredictedRecords() throws Exception {

        List<Integer> predictedWinsInListNFL = new ArrayList<Integer>();
        int predictedWinsNFL;
        List<Integer> predictedLossesInListNFL = new ArrayList<Integer>();
        int predictedLossesNFL;

        List predictedWinsInListNFC = new ArrayList();
        int predictedWinsNFC;
        List predictedLossesInListNFC = new ArrayList();
        int predictedLossesNFC;

        List predictedWinsInListAFC = new ArrayList();
        int predictedWinsAFC;
        List predictedLossesInListAFC = new ArrayList();
        int predictedLossesAFC;


        List listOfNFLTeams = NFLPredictor.getListOfTeamsFromNFL();
        List listOfNFCTeams = NFLPredictor.getListOfTeamsFromSection("nfc");
        List listOfAFCTeams = NFLPredictor.getListOfTeamsFromSection("afc");
        List listOfNFCEastTeams = NFLPredictor.getListOfTeamsFromSection("nfcEast");
        List listOfNFCWestTeams = NFLPredictor.getListOfTeamsFromSection("nfcWest");
        List listOfNFCNorthTeams = NFLPredictor.getListOfTeamsFromSection("nfcNorth");
        List listOfNFCSouthTeams = NFLPredictor.getListOfTeamsFromSection("nfcSouth");
        List listOfAFCEastTeams = NFLPredictor.getListOfTeamsFromSection("afcEast");
        List listOfAFCWestTeams = NFLPredictor.getListOfTeamsFromSection("afcWest");
        List listOfAFCNorthTeams = NFLPredictor.getListOfTeamsFromSection("afcNorth");
        List listOfAFCSouthTeams = NFLPredictor.getListOfTeamsFromSection("afcSouth");

        JTabbedPane tabbedPane = new JTabbedPane();

        // nfl
        Object[][] rowData = {};
        String[] columnNames = {"Team", "Wins","Losses"};

        DefaultTableModel tabModel;
        tabModel = new DefaultTableModel(rowData, columnNames);

        for (int i = 0; i < 32; i++) {
            predictedWinsNFL = NFLPredictor.getCountOfWinsOrLosses(listOfNFLTeams.get(i).toString(), 1);
            predictedWinsInListNFL.add(predictedWinsNFL);
            predictedLossesNFL = NFLPredictor.getCountOfWinsOrLosses(listOfNFLTeams.get(i).toString(), 0);
            predictedLossesInListNFL.add(predictedLossesNFL);
            tabModel.addRow(new Object[]{listOfNFLTeams.get(i).toString(), predictedWinsInListNFL.get(i), predictedLossesInListNFL.get(i)});
        }


        JTable table = new JTable(tabModel);
        table.setAutoCreateRowSorter(true);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150);
        columnModel.getColumn(1).setPreferredWidth(20);
        columnModel.getColumn(2).setPreferredWidth(20);


        tabbedPane.addTab("NFL", null, new JScrollPane(table),
                "Does nothing");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

//        // nfc
//        JPanel nfcPanel = new JPanel();
//        nfcPanel.setBorder(border);
//        nfcPanel.setLayout(new GridLayout(16, 4));
//        for (int i=0; i<16; i++) {
//            predictedWinsNFC = NFLPredictor.getCountOfWinsOrLosses(listOfNFCTeams.get(i).toString(), 1);
//            predictedWinsInListNFC.add(predictedWinsNFC);
//            predictedLossesNFC = NFLPredictor.getCountOfWinsOrLosses(listOfNFCTeams.get(i).toString(), 0);
//            predictedLossesInListNFC.add(predictedLossesNFC);
//
//            nfcPanel.add(new JLabel(listOfNFCTeams.get(i).toString()));
//            nfcPanel.add(new JLabel(""));
//            nfcPanel.add(new JLabel(predictedWinsInListNFC.get(i).toString()));
//            nfcPanel.add(new JLabel(predictedLossesInListNFC.get(i).toString()));
//        }
//        tabbedPane.addTab("NFC", null, nfcPanel,
//                "Does nothing");
//        tabbedPane.setMnemonicAt(1, KeyEvent.VK_1);
//
//        // afc
//        JPanel afcPanel = new JPanel();
//        afcPanel.setBorder(border);
//        afcPanel.setLayout(new GridLayout(16, 4));
//        for (int i=0; i<16; i++) {
//            predictedWinsAFC = NFLPredictor.getCountOfWinsOrLosses(listOfAFCTeams.get(i).toString(), 1);
//            predictedWinsInListAFC.add(predictedWinsAFC);
//            predictedLossesAFC = NFLPredictor.getCountOfWinsOrLosses(listOfAFCTeams.get(i).toString(), 0);
//            predictedLossesInListAFC.add(predictedLossesAFC);
//
//            afcPanel.add(new JLabel(listOfAFCTeams.get(i).toString()));
//            afcPanel.add(new JLabel(""));
//            afcPanel.add(new JLabel(predictedWinsInListAFC.get(i).toString()));
//            afcPanel.add(new JLabel(predictedLossesInListAFC.get(i).toString()));
//        }
//        tabbedPane.addTab("AFC", null, afcPanel,
//                "Does nothing");
//        tabbedPane.setMnemonicAt(2, KeyEvent.VK_1);

        // ok button
        Object[] option = {
                "Congrats You Have Predicted These Outcomes"
        };

        int result = JOptionPane.showOptionDialog(null, tabbedPane, "Predicted Wins & Losses",
                JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, option, null);
    }
}

