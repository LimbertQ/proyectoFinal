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
        itemImageLabel.setIcon(new ImageIcon(image.getScaledInstance(340, 340, Image.SCALE_SMOOTH)));
        itemImageName.setText(text);
    }

    public void addLabelListener(String button, ActionListener listener) {
        JLabel labelButton;
        if(button.equals("previous")){
            labelButton = left;
        }else{
            labelButton = right;
        }
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
