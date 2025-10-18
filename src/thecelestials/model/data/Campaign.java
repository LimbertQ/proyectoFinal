/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

/**
 *
 * @author pc
 */
public class Campaign extends GameEntity{
    private final String videoPath;
    public Campaign(String campaignID, String campaignName, String mapPath, String videoPath, String campaignDescription, int campaignState){
        super(campaignID, campaignName, mapPath, campaignDescription, campaignState);
        this.videoPath = videoPath;
    }

    public String getVideoPath() {
        return videoPath;
    }    
    
    
    public void setCampaignState(){
        //campaignState = 1;
    }
}
