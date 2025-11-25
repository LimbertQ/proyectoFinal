/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import thecelestials.controller.Keyboard;
import thecelestials.model.data.ShipStats;
import thecelestials.model.managers.GameObjectCreator;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;
import thecelestials.view.ui.animations.Animation;

/**
 *
 * @author pc
 */
public class PlayerShip extends Ship {

    private Vector2D heading, acceleration;
    private boolean visible = true;

    private int lives;
    private final int copyHealt;
    private final double x, y;
    private long spawnTime, flickerTime = 0;
    private final Animation shield;
    private boolean shieldOn, doubleGunOn;
    private long fireRate = 0;
    private long fireRateConstants = 0;
    public PlayerShip(Vector2D position, Vector2D velocity, ShipStats shipStats, double maxVel, GameObjectCreator creator, Animation shield, int life) {
        super(position, shipStats, velocity, maxVel, creator, Constants.FIRERATE);
        x = position.getX();
        y = position.getY();
        heading = new Vector2D(0, 1);
        acceleration = new Vector2D();
        lives = life;
        copyHealt = shipStats.getHealth();
        shieldOn = false;
        doubleGunOn = false;
        this.shield = shield;
        fireRateConstants = Constants.FIRERATE;
    }

    public int getLives() {
        return lives;
    }

    public void addLives() {
        lives++;
    }

    public void setShield(boolean flag) {
        shieldOn = flag;
    }

    public boolean isShieldOn() {
        return shieldOn;
    }

    public void setFastFire(boolean flag) {
        if (flag) {
            fireRateConstants /= 2;
        } else {
            fireRateConstants = Constants.FIRERATE;
        }
    }

    public void setDoubleGun(boolean flag) {
        doubleGunOn = flag;
    }

    private void handleInput() {
        if (Keyboard.RIGHT) {
            angle += Constants.DELTAANGLE;
        }
        if (Keyboard.LEFT) {
            angle -= Constants.DELTAANGLE;
        }

        if (Keyboard.UP) {
            acceleration = heading.scale(Constants.ACC);
            accelerating = true;
        } else {
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
    }

    public boolean isDestroy() {
        return isInvulnerable && spawnTime == 0;
    }

    private void updateSpawningState(float dt) {
        if (isInvulnerable) {
            if (spawnTime == 0) {
                resetValues();
            }
            spawnTime += dt;
            flickerTime += dt;
            if (flickerTime > Constants.FLICKER_TIME) {
                visible = !visible;
                flickerTime = 0;
            }
            if (spawnTime > Constants.SPAWNING_TIME) {
                isInvulnerable = false;
                visible = true;
            }
        }
    }

    @Override
    public void destroy(int da) {

        healt -= da;
        if (healt < 1) {
            lives--;
            //System.out.println("vidas"+lives);
            isInvulnerable = true;
            spawnTime = 0;
            if (lives < 1) {
                super.destroy(da);
            }
        }
        updateBarWidth();
    }

    public void resetValues() {
        angle = 0;
        velocity = new Vector2D();
        healt = copyHealt;
        position = new Vector2D(x, y);
        switchLocked(false);
    }

    private void shootFire(Vector2D center, float dt) {
        if (fireRate > fireRateConstants) {
            fireRate = 0;
            if (doubleGunOn) {
                
                Vector2D left = new Vector2D(-heading.getY(), heading.getX());
                Vector2D leftWingPosition = center.copy().add(left.scale(-width * 0.35));
                shooti(leftWingPosition, heading);

                Vector2D right = new Vector2D(-heading.getY(), heading.getX());
                Vector2D rightWingPosition = center.copy().add(right.scale(width * 0.35));
                shooti(rightWingPosition, heading);
            } else {
                shooti(center, heading);
            }
        }
    }

    @Override
    public void update(float dt) {

        updateSpawningState(dt);
        if (isMovementLocked()) {
            accelerating = false;
            return;
        }
        updateValuesShip(dt);
        fireRate += dt;
        handleInput();
        if (shieldOn) {
            shield.update(dt);
        }
        if (Keyboard.SHOOT) {
            Vector2D center = getCenter();

            shootFire(center, dt);
        }
        if (Keyboard.SPECIAL) {
            Vector2D muzzle = getCenter().add(heading.scale(width));
            specialTechnique(muzzle, heading, dt);
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
        if (visible == false) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;

        drawSpeed(g2d);
        drawRectangle(g2d);
        if (shieldOn) {
            BufferedImage frame = shield.getCurrentFrame();
            AffineTransform atShield = AffineTransform.getTranslateInstance(
                    position.getX() - frame.getWidth() / 2 + width / 2,
                    position.getY() - frame.getHeight() / 2 + height / 2);
            atShield.rotate(angle, frame.getWidth() / 2.0, frame.getHeight() / 2.0);
            g2d.drawImage(frame, atShield, null);
        }
        AffineTransform at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        at.rotate(angle, width / 2.0, height / 2.0);
        g2d.drawImage(texture, at, null);
    }
}
