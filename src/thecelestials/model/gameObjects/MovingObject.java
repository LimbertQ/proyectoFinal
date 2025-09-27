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
    protected int healt;
    protected int damage;
    private boolean isDead = false;
    protected boolean isInvulnerable = false;
    private boolean isMovementLocked = false;
    public MovingObject(Vector2D position, BufferedImage texture, Vector2D velocity, double maxVel) {
        super(position, texture);
        this.velocity = velocity;
        this.angle = 0;
        this.maxVel = maxVel;
    }    
    
    public boolean isDead(){
        return isDead;
    }
    
    public boolean isInvulnerable(){
        return isInvulnerable;
    }
    
    public void destroy(int d){
        healt-= d;
        if(healt < 1){
            isInvulnerable = true;
            isDead = true;
        }
    }
    
    public int getDamage(){
        return damage;
    }
    
    public int getHealt(){
        return healt;
    }
    
    protected boolean isMovementLocked(){
        return isMovementLocked;
    }
    
    public void applyExternalControl(double newX, double newY){
        isMovementLocked = true;
        position.setX(newX);
        position.setY(newY);
    }
}
