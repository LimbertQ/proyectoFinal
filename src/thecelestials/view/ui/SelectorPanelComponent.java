/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import thecelestials.model.math.Constants;

/**
 *
 * @author pc
 */
public class SelectorPanelComponent extends JPanel {

    private final JLabel itemImageLabel;
    private final JLabel itemImageName;
    private final JLabel left;
    private final JLabel right;
    private final JLabel back;

    public SelectorPanelComponent(JLabel itemImageLabel, JLabel back, JLabel left, JLabel right, JLabel nameImage) {
        this.itemImageLabel = itemImageLabel;
        this.back = back;
        this.left = left;
        this.right = right;
        this.itemImageName = nameImage;
    }

    public void setSelectorItemIcon(BufferedImage image, String text) {
        // 1. Calculamos el tamaño proporcional basado en la altura de la pantalla
        // Usamos Constants.HEIGHT para que sea dinámico
        int size = (int) (Constants.HEIGHT * 0.442);

        // 2. Aplicamos el escalado usando ese tamaño para que siga siendo un cuadrado
        // Esto asegura que en tu compu siga siendo 340x340
        itemImageLabel.setIcon(new ImageIcon(image.getScaledInstance(size, size, Image.SCALE_SMOOTH)));

        itemImageName.setText(text);
    }

    public void setItemNameLabelText(String text) {
        // Asumiendo que 'itemNameLabel' es el JLabel que muestra el nombre del ítem/vidas/dinero
        this.itemImageName.setText(text);
    }

    public void addLabelListener(String button, ActionListener listener) {
        JLabel labelButton;
        labelButton = switch (button) {
            case "previous" ->
                left;
            case "next" ->
                right;
            case "imageName" ->
                itemImageName;
            default ->
                back;
        };
        labelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Esta acción usa los valores actualizados de this.nextPanel y this.missionID
                listener.actionPerformed(new ActionEvent(labelButton, ActionEvent.ACTION_PERFORMED, button));
                //switcher.showCard(nextPanel, missionID);
            }
        });
    }
}
