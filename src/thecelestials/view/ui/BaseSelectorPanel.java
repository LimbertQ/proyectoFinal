/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import thecelestials.controller.ScreenSwitcher;
import thecelestials.model.data.AssetDefinition;
import thecelestials.model.data.Assets;
import thecelestials.model.data.Campaign;

/**
 *
 * @author pc
 */
public abstract class BaseSelectorPanel extends BaseMenuPanel {

    protected String menuName;
    private final JLabel menuTitle;
    private final JLabel imageWestPanel;
    private final SelectorPanelComponent selector;
    private int currentIndex = 0;
    private List<? extends AssetDefinition> dataNavigator;

    public BaseSelectorPanel(ScreenSwitcher switcher, String menuType) {
        super(switcher, menuType);
        menuTitle = findComponentByType(JLabel.class, northPanel);
        imageWestPanel = findComponentByType(JLabel.class, westPanel);
        selector = findComponentByType(SelectorPanelComponent.class, this.eastPanel);
        selector.addLabelListener("previous", e -> {
            updateSelectorContent(-1);
        });
        selector.addLabelListener("next", e -> {
            updateSelectorContent(1);
        });
        selector.addLabelListener("back", e -> {
            navigateBack();
        });
    }

    protected void onMenuDataInicializate(String missionID) {
    }

    protected abstract void updateCenterContent(AssetDefinition currentAsset);

    private void navigateBack() {
        onBackButtonClicked();
    }

    protected SelectorPanelComponent getSelector() {
        return selector;
    }

    protected abstract void onBackButtonClicked();

    @Override
    public final void updateContentForMenu(String type) {
        menuName = type;
        if (Assets.campaigns.containsKey(type)) {
            //INSERTA EL TITULO DE LA VENTANA
            menuTitle.setText(Assets.campaigns.get(type).getName());
            //INSERTA LA IMAGEN DE LA VENTANA
            imageWestPanel.setIcon(new ImageIcon(Assets.campaigns.get(type).getProfile().getScaledInstance(250, 200, Image.SCALE_SMOOTH)));
            
            System.err.println(type + "esta entrando");
        }else{
            //INSERTA EL TITULO DE LA VENTANA
            menuTitle.setText(type);
            //INSERTA LA IMAGEN DE LA VENTANA
            imageWestPanel.setIcon(new ImageIcon(Assets.fondo.getScaledInstance(250, 200, Image.SCALE_SMOOTH)));
        }
        //INSERTA LA IMAGEN Y NOMBRE DEL SELECTOR
        //---------------------------------------;
        //dataNavigator = Assets.informations.get(type);
        System.err.println(type);
        switch (type) {
            case "galery", "buttonSelectorCard" ->
                dataNavigator = Assets.shipsAvaible;
            case "credits", "tutorial", "civilizations" ->
                dataNavigator = Assets.informations.get(type);
            case "cinematic" -> {
                List<AssetDefinition> temp = new ArrayList<>();
                for (Map.Entry<String, Campaign> entry : Assets.campaigns.entrySet()) {
                    temp.add(entry.getValue());
                }
                dataNavigator = temp;
            }
            default ->
                dataNavigator = Assets.shipsAvaible;
        }
        //---------------------------------------;
        currentIndex = 0;
        updateSelectorContent(0);
        onMenuDataInicializate(type);
    }

    public void changeDataNavigator(List<? extends AssetDefinition> data) {
        currentIndex = 0;
        dataNavigator = data;
    }

    protected void updateSelectorContent(int i) {
        if (dataNavigator == null || dataNavigator.isEmpty()) {
            return;
        }

        currentIndex += i;
        if (currentIndex >= 0 && currentIndex < dataNavigator.size()) {
            AssetDefinition currentAsset = dataNavigator.get(currentIndex);

            selector.setSelectorItemIcon(currentAsset.getProfile(), currentAsset.getName());
            updateCenterContent(currentAsset);
        } else {
            currentIndex -= i;
        }
    }
}
