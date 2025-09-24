/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.Clip;
import thecelestials.view.util.Loader;

/**
 *
 * @author pc
 */
public class Assets {

    public static int count = 0;
    public static int MAX_COUNT = 100;
    public static BufferedImage player, laser, effect;
    public static BufferedImage[] numbers = new BufferedImage[11];
    public static BufferedImage[] meteors = new BufferedImage[10];
    public static BufferedImage[] explosions = new BufferedImage[9];
    public static Map<String, BufferedImage> images = new HashMap<>();
    public static Clip fireSound, explosion;

    public static Font fontBig, fontMed;

    public static void init() {
        player = loadImage("/images/ships/caza1.png");
        laser = loadImage("/images/lasers/laserGreen01.png");
        effect = loadImage("/images/effects/fire08.png");
        fireSound = loadSound("/audio/playerShoot.wav");
        explosion = loadSound("/audio/explosion.wav");
        fontBig = loadFont("/fonts/futureFont.ttf", 42);
        fontMed = loadFont("/fonts/futureFont.ttf", 20);
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = loadImage("/images/numbers/num" + i + ".png");
        }
        //CARGAR
        for (int i = 1; i < 5; i++) {
            //meteors[i] = loadImage("/images/meteors/Bbig"+i+".png");
            images.put("Bbig" + i, loadImage("/images/meteors/Bbig" + i + ".png"));
        }
        for (int i = 1; i < 3; i++) {
            //meteors[i] = loadImage("/images/meteors/Bmed"+i+".png");
            images.put("Bmed" + i, loadImage("/images/meteors/Bmed" + i + ".png"));
        }
        for (int i = 1; i < 3; i++) {
            //meteors[i] = loadImage("/images/meteors/Bsmall"+i+".png");
            images.put("Bsmall" + i, loadImage("/images/meteors/Bsmall" + i + ".png"));
        }
        for (int i = 1; i < 3; i++) {
            //meteors[i] = loadImage("/images/meteors/Btiny"+i+".png");
            images.put("Btiny" + i, loadImage("/images/meteors/Btiny" + i + ".png"));
        }
        
        for(int i=0; i<9; i++){
            explosions[i] = loadImage("/images/explosions/exp" + i + ".png");
        }
    }

    private static BufferedImage loadImage(String path) {
        count++;
        return Loader.ImageLoader(path);
    }

    public static Clip loadSound(String path) {
        count++;
        return Loader.loadSound(path);
    }
    
    private static Font loadFont(String path, int size) {
        count++;
        return Loader.loadFont(path, size);
    }
}
