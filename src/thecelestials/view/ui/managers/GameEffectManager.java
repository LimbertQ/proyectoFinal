/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui.managers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import thecelestials.model.data.Assets;
import thecelestials.model.gameObjects.Laser;
import thecelestials.model.gameObjects.MovingObject;
import thecelestials.model.managers.GameManager;
import thecelestials.model.managers.GameObjectDestroyedListener;
import thecelestials.model.managers.IGameLoopEntity;
import thecelestials.model.math.Vector2D;
import thecelestials.view.ui.animations.Animation;

/**
 *
 * @author pc
 */
public class GameEffectManager extends GameManager implements IGameLoopEntity, GameObjectDestroyedListener {

    private final BufferedImage[] explosions;
    private final List<Animation> animations = new ArrayList<>();
    private final List<Animation> animToAdd = new ArrayList<>();

    public GameEffectManager() {
        explosions = new BufferedImage[9];
        loadExplosionFrames();
    }
    
    @Override
    public void clear(){
        animations.clear();
        animToAdd.clear();
    }
    
    private void loadExplosionFrames(){
        for(int i=0; i<explosions.length; i++){
            explosions[i] = Assets.images.get("exp"+i);
        }
    }

    @Override
    public void update(float dt) {
        for (Animation anim : animations) {
            anim.update(dt);
        }

        //eliminar animaciones muertas
        Iterator<Animation> it = animations.iterator();
        while (it.hasNext()) {
            Animation anim = it.next();
            if (!anim.isRunning()) {
                it.remove();
            }
        }

        if (!animToAdd.isEmpty()) {
            animations.addAll(animToAdd);
            animToAdd.clear();
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        for (Animation anim : animations) {
            anim.draw(g2d);
        }
    }

    private boolean isEquals(MovingObject destroyedObject) {
        return destroyedObject instanceof Laser;
    }

    @Override
    public void onGameObjectDestroyed(MovingObject destroyedObject) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (!isEquals(destroyedObject)) {
            Vector2D adjusted = destroyedObject.getPosition().subtract(new Vector2D(explosions[0].getWidth() / 2, explosions[0].getHeight() / 2));
            animToAdd.add(new Animation(explosions, 50, adjusted));
        }
    }
}
