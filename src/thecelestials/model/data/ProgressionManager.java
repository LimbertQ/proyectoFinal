/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

import static thecelestials.model.data.Assets.campaigns;

/**
 *
 * @author pc
 */
public class ProgressionManager {
    private static ProgressionManager instance;

    public static ProgressionManager getInstance(){
        if (instance == null) {
            instance = new ProgressionManager();
        }
        return instance;
    }

    private String nextID(String ID) {
        int length = ID.length();
        int digit = Integer.parseInt(ID.substring(length - 2, length)) + 1;
        if (digit < 10) {
            ID = ID.substring(0, length - 1) + digit;
        } else {
            ID = ID.substring(0, length - 2) + digit;
        }
        return ID;
    }

    private void unlocksMission(String missionID) {
        //DESBLOQUEAMOS MISSION
        if (Assets.campaigns.get(missionID).getState() == 0) {
            Assets.campaigns.get(missionID).setState();
            DataBaseManager.getInstance("").updateMissionState(missionID);
        }
    }
    
    private void unlocksShip(){
        //DESBLOQUEAMOS NAVE
        String shipID = MissionStats.nextShipID;
        if (shipID != null) {
            DataBaseManager.getInstance("").updateShipState(shipID);
            Assets.loadShipAvaible();
        }
    }
    
    public String nextMission(){
        return nextID(MissionStats.missionID);
    }
    
    public String nextCampaign(){
        return nextID(MissionStats.campaignID);
    }

    public void unlocks() {
        String missionID = nextID(MissionStats.missionID);
        if (Assets.campaigns.get(MissionStats.campaignID).containsMission(missionID)) {
            unlocksMission(missionID);
            unlocksShip();
        } else {
            String nextID = nextID(MissionStats.campaignID);
            if (Assets.campaigns.get(nextID).getState() == 0 && Assets.campaigns.get(nextID).containsMission(missionID)) {
                //DESBLOQUEAMOS CAMPAÑA
                Assets.campaigns.get(nextID).setState();
                DataBaseManager.getInstance("").updateCampaignState(nextID);
                //DESBLOQUEAMOS MISSION
                unlocksMission(missionID);
                //DESBLOQUEAMOS NAVE
                unlocksShip();
                //DESBLOQUEAMOS CIVILIZACIONES puede ser load
                //Assets.informations.get("civilizaciones");
                Assets.unlock = true;
            } else {
                //DESBLOQUEAMOS TODAS LAS NAVES
                unlocksShip();
                MissionStats.nextShipID = nextID(MissionStats.nextShipID);
                unlocksShip();
            }
        }
        //campaigns.get(MissionStats.campaignID).unlocks(MissionStats.missionID)
    }
}
