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
import javax.sound.sampled.Clip;
import thecelestials.view.util.Loader;

/**
 *
 * @author pc
 */
public class Assets {

    public static int count = 0;
    public static int MAX_COUNT = 100;
    public static int currentShip = 0;
    public static BufferedImage player, effect, fondo;
    //public static BufferedImage[] numbers = new BufferedImage[11];
    public static BufferedImage[] meteors = new BufferedImage[10];
    //public static BufferedImage[] explosions = new BufferedImage[9];
    public static Map<String, BufferedImage> images = new HashMap<>();
    public static Map<String, BufferedImage> missionMaps = new HashMap<>();
    public static Map<String, BufferedImage> stars = new HashMap<>();
    public static Map<String, Clip> audioCache = new HashMap<>();
    public static List<ShipStats> shipsAvaible = new ArrayList<>();
    public static Map<String, Campaign> campaigns = null;
    public static EntityStats powerBullet;
    public static BufferedImage vortex, pulsar;

    public static Font fontBig, fontMed;
    private static DataBaseManager db;

    public static void init() {
        if (db == null) {
            db = DataBaseManager.getInstance("src/thecelestials/model/data/TheCelestialsDB.db");
            db.openConnection();

            loadShipAvaible();
            readAllSounds();
            readAllImages();
            readStarsImages();
            powerBullet = db.readLaserByID("LSR06");
            setImageLaser(powerBullet);
            campaigns = loadCampaigns();
            //db.closeConnection();
        }
        player = loadImage("/images/ships/fighter01.png");
        fondo = loadImage("/images/maps/exoplaneta.jpeg");
        effect = loadImage("/images/effects/fire08.png");
        images.put("effect", loadImage("/images/effects/fire08.png"));
        fontBig = loadFont("/fonts/futureFont.ttf", 42);
        fontMed = loadFont("/fonts/futureFont.ttf", 20);
        vortex = loadImage("/images/gravitationalFields/vortex.png");
        pulsar = loadImage("/images/gravitationalFields/pulsar.png");
        for (int i = 0; i < meteors.length; i++) {
            meteors[i] = loadImage("/images/numbers/num" + i + ".png");
        }
        /*for (int i = 0; i < numbers.length; i++) {
            numbers[i] = loadImage("/images/numbers/num" + i + ".png");
        }

        for (int i = 0; i < 9; i++) {
            explosions[i] = loadImage("/images/explosions/exp" + i + ".png");
        }*/
    }
    
    public static void closeDbConnection() {
        if (db != null) {
            db.closeConnection();
            db = null; // Opcional: Establecer a null para liberar la referencia
            System.out.println("Conexión a la base de datos cerrada.");
        }
    }
    
    private static void setImageLaser(EntityStats bullet){
        //images.put(bullet.getName(), loadImage(bullet.getProfileImagePath()));
        images.put(bullet.getSpriteKey(), loadImage(bullet.getSpritePath()));
    }
    
    private static void loadShipAvaible(){
        shipsAvaible = db.readAvailableShips();
        for(ShipStats ship: shipsAvaible){
            images.put(ship.getName(), loadImage(ship.getProfileImagePath()));
            images.put(ship.getSpriteKey(), loadImage(ship.getSpritePath()));
            EntityStats bullet = ship.getEntityStats();
            setImageLaser(bullet);
            System.out.println("ship dispo");
        }
    }
    
    public static ShipStats getCurrentShip(){
        return shipsAvaible.get(currentShip);
    }
    
    private static Map<String, Campaign> loadCampaigns(){
        Map<String, Campaign> list = db.readCampaigns();
        for(Campaign campaign : list.values()){
            //System.out.println(campaign.getProfileImagePath());
            images.put(campaign.getName(), loadImage(campaign.getProfileImagePath()));
        }
        return list;
    }
    
    public static Map<String, Mission> loadMissionsByCampaign(String campaignID){
        missionMaps.clear();
        Map<String, Mission> missions = db.readMissionsByCampaign(campaignID);
        for(Mission mission : missions.values()){
            //System.out.println(mission.getProfileImagePath());
            missionMaps.put(mission.getName(), loadImage(mission.getProfileImagePath()));
        }
        return missions;
    }
    
    public static void loadGame(String missionID){
        Mission mission = db.readMissionsByID(missionID);
        List<ShipStats>[] shipsList = db.readShipsByMission(missionID);
        byte challenge = 0;
        if(mission.getChallenge().equals("WAVES")){
            challenge++;
        }
        MissionStats.setMissionStats(missionID, mission.getName(), mission.getDescription(), shipsList, challenge, (byte)mission.getAssaults());
        for(ShipStats ship: MissionStats.allies){
            images.put(ship.getName(), loadImage(ship.getProfileImagePath()));
            images.put(ship.getSpriteKey(), loadImage(ship.getSpritePath()));
            EntityStats bullet = ship.getEntityStats();
            setImageLaser(bullet);
            //System.out.println("ship dispo");
        }
        
        for(ShipStats ship: MissionStats.axis){
            images.put(ship.getName(), loadImage(ship.getProfileImagePath()));
            images.put(ship.getSpriteKey(), loadImage(ship.getSpritePath()));
            EntityStats bullet = ship.getEntityStats();
            setImageLaser(bullet);
            //System.out.println("ship dispo");
        }
    }
    
    public static List<AssetDefinition> loadCivilizations(){
        List<AssetDefinition> civilizations = db.readAvailableCivilization();
        return civilizations;
    }
    
    private static void readAllSounds(){
        List<Map<String, String>> AllSounds = db.readSounds();
        for(Map<String, String> audio : AllSounds){
            audioCache.put(audio.get("soundName"), loadSound(audio.get("soundPath")));
        }
        
    }
    
    private static void readAllImages(){
        List<Map<String, String>> AllImages = db.readAllImages();
        for(Map<String, String> image : AllImages){
            BufferedImage imagen = loadImage(image.get("imagePath"));
            images.put(image.get("imageName"), imagen);
            if(imagen == null){
                System.err.println(false);
            }else{
                System.err.println(true);
            }
        }
    }
    
    private static void readStarsImages(){
        List<Map<String, String>> AllImages = db.readStars();
        for(Map<String, String> image : AllImages){
            BufferedImage imagen = loadImage(image.get("starAssetPath"));
            stars.put(image.get("starName"), imagen);
            System.out.println(image.get("starName") + ":_:" + image.get("starAssetPath"));
            if(imagen == null){
                System.err.println(false);
            }else{
                System.err.println(true);
            }
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
