/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import thecelestials.model.managers.GameObjectCreator;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public class Meteor extends MovingObject {

    private final MeteorSize size;
    private final GameObjectCreator creator;
    public Meteor(Vector2D position, BufferedImage texture, Vector2D velocity, double maxVel, MeteorSize size, GameObjectCreator creator) {
        super(position, texture, velocity, maxVel);
        this.velocity = velocity.scale(maxVel);
        this.size = size;
        this.healt = size.getHealt();
        this.damage = size.getDamage();
        this.creator = creator;
    }

    @Override
    public void update(float dt) {
        // Limitar la velocidad si excede la máxima
        if (velocity.getMagnitudeSq() >= maxVel * maxVel) {
            velocity = velocity.add(velocity.normalize().scale(-0.01f)); // ligera desaceleración
        }

        velocity = velocity.limit(Constants.METEOR_MAX_VEL);
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
        
        angle += Constants.DELTAANGLE / 2;
    }
    
    @Override
    public void destroy(int d){
        super.destroy(d);
        if(isDead() && size.getNextSize() != null){
            creator.createFragmentedMeteors(this);
        }
    }
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        at.rotate(angle, width / 2.0, height / 2.0);
        g2d.drawImage(texture, at, null);
    }

    public MeteorSize getSize() {
        return size;
    }
}