/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

import java.awt.image.BufferedImage;
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
    public static Clip fireSound;
    public static void init(){
        player = loadImage("/images/ships/caza1.png");
        laser = loadImage("/images/lasers/laserGreen01.png");
        effect = loadImage("/images/effects/fire08.png");
        fireSound = loadSound("/audio/ufoShoot.wav");
    }
    
    private static BufferedImage loadImage(String path) {
        count++;
        return Loader.ImageLoader(path);
    }
    
    public static Clip loadSound(String path) {
        count++;
        return Loader.loadSound(path);
    }
}
