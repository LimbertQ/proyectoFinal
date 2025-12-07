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
    private String nextID(String ID){
        int length = ID.length();
        int digit = Integer.parseInt(ID.substring(length - 2, length)) + 1;
        if (digit < 10) {
            ID = ID.substring(0, length - 1) + digit;
        } else {
            ID = ID.substring(0, length - 2) + digit;
        }
        return ID;
    }

    private void unlocks(String campaignID, String missionID) {
        //DESBLOQUEAMOS MISSION
        Assets.campaigns.get(missionID).setState();
        DataBaseManager.getInstance("").updateMissionState(missionID);
        //DESBLOQUEAMOS NAVE
        String shipID = DataBaseManager.getInstance("").readIDShipUnlock();
        if (shipID != null) {
            DataBaseManager.getInstance("").updateShipState(shipID);
            //Assets.loadShipsAvaible();
        }
    }

    public void unlocks() {
        String missionID = nextID(MissionStats.missionID); 
        if (Assets.campaigns.get(MissionStats.campaignID).containsMission(missionID)) {
            unlocks(MissionStats.campaignID, missionID);
        } else {
            String nextID = nextID(MissionStats.campaignID);
            if (Assets.campaigns.get(nextID).containsMission(missionID)){
                unlocks(nextID, missionID);
                //DESBLOQUEAMOS CIVILIZACIONES puede ser load
                //Assets.informations.get("civilizaciones");
                Assets.unlock = true;
            }else{
                //DESBLOQUEAMOS TODAS LAS NAVES
            }
        }
        //campaigns.get(MissionStats.campaignID).unlocks(MissionStats.missionID)
    }
}
