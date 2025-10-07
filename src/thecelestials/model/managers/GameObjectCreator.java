/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package thecelestials.model.managers;

import thecelestials.model.gameObjects.Meteor;
import thecelestials.model.gameObjects.MovingObject;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public interface GameObjectCreator {
    void createGameObject(MovingObject obj);
    void createFragmentedMeteors(Meteor meteor);
    void cloneShip(Vector2D position, int team);
}
