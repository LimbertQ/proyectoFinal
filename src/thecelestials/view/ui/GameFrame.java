/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import thecelestials.controller.ScreenSwitcher;
import thecelestials.model.data.Assets;

/**
 *
 * @author Limbert Quispe
 */
public class GameFrame extends JFrame implements ScreenSwitcher{
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final MenuPanel menuPanel;
    private final MenuPanel missionsPanel;
    private final GameCanvas gameCanvas;
    private final String gameCanvasCard = "gameCanvasCard";
    private final String campaignsMenuCard = "campaignMenuCard";
    private final String missionsMenuCard = "missionsMenuCard";
    public GameFrame(){
        setTitle("Los Celestiales");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Asegúrate de cerrar la conexión a la base de datos (o cualquier recurso)
                // de forma limpia antes de que la aplicación finalice.
                Assets.closeDbConnection();
                System.exit(0); // Ahora sí, termina el proceso de la aplicación
            }
        });
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setLocationRelativeTo(null);
        //-----------
        Assets.init();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        gameCanvas = new GameCanvas();
        menuPanel = new MenuPanel(this, campaignsMenuCard);
        missionsPanel = new MenuPanel(this, missionsMenuCard);
        
        mainPanel.add(menuPanel, campaignsMenuCard);
        mainPanel.add(missionsPanel, missionsMenuCard);
        mainPanel.add(gameCanvas, gameCanvasCard);
        
        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, campaignsMenuCard);
        setVisible(true);
        gameCanvas.start();
    }

    @Override
    public void showCard(String cardName, String menuID) {
        if(cardName.equals(missionsMenuCard)){
            missionsPanel.updateContentForMenu(menuID);
        }else if(cardName.equals(gameCanvasCard)){
            gameCanvas.playGame();
        }
        cardLayout.show(mainPanel, cardName);
    }
}
