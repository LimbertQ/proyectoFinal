/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.Component;
import javax.swing.JPanel;
import thecelestials.controller.ScreenSwitcher;
import thecelestials.model.data.AssetDefinition;
import thecelestials.model.data.Assets;
import thecelestials.view.ui.Factory.MenuComponentFactory;

/**
 *
 * @author pc
 */
public class ButtonSelectorPanel extends BaseSelectorPanel {

    public ButtonSelectorPanel(ScreenSwitcher switcher, String menuType) {
        super(switcher, menuType);
    }

    @Override
    protected void onMenuDataInicializate(String missionID) {

        centerPanel.removeAll();
        //String destino = "buyPanel";
        if (this.menuName.equals("buttonSelectorCard")) {
            getSelector().setItemNameLabelText("vidas: " + Assets.lives);
            missionID = "buyPanel";
        }else{
            super.onMenuDataInicializate(Assets.campaigns.get(missionID).getName());
        }
        
        JPanel newContentFromFactory = MenuComponentFactory.putButtons(34, false, MenuComponentFactory.createActionsContent(switcher, missionID));

        Component[] comps = newContentFromFactory.getComponents();

        for (Component comp : comps) {
            centerPanel.add(comp);
        }
        centerPanel.revalidate();
        centerPanel.repaint();

    }

    @Override
    protected void updateCenterContent(AssetDefinition currentAsset) {

    }

    @Override
    protected void onBackButtonClicked() {
        String destinationCard = switch (menuName) {
            case "buttonSelectorCard" ->
                "optionsMenuCard";
            default ->
                "campaignMenuCard"; // Caso de seguridad
        };
        this.switcher.showCard(destinationCard, "");
    }
}
