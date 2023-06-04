package org.example;

import jakarta.xml.ws.Endpoint;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends JFrame {
    private final String fileName = "katalog.txt";
    private JTable jTable;
    private DefaultTableModel defaultTableModel;
    private JPanel jPanel;
    private JButton importButton;
    private JButton exportButton;
    private int fileNr = 0;
    private JButton writeXMLButton;
    private JButton readXMLButton;
    private JButton writeDatabaseButton;
    private JButton readDatabaseButton;
    private JLabel copiesLabel;
    private JLabel newValuesLabel;
    String[] headers = {
            "Nazwa producenta", "Przekątna ekranu", "Rozdzielczość ekranu", "Rodzaj powierzchni ekranu",
            "Czy ekran jest dotykowy", "Nazwa procesora", "Liczba rdzeni fizycznych", "Prędkość taktowania MHz",
            "Wielkość pamięci RAM", "Pojemność dysku", "Rodzaj dysku", "Nazwa układu graficznego",
            "Pamięć układu graficznego", "Nazwa systemu operacyjnego", "Rodzaj napędu fizycznego"
    };
    List<Object[]> laptops = new ArrayList<>();

    private List<Object[]> previousLaptops = new ArrayList<>();
    private List<Integer> sameRows = new ArrayList<>();
    private List<Integer> editedRows = new ArrayList<>();

    private int nullCounter = 0;
    private boolean errors = false;
    private boolean ifImportButtonClicked = false;
    public static LaptopsBean laptopsBean = new LaptopsBean();

    public Main(){

        //soap
        Endpoint.publish("http://localhost:8080/laptops", laptopsBean);

        super.setTitle("Projekt 5 - strona serwera");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        defaultTableModel = new DefaultTableModel(null, headers);//change
        jTable = new JTable(defaultTableModel);

        jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 5));

        //insert focus
        jTable.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), "insertAction");
        jTable.getActionMap().put("insertAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = jTable.getSelectedRow();
                int selectedColumn = jTable.getSelectedColumn();

                if (selectedRow >= 0 && selectedColumn >= 0) {
                    jTable.editCellAt(selectedRow, selectedColumn);
                    Component editor = jTable.getEditorComponent();
                    if (editor != null) {
                        editor.requestFocusInWindow();
                    }
                }
            }
        });

        importButton = new JButton("Importuj z pliku txt");
        importButton.addActionListener(e -> {
            editedRows = new ArrayList<>();
            setPreviousLaptopList();
            nullCounter = 0;
            ifImportButtonClicked = true;
            defaultTableModel.setRowCount(0);
            TxtFiles.readData(headers, laptops, fileName).forEach(
                    laptop -> defaultTableModel.addRow(laptop));

            ifImportButtonClicked = false;
            countCopies();
            laptopsBean.laptops = laptops.toArray(new Object[0][]);
        });
        exportButton = new JButton("Eksportuj do pliku");
        exportButton.addActionListener(e -> {
            System.out.println(nullCounter);
            System.out.printf(String.valueOf(errors));
            if (nullCounter == 0 && !errors)
            {
                //check
                TxtFiles.saveData(defaultTableModel, fileName, fileNr);
                laptopsBean.laptops = laptops.toArray(new Object[0][]);
                JOptionPane.showMessageDialog(
                        null,
                        "Plik został zapisany",
                        "Alert",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            else if (nullCounter > 0)
            {
                JOptionPane.showMessageDialog(
                        null,
                        "Pola nie mogą być puste",
                        "Alert",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(
                        null,
                        "Wprowadź dane w poprawnym formacie",
                        "Alert",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        //xml
        readXMLButton = new JButton("Importuj z pliku XML");
        readXMLButton.addActionListener(e -> {
            editedRows = new ArrayList<>();
            setPreviousLaptopList();
            nullCounter = 0;
            ifImportButtonClicked = true;
            defaultTableModel.setRowCount(0);

            XmlFiles.readDataFromXML(
                    laptops).forEach(laptop -> defaultTableModel.addRow(laptop));
            ifImportButtonClicked = false;
            countCopies();
            laptopsBean.laptops = laptops.toArray(new Object[0][]);
        });

        writeXMLButton = new JButton("Eksportuj do pliku XML");
        writeXMLButton.addActionListener(e -> {
            if (nullCounter == 0 && !errors)
            {
                XmlFiles.saveDataToXML(defaultTableModel);
                laptopsBean.laptops = laptops.toArray(new Object[0][]);
                JOptionPane.showMessageDialog(
                        null,
                        "Plik został zapisany",
                        "Alert",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            else if (nullCounter > 0)
            {
                JOptionPane.showMessageDialog(
                        null,
                        "Pola nie mogą być puste",
                        "Alert",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(
                        null,
                        "Wprowadź dane w poprawnym formacie",
                        "Alert",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        //koniec xml

        //baza
        readDatabaseButton = new JButton("Import z bazy danych");
        readDatabaseButton.addActionListener(e -> {
            editedRows = new ArrayList<>();
            setPreviousLaptopList();

            nullCounter = 0;
            ifImportButtonClicked = true;
            defaultTableModel.setRowCount(0);
            try {
                DatabaseOperations.readData(laptops).forEach(laptop -> defaultTableModel.addRow(laptop));
            } catch (ClassNotFoundException | SQLException ex) {
                throw new RuntimeException(ex);
            }
            ifImportButtonClicked = false;

            countCopies();
            laptopsBean.laptops = laptops.toArray(new Object[0][]);
        });
        writeDatabaseButton = new JButton("Eksport do bazy danych");
        writeDatabaseButton.addActionListener(e -> {

            if (nullCounter == 0 && !errors) {
                try {
                    System.out.printf(String.valueOf(editedRows.size()));
                    if (editedRows.size() == 0) {

                        DatabaseOperations.saveData(defaultTableModel);
                        laptopsBean.laptops = laptops.toArray(new Object[0][]);

                        JOptionPane.showMessageDialog(
                                null,
                                "Zapisano dane do bazy danych",
                                "Alert",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        for (Integer id : editedRows)
                            DatabaseOperations.updateData(defaultTableModel, id);
                        JOptionPane.showMessageDialog(
                                null,
                                "Zapisano dane do bazy danych",
                                "Alert",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Problem z zapisem danych",
                            "Alert",
                            JOptionPane.INFORMATION_MESSAGE);
                    throw new RuntimeException(ex);

                }
            } else if (nullCounter > 0)
            {
                JOptionPane.showMessageDialog(
                        null,
                        "Pola nie mogą być puste",
                        "Alert",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(
                        null,
                        "Wprowadź dane w poprawnym formacie",
                        "Alert",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        //koniec bazy

        //poczatek walidacji
        jTable.getModel().addTableModelListener(e -> {
            DefaultTableModel tableModel = (DefaultTableModel) e.getSource();
            if (tableModel.getRowCount() > 0 && !ifImportButtonClicked) {
                Object value = tableModel.getValueAt(e.getFirstRow(), e.getColumn());

                int curCol = e.getColumn();
                String val = value.toString();

                if (val.equals(""))
                {
                    nullCounter++;
                }
                else
                {
                    nullCounter--;
                    if (nullCounter<0)
                    {
                        nullCounter = 0;
                    }
                }

                switch (curCol) {
                    case 1: {
                        if (!val.matches("([0-9])+(\")") && !val.equals("brak danych")) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Wprowadź dane w formacie np. cyfry + cudzysłów (np. 15\") lub, w przypadku braku informacji, wpisz brak danych",
                                    "Alert",
                                    JOptionPane.INFORMATION_MESSAGE);
                            errors = true;
                        }
                        else
                        {
                            errors = false;
                        }
                        break;
                    }
                    case 2: {
                        if (!val.matches("([0-9]){3,}x([0-9]){3,}")&& !val.equals("brak danych")) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Wprowadź dane w formacie np. min. 3 cyfry + znak x + min. 3 cyfry (np. 1920x1080) lub, w przypadku braku informacji, wpisz brak danych",
                                    "Alert",
                                    JOptionPane.INFORMATION_MESSAGE);
                            errors = true;
                        }
                        else
                        {
                            errors = false;
                        }
                        break;

                    }
                    case 4:
                    {
                        if (!val.equals("tak") && !val.equals("nie") && !val.equals("brak danych")) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Wpisz tak, nie lub, w przypadku braku informacji, brak danych",
                                    "Alert",
                                    JOptionPane.INFORMATION_MESSAGE);
                            errors = true;
                        }
                        else
                        {
                            errors = false;
                        }
                        break;
                    }
                    case 6:
                    case 7:{
                        if (!val.equals("brak danych")) {
                            try {
                                int nr = Integer.parseInt(val);
                                errors = false;
                            } catch (NumberFormatException nfe) {
                                errors = true;
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Wprowadź wartość w formacie liczbowym, w przypadku braku informacji, wpisz brak danych",
                                        "Alert",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        break;

                    }
                    case 8:
                    case 9:
                    case 12:{
                        if (!val.matches("([0-9])+GB")&& !val.equals("brak danych")) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Wprowadź dane w formacie np. cyfry + GB (np. 15GB) lub, w przypadku braku informacji, wpisz brak danych",
                                    "Alert",
                                    JOptionPane.INFORMATION_MESSAGE);
                            errors = true;
                        }
                        else
                        {
                            errors = false;
                        }
                        break;

                    }
                    case 10:
                    {
                        if (!val.equals("HDD") && !val.equals("SSD") && !val.equals("brak danych")) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Wpisz HDD, SSD lub, w przypadku braku informacji, brak danych",
                                    "Alert",
                                    JOptionPane.INFORMATION_MESSAGE);
                            errors = true;
                        }
                        else
                        {
                            errors = false;
                        }
                        break;
                    }
                    case 14:
                    {
                        if (!val.equals("DVD") && !val.equals("Blu-Ray") && !val.equals("brak") && !val.equals("brak danych")) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Wpisz DVD, Blu-Ray, brak lub, w przypadku braku informacji, brak danych",
                                    "Alert",
                                    JOptionPane.INFORMATION_MESSAGE);
                            errors = true;
                        }
                        else
                        {
                            errors = false;
                        }
                        break;
                    }
                }

            }
        });
        //koniec walidacji

        jPanel.add(importButton);
        jPanel.add(exportButton);
        jPanel.add(readXMLButton);
        jPanel.add(writeXMLButton);
        jPanel.add(readDatabaseButton);
        jPanel.add(writeDatabaseButton);

        newValuesLabel = new JLabel("Nowe wartości = 0");
        copiesLabel = new JLabel("Duplikaty = 0");

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(newValuesLabel);
        infoPanel.add(copiesLabel);
        jPanel.add(infoPanel);

        jPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Laptopy", TitledBorder.CENTER, TitledBorder.TOP));

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(jTable), BorderLayout.CENTER);
        contentPane.add(jPanel, BorderLayout.NORTH);

        setSize(1540, 700);
        setLocationRelativeTo(null);
        setVisible(true);

        //sprawdzanie edytowanych wartosci w tabeli
        defaultTableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                Object newValue = defaultTableModel.getValueAt(row, column);
                Object oldValue = laptops.get(row)[column];

                if (!newValue.equals(oldValue)) {
                    editedRows.add(row);
                    countCopies();
                }
            }
        });

    }
    //ustawienie listy wartosci poprzednich wyswietlanych danych
    public void setPreviousLaptopList() {
        previousLaptops = new ArrayList<>();
        for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
            Object[] row = new Object[defaultTableModel.getColumnCount()];
            for (int j = 0; j < defaultTableModel.getColumnCount(); j++) {
                row[j] = defaultTableModel.getValueAt(i, j);
            }
            previousLaptops.add(row);
        }
    }
    //zliczanie duplikatow
    public void countCopies() {
        if (previousLaptops.size() > 0) {
            int copiesNumber = 0;
            sameRows = new ArrayList<>();
            for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
                Object[] row = new Object[defaultTableModel.getColumnCount()];

                for (int j = 0; j < defaultTableModel.getColumnCount(); j++)
                    row[j] = defaultTableModel.getValueAt(i, j);

                int counter = 0;
                for (Object[] laptop : previousLaptops) {
                    if (Arrays.equals(laptop, row) && counter == i) {
                        copiesNumber++;
                        sameRows.add(i);
                        break;
                    }
                    counter++;
                }
            }
            newValuesLabel.setText("Nowe wartości = " + (defaultTableModel.getRowCount() - copiesNumber));
            copiesLabel.setText("Duplikaty = " + copiesNumber);

            jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(sameRows, editedRows));
        } else {
            jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(sameRows, editedRows));
            newValuesLabel.setText("Nowe wartości = " + defaultTableModel.getRowCount());
            copiesLabel.setText("Duplikaty = " + 0);
        }
    }
    public static void main(String[] args) {
        new Main();
    }
}