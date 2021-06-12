package viewmodel;


import model.Player;
import model.Platform;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import model.GameObject;
import model.ID;
import model.TabelLevel;
import view.*;

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
    
    // Thread
    private Thread thread;
    private boolean running = false;
    
    // Data Game
    private String username;
    private int success = 0;
    private int fail = 0;    

    // Game variable
    private int currentLv = 0;
    private int fps;
    private boolean hit = false;
    private boolean landed = false;
    private int pBtm = 0;
    
    // Clip untuk bgm
    private Clip clip;
    
    // Background colors
    private String[] bgColors = {
        "#BFD1FF",
        "#BFF0FF",
        "#BFFFCD",
        "#E7FFBF",
        "#FFF5BF",
        "#FFCBBF",
        "#FFBFDE",
        "#DBBFFF"
    };
    
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
            // Add Player
            handler.addObject(new Player(340, 510, ID.Player));
            int tempX = 0;
            int tempY = 400;
            for(int i = 0; i < 3; i++){
                tempX = 0;
                for(int j = 0; j < 8; j++){
                    // Create platform
                    if(j != randInt(0, 7) && j != randInt(0, 7)){ // randomize gap
                        handler.addObject(new Platform(tempX, tempY, ID.Platform, i, j));
                    }
                    tempX += 100;
                }
                tempY -= 180;
            }
        }
    }
    
    // Username setter
    public void setUsername(String username){
        this.username = username;
    }

    // Thread Start
    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    
    // Thread Stop
    public synchronized void stop() {
        try{
            thread.join();
            running = false;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    // Main Gameloop
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        int elapsed = 0;
        int ypos = -1;
        int dy = 0;
        int dmax = 250;
        boolean fall = false;
        int t0 = 0;
        boolean tempS = false;
        Sound bgm = new Sound();
        clip = bgm.playSound(this.clip, "Stage.wav");

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
            
            if(gameState == STATE.GameOver){
                bgm.stopSound(this.clip);
                stop();
            }
            
            GameObject player = null;
            for(int i=0;i< handler.object.size(); i++){
                if(handler.object.get(i).getId() == ID.Player){
                    player = handler.object.get(i);
                }
            }
            
            // Platforms moves
            if(System.currentTimeMillis() - timer > 1000){
                elapsed++;
                timer += 1000;
                System.out.println("FPS: " + frames + " | Elapsed : " + elapsed + "s" + " | " + player.isOnAir() + " | " + landed);
                fps = frames;
                GameObject tempP = null;
                for(int i=0;i< handler.object.size(); i++){
                    if(handler.object.get(i).getId() == ID.Platform) {
                        tempP = handler.object.get(i);
                        int vel = 2;
                        if(tempP.getLevel() % 2 == 0){                           
                            if(tempP.getVel_x() < 0){
                                tempP.setVel_x(+vel);
                            }
                            else{
                                tempP.setVel_x(-vel);
                            }
                        }
                        else{
                            if(tempP.getVel_x() > 0){
                                tempP.setVel_x(-vel);
                            }
                            else{
                                tempP.setVel_x(+vel);
                            }
                        }
                    }
                }              
                frames = 0;
            }
            // .platforms moves
            
            if(dy > this.HEIGHT){
                dy = 0;
            }
            
            // Gravity System
            if(player != null && player.isOnAir()){
                if(ypos == -1){
                    // System.out.println("set ypos");
                    ypos = player.getY();
                    t0 = 0;                    
                }
                dy = ypos - player.getY();
                // on dmax
                if((dy >= dmax && !fall) || hit == true) {
                    // System.out.println("dmax");
                    if(hit == true && !landed && !tempS){
                        fail += 10;
                        tempS = true;
                    }
                    fall = true;
                    hit = false;
                    //player.setVel_y(+5);
                }
                // jump
                if(!fall && dy < dmax){
                    // System.out.println("jump");
                    player.setVel_y((dmax/10-dy/10)*-1);
                    // player.setVel_y(-3);
                }
                // fall
                else if(fall && dy > 0 && landed == false){
                    // System.out.println("fall");
                    t0++;                                         
                    player.setVel_y(t0/7);
                    // player.setVel_y(((dmax)/10));
                }
                // landed
                else if(dy <= 0 && fall || player.getY() == ypos || landed){
                    // System.out.println("reset");
                    player.setVel_y(0);
                    player.setY(ypos);
                    if(landed){ 
                        player.setY(pBtm);
                        levelClear();
                    }
                    ypos = -1;
                    dy = 0;
                    player.setOnAir(false);
                    fall = false;
                    t0 = 0;                  
                    landed = false;
                    tempS = false;
                }                
            }
            // .gravity
        }
        stop();
    }
    
    private void tick() {
        // move all object per tick
        handler.tick();
        if(gameState == STATE.Game){
            GameObject playerObject = null;
            // get player object
            for(int i=0;i< handler.object.size(); i++){
                if(handler.object.get(i).getId() == ID.Player){
                    playerObject = handler.object.get(i);
                }
            }
            if(playerObject != null){
                // then check collision
                for(int i=0;i< handler.object.size(); i++){
                    if(handler.object.get(i).getId() == ID.Platform && handler.object.get(i).getLevel() == currentLv){
                        GameObject prevP = null;
                        GameObject nextP = null;
                        if(i > 0 && handler.object.get(i-1).getIdx() == handler.object.get(i).getIdx()-1 && handler.object.get(i).getIdx() > 0){
                            prevP = handler.object.get(i-1);
                        }
                        if(i < handler.object.size()-1 &&handler.object.get(i+1).getIdx() == handler.object.get(i).getIdx()+1 && handler.object.get(i).getIdx() < 7){
                            nextP = handler.object.get(i+1);
                        }
                        if(checkCollision(playerObject, handler.object.get(i), prevP, nextP)){
                            // System.out.print("Collided on : ");
                            // System.out.println(playerObject.getX() + "|" + playerObject.getY());
                            hit = true;
                            break;
                        }
                    }
                }
            }
        }
    }
    
    // Check collision function
    public boolean checkCollision(GameObject player, GameObject platform, GameObject prevP, GameObject nextP){
        boolean result = false;
        
        int sizePlayer = 50;
        int wPlatform = 100;
        int hPlatform = 25;
     
        int playerLeft = player.x;
        int playerRight = player.x + sizePlayer;
        int playerTop = player.y;
        int playerBottom = player.y + sizePlayer;
        
        int platformLeft = platform.x;
        int platformRight = platform.x + wPlatform;
        int platformTop = platform.y;
        int platformBottom = platform.y + hPlatform;
                
        if((playerRight > platformLeft ) && (playerLeft < platformRight) && (platformBottom > playerTop) && (platformTop < playerBottom) ){
            // Check if player landed on platform
            if((playerBottom  <= platformBottom) && hrCheck(player)){
                // System.out.println("landed");
                landed = true;
                pBtm = platformTop-50;
            }
            result = true;
        }
        
        return result;
    }
    
    // Horizontal check when player landed on platform
    public boolean hrCheck(GameObject player){
        boolean result = false;
        
        int sizePlayer = 50;
        int wPlatform = 100;
     
        int playerLeft = player.x;
        int playerRight = player.x + sizePlayer;
        
        int[] arr = {0,0,0,0,0,0,0,0};
        int n = 0;
        
        for(int i=0;i< handler.object.size(); i++){
            if(handler.object.get(i).getId() == ID.Platform && handler.object.get(i).getLevel() == currentLv){
                arr[n] = handler.object.get(i).getX();
                n++;
            }
        }
        boolean lside = false;
        boolean rside = false;
        for(int i = 0; i < n; i++){
            if(playerLeft > arr[i] && playerLeft < arr[i]+wPlatform) lside = true;
            if(playerRight > arr[i] && playerRight < arr[i]+wPlatform) rside = true;
        }
        
        if(lside && rside) result = true;
        
        return result;
    }
    
    // When Player landed on platform
    private void levelClear(){
        // System.out.println("lvClear obj: " + handler.object.size());
        int tempY = 0;

        for(int i=0;i< handler.object.size(); i++){
            GameObject tempObj = handler.object.get(i);
            // Move player
            if(tempObj.getId() == ID.Player){
                tempObj.setY(510);
            }
            
            // Move all platforms
            else if(tempObj.getId() == ID.Platform && tempObj.getLevel() > currentLv){
                // System.out.println("P " + tempObj.getLevel() + tempObj.getIdx() + " moved");
                tempY = tempObj.getY();
                tempObj.setY(tempY += 180);
            }
            
            // Remove all unused platforms
            else if(tempObj.getId() == ID.Platform && tempObj.getLevel() == currentLv){
                // System.out.println("P " + tempObj.getLevel() + tempObj.getIdx() + " removed");
                handler.removeObject(tempObj);
                i = -1;
            }
        }
        tempY -= 180;
        int tempX = 0;
        
        // Add new platforms
        for(int j = 0; j < 8; j++){
            if(j != randInt(0, 7) && j != randInt(0, 7)){
                handler.addObject(new Platform(tempX, tempY, ID.Platform, currentLv+3, j));
            }
            tempX += 100;
        }
        
        // Add score
        success += 10;
        currentLv++;
    }
    
    // Render all object
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        
        Graphics g = bs.getDrawGraphics();
        
        g.setColor(Color.decode(this.bgColors[currentLv % 8]));
        g.fillRect(0, 0, WIDTH, HEIGHT);     
        
        if(gameState ==  STATE.Game){
            handler.render(g);
            
            Font currentFont = g.getFont();
            Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
            g.setFont(new java.awt.Font("Ubuntu", 1, 14));
            
            g.setColor(Color.WHITE);
            g.fillRoundRect(15, 5, 100, 20, 15, 15);
            g.fillRoundRect(15, 35, 100, 20, 15, 15);
            
            g.setColor(Color.decode("#3A1070"));
            g.drawString("Success: " +Integer.toString(success) , 20, 20);
            g.drawString("Fail: " +Integer.toString(fail) , 20, 50);
            
            // g.setColor(Color.BLACK);
            // g.drawString("FPS: " +Integer.toString(fps), WIDTH-120, 20);
            
        }       
        g.dispose();
        bs.show();
    }
    
    public void close(){
        window.CloseWindow();
    }
    
    // Random between function
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
    // Upload score to database
    public void uploadScore(){
        try {
            TabelLevel tlevel = new TabelLevel();
            tlevel.insertData(this.username, this.success, this.fail);
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        JOptionPane.showMessageDialog(null, "Username : " + this.username + "\n" + "Success : " + this.success + "\n" + "Fail : " + this.fail);
    }

}
