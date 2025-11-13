/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.gameObjects;

import thecelestials.model.data.ShipStats;
import thecelestials.model.managers.GameObjectCreator;
import thecelestials.model.managers.TargetProvider;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public class ReinforcementShip extends NPCShip{
    
    public ReinforcementShip(Vector2D position, ShipStats shipStats, Vector2D velocity, double maxVel, GameObjectCreator creator, TargetProvider provider) {
        super(position, shipStats, velocity, maxVel, creator, provider);
        accelerating = true;
    }
    
    @Override
    public void update(float dt){
        patrol(getCenter(), dt);
    }
}
