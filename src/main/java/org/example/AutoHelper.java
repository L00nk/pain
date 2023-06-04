package org.example;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class AutoHelper {
    public static void click(int x, int y) throws AWTException {
        Robot bot = new Robot();
        bot.mouseMove(x, y);
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }
    public static void goRight() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
    }
    public static void deleteText() throws AWTException {
        Robot robot = new Robot();
        for(int i = 0; i<25; i++)
        {
            robot.keyPress(KeyEvent.VK_BACK_SPACE);
            robot.keyRelease(KeyEvent.VK_BACK_SPACE);
        }

    }
    public static void write(String text) throws AWTException, InterruptedException {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);

        Robot robot = new Robot();
        for (int i = 0; i < text.length(); i++)
        {
            //Getting current char
            char c = text.charAt(i);

            //Pressing shift if it's uppercase
            if (Character.isUpperCase(c))
            {
                robot.keyPress(KeyEvent.VK_SHIFT);
            }

            //Actually pressing the key
            robot.keyPress(Character.toUpperCase(c));
            robot.keyRelease(Character.toUpperCase(c));

            //Releasing shift if it's uppercase
            if (Character.isUpperCase(c))
            {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }

            //Optional delay to make it look like it's a human typing
            Thread.sleep(200);
        }
    }
}
