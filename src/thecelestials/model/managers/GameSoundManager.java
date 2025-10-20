/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.sound.sampled.Clip;
import thecelestials.model.data.Assets;
import thecelestials.model.gameObjects.Meteor;
import thecelestials.model.gameObjects.MovingObject;
import thecelestials.model.gameObjects.Ship;

/**
 *
 * @author pc
 */
public class GameSoundManager implements GameObjectDestroyedListener, GameNotificationListener {

    private final Map<String, Clip> audioCache;
    private final List<Clip> pauseClip = new ArrayList<>();
    private final Random random = new Random();
    public GameSoundManager() {
        audioCache = Assets.audioCache;
    }
    
    public void clear(){
        for(Clip clip : audioCache.values()){
            clip.stop();
            clip.setFramePosition(0);
        }
    }

    private void playSound(String soundKey) {
        Clip clip = audioCache.get(soundKey);
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
    
    public void pause(){
        for (Map.Entry<String, Clip> entry : audioCache.entrySet()) {
            Clip sound = entry.getValue();
            if (sound != null && sound.isRunning()) {
                pauseClip.add(sound);
                sound.stop();
            }
        }
    }
    
    public void resume(){
        for(Clip clip : pauseClip){
            clip.setFramePosition(clip.getFramePosition());
            clip.start();
        }
        pauseClip.clear();
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
            case "laser" -> playSound("shoot");
            case "WAVE" -> playSound("wave"+random.nextInt(11));
            case "ASSAULT" -> playSound("assault"+random.nextInt(12));
            default -> {
            }
        }
    }
}
