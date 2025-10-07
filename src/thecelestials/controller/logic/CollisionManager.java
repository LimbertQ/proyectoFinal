/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.controller.logic;

import java.util.List;
import thecelestials.model.gameObjects.Meteor;
import thecelestials.model.gameObjects.MovingObject;
import thecelestials.model.gameObjects.Ship;

/**
 *
 * @author pc
 */
public class CollisionManager {
    public CollisionManager(){
        
    }
    public void checkCollisions(List<MovingObject> allObjects){
        for (int i = 0; i < allObjects.size() - 1; i++) {
            MovingObject a = allObjects.get(i);
            if (!a.isInvulnerable()) {
                for (int j = i + 1; j < allObjects.size(); j++) {
                    MovingObject b = allObjects.get(j);
                    if (!b.isInvulnerable()) {
                        double distance = a.getCenter().subtract(b.getCenter()).getMagnitude();
                        if (distance < (a.getWidth() / 2.0 + b.getHeight() / 2.0)) {
                            if(!handleCollision(a, b)){
                                a.destroy(b.getDamage());
                                b.destroy(a.getDamage());
                            }
                        }
                    }
                }
            }
        }
    }
    
    public boolean handleCollision(MovingObject a, MovingObject b){
        return a instanceof Meteor && b instanceof Meteor || a instanceof Ship p1 && b instanceof Ship p2 && p1.getTeam() == p2.getTeam();
    }
}
