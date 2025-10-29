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
public class Campaign extends GameEntity {

    private final String videoPath;
    private final Map<String, Mission> missions;

    public Campaign(String campaignID, String campaignName, String mapPath, String videoPath, String campaignDescription, int campaignState, Map<String, Mission> missions) {
        super(campaignID, campaignName, mapPath, campaignDescription, campaignState);
        this.videoPath = videoPath;
        this.missions = missions;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String nextMission(String missionID) {
        //boolean flag = false;
        int length = missionID.length();
        int digit = Integer.parseInt(missionID.substring(length - 2, length)) + 1;
        if (digit < 10) {
            missionID = missionID.substring(0, length - 1) + digit;
        } else {
            missionID = missionID.substring(0, length - 2) + digit;
        }
        
        return missionID;
    }

    public boolean unlocks(String missionID) {
        boolean flag = false;
        int length = missionID.length();
        int digit = Integer.parseInt(missionID.substring(length - 2, length)) + 1;
        if (digit < 10) {
            missionID = missionID.substring(0, length - 1) + digit;
        } else {
            missionID = missionID.substring(0, length - 2) + digit;
        }

        if (missions.containsKey(missionID)) {
            if (missions.get(missionID).getState() == 0) {
                missions.get(missionID).setState();
                DataBaseManager.getInstance("").updateMissionState(missionID);
                String shipID = DataBaseManager.getInstance("").readIDShipUnlock();
                if (shipID != null) {
                    DataBaseManager.getInstance("").updateShipState(shipID);
                }
            }
            flag = true;
        }
        if (getState() == 0) {
            setState();
            DataBaseManager.getInstance("").updateCampaignState(getID());
        }
        return flag;
    }
}
