/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.gameObjects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public abstract class GravitationalField extends GameObject {

    protected Vector2D velocity;

    protected double angle = 0;
    protected final double radio, nucleo;
    protected double rotate = 0.1;
    protected Vector2D center;

    private double xw = 1, yh = 1;
    public GravitationalField(Vector2D position, BufferedImage texture, Vector2D velocity) {
        super(position, texture);
        this.velocity = velocity;
        radio = width / 2;
        nucleo = width / 5;
        center = getCenter();
    }
    
    public abstract void update(float dt, List<MovingObject> objects);
    
    
    @Override
    public void update(float dt) {
        updatePosition();
        center = getCenter();
    }
    
    private void updatePosition(){
        if(position.getX()+height > Constants.WIDTH || position.getX() < 0){
            xw*=-1;
        }
        if(position.getY()+height > Constants.HEIGHT || position.getY() < 0){
            yh*=-1;
        }
        
        position.setX(position.getX()+xw);
        position.setY(position.getY()+yh);
    }

    protected void destroyObject(MovingObject destroyedObject) {
        destroyedObject.destroy(100000);
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        at.rotate(angle, width / 2.0, height / 2.0);
        g2d.drawImage(texture, at, null);
    }
}
