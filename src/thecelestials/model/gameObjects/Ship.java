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
    //private final BufferedImage effect;
    private long special = 2990;
    protected final EntityStats bullet;
    private final ShipStats shipStats;
    protected boolean accelerating = false;
    protected long fireRate = 0;
    private final int team;

    public Ship(Vector2D position, ShipStats shipStats, Vector2D velocity, double maxVel, GameObjectCreator creator, BufferedImage effect, int team) {
        super(position, shipStats, velocity, maxVel);
        //this.effect = effect;
        this.bullet = shipStats.getEntityStats();
        this.creator = creator;
        this.team = team;
        this.shipStats = shipStats;
    }

    //LASER FUERTE
    //CLONAR
    //CLONAR VARIOS
    protected void specialTechnique(Vector2D position, Vector2D direction) {
        special += 500;
        if (special > Constants.UFO_CLONE_RATE) {
            special = 0;
            if (shipStats.getShipClass().equals("caza")) {
                shootLaserBig(position, direction);
                
            } else if (shipStats.getShipClass().equals("ufo")) {
                creator.cloneShip(position, team);
            } else {
                creator.cloneShip(position, team);
                creator.cloneShip(position, team);
                creator.cloneShip(position, team);
            }
        }
    }

    private void shootLaserBig(Vector2D position, Vector2D direction) {
        Laser laser = new Laser(
                position,
                Assets.powerBullet,
                direction,
                Constants.LASER_VEL,
                angle);
        creator.createGameObject(laser);
    }

    protected void shooti(Vector2D position, Vector2D direction) {
        if(special == 0){
            return;
        }
        Laser laser = new Laser(
                getCenter().add(direction.scale(width)),
                bullet,
                direction,
                Constants.LASER_VEL,
                angle);
        creator.createGameObject(laser);
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
