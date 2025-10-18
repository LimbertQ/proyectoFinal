/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.model.data;

/**
 *
 * @author pc
 */
public class GameEntity extends AssetDefinition{
    private int state;
    public GameEntity(String id, String name, String description, String profileImagePath, int state) {
        super(id, name, description, profileImagePath);
        this.state = state;
    }
    
    public int getState(){
        return state;
    }
    
    public void setState(){
        state = 1;
    }
}
