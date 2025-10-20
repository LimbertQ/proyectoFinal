/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pc
 */
public class DataBaseManager {
    private static DataBaseManager instance;
    private Connection conn;
    private final String dbPath;

    public DataBaseManager(String dbFilePath) {
        this.dbPath = dbFilePath;
    }

    public static DataBaseManager getInstance(String dbFilePath) {
        if (instance == null) {
            instance = new DataBaseManager(dbFilePath);
        }
        return instance;
    }
    
    public boolean openConnection() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            System.out.println("Conexión a SQLite establecida con " + dbPath);
            return true;
        } catch (SQLException e) {
            System.err.println("Error al interactuar con la base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo conectar a la base de datos: " + dbPath, e);
        }
    }
    
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexión a SQLite cerrada.");
            } catch (SQLException e) {
                throw new RuntimeException("No al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    
    public List<ShipStats> readAvailableShips() {
        List<ShipStats> navesData = new ArrayList<>();
        //ShipStats ship = null;
        String sql = "SELECT * FROM Ship s WHERE s.shipState = 1;";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ShipStats ship = new ShipStats(rs.getString("shipID"), rs.getString("shipClass"), rs.getString("shipName"), rs.getString("shipDescription"), rs.getString("profileShipPath"), rs.getString("shipAssetPath"), rs.getInt("shipHealth"), rs.getInt("shipDamage"), rs.getInt("shipState"), rs.getString("laserID"), rs.getString("civilizationID"), 1);
                ship.setEntityState(readLaserByID(ship.getEntityStatsID()));
                navesData.add(ship);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer naves disponibles tmr: " + e.getMessage());
        }
        return navesData;
    }
    
    public EntityStats readLaserByID(String ID){
        String sql = "SELECT * FROM Laser la WHERE la.laserID = '"+ID+"';";
        EntityStats entity = null;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                entity = new EntityStats(rs.getString("laserID"), rs.getString("laserName"), null, rs.getString("laserAssetPath"), rs.getString("laserAssetPath"), 1, (int)rs.getInt("laserDamage"));
                
            }
        } catch (SQLException e) {
            System.err.println("Error al leer naves disponibles tmr: " + e.getMessage());
        }
        return entity;
    }
    
    public Map<String, Object> readCVLByID(String ID){
        Map<String, Object> laser = new HashMap<>();
        String sql = "SELECT * FROM Civilization cvl WHERE cvl.civilizationID = '"+ID+"';";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                laser.put("civilizationID", rs.getString("civilizationID"));
                laser.put("civilizationName", rs.getString("civilizationName"));
                laser.put("civilizationPath", rs.getString("civilizationPath"));     // <-- URL de la imagen de la nave
                laser.put("civilizationDescription", rs.getString("civilizationDescription"));
            }
        } catch (SQLException e) {
            System.err.println("Error al leer civilizacion por ID: " + e.getMessage());
        }
        return laser;
    }
    
    public List<Map<String, Object>> readAvailableLaser() {
        List<Map<String, Object>> navesData = new ArrayList<>();
        String sql = "SELECT * FROM Laser s;";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> naveMap = new HashMap<>();
                naveMap.put("laserID", rs.getString("laserID"));
                naveMap.put("laserName", rs.getString("laserName"));
                naveMap.put("laserAssetPath", rs.getString("laserAssetPath"));     // <-- URL de la imagen de la nave
                naveMap.put("laserDamage", (int)rs.getInt("laserDamage"));
                
                //team = 1;
                navesData.add(naveMap);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer naves disponibles tmr: " + e.getMessage());
        }
        return navesData;
    }
    
    public List<AssetDefinition> readAvailableCivilization() {
        List<AssetDefinition> list = new ArrayList<>();
        String sql = "SELECT * FROM Civilization c;";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AssetDefinition civilization = new AssetDefinition(rs.getString("civilizationID"),
                        rs.getString("civilizationName"), rs.getString("civilizationPath"), rs.getString("civilizationDescription"));
                
                //team = 1;
                list.add(civilization);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer naves disponibles tmr: " + e.getMessage());
        }
        return list;
    }
    
    public List<Map<String, String>> readSounds(){
        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT * FROM ActiveSound a;";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, String> naveMap = new HashMap<>();
                naveMap.put("soundName", rs.getString("soundName"));
                naveMap.put("soundPath", rs.getString("soundPath"));
                
                //team = 1;
                list.add(naveMap);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer naves disponibles tmr: " + e.getMessage());
        }
        return list;
    }
    
    public List<Map<String, String>> readAllImages(){
        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT * FROM ActiveImage a;";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, String> naveMap = new HashMap<>();
                naveMap.put("imageName", rs.getString("imageName"));
                naveMap.put("imagePath", rs.getString("imagePath"));
                
                //team = 1;
                list.add(naveMap);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer imagenes disponibles tmr: " + e.getMessage());
        }
        return list;
    }
    
    public List<Map<String, String>> readStars(){
        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT * FROM Star s;";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, String> naveMap = new HashMap<>();
                naveMap.put("starName", rs.getString("starName"));
                naveMap.put("starAssetPath", rs.getString("starAssetPath"));
                
                //team = 1;
                list.add(naveMap);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer imagenes disponibles tmr: " + e.getMessage());
        }
        return list;
    }
    
    public Map<String, Campaign> readCampaigns(){
        Map<String, Campaign> list = new LinkedHashMap<>();
        String sql = "SELECT * FROM Campaign c;";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Campaign campaign = new Campaign(rs.getString("campaignID"), rs.getString("campaignName"), rs.getString("campaignDescription"), rs.getString("videoPath"), rs.getString("mapPath"), rs.getInt("campaignState"));
                list.put(campaign.getID(), campaign);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer campanias disponibles: " + e.getMessage());
        }
        return list;
    }
    
    public Map<String, Mission> readMissionsByCampaign(String campaignID){
        Map<String, Mission> list = new LinkedHashMap<>();
        String sql = "SELECT * FROM Mission m WHERE m.campaignID = '"+campaignID+"';";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Mission mission = new Mission(rs.getString("missionID"), rs.getString("missionName"), rs.getString("missionMapPath"), rs.getString("missionDescription"), rs.getString("voiceStartPath"), rs.getString("voiceEndPath"), rs.getString("challenge"), rs.getInt("assaults"), rs.getInt("reinforcement"), rs.getInt("missionState"), rs.getString("campaignID"));
                list.put(mission.getID(), mission);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer las misiones por campanias: " + e.getMessage());
        }
        return list;
    }
    
    public Mission readMissionsByID(String missionID){
        String sql = "SELECT * FROM Mission m WHERE m.missionID = '"+missionID+"';";
        Mission mission = null;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                mission = new Mission(rs.getString("missionID"), rs.getString("missionName"), rs.getString("missionMapPath"), rs.getString("missionDescription"), rs.getString("voiceStartPath"), rs.getString("voiceEndPath"), rs.getString("challenge"), rs.getInt("assaults"), rs.getInt("reinforcement"), rs.getInt("missionState"), rs.getString("campaignID"));
                //list.put(mission.getID(), mission);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer el juego: " + e.getMessage());
        }
        return mission;
    }
    
    public List<ShipStats>[] readShipsByMission(String missionID){
        String sql = "SELECT s.shipID, s.shipClass, s.shipName, s.profileShipPath, s.shipAssetPath, s.shipHealth, s.shipDamage, s.shipDescription, s.shipState, s.laserID, s.civilizationID, mhs.team FROM Ship s INNER JOIN MissionHasShip mhs ON s.shipID = mhs.shipID WHERE mhs.missionID = '"+missionID+"';";
        List<ShipStats> allies = new ArrayList<>();
        List<ShipStats> axis = new ArrayList<>();
        List<ShipStats> cruisers = new ArrayList<>();
        List<ShipStats>[] shipList = new List[3];
        shipList[0] = allies;
        shipList[1] = axis;
        shipList[2] = cruisers;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ShipStats ship = new ShipStats(rs.getString("shipID"), rs.getString("shipClass"), rs.getString("shipName"), rs.getString("shipDescription"), rs.getString("profileShipPath"), rs.getString("shipAssetPath"), rs.getInt("shipHealth"), rs.getInt("shipDamage"), rs.getInt("shipState"), rs.getString("laserID"), rs.getString("civilizationID"), rs.getInt("team"));
                
                ship.setEntityState(readLaserByID(ship.getEntityStatsID()));
                if(ship.getShipClass().equals("crucero")){
                    cruisers.add(ship);
                }else if(ship.getTeam() == 1){
                    allies.add(ship);
                }else{
                    axis.add(ship);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al leer las naves de la mision: " + e.getMessage());
        }
        return shipList;
    }
}
