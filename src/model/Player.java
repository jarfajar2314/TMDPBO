/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Muhammad Fajar
 */
public class Player extends PlayerObject {

    public Player(int x, int y, ID id) {
        super(x, y, id);
    }

    @Override
    public void tick() {
        x += vel_x;
        y += vel_y;
        
//        x = Game.clamp(x, 0, Game.WIDTH - 60);
//        y = Game.clamp(y, 0, Game.HEIGHT - 80);
    }

    @Override
    public void render(Graphics g) {
        String colorCode;
        colorCode = "#3f6082";
        g.setColor(Color.decode(colorCode));
        g.fillOval(x, y, 50, 50);
    }
    
}
