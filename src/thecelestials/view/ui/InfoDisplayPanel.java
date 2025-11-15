/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import javax.swing.JTextArea;
import thecelestials.controller.ScreenSwitcher;
import thecelestials.model.data.AssetDefinition;

/**
 *
 * @author pc
 */
public class InfoDisplayPanel extends BaseSelectorPanel {

    private final JTextArea infoTextArea;
    private String currentAssetID = null;

    public InfoDisplayPanel(ScreenSwitcher switcher, String menuType) {
        super(switcher, menuType);
        infoTextArea = findComponentByType(JTextArea.class, centerPanel);
        getSelector().addLabelListener("imageName", e -> {
            onImageNameClicked();
        });
    }

    private void onImageNameClicked() {
        if (menuName.equals("cinematic")) {
            this.switcher.showCard("mediaPlayerCard", currentAssetID);
        }
    }

    @Override
    protected void updateCenterContent(AssetDefinition currentAsset) {
        infoTextArea.setText(currentAsset.getDescription());
        if (menuName.equals("cinematic")) {
            currentAssetID = currentAsset.getID();
        }
    }

    @Override
    protected void onBackButtonClicked() {
        String destinationCard = switch (menuName) {
            case "tutorial", "credits" ->
                "optionsMenuCard";
            default ->
                "extraMenuCard"; // Caso de seguridad
        };
        this.switcher.showCard(destinationCard, "");
    }
}
