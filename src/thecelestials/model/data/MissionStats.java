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
    public static void setPlayerShip(ShipStats player){
        playerShip = player;
    }
    
    public static void setMissionStats(String ID, String name, String description, List<ShipStats> AShip, List<ShipStats> BShip){
        missionID = ID;
        missionName = name;
        missionDescription = description;
        allies = AShip;
        axis = BShip;
    }
}
