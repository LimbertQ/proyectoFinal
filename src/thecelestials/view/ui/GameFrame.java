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
import thecelestials.model.data.MissionStats;
import thecelestials.view.ui.Factory.MenuComponentFactory;

/**
 *
 * @author Limbert Quispe
 */
public class GameFrame extends JFrame implements ScreenSwitcher {

    private final CardLayout cardLayout;
    private final LoadingPanel loadingPanel;
    private final JPanel mainPanel;
    private MenuPanel menuPanel;
    private MenuPanel optionsMenuPanel;
    private MenuPanel extraMenuPanel;
    private MenuPanel campaignPanel;
    private MenuPanel missionsPanel;
    //private MenuSelectorPanel selectorMenuPanel;
    private InfoSelectorPanel selectorMenuPanel;
    private ButtonSelectorPanel buttonSelectorPanel;
    private MediaPlayerPanel mediaPlayerPanel;
    private GameCanvas gameCanvas;
    private final String gameCanvasCard = "gameCanvasCard";
    private final String extraMenuCard = "extraMenuCard";
    private final String optionsMenuCard = "optionsMenuCard";
    private final String campaignsMenuCard = "campaignMenuCard";
    private final String selectorMenuCard = "selectorMenuCard";
    private final String buttonSelectorCard = "buttonSelectorCard";
    private final String missionsMenuCard = "missionsMenuCard";
    private final String mainMenuCard = "mainMenuCard";
    private final String loadingCard = "loadingCard";
    private final String mediaPlayerCard = "mediaPlayerCard";

    public GameFrame() {
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
        loadingPanel.nextPanel(mainMenuCard, "");
        mainPanel.add(loadingPanel, loadingCard);
        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, loadingCard);
        setVisible(true);

    }

    @Override
    public void showCard(String cardName, String menuID) {
        //MISIONES || LOADING
        if (Assets.unlock && cardName.equals("loadingGameCard")) {
            cardName = missionsMenuCard;
        }
        switch (cardName) {
            case "loadingGameCard" -> {
                loadingPanel.nextPanel(gameCanvasCard, menuID);
                cardName = loadingCard;
            }

            case mediaPlayerCard -> {
                mediaPlayerPanel.updateVideo("selectorMenuCard", menuID);
            }
            case selectorMenuCard ->
                //selectorMenuPanel.updateContentForMenu(menuID);
                selectorMenuPanel.updateContentForMenu(menuID);
            case buttonSelectorCard ->
                buttonSelectorPanel.updateContentForMenu(menuID);
            case missionsMenuCard -> {
                if (Assets.unlock) {
                    //ACTUALIZAMOS CAMPANIA
                    Assets.unlock = false;
                    campaignPanel.updateContentForMenu("unlock");
                    //LA CAMPAÑA Y LA MISION ESTAN PREVIAMENTE DESBLOQUEADO ACTUALIZAMOS
                    //BUSCAMOS LA CAMPAÑA RECIENTEMENTE DESBLOQUEADO actual+1
                    menuID = MissionStats.campaignID;
                    menuID = menuID.substring(0, menuID.length() - 1) + (Integer.parseInt(menuID.substring(menuID.length() - 1, menuID.length())) + 1);

                    mediaPlayerPanel.updateVideo(missionsMenuCard, menuID);
                    cardName = mediaPlayerCard;
                } else {
                    System.out.println("entro?" + menuID);
                    missionsPanel.updateContentForMenu(menuID);
                }
            }
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
    public void initializeMenus() {
        menuPanel = new MenuPanel(this, mainMenuCard);
        optionsMenuPanel = new MenuPanel(this, optionsMenuCard);
        extraMenuPanel = new MenuPanel(this, extraMenuCard);
        campaignPanel = new MenuPanel(this, campaignsMenuCard);
        //selectorMenuPanel = new MenuSelectorPanel(this, selectorMenuCard);
        selectorMenuPanel = new InfoSelectorPanel(this, selectorMenuCard);
        
        buttonSelectorPanel = new ButtonSelectorPanel(this, buttonSelectorCard);
        missionsPanel = new MenuPanel(this, missionsMenuCard);
        mediaPlayerPanel = new MediaPlayerPanel(this, mediaPlayerCard);

        mainPanel.add(menuPanel, mainMenuCard);
        mainPanel.add(optionsMenuPanel, optionsMenuCard);
        mainPanel.add(extraMenuPanel, extraMenuCard);
        mainPanel.add(campaignPanel, campaignsMenuCard);
        //mainPanel.add(selectorMenuPanel, selectorMenuCard);
        mainPanel.add(selectorMenuPanel, selectorMenuCard);
        
        mainPanel.add(buttonSelectorPanel, buttonSelectorCard);
        mainPanel.add(missionsPanel, missionsMenuCard);
        mainPanel.add(mediaPlayerPanel, mediaPlayerCard);

        gameCanvas = new GameCanvas();
        mainPanel.add(gameCanvas, gameCanvasCard);
        MenuComponentFactory.createDialogs(this);
        gameCanvas.start();
    }

    @Override
    public void resume() {
        gameCanvas.resume();
    }
}
