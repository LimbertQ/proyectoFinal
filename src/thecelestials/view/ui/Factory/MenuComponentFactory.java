/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui.Factory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import thecelestials.controller.ScreenSwitcher;
import thecelestials.model.data.Assets;
import thecelestials.model.data.GameEntity;
import thecelestials.model.data.MissionStats;
import thecelestials.view.ui.GameFrame;
import thecelestials.view.ui.SelectorPanelComponent;

/**
 *
 * @author pc
 */
public class MenuComponentFactory {

    private static JDialog exitDialog, looseDialog, winDialog;
    private static JDialog closeDialog;

    public static void createDialogs(GameFrame frame) {
        ScreenSwitcher switcher = frame;
        exitDialog = createJDialog(frame, "Estas seguro que deseas salir?");
        putButtonsDialog(e -> {
            exitDialog.dispose();
            switcher.resume();
        }, exitDialog, switcher, "CONTINUAR");

        looseDialog = createJDialog(frame, "Deseas reintentar mission?");
        putButtonsDialog(e -> {
            looseDialog.dispose();
            switcher.showCard("loadingGameCard", MissionStats.missionID);
        }, looseDialog, switcher, "REINTENTAR");

        winDialog = createJDialog(frame, "Deseas continuar?");
        putButtonsDialog(e -> {
            winDialog.dispose();
            switcher.showCard("loadingGameCard", Assets.campaigns.get(MissionStats.campaignID).nextMission(MissionStats.missionID));
        }, winDialog, switcher, "CONTINUAR");
        //SALIR DEL JUEGO
        closeDialog = createJDialog(frame, "¿Desea salir del juego");
        putButtonsExitDialog(closeDialog);

    }
    
    private static void putButtonsExitDialog(JDialog dialog) {
        JLabel button1 = createClickableLabel("ACEPTAR", 1, e -> {
            Assets.closeDbConnection();
            System.exit(0); // Ahora sí, termina el proceso de la aplicación
        });
        JLabel button2 = createClickableLabel("CANCELAR", 0, e -> {
            dialog.dispose();
        });

        JPanel buttonPanel = new JPanel(); // Añadir un poco de espacio horizontal
        buttonPanel.setOpaque(false); // Hacerlo transparente para ver el fondo del diálogo

        buttonPanel.add(button1);
        buttonPanel.add(button2);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack(); // Empaqueta el diálogo para ajustar el tamaño al contenido

    }

    private static void putButtonsDialog(ActionListener action, JDialog dialog, ScreenSwitcher switcher, String textButton1) {
        JLabel button1 = createClickableLabel(textButton1, 1, action);
        JLabel button2 = createClickableLabel("ABANDONAR", 0, e -> {
            dialog.dispose();
            switcher.showCard("missionsMenuCard", MissionStats.campaignID);
        });

        JPanel buttonPanel = new JPanel(); // Añadir un poco de espacio horizontal
        buttonPanel.setOpaque(false); // Hacerlo transparente para ver el fondo del diálogo

        buttonPanel.add(button1);
        buttonPanel.add(button2);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack(); // Empaqueta el diálogo para ajustar el tamaño al contenido

    }

    public static void showDialog(int type) {
        switch (type) {
            case 1 ->
                looseDialog.setVisible(true);
            case 2 ->
                winDialog.setVisible(true);
            default ->
                exitDialog.setVisible(true);
        }
    }

    private static JDialog createJDialog(JFrame frame, String text) {
        JDialog dialog = new JDialog(frame, true);
        // En tu MenuComponentFactory, dentro del switch para cada tipo de diálogo:
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setSize(300, 150);
        dialog.setResizable(false);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));

        dialog.setLayout(new BorderLayout(10, 10));

        dialog.add(sampleLabel(text, 1, 0), BorderLayout.CENTER);
        dialog.setLocationRelativeTo(frame); // Centra el diálogo en relación al marco principal
        return dialog;
    }

    public static JPanel createContentPanelForType(String menuType, ScreenSwitcher switcher, int campaignId) {// campaignId para misiones
        JPanel panel = new JPanel();
        // Configura el layout del panel de botones aquí (ej. GridLayout, BoxLayout)
        // panel.setLayout(new GridLayout(0, 1, 10, 10)); // Ejemplo
        // panel.setOpaque(false); // Para ver el fondo del MenuPadre
        JPanel panelTitle = new JPanel();
        panelTitle.setOpaque(false);
        //panelTitle.add(titleMenu(menuType));
        JLabel title = null;
        panel.add(panelTitle);

        Map<String, ActionListener> actions;
        switch (menuType) {
            case "mainMenuCard" -> {
                //System.out.println(menug+"mrd");
                title = titleMenu("LOS CELESTIALES");
                actions = new LinkedHashMap<>();
                actions.put("SELECCION DE MISION", e -> switcher.showCard("campaignMenuCard", ""));
                actions.put("OPCIONES", e -> switcher.showCard("optionsMenuCard", ""));
                actions.put("EXTRAS", e -> switcher.showCard("extraMenuCard", ""));
                actions.put("SALIR", e -> closeDialog.setVisible(true));
                panel.add(putButtons(25, true, actions));
                //------------
                //panel.add(new JPanel());
                //panel.add(new JPanel());
                //------------------------------

            }
            case "optionsMenuCard" -> {
                title = titleMenu("OPCIONES");
                panel.add(createMenuLabel(1));
                //----------------
                actions = new LinkedHashMap<>();
                actions.put("CREDITO", e -> switcher.showCard("selectorMenuCard", "credits"));
                actions.put("INSTRUCCIONES", e -> switcher.showCard("selectorMenuCard", "tutorial"));
                actions.put("COMPRAS", e -> switcher.showCard("buttonSelectorCard", ""));
                panel.add(putButtons(34, false, actions));
                //------------

                panel.add(createBackPanel(e -> switcher.showCard("mainMenuCard", "1")));
            }
            case "extraMenuCard" -> {
                title = titleMenu("EXTRAS");
                panel.add(createMenuLabel(2));
                //----------------
                actions = new LinkedHashMap<>();
                actions.put("GALERIA", e -> switcher.showCard("selectorMenuCard", "galery"));
                actions.put("CINEMATICA", e -> switcher.showCard("selectorMenuCard", "cinematic"));
                actions.put("CIVILIZACIONES", e -> switcher.showCard("selectorMenuCard", "civilizations"));
                panel.add(putButtons(34, false, actions));
                //------------
                panel.add(createBackPanel(e -> switcher.showCard("mainMenuCard", "1")));
            }
            case "campaignMenuCard" -> {
                //System.out.println(menug+"mrd");
                title = titleMenu("campaña");
                panel.add(createMenuLabel(0));

                //------------
                actions = createActionsContent(switcher, "MENU");

                panel.add(putButtons(34, false, actions));
                //------------------------------

                panel.add(createBackPanel(e -> switcher.showCard("mainMenuCard", "1")));
                //System.out.println("grrrr");
            }
            case "selectorMenuCard" -> {
                title = titleMenu("SELECTOR CARD");
                panel.add(contentWidthLabel());
                //----------------
                panel.add(textArea());
                //------------
                //panel.add(createClickableLabel("ATRAS", 0, e -> switcher.showCard("optionsMenuCard", "1")));
                panel.add(createSelectorPanel());
            }
            case "buttonSelectorCard" -> {
                title = titleMenu("COMPRAS");
                panel.add(contentWidthLabel());
                //----------------
                panel.add(textArea());
                //------------
                panel.add(createSelectorPanel());
            }
        }
        panelTitle.add(title);
        return panel;
    }
    
    private static JPanel contentWidthLabel(){
        JPanel mainMenu = new JPanel();
        mainMenu.setBorder(BorderFactory.createEmptyBorder(286, 0, 0, 0));
        mainMenu.setOpaque(false);
        mainMenu.setLayout(new BoxLayout(mainMenu, BoxLayout.Y_AXIS));
        mainMenu.add(new JLabel());
        return mainMenu;
    }

    private static JPanel textArea() {
        JPanel contentPanel = new JPanel();
        //contentPanel.setLayout(new BorderLayout());
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(250, 50, 50, 50));
        contentPanel.setOpaque(false);

        JTextArea texto = new JTextArea();
        //texto.setMaximumSize(new Dimension(Integer.MAX_VALUE, texto.getPreferredSize().height));
        texto.setFont(Assets.fontMed);
        texto.setBackground(new Color(41, 41, 41, 130));
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        //texto.setText("alison de mrd");
        texto.setForeground(Color.WHITE);
        texto.setOpaque(true);
        texto.setText("mrd");
        contentPanel.add(texto);
        //contentPanel.add(texto, BorderLayout.CENTER);
        return contentPanel;
    }

    public static JPanel putButtons(int strut, boolean flag, Map<String, ActionListener> botones) {
        JPanel panel = new JPanel();

        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (flag) {
            panel.setBorder(BorderFactory.createEmptyBorder(286, 0, 0, 0));
        } else {
            panel.setBorder(BorderFactory.createEmptyBorder(250, 50, 50, 50));
        }

        int i = 0;
        for (Map.Entry<String, ActionListener> entry : botones.entrySet()) {
            JLabel boton;
            //conteinsForMenu.add(sampleLabel(campaign.getNom() + bloq, 1, 0));
            if (entry.getValue() == null) {
                boton = sampleLabel(entry.getKey(), 1, 0);
            } else {
                boton = createClickableLabel(entry.getKey(), 1, entry.getValue());
            }

            panel.add(boton);
            if (i < botones.size() - 1) {
                panel.add(Box.createVerticalStrut(strut));
            }
            i++;

        }
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private static JPanel createBackPanel(ActionListener action) {
        JPanel panelBack = new JPanel();
        panelBack.setLayout(new BorderLayout());
        panelBack.setBorder(BorderFactory.createEmptyBorder(0, 190, 0, 190));
        panelBack.setPreferredSize(new Dimension(468, Integer.MAX_VALUE));
        panelBack.setMaximumSize(new Dimension(468, Integer.MAX_VALUE));
        panelBack.setOpaque(false);
        JLabel backLabel = createClickableLabel("ATRAS", 0, action);
        //backLabel.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));
        panelBack.add(Box.createVerticalGlue(), BorderLayout.CENTER);
        panelBack.add(backLabel, BorderLayout.SOUTH);
        return panelBack;
    }

    private static JLabel titleMenu(String text) {
        JLabel title = new JLabel();
        title.setForeground(Color.WHITE);
        title.setFont(Assets.fontBig);
        title.setText(text);
        return title;
    }

    private static JPanel createMenuLabel(int label) {
        JPanel mainMenu = new JPanel();
        mainMenu.setBorder(BorderFactory.createEmptyBorder(286, 0, 0, 0));
        mainMenu.setOpaque(false);
        mainMenu.setLayout(new BoxLayout(mainMenu, BoxLayout.Y_AXIS));
        String[] mainMenus = {"SELECCION DE MISION", "OPCIONES", "EXTRAS", "SALIR"};
        for (int i = 0; i < mainMenus.length; i++) {
            mainMenu.add(sampleLabel(mainMenus[i], label, i));
            if (i < mainMenus.length) {
                mainMenu.add(Box.createVerticalStrut(25));
            }
        }
        mainMenu.add(Box.createVerticalGlue());
        //System.out.println(mainMenu);
        return mainMenu;
    }

    public static JLabel sampleLabel(String text, int label, int i) {
        JLabel boton = new JLabel(text);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, boton.getPreferredSize().height));
        boton.setFont(Assets.fontMed);
        if (i != label) {
            boton.setBackground(new Color(41, 41, 41, 128));
        } else {
            boton.setBackground(new Color(101, 37, 126, 128));
        }

        boton.setForeground(Color.WHITE);
        boton.setOpaque(true);
        boton.setHorizontalAlignment(SwingConstants.CENTER);
        boton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return boton;
    }
    
    public static JLabel createArrowLabel(String text){
        JLabel boton = new JLabel(text);
        Color color = new Color(41, 41, 41, 128);
        boton.setFont(Assets.fontMed);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setOpaque(true);
        boton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        boton.setVerticalAlignment(SwingConstants.CENTER);
        boton.setHorizontalAlignment(SwingConstants.CENTER);
        return boton;
    }

    public static JLabel createArrowButton(String text, ActionListener clickAction) {
        JLabel boton = new JLabel(text);
        Color color = new Color(41, 41, 41, 128);
        boton.setFont(Assets.fontMed);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setOpaque(true);
        boton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        boton.setVerticalAlignment(SwingConstants.CENTER);
        boton.setHorizontalAlignment(SwingConstants.CENTER);

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                clickAction.actionPerformed(new ActionEvent(boton, 0, "clicked"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //label.setBackground(hoverBg); // Cambia el fondo al pasar el ratón
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //label.setBackground(defaultBg); // Vuelve al fondo original al salir el ratón
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //label.setBackground(clickBg); // Fondo al presionar el ratón
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }
        });
        return boton;
    }
    
    public static JPanel createSelectorPanel(){
        JLabel currentImage = new JLabel();
        JLabel back = sampleLabel("ATRAS", 1, 1);
        JLabel left = createArrowLabel("<<");
        JLabel right = createArrowLabel(">>");
        JLabel nameImage = sampleLabel("", 1, 0);
        SelectorPanelComponent selectorPanelComponent = new SelectorPanelComponent(currentImage, back, left, right, nameImage);
        selectorPanelComponent.setLayout(new BoxLayout(selectorPanelComponent, BoxLayout.Y_AXIS));
        //setBackground(Color.red);
        selectorPanelComponent.setOpaque(false); // Hazlo transparente si quieres ver el fondo del panel padre
        JLabel selectorTitle = sampleLabel("SELECTOR", -1, 1);
        selectorTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectorTitle.setOpaque(false);
        selectorPanelComponent.add(selectorTitle);
        //------------
        JPanel contents = new JPanel();
        //contents.setLayout(new BorderLayout());
        contents.setLayout(new BoxLayout(contents, BoxLayout.X_AXIS));
        //currentImage = new JLabel();
        contents.setOpaque(false);
        contents.add(left);
        contents.add(Box.createHorizontalStrut(20));
        contents.add(currentImage);
        contents.add(Box.createHorizontalStrut(20));
        contents.add(right);
        selectorPanelComponent.add(contents);
        
        nameImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameImage.setOpaque(false);
        selectorPanelComponent.add(nameImage);
        selectorPanelComponent.add(Box.createVerticalStrut(67));

        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);

        southPanel.add(back);
        //southPanel.add(Box.createHorizontalStrut(Integer.MAX_VALUE));

        selectorPanelComponent.add(southPanel);
        return selectorPanelComponent;
    }

    // Método de utilidad para crear botones con un estilo común
    public static JLabel createClickableLabel(String text, int label, ActionListener clickAction) {

        JLabel boton = new JLabel(text);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, boton.getPreferredSize().height));
        boton.setFont(Assets.fontMed);
        if (label == 1) {
            boton.setBackground(new Color(41, 41, 41, 128));
        } else {
            boton.setBackground(new Color(101, 37, 126, 128));
            //boton.setBackground(Color.red);
            //System.out.println("soy rojo");
        }

        boton.setForeground(Color.WHITE);
        boton.setOpaque(true);
        boton.setVerticalAlignment(SwingConstants.CENTER); // Por si el JLabel tiene espacio vertical extra
        boton.setHorizontalAlignment(SwingConstants.CENTER);
        boton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickAction.actionPerformed(new java.awt.event.ActionEvent(boton, 0, "clicked"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //label.setBackground(hoverBg); // Cambia el fondo al pasar el ratón
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //label.setBackground(defaultBg); // Vuelve al fondo original al salir el ratón
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //label.setBackground(clickBg); // Fondo al presionar el ratón
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Vuelve al estado de hover si el ratón sigue encima, o al default si ya salió
                /*
                if (label.contains(e.getPoint())) { // Verifica si el ratón sigue dentro del label
                    label.setBackground(hoverBg);
                } else {
                    label.setBackground(defaultBg);
                }*/
            }
        });

        /*
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180)); // Steel Blue
        button.setFocusPainted(false); // No pintar el borde de foco
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Para BoxLayout
        button.setMaximumSize(new Dimension(300, 60)); // Tamaño máximo para centrar y uniformar*/
        return boton;
    }

    public static Map<String, ActionListener> createActionsContent(ScreenSwitcher switcher, String menuID) {
        Map<String, ActionListener> actions = new LinkedHashMap<>();
        
        if (menuID.equals("buyPanel")) {
            int cost = 5000;
            actions.put("monedas: " + Assets.money, null);
            for (int i = 5; i <= 20; i *= 2) {
                final int ii = i;
                final int costLife = cost;
                if (Assets.money >= cost) {
                    actions.put(i + "LIFE X " + cost + " Bs", e -> {
                        Assets.updatePlayerStatus(ii, -costLife);
                        switcher.showCard("buttonSelectorCard", "");
                    });
                } else {
                    actions.put(i + "LIFE X " + cost + " Bs -Bloq", null);
                }
                cost += 3000;
            }
        } else {
            Map<String, ? extends GameEntity> mapEntitys;
            String ID = menuID.substring(0, 4);
            String menuCard;
            switch (ID) {
                //CAMP01 soy el menu de mision->gameCanvasCard
                case "CAMP" -> {
                    //cargando misiones ---CORREGIR
                    menuCard = "loadingGameCard";
                    //menuCard = "gameCanvasCard";
                    mapEntitys = Assets.loadMissionsByCampaign(menuID);
                }
                //CAMPA soy menuCamp
                default -> {
                    //cargando campanias->misiones
                    menuCard = "missionsMenuCard";
                    mapEntitys = Assets.campaigns;
                }
            }

            for (GameEntity gameEntity : mapEntitys.values()) {
                if (gameEntity.getState() == 0) {
                    actions.put(gameEntity.getName() + "-Bloq", null);
                } else {
                    actions.put(gameEntity.getName(), e -> {
                        switcher.showCard(menuCard, gameEntity.getID());
                    });
                }
            }
        }
        return actions;
    }
}
