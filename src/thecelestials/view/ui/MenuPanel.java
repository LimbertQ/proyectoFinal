/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import thecelestials.controller.ScreenSwitcher;
import thecelestials.model.data.Assets;
import thecelestials.view.ui.Factory.MenuComponentFactory;

/**
 *
 * @author pc
 */
public class MenuPanel extends JPanel{
    private final ScreenSwitcher switcher;
    private JPanel contentWest;
    private JPanel contentCenter; // Contendrá los componentes específicos del menú (botones, sliders, etc.)

    public MenuPanel(ScreenSwitcher switcher, String menuType) {
        this.switcher = switcher;
        
        setLayout(new BorderLayout());

        // 1. Crear contentPanel UNA SOLA VEZ
        setBorder(BorderFactory.createEmptyBorder(80, 40, 80, 40)); // Borde para separación y posición

        
        loadMenuContent(menuType);
        revalidate();
        repaint();
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
        add(comps[0], BorderLayout.NORTH);
        //poner el panel izquierdo
        contentWest = (JPanel) comps[1];
        add(contentWest, BorderLayout.WEST);
        contentWest.revalidate();
        contentWest.repaint();

        //if (!menuType.equals(MenuComponentFactory.TYPE_MAIN_MENU)) {
            //poner el panel central
            contentCenter = (JPanel) comps[2];
            add(contentCenter, BorderLayout.CENTER);
            //poner el boton atras o el shipSelectorPanel
            add(comps[3], BorderLayout.EAST);

            contentCenter.revalidate();
            contentCenter.repaint();
        //}
        // 6. Opcional: limpiar el panel temporal si ya no lo necesitas
        newContentFromFactory.removeAll();

        // 7. Revalidar y repintar el contentPanel EXISTENTE
    }
    
    public void updateContentForMenu(String type) {
        //int typ = 0;
        contentCenter.removeAll();
        JPanel newContentFromFactory = MenuComponentFactory.putButtons(34, false, MenuComponentFactory.createActionsContent(switcher, type));
        
        Component[] comps = newContentFromFactory.getComponents();
        for (Component comp : comps) {
            contentCenter.add(comp);
        }
        //PONER LA IMAGEN DE LOS MAPAS
        contentWest.removeAll();
        JLabel imageShow = new JLabel();
        ImageIcon imagen = new ImageIcon(Assets.campaigns.get(type).getProfile());
        imageShow.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH)));
        contentWest.add(imageShow);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(Assets.fondo, 0, 0, getWidth(), getHeight(), this);
    }
}
