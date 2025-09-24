/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package thecelestials.model.managers;

import thecelestials.model.gameObjects.MovingObject;

/**
 *
 * @author pc
 */
public interface GameObjectDestroyedListener {
    void onGameObjectDestroyed(MovingObject destroyedObject);
}
