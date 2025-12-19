/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui.animations;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import thecelestials.model.gameObjects.GameObject;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public class Animation extends GameObject{

    private final BufferedImage[] frames;
    private final int velocity;
    private int index;
    private boolean running;
    private long time;

    public Animation(BufferedImage[] frames, int velocity, Vector2D position){
        super(position, frames[0]);
        this.frames = frames;
        this.velocity = velocity;
        index = 0;
        running = true;
    }

    @Override
    public void update(float dt){
        time += dt;

        if(time > velocity){
            time  = 0;
            index ++;
            if(index >= frames.length){
                running = false;
                index = 0;
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public BufferedImage getCurrentFrame(){
        return frames[index];
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(getCurrentFrame(), (int)position.getX(), (int)position.getY(), null);
    }
}