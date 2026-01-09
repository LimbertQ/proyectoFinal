/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FontMetrics;
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
import thecelestials.model.math.Vector2D;
import thecelestials.view.ui.Factory.MenuComponentFactory;
import thecelestials.view.ui.animations.Text;

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
        if(!nextPanel.equals("mainMenuCard")){
            Assets.setear();
        }
        //final String destinationCard ;
        //repaint();
        progressTimer = new Timer(30, e -> {
            repaint();
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
                            // Esta acción usa los valores actualizados de this.nextPanel y this.missionID
                            if (nextPanel.equals("mainMenuCard")) {
                                switcher.initializeMenus();
                            }

                            nextButton.setFocusable(false);
                            switcher.showCard(nextPanel, missionID);
                        }
                    });
                    if (nextPanel.equals("mainMenuCard")) {
                        nextButton.setFont(Assets.fontMed);
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
        Graphics2D g2d = (Graphics2D) g;

        // 1. Fondo negro (opcional, para que no se vea rastro de otros frames)
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // 2. Configuración de la barra
        float percentage = (Assets.MAX_COUNT > 0) ? (float) Assets.count / Assets.MAX_COUNT : 0;
        int barX = Constants.WIDTH / 2 - Constants.LOADING_BAR_WIDTH / 2;
        int barY = Constants.HEIGHT / 2 - Constants.LOADING_BAR_HEIGHT / 2;

        // 3. Dibujo de la barra con Gradiente
        GradientPaint gp = new GradientPaint(barX, barY, Color.WHITE, barX + Constants.LOADING_BAR_WIDTH, barY, Color.BLUE);
        g2d.setPaint(gp);
        g2d.fillRect(barX, barY, (int) (Constants.LOADING_BAR_WIDTH * percentage), Constants.LOADING_BAR_HEIGHT);

        // Contorno de la barra
        g2d.drawRect(barX, barY, Constants.LOADING_BAR_WIDTH, Constants.LOADING_BAR_HEIGHT);

        // 4. Dibujo del Texto usando tu clase Text
        if (Assets.fontMed != null) {
            int displayPercent = (int) (percentage * 100);
            String progressText = "CARGANDO DATOS... " + displayPercent + "%";

            // Calculamos el centro exacto: 
            // X = Mitad de pantalla
            // Y = Un poco debajo de la barra (barY + altura de la barra + margen)
            int textY = barY + Constants.LOADING_BAR_HEIGHT + 40;

            Text.drawText(g2d, progressText, new Vector2D(Constants.WIDTH / 2, textY),
                    true, Color.WHITE, Assets.fontMed);
        }
    }
}
