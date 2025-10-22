/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

import java.util.List;
import java.util.Map;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author pc
 */
public class MissionStats {

    public static String missionID;
    public static String missionName;
    public static String missionDescription;
    public static ShipStats playerShip;
    public static List<ShipStats> allies;
    public static List<ShipStats> axis;
    public static ShipStats cruiserAllie;
    public static Map<String, MediaPlayer> missionVoicePath;
    public static boolean alliesExist;
    public static byte challenge;
    public static byte assaults;
    public static byte reinforcement;

    public static void setPlayerShip(ShipStats player) {
        playerShip = player;
    }

    public static void setMissionStats(String ID, String name, String description, List<ShipStats>[] shipsArray, byte challeng, byte ass, Map<String, MediaPlayer> audioMission) {
        missionID = ID;
        missionName = name;
        missionDescription = description;
        allies = shipsArray[0];
        axis = shipsArray[1];
        cruiserAllie = null;
        cruiserAllie = shipsArray[2].getFirst();
        alliesExist = !allies.isEmpty();
        challenge = challeng;
        assaults = ass;
        clear();
        missionVoicePath = audioMission;
        if (challenge == 1) {
            assaults *= 3;
        }
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
