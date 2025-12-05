/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

import java.awt.image.BufferedImage;
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
    public static Map<String, BufferedImage> stars;
    public static boolean alliesExist;
    public static byte challenge;
    public static byte assaults;
    public static byte reinforcement;

    public static void setPlayerShip(ShipStats player) {
        playerShip = player;
    }

    public static void setMissionStats(String ID, String name, String description, List<ShipStats>[] shipsArray, byte challeng, byte ass, Map<String, MediaPlayer> audioMission, Map<String, BufferedImage> star, byte reinfor, String campID) {
        missionID = ID;
        missionName = name;
        missionDescription = description;
        allShips = shipsArray;
        alliesExist = allShips[1].isEmpty();
        challenge = challeng;
        assaults = ass;
        clear();
        missionVoicePath = audioMission;
        if (challenge == 1) {
            assaults *= 3;
        }
        stars = star;
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
        }
    }
}