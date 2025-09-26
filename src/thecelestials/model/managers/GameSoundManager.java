/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.managers;

import javax.sound.sampled.Clip;
import thecelestials.model.data.Assets;
import thecelestials.model.gameObjects.Meteor;
import thecelestials.model.gameObjects.MovingObject;
import thecelestials.model.gameObjects.PlayerShip;

/**
 *
 * @author pc
 */
public class GameSoundManager implements GameObjectDestroyedListener {

    private final Clip explosion;

    public GameSoundManager() {
        explosion = Assets.explosion;
    }

    @Override
    public void onGameObjectDestroyed(MovingObject mo) {
        if (mo instanceof Meteor || mo instanceof PlayerShip) {
            explosion.stop();
            explosion.setFramePosition(0);
            explosion.start();
        }
    }
}
