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
import thecelestials.view.ui.Factory.MenuComponentFactory;

/**
 *
 * @author Limbert Quispe
 */
public class GameFrame extends JFrame implements ScreenSwitcher{
    private final CardLayout cardLayout;
    private final LoadingPanel loadingPanel;
    private final JPanel mainPanel;
    private MenuPanel menuPanel;
    private MenuPanel missionsPanel;
    private GameCanvas gameCanvas;
    private final String gameCanvasCard = "gameCanvasCard";
    private final String campaignsMenuCard = "campaignMenuCard";
    private final String missionsMenuCard = "missionsMenuCard";
    private final String loadingCard = "loadingCard";
    
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
        //Assets.init();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        loadingPanel = new LoadingPanel(loadingCard, this);
        loadingPanel.nextPanel(campaignsMenuCard, "");
        mainPanel.add(loadingPanel, loadingCard);
        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, loadingCard);
        setVisible(true);
        
        
    }
    
    

    @Override
    public void showCard(String cardName, String menuID) {
        if(Assets.unlock){
            Assets.unlock = false;
            menuPanel.updateContentForMenu("unlock");
        }
        switch (cardName) {
            case "loadingGameCard" -> {
                loadingPanel.nextPanel(gameCanvasCard, menuID);
                cardName = loadingCard;
            }
            case missionsMenuCard -> missionsPanel.updateContentForMenu(menuID);
            case gameCanvasCard -> {
                //Assets.loadGame(menuID);
                gameCanvas.playGame();
            }
            //System.out.println(cardName+" soy genio");
            default -> {
            }
        }
        cardLayout.show(mainPanel, cardName);
    }
    
    @Override
    public void initializeMenus(){
        menuPanel = new MenuPanel(this, campaignsMenuCard);
        missionsPanel = new MenuPanel(this, missionsMenuCard);
        
        mainPanel.add(menuPanel, campaignsMenuCard);
        mainPanel.add(missionsPanel, missionsMenuCard);
        
        gameCanvas = new GameCanvas();
        mainPanel.add(gameCanvas, gameCanvasCard);
        MenuComponentFactory.createDialogs(this);
        gameCanvas.start();
    }
    
    @Override
    public void resume(){
        gameCanvas.resume();
    }
}
