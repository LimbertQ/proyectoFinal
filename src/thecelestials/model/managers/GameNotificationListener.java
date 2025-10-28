/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.managers;

import thecelestials.model.gameObjects.PowerUp;

/**
 *
 * @author pc
 */
public interface GameNotificationListener {
    public void onGameNotify(String type);
    public void notifyPowerUp(PowerUp po);
}
