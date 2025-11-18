/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;


/**
 *
 * @author pc
 */
public class ShipStats extends EntityStats{
    private EntityStats bullet;
    private final String shipClass;
    private final String laserID;
    private final String civilizationID;
    private final int team;
    public ShipStats(String id, String shipClass, String name, String description, String profileImagePath, int state, String spritePath, int health, int damage, String laserID, String civID, int team) {
        super(id, name, description, profileImagePath, state, spritePath, health, damage);
        
        this.shipClass = shipClass;
        this.laserID = laserID;
        this.civilizationID = civID;
        this.team = team;
    }
    
    public EntityStats getEntityStats(){
        return bullet;
    }
    
    public int getTeam(){
        return team;
    }
    
    public void setEntityStats(EntityStats entity){
        this.bullet = entity;
    }
    //CIVILIZACION
    public String getCivilizationID(){
        return civilizationID;
    }
    
    public String getEntityStatsID(){
        return laserID;
    }
    //-----
    public String getShipClass(){
        return shipClass;
    }
}
