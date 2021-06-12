/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewmodel;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Muhammad Fajar
 */

// Source
// Menu.wav = https://www.youtube.com/watch?v=Hvdfx9avekU
// Stage.wav = https://www.youtube.com/watch?v=L_OYo2RS8iU
public class Sound {
    
    public Clip playSound(Clip clip, String filename){      
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("resources/" + filename).getAbsoluteFile());
            // Get a sound clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            
            
        } catch (UnsupportedAudioFileException e) {
           e.printStackTrace();
        } catch (IOException e) {
           e.printStackTrace();
        } catch (LineUnavailableException e) {
           e.printStackTrace();
        }
        return clip;
    }
    
    public void stopSound(Clip clip){
        // System.out.println("Stopping");
        clip.stop();
    }
    
}
