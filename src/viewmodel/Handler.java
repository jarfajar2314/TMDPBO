/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewmodel;

import java.awt.Graphics;
import java.util.LinkedList;
import model.*;

/**
 *
 * @author Muhammad Fajar
 */
public class Handler {
    LinkedList<PlayerObject> object = new LinkedList<PlayerObject>();
    
    public void tick(){
        for(int i=0;i<object.size(); i++){
            PlayerObject tempObject = object.get(i);
            
            tempObject.tick();
        }
    }
    
    public void render(Graphics g){
        for(int i=0;i<object.size(); i++){
            PlayerObject tempObject = object.get(i);
            
            tempObject.render(g);
        }
    }
    
    public void addObject(PlayerObject object){
        this.object.add(object);
    }
    
    public void removeObject(PlayerObject object){
        this.object.remove(object);
    }
    
    public int countObject(){
        return this.object.size();
    }
}
