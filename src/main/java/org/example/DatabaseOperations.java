package org.example;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.List;

public class DatabaseOperations {
    public static void saveData(DefaultTableModel defaultTableModel)
            throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/files";
        String username = "root";
        String password = "";

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "INSERT INTO laptop (id, producent, ekran, rozdzielczosc, powierzchnia, czy_dotyk, procesor, " +
                "l_rdzeni, taktowanie, ram, pamiec, dysk, karta_graf, vram, system, " +
                "naped) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);

        for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
            statement.setInt(1, i + 1);
            for (int j = 0; j < defaultTableModel.getColumnCount(); j++) {
                Object value = defaultTableModel.getValueAt(i, j);
                if (value != null)
                    statement.setString(j + 2, value.toString());
                else
                    statement.setString(j + 2, ";");
            }
            statement.executeUpdate();
        }

        statement.close();
        connection.close();
    }

    public static void updateData(DefaultTableModel defaultTableModel, int id)
            throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/files";
        String username = "root";
        String password = "";

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "UPDATE laptop SET producent = ?, ekran = ?, rozdzielczosc = ?, powierzchnia = ?, czy_dotyk = ?, " +
                "procesor = ?, l_rdzeni = ?, taktowanie = ?, ram = ?, pamiec = ?, dysk = ?, " +
                "karta_graf = ?, vram = ?, system = ?, naped = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < defaultTableModel.getColumnCount(); i++) {

            Object newCellValue = defaultTableModel.getValueAt(id, i);
            if (newCellValue != null)
                statement.setString(i + 1, newCellValue.toString());
            else
                statement.setString(i + 1, ";");
        }
        statement.setInt(defaultTableModel.getColumnCount() + 1, id + 1);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }
    public static List<Object[]> readData(List<Object[]> laptops)
            throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/files";
        String username = "root";
        String password = "";

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM laptop";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        laptops.clear();

        while (resultSet.next()) {
            int i = 0;
            Object[] laptop = new Object[15];
            laptop[i++] = resultSet.getString("producent");
            laptop[i++] = resultSet.getString("ekran");
            laptop[i++] = resultSet.getString("rozdzielczosc");
            laptop[i++] = resultSet.getString("powierzchnia");
            laptop[i++] = resultSet.getString("czy_dotyk");
            laptop[i++] = resultSet.getString("procesor");
            laptop[i++] = resultSet.getString("l_rdzeni");
            laptop[i++] = resultSet.getString("taktowanie");
            laptop[i++] = resultSet.getString("ram");
            laptop[i++] = resultSet.getString("pamiec");
            laptop[i++] = resultSet.getString("dysk");
            laptop[i++] = resultSet.getString("karta_graf");
            laptop[i++] = resultSet.getString("vram");
            laptop[i++] = resultSet.getString("system");
            laptop[i++] = resultSet.getString("naped");

            laptops.add(laptop);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return laptops;
    }
}
