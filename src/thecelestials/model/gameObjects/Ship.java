/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.gameObjects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import thecelestials.model.data.Assets;
import thecelestials.model.data.EntityStats;
import thecelestials.model.data.ShipStats;
import thecelestials.model.managers.GameObjectCreator;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public abstract class Ship extends MovingObject {

    private final GameObjectCreator creator;
    protected final EntityStats bullet;
    private final ShipStats shipStats;
    protected boolean accelerating = false;
    private long special = 0;
    private long fireRate = 0;
    private final long fireRateConstants;
    private final int team;

    public Ship(Vector2D position, ShipStats shipStats, Vector2D velocity, double maxVel, GameObjectCreator creator, BufferedImage effect, int team, long shipFireRate) {
        super(position, shipStats, velocity, maxVel);
        this.bullet = shipStats.getEntityStats();
        this.creator = creator;
        this.team = team;
        this.shipStats = shipStats;
        this.fireRateConstants = shipFireRate;
    }

    protected void updateValuesShip(float dt) {
        fireRate += dt;
        special += dt;
    }

    //LASER FUERTE
    //CLONAR
    //CLONAR VARIOS
    protected void specialTechnique(Vector2D position, Vector2D direction, float dt) {
        if (special > Constants.UFO_CLONE_RATE) {
            special = 0;
            switch (shipStats.getShipClass()) {
                case "caza" ->
                    shootLaserBig(position, direction);
                case "ufo" ->
                    creator.cloneShip(position, team);
                default -> {
                    healt += 50;
                    shootLaserBig(position, direction);
                    creator.cloneShip(getPosition(), team);
                    creator.cloneShip(getPosition(), team);
                }
            }
        }
    }

    private void shootLaserBig(Vector2D position, Vector2D direction) {
        Laser laser = new Laser(
                position.add(direction.scale(width)),
                Assets.powerBullet,
                direction,
                Constants.LASER_VEL,
                angle);
        creator.createGameObject(laser);
    }

    protected void shooti(Vector2D positione, Vector2D direction) {
        if (special == 0) {
            return;
        }
        if (fireRate > fireRateConstants) {
            fireRate = 0;
            Laser laser = new Laser(
                    positione.add(direction.scale(width)),
                    bullet,
                    direction,
                    Constants.LASER_VEL,
                    angle);
            creator.createGameObject(laser);
        }
    }

    public int getTeam() {
        return team;
    }

    protected void drawSpeed(Graphics2D g2d) {
        if (accelerating) {
            AffineTransform at1 = AffineTransform.getTranslateInstance(position.getX() + width / 2 + 5,
                    position.getY() + height / 2 + 10);
            
            AffineTransform at2 = AffineTransform.getTranslateInstance(position.getX() + 5,
                    position.getY() + height / 2 + 10);
            at1.rotate(angle, -5, -10);
            at2.rotate(angle, width / 2 - 5, -10);
            g2d.drawImage(Assets.effect, at1, null);
            g2d.drawImage(Assets.effect, at2, null);
        }
    }
}
