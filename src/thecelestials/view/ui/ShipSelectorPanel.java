/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import java.awt.Component;
import java.awt.Image;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import thecelestials.model.data.Assets;
import thecelestials.view.ui.Factory.MenuComponentFactory;

/**
 *
 * @author pc
 */
public class ShipSelectorPanel extends JPanel{
    private int index;
    private final JLabel image = new JLabel();
    //private final ImageIcon icon = new ImageIcon();
    public ShipSelectorPanel(JLabel back){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //setBackground(Color.red);
        setOpaque(false); // Hazlo transparente si quieres ver el fondo del panel padre
        
        JLabel title = MenuComponentFactory.sampleLabel("SELECTOR", -1, 1);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setOpaque(false);
        add(title);
        
        //ImageIcon imagen = new ImageIcon(Assets.shipsAvaible.getFirst().getProfile());
        //icon.setImage(Assets.shipsAvaible.getFirst().getProfile().getScaledInstance(340, 340, Image.SCALE_SMOOTH));
        //currentShip.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(340, 340, Image.SCALE_SMOOTH)));
        
        //JLabel image = new JLabel();
        image.setIcon(new ImageIcon(Assets.getCurrentShip().getProfile().getScaledInstance(340, 340, Image.SCALE_SMOOTH)));
        
        
        JLabel left = MenuComponentFactory.createArrowButton("<<", e ->{nextShip(-1);});
        JLabel right = MenuComponentFactory.createArrowButton("<<", e ->{nextShip(1);});
        JPanel contents = new JPanel();
        //contents.setLayout(new BorderLayout());
        contents.setLayout(new BoxLayout(contents, BoxLayout.X_AXIS));
        // Alineación vertical de los componentes dentro del BoxLayout.X_AXIS:
        //left.setAlignmentY(Component.CENTER_ALIGNMENT);
        //currentShip.setAlignmentY(Component.CENTER_ALIGNMENT);
        //right.setAlignmentY(Component.CENTER_ALIGNMENT);
        contents.setOpaque(false);
        contents.add(left);
        contents.add(Box.createHorizontalStrut(20));
        contents.add(image);
        contents.add(Box.createHorizontalStrut(20));
        contents.add(right);
        
        add(contents);
        
        JLabel infNave = MenuComponentFactory.sampleLabel(Assets.shipsAvaible.getFirst().getName(), -1, 1);
        infNave.setAlignmentX(Component.CENTER_ALIGNMENT);
        infNave.setOpaque(false);
        add(infNave);
        add(Box.createVerticalStrut(67));
        
        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        //southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
        //southPanel.add(Box.createHorizontalStrut(Integer.MAX_VALUE));
        //southPanel.add(Box.createVerticalGlue(), BorderLayout.CENTER);
        //exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        //exit.setPreferredSize(new Dimension(40, exit.getPreferredSize().height));
        southPanel.add(back);
        //southPanel.add(Box.createHorizontalStrut(Integer.MAX_VALUE));
        
        
        add(southPanel);
    }
    
    private void nextShip(int num){
        if(index+num < Assets.shipsAvaible.size() && index+num >= 0){
            index+=num;
            Assets.currentShip = index;
            //icon.setImage(Assets.shipsAvaible.get(Assets.currentShip).getProfile().getScaledInstance(340, 340, Image.SCALE_SMOOTH));
            //image.setIcon(new ImageIcon(icon.getImage().getScaledInstance(340, 340, Image.SCALE_SMOOTH)));
        
            image.setIcon(new ImageIcon(Assets.shipsAvaible.get(Assets.currentShip).getProfile().getScaledInstance(340, 340, Image.SCALE_SMOOTH)));
        }else{
            //System.out.println("fuera de rango"+(index+num));
        }
    }
}
