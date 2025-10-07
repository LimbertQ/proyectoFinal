/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.managers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import thecelestials.model.data.Assets;
import thecelestials.model.gameObjects.Meteor;
import thecelestials.model.gameObjects.MovingObject;
import thecelestials.model.gameObjects.PlayerShip;
import thecelestials.model.gameObjects.Ship;
import thecelestials.model.math.Constants;

/**
 *
 * @author pc
 */
public class HUDManager implements GameObjectDestroyedListener{
    private int score = 0;
    private final PlayerShip player;
    private final BufferedImage[] numbers;
    public HUDManager(PlayerShip p){
        this.player = p;
        numbers = new BufferedImage[11];
        loadNumbersImages();
    }
    
    private void loadNumbersImages(){
        for(int i=0; i<numbers.length; i++){
            numbers[i] = Assets.images.get("num"+i);
        }
    }
    
    private void drawNumbers(int x, int y, int number, Graphics g) {
        String str = String.valueOf(number);
        for (char c : str.toCharArray()) {
            g.drawImage(numbers[Character.getNumericValue(c)], x, y, null);
            x += 20;
        }
    }
    
    public void draw(Graphics g){
        drawScore(g);
        drawLives(g);
    }
    
    private void drawScore(Graphics g) {
        drawNumbers(1200, 25, score, g);
    }

    private void drawLives(Graphics g) {
        if (!player.isDead()) {
            //g.drawImage(Assets.player, 25, 25, null);
            g.drawImage(numbers[10], 65, 30, null); // X
            drawNumbers(85, 30, player.getLives(), g);
        }
    }

    @Override
    public void onGameObjectDestroyed(MovingObject destroyedObject) {
        if(destroyedObject instanceof Meteor){
            score += Constants.METEOR_SCORE;
        }else if(destroyedObject instanceof Ship ship && ship.getTeam()==0){
            score += Constants.UFO_SCORE;
        }
    }
}
