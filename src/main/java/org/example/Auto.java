package org.example;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Auto extends JFrame {
    private JLabel recordNr;
    private JTextField recordText;
    private JPanel jPanel;
    private JButton executeButton;
    private JCheckBox xmlCheckBox;
    private String producent;
    private String matryca;


    String[] headers = {
            "Nazwa producenta", "Przekątna ekranu", "Rozdzielczość ekranu", "Rodzaj powierzchni ekranu",
            "Czy ekran jest dotykowy", "Nazwa procesora", "Liczba rdzeni fizycznych", "Prędkość taktowania MHz",
            "Wielkość pamięci RAM", "Pojemność dysku", "Rodzaj dysku", "Nazwa układu graficznego",
            "Pamięć układu graficznego", "Nazwa systemu operacyjnego", "Rodzaj napędu fizycznego"
    };
    private final List<CheckboxHelper> checkboxHelperList = new ArrayList<>();

    public Auto (){
        setTitle("T6");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jPanel = new JPanel(new GridLayout(1, 2));
        recordNr = new JLabel("Który rekord ma zostać zmodyfikowany?");
        recordText = new JTextField(10);

        executeButton = new JButton("Wykonaj");
        executeButton.addActionListener(e -> {
            try {
                if(Objects.equals(recordText.getText(), "") || Objects.equals(recordNr.getText(), null))
                {
                    JOptionPane.showMessageDialog(
                            null,
                            "Wpisz nr wiersza",
                            "Alert",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    Robot robot = new Robot();
                    AutoHelper.click(60,70,robot);
                    Thread.sleep(20);
                    changeForm(80,180,robot);
                    Thread.sleep(20);
                    AutoHelper.click(200,30,robot);
                    Thread.sleep(20);
                    filterFormP(robot);
                    Thread.sleep(200);
                    filterFormM(robot);
                    Thread.sleep(2000);

                }

            } catch (AWTException | InterruptedException | IOException | UnsupportedFlavorException ex) {
                throw new RuntimeException(ex);
            }
        });

        jPanel.add(recordNr);
        jPanel.add(recordText);

        JPanel optionsPanel = new JPanel(new GridLayout(0, 3));
        optionsPanel.add(new JLabel("Cecha"));
        optionsPanel.add(new JLabel("Czy zmodyfikować"));
        optionsPanel.add(new JLabel("Wartość"));

        for (String label : headers) {
            CheckboxHelper checkboxHelper = new CheckboxHelper(label);
            checkboxHelperList.add(checkboxHelper);
            optionsPanel.add(checkboxHelper.getLabel());
            optionsPanel.add(checkboxHelper.getCheckBox());
            optionsPanel.add(checkboxHelper.getTextField());
        }
        optionsPanel.add(new JLabel("Zapisz do XML?"));
        xmlCheckBox = new JCheckBox();
        optionsPanel.add(xmlCheckBox);

        JPanel executePanel = new JPanel(new GridLayout(1, 3));
        executePanel.add(executeButton);

        jPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Wiersz", TitledBorder.CENTER, TitledBorder.TOP));
        optionsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Opcje", TitledBorder.CENTER, TitledBorder.TOP));

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(jPanel, BorderLayout.NORTH);
        contentPane.add(optionsPanel,BorderLayout.CENTER);
        contentPane.add(executePanel,BorderLayout.SOUTH);

        setSize(500, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void changeForm(int x, int y,Robot robot) throws AWTException, InterruptedException, IOException, UnsupportedFlavorException {
        //Robot robot = new Robot();
        AutoHelper.click(90,120,robot);
        AutoHelper.click(x,y,robot);


        //zejście do wybranego wiersza za pomcą strzałek
        int recordNumber = Integer.parseInt(recordText.getText());
        for (int i = 0; i < recordNumber - 1; i++) {
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            Thread.sleep(200);
        }
        Thread.sleep(200);
        int index = 0;
        for (CheckboxHelper checkboxHelper: checkboxHelperList){
            if(index == 0 || index == 3)
            {
                robot.keyPress(KeyEvent.VK_INSERT);
                robot.keyRelease(KeyEvent.VK_INSERT);
                Thread.sleep(20);

                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_A);
                robot.keyRelease(KeyEvent.VK_A);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                Thread.sleep(20);

                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_C);
                robot.keyRelease(KeyEvent.VK_C);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                Thread.sleep(20);
                robot.keyPress(KeyEvent.VK_ESCAPE);

                robot.keyRelease(KeyEvent.VK_ESCAPE);

                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable contents = clipboard.getContents(null);

                if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    try {
                        String text = (String) contents.getTransferData(DataFlavor.stringFlavor);
                        System.out.println("Tekst ze schowka: " + text);
                        if (index == 0)
                        {
                            producent = text;
                        }
                        else
                        {
                            matryca = text;
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            if(!checkboxHelper.getCheckBox().isSelected())
            {
                AutoHelper.goRight(robot);
                Thread.sleep(200);
            }
            else
            {
                String text = checkboxHelper.getTextField().getText();
                //System.out.print(text);
                AutoHelper.deleteText(robot);
                Thread.sleep(20);
                AutoHelper.write(text,robot);
                Thread.sleep(20);
                AutoHelper.goRight(robot);
                Thread.sleep(200);
            }
            index++;
        }
        if(xmlCheckBox.isSelected())
        {
            AutoHelper.click(800,120,robot);
            Thread.sleep(200);
            AutoHelper.click(750,450,robot);
            Thread.sleep(200);
        }
        AutoHelper.click(1200,130,robot);
        Thread.sleep(200);
        AutoHelper.click(750,450,robot);
        Thread.sleep(200);
        AutoHelper.click(1000,130,robot);
        //index = 0;
        Thread.sleep(200);
    }
    public void filterFormP(Robot robot) throws AWTException, InterruptedException {

        //Robot robot = new Robot();
        AutoHelper.click(550,70,robot);
        Thread.sleep(200);

        String filterProd = checkboxHelperList.get(0).getTextField().getText();

        if(!filterProd.equals(""))
        {
            AutoHelper.write(filterProd,robot);
            Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(200);
            AutoHelper.click(650,70,robot);
        }
        else if (filterProd.equals("") && !Objects.equals(producent, "")){
            AutoHelper.write(producent,robot);
            Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(200);
            AutoHelper.click(650,70,robot);
        }
        else
        {
            for (int i = 0; i < 8; i++) {
                robot.keyPress(KeyEvent.VK_DOWN);
                robot.keyRelease(KeyEvent.VK_DOWN);
                Thread.sleep(200);
            }
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(200);
            //AutoHelper.click(470,70);
            AutoHelper.click(650,70,robot);
        }


    }
    public void filterFormM(Robot robot) throws AWTException, InterruptedException {

        //Robot robot = new Robot();
        AutoHelper.click(900,70,robot);
        Thread.sleep(200);


        String filterMat = checkboxHelperList.get(3).getTextField().getText();

        if(!filterMat.equals(""))
        {
            AutoHelper.write(filterMat,robot);
            Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(200);
            AutoHelper.click(1000,70,robot);

        }
        else if(filterMat.equals("") && !Objects.equals(matryca, "")){
            AutoHelper.write(matryca,robot);
            Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(200);
            AutoHelper.click(1000,70,robot);
        }
        else
        {
            for (int i = 0; i < 2; i++) {
                robot.keyPress(KeyEvent.VK_DOWN);
                robot.keyRelease(KeyEvent.VK_DOWN);
                Thread.sleep(200);
            }
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(200);
            AutoHelper.click(1000,70,robot);
        }



    }
    public static void main(String[] args){
        new Auto();
    }

}
