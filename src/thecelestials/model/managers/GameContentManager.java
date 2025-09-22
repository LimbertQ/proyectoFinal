/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.managers;

import java.awt.Graphics;
import java.util.ArrayList;
import thecelestials.model.data.Assets;
import thecelestials.model.gameObjects.MovingObject;
import thecelestials.model.gameObjects.PlayerShip;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public class GameContentManager {

    private final ArrayList<MovingObject> movingObjects = new ArrayList<>();
    private final ArrayList<MovingObject> listToAdd = new ArrayList<>();
    private final PlayerShip player;
    public GameContentManager() {
        player = new PlayerShip(new Vector2D(1366/2-Assets.player.getWidth(), 768/2), new Vector2D(), Assets.player, this, Assets.effect);
        movingObjects.add(player);
    }

    public void add(MovingObject mo) {
        listToAdd.add(mo);
    }

    public void update(float dt) {
        for (MovingObject mo : movingObjects) {
            mo.update(dt);
        }

        for (int i = 0; i < movingObjects.size(); i++) {
            MovingObject mo = movingObjects.get(i);
            if (mo.isDead()) {
                movingObjects.remove(mo);
            }
        }

        if (!listToAdd.isEmpty()) {
            movingObjects.addAll(listToAdd);
        }
    }

    public void draw(Graphics g) {
        for (MovingObject mo : movingObjects) {
            mo.draw(g);
        }
    }
}
