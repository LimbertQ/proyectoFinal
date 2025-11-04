/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.gameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import thecelestials.model.data.Assets;
import thecelestials.model.data.EntityStats;
import thecelestials.model.data.ShipStats;
import thecelestials.model.managers.GameObjectCreator;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public abstract class Ship extends MovingObject {

    private final GameObjectCreator creator;
    protected final EntityStats bullet;
    private final ShipStats shipStats;
    protected boolean accelerating = false;
    private long special = 0;
    private final int team;
    private final Color color;

    public Ship(Vector2D position, ShipStats shipStats, Vector2D velocity, double maxVel, GameObjectCreator creator, long shipFireRate) {
        super(position, shipStats, velocity, maxVel);
        this.bullet = shipStats.getEntityStats();
        this.creator = creator;
        this.team = shipStats.getTeam();
        this.shipStats = shipStats;
        if (this instanceof PlayerShip) {
            color = Color.GREEN;
        } else {
            if (team == 1) {
                color = Color.BLUE;
            } else {
                color = Color.RED;
            }
        }
    }

    protected void updateValuesShip(float dt) {
        special += dt;
    }

    //LASER FUERTE
    //CLONAR
    //CLONAR VARIOS
    protected void specialTechnique(Vector2D position, Vector2D direction, float dt) {
        if (special > Constants.UFO_CLONE_RATE) {
            special = 0;
            switch (shipStats.getShipClass()) {
                case "caza" ->
                    shootLaserBig(position, direction);
                case "ufo" ->
                    creator.cloneShip(position, team);
                default -> {
                    healt += 50;
                    shootLaserBig(position, direction);
                    creator.cloneShip(getPosition(), team);
                    creator.cloneShip(getPosition(), team);
                }
            }
        }
    }

    private void shootLaserBig(Vector2D position, Vector2D direction) {
        Laser laser = new Laser(
                position.add(direction.scale(width)),
                Assets.powerBullet,
                direction,
                Constants.LASER_VEL,
                angle);
        creator.createGameObject(laser);
    }

    protected void shooti(Vector2D positione, Vector2D direction) {
        if (special == 0) {
            return;
        }
        Laser laser = new Laser(
                positione.add(direction.scale(width)),
                bullet,
                direction,
                Constants.LASER_VEL,
                angle);
        creator.createGameObject(laser);
    }

    public int getTeam() {
        return team;
    }

    protected void drawSpeed(Graphics2D g2d) {
        if (accelerating) {
            AffineTransform right = AffineTransform.getTranslateInstance(position.getX() + width * 0.6,
                    position.getY() + height / 2 + 10);

            AffineTransform left = AffineTransform.getTranslateInstance(position.getX() + width - (width * 0.6 + 16),
                    position.getY() + height / 2 + 10);
            right.rotate(angle, width * -0.1, -10);
            left.rotate(angle, width * 0.5 - (width - (width * 0.6 + 16)), -10);
            g2d.drawImage(Assets.effect, right, null);
            g2d.drawImage(Assets.effect, left, null);
        }
    }

    protected void drawRectangle(Graphics2D g2d) {
        // 1. **GUARDAR** el estado original de Graphics2D. ¡Crucial!
        AffineTransform originalTransform = g2d.getTransform();
        
        // 2. **DEFINIR la Transformación** para el rectángulo:
        // A. Traslación: Mueve el origen a la posición (x, y) de la nave.
        // B. Rotación: Rota alrededor del centro de la nave (si la nave rota).
        // Primero, nos movemos a la posición de la nave:
        g2d.translate(position.getX(), position.getY());

        // Luego, rotamos (si la nave rota):
        // La rotación debe ser alrededor del centro de tu nave. 
        // Asumiré que el centro es (width/2, height/2) de tu área de dibujo.
        g2d.rotate(angle, width / 2, height / 2);

        // 3. **DIBUJAR** el rectángulo:
        // Define el color del rectángulo (ej: rojo para indicar el bando o un color de sombra)
        g2d.setColor(color);

        // Definición de la posición RELATIVA del rectángulo (para que esté debajo de la nave):
        // X = 0 (centrado en la nave)
        // Y = height + 5 (un poco debajo de la nave)
        // Ancho = width (el ancho de la nave)
        // Alto = 10 (la altura del rectángulo)
        int rectX = 0; // Se dibuja desde el borde izquierdo del área de la nave
        int rectY = height + 5; // Se dibuja 5px debajo del borde inferior de la nave
        int rectWidth = width;
        int rectHeight = 10;

        g2d.fillRect(rectX, rectY, rectWidth, rectHeight);

        // 4. **RESTAURAR** el estado original de Graphics2D.
        // Esto asegura que los dibujos posteriores (como la nave misma) no hereden esta rotación y traslación.
        g2d.setTransform(originalTransform);
    }
}
