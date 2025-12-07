/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author pc
 */
public class MissionStats {

    public static String missionID;
    public static String campaignID;
    public static String missionName;
    public static String missionDescription;
    public static ShipStats playerShip;
    public static List<ShipStats>[] allShips;
    public static Map<String, MediaPlayer> missionVoicePath;
    //public static Map<String, BufferedImage> stars;
    public static Map<String, String> allPaths;
    public static List<AssetDefinition> starsAssets;
    public static boolean alliesExist;
    public static byte challenge;
    public static byte assaults;
    public static byte reinforcement;
    public static void setPlayerShip(ShipStats player) {
        playerShip = player;
    }

    public static void setMissionStats(String ID, String name, String description, String mapPath, String challeng, byte assau, byte reinfor, List<ShipStats>[] shipsArray, Map<String, String> paths, List<AssetDefinition> assets, String campID) {
        missionID = ID;
        missionName = name;
        missionDescription = description;
        allShips = shipsArray;
        allPaths = paths;
        alliesExist = !allShips[1].isEmpty();
        starsAssets = assets;
        assaults = assau;
        challenge = 0;
        clear();
        if (challeng.equals("WAVES")) {
            challenge++;
            assaults *= 3;
        }
        campaignID = campID;
        reinforcement = reinfor;
    }

    private static void clear() {
        if (missionVoicePath != null) {
            for (MediaPlayer mediaPlayer : missionVoicePath.values()) {
                mediaPlayer.pause();
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            missionVoicePath.clear();
        }else{
            missionVoicePath = new HashMap<>();
        }
    }
}