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
import thecelestials.model.gameObjects.Ship;
import thecelestials.model.gameObjects.Vortex;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;
import thecelestials.view.ui.animations.Animation;
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
    private Ship cruiser;
    private PlayerShip player = null;
    private final List<GravitationalField> gravitationalsFields = new ArrayList<>();
    private final HUDManager gameHudManager;
    private final GameEventManager gameEventManager;
    private final GameSoundManager gameSoundManager;
    private final GameMessageManager gameMessageManager;
    private final CollisionManager gameCollisionManager;
    private final GameEffectManager gameEffectManager;
    private final GamePowerUpManager gamePowerUpManager;
    private final Random random = new Random();
    private final Map<String, BufferedImage> images;

    private long assault = 0;
    private byte type;
    private byte waves = 0;
    private boolean meteor = false;
    private BufferedImage missionMap;

    public GameContentManager() {
        gameHudManager = new HUDManager();
        gameEventManager = new GameEventManager();
        gameEventManager.addGameObjectDestroyedListener(gameHudManager);
        gameSoundManager = new GameSoundManager();
        gameEventManager.addGameObjectDestroyedListener(gameSoundManager);
        gameCollisionManager = new CollisionManager();
        gameMessageManager = new GameMessageManager(new Vector2D(50, Constants.HEIGHT / 4), new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2));
        gameEventManager.addGameObjectDestroyedListener(gameMessageManager);
        gameEffectManager = new GameEffectManager();
        gameEventManager.addGameObjectDestroyedListener(gameEffectManager);

        gameEventManager.addGameNotificationListener(gameSoundManager);
        gameEventManager.addGameNotificationListener(gameMessageManager);
        gameEventManager.addGameNotificationListener(gameHudManager);
        
        gameEventManager.addGameScoreListener(gameHudManager);
        gameEventManager.addGameScoreListener(gameMessageManager);
        images = Assets.images;
        gamePowerUpManager = new GamePowerUpManager(images, gameEventManager);
    }

    public void clear() {
        missionMap = null;
        
        cruiser = null;

        gameHudManager.clear();
        //---------
        gameEffectManager.clear();
        //---------
        gameMessageManager.clear();
        //---------
        gameSoundManager.clear();

        //---------
        gravitationalsFields.clear();
        gamePowerUpManager.clear();
        movingObjects.clear();
        listToAdd.clear();
        enemys.clear();
        allies.clear();
        player = null;
        meteor = false;
        assault = 0;
        waves = 0;
        type = -1;
    }

    public void playGame() {
        missionMap = Assets.missionMaps.get(MissionStats.missionName);
        this.player = new PlayerShip(new Vector2D(1366 / 2 - Assets.player.getWidth(), 768 / 2), new Vector2D(), Assets.getCurrentShip(), Constants.PLAYER_MAX_VEL, this, Assets.effect, new Animation(Assets.shieldEffects, 80, null));
        
        movingObjects.add(player);

        gameHudManager.playGame(player);
        gamePowerUpManager.playGame(player);
        gameSoundManager.playGame();
        gameEventManager.notifyGameEvent("DESCRIPTION");
        if(MissionStats.stars.containsKey("big1")){
            meteor = true;
        }
        for(Map.Entry<String, BufferedImage> entry: MissionStats.stars.entrySet()){
            if(entry.getKey().equals("pulsar")){
                GravitationalField pulsar = new Pulsar(new Vector2D(500, 0), entry.getValue(), new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2));
                gravitationalsFields.add(pulsar);
            }else if(entry.getKey().equals("vortice")){
                GravitationalField vortex = new Vortex(new Vector2D(100, 100), entry.getValue(), new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2));
                gravitationalsFields.add(vortex);
            }
        }
        
        if(MissionStats.cruiser != null){
            Ship cruisero;
            if(MissionStats.cruiser.getTeam() == 1)
                cruisero = new NPCShip(new Vector2D(1366/2 , 768/2), MissionStats.cruiser, new Vector2D(), Constants.UFO_MAX_VEL, this, images.get("effect"), 1, this);
            
            else{
                cruisero = new NPCShip(new Vector2D(1366/2 , 768/2), MissionStats.cruiser, new Vector2D(), Constants.UFO_MAX_VEL, this, images.get("effect"), 0, this);
            }
            movingObjects.add(cruisero);
            cruiser = cruisero;
            //Ship ship = new NPCShip(new Vector2D(random.nextInt(Constants.WIDTH - 100 + 1), y), shipsList.get(random.nextInt(shipsList.size())), new Vector2D(), Constants.UFO_MAX_VEL, this, images.get("effect"), team, this);
        }
        assault = 0;
        type = -1;
    }

    public void resume(){
        gameSoundManager.resume();
    }
    
    public void pause(){
        gameSoundManager.pause();
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

            BufferedImage texture = MissionStats.stars.get("big" + 2);
            createGameObject(new Meteor(new Vector2D(x, y), texture, new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2), Constants.METEOR_INIT_VEL * random.nextDouble() + 1, MeteorSize.BIG, this, player));
        }
    }

    private void spawnShip(int limit, int x, int y, int team, List<ShipStats> shipsList) {
        for (int i = 0; i < limit; i++) {
            Ship ship = new NPCShip(new Vector2D(random.nextInt(Constants.WIDTH - 100 + 1), y), shipsList.get(random.nextInt(shipsList.size())), new Vector2D(), Constants.UFO_MAX_VEL, this, images.get("effect"), team, this);
            createGameObject(ship);
        }
    }

    private void spawnObjects(float dt) {
        if (waves <= MissionStats.assaults) {

            if (meteor && !hasActiveMeteors()) {
                startWave();
            }

            if (MissionStats.alliesExist && allies.isEmpty()) {
                spawnShip(1, 0, Constants.HEIGHT - 100, 1, MissionStats.allies);
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
                spawnShip(nroRandom, 0, 100, 0, MissionStats.axis);
            }
        }else if(type < 0){
            type = 0;
        }
    }
    
    public byte gameOver(){
        return type;
    }
    
    private void cinematic(float dt) {
        //type = 3 muere, type = 4 gana --cinematic
        if (type == 0) {
            if (MissionStats.reinforcement == 1) {
                //reforces
                if (assault == 0) {
                    //axis and reforces
                } else if (assault > 3000) {
                    //destroyAxis
                    type = 4;
                    assault = 0;
                }
            } else if(enemys.isEmpty()){
                //mensaje victoria
                gameEventManager.notifyGameEvent("VICTORY");
                type = 4;
                assault = 0;
            }
        } else if (assault == 0) {
            if (type == 3) {
                //mensaje gameOver ------ assault = 0
                gameEventManager.notifyGameEvent("GAME OVER");
            } else if (type == 4) {
                //mensaje victoria
                gameEventManager.notifyGameEvent("VICTORY");
            }
        }

        if (assault > 10000) {
            //termina el juego
            type -= 2;
        }
        assault += dt;
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
        
        gamePowerUpManager.update(dt);
        
        if(type < 0)
            spawnObjects(dt);
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
            //player.resetValues();
            //System.out.println("morir");
            objectsToNotify.add(player);
        } else if (player.isDead() || (cruiser != null && cruiser.isDead())) {
            //mensaje -> GAME OVER
            if (type > 3 || type < 1) {
                type = 3;
                assault = 0;
            }
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

        g.drawImage(missionMap, 0, 0, 1366, 768, null);

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
        gamePowerUpManager.draw(g);
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
            listToAdd.add(new Meteor(meteor.getPosition(), MissionStats.stars.get(nextSize.getSize() + nro), new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2), Constants.METEOR_INIT_VEL * Math.random() + 1, meteor.getSize().getNextSize(), this, player));
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
        Ship ship = new NPCShip(position, Assets.shipsAvaible.get(random.nextInt(Assets.shipsAvaible.size() - 1)), new Vector2D(), Constants.UFO_MAX_VEL, this, images.get("effect"), team, this);
        createGameObject(ship);
    }
}
