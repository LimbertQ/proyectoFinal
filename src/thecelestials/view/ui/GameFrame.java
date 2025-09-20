/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import thecelestials.model.data.Assets;

/**
 *
 * @author Limbert Quispe
 */
public class GameFrame extends JFrame{
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final GameCanvas gameCanvas;
    private final String gameCanvasCard = "juego";
    public GameFrame(){
        setTitle("Los Celestiales");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setLocationRelativeTo(null);
        //-----------
        Assets.init();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        gameCanvas = new GameCanvas();
        mainPanel.add(gameCanvas, gameCanvasCard);
        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, gameCanvasCard);
        setVisible(true);
        gameCanvas.start();
    }
}
