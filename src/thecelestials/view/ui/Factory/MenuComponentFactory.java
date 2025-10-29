/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui.Factory;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.SwingConstants;
import thecelestials.controller.ScreenSwitcher;
import thecelestials.model.data.Assets;
import thecelestials.model.data.GameEntity;
import thecelestials.model.data.MissionStats;
import thecelestials.view.ui.GameFrame;
import thecelestials.view.ui.ShipSelectorPanel;

/**
 *
 * @author pc
 */
public class MenuComponentFactory {
    private static JDialog exitDialog, looseDialog, winDialog;
    public static void createDialogs(GameFrame frame){
        ScreenSwitcher switcher = frame;
        exitDialog = createJDialog(frame, "Estas seguro que deseas salir?");
        putButtonsDialog(e -> {exitDialog.dispose(); switcher.resume();}, exitDialog, switcher, "CONTINUAR");
        
        looseDialog = createJDialog(frame, "Deseas reintentar mission?");
        putButtonsDialog(e -> {looseDialog.dispose(); switcher.showCard("loadingGameCard", MissionStats.missionID);}, looseDialog, switcher, "REINTENTAR");
        
        winDialog = createJDialog(frame, "Deseas continuar?");
        putButtonsDialog(e -> {winDialog.dispose(); switcher.showCard("loadingGameCard", Assets.campaigns.get(MissionStats.campaignID).nextMission(MissionStats.missionID));}, winDialog, switcher, "CONTINUAR");
    }
    
    private static void putButtonsDialog(ActionListener action, JDialog dialog, ScreenSwitcher switcher, String textButton1){
        JLabel button1 = createClickableLabel(textButton1, 1, action);
        JLabel button2 = createClickableLabel("ABANDONAR", 0, e->{dialog.dispose(); switcher.showCard("missionsMenuCard", MissionStats.campaignID);});
        
        JPanel buttonPanel = new JPanel(); // Añadir un poco de espacio horizontal
        buttonPanel.setOpaque(false); // Hacerlo transparente para ver el fondo del diálogo
        
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack(); // Empaqueta el diálogo para ajustar el tamaño al contenido
        
    }
    
    public static void showDialog(int type){
        switch (type) {
            case 1 -> looseDialog.setVisible(true);
            case 2 -> winDialog.setVisible(true);
            default -> exitDialog.setVisible(true);
        }
    }
    
    private static JDialog createJDialog(JFrame frame, String text){
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
            case "campaignMenuCard" -> {
                //System.out.println(menug+"mrd");
                title = titleMenu("campaña");
                panel.add(createMenuLabel(0));

                //------------
                actions = createActionsContent(switcher, "MENU");

                panel.add(putButtons(34, false, actions));
                //------------------------------

                panel.add(createBackPanel(e -> switcher.showCard("MAIN_MENU_CARD", "1")));
                //System.out.println("grrrr");
            }
            case "missionsMenuCard" -> {
                title = titleMenu("misiones");
                panel.add(createMenuLabel(2));

                JPanel missions = new JPanel();
                missions.setBorder(BorderFactory.createEmptyBorder(250, 50, 50, 50));
                missions.setOpaque(false);
                missions.setLayout(new BoxLayout(missions, BoxLayout.Y_AXIS));
                //missions.add(Box.createVerticalGlue());
                panel.add(missions);

                JLabel back = createClickableLabel("ATRAS", 0, e -> switcher.showCard("campaignMenuCard", "1"));
                //back.setSize(50,10);
                //------AQUI
                ShipSelectorPanel selector = new ShipSelectorPanel(back);
                //SelectorPanel selector = new SelectorPanel(back);
                panel.add(selector);
                //------------------------------

            }
        }
        panelTitle.add(title);
        return panel;
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
        Map<String, ? extends GameEntity> mapEntitys;
        String ID = menuID.substring(0, 4);
        String menuCard;
        switch (ID) {
            //CAMP01 soy mision->gameCanvasCard
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
        return actions;
    }
}
