/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.managers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import thecelestials.controller.logic.CollisionManager;
import thecelestials.model.data.Assets;
import thecelestials.model.gameObjects.Meteor;
import thecelestials.model.gameObjects.MeteorSize;
import thecelestials.model.gameObjects.MovingObject;
import thecelestials.model.gameObjects.PlayerShip;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;
import thecelestials.view.ui.managers.GameEffectManager;
import thecelestials.view.ui.managers.GameMessageManager;

/**
 *
 * @author pc
 */
public class GameContentManager implements GameObjectCreator {

    private final ArrayList<MovingObject> movingObjects = new ArrayList<>();
    private final ArrayList<MovingObject> listToAdd = new ArrayList<>();
    private final PlayerShip player;
    private final HUDManager gameHudManager;
    private final GameEventManager gameEventManager;
    private final GameSoundManager gameSoundManager;
    private final GameMessageManager gameMessageManager;
    private final CollisionManager gameCollisionManager;
    private final GameEffectManager gameEffectManager;
    private final Random random = new Random();
    private final Map<String, BufferedImage> images;

    public GameContentManager() {
        player = new PlayerShip(new Vector2D(1366 / 2 - Assets.player.getWidth(), 768 / 2), new Vector2D(), Assets.player, Constants.PLAYER_MAX_VEL, this, Assets.effect);
        movingObjects.add(player);
        gameHudManager = new HUDManager(player);
        gameEventManager = new GameEventManager();
        gameEventManager.addGameObjectDestroyedListener(gameHudManager);
        gameSoundManager = new GameSoundManager();
        gameEventManager.addGameObjectDestroyedListener(gameSoundManager);
        gameCollisionManager = new CollisionManager();
        gameMessageManager = new GameMessageManager();
        gameEventManager.addGameObjectDestroyedListener(gameMessageManager);
        gameEffectManager = new GameEffectManager();
        gameEventManager.addGameObjectDestroyedListener(gameEffectManager);
        images = Assets.images;
    }

    private void startWave() {
        
        for (int i = 0; i < 1; i++) {
            double x, y;
            if (random.nextBoolean()) { // Decidimos si empieza en X o Y
                x = 300;
                y = 3000;
            } else {
                x = 300;
                y = 300;
            }
            
            BufferedImage texture = images.get("Bbig" + 2);
            createGameObject(new Meteor(new Vector2D(x, y), texture, new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2), Constants.METEOR_INIT_VEL * random.nextDouble() + 1, MeteorSize.BIG, this));
            
        }
    }

    private boolean hasActiveMeteors() {
        return movingObjects.stream().anyMatch(m -> m instanceof Meteor);
    }

    public void update(float dt) {
        
        gameEffectManager.update(dt);
        if(!hasActiveMeteors()){
            startWave();
        }
        for (MovingObject mo : movingObjects) {
            mo.update(dt);
        }
        
        gameCollisionManager.checkCollisions(movingObjects);
        gameMessageManager.update(dt);

        if (!listToAdd.isEmpty()) {
            movingObjects.addAll(listToAdd);
        }
        
        removeDeadObjects();
    }
    
    private void removeDeadObjects(){
        List<MovingObject> objectsToNotify = new ArrayList<>();
        // Primero, identifica cuáles van a ser eliminados
        for (MovingObject obj : movingObjects) {
            if (obj.isDead()) {
                objectsToNotify.add(obj);
            }
        }
        if(player.isDestroy() && !player.isDead()){
            //player.resetValues();
            objectsToNotify.add(player);
        }
        for (MovingObject obj : objectsToNotify) {
            gameEventManager.notifyGameObjectDestroyed(obj);
        }
        
        // Tercero, elimina los objetos muertos de cada lista
        movingObjects.removeIf(MovingObject::isDead);
        listToAdd.clear();
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (MovingObject mo : movingObjects) {
            mo.draw(g);
        }
        gameEffectManager.draw(g);
        gameHudManager.draw(g);
        gameMessageManager.draw(g2d);
    }

    @Override
    public void createGameObject(MovingObject obj) {
        listToAdd.add(obj);
    }

    @Override
    public void createFragmentedMeteors(Meteor meteor) {
        MeteorSize nextSize = meteor.getSize().getNextSize();
        for (int i = 0; i < 2; i++) {
            int nro = random.nextInt(2) + 1;
            listToAdd.add(new Meteor(meteor.getPosition(), images.get(nextSize.getSize() + nro), new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2), Constants.METEOR_INIT_VEL * Math.random() + 1, meteor.getSize().getNextSize(), this));
        }
    }
}
