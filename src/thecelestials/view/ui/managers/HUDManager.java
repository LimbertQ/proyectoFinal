/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui.managers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import thecelestials.model.data.Assets;
import thecelestials.model.data.MissionStats;
import thecelestials.model.gameObjects.Meteor;
import thecelestials.model.gameObjects.MovingObject;
import thecelestials.model.gameObjects.PlayerShip;
import thecelestials.model.gameObjects.PowerUp;
import thecelestials.model.gameObjects.Ship;
import thecelestials.model.managers.GameManager;
import thecelestials.model.managers.GameNotificationListener;
import thecelestials.model.managers.GameObjectDestroyedListener;
import thecelestials.model.managers.IGameControl;
import thecelestials.model.managers.IGameLoopEntity;
import thecelestials.model.managers.IStartableWithPlayer;
import thecelestials.model.managers.ScoreChangeListener;
import thecelestials.model.math.Constants;

/**
 *
 * @author pc
 */
public class HUDManager extends GameManager implements IGameControl, IGameLoopEntity, IStartableWithPlayer, GameObjectDestroyedListener, GameNotificationListener, ScoreChangeListener {

    private int score = 0;
    private byte finalScore = 1;
    private PlayerShip player;
    private final BufferedImage[] numbers;
    private byte assaults = 0;
    private byte waves = 0;

    public HUDManager() {
        numbers = new BufferedImage[11];
        loadNumbersImages();
    }

    @Override
    public void clear() {
        player = null;
        score = 0;
        finalScore = 1;
        assaults = 0;
        waves = 0;
    }

    @Override
    public void playGame(PlayerShip p) {
        player = p;
        waves = MissionStats.assaults;
        if (MissionStats.challenge == 1) {
            waves /= 3;
        }
    }

    public int getScore() {
        return score;
    }

    private void loadNumbersImages() {
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Assets.images.get("num" + i);
        }
    }

    private void drawNumbers(int x, int y, int number, Graphics2D g) {
        String str = String.valueOf(number);
        for (char c : str.toCharArray()) {
            g.drawImage(numbers[Character.getNumericValue(c)], x, y, null);
            x += 20;
        }
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void draw(Graphics2D g) {
        if (player != null) {
            drawScore(g);
            drawLives(g);
        }
        drawAssaults(g);
    }

    private void drawScore(Graphics2D g) {
        drawNumbers(1200, 25, score, g);
    }

    private void drawLives(Graphics2D g) {
        if (!player.isDead()) {
            g.drawImage(Assets.player, 25, 25, null);
            g.drawImage(numbers[10], 65, 30, null); // X
            drawNumbers(85, 30, player.getLives(), g);
        }
    }

    private void drawAssaults(Graphics2D g) {
        g.drawImage(Assets.player, 600, 25, null);
        drawNumbers(650, 30, assaults, g);
        g.drawImage(numbers[10], 675, 30, null);
        drawNumbers(700, 30, waves, g);
    }

    @Override
    public void onGameObjectDestroyed(MovingObject destroyedObject) {
        if (destroyedObject instanceof Meteor) {
            score += Constants.METEOR_SCORE * finalScore;
        } else if (destroyedObject instanceof Ship ship && ship.getTeam() == 0) {
            score += Constants.UFO_SCORE * finalScore;
        }
    }

    @Override
    public void onGameNotify(String type) {
        if (type.equals("WAVE") || (type.equals("ASSAULT") && MissionStats.challenge != 1)) {
            if(assaults<waves){
                assaults++;
            }
        }
    }

    @Override
    public void notifyPowerUp(PowerUp type) {
        if (type.getType().textureKey.equals("star")) {
            score += 1000;
        }
    }

    @Override
    public void onScoreChanged(byte finalScore) {
        this.finalScore = finalScore;
    }

    @Override
    public void pause() {
        Assets.lives = player.getLives();
        Assets.updatePlayerStatus(0, score);
    }

    @Override
    public void resume() {

    }

}
