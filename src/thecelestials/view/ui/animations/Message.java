/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui.animations;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public class Message {
    private float alpha;
    private final String text;
    private Vector2D position;
    private final Color color;
    private boolean center;
    private boolean fade;
    private final Font font;
    private final float deltaAlpha;
    private boolean dead;
    private final float dis;

    public Message(Vector2D position, boolean fade, String text, Color color,
    boolean center, Font font, float dis) {
        this.font = font;
        this.text = text;
        this.position = new Vector2D(position);
        this.fade = fade;
        this.color = color;
        this.center = center;
        this.dead = false;
        this.dis = dis;
        if(dis == 0)
            deltaAlpha = 0.004f;
        else
            deltaAlpha = 0.01f;

        if(fade)
            alpha = 1;
        else
            alpha = 0;

    }

    public void draw(Graphics2D g2d) {

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        Text.drawText(g2d, text, position, center, color, font);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        position.setY(position.getY() - dis);

        if(fade)
            alpha -= deltaAlpha;
        else
            alpha += deltaAlpha;

        if(fade && alpha < 0) {
            dead = true;
        }

        if(!fade && alpha > 1) {
            fade = true;
            alpha = 1;
        }

    }

    public boolean isDead() {return dead;}

}
