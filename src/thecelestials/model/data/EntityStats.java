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
public class EntityStats extends AssetDefinition{
    //LASER
    private final int health;
    private final int damage;
    private final String spriteKey;
    private final String spritePath;
    public EntityStats(String id, String name, String description, String profileImagePath, int state, String spritePath, int health, int damage) {
        super(id, name, description, profileImagePath, state);
        this.health = health;
        this.damage = damage;
        this.spriteKey = "sprite"+name;
        this.spritePath = spritePath;
    }
    
    public int getHealth(){
        return health;
    }
    
    public int getDamage(){
        return damage;
    }
    
    public String getSpriteKey(){
        return spriteKey;
    }
    
    public String getSpritePath(){
        return spritePath;
    }
    
    public BufferedImage getSprite(){
        //return Assets.images.get(spriteKey);
        return Assets.getImage(spriteKey);
    }
}