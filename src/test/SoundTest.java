package test;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Lit un fichier wav.
 * Permet de savoir si l'algorithme de diffusion du son marche.
 *  
 * ( Classe Témoin )
 * 
 * */

public class SoundTest {
	
	private final int BUFFER_SIZE = 128000;
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;

    public SoundTest(String filename){

        String strFilename = filename;

        // on ouvre le fichier
        try {
            soundFile = new File(strFilename);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // on ouvre un flux audio
        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        
        //Il est nécessaire de connaître le format audio du fichier d'entrée 
        //pour permettre à java de créer l'objet DataLine adéquat
        audioFormat = audioStream.getFormat();

        
        // En plus du format du flux audio d'entrée il est nécessaire de
        // spécifier le type de DataLine qu'on veut
        // ici le DataLine qu'on souhaite est un SourceDataLine qui permet
        // la lecture (targetDataLine permet l'enregistrement).
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        
        
        // On récupère le DataLine adéquat et on l'ouvre
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // permet à la ligne d'engager l' entrée/sortie
        sourceLine.start();

        // lit le fichier audio
        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE*10];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                sourceLine.write(abData, 0, nBytesRead);
            }
        }

        //ferme le fichier audio
        sourceLine.drain();
        sourceLine.close();
    }
    
    
    public static void main(String[] args) throws Exception{
 
    	//new Sound("sons/250Hz_44100Hz_16bit_30sec.wav");
    	new SoundTest("sons/guitar.wav");
    	//new Sound("sons/Tiger.wav");
	}

}
