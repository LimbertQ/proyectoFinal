/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import thecelestials.controller.ScreenSwitcher;
import thecelestials.model.data.Assets;
import thecelestials.view.ui.Factory.MenuComponentFactory;

/**
 *
 * @author pc
 */
public abstract class BaseMenuPanel extends BasePanel {

    protected JPanel northPanel;
    protected JPanel centerPanel;
    protected JPanel westPanel;
    protected JPanel eastPanel;

    public BaseMenuPanel(ScreenSwitcher switcher, String menuType) {
        super(switcher);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(80, 40, 80, 40)); // Borde para separación y posición

        loadContent(menuType);
    }

    private void loadContent(String menuType) {
        JPanel newContentFromFactory = MenuComponentFactory.createContentPanelForType(menuType, switcher, 0);
        Component[] comps = newContentFromFactory.getComponents();
        //poner el titulo primero en la parte de arriba
        northPanel = (JPanel) comps[0];
        add(northPanel, BorderLayout.NORTH);

        westPanel = (JPanel) comps[1];
        add(westPanel, BorderLayout.WEST);
        if (!menuType.equals("mainMenuCard")) {
            centerPanel = (JPanel) comps[2];
            add(centerPanel, BorderLayout.CENTER);
            //poner el boton atras o el shipSelectorPanel
            eastPanel = (JPanel) comps[3];
            add(eastPanel, BorderLayout.EAST);
        }
    }

    public abstract void updateContentForMenu(String type);

    protected <T extends Component> T findComponentByType(Class<T> type, Container parent) {
        if (type.isInstance(parent)) {
            return type.cast(parent); // Si el contenedor es el SelectorPanelComponent, devolverlo.
        }
        // 1. Itera sobre TODOS los componentes hijos directos
        for (Component comp : parent.getComponents()) {

            // Comprobación de Éxito: Si el componente actual es del tipo buscado
            if (type.isInstance(comp)) {
                return type.cast(comp); // ¡Éxito! Retorna y termina la función
            }

            // Comprobación Recursiva: Si el componente actual es otro contenedor (ej. JPanel, JScrollPane)
            if (comp instanceof Container container) {
                // Llama a la función recursivamente para buscar dentro de este contenedor hijo
                T found = findComponentByType(type, container);
                if (found != null) {
                    return found; // Si se encuentra en una rama más profunda, lo devuelve
                }
            }

            // Si no se encuentra ni en el componente ni en el contenedor anidado, el bucle continúa
        }

        // 2. Comprobación de Fallo: SOLO se retorna null después de revisar TODOS los componentes
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(Assets.fondo, 0, 0, getWidth(), getHeight(), this);
    }
}
