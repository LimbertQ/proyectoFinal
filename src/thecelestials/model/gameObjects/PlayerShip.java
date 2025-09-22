/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.sound.sampled.Clip;
import thecelestials.controller.Keyboard;
import thecelestials.model.data.Assets;
import thecelestials.model.managers.GameContentManager;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public class PlayerShip extends MovingObject {

    private Vector2D heading, acceleration;
    private final GameContentManager gc;
    private long fireRate;
    private final BufferedImage effect;
    private boolean accelerating = false;

    private final Clip shoot;

    public PlayerShip(Vector2D position, Vector2D velocity, BufferedImage texture, GameContentManager gg, BufferedImage effect) {
        super(position, texture, velocity);
        this.effect = effect;
        heading = new Vector2D(0, 1);
        acceleration = new Vector2D();
        maxVel = 7.0;
        gc = gg;
        fireRate = 0;
        shoot = Assets.fireSound;
    }

    @Override
    public void update(float dt) {
        fireRate += dt;
        if (Keyboard.RIGHT) {
            angle += Math.PI / 20;
        }
        if (Keyboard.LEFT) {
            angle -= Math.PI / 20;
        }

        if (Keyboard.SHOOT && fireRate > Constants.FIRERATE) {
            fireRate = 0;
            Vector2D laser = getCenter().add(heading.scale(width));
            Laser la = new Laser(laser, Assets.laser, heading, angle);
            shoot.stop();
            shoot.setFramePosition(0);
            shoot.start();
            gc.add(la);
        }

        if (Keyboard.UP) {
            acceleration = heading.scale(0.2);
            accelerating = true;
        } else {
            //acceleration = heading.scale(0.2);
            accelerating = false;

            if (velocity.getMagnitudeSq() > 0.01) {
                acceleration = velocity.normalize().scale(-Constants.ACC / 2);
            } else {
                velocity.setX(0);
                velocity.setY(0);

                acceleration.setX(0);
                acceleration.setY(0);
            }
        }

        velocity = velocity.add(acceleration).limit(maxVel);
        heading = heading.setDirection(angle - Math.PI / 2);
        position = position.add(velocity);

        if (position.getX() > Constants.WIDTH) {
            position.setX(0);
        }
        if (position.getY() > Constants.HEIGHT) {
            position.setY(0);
        }
        if (position.getX() < -width) {
            position.setX(Constants.WIDTH);
        }
        if (position.getY() < -height) {
            position.setY(Constants.HEIGHT);
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        //Vector2D center = getCenter();
        if (accelerating) {
            AffineTransform at1 = AffineTransform.getTranslateInstance(position.getX() + width / 2 + 5,
                    position.getY() + height / 2 + 10);

            AffineTransform at2 = AffineTransform.getTranslateInstance(position.getX() + 5,
                    position.getY() + height / 2 + 10);
            at1.rotate(angle, -5, -10);
            at2.rotate(angle, width / 2 - 5, -10);
            g2d.drawImage(effect, at1, null);
            g2d.drawImage(effect, at2, null);
        }

        AffineTransform at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        at.rotate(angle, width / 2.0, height / 2.0);
        g2d.drawImage(texture, at, null);

    }
}
