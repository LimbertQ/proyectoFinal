/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.gameObjects;

import java.awt.image.BufferedImage;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public abstract class MovingObject extends GameObject{
    protected Vector2D velocity;
    protected double angle;
    protected double maxVel;
    private boolean isDead = false;
    public MovingObject(Vector2D position, BufferedImage texture, Vector2D velocity) {
        super(position, texture);
        this.velocity = velocity;
        angle = 0;
    }    
    
    public boolean isDead(){
        return isDead;
    }
    
    public void destroy(){
        isDead = true;
    }
}
