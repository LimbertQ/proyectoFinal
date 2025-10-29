/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import thecelestials.model.data.ShipStats;
import thecelestials.model.managers.GameObjectCreator;
import thecelestials.model.managers.TargetProvider;
import thecelestials.model.math.Constants;
import thecelestials.model.math.Vector2D;

/**
 *
 * @author pc
 */
public class NPCShip extends Ship {

    private Ship target = null;
    private final TargetProvider provider;
    private long contMoves = 0;
    private int currentPattern = 0;
    private final Vector2D centerBattle;
    private final Vector2D goal;
    private long fireRate = 0;

    public NPCShip(Vector2D position, ShipStats shipStats, Vector2D velocity, double maxVel, GameObjectCreator creator, BufferedImage effect, int team, TargetProvider provider) {
        super(position, shipStats, velocity, maxVel, creator, effect, team, Constants.UFO_FIRE_RATE);
        goal = new Vector2D();
        centerBattle = new Vector2D(Constants.WIDTH / 2.0, Constants.HEIGHT / 2.0);
        this.provider = provider;
    }

    private void searchTarget() {
        target = provider.getTarget(getTeam());
    }

    public Vector2D targetDirection(Vector2D targetPos, Vector2D center) {
        return new Vector2D(targetPos.subtract(center).normalize());
    }

    private void toTarget(Vector2D direction) {
        angle = direction.getAngle();
        if (direction.getX() < 0) {
            angle = -angle + Math.PI;
        }
        angle += Math.PI / 2;
    }

    private void shoot(Vector2D direction) {
        specialTechnique(getCenter(), direction, 1000f);
        if (fireRate > Constants.UFO_FIRE_RATE) {
            shooti(getCenter(), direction);
            fireRate = 0;
        }

    }

    private void flankAttack(Vector2D targetPos, Vector2D center, double distance, float dt, int leftRight) {
        if (distance > 200) {
            frontalAttack(targetPos, center, distance, dt);
        } else {
            Vector2D jugadorANave = center.subtract(targetPos);
            Vector2D lateral = new Vector2D(-jugadorANave.getY() * leftRight, jugadorANave.getX() * leftRight).normalize();
            Vector2D direccionDeseada;
            if (distance < 100) {
                direccionDeseada = jugadorANave.add(lateral).normalize();
            } else {
                direccionDeseada = lateral;
            }

            velocity = direccionDeseada.scale(maxVel * 0.05f);
            position = position.add(velocity.scale(dt));
            Vector2D objetivo = targetDirection(targetPos, center);
            toTarget(objetivo);
            accelerating = true;

            shoot(objetivo);
        }
    }

    public void frontalAttack(Vector2D targetPos, Vector2D center, double distancia, float dt) {
        Vector2D targetDirection = targetDirection(targetPos, center);
        toTarget(targetDirection);

        shoot(targetDirection);
        if (distancia > 100) {
            velocity = targetDirection.scale(maxVel * 0.05f);
            position = position.add(velocity.scale(dt));
            accelerating = true;
        }
    }

    private void defensive(Vector2D targetPos, Vector2D center, float dt) {
        Vector2D targetDirection = targetDirection(targetPos, center);
        toTarget(targetDirection);
        shoot(targetDirection);

        double distancia = centerBattle.subtract(center).getMagnitude();
        targetDirection = targetDirection(centerBattle, center);
        if (distancia < 300) {
            velocity = targetDirection.scale(maxVel * -0.05f);
            position = position.add(velocity.scale(dt));
            accelerating = true;
        }
    }

    private void patrol(Vector2D targetPos, Vector2D center, float dt) {
        Vector2D targetDirection = targetDirection(targetPos, center);
        toTarget(targetDirection);

        velocity = targetDirection.scale(maxVel * 0.05f);
        position = position.add(velocity.scale(dt));
        accelerating = true;
        if (position.getY() < 0) {
            position.setY(0);
        }

        if (position.getX() < 0) {
            position.setX(0);
        }
    }

    @Override
    public void update(float dt) {
        accelerating = false;

        if (isMovementLocked()) {
            return;
        }
        contMoves += dt;
        if (target == null || target.isDead() || target.isInvulnerable()) {
            searchTarget();
            if (target == null) {
                if (contMoves > 5000) {
                    contMoves = 0;

                    goal.setX(Math.random() * Constants.WIDTH);
                    goal.setY(Math.random() * Constants.HEIGHT);
                }

                double distance = goal.subtract(getCenter()).getMagnitude();
                if (distance > 10) {
                    patrol(goal, getCenter(), dt);
                }
            }
        } else {
            updateValuesShip(dt);
            fireRate += dt;
            if (contMoves > 5000) {
                contMoves = 0;
                currentPattern = (int) (Math.random() * 4);
                //searchTarget();
                //currentPattern = 2;
            }
            Vector2D enemiCenter = target.getCenter();
            Vector2D center = getCenter();
            double distance = enemiCenter.subtract(center).getMagnitude();
            switch (currentPattern) {
                case 0 ->
                    frontalAttack(enemiCenter, center, distance, dt);
                case 1 ->
                    flankAttack(enemiCenter, center, distance, dt, -1);
                case 2 ->
                    flankAttack(enemiCenter, center, distance, dt, 1);
                default ->
                    defensive(enemiCenter, center, dt);
            }

            if (position.getX() > Constants.WIDTH || position.getY() > Constants.HEIGHT
                    || position.getX() < 0 || position.getY() < 0) {
                destroy(10);
            }
        }
    }

    @Override
    public void draw(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        drawSpeed(g2d);
        drawRectangle(g2d);
        AffineTransform at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        at.rotate(angle, width / 2.0, height / 2.0);
        g2d.drawImage(texture, at, null);
    }

}
