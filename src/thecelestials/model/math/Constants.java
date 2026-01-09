/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.math;

/**
 *
 * @author pc
 */
public class Constants {

    // frame dimensions

    public static int WIDTH = 0;
    public static int HEIGHT = 0;

    // player properties

    public static final int FIRERATE = 300;
    public static final int FIRERATE_X2 = 150;
    public static final double DELTAANGLE = 0.05;
    public static final double ACC = 0.2;
    public static final double PLAYER_MAX_VEL = 7.0;
    public static final long FLICKER_TIME = 200;
    public static final long SPAWNING_TIME = 3000;
    public static final long GAME_OVER_TIME = 3000;

    // Laser properties

    public static final double LASER_VEL = 18.0;

    // Meteor properties

    public static final double METEOR_INIT_VEL = 2.0;

    public static final int METEOR_SCORE = 20;

    public static final double METEOR_MAX_VEL = 6.0;

    public static final int SHIELD_DISTANCE = 150;

    // Ufo properties

    public static final int NODE_RADIUS = 160;

    public static final double UFO_MASS = 60;

    public static final int UFO_MAX_VEL = 3;
    
    public static final long UFO_CLONE_RATE = 7000;
    
    public static final long UFO_CLONE_RATE_PRE = 6000;

    public static long UFO_FIRE_RATE = 1000;

    public static double UFO_ANGLE_RANGE = Math.PI / 2;

    public static final int UFO_SCORE = 40;

    public static final long UFO_SPAWN_RATE = 15000;
    
    public static final long UFO_SPAWN_WAVE = 50000;

    public static final int LOADING_BAR_WIDTH = 1000;
    public static final int LOADING_BAR_HEIGHT = 50;


    //public static final String SCORE_PATH = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +
      //  "\\Space_Ship_Game\\data.json"; // data.xml if you use XMLParser

    // This variables are required to use XMLParser

    // =============================================

    public static final long POWER_UP_DURATION = 10000;
    public static final long POWER_UP_SPAWN_TIME = 8000;

    public static final long SHIELD_TIME = 12000;
    public static final long DOUBLE_SCORE_TIME = 10000;
    public static final long FAST_FIRE_TIME = 14000;
    public static final long DOUBLE_GUN_TIME = 12000;
    
    
    public static final int SCORE_STACK = 1000;
    
    public static Vector2D centerBattle;
    public static int PWidth(double val){
        return (int)(WIDTH*val);
    }
    public static int PHeight(double val){
        return (int)(HEIGHT*val);
    }
    public static void init(int w, int h){
        WIDTH = w;
        HEIGHT = h;
        centerBattle = new Vector2D(WIDTH / 2.0, HEIGHT / 2.0);
    }
}