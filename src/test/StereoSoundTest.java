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
 * Lit un fichier wav 16bits MONO pour le transformer en STEREO.
 * La fonction "monoToStereo" permet également de baisser à volonté le son arrivant dans chaque oreille.
 * 
 * 
 * !!!!!!!!! Le fichier doit être un MONO codé en 16 BITS !!!!!!!!!!!!!!!!!!!!!
 * 				(sinon vos oreilles vont souffrir)
 * 
 * */

public class  StereoSoundTest {
	
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;
    private int dataSize;
    static final int INFINI = 2147483647;
    
    
	public  StereoSoundTest(String filename){

		System.out.println("Début !!!");
        
		setSoundOptions(filename);
		sourceLine.start();
        lecture();
        sourceLine.drain();
        sourceLine.close();

        System.out.println("Fin !!!");
    }
    
	
    // on remplit les variables
	private void setSoundOptions(String filename){
		
		// on ouvre le fichier
        try {
        	soundFile = new File(filename);
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
		
		 // on modifie le format du fichier son
        audioFormat = getStereoFormat(audioStream.getFormat());

		
		 // dans infos toutes les infos de lecture du son ()
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);;
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
    }
   
	// lit le fichier audio
	private void lecture(){
		
		// on regarde combien de bytes on va lire
		dataSize = ((int) audioStream.getFrameLength() )*audioFormat.getSampleSizeInBits()/8;
  
		// on initialise les tableaux de bytes
		byte[] monoData = new byte[dataSize];
        byte[] stereoData =  new byte[dataSize*2];
        
        // on lit les bytes de musique en mono
        try {
            audioStream.read(monoData, 0, dataSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // on passe en stereo
        monoToStereo(monoData, stereoData);
        
        sourceLine.write(stereoData, 0, dataSize*2);
	}
    
	
	 // incoming: mono input stream  |   outgoing: stereo output stream
	private void monoToStereo(byte[] incoming, byte[] outgoing){
		
		// éviter de mettre coef entre 0 et 1 (ouille les oreilles)
		double coefGauche = 1;
		double coefDroit = INFINI;
		
		for (int i = 0; i < incoming.length; i=i+2){
			outgoing[ 2*i ] = (byte) (incoming[i]/coefGauche); //gauche 1
			outgoing[2*i+1] = (byte) (incoming[i+1]/coefGauche); //gauche 2
			outgoing[2*i+2] = (byte) (incoming[i]/coefDroit); // droite 1
			outgoing[2*i+3] = (byte) (incoming[i+1]/coefDroit); // droite 2
		}
	} 
	 

	// retourne un format audio stereo avec les memes caractéristiques que le mono en entrée
	private AudioFormat getStereoFormat(AudioFormat baseFormat) {
		AudioFormat.Encoding encoding = baseFormat.getEncoding();
		float sampleRate = baseFormat.getSampleRate();
		int sampleSizeInBits = baseFormat.getSampleSizeInBits();
		int channels = 2;
		int frameSize = sampleSizeInBits * channels/8;
		float frameRate = baseFormat.getFrameRate();
		boolean bigEndian = false;
		
		/*int channelOrigin = baseFormat.getChannels();
		System.out.println("channelOrigin " + channelOrigin);*/
		
		return new AudioFormat(encoding, sampleRate, sampleSizeInBits,channels, frameSize, frameRate, bigEndian);
	}
    
	
    
	public static void main(String[] args) throws Exception {
		
		
    	//new  StereoSound("sons/250Hz_44100Hz_16bit_30sec.wav");
		new  StereoSoundTest("sons/guitar.wav");
		//new  StereoSound("sons/Tiger.wav");
		
	}
}


