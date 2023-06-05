package org.example;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class AutoHelper {
    public static void click(int x, int y, Robot robot) throws AWTException {
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }
    public static void goRight(Robot robot) throws AWTException {
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
    }
    public static void deleteText(Robot robot) throws AWTException {
        for(int i = 0; i<25; i++)
        {
            robot.keyPress(KeyEvent.VK_BACK_SPACE);
            robot.keyRelease(KeyEvent.VK_BACK_SPACE);
        }

    }
    public static void write(String text, Robot robot) throws AWTException, InterruptedException {
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
