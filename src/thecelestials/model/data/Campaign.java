/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

import java.util.Map;

/**
 *
 * @author pc
 */
public class Campaign extends AssetDefinition{

    private final String videoPath;
    private final Map<String, AssetDefinition> missions;

    public Campaign(String campaignID, String campaignName, String mapPath, String videoPath, String campaignDescription, int campaignState, Map<String, AssetDefinition> missions) {
        super(campaignID, campaignName, mapPath, campaignDescription, campaignState);
        this.videoPath = videoPath;
        this.missions = missions;
    }
    
    public AssetDefinition getMissionByID(String missionID){
        return missions.get(missionID);
    }
    
    public boolean containsMission(String missionID){
        return missions.containsKey(missionID);
    }

    public String getVideoPath() {
        return videoPath;
    }
}
