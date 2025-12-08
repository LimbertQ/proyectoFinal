/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

/**
 *
 * @author pc
 */
public class ProgressionManager {
    private static byte unlock = 0;
    private static ProgressionManager instance;

    public static ProgressionManager getInstance() {
        if (instance == null) {
            instance = new ProgressionManager();
        }
        return instance;
    }
    
    public ProgressionManager(){
        if(Assets.campaigns.get("CAMP01").getMissionByID("MSN01").getState() == 0){
            unlocksMission("CAMP01", "MSN01");
            unlock = 1;
        }
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
    
    public void changeUnlock(){
        unlock = 0;
    }
    
    public byte unlock(){
        return unlock;
    }
    
    private void unlocksCampaign(String campaignID) {
        //DESBLOQUEAMOS MISSION
        if (Assets.campaigns.get(campaignID).getState() == 0) {
            Assets.campaigns.get(campaignID).setState();
            DataBaseManager.getInstance("").updateCampaignState(campaignID);
        }
    }

    private void unlocksMission(String campaignID, String missionID) {
        //DESBLOQUEAMOS MISSION
        if (Assets.campaigns.get(campaignID).getMissionByID(missionID).getState() == 0) {
            Assets.campaigns.get(campaignID).getMissionByID(missionID).setState();
            DataBaseManager.getInstance("").updateMissionState(missionID);
        }
    }

    private void unlocksShip() {
        //DESBLOQUEAMOS NAVE
        String shipID = MissionStats.nextShipID;
        if (shipID != null) {
            DataBaseManager.getInstance("").updateShipState(shipID);
            Assets.loadShipAvaible();
        }
    }

    private void unlocksCivilization() {
        boolean flag = false;
        for (int i = 0; flag == false && i < Assets.informations.get("civilizaciones").size(); i++) {
            AssetDefinition civ = Assets.informations.get("civilizaciones").get(i);
            if (civ.getState() == 0) {
                civ.setState();
                DataBaseManager.getInstance("").updateCivilizationState(civ.getID());
                flag = true;
            }
        }
    }

    public String nextMission() {
        return nextID(MissionStats.missionID);
    }

    public String nextCampaign() {
        return nextID(MissionStats.campaignID);
    }

    public void unlocks() {
        String missionID = nextID(MissionStats.missionID);
        if (Assets.campaigns.get(MissionStats.campaignID).containsMission(missionID)) {
            unlocksMission(MissionStats.campaignID, missionID);
            unlocksShip();
        } else {
            String nextCampaignID = nextID(MissionStats.campaignID);
            if (Assets.campaigns.containsKey(nextCampaignID) && Assets.campaigns.get(nextCampaignID).getState() == 0 && Assets.campaigns.get(nextCampaignID).containsMission(missionID)) {
                //DESBLOQUEAMOS CAMPAÑA
                unlocksCampaign(nextCampaignID);
                //DESBLOQUEAMOS MISSION
                unlocksMission(nextCampaignID, missionID);
                //DESBLOQUEAMOS NAVE
                unlocksShip();
                //DESBLOQUEAMOS CIVILIZACIONES puede ser load
                unlocksCivilization();
                
                unlock = 1;
            } else if (!Assets.campaigns.containsKey(nextCampaignID) || Assets.campaigns.containsKey(nextCampaignID)){
                //DESBLOQUEAMOS TODAS LAS NAVES
                unlocksShip();
                MissionStats.nextShipID = "CRS01";
                unlocksShip();
                MissionStats.nextShipID = "CRS02";
                unlocksShip();
                unlock = -1;
                unlocksCivilization();
            }
        }
        //campaigns.get(MissionStats.campaignID).unlocks(MissionStats.missionID)
    }
}
