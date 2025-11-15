/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import thecelestials.controller.ScreenSwitcher;
import thecelestials.model.data.Assets;

/**
 *
 * @author pc
 */
public class InfoSelectorPanel extends BaseMenuPanel{
    private final JLabel menuTitle;
    private final JLabel imageWestPanel;
    private final SelectorPanelComponent selector;
    public InfoSelectorPanel(ScreenSwitcher switcher, String menuType) {
        super(switcher, menuType);
        menuTitle = findComponentByType(JLabel.class, northPanel);
        imageWestPanel = findComponentByType(JLabel.class, westPanel);
        //selector = (SelectorPanelComponent)eastPanel;
        selector = findComponentByType(SelectorPanelComponent.class, this.eastPanel);
    }

    @Override
    public void updateContentForMenu(String type) {
        //INSERTA EL TITULO DE LA VENTANA
        menuTitle.setText(type);
        //INSERTA LA IMAGEN DE LA VENTANA
        imageWestPanel.setIcon(new ImageIcon(Assets.fondo.getScaledInstance(250, 200, Image.SCALE_SMOOTH)));
        //INSERTA LA IMAGEN DEL SELECTOR
        selector.setSelectorItemIcon(Assets.fondo, "hola");
    }
}