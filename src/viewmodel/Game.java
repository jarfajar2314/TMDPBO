package viewmodel;


import model.Player;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;
import model.GameObject;
import model.ID;
import view.*;
import viewmodel.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Muhammad Fajar
 */
public class Game extends Canvas implements Runnable {
    // Untuk window nya
    Window window;
    // Width
    public static final int WIDTH = 800;
    // Height
    public static final int HEIGHT = 600;
    
    private Thread thread;
    private boolean running = false;
    
    private int time = 10;

    
    // Object handler
    private Handler handler;

    // Enum state pada game
    public enum STATE{
        Game,
        GameOver
    };
    
    // Dimulai dengan state 'game'
    public STATE gameState = STATE.Game;
    
    public Game() {
        // Call window
        window = new Window(WIDTH, HEIGHT, "The Highest Level", this);
        // Call handler
        handler = new Handler();
        // KeyListener -> menerima input keyboard
        this.addKeyListener(new KeyInput(handler, this));
        
        if(gameState == STATE.Game){
//            handler.addObject(new Items(100,150, ID.Item));
            handler.addObject(new Player(400, 500, ID.Player));
        }
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    
    public synchronized void stop() {
        try{
            thread.join();
            running = false;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
//        playSound("/BGM.wav"); // play BGM
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            
            while(delta >= 1){
                tick();
                delta--;
            }
            if(running){
                render();
                frames++;
            }
            // Moving Player           
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
                if(gameState == STATE.Game){
                    
                }
            }
        }
        stop();
    }
    
    private void tick() {
        handler.tick();
        if(gameState == STATE.Game){
            GameObject playerObject = null;
            for(int i=0;i< handler.object.size(); i++){
                if(handler.object.get(i).getId() == ID.Player){
                    playerObject = handler.object.get(i);
                }
            }
            if(playerObject != null){
                for(int i=0;i< handler.object.size(); i++){
                    if(handler.object.get(i).getId() == ID.Item){
//                        if(checkCollision(playerObject, handler.object.get(i), 0)){
//                            playSound("/Eat.wav");
//                            handler.removeObject(handler.object.get(i));
//                            Random rand = new Random();
//                            score = score + (rand.nextInt(10) + 1);
//                            time = time + rand.nextInt(5);
//                            break;
//                        }
                    }
                }
            }
        }
    }
    
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        
        Graphics g = bs.getDrawGraphics();
        
        g.setColor(Color.decode("#F1f3f3"));
        g.fillRect(0, 0, WIDTH, HEIGHT);
                
        
        
        if(gameState ==  STATE.Game){
            handler.render(g);
            
            Font currentFont = g.getFont();
            Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
            g.setFont(newFont);

//            g.setColor(Color.BLACK);
//            g.drawString("Score: " +Integer.toString(score), 20, 20);
//
//            g.setColor(Color.BLACK);
//            g.drawString("Time: " +Integer.toString(time), WIDTH-120, 20);
            
        }else{
            Font currentFont = g.getFont();
            Font newFont = currentFont.deriveFont(currentFont.getSize() * 3F);
            g.setFont(newFont);

            g.setColor(Color.BLACK);
            g.drawString("GAME OVER", WIDTH/2 - 120, HEIGHT/2 - 30);

            currentFont = g.getFont();
            Font newScoreFont = currentFont.deriveFont(currentFont.getSize() * 0.5F);
            g.setFont(newScoreFont);

//            g.setColor(Color.BLACK);
//            g.drawString("Score: " +Integer.toString(score), WIDTH/2 - 50, HEIGHT/2 - 10);
//            
//            g.setColor(Color.BLACK);
//            g.drawString("Press Space to Continue", WIDTH/2 - 100, HEIGHT/2 + 30);
        }
                
        g.dispose();
        bs.show();
    }
    
    public void close(){
        window.CloseWindow();
    }
}
