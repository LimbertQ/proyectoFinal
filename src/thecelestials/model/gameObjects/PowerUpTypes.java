/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package thecelestials.model.gameObjects;

import java.awt.Color;

/**
 *
 * @author pc
 */
public enum PowerUpTypes {
    LIFE("+1 LIFE", "life", Color.GREEN),
    SCORE_X2("SCORE x2", "doubleScore", Color.YELLOW),
    FASTER_FIRE("FAST FIRE", "fastFire", Color.BLUE),
    SCORE_STACK("+1000 SCORE", "star", Color.MAGENTA),
    DOUBLE_GUN("DOUBLE GUN", "doubleGun", Color.ORANGE),
    SHIELD("SHIELD", "shield", Color.DARK_GRAY);

    public String type;
    public String textureKey;
    public Color colorPowerUp;

    private PowerUpTypes(String text, String texture, Color color){
        this.type = text;
        this.textureKey = texture;
        this.colorPowerUp = color;
    }
}
