/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

/**
 *
 * @author pc
 */
public class MissionStats {
    public static String missionID;
    public static String missionName;
    public static ShipStats playerShip;
    public static void setPlayerShip(ShipStats player){
        playerShip = player;
    }
    
    public static void setMissionStats(String ID, String name){
        missionID = ID;
        missionName = name;
    }
}
