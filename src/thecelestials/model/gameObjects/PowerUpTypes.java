/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package thecelestials.model.gameObjects;

/**
 *
 * @author pc
 */
public enum PowerUpTypes {
    LIFE("+1 LIFE", "life"),
    SCORE_X2("SCORE x2", "doubleScore"),
    FASTER_FIRE("FAST FIRE", "fastFire"),
    SCORE_STACK("+1000 SCORE", "star"),
    DOUBLE_GUN("DOUBLE GUN", "doubleGun"),
    SHIELD("SHIELD", "shield");

    public String type;
    public String textureKey;

    private PowerUpTypes(String text, String texture){
        this.type = text;
        this.textureKey = texture;
    }
}
