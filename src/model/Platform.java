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
public class Platform extends GameObject {
    private int level;
    private int idx;
    private String[] colors = {
        "#772E25",
        "#C44536",
        "#E6A786",
        "#197278",
        "#2F3D66",
        "#E25985",
    };
    private String color;
    
    public Platform(int x, int y, ID id, int level, int idx) {
        super(x, y, id);
        this.level = level;
        this.idx = idx;
        this.color = colors[level];
    }
    
    public int getLevel(){
        return this.level;
    }
    
    public int getIdx(){
        return this.idx;
    }

    @Override
    public void tick() {
        x += vel_x;
    }

    @Override
    public void render(Graphics g) {
        String colorCode;
        colorCode = this.color;
        g.setColor(Color.decode(colorCode));
        g.fillRect(x, y, 100, 25);
    }
    
}
