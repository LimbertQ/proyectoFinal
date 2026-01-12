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

    private static String nextMenu;
    private static String nextMenuID;
    private static ProgressionManager instance;
    //public static boolean display = false;
    public static ProgressionManager getInstance() {
        if (instance == null) {
            instance = new ProgressionManager();
        }
        return instance;
    }

    public ProgressionManager() {
        nextMenu = "";
        nextMenuID = "";
        if (Assets.campaigns.get("CAMP01").getMissionByID("MSN01").getState() == 0) {
            nextMenu = "mediaPlayerCard";
            nextMenuID = "CAMP01";
        }
    }

    public void unlockInit() {
        if (nextMenuID.equals("CAMP01")) {
            unlocksMission(Assets.campaigns.get("CAMP01"), "MSN01");
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

    private void unlocksCampaign(Campaign campaign) {
        //DESBLOQUEAMOS CAMPANIA
        if (campaign.getState() == 0) {
            campaign.setState();
            DataBaseManager.getInstance("").updateCampaignState(campaign.getID());
        }
    }

    private void unlocksMission(Campaign campaign, String missionID) {
        //DESBLOQUEAMOS MISSION
        if (campaign.getMissionByID(missionID).getState() == 0) {
            campaign.getMissionByID(missionID).setState();
            DataBaseManager.getInstance("").updateMissionState(missionID);
            unlocksShip();
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

    public String nextMenu() {
        return nextMenu;
    }

    public String nextMenuID() {
        return nextMenuID;
    }

    public String nextPanel() {
        String res = nextMenuID;
        if (res.equals("loadingGameCard")) {
            res = "missionsMenuCard";
        }
        return res;
    }

    public String getMissionsForCampaign() {
        String res = "missionsMenuCard";
        if (nextMenu.equals("mediaPlayerCard")) {
            res = nextMenu;
        }
        return res;
    }

    public String currentCampaignID() {
        String res = MissionStats.campaignID;
        if (nextMenu.equals("mediaPlayerCard")) {
            res = nextMenuID;
        }
        return res;
    }

    public void reset() {
        nextMenu = "";
    }

    //gane una partida
    public void unlocks() {
        nextMenuID = nextID(MissionStats.missionID);
        if (Assets.campaigns.get(MissionStats.campaignID).containsMission(nextMenuID)) {
            //VERIFICAMOS Y DESBLOQUEAMOS SI SE PUEDE LA MISION Y LA NAVE
            unlocksMission(Assets.campaigns.get(MissionStats.campaignID), nextMenuID);
            //------------------------------------
            //VENTANA CARGA DE RECURSOS
            nextMenu = "loadingGameCard";
        } else {
            String nextCampaignID = nextID(MissionStats.campaignID);
            Campaign nextCampaign = Assets.campaigns.get(nextCampaignID);
            nextMenu = "missionsMenuCard";
            if (nextCampaign != null && nextCampaign.containsMission(nextMenuID)) {
                if (nextCampaign.getState() == 0) {
                    //DESBLOQUEAMOS CAMPAÑA
                    unlocksCampaign(nextCampaign);
                    //DESBLOQUEAMOS MISSION Y LA NAVE
                    unlocksMission(nextCampaign, nextMenuID);
                    //DESBLOQUEAMOS CIVILIZACIONES
                    unlocksCivilization();

                    //reproducimos el video de la siguiente campaña
                    nextMenu = "mediaPlayerCard";
                }
                nextMenuID = nextCampaignID;
            } else {
                //DESBLOQUEAMOS TODAS LAS NAVES
                unlocksShip();
                MissionStats.nextShipID = "CRS01";
                unlocksShip();
                MissionStats.nextShipID = "CRS02";
                unlocksShip();
                unlocksCivilization();
                //menu actual
                nextMenuID = MissionStats.campaignID;
            }
        }
        //campaigns.get(MissionStats.campaignID).unlocks(MissionStats.missionID)
    }
}
