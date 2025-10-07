/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.managers;

import java.util.Map;
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

    public GameSoundManager() {
        audioCache = Assets.audioCache;
    }

    private void playSound(String soundKey) {
        Clip clip = audioCache.get(soundKey);
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    @Override
    public void onGameObjectDestroyed(MovingObject mo) {
        if (mo instanceof Meteor || mo instanceof Ship) {
            playSound("explosion");
        }
    }

    @Override
    public void onGameNotify(String type) {
        if (type.equals("laser")) {
            playSound("shoot");
        }
    }
}
