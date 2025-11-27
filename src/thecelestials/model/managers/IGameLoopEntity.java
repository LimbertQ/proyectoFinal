/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package thecelestials.model.managers;

import java.awt.Graphics2D;

/**
 *
 * @author pc
 */
public interface IGameLoopEntity {
    public void update(float dt);
    public void draw(Graphics2D g2d);
}
