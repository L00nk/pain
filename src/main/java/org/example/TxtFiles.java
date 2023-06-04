package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TxtFiles {
    public static List<Object[]> readData(String[] headers, List<Object[]> laptops, String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            laptops.clear();
            while ((line = bufferedReader.readLine()) != null) {
                List<String> data = new ArrayList<>(Arrays.asList(line.split(";",-2)));

                int rightCounter = 0;

                Object[] computer = new Object[15];
                for (String property : data) {
                    if (property.isBlank())
                        property = "brak danych";
                    computer[rightCounter] = property;
                    rightCounter++;
                }
                laptops.add(computer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Plik nie znaleziony",
                    "Alert",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        return laptops;
    }
    public static void saveData(DefaultTableModel defaultTableModel, String fileName, int fileNr){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
                for (int j = 0; j < defaultTableModel.getColumnCount(); j++) {
                    Object value = defaultTableModel.getValueAt(i, j);

                    if (value != null)
                    {
                        bw.write(value.toString());
                    }

                    if (j < defaultTableModel.getColumnCount() - 1)
                    {
                        bw.write(";");
                    }
                }
                if (defaultTableModel.getRowCount() - 1 != i)
                {
                    bw.newLine();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileNr++;
    }
}
