package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class XmlFiles {
    public static List<Object[]> readDataFromXML(List<Object[]> laptops) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("katalog.xml"));
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("laptop");
            int counter = 0;

            laptops.clear();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element laptopElement = (Element) nodeList.item(i);
                Element screenElement = (Element) laptopElement.getElementsByTagName("screen").item(0);
                Element processorElement = (Element) laptopElement.getElementsByTagName("processor").item(0);
                Element discElement = (Element) laptopElement.getElementsByTagName("disc").item(0);
                Element graphicCardElement = (Element) laptopElement.getElementsByTagName("graphic_card").item(0);

                Object[] laptop = new Object[15];

                laptop[counter++] = laptopElement.
                        getElementsByTagName("manufacturer").item(0).getTextContent();
                laptop[counter++] = screenElement.getElementsByTagName("size").item(0).getTextContent();
                laptop[counter++] = screenElement.getElementsByTagName("resolution").item(0).getTextContent();
                laptop[counter++] = screenElement.getElementsByTagName("type").item(0).getTextContent();
                laptop[counter++] = screenElement.getAttribute("touch");
                laptop[counter++] = processorElement.getElementsByTagName("name").item(0).getTextContent();
                laptop[counter++] = processorElement.
                        getElementsByTagName("physical_cores").item(0).getTextContent();
                laptop[counter++] = processorElement.
                        getElementsByTagName("clock_speed").item(0).getTextContent();
                laptop[counter++] = laptopElement.getElementsByTagName("ram").item(0).getTextContent();
                laptop[counter++] = discElement.getElementsByTagName("storage").item(0).getTextContent();
                laptop[counter++] = discElement.getAttribute("type");
                laptop[counter++] = graphicCardElement.getElementsByTagName("name").item(0).getTextContent();
                laptop[counter++] = graphicCardElement.getElementsByTagName("memory").item(0).getTextContent();
                laptop[counter++] = laptopElement.getElementsByTagName("os").item(0).getTextContent();
                laptop[counter] = laptopElement.getElementsByTagName("disc_reader").item(0).getTextContent();
                counter = 0;

                laptop = Arrays.stream(laptop).map(s -> s.equals("") ? "brak danych" : s).toArray(Object[]::new);

                laptops.add(laptop);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Plik nie znaleziony",
                    "Alert",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        return laptops;
    }
    public static void saveDataToXML(DefaultTableModel defaultTableModel) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element laptops = document.createElement("laptops");

            int counter = 0;
            for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
                Element laptop = document.createElement("laptop");

                Element manufacturer = document.createElement("manufacturer");
                manufacturer.setTextContent((String) defaultTableModel.getValueAt(i, counter++));

                Element screen = document.createElement("screen");

                Element size = document.createElement("size");
                Element resolution = document.createElement("resolution");
                Element type = document.createElement("type");

                size.setTextContent((String) defaultTableModel.getValueAt(i, counter++));
                resolution.setTextContent((String) defaultTableModel.getValueAt(i, counter++));
                type.setTextContent((String) defaultTableModel.getValueAt(i, counter++));

                screen.setAttribute("touch", (String) defaultTableModel.getValueAt(i, counter++));

                Element processor = document.createElement("processor");

                Element name = document.createElement("name");
                Element physicalCores = document.createElement("physical_cores");
                Element clockSpeed = document.createElement("clock_speed");

                name.setTextContent((String) defaultTableModel.getValueAt(i, counter++));
                physicalCores.setTextContent((String) defaultTableModel.getValueAt(i, counter++));
                clockSpeed.setTextContent((String) defaultTableModel.getValueAt(i, counter++));

                Element ram = document.createElement("ram");
                ram.setTextContent((String) defaultTableModel.getValueAt(i, counter++));

                Element disc = document.createElement("disc");

                Element storage = document.createElement("storage");
                storage.setTextContent((String) defaultTableModel.getValueAt(i, counter++));

                disc.setAttribute("type", (String) defaultTableModel.getValueAt(i, counter++));

                Element graphicCard = document.createElement("graphic_card");

                Element name2 = document.createElement("name");
                Element memory = document.createElement("memory");

                name2.setTextContent((String) defaultTableModel.getValueAt(i, counter++));
                memory.setTextContent((String) defaultTableModel.getValueAt(i, counter++));

                Element os = document.createElement("os");
                os.setTextContent((String) defaultTableModel.getValueAt(i, counter++));

                Element discReader = document.createElement("disc_reader");
                discReader.setTextContent((String) defaultTableModel.getValueAt(i, counter));
                counter = 0;

                screen.appendChild(size);
                screen.appendChild(resolution);
                screen.appendChild(type);

                processor.appendChild(name);
                processor.appendChild(physicalCores);
                processor.appendChild(clockSpeed);

                disc.appendChild(storage);

                graphicCard.appendChild(name2);
                graphicCard.appendChild(memory);

                laptop.appendChild(manufacturer);
                laptop.appendChild(screen);
                laptop.appendChild(processor);
                laptop.appendChild(ram);
                laptop.appendChild(disc);
                laptop.appendChild(graphicCard);
                laptop.appendChild(os);
                laptop.appendChild(discReader);

                laptops.appendChild(laptop);
            }

            LocalDateTime newDate = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            laptops.setAttribute("moddate", dtf.format(newDate));

            document.appendChild(laptops);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.transform(new DOMSource(document), new StreamResult(new FileOutputStream("katalog.xml")));
        } catch (ParserConfigurationException | TransformerException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
