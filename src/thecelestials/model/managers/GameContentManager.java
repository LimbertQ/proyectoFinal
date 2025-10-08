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
import thecelestials.model.gameObjects.GravitationalField;
import thecelestials.model.gameObjects.Laser;
import thecelestials.model.gameObjects.Meteor;
import thecelestials.model.gameObjects.MeteorSize;
import thecelestials.model.gameObjects.MovingObject;
import thecelestials.model.gameObjects.NPCShip;
import thecelestials.model.gameObjects.PlayerShip;
import thecelestials.model.gameObjects.Pulsar;
import thecelestials.model.gameObjects.Ship;
import thecelestials.model.gameObjects.Vortex;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;
import thecelestials.view.ui.managers.GameEffectManager;
import thecelestials.view.ui.managers.GameMessageManager;

/**
 *
 * @author pc
 */
public class GameContentManager implements GameObjectCreator, TargetProvider {

    private final List<MovingObject> movingObjects = new ArrayList<>();
    private final List<MovingObject> listToAdd = new ArrayList<>();
    private final List<Ship> enemys = new ArrayList<>();
    private final List<Ship> allies = new ArrayList<>();
    private final PlayerShip player;
    private final List<GravitationalField> gravitationalsFields = new ArrayList<>();
    //private final Vortex vortex;
    //private final Pulsar pulsar;
    private final HUDManager gameHudManager;
    private final GameEventManager gameEventManager;
    private final GameSoundManager gameSoundManager;
    private final GameMessageManager gameMessageManager;
    private final CollisionManager gameCollisionManager;
    private final GameEffectManager gameEffectManager;
    private final Random random = new Random();
    private final Map<String, BufferedImage> images;

    public GameContentManager() {
        player = new PlayerShip(new Vector2D(1366 / 2 - Assets.player.getWidth(), 768 / 2), new Vector2D(), Assets.shipsAvaible.getLast(), Constants.PLAYER_MAX_VEL, this, Assets.effect);
        GravitationalField vortex = new Vortex(new Vector2D(100, 100), Assets.vortex, new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2));
        GravitationalField pulsar = new Pulsar(new Vector2D(500, 0), Assets.pulsar, new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2));
        gravitationalsFields.add(vortex);
        gravitationalsFields.add(pulsar);

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

        gameEventManager.addGameNotificationListener(gameSoundManager);
        images = Assets.images;
    }

    private void startWave() {

        for (int i = 0; i < 1; i++) {
            double x, y;
            if (random.nextBoolean()) { // Decidimos si empieza en X o Y
                x = random.nextDouble() * Constants.WIDTH;
                y = 0;
            } else {
                x = 0;
                y = random.nextDouble() * Constants.HEIGHT;
            }

            BufferedImage texture = Assets.stars.get("big" + 2);
            createGameObject(new Meteor(new Vector2D(x, y), texture, new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2), Constants.METEOR_INIT_VEL * random.nextDouble() + 1, MeteorSize.BIG, this));
        }
    }

    private void spawnShip(int limit, int x, int y, int team) {
        for (int i = 0; i < limit; i++) {
            Ship ship = new NPCShip(new Vector2D(random.nextInt(Constants.WIDTH - 100 + 1), y), Assets.shipsAvaible.get(random.nextInt(Assets.shipsAvaible.size()-1)), new Vector2D(), Constants.UFO_MAX_VEL, this, images.get("effect"), team, this);
            createGameObject(ship);
        }
    }

    private void spawnObjects() {
        if(!hasActiveMeteors()){
            startWave();
        }
        
        if (allies.isEmpty()) {
            spawnShip(1, 0, Constants.HEIGHT - 100, 1);
        }

        if (enemys.isEmpty()) {
            spawnShip(2, 0, 100, 0);
        }
    }

    private boolean hasActiveMeteors() {
        return movingObjects.stream().anyMatch(m -> m instanceof Meteor);
    }

    public void update(float dt) {

        gameEffectManager.update(dt);

        for (MovingObject mo : movingObjects) {
            mo.update(dt);
        }
        //System.out.println(movingObjects.size());

        gameCollisionManager.checkCollisions(movingObjects);

        for (GravitationalField gf : gravitationalsFields) {
            gf.update(dt, movingObjects);
        }
        gameMessageManager.update(dt);
        spawnObjects();

        if (!listToAdd.isEmpty()) {
            movingObjects.addAll(listToAdd);
        }

        removeDeadObjects();
    }

    private void removeDeadObjects() {
        List<MovingObject> objectsToNotify = new ArrayList<>();
        // Primero, identifica cuáles van a ser eliminados
        for (MovingObject obj : movingObjects) {
            if (obj.isDead()) {
                objectsToNotify.add(obj);
            }
        }
        if (player.isDestroy() && !player.isDead()) {
            //player.resetValues();
            //System.out.println("morir");
            objectsToNotify.add(player);
        }
        for (MovingObject obj : objectsToNotify) {
            gameEventManager.notifyGameObjectDestroyed(obj);
        }

        // Tercero, elimina los objetos muertos de cada lista
        movingObjects.removeIf(MovingObject::isDead);
        allies.removeIf(Ship::isDead);
        enemys.removeIf(Ship::isDead);
        listToAdd.clear();
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (GravitationalField gf : gravitationalsFields) {
            gf.draw(g);
        }
        for (MovingObject mo : movingObjects) {
            mo.draw(g);
        }
        gameEffectManager.draw(g);
        gameHudManager.draw(g);
        gameMessageManager.draw(g2d);
    }

    @Override
    public void createGameObject(MovingObject obj) {
        if (obj instanceof Laser) {
            gameEventManager.notifyGameEvent("laser");
        } else if (obj instanceof Ship ship) {
            if (ship.getTeam() == 1) {
                allies.add(ship);
            } else {
                enemys.add(ship);
            }
        }
        listToAdd.add(obj);
    }

    @Override
    public void createFragmentedMeteors(Meteor meteor) {
        MeteorSize nextSize = meteor.getSize().getNextSize();
        for (int i = 0; i < 2; i++) {
            int nro = random.nextInt(2) + 1;
            listToAdd.add(new Meteor(meteor.getPosition(), Assets.stars.get(nextSize.getSize() + nro), new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2), Constants.METEOR_INIT_VEL * Math.random() + 1, meteor.getSize().getNextSize(), this));
        }
    }

    @Override
    public Ship getTarget(int team) {

        Ship ship = null;
        if (team == 1 && !enemys.isEmpty()) {
            ship = enemys.get(random.nextInt(enemys.size()));
        } else if (team == 0) {

            int value = random.nextInt(allies.size() + 1);
            if (value < allies.size()) {
                ship = allies.get(value);
            } else {
                if (!player.isInvulnerable()) {
                    ship = player;
                }
            }

        }
        return ship;
    }

    @Override
    public void cloneShip(Vector2D position, int team) {
        Ship ship = new NPCShip(position, Assets.shipsAvaible.get(random.nextInt(Assets.shipsAvaible.size()-1)), new Vector2D(), Constants.UFO_MAX_VEL, this, images.get("effect"), team, this);
        createGameObject(ship);
    }
}
