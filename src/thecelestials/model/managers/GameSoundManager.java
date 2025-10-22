/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javax.sound.sampled.Clip;
import thecelestials.model.data.Assets;
import thecelestials.model.data.MissionStats;
import thecelestials.model.gameObjects.Meteor;
import thecelestials.model.gameObjects.MovingObject;
import thecelestials.model.gameObjects.Ship;

/**
 *
 * @author pc
 */
public class GameSoundManager implements GameObjectDestroyedListener, GameNotificationListener {

    private final Map<String, Clip> audioCache;
    private final Map<String, MediaPlayer> mediaCache;
    private final List<Clip> pauseClip = new ArrayList<>();
    private final Random random = new Random();

    public GameSoundManager() {
        audioCache = Assets.audioCache;
        mediaCache = Assets.audioMediaCache;
    }

    public void clear() {
        for (Clip clip : audioCache.values()) {
            clip.stop();
            clip.setFramePosition(0);
        }
    }

    public void playGame() {
        MediaPlayer player = mediaCache.get("warzone");
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
    }

    private void playSound(String soundKey) {
        Clip clip = audioCache.get(soundKey);
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    private void mediaSound(String soundKey, boolean flag) {
        if (flag) {
            MediaPlayer sound = MissionStats.missionVoicePath.get(soundKey);

            if (sound != null) {
                sound.pause();
                sound.seek(Duration.ZERO);
                sound.setOnEndOfMedia(() -> {
                    sound.dispose();
                });
                sound.play();
            }
        }else{
            MediaPlayer sound = mediaCache.get(soundKey);
            if (sound != null) {
                sound.pause();
                sound.seek(Duration.ZERO);
                sound.play();
            }
        }
    }

    public void pause() {
        for (Map.Entry<String, Clip> entry : audioCache.entrySet()) {
            Clip sound = entry.getValue();
            if (sound != null && sound.isRunning()) {
                pauseClip.add(sound);
                sound.stop();
            }
        }

        for (MediaPlayer mediaPlayer : mediaCache.values()) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            }
        }
        
        for (MediaPlayer mediaPlayer : MissionStats.missionVoicePath.values()) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            }
        }
    }

    public void resume() {
        for (Clip clip : pauseClip) {
            clip.setFramePosition(clip.getFramePosition());
            clip.start();
        }
        pauseClip.clear();

        for (MediaPlayer mediaPlayer : mediaCache.values()) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                mediaPlayer.play();
            }
        }
        
        for (MediaPlayer mediaPlayer : MissionStats.missionVoicePath.values()) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                mediaPlayer.play();
            }
        }
    }

    @Override
    public void onGameObjectDestroyed(MovingObject mo) {
        if (mo instanceof Meteor || mo instanceof Ship) {
            playSound("explosion");
        }
    }

    @Override
    public void onGameNotify(String reason) {
        switch (reason) {
            case "DESCRIPTION" ->
                mediaSound("voiceStartPath", true);
            case "laser" ->
                playSound("shoot");
            case "WAVE" ->
                playSound("wave" + random.nextInt(11));
            case "ASSAULT" ->
                playSound("assault" + random.nextInt(12));
            case "VICTORY" -> {
                mediaSound("voiceEndPath", true);
                mediaSound("endingSong", false);
            }
            case "GAME OVER" ->
                mediaSound("loosingSong", false);
            default -> {
            }
        }
    }
}
