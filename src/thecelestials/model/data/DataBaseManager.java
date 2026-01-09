/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
            conn = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
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
    
    public int[] readProgress(){
        String sql = "SELECT * FROM GameProgress gp;";
        int[] progress = new int[2];
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                progress[0] = rs.getInt("lives");
                progress[1] = rs.getInt("coins");
            }
        } catch (SQLException e) {
            System.err.println("Error al leer progreso del jugador: " + e.getMessage());
        }
        return progress;
    }
    
    public void updateProgress(int lives, int coins){
        String sql = "UPDATE GameProgress SET lives = ?, coins = ? WHERE gameProgressID = 'GMP01';";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, lives);
            pstmt.setInt(2, coins);
            pstmt.executeUpdate();
            
            //System.out.println("Estado de misión '" + missionId + "' actualizado a '" + newState + "'.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar progreso del jugador" + e.getMessage());
        }
    }
    
    public List<ShipStats> readAvailableShips() {
        List<ShipStats> navesData = new ArrayList<>();
        //ShipStats ship = null;
        String sql = "SELECT * FROM Ship s WHERE s.shipState = 1;";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ShipStats ship = new ShipStats(rs.getString("shipID"), rs.getString("shipClass"), rs.getString("shipName"), rs.getString("shipDescription"), rs.getString("profileShipPath"), rs.getInt("shipState"), rs.getString("shipAssetPath"), rs.getInt("shipHealth"), rs.getInt("shipDamage"), rs.getString("laserID"), rs.getString("civilizationID"), 1);
                ship.setEntityStats(readLaserByID(ship.getEntityStatsID()));
                navesData.add(ship);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer naves disponibles : " + e.getMessage());
        }
        return navesData;
    }
    
    public EntityStats readLaserByID(String ID){
        String sql = "SELECT * FROM Laser la WHERE la.laserID = '"+ID+"';";
        EntityStats entity = null;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                entity = new EntityStats(rs.getString("laserID"), rs.getString("laserName"), null, rs.getString("laserAssetPath"), 1, rs.getString("laserAssetPath"), 1, (int)rs.getInt("laserDamage"));
                
            }
        } catch (SQLException e) {
            System.err.println("Error al leer lasers disponibles : " + e.getMessage());
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
            System.err.println("Error al leer naves disponibles : " + e.getMessage());
        }
        return navesData;
    }
    
    public List<AssetDefinition> readAvailableCivilization() {
        List<AssetDefinition> list = new ArrayList<>();
        String sql = "SELECT * FROM Civilization c;";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AssetDefinition civilization = new AssetDefinition(rs.getString("civilizationID"),
                        rs.getString("civilizationName"), rs.getString("civilizationDescription"), rs.getString("civilizationPath"), rs.getInt("civilizationState"));
                
                //team = 1;
                list.add(civilization);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer civilizaciones disponibles: " + e.getMessage());
        }
        return list;
    }
    
    public List<Map<String, String>> readSounds(){
        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT * FROM ActiveSound a WHERE a.audioFileFormat = 'wav';";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, String> naveMap = new HashMap<>();
                naveMap.put("soundName", rs.getString("soundName"));
                naveMap.put("soundPath", rs.getString("soundPath"));
                
                //team = 1;
                list.add(naveMap);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer los sonidos wav: " + e.getMessage());
        }
        return list;
    }
    
    public List<Map<String, String>> readSoundsMedia(){
        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT * FROM ActiveSound a WHERE a.audioFileFormat = 'mp3';";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, String> naveMap = new HashMap<>();
                naveMap.put("soundName", rs.getString("soundName"));
                naveMap.put("soundPath", rs.getString("soundPath"));
                
                list.add(naveMap);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer los sonidos: " + e.getMessage());
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
            System.err.println("Error al leer imagenes disponibles : " + e.getMessage());
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
            System.err.println("Error al leer imagenes disponibles : " + e.getMessage());
        }
        return list;
    }
    
    public List<AssetDefinition> readStarsByMission(String missionID){
        //List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT s.starID, s.starName, s.starAssetPath, sc.starClassName FROM "
                + "Star s INNER JOIN StarClass sc ON s.starClassID = sc.starClassID INNER JOIN"
                + " MissionHasStarClass mhsc ON sc.starClassID = mhsc.starClassID WHERE mhsc.missionID = '"+missionID+"';";

        List<AssetDefinition> starList = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                //Map<String, String> naveMap = new HashMap<>();
                //naveMap.put("starName", rs.getString("starName"));
                //naveMap.put("starAssetPath", rs.getString("starAssetPath"));
                
                AssetDefinition star = new AssetDefinition(rs.getString("starID"), rs.getString("starName"), rs.getString("starClassName"), rs.getString("starAssetPath"), 1);
                starList.add(star);
                //team = 1;
                //list.add(naveMap);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer fenomenos astronomicos por mision: " + e.getMessage());
        }
        return starList;
    }
    
    public Map<String, Campaign> readCampaigns(){
        Map<String, Campaign> list = new LinkedHashMap<>();
        String sql = "SELECT * FROM Campaign c;";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Campaign campaign = new Campaign(rs.getString("campaignID"), rs.getString("campaignName"), rs.getString("campaignDescription"), rs.getString("videoPath"), rs.getString("mapPath"), rs.getInt("campaignState"), readMissionsByCampaign(rs.getString("campaignID")));
                list.put(campaign.getID(), campaign);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer campanias disponibles: " + e.getMessage());
        }
        return list;
    }
    
    public Map<String, AssetDefinition> readMissionsByCampaign(String campaignID){
        Map<String, AssetDefinition> list = new LinkedHashMap<>();
        String sql = "SELECT * FROM Mission m WHERE m.campaignID = '"+campaignID+"';";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AssetDefinition mission = new AssetDefinition(rs.getString("missionID"), rs.getString("missionName"), rs.getString("missionDescription"), rs.getString("missionMapPath"), rs.getInt("missionState"));
                list.put(mission.getID(), mission);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer las misiones por campanias: " + e.getMessage());
        }
        return list;
    }
    
    public void readMissionsByID(String missionID){
        String sql = "SELECT * FROM Mission m WHERE m.missionID = '"+missionID+"';";
        //Mission mission = null;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                //mission = new Mission(rs.getString("missionID"), rs.getString("missionName"), rs.getString("missionMapPath"), rs.getString("missionDescription"), rs.getString("voiceStartPath"), rs.getString("voiceEndPath"), rs.getString("challenge"), rs.getInt("assaults"), rs.getInt("reinforcement"), rs.getInt("missionState"), rs.getString("campaignID"));
                //list.put(mission.getID(), mission);
                Map<String, String> paths = new HashMap<>();
                paths.put("voiceStartPath", rs.getString("voiceStartPath"));
                paths.put("voiceEndPath", rs.getString("voiceEndPath"));
                paths.put("missionMapPath", rs.getString("missionMapPath"));
                MissionStats.setMissionStats(rs.getString("missionID"), rs.getString("missionName"), rs.getString("missionDescription"), rs.getString("missionMapPath"), rs.getString("challenge"), (byte)rs.getInt("assaults"), (byte)rs.getInt("reinforcement"), readShipsByMission(missionID), paths, readStarsByMission(missionID), readIDShipUnlock(), rs.getString("campaignID"));
            }
        } catch (SQLException e) {
            System.err.println("Error al leer el juego: " + e.getMessage());
        }
    }
    
    public void updateMissionState(String missionID){
        String sql = "UPDATE Mission SET missionState = 1 WHERE missionID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            System.out.println(missionID);
            pstmt.setString(1, missionID);
            pstmt.executeUpdate();
            
            //System.out.println("Estado de misión '" + missionId + "' actualizado a '" + newState + "'.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar mision" + e.getMessage());
        }
    }
    
    public void updateShipState(String shipID){
        String sql = "UPDATE Ship SET shipState = 1 WHERE shipID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //System.out.println(missionID);
            pstmt.setString(1, shipID);
            pstmt.executeUpdate();
            
            //System.out.println("Estado de misión '" + missionId + "' actualizado a '" + newState + "'.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar mision" + e.getMessage());
        }
    }
    
    public void updateCivilizationState(String civID){
        String sql = "UPDATE Civilization SET civilizationState = 1 WHERE civilizationID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //System.out.println(missionID);
            pstmt.setString(1, civID);
            pstmt.executeUpdate();
            
            //System.out.println("Estado de misión '" + missionId + "' actualizado a '" + newState + "'.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar mision" + e.getMessage());
        }
    }
    
    public String readIDShipUnlock(){
        String sql = "SELECT s.shipID FROM Ship s WHERE s.shipClass != 'crucero' AND s.shipState = 0 ORDER BY s.civilizationID ASC LIMIT 1;";
        String shipID = null;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                shipID = rs.getString("shipID");
            }
        } catch (SQLException e) {
            System.err.println("Error al leer el juego: " + e.getMessage());
        }
        return shipID;
    }
    
    public void updateCampaignState(String campaignID){
        String sql = "UPDATE Campaign SET campaignState = 1 WHERE campaignID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, campaignID);
            pstmt.executeUpdate();
            //System.out.println("Estado de misión '" + missionId + "' actualizado a '" + newState + "'.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar campania" + e.getMessage());
        }
    }
    
    public List<ShipStats>[] readShipsByMission(String missionID){
        String sql = "SELECT s.shipID, s.shipClass, s.shipName, s.profileShipPath, s.shipAssetPath, s.shipHealth, s.shipDamage, s.shipDescription, s.shipState, s.laserID, s.civilizationID, mhs.team FROM Ship s INNER JOIN MissionHasShip mhs ON s.shipID = mhs.shipID WHERE mhs.missionID = '"+missionID+"';";
        List<ShipStats> allies = new ArrayList<>();
        List<ShipStats> axis = new ArrayList<>();
        List<ShipStats> cruisers = new ArrayList<>();
        List<ShipStats>[] shipList = new List[3];
        shipList[0] = axis;
        shipList[1] = allies;
        shipList[2] = cruisers;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ShipStats ship = new ShipStats(rs.getString("shipID"), rs.getString("shipClass"), rs.getString("shipName"), rs.getString("shipDescription"), rs.getString("profileShipPath"), rs.getInt("shipState"), rs.getString("shipAssetPath"), rs.getInt("shipHealth"), rs.getInt("shipDamage"), rs.getString("laserID"), rs.getString("civilizationID"), rs.getInt("team"));
                
                ship.setEntityStats(readLaserByID(ship.getEntityStatsID()));
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
