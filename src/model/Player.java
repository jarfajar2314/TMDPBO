/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

//import static TMD.Main.STATE.Game;
import model.GameObject;
import java.awt.Color;
import java.awt.Graphics;
import model.ID;
import viewmodel.Game;

/**
 *
 * @author Muhammad Fajar
 */
public class Player extends GameObject {

    public Player(int x, int y, ID id) {
        super(x, y, id);
    }

    @Override
    public void tick() {
        x += vel_x;
        y += vel_y;
        
        if(y >= Game.HEIGHT-80) {
            y = Game.HEIGHT-80;
        }
        if(x <= 0) {
            x = 0;
        }
        if(x >= Game.WIDTH-60) {
            x = Game.WIDTH-60;
        }
        
    }

    @Override
    public void render(Graphics g) {
        String colorCode;
        colorCode = "#3f6082";
        g.setColor(Color.decode(colorCode));
        g.fillOval(x, y, 50, 50);
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getIdx() {
        return 0;
    }
    
}
