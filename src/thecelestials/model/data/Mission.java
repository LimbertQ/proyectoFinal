/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

/**
 *
 * @author pc
 */
public class Mission extends AssetDefinition{
    //private final String missionID;
    //private final String missionName;
    //private final String missionMapPath;
    private final String voiceStartPath;
    private final String voiceEndPath;
    //private final String missionDescription;
    private final String challenge;
    private final int assaults;
    private final int reinforcement;    
    private final String campaignID;    

    public Mission(String missionID, String missionName, String missionMapPath, String missionDescription, String voiceStartPath, String voiceEndPath, String challenge, int assaults, int reinforcement, int missionState, String campaignID) {
        super(missionID, missionName, missionDescription, missionMapPath, missionState);
        //this.missionID = missionID;
        //this.missionName = missionName;
        //this.missionMapPath = missionMapPath;
        this.voiceStartPath = voiceStartPath;
        this.voiceEndPath = voiceEndPath;
        //this.missionDescription = missionDescription;
        this.challenge = challenge;
        this.assaults = assaults;
        this.reinforcement = reinforcement;
        this.campaignID = campaignID;
    }

    public String getVoiceStartPath() {
        return voiceStartPath;
    }

    public String getVoiceEndPath() {
        return voiceEndPath;
    }

    public String getChallenge() {
        return challenge;
    }

    public int getAssaults() {
        return assaults;
    }

    public int getReinforcement() {
        return reinforcement;
    }

    public String getCampaignID() {
        return campaignID;
    }
    
}
