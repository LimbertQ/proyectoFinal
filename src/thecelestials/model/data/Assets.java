/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
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
    public static BufferedImage vortex, pulsar;
    public static Clip fireSound, explosion;

    public static Font fontBig, fontMed;
    private static DataBaseManager db;

    public static void init() {
        if (db == null) {
            db = DataBaseManager.getInstance("src/thecelestials/model/data/TheCelestialsDB.db");
            db.openConnection();

            List<Map<String, Object>> rawNave = db.readAvailableShips();
            for (Map<String, Object> audio : rawNave) {
                BufferedImage buffer = loadImage("" + audio.get("shipAssetPath"));
                BufferedImage buffer2 = loadImage("" + audio.get("profileShipPath"));

                Map<String, Object> lasers = db.readLaserByID("" + audio.get("laserID"));
                BufferedImage bufferLaser = loadImage("" + lasers.get("laserAssetPath"));

                Map<String, Object> cvl = db.readCVLByID("" + audio.get("civilizationID"));
                BufferedImage bufferCvl = loadImage("" + cvl.get("civilizationPath"));

                if (buffer != null && buffer2 != null && bufferLaser != null && bufferCvl != null) {

                    System.out.println(audio.get("shipHealth") + "h : d" + audio.get("shipDamage") + " nom: " + audio.get("shipName") + true + "IDLaser" + audio.get("laserID") + "civID: " + audio.get("civilizationID"));

                } else {
                    if (bufferCvl == null) {
                        System.err.println("ERROR en Civilizacion shipID: " + audio.get("shipID") +" "+ audio.get("shipName") + " ID: " + audio.get("civilizationID"));

                    } else if (bufferLaser == null) {
                        System.err.println("ERROR en Laser shipID: " + audio.get("shipID") + " " +audio.get("shipName") + " ID: " + audio.get("laserID"));
                    } else {
                        System.err.println("ERROR en la Nave shipID: " + audio.get("shipName"));
                    }
                }
            }

            List<Map<String, Object>> rawLaser = db.readAvailableLaser();
            for (Map<String, Object> audio : rawLaser) {
                BufferedImage buffer = loadImage("" + audio.get("laserAssetPath"));
                if (buffer != null) {
                    System.out.println("laserID: "+audio.get("laserID")+" Danio: "+audio.get("laserDamage") + " tipo de laser: "+ audio.get("laserName") + " Ruta: "+audio.get("laserAssetPath"));
                } else {
                    System.out.println("" + audio.get("laserName") + false);
                }
            }

            List<Map<String, String>> rawCiv = db.readAvailableCivilization();
            for (Map<String, String> audio : rawCiv) {
                BufferedImage buffer = loadImage("" + audio.get("civilizationPath"));
                if (buffer != null) {
                    System.out.println("ID"+audio.get("civilizationID") + " : " + audio.get("civilizationName") + " Ruta: "+ audio.get("civilizationPath"));
                } else {
                    System.err.println("ID"+ audio.get("civilizationID") + " : " + audio.get("civilizationName"));
                }
            }

            db.closeConnection();

        }
        player = loadImage("/images/ships/fighter01.png");

        //player = loadImage("/images/ships/fighter01.png");
        //player = loadImage("/images/ships/fighterProfile01.png");
        laser = loadImage("/images/lasers/laserBlue01.png");
        effect = loadImage("/images/effects/fire08.png");
        fireSound = loadSound("/audio/playerShoot.wav");
        explosion = loadSound("/audio/explosion.wav");
        fontBig = loadFont("/fonts/futureFont.ttf", 42);
        fontMed = loadFont("/fonts/futureFont.ttf", 20);
        vortex = loadImage("/images/gravitationalFields/vortex.png");
        pulsar = loadImage("/images/gravitationalFields/pulsar.png");
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

        for (int i = 0; i < 9; i++) {
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
