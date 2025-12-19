/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import thecelestials.controller.ScreenSwitcher;
import thecelestials.model.data.Assets;
import thecelestials.model.math.Constants;
import thecelestials.view.ui.Factory.MenuComponentFactory;

/**
 *
 * @author pc
 */
public class LoadingPanel extends JPanel {

    //private String nextPanel, missionID;
    private final JLabel nextButton;
    private Timer progressTimer;
    private final ScreenSwitcher switcher;
    //private String destinationCard;
    public LoadingPanel(String menuType, ScreenSwitcher switcher) {
        //setLayout(new BorderLayout());
        //setBackground(Color.BLACK);
        setOpaque(false);
        //contentPanel.setOpaque(false);
        //contentPanel.setLayout(BorderLayout.SOUTH);
        setBorder(BorderFactory.createEmptyBorder(Constants.PHeight(0.326), 0, 0, 0));
        //contentPanel.setBackground(Color.black);
        //add(contentPanel, BorderLayout.CENTER);

        //setBackground(Color.BLACK);
        //revalidate();
        this.switcher = switcher;
        //nextButton = MenuComponentFactory.createClickableLabel("SIGUIENTE", 0, e -> switcher.showCard(nextPanel, missionID));
        nextButton = MenuComponentFactory.sampleLabel("SIGUIENTE", 0, 1);
        add(nextButton, BorderLayout.CENTER);
        for (MouseListener ml : nextButton.getMouseListeners()) {
            nextButton.removeMouseListener(ml);
        }
        nextButton.setVisible(false);
    }

    public void nextPanel(String nextPanel, String missionID) {
        for (MouseListener ml : nextButton.getMouseListeners()) {
            nextButton.removeMouseListener(ml);
        }
        nextButton.setVisible(false);
        
        Assets.setear();
        //final String destinationCard ;
        //repaint();
        progressTimer = new Timer(30, e -> {
            repaint();
            System.out.println(Assets.count);
        });
        progressTimer.start();
        // 2. INICIAR EL HILO DE CARGA (El trabajo pesado en segundo plano)
        Thread loadingThread = new Thread(() -> {
            if (nextPanel.equals("mainMenuCard")) {
                Assets.init(); // <--- 1. ESTO HACE Assets.loaded = true
                //destinationCard = "campaignMenuCard";
            } else if (nextPanel.equals("gameCanvasCard")) {
                Assets.loadGame(missionID);
                //destinationCard = nextPanel;
                //repaint();
                //System.out.println("esty aqui");
            }

            // 2. Notificación al Hilo de la UI (EDT)
            SwingUtilities.invokeLater(() -> {

                // El check de Assets.loaded es solo una formalidad, 
                // ya que init() termina justo antes:
                if (Assets.loaded) {
                    progressTimer.stop(); // ✅ Funciona porque es un campo de clase
                    nextButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            //System.err.println("click de mrd"+nextPanel);
                            // Esta acción usa los valores actualizados de this.nextPanel y this.missionID
                            nextButton.setFocusable(false);
                            switcher.showCard(nextPanel, missionID);
                        }
                    });
                    //nextButton.setText("SIGUIENTE");
                    
                    if(nextPanel.equals("mainMenuCard")){
                        nextButton.setFont(Assets.fontMed);
                        switcher.initializeMenus();
                    }
                    nextButton.setVisible(true);
                    repaint(); // Para asegurar el 100%
                    
                    //switcher.showCard(nextPanel, missionID);
                }
            });
        });
        loadingThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.fillRect(0, 0, getWidth(), getHeight());
        //g.drawImage(Assets.fondo, WIDTH, HEIGHT, this);

        GradientPaint gp = new GradientPaint(
                Constants.WIDTH / 2 - Constants.LOADING_BAR_WIDTH / 2,
                Constants.HEIGHT / 2 - Constants.LOADING_BAR_HEIGHT / 2,
                Color.WHITE,
                Constants.WIDTH / 2 + Constants.LOADING_BAR_WIDTH / 2,
                Constants.HEIGHT / 2 + Constants.LOADING_BAR_HEIGHT / 2,
                Color.BLUE
        );

        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(gp);
        //System.out.println(max);

        float percentage = (Assets.count / Assets.MAX_COUNT);

        g2d.fillRect(Constants.WIDTH / 2 - Constants.LOADING_BAR_WIDTH / 2,
                Constants.HEIGHT / 2 - Constants.LOADING_BAR_HEIGHT / 2,
                (int) (Constants.LOADING_BAR_WIDTH * percentage),
                Constants.LOADING_BAR_HEIGHT);

        g2d.drawRect(Constants.WIDTH / 2 - Constants.LOADING_BAR_WIDTH / 2,
                Constants.HEIGHT / 2 - Constants.LOADING_BAR_HEIGHT / 2,
                Constants.LOADING_BAR_WIDTH,
                Constants.LOADING_BAR_HEIGHT);
    }
}
