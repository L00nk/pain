package org.example;

import jakarta.xml.ws.Service;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.xml.namespace.QName;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Client extends JFrame {
    private JTable jTable;
    private DefaultTableModel defaultTableModel;
    private JPanel jPanel;
    private JComboBox<String> producerComboBox;
    private JButton producerButton;
    private JTextArea producerNumber;

    private JComboBox<String> matrixComboBox;
    private JButton matrixButton;
    private JTextArea matrixNumber;

    private JComboBox<String> proportionComboBox;
    private JButton proportionButton;
    private JTextArea proportionNumber;
    String[] headers = {
            "Nazwa producenta", "Przekątna ekranu", "Rozdzielczość ekranu", "Rodzaj powierzchni ekranu",
            "Czy ekran jest dotykowy", "Nazwa procesora", "Liczba rdzeni fizycznych", "Prędkość taktowania MHz",
            "Wielkość pamięci RAM", "Pojemność dysku", "Rodzaj dysku", "Nazwa układu graficznego",
            "Pamięć układu graficznego", "Nazwa systemu operacyjnego", "Rodzaj napędu fizycznego"
    };
    public static Object[][] laptops = new Object[0][15];
    private final static String[] producers = {"Samsung", "Acer", "Asus", "Fujitsu", "Huawei", "MSI", "Dell", "Sony"};
    private final static String[] matrixs = {"matowa", "blyszczaca"};
    private final static String[] proportions = {"5:4", "4:3", "3:2", "16:10", "16:9", "5:3", "2:1", "21:9"};

    //soap
    public static URL url = null;
    public static QName qName;
    public static Service service;
    public static LaptopsInterface laptopsInterface;

    public Client() throws MalformedURLException {
        url = new URL("http://localhost:8080/laptops?wsdl");
        qName = new QName("http://example.org/", "LaptopsBeanService");
        service = Service.create(url, qName);
        laptopsInterface = service.getPort(LaptopsInterface.class);

        setTitle("Projekt 5 - strona klienta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        defaultTableModel = new DefaultTableModel(null, headers);
        jTable = new JTable(defaultTableModel);

        jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        producerComboBox = new JComboBox<>(producers);
        producerButton = new JButton("Producent");
        producerNumber = new JTextArea();
        producerNumber.setText("Laptopy - producenci: 0");
        producerNumber.setFocusable(false);

        matrixComboBox = new JComboBox<>(matrixs);
        matrixButton = new JButton("Matryca");
        matrixNumber = new JTextArea();
        matrixNumber.setText("Laptopy - matryce: 0");
        matrixNumber.setFocusable(false);

        //proportionComboBox = new JComboBox<>(proportions);
        //proportionButton = new JButton("Proporcje");
        //proportionNumber = new JTextArea();
        //proportionNumber.setText("Laptopy - proporcje: 0");
        //proportionNumber.setFocusable(false);

        jPanel.add(producerComboBox);
        jPanel.add(producerButton);
        jPanel.add(producerNumber);

        jPanel.add(matrixComboBox);
        jPanel.add(matrixButton);
        jPanel.add(matrixNumber);

        //jPanel.add(proportionComboBox);
        //jPanel.add(proportionButton);
        //jPanel.add(proportionNumber);
        jPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Laptopy", TitledBorder.CENTER, TitledBorder.TOP));
        jPanel.setVisible(true);

        producerButton.addActionListener(e -> {
            for (int i = laptops.length - 1; i >= 0; i--)
                defaultTableModel.removeRow(i);

            laptops = laptopsInterface.laptopsByProducent(
                    Objects.requireNonNull(producerComboBox.getSelectedItem()).toString());

            producerNumber.setText("Laptopy - producenci: " + laptops.length);

            for (Object[] objects : laptops)
                defaultTableModel.addRow(objects);
        });

        matrixButton.addActionListener(e -> {
            for (int i = laptops.length - 1; i >= 0; i--)
                defaultTableModel.removeRow(i);

            laptops = laptopsInterface.laptopsByPowierzchnia(
                    Objects.requireNonNull(matrixComboBox.getSelectedItem()).toString());

            matrixNumber.setText("Laptopy - matryce: " + laptops.length);

            for (Object[] objects : laptops)
                defaultTableModel.addRow(objects);
        });

        /*
        proportionButton.addActionListener(e -> {
            for (int i = laptops.length - 1; i >= 0; i--) {
                defaultTableModel.removeRow(i);
            }

            laptops = laptopsInterface.laptopsByProporcje(
                    Objects.requireNonNull(proportionComboBox.getSelectedItem()).toString());

            proportionNumber.setText("Laptopy - proporcje: " + laptops.length);

            for (Object[] objects : laptops) {
                defaultTableModel.addRow(objects);
            }
        });

         */

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(jTable), BorderLayout.CENTER);
        contentPane.add(jPanel, BorderLayout.NORTH);

        setSize(1400, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) throws MalformedURLException {
        new Client();
    }


}
