/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.gameObjects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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
    private final Vector2D goal;
    private long fireRate = 0;
    private final int ufoFireRate;
    public NPCShip(Vector2D position, ShipStats shipStats, Vector2D velocity, double maxVel, GameObjectCreator creator, TargetProvider provider) {
        super(position, shipStats, velocity, maxVel, creator, Constants.UFO_FIRE_RATE);
        goal = new Vector2D(Constants.WIDTH, getPosition().getY());
        this.provider = provider;
        ufoFireRate = (int)Constants.UFO_FIRE_RATE+(int)(Math.random()*100);
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
        if (fireRate > ufoFireRate) {
            shooti(getCenter(), direction);
            fireRate = 0;
        }

    }

    private void flankAttack(Vector2D targetPos, Vector2D center, double distance, float dt, int leftRight) {
        if (distance > 120) {
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

        //double distancia = Constants.centerBattle.subtract(center).getMagnitude();
        targetDirection = targetDirection(Constants.centerBattle, center);
        velocity = targetDirection.scale(maxVel * -0.05f);
        Vector2D nextPos = position.add(velocity.scale(dt));
        boolean insideX = nextPos.getX() > 0 && nextPos.getX() + width < Constants.WIDTH;
        boolean insideY = nextPos.getY() > 0 && nextPos.getY() + height < Constants.HEIGHT;
        if (insideX && insideY) {
            position = nextPos;
            accelerating = true;
        }
    }

    protected void patrol(Vector2D center, float dt) {
        double distance = goal.subtract(getCenter()).getMagnitude();
        if (distance > 10) {
            Vector2D targetDirection = targetDirection(goal, center);
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
    }

    private Ship searchTarget() {
        if (target == null || target.isDead() || target.isInvulnerable()) {
            target = null;
            Ship target2 = provider.getTarget(getTeam());
            if(target2 != null && !target2.isInvulnerable()){
                target = target2;
            }
        }
        return target;
    }

    @Override
    public void update(float dt) {
        accelerating = false;

        if (isMovementLocked()) {
            return;
        }
        contMoves += dt;
        if (searchTarget() == null) {
            if (contMoves > 5000) {
                contMoves = 0;

                goal.setX(Math.random() * Constants.WIDTH);
                goal.setY(Math.random() * Constants.HEIGHT);
            }
            //-------
            patrol(getCenter(), dt);
            //-------
        } else {
            updateValuesShip(dt);
            fireRate += dt;
            if (contMoves > 5000) {
                contMoves = 0;
                if(shipClass.equals("crucero")){
                    currentPattern = (int) (Math.random() * 2);
                }else{
                    currentPattern = (int) (Math.random() * 4);
                }
            }
            Vector2D enemiCenter = target.getCenter();
            Vector2D center = getCenter();
            double distance = enemiCenter.subtract(center).getMagnitude();
            switch (currentPattern) {
                case 0 ->
                    frontalAttack(enemiCenter, center, distance, dt);
                case 1 ->
                    defensive(enemiCenter, center, dt);
                case 2 ->
                    flankAttack(enemiCenter, center, distance, dt, 1);
                default ->
                    flankAttack(enemiCenter, center, distance, dt, -1);
            }

            if (position.getX() > Constants.WIDTH || position.getY() > Constants.HEIGHT
                    || position.getX() < 0 || position.getY() < 0) {
                destroy(10);
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {

        drawSpeed(g2d);
        AffineTransform at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        at.rotate(angle, width / 2.0, height / 2.0);
        g2d.drawImage(texture, at, null);
        drawRectangle(g2d);
    }
}
