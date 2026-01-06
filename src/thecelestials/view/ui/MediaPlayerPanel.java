/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.BorderLayout;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import thecelestials.controller.ScreenSwitcher;
import thecelestials.model.data.Assets;
import thecelestials.model.data.ProgressionManager;

/**
 *
 * @author pc
 */
public class MediaPlayerPanel extends BasePanel{
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    public MediaPlayerPanel(ScreenSwitcher switcher, String menuType) {
        super(switcher);
        setLayout(new BorderLayout());
        JFXPanel fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);
        
        Platform.runLater(() -> {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root);
            fxPanel.setScene(scene);
            mediaView = new MediaView();
            root.setCenter(mediaView);

            mediaView.fitWidthProperty().bind(root.widthProperty());
            mediaView.fitHeightProperty().bind(root.heightProperty());
            mediaView.setPreserveRatio(false);
        });
    }
    
    public void updateVideo(String menuCard, String campaignID){
        if(ProgressionManager.getInstance().nextMenu().equals("mediaPlayerCard")){
            ProgressionManager.getInstance().unlockInit();
            ProgressionManager.getInstance().reset();
            switcher.updateMenus();
            abrirArchivo("missionsMenuCard", campaignID, campaignID);
        }else{
            abrirArchivo("selectorMenuCard", campaignID, "cinematica");
        }
    }

    private void abrirArchivo(String menuCard, String campaignID, String goal) {
        Platform.runLater(() -> {
            // Limpiar el reproductor viejo antes de crear uno nuevo
            if (mediaPlayer != null) {
                mediaPlayer.dispose();
            }
            String mediaURL = getClass().getResource(Assets.campaigns.get(campaignID).getVideoPath()).toExternalForm();
            
            Media media = new Media(mediaURL);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            mediaView.setOnMouseClicked(event -> {
                mediaPlayer.stop();
                mediaPlayer.dispose(); // Agrega dispose() aquí
                
                switcher.showCard(menuCard, goal);
            });

            mediaPlayer.setOnReady(() -> {
                mediaPlayer.play();
            });

            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.stop();
                mediaPlayer.dispose(); // Agrega dispose() aquí
                switcher.showCard(menuCard, goal);
            });
        });
    }
}
