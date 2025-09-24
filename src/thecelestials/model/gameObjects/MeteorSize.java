/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package thecelestials.model.gameObjects;

/**
 *
 * @author pc
 */
public enum MeteorSize {
    // Definimos los enums del más grande al más pequeño.
    // Su fragmento siguiente es 'null'.
    TINY(null, "Btiny", 5, 1),

    // 'SMALL' se rompe en 'TINY'. 'TINY' ya está definido.
    SMALL(TINY, "Bsmall", 10, 5),

    // 'MED' se rompe en 'SMALL'. 'SMALL' ya está definido.
    MED(SMALL, "Bmed", 15, 10),

    // 'BIG' se rompe en 'MED'. 'MED' ya está definido.
    BIG(MED, "Bbig", 30, 15);

    private final MeteorSize nextSize;
    private final String quantity;
    
    private final int healt;
    private final int damage;
    MeteorSize(MeteorSize nextSize, String quantity, int healt, int damage) {
        this.nextSize = nextSize;
        this.quantity = quantity;
        this.healt = healt;
        this.damage = damage;
    }

    public MeteorSize getNextSize() {
        return nextSize;
    }

    public String getSize() {
        return quantity;
    }
    
    public int getHealt(){
        return healt;
    }
    
    public int getDamage(){
        return damage;
    }
}
