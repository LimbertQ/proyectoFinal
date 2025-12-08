/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.managers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import thecelestials.controller.logic.CollisionManager;
import thecelestials.model.data.AssetDefinition;
import thecelestials.model.data.Assets;
import thecelestials.model.data.MissionStats;
import thecelestials.model.data.ShipStats;
import thecelestials.model.gameObjects.GravitationalField;
import thecelestials.model.gameObjects.Laser;
import thecelestials.model.gameObjects.Meteor;
import thecelestials.model.gameObjects.MeteorSize;
import thecelestials.model.gameObjects.MovingObject;
import thecelestials.model.gameObjects.NPCShip;
import thecelestials.model.gameObjects.PlayerShip;
import thecelestials.model.gameObjects.Pulsar;
import thecelestials.model.gameObjects.ReinforcementShip;
import thecelestials.model.gameObjects.Ship;
import thecelestials.model.gameObjects.Vortex;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;
import thecelestials.view.ui.animations.Animation;
import thecelestials.view.ui.managers.GameEffectManager;
import thecelestials.view.ui.managers.GameMessageManager;
import thecelestials.view.ui.managers.GameSoundManager;
import thecelestials.view.ui.managers.HUDManager;

/**
 *
 * @author pc
 */
public class GameContentManager extends GameManager implements IGameControl, GameObjectCreator, TargetProvider {

    private final List<MovingObject> movingObjects = new ArrayList<>();
    private final List<MovingObject> listToAdd = new ArrayList<>();
    private final Map<Integer, List<Ship>> allShips = new HashMap<>();
    private Ship cruiser;
    private PlayerShip player = null;
    private final List<GravitationalField> gravitationalsFields = new ArrayList<>();
    private final GameEventManager gameEventManager;
    private final GameSoundManager gameSoundManager;
    private final CollisionManager gameCollisionManager;
    private final List<GameManager> allManagers = new ArrayList<>();
    private final Random random = new Random();
    private final Map<String, BufferedImage> images;

    private long assault = 0;
    private byte type;
    private byte waves = 0;
    private boolean meteor = false;
    private BufferedImage missionMap;

    public GameContentManager() {
        images = Assets.images;
        
        allShips.put(1, new ArrayList<>());
        allShips.put(0, new ArrayList<>());
        
        gameEventManager = new GameEventManager();

        HUDManager gameHudManager = new HUDManager();
        allManagers.add(gameHudManager);

        GameMessageManager gameMessageManager = new GameMessageManager(new Vector2D(50, Constants.HEIGHT / 4), new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2));
        allManagers.add(gameMessageManager);

        gameSoundManager = new GameSoundManager();
        allManagers.add(gameSoundManager);

        GamePowerUpManager gamePowerUpManager = new GamePowerUpManager(images, gameEventManager);
        allManagers.add(gamePowerUpManager);

        gameCollisionManager = new CollisionManager();

        GameEffectManager gameEffectManager = new GameEffectManager();
        allManagers.add(gameEffectManager);
        for (GameManager manager : allManagers) {
            if (manager instanceof GameObjectDestroyedListener managerEntity) {
                gameEventManager.addGameObjectDestroyedListener(managerEntity);
            }
            if (manager instanceof GameNotificationListener managerEntity) {
                gameEventManager.addGameNotificationListener(managerEntity);
            }
            if (manager instanceof ScoreChangeListener managerEntity) {
                gameEventManager.addGameScoreListener(managerEntity);
            }
        }
    }

    @Override
    public void clear() {
        missionMap = null;

        cruiser = null;

        for (GameManager manager : allManagers) {
            manager.clear();
        }

        //---------
        gravitationalsFields.clear();
        movingObjects.clear();
        listToAdd.clear();
        allShips.get(1).clear();
        allShips.get(0).clear();
        player = null;
        meteor = false;
        assault = 0;
        waves = 0;
        type = -1;
    }

    public void playGame() {
        missionMap = Assets.missionMaps.get(MissionStats.missionName);
        this.player = new PlayerShip(new Vector2D(1366 / 2 - Assets.player.getWidth(), 768 / 2), new Vector2D(), Assets.getCurrentShip(), Constants.PLAYER_MAX_VEL, this, new Animation(Assets.shieldEffects, 80, null), Assets.lives);
        allShips.get(1).add(player);
        movingObjects.add(player);
        //-----------
        for (GameManager manager : allManagers) {
            if (manager instanceof IStartableWithPlayer startableWithPlayer) {
                startableWithPlayer.playGame(player);
            }
        }
        gameSoundManager.playGame();
        //-----------
        gameEventManager.notifyGameEvent("DESCRIPTION");
        if (Assets.missionMaps.containsKey("big1")) {
            meteor = true;
        }
        for (AssetDefinition entry : MissionStats.starsAssets) {
            if (entry.getName().equals("pulsar")) {
                GravitationalField pulsar = new Pulsar(new Vector2D(500, 0), entry.getProfile(), new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2));
                gravitationalsFields.add(pulsar);
            } else if (entry.getName().equals("vortice")) {
                GravitationalField vortex = new Vortex(new Vector2D(100, 100), entry.getProfile(), new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2));
                gravitationalsFields.add(vortex);
            }
        }

        if (!MissionStats.allShips[2].isEmpty()) {
            Ship cruisero = new NPCShip(new Vector2D(1366 / 2, 768 / 2), MissionStats.allShips[2].getFirst(), new Vector2D(), Constants.UFO_MAX_VEL, this, this);
            movingObjects.add(cruisero);
            allShips.get(cruisero.getTeam()).add(cruisero);
            if (cruisero.getTeam() == 1) {
                cruiser = cruisero;
            }
            
        }
        assault = 0;
        type = -1;
        MissionStats.starsAssets.clear();
    }

    @Override
    public void resume() {
        for (GameManager manager : allManagers) {
            if (manager instanceof IGameControl gameControl) {
                gameControl.resume();
            }
        }
    }

    @Override
    public void pause() {
        for (GameManager manager : allManagers) {
            if (manager instanceof IGameControl gameControl) {
                gameControl.pause();
            }
        }
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

            BufferedImage texture = Assets.missionMaps.get("big" + 2);
            createGameObject(new Meteor(new Vector2D(x, y), texture, new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2), Constants.METEOR_INIT_VEL * random.nextDouble() + 1, MeteorSize.BIG, this, player));
        }
    }

    private void spawnShip(int limit, int x, int y, int team, List<ShipStats> shipsList) {
        for (int i = 0; i < limit; i++) {
            Ship ship = new NPCShip(new Vector2D(random.nextInt(Constants.WIDTH - 100 + 1), y), shipsList.get(random.nextInt(shipsList.size())), new Vector2D(), Constants.UFO_MAX_VEL, this, this);
            createGameObject(ship);
        }
    }

    private void spawnReinforcement(int limit, List<ShipStats> shipsList) {
        for (int i = 0; i < limit; i++) {
            Ship ship = new ReinforcementShip(new Vector2D(100, (i + 1) * 20), shipsList.get(random.nextInt(shipsList.size())), new Vector2D(), Constants.UFO_MAX_VEL, this, this);
            createGameObject(ship);
        }
    }

    private void spawnObjects(float dt) {
        if (waves <= MissionStats.assaults) {

            if (meteor && !hasActiveMeteors()) {
                startWave();
            }

            if (MissionStats.alliesExist && allShips.get(1).size() < 2) {
                spawnShip(1, 0, Constants.HEIGHT - 100, 1, MissionStats.allShips[1]);
            }
            assault += dt;
            if (assault > 20000) {
                assault = 0;
                waves++;
                int nroRandom;
                if (MissionStats.challenge == 1 && waves % 3 == 0) {
                    nroRandom = random.nextInt(4) + 5;
                    gameEventManager.notifyGameEvent("WAVE");
                } else {
                    nroRandom = random.nextInt(3) + 2;
                    gameEventManager.notifyGameEvent("ASSAULT");
                }
                spawnShip(nroRandom, 0, 100, 0, MissionStats.allShips[0]);
            }
        } else if (type < 0) {
            type = 0;
        }
    }

    public byte gameOver() {
        return type;
    }

    private void blockShips() {
        for (Ship ship : allShips.get(0)) {
            ship.switchLocked(true);
        }
        for (Ship ship : allShips.get(1)) {
            if(ship instanceof NPCShip)
                ship.switchLocked(true);
        }
    }

    private void cinematic(float dt) {
        //type = 3 muere, type = 4 gana --cinematic
        if (type == 0) {
            if (MissionStats.reinforcement == 1) {
                //reforces
                if (assault == 0) {
                    //axis and reforces
                    blockShips();
                    spawnReinforcement(8, MissionStats.allShips[1]);
                } else if (assault > 3000) {
                    //destroyAxis
                    for (Ship ship : allShips.get(0)) {
                        ship.destroy(10000);
                    }
                    type = 4;
                    assault = -1000;
                }
            } else if (allShips.get(0).isEmpty()) {
                //mensaje victoria
                type = 4;
                assault = -1000;
            }
        } else if (assault < 0) {
            assault = 0;
            if (type == 3) {
                //mensaje gameOver ------ assault = 0
                gameEventManager.notifyGameEvent("GAME OVER");
            } else if (type == 4) {
                //mensaje victoria
                gameEventManager.notifyGameEvent("VICTORY");
            }
        }

        if (assault > 15000) {
            //termina el juego
            type -= 2;
        }
        assault += dt;
    }

    private boolean hasActiveMeteors() {
        return movingObjects.stream().anyMatch(m -> m instanceof Meteor);
    }

    public void update(float dt) {
        for (GameManager manager : allManagers) {
            if (manager instanceof IGameLoopEntity gameLoopEntity) {
                gameLoopEntity.update(dt);
            }
        }

        for (MovingObject mo : movingObjects) {
            mo.update(dt);
        }
        //System.out.println(movingObjects.size());

        gameCollisionManager.checkCollisions(movingObjects);

        for (GravitationalField gf : gravitationalsFields) {
            gf.update(dt, movingObjects);
        }

        if (type < 0) {
            spawnObjects(dt);
        }
        if (type > -1) {
            cinematic(dt);
        }

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
            objectsToNotify.add(player);
        } else if (player.isDead() || (cruiser != null && cruiser.isDead())) {
            //mensaje -> GAME OVER
            if (type > 3 || type < 1) {
                type = 3;
                assault = -10;
            }
        }
        for (MovingObject obj : objectsToNotify) {
            gameEventManager.notifyGameObjectDestroyed(obj);
        }

        // Tercero, elimina los objetos muertos de cada lista
        movingObjects.removeIf(MovingObject::isDead);
        allShips.get(0).removeIf(Ship::isDead);
        allShips.get(1).removeIf(Ship::isDead);
        listToAdd.clear();
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(missionMap, 0, 0, 1366, 768, null);

        //Graphics2D g2d = (Graphics2D) g;
        for (GravitationalField gf : gravitationalsFields) {
            gf.draw(g2d);
        }
        for (MovingObject mo : movingObjects) {
            mo.draw(g2d);
        }
        for (GameManager manager : allManagers) {
            if (manager instanceof IGameLoopEntity gameLoopEntity) {
                gameLoopEntity.draw(g2d);
            }
        }
    }

    @Override
    public void createGameObject(MovingObject obj) {
        if (obj instanceof Laser) {
            gameEventManager.notifyGameEvent("laser");
        } else if (obj instanceof Ship ship) {
            allShips.get(ship.getTeam()).add(ship);
        }
        listToAdd.add(obj);
    }

    @Override
    public void createFragmentedMeteors(Meteor meteor) {
        MeteorSize nextSize = meteor.getSize().getNextSize();
        for (int i = 0; i < 2; i++) {
            int nro = random.nextInt(2) + 1;
            listToAdd.add(new Meteor(meteor.getPosition(), Assets.missionMaps.get(nextSize.getSize() + nro), new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2), Constants.METEOR_INIT_VEL * Math.random() + 1, meteor.getSize().getNextSize(), this, player));
        }
    }

    @Override
    public Ship getTarget(int team) {
        if(team > 0){
            team = 0;
        }else{
            team = 1;
        }
        int length = allShips.get(team).size();
        Ship ship = null;
        if(length > 0){
            ship = allShips.get(team).get(random.nextInt(length));
        }
                
        return ship;
    }

    @Override
    public void cloneShip(Vector2D position, int team) {
        Ship ship = new NPCShip(position, MissionStats.allShips[team].get(random.nextInt(MissionStats.allShips[team].size())), new Vector2D(), Constants.UFO_MAX_VEL, this, this);
        createGameObject(ship);
    }
}
