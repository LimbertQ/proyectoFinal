/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

import java.awt.image.BufferedImage;

/**
 *
 * @author pc
 */
public class AssetDefinition {
    private final String id;
    private final String name;
    private final String profileImagePath;
    private final String description; // Esta es la clave para la reutilización
    public AssetDefinition(String id, String name, String description, String profileImagePath){
        this.id = id;
        this.name = name;
        this.description = description;
        this.profileImagePath = profileImagePath;
    }
    
    public String getID(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public String getDescription(){
        return description;
    }
    
    public BufferedImage getProfile(){
        return Assets.images.get(name);
    }
    
    public String getProfileImagePath(){
        return profileImagePath;
    }
}
