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

        // Definimos la velocidad de la transparencia
        if (dis == 0) {
            deltaAlpha = 0.004f;
        } else {
            deltaAlpha = 0.01f;
        }

        // Definimos el alpha inicial
        if (fade) {
            alpha = 1f;
        } else {
            alpha = 0f;
        }
    }

    /**
     * Lógica de movimiento y transparencia. 
     * Se debe llamar desde el update del Manager.
     */
    public void update() {
        if (dead) return;

        // 1. Movimiento vertical hacia arriba
        position.setY(position.getY() - dis);

        // 2. Manejo de la transparencia (Fade out / Fade in)
        if (fade) {
            alpha -= deltaAlpha;
            // Si el alpha baja de 0, lo frenamos en 0 y matamos el mensaje
            if (alpha <= 0) {
                alpha = 0f; 
                dead = true;
            }
        } else {
            alpha += deltaAlpha;
            // Si el alpha sube de 1, lo frenamos en 1 y empezamos el fade out
            if (alpha >= 1) {
                alpha = 1f;
                fade = true;
            }
        }
    }

    /**
     * Solo dibujo. No calcula nada, solo usa el alpha actual.
     */
    public void draw(Graphics2D g2d) {
        // Establecemos la transparencia actual
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // Dibujamos el texto
        Text.drawText(g2d, text, position, center, color, font);

        // IMPORTANTE: Resetear el composite a 1 para que el resto del juego 
        // no se dibuje transparente.
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public boolean isDead() {
        return dead;
    }
}
