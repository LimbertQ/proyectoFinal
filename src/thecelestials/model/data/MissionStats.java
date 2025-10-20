/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

import java.util.List;

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
    public static boolean alliesExist;
    public static byte challenge;
    public static byte assaults;
    public static byte reinforcement;
    public static void setPlayerShip(ShipStats player){
        playerShip = player;
    }
    
    public static void setMissionStats(String ID, String name, String description, List<ShipStats>[] shipsArray, byte challeng, byte ass){
        missionID = ID;
        missionName = name;
        missionDescription = description;
        allies = shipsArray[0];
        axis = shipsArray[1];
        alliesExist = !allies.isEmpty();
        challenge = challeng;
        assaults = ass;
        if(challenge == 1)
            assaults *= 3;
    }
}
