/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.controller;

/**
 *
 * @author pc
 */
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;


public class Keyboard implements KeyListener{
    
    private final boolean[] keys = new boolean[256];
    
    public static boolean UP, DOWN, LEFT, RIGHT, SHOOT, ESC, SPECIAL;
    
    public Keyboard(){ 
        UP    = false;
        LEFT  = false;
        RIGHT = false;
        SHOOT = false;
        ESC   = false;
        SPECIAL = false;
    } 
    
    public void update(){ 
        UP = keys[KeyEvent.VK_W];
        DOWN = keys[KeyEvent.VK_S];
        LEFT = keys[KeyEvent.VK_A];
        RIGHT = keys[KeyEvent.VK_D];
        SHOOT = keys[KeyEvent.VK_P];
        ESC   = keys[KeyEvent.VK_ESCAPE];
        SPECIAL = keys[KeyEvent.VK_O];
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() < 256)
            keys[e.getKeyCode()] = true;
    }
    
    @Override
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode() < 256)
            keys[e.getKeyCode()] = false;
    }
    
    @Override
    public void keyTyped(KeyEvent e){
        
    }
}

