/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.managers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import thecelestials.model.data.MissionStats;
import thecelestials.model.gameObjects.PlayerShip;
import thecelestials.model.gameObjects.PowerUp;
import thecelestials.model.gameObjects.PowerUpTypes;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public class GamePowerUpManager extends GameManager implements IGameLoopEntity, IStartableWithPlayer{
    private PlayerShip player;
    private final List<PowerUp> activePowerUps = new ArrayList<>();
    //private final List<PowerUp> consumedPowerUps = new ArrayList<>();
    private final Map<String, BufferedImage> imagesCache;
    private long powerUpSpawner = 0;
    private final GameEventManager gameEventManager;
    protected byte scoreMultiplier = 1;
    private int aleatorio = 0;
    
    private long shieldTimer, doubleGunTimer, fasterFireTimer, scoreTimer;
    public GamePowerUpManager(Map<String, BufferedImage> imagesCache, GameEventManager gameEventManager) {
        this.imagesCache = imagesCache;
        this.gameEventManager = gameEventManager;
        shieldTimer = 0;
        doubleGunTimer = 0;
        fasterFireTimer = 0;
        scoreTimer = 0;
    }
    
    private void executeAction(PowerUpTypes type){
        
        switch (type) {
            case LIFE -> {
                //player.addLives();
            }
            case SHIELD -> {
                shieldTimer = 0;
            }
            case SCORE_X2 -> {
                scoreTimer = 0;
                gameEventManager.notifyScoreChanged((byte)2);
            }
            case FASTER_FIRE -> {
                fasterFireTimer = 0;
            }
            case SCORE_STACK -> {
                //sm.addScore(1000);
            }
            case DOUBLE_GUN -> {
                doubleGunTimer = 0;
            }

        };
    }
    
    private void notifyPowerUp(PowerUp po){
        if(po.isConsumed()){
            //NOTIFICAR
            po.executeTimer();
            executeAction(po.getType());
            gameEventManager.notifyPowerUp(po);
            //consumedPowerUps.add(po);
        }
    }
    
    private void powerUpsTimer(float dt){
        if(shieldTimer > -1){
            shieldTimer+=dt;
            if(shieldTimer > Constants.SHIELD_TIME){
                shieldTimer = -1;
                player.setShield(false); 
            }
        }
        if(doubleGunTimer > -1){
            doubleGunTimer+=dt;
            if(doubleGunTimer > Constants.DOUBLE_GUN_TIME){
                doubleGunTimer = -1;
                player.setDoubleGun(false);
            }
        }
        if(fasterFireTimer > -1){
            fasterFireTimer+=dt;
            if(fasterFireTimer > Constants.FAST_FIRE_TIME){
                fasterFireTimer = -1;
                player.setFastFire(false);
            }
        }
        if(scoreTimer > -1){
            scoreTimer+=dt;
            if(scoreTimer > Constants.DOUBLE_SCORE_TIME){
                scoreTimer = -1;
                gameEventManager.notifyScoreChanged((byte)1);
            }
        }
    }
    
    private void spawnPowerUp() {
        int x = (int) ((Constants.WIDTH - 64) * Math.random());
        int y = (int) ((Constants.HEIGHT - 64) * Math.random());
        Vector2D pos = new Vector2D(x, y);
        PowerUpTypes type = PowerUpTypes.values()[(int) (Math.random() * aleatorio)];
        
        activePowerUps.add(new PowerUp(pos, imagesCache.get("orb"), imagesCache.get(type.textureKey), type, player));
        //System.out.println("pow"+type.type);
    }
    
    @Override
    public void clear(){
        player = null;
        activePowerUps.clear();
        powerUpSpawner = 0;
        
        shieldTimer = -1;
        doubleGunTimer = -1;
        fasterFireTimer = -1;
        scoreTimer = -1;
        scoreMultiplier = 1;
        aleatorio = PowerUpTypes.values().length;
    }
    
    @Override
    public void playGame(PlayerShip player){
        this.player = player;
        if(!MissionStats.stars.containsKey("big1")){
            aleatorio--;
        }
    }
    
    @Override
    public void update(float dt){
        powerUpSpawner += dt;
        Iterator<PowerUp> iterator = activePowerUps.iterator();
        while(iterator.hasNext()){
            PowerUp po = iterator.next();
            po.update(dt);
            if(po.isExpired()){
                notifyPowerUp(po);
                iterator.remove();
            }
                
        }
        powerUpsTimer(dt);
        
        if (powerUpSpawner > Constants.POWER_UP_SPAWN_TIME) { spawnPowerUp(); powerUpSpawner = 0; }
    }
    
    @Override
    public void draw(Graphics2D g){
        for(PowerUp pu : activePowerUps)pu.draw(g);
    }
}
