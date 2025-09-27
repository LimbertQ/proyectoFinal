/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.gameObjects;

import java.awt.image.BufferedImage;
import java.util.List;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public class Pulsar extends GravitationalField{

    public Pulsar(Vector2D position, BufferedImage texture, Vector2D velocity) {
        super(position, texture, velocity);
    }

    @Override
    public void update(float dt, List<MovingObject> objects) {
        angle += rotate;
        if(Math.abs(angle) > 5){
            rotate *= -1;
            applyOnda(objects);
        }
        update(dt);
    }
    
    private void applyOnda(List<MovingObject> objects){
        for(int i = 0; i < objects.size(); i++){
            MovingObject o = objects.get(i);
            if(!o.isDead()){
                Vector2D oCenter = o.getCenter();
                double dist = oCenter.subtract(center).getMagnitude();

                if (dist < radio) destroyObject(o);
            }
        }
        
        //objects.removeIf(MovingObject::isDead);
    }
}
