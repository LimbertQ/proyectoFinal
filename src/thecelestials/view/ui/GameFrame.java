/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import thecelestials.controller.ScreenSwitcher;
import thecelestials.model.data.Assets;
import thecelestials.model.data.ProgressionManager;
import thecelestials.model.math.Constants;
import thecelestials.view.ui.Factory.MenuComponentFactory;

/**
 *
 * @author Limbert Quispe
 */
public class GameFrame extends JFrame implements ScreenSwitcher {

    private final CardLayout cardLayout;
    private final LoadingPanel loadingPanel;
    private final JPanel mainPanel;
    private SimpleMenuPanel menuPanel;
    private SimpleMenuPanel optionsMenuPanel;
    private SimpleMenuPanel extraMenuPanel;
    private SimpleMenuPanel campaignPanel;
    //private MenuPanel missionsPanel;
    //private MenuSelectorPanel selectorMenuPanel;
    private InfoDisplayPanel selectorMenuPanel;
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
                Assets.closeDbConnection();
                System.exit(0);
            }
        });

        // 1. OBTENER EL TAMAÑO REAL DE LA COMPU QUE ABRE EL JUEGO
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenW = screenSize.width;
        int screenH = screenSize.height;

        // 2. TU RESOLUCIÓN DE DISEÑO (Donde todo se ve bien)
        // Pon aquí la resolución de TU computadora
        int baseW = 1366;
        int baseH = 768;

        // 3. LA LÓGICA DE SEGURIDAD
        // Si la pantalla es más chica que tu juego, usamos el tamaño de la pantalla.
        // Si la pantalla es más grande, usamos tu tamaño de diseño (y habrá bordes negros).
        int finalW = Math.min(baseW, screenW);
        int finalH = Math.min(baseH, screenH);
        Dimension safeSize = new Dimension(finalW, finalH);
        Constants.init(finalW, finalH);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        // 4. EL CONTENEDOR DE BORDES NEGROS
        JPanel backgroundWrapper = new JPanel(new GridBagLayout());
        backgroundWrapper.setBackground(Color.BLACK);

        cardLayout = new CardLayout();

        // 5. TU MAINPANEL
        mainPanel = new JPanel(cardLayout);

        // Usamos el tamaño "seguro" para que nunca se salga de la pantalla
        mainPanel.setPreferredSize(safeSize);
        mainPanel.setMinimumSize(safeSize);
        mainPanel.setMaximumSize(safeSize);

        loadingPanel = new LoadingPanel(loadingCard, this);
        loadingPanel.nextPanel(mainMenuCard, "");
        mainPanel.add(loadingPanel, loadingCard);

        // 6. ENSAMBLAJE
        backgroundWrapper.add(mainPanel);
        add(backgroundWrapper, BorderLayout.CENTER);

        cardLayout.show(mainPanel, loadingCard);
        setVisible(true);

    }

    @Override
    public void showCard(String cardName, String menuID) {
        switch (cardName) {
            case "loadingGameCard" -> {
                loadingPanel.nextPanel(gameCanvasCard, menuID);
                cardName = loadingCard;
            }
            case mediaPlayerCard -> {
                mediaPlayerPanel.updateVideo("", menuID);
            }
            case selectorMenuCard ->
                selectorMenuPanel.updateContentForMenu(menuID);
            case buttonSelectorCard -> {
                buttonSelectorPanel.updateContentForMenu("compras");
            }
            case missionsMenuCard -> {
                cardName = buttonSelectorCard;
                if (ProgressionManager.getInstance().nextMenu().equals("mediaPlayerCard")) {
                    campaignPanel.updateContentForMenu("unlock");
                    cardName = mediaPlayerCard;
                    mediaPlayerPanel.updateVideo("", menuID);
                }
                buttonSelectorPanel.updateContentForMenu(menuID);
            }
            case gameCanvasCard -> {
                gameCanvas.playGame();
            }
            default -> {
            }
        }
        cardLayout.show(mainPanel, cardName);
    }

    @Override
    public void initializeMenus() {
        menuPanel = new SimpleMenuPanel(this, mainMenuCard);
        optionsMenuPanel = new SimpleMenuPanel(this, optionsMenuCard);
        extraMenuPanel = new SimpleMenuPanel(this, extraMenuCard);
        campaignPanel = new SimpleMenuPanel(this, campaignsMenuCard);
        //selectorMenuPanel = new MenuSelectorPanel(this, selectorMenuCard);
        selectorMenuPanel = new InfoDisplayPanel(this, selectorMenuCard);

        buttonSelectorPanel = new ButtonSelectorPanel(this, buttonSelectorCard);
        //missionsPanel = new MenuPanel(this, missionsMenuCard);
        mediaPlayerPanel = new MediaPlayerPanel(this, mediaPlayerCard);

        mainPanel.add(menuPanel, mainMenuCard);
        mainPanel.add(optionsMenuPanel, optionsMenuCard);
        mainPanel.add(extraMenuPanel, extraMenuCard);
        mainPanel.add(campaignPanel, campaignsMenuCard);
        //mainPanel.add(selectorMenuPanel, selectorMenuCard);
        mainPanel.add(selectorMenuPanel, selectorMenuCard);

        mainPanel.add(buttonSelectorPanel, buttonSelectorCard);
        //mainPanel.add(missionsPanel, missionsMenuCard);
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
