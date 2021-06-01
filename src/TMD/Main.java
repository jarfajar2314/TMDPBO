package TMD;


import java.awt.Canvas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Muhammad Fajar
 */
public class Main extends Canvas implements Runnable {
    
    public enum STATE{
        Game,
        GameOver
    };
    
    public STATE gameState = STATE.Game;

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
