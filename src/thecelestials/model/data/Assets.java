/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.Clip;
import thecelestials.view.util.Loader;

//import javafx.scene.media.Media;
/**
 *
 * @author pc
 */
public class Assets {

    public static int count = 0;
    public static int MAX_COUNT = 131;
    public static int currentShip = 0;
    public static boolean loaded = false;
    //public static boolean unlock = false;

    public static int lives = 3;
    public static int money = 0;

    public static BufferedImage player, effect, fondo;
    public static BufferedImage[] shieldEffects = new BufferedImage[3];
    //public static BufferedImage[] explosions = new BufferedImage[9];
    public static Map<String, BufferedImage> images = new HashMap<>();
    //public static Map<String, BufferedImage> imagesTemp = new HashMap<>();
    public static Map<String, BufferedImage> missionMaps = new HashMap<>();
    public static Map<String, Clip> audioCache = new HashMap<>();
    public static Map<String, MediaPlayer> audioMediaCache = new HashMap<>();
    public static List<ShipStats> shipsAvaible = new ArrayList<>();
    public static Map<String, Campaign> campaigns = null;
    public static Map<String, List<AssetDefinition>> informations = new HashMap<>();
    public static EntityStats powerBullet;

    public static Font fontBig, fontMed, fontSmall;
    private static DataBaseManager db;

    public static void init() {
        if (db == null) {
            //src/thecelestials/model/data/
            db = DataBaseManager.getInstance("src/thecelestials/model/data/TheCelestialsDB.db");
            db.openConnection();
            fontBig = loadFont("/fonts/futureFont.ttf", 42);
            fontMed = loadFont("/fonts/futureFont.ttf", 20);
            fontSmall = loadFont("/fonts/futureFont.ttf", 18);
            int[] progress = db.readProgress();
            lives = progress[0];
            money = progress[1];
            campaigns = loadCampaigns();
            ProgressionManager.getInstance();
            fondo = images.get("la doble alianza");
            loadShipAvaible();
            loadCivilizations();
            getInformation();
            readAllSounds();
            readAllSoundsMedia();
            readAllImages();
            powerBullet = db.readLaserByID("LSR06");
            setImageLaser(powerBullet);

        }
        player = images.get("life");
        effect = images.get("effect");
        for (int i = 0; i < 3; i++) {
            shieldEffects[i] = images.get("shield" + i);
        }
        count = MAX_COUNT;
        loaded = true;
    }

    public static void closeDbConnection() {
        if (db != null) {
            db.closeConnection();
            db = null; // Opcional: Establecer a null para liberar la referencia
            System.out.println("Conexión a la base de datos cerrada.");
        }
    }

    public static BufferedImage getImage(String keyImage) {
        BufferedImage res;
        if (images.containsKey(keyImage)) {
            res = images.get(keyImage);
        } else {
            res = missionMaps.get(keyImage);
        }
        return res;
    }

    private static void setImageLaser(EntityStats bullet) {
        if (images.containsKey(bullet.getSpriteKey())) {
            count++;
        }else{
            images.put(bullet.getSpriteKey(), loadImage(bullet.getSpritePath()));
        }
    }

    public static void loadShipAvaible() {
        shipsAvaible = db.readAvailableShips();
        for (ShipStats ship : shipsAvaible) {
            images.put(ship.getName(), loadImage(ship.getProfileImagePath()));
            images.put(ship.getSpriteKey(), loadImage(ship.getSpritePath()));
            EntityStats bullet = ship.getEntityStats();
            setImageLaser(bullet);
        }
    }

    public static void updatePlayerStatus(int life, int coins) {
        lives += life;
        if (lives < 3) {
            lives = 3;
        }

        money += coins;
        if (money < 0) {
            money = 0;
        }
        //UPDATE DATABASE
        db.updateProgress(lives, money);
    }

    public static ShipStats getCurrentShip() {
        return shipsAvaible.get(currentShip);
    }

    private static Map<String, Campaign> loadCampaigns() {
        Map<String, Campaign> list = db.readCampaigns();
        for (Campaign campaign : list.values()) {
            images.put(campaign.getName(), loadImage(campaign.getProfileImagePath()));
        }
        return list;
    }

    public static Map<String, AssetDefinition> loadMissionsByCampaign(String campaignID) {
        missionMaps.clear();
        Map<String, AssetDefinition> missions = db.readMissionsByCampaign(campaignID);
        return missions;
    }

    private static void loadSpriteShips(List<ShipStats> ships) {
        for (ShipStats ship : ships) {
            if (!images.containsKey(ship.getName())) {
                missionMaps.put(ship.getName(), loadImage(ship.getProfileImagePath()));
                missionMaps.put(ship.getSpriteKey(), loadImage(ship.getSpritePath()));
            } else {
                count += 2;
            }
            EntityStats bullet = ship.getEntityStats();
            setImageLaser(bullet);
        }
    }

    public static void getInformation() {
        String[][] infoGame = {{"credits", """
                                           === CREDITOS ===
                                           
                                           DISEÑO Y PRODUCCION: LIMBERT QUISPE QUISPE.
                                           DESARROLLO: LOS CELESTIALES
                                           
                                           CONCEPTO Y NARRATIVA
                                           UNIVERSO Y NAVES: LIMBERT QUISPE Q.
                                           
                                           ARTE Y GRAFICOS
                                           ARTE CONCEPTUAL: LIMBERT QUISPE Q.""", "/images/others/dev.png", "creditos"},
        {"tutorial", """
                                                CONTROLES:
                                                
                                                MOVIMIENTO: W AVANZAR, A Y D ROTAR 
                                                ATAQUE: P DISPARAR, O TECNICA ESPECIAL
                                                
                                                TECNICA ESPECIAL:
                                                
                                                UFO: INVOCAR COMPAÑERO, CAZA: MISIL,
                                                CRUCERO: MISIL E INVOCAR COMPAÑERO""", "/images/others/tutorial.png", "instrucciones"},
        {"shoop", "", "/images/ships/profileFighter01.png", "compras"}};
        for (int i = 0; i < infoGame.length; i++) {
            AssetDefinition info = new AssetDefinition("DES0" + (i + 1), infoGame[i][0], infoGame[i][1], infoGame[0][2], 1);
            List<AssetDefinition> infoList = new ArrayList<>();
            infoList.add(info);
            images.put(infoGame[i][0], loadImage(infoGame[i][2]));
            informations.put(infoGame[i][3], infoList);
        }
    }

    public static void setear() {
        loaded = false;
        count = 0;

    }

    public static void loadGame(String missionID) {
        //setear();
        MAX_COUNT = 10;
        missionMaps.clear();
        db.readMissionsByID(missionID);
        MAX_COUNT = MissionStats.allShips[0].size() * 3 + MissionStats.allShips[1].size() * 3 + MissionStats.allShips[2].size() * 3 + MissionStats.starsAssets.size() + 2;
        readStarsImages(MissionStats.starsAssets);
        missionMaps.put(MissionStats.missionName, loadImage(MissionStats.allPaths.get("missionMapPath")));
        //MAX_COUNT = shipsList.length;
        for (List<ShipStats> listShips : MissionStats.allShips) {
            loadSpriteShips(listShips);
        }

        for (Map.Entry<String, String> entry : MissionStats.allPaths.entrySet()) {
            if (entry.getKey().substring(0, 5).equals("voice")) {
                loadMediaSound(entry.getKey(), entry.getValue(), MissionStats.missionVoicePath);
            }
        }
        loaded = true;
    }

    public static void clear() {

        for (MediaPlayer media : MissionStats.missionVoicePath.values()) {
            media.pause();
            media.stop();
            media.dispose();
        }

        for (MediaPlayer media : audioMediaCache.values()) {
            media.pause();
            media.stop();
            media.dispose();
        }
    }

    private static void loadCivilizations() {
        List<AssetDefinition> civilizations = db.readAvailableCivilization();
        for (AssetDefinition civilization : civilizations) {
            images.put(civilization.getName(), loadImage(civilization.getProfileImagePath()));
        }
        informations.put("civilizaciones", civilizations);
    }

    private static void readAllSounds() {
        List<Map<String, String>> AllSounds = db.readSounds();
        for (Map<String, String> audio : AllSounds) {
            audioCache.put(audio.get("soundName"), loadSound(audio.get("soundPath")));
        }
    }

    private static void readAllSoundsMedia() {
        List<Map<String, String>> AllSounds = db.readSoundsMedia();
        for (Map<String, String> audio : AllSounds) {
            loadMediaSound(audio.get("soundName"), audio.get("soundPath"), audioMediaCache);
        }
    }

    private static void loadMediaSound(String key, String path, Map<String, MediaPlayer> media) {
        String path2 = Loader.loadMedia(path);
        if (path2 != null) {
            count++;
            Platform.runLater(() -> {
                media.put(key, new MediaPlayer(new Media(path2)));
            });
        }
    }

    private static void readAllImages() {
        List<Map<String, String>> AllImages = db.readAllImages();
        for (Map<String, String> image : AllImages) {
            BufferedImage imagen = loadImage(image.get("imagePath"));
            images.put(image.get("imageName"), imagen);
        }
    }

    private static void readStarsImages(List<AssetDefinition> stars) {
        for (AssetDefinition starAsset : stars) {
            missionMaps.put(starAsset.getName(), loadImage(starAsset.getProfileImagePath()));
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
