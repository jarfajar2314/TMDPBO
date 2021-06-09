package viewmodel;


import model.Player;
import model.Platform;
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
    private int success = 0;
    private int fail = 0;
    
    private int currentLv = 0;

    private int pX;
    private int pY;
    private int fps;
    private boolean hit = false;
    private boolean landed = false;
    private boolean lvUp = false;
    private int pBtm = 0;
    
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
            handler.addObject(new Player(340, 520, ID.Player));
//            int temp = 0;
//            for(int i = 0; i < 8; i++){
//                if(i != 2 && i != 7){
//                    handler.addObject(new Platform(temp, 400, ID.Platform, 0, i));
//                    handler.addObject(new Platform(temp, 400, ID.Platform, 0, i));
//                }
//                temp += 100;
//            }
            int tempX = 0;
            int tempY = 400;
            for(int i = 0; i < 3; i++){
                tempX = 0;
                for(int j = 0; j < 8; j++){
                    if(j != 2 && j != 7){
                        handler.addObject(new Platform(tempX, tempY, ID.Platform, i, j));
                    }
                    tempX += 100;
                }
                tempY -= 180;
            }
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
        int elapsed = 0;
        int ypos = -1;
        int dy = 0;
        int dmax = 250;
        boolean fall = false;
        int t0 = 0;
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
            
            if(gameState == STATE.GameOver){
                stop();
            }
            
            GameObject player = null;
            for(int i=0;i< handler.object.size(); i++){
                if(handler.object.get(i).getId() == ID.Player){
                    player = handler.object.get(i);
                }
            }
            
            // Moving Player           
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
                
//                GameObject player = null;
//                for(int i=0;i< handler.object.size(); i++){
//                    if(handler.object.get(i).getId() == ID.Player){
//                        player = handler.object.get(i);
//                    }
//                }
//                int pX = player.getX();
//                int pY = player.getY();
//                System.out.println("X: " + pX + " | Y : " + pY);
                frames = 0;
                if(gameState == STATE.Game){
                    
                }
            }
            if(dy > this.HEIGHT){
                dy = 0;
            }

//          Gravity System
            if(player != null && player.isOnAir()){
//                if(y0 == -1){
//                    y0 = player.getY();
//                    t0 = elapsed;
//                }
//                else{
//                    int yx = y0 - g * ((elapsed-t0)*(elapsed-t0)) / 2;
//                    player.setY(yx);
//                }
                
//                =============================================
//                System.out.println("dy = " + dy + " | ypos = " + ypos + " | fall =" + fall);
                if(ypos == -1){
                    System.out.println("set ypos");
                    ypos = player.getY();
                    t0 = 0;                    
                }
                dy = ypos - player.getY();
                if((dy >= dmax && !fall) || hit == true) {
                    System.out.println("dmax");
                    fall = true;
                    hit = false;
                    //player.setVel_y(+5);
                }
                if(!fall && dy < dmax){
//                        System.out.println("jump");
                    // jump
                    player.setVel_y((dmax/10-dy/10)*-1);
                }
                else if(fall && dy > 0 && landed == false){
                    // fall
//                        System.out.println("fall");
                    t0++;
                    player.setVel_y(t0/3);
                }
                else if(dy <= 0 && fall || player.getY() == ypos || landed){
                    System.out.println("reset");
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
//                hrCheck(playerObject);
                for(int i=0;i< handler.object.size(); i++){
                    if(handler.object.get(i).getId() == ID.Platform && handler.object.get(i).getLevel() == currentLv){
                        GameObject prevP = null;
                        GameObject nextP = null;
                        if(i > 0 && handler.object.get(i-1).getIdx() == handler.object.get(i).getIdx()-1){
                            prevP = handler.object.get(i-1);
                        }
                        if(i < handler.object.size()-1 &&handler.object.get(i+1).getIdx() == handler.object.get(i).getIdx()+1){
                            nextP = handler.object.get(i+1);
                        }
                        if(checkCollision(playerObject, handler.object.get(i), prevP, nextP)){
                            System.out.print("Collided on : ");
                            System.out.println(playerObject.getX() + "|" + playerObject.getY());
                            if(landed == true){
//                                STATE.LevelUp;
                            }
                            hit = true;
                            fail += 10;
                            break;
                        }
                    }
                }
            }
        }
    }
    
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
        
        if(prevP != null) {
            platformLeft -= wPlatform;
        }
        
        if(nextP != null){
            platformRight += wPlatform;
        }
                
        if((playerRight > platformLeft ) &&
        (playerLeft < platformRight) &&
        (platformBottom > playerTop) &&
        (platformTop < playerBottom)
        ){
//            if((playerBottom  <= platformBottom)){
//                System.out.println("top side");
//            }
//            
//            if((playerLeft >= platformLeft)){
//                System.out.println("left side in");
//            }
//            else{
//                System.out.println("left is :" + platformLeft);
//            }
//            
//            if((playerRight <= platformRight)){
//                System.out.println("right side in");
//            }
//            else{
//                System.out.println("right is :" + platformRight);
//            }
            
//            if((playerBottom  <= platformBottom) && (playerLeft >= platformLeft) && (playerRight <= platformRight))
            
            if((playerBottom  <= platformBottom) && hrCheck(player)){
                System.out.println("landed");
                landed = true;
                pBtm = platformTop-50;
            }
            result = true;
        }
        
        return result;
    }
    
    
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
    
    private void levelClear(){
        System.out.println("lvClear obj: " + handler.object.size());
        int tempY = 0;

        for(int i=0;i< handler.object.size(); i++){
            GameObject tempObj = handler.object.get(i);
            if(tempObj.getId() == ID.Player){
                tempObj.setY(520);
            }

            else if(tempObj.getId() == ID.Platform && tempObj.getLevel() > currentLv){
//                System.out.println("P " + tempObj.getLevel() + tempObj.getIdx() + " moved");
                tempY = tempObj.getY();
                tempObj.setY(tempY += 180);
            }
            else if(tempObj.getId() == ID.Platform && tempObj.getLevel() == currentLv){
//                System.out.println("P " + tempObj.getLevel() + tempObj.getIdx() + " removed");
                handler.removeObject(tempObj);
                i = -1;
            }
        }
        
        success += 10;
        currentLv++;
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

            g.setColor(Color.BLACK);
            g.drawString("Success: " +Integer.toString(success) , 20, 20);
            
            g.setColor(Color.BLACK);
            g.drawString("Fail: " +Integer.toString(success) , 20, 30);
//
//            g.setColor(Color.BLACK);
//            g.drawString("FPS: " +Integer.toString(fps), WIDTH-120, 20);
            
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
