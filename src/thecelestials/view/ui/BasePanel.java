/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.ui;

import javax.swing.JPanel;
import thecelestials.controller.ScreenSwitcher;

/**
 *
 * @author pc
 */
public abstract class BasePanel extends JPanel{
    protected final ScreenSwitcher switcher;
    public BasePanel(ScreenSwitcher switcher){
        this.switcher = switcher;
    }
}
