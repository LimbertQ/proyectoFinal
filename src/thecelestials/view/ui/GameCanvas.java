/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;
import thecelestials.controller.Keyboard;
import thecelestials.model.data.Assets;
import thecelestials.model.managers.GameContentManager;
import thecelestials.view.ui.Factory.MenuComponentFactory;

/**
 *
 * @author pc
 */
public class GameCanvas extends Canvas implements Runnable {

    private BufferStrategy bs;
    private Graphics g;
    private Thread thread;
    private boolean running = false;

    private volatile boolean isPaused = true;

    private static final int FPS = 60;
    private static final double TARGET_TIME = 1_000_000_000.0 / FPS;
    private int averageFps = FPS;

    private long lastTime;

    private final Keyboard keyboard;

    private final GameContentManager gcm;

    public GameCanvas() {
        setFocusable(true);
        keyboard = new Keyboard();
        addKeyListener(keyboard);
        gcm = new GameContentManager();
    }

    private void showDialog(int n) {
        if (!(n > 0 && n < 3)) {
            n = 0;
            Keyboard.ESC = false;
        }
        MenuComponentFactory.showDialog(n);
    }

    private void update(float dt) {

        if (Keyboard.ESC || gcm.gameOver() > 0 && gcm.gameOver() < 3) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameCanvas.class.getName()).log(Level.SEVERE, null, ex);
            }
            isPaused = true;
            gcm.pause();
            if (gcm.gameOver() == 2) {
                Assets.unlocks();
            }
            showDialog(gcm.gameOver());
        } else {
            keyboard.update();
            gcm.update(dt);
        }
    }

    private void draw() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        gcm.draw(g);

        g.setColor(Color.WHITE);
        g.drawString("" + averageFps, 10, 20);

        g.dispose();
        bs.show();
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    //al empezar un nivel
    public void playGame() {
        lastTime = System.nanoTime();
        gcm.clear();
        gcm.playGame();
        isPaused = false;
    }

    public void resume() {
        isPaused = false;
        lastTime = System.nanoTime();
        gcm.resume();
    }

    private void stop() {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long now;
        lastTime = System.nanoTime();
        int frames = 0;
        long timeAccumulator = 0;
        double delta = 0;
        while (running) {
            if (isPaused == false) {
                now = System.nanoTime();
                delta += (now - lastTime) / TARGET_TIME;
                timeAccumulator += (now - lastTime);
                lastTime = now;

                if (delta >= 1) {

                    update((float) (delta * TARGET_TIME * 0.000001f));
                    draw();
                    delta--;
                    frames++;
                }

                if (timeAccumulator >= 1_000_000_000) {
                    averageFps = frames;
                    frames = 0;
                    timeAccumulator = 0;
                }
            }
        }
        stop();
    }
}
