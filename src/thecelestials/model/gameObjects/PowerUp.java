/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public class PowerUp extends GameObject {

    private final PowerUpTypes type;
    private final BufferedImage orb;
    private final PlayerShip player;

    private double angle = 0;
    private long duration;
    private boolean isConsumed = false;

    public PowerUp(Vector2D position, BufferedImage texture, BufferedImage orb, PowerUpTypes type, PlayerShip player) {
        super(position, texture);
        this.type = type;
        this.orb = orb;
        this.player = player;
    }
    
    public PowerUpTypes getType(){
        return type;
    }

    private void createAction() {
        switch (type) {
            case LIFE -> {
                player.addLives();
            }
            case SHIELD -> {
                player.setShield(true);
            }
            case SCORE_X2 -> {
                //p.setDoubleScore();
            }
            case FASTER_FIRE -> {
                player.setFastFire(true);
            }
            case SCORE_STACK -> {
                //sm.addScore(1000);
            }
            case DOUBLE_GUN -> {
                player.setDoubleGun(true);
            }

        };
    }

    private void executeAction() {
        //action.doAction();
        createAction();
        isConsumed = true;
        duration = Constants.POWER_UP_DURATION;
    }
    
    public void executeTimer(){
        duration = 0;
    }

    public boolean isExpired() {
        return duration > Constants.POWER_UP_DURATION;
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    @Override
    public void update(float dt) {
        if (!player.isInvulnerable()) {
            // Solo verificamos colisión con el jugador
            double distance = getCenter().subtract(player.getCenter()).getMagnitude();

            if (distance < width / 2 + player.width / 2) {
                executeAction();
            }
        }
        angle += 0.1;
        duration += dt;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g.drawImage(orb, (int) position.getX(), (int) position.getY(), null);

        AffineTransform at = AffineTransform.getTranslateInstance(
                position.getX() + orb.getWidth() / 2 - width / 2,
                position.getY() + orb.getHeight() / 2 - height / 2);

        at.rotate(angle, width / 2, height / 2);

        g2d.drawImage(texture, at, null);
    }

}
