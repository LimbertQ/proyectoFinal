/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.Component;
import javax.swing.JPanel;
import thecelestials.controller.ScreenSwitcher;
import thecelestials.view.ui.Factory.MenuComponentFactory;

/**
 *
 * @author pc
 */
public class SimpleMenuPanel extends BaseMenuPanel{

    public SimpleMenuPanel(ScreenSwitcher switcher, String menuType) {
        super(switcher, menuType);
    }

    @Override
    public void updateContentForMenu(String type) {
        if (type.equals("unlock")) {
            centerPanel.removeAll();
            JPanel newContentFromFactory = MenuComponentFactory.putButtons(34, false, MenuComponentFactory.createActionsContent(switcher, "MENU"));

            Component[] comps = newContentFromFactory.getComponents();
            for (Component comp : comps) {
                centerPanel.add(comp);
            }
        }
    }
    
}
