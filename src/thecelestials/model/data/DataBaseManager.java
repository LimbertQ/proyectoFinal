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
    
    public List<Map<String, Object>> readAvailableShips() {
        List<Map<String, Object>> navesData = new ArrayList<>();
        String sql = "SELECT * FROM Ship s;";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> naveMap = new HashMap<>();
                naveMap.put("shipID", rs.getString("shipID"));
                naveMap.put("shipClass", rs.getString("shipClass"));
                naveMap.put("shipName", rs.getString("shipName"));
                naveMap.put("profileShipPath", rs.getString("profileShipPath")); // <-- URL de la imagen de perfil
                naveMap.put("shipAssetPath", rs.getString("shipAssetPath"));     // <-- URL de la imagen de la nave
                naveMap.put("shipHealth", rs.getInt("shipHealth"));
                naveMap.put("shipDamage", rs.getInt("shipDamage"));
                naveMap.put("shipDescription", rs.getString("shipDescription"));
                naveMap.put("shipState", rs.getInt("shipState"));
                naveMap.put("laserID", rs.getString("laserID"));
                naveMap.put("civilizationID", rs.getString("civilizationID"));
                
                //team = 1;
                navesData.add(naveMap);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer naves disponibles tmr: " + e.getMessage());
        }
        return navesData;
    }
    
    public Map<String, Object> readLaserByID(String ID){
        Map<String, Object> laser = new HashMap<>();
        String sql = "SELECT * FROM Laser la WHERE la.laserID = '"+ID+"';";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                laser.put("laserID", rs.getString("laserID"));
                laser.put("laserName", rs.getString("laserName"));
                laser.put("laserAssetPath", rs.getString("laserAssetPath"));     // <-- URL de la imagen de la nave
                laser.put("laserDamage", (int)rs.getInt("laserDamage"));
                
            }
        } catch (SQLException e) {
            System.err.println("Error al leer naves disponibles tmr: " + e.getMessage());
        }
        return laser;
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
    
    public List<Map<String, String>> readAvailableCivilization() {
        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT * FROM Civilization c;";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, String> naveMap = new HashMap<>();
                naveMap.put("civilizationID", rs.getString("civilizationID"));
                naveMap.put("civilizationName", rs.getString("civilizationName"));
                naveMap.put("civilizationPath", rs.getString("civilizationPath"));     // <-- URL de la imagen de la nave
                naveMap.put("civilizationDescription", rs.getString("civilizationDescription"));
                
                //team = 1;
                list.add(naveMap);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer naves disponibles tmr: " + e.getMessage());
        }
        return list;
    }
}
