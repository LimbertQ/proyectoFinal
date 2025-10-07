/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui.managers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import thecelestials.model.data.Assets;
import thecelestials.model.gameObjects.Meteor;
import thecelestials.model.gameObjects.MovingObject;
import thecelestials.model.gameObjects.NPCShip;
import thecelestials.model.gameObjects.PlayerShip;
import thecelestials.model.managers.GameObjectDestroyedListener;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;
import thecelestials.view.ui.animations.Message;

/**
 *
 * @author pc
 */
public class GameMessageManager implements GameObjectDestroyedListener {

    private final List<Message> activeMessages;

    public GameMessageManager() {
        activeMessages = new ArrayList<>();
    }

    public void showMessage(Vector2D pos, String text, Color color) {
        activeMessages.add(new Message(pos, false, text, color, false, Assets.fontMed, 1));
    }

    @Override
    public void onGameObjectDestroyed(MovingObject destroyedObject) {
        if (destroyedObject instanceof Meteor) {
            showMessage(destroyedObject.getPosition(), "+" + Constants.METEOR_SCORE + " SCORE", Color.WHITE);
        } else if (destroyedObject instanceof PlayerShip player && player.isDead()) {
            showMessage(destroyedObject.getPosition(), "-1 LIFE", Color.RED);
        } else if (destroyedObject instanceof NPCShip npcShip && npcShip.getTeam() == 0) {
            showMessage(destroyedObject.getPosition(), "+" + Constants.UFO_SCORE + " SCORE", Color.WHITE);
        }    
    }

    public void update(float deltaTime) {
        activeMessages.removeIf(Message::isDead);
    }

    public void draw(Graphics2D g) {
        for (Message msg : activeMessages) {
            msg.draw(g);
        }
        activeMessages.removeIf(Message::isDead);
    }
}
