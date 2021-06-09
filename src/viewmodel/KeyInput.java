/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewmodel;

import model.GameObject;
import viewmodel.Game.STATE;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import model.ID;
import view.Menu;

/**
 *
 * @author Muhammad Fajar
 */
public class KeyInput extends KeyAdapter {
    private Handler handler;
    Game main;
    
    public KeyInput(Handler handler, Game main){
        this.main = main;
        this.handler = handler;
    }
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        
        if(main.gameState == STATE.Game){
            for(int i = 0;i<handler.object.size();i++){
                if(handler.object.get(i).getId() == ID.Player){                 
                    GameObject tempObject = handler.object.get(i);
                    if((key == KeyEvent.VK_W) || (key == KeyEvent.VK_UP)){
    //                    tempObject.setVel_y(-5);
                        tempObject.setOnAir(true);
                    }

                    if((key == KeyEvent.VK_S) || (key == KeyEvent.VK_DOWN)){
                        tempObject.setVel_y(+5);
                    }

                    if((key == KeyEvent.VK_A) || (key == KeyEvent.VK_LEFT)){
                        tempObject.setVel_x(-5);
                    }

                    if((key == KeyEvent.VK_D) || (key == KeyEvent.VK_RIGHT)){
                        tempObject.setVel_x(+5);
                    }
                    if(key == KeyEvent.VK_SPACE){
//                        tempObject.setOnAir(true);
                        main.gameState = STATE.GameOver;
                    }
                    break;
                }
            }
            
        }
        
        if(main.gameState == STATE.GameOver){
            if(key == KeyEvent.VK_SPACE){
                new Menu().setVisible(true);
                main.close();
            }
        }
        if(key == KeyEvent.VK_ESCAPE){
            System.exit(1);
        }   
    }
    
    
    
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        
        for(int i = 0;i<handler.object.size();i++){
            if(handler.object.get(i).getId() == ID.Player){
                GameObject tempObject = handler.object.get(i);
                if((key == KeyEvent.VK_W) || (key == KeyEvent.VK_UP)){
                    tempObject.setVel_y(0);
                }

                if((key == KeyEvent.VK_S) || (key == KeyEvent.VK_DOWN)){
                    tempObject.setVel_y(0);
                }

                if((key == KeyEvent.VK_A) || (key == KeyEvent.VK_LEFT)){
                    tempObject.setVel_x(0);
                }

                if((key == KeyEvent.VK_D) || (key == KeyEvent.VK_RIGHT)){
                    tempObject.setVel_x(0);
                }
                break;
            }
        }
    }
}
