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
        // Hilo para monitorear el consumo de RAM en consola
        
        /*Thread monitorRam = new Thread(() -> {
            try {
                while (true) {
                    // Cálculo de memoria consumida en Megabytes
                    long memoryUsed = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
                    System.out.println("Consumo de RAM actual: " + memoryUsed + " MB");
                    Thread.sleep(5000); // Mide cada 5 segundos
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        monitorRam.setDaemon(true); // Para que el hilo se cierre al cerrar el juego
        monitorRam.start();
        */
        // Tu código original
        new JFXPanel();
        javax.swing.SwingUtilities.invokeLater(() -> {
            GameFrame gameFrame = new GameFrame();
        });
    }
}