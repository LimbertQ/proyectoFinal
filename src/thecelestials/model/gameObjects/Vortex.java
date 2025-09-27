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
public class Vortex extends GravitationalField{

    public Vortex(Vector2D position, BufferedImage texture, Vector2D velocity) {
        super(position, texture, velocity);
        this.rotate = 0.01;
    }

    @Override
    public void update(float dt, List<MovingObject> objects) {
        boolean flag = false;
        for (int i = 0; i < objects.size(); i++) {
            MovingObject o = objects.get(i);
            if(!o.isDead()){
                Vector2D oCenter = o.getCenter();
                double dist = oCenter.subtract(center).getMagnitude();

                if (dist < radio) {
                    if(dist < nucleo){
                        destroyObject(o);
                    }else{
                        if(o instanceof Laser)
                            destroyObject(o);
                        else
                            applyGravitationalPull(o, oCenter, dist, dt);
                    }
                    flag = true;
                }
            }
        }
        angle -= rotate;
        if(!flag){
            update(dt);
        }
    }
    
    private void applyGravitationalPull(MovingObject o, Vector2D oCenter, double distance, float dt) {
        
        final double attraction = 0.02;
        final double angularSpeed = -0.01;

        double angleActual = Math.atan2(oCenter.getY() - center.getY(), oCenter.getX() - center.getX());
        double newAngle = angleActual + angularSpeed * dt;

        double newDist = Math.max(0, distance - attraction * dt);
        double newX = center.getX() + newDist * Math.cos(newAngle);
        double newY = center.getY() + newDist * Math.sin(newAngle);

        //ESTABLECE UNA BANDERA BOOLEAN PARA BLOQUEAR MOVIMIENTOS DEL OBJETO Y DARLE SU PROPIA POSICION
        //EN ESPIRA
        o.applyExternalControl(newX - o.width / 2.0, newY - o.height / 2.0);
        //o.getPosition().setX(newX - o.width / 2.0);
        //o.getPosition().setY(newY - o.height / 2.0);
    }
}
