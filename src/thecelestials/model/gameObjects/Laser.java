/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import thecelestials.model.data.EntityStats;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public class Laser extends MovingObject {

    public Laser(Vector2D position, EntityStats entityStats, Vector2D velocity, double maxVel, double angle) {
        super(position, entityStats, velocity, maxVel);
        this.angle = angle;
        this.velocity = velocity.scale(maxVel);
    }

    @Override
    public void update(float dt) {
        position = position.add(velocity);

        if (position.getX() > Constants.WIDTH || position.getY() > Constants.HEIGHT
                || position.getX() < 0 || position.getY() < 0) {
            destroy(5);
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        AffineTransform at = AffineTransform.getTranslateInstance(position.getX() - width / 2, position.getY());

        at.rotate(angle, width / 2, 0);

        g2d.drawImage(texture, at, null);
    }
    
    @Override
    public Vector2D getCenter(){
        return new Vector2D(position.getX() + width/2, position.getY() + width/2);
    }
}
