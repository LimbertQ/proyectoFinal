/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.managers;

import java.util.ArrayList;
import java.util.List;
import thecelestials.model.gameObjects.MovingObject;

/**
 *
 * @author pc
 */
public class GameEventManager {
    private final List<GameObjectDestroyedListener> destroyedListeners;
    private final List<GameNotificationListener> notifyListener;
    public GameEventManager(){
        destroyedListeners = new ArrayList<>();
        this.notifyListener = new ArrayList<>();
    }
    
    // Método para que otros managers o el GameState se suscriban a eventos de destrucción
    public void addGameObjectDestroyedListener(GameObjectDestroyedListener listener) {
        destroyedListeners.add(listener);
    }
    
    // Método para que otros managers o el GameState se suscriban a eventos de destrucción
    public void addGameNotificationListener(GameNotificationListener listener) {
        notifyListener.add(listener);
    }
    
    // Método para notificar que un GameObject ha sido destruido
    public void notifyGameEvent(String type) {
        for (GameNotificationListener listener : notifyListener) {
            listener.onGameNotify(type);
        }
    }
    
    // Método para notificar que un GameObject ha sido destruido
    public void notifyGameObjectDestroyed(MovingObject destroyedObject) {
        for (GameObjectDestroyedListener listener : destroyedListeners) {
            listener.onGameObjectDestroyed(destroyedObject);
        }
    }
}