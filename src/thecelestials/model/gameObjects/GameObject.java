/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.gameObjects;

/**
 *
 * @author pc
 */
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import thecelestials.model.math.Vector2D;

public abstract class GameObject {
    protected BufferedImage texture;
    protected Vector2D position;
    
    protected int width;
    protected int height;
    
    public GameObject(Vector2D position, BufferedImage texture)
    {
        this.position = position;
        this.texture = texture;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }

    public abstract void update(float dt);

    public abstract void draw(Graphics2D g2d);

    public Vector2D getPosition() {
        return position;
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }
    
    public Vector2D getCenter() {
        return new Vector2D(position.getX() + width / 2.0, position.getY() + height / 2.0);
    }
}
