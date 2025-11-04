/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import thecelestials.controller.ScreenSwitcher;
import thecelestials.model.data.Assets;
import thecelestials.view.ui.Factory.MenuComponentFactory;

/**
 *
 * @author pc
 */
public class MenuSelectorPanel extends JPanel{
    private final ScreenSwitcher switcher;
    private JLabel menuTitle;
    private JLabel currentImage;
    private JLabel nameImage;
    private JPanel contentWest;
    private JPanel contentCenter; // Contendrá los componentes específicos del menú (botones, sliders, etc.)
    private JTextArea information;
    
    private String MenuPanelType;
    public MenuSelectorPanel(ScreenSwitcher switcher, String menuType) {
        this.switcher = switcher;
        this.MenuPanelType = menuType;
        setLayout(new BorderLayout());

        // 1. Crear contentPanel UNA SOLA VEZ
        setBorder(BorderFactory.createEmptyBorder(80, 40, 80, 40)); // Borde para separación y posición

        loadMenuContent(menuType);
        revalidate();
        repaint();
    }
    
    private JPanel createSelectorPanel(JLabel back){
        JPanel selector = new JPanel();

        selector.setLayout(new BoxLayout(selector, BoxLayout.Y_AXIS));
        //setBackground(Color.red);
        selector.setOpaque(false); // Hazlo transparente si quieres ver el fondo del panel padre
        
        //-----------------
        JLabel selectorTitle = MenuComponentFactory.sampleLabel("SELECTOR", -1, 1);
        selectorTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectorTitle.setOpaque(false);
        selector.add(selectorTitle);
        //--------------------
        
        
        
        JPanel contents = new JPanel();
        //contents.setLayout(new BorderLayout());
        contents.setLayout(new BoxLayout(contents, BoxLayout.X_AXIS));
        JLabel left = MenuComponentFactory.createArrowButton("<<", e ->{nextShip(-1);});
        JLabel right = MenuComponentFactory.createArrowButton("<<", e ->{nextShip(1);});
        currentImage = new JLabel();
        currentImage.setIcon(new ImageIcon(Assets.getCurrentShip().getProfile().getScaledInstance(340, 340, Image.SCALE_SMOOTH)));
        
        contents.setOpaque(false);
        contents.add(left);
        contents.add(Box.createHorizontalStrut(20));
        contents.add(currentImage);
        contents.add(Box.createHorizontalStrut(20));
        contents.add(right);
        selector.add(contents);
        
        nameImage = MenuComponentFactory.sampleLabel("name", -1, 1);
        nameImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameImage.setOpaque(false);
        selector.add(nameImage);
        selector.add(Box.createVerticalStrut(67));
        
        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        
        southPanel.add(back);
        //southPanel.add(Box.createHorizontalStrut(Integer.MAX_VALUE));
        
        
        selector.add(southPanel);
        return selector;
    }
    
    private void nextShip(int n){
        
    }
    
    private void loadMenuContent(String menuType) {
        // 1. Limpiar el contentPanel EXISTENTE

        // 2. Obtener los componentes del factory en un panel TEMPORAL
        JPanel newContentFromFactory = MenuComponentFactory.createContentPanelForType(menuType, switcher, 0);

        // 3. Establecer el Layout del contentPanel EXISTENTE
        // 4. Añadir el glue si quieres empujar los botones hacia abajo (dentro del contentPanel)
        //contentPanel.add(Box.createVerticalGlue()); 
        // 5. Copiar los componentes del panel temporal al contentPanel EXISTENTE
        Component[] comps = newContentFromFactory.getComponents();
        //poner el titulo primero en la parte de arriba
        JPanel panelsito = (JPanel)comps[0];
        menuTitle = (JLabel)panelsito.getComponent(0);
        add(menuTitle, BorderLayout.NORTH);
        //poner el panel izquierdo
        contentWest = (JPanel) comps[1];
        add(contentWest, BorderLayout.WEST);
        contentWest.revalidate();
        contentWest.repaint();

        //poner el panel central
        contentCenter = (JPanel) comps[2];
        information = (JTextArea) contentCenter.getComponent(0);
        information.setText("este es mi texto");
        
        
        add(contentCenter, BorderLayout.CENTER);
        //poner el boton atras o el shipSelectorPanel
        add(comps[3], BorderLayout.EAST);

        contentCenter.revalidate();
        contentCenter.repaint();
        //derecho
        JLabel exit = MenuComponentFactory.createClickableLabel("ATRAS", 0, null);
        add(createSelectorPanel(exit), BorderLayout.EAST);
        // 6. Opcional: limpiar el panel temporal si ya no lo necesitas
        newContentFromFactory.removeAll();

        // 7. Revalidar y repintar el contentPanel EXISTENTE
    }
    
    public void updateContentForMenu(String type) {
        information.setText(Assets.getInformation());
        /*
        contentCenter.removeAll();
        JPanel newContentFromFactory = MenuComponentFactory.putButtons(34, false, MenuComponentFactory.createActionsContent(switcher, type));
        
        Component[] comps = newContentFromFactory.getComponents();
        for (Component comp : comps) {
            contentCenter.add(comp);
        }*/
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(Assets.fondo, 0, 0, getWidth(), getHeight(), this);
    }
}
