/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials;

import javafx.embed.swing.JFXPanel;
import thecelestials.view.ui.GameFrame;

/**
 *
 * @author pc
 */
public class Main {

    public static void main(String[] args) {
        //System.setProperty("sun.java2d.uiScale", "1.0");
        new JFXPanel();
        javax.swing.SwingUtilities.invokeLater(() -> {
            GameFrame gameFrame = new GameFrame();
        });
    }
}
