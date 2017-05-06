import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class StereoSound {

	private final int BUFFER_SIZE = 128000;
    private File soundFile;
    private AudioInputStream audioStream;
    private SourceDataLine sourceLine;
    private boolean mono = false;

	public StereoSound(String filename){

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
       
        getsourceLine();
	
        //permet à la ligne d'engager l' entrée/sortie
        sourceLine.start();

        lecture();

        //ferme le fichier audio
        sourceLine.drain();
        sourceLine.close();
    }
    
    
	private void getsourceLine(){
		
		AudioFormat audioFormat;
		
		 // on recupere le format du fichier son
		if(mono){
			audioFormat = audioStream.getFormat();
		}
		else{
			audioFormat = getStereoFormat(audioStream.getFormat());
		}
		
		 // dans infos toutes les infos de lecture du son ()
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
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
		 
        int nBytesRead = 0;
        
        byte[] monoData = new byte[BUFFER_SIZE];
        byte[] stereoData =  new byte[BUFFER_SIZE*2];
        
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(monoData, 0, BUFFER_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            if(mono){
            	if (nBytesRead >= 0) {
            		sourceLine.write(monoData, 0, BUFFER_SIZE);
            	}
            }
            else{
            	monoToStereo(monoData, stereoData);
            	if (nBytesRead >= 0) {
                	sourceLine.write(stereoData, 0, BUFFER_SIZE*2);
                }
            }
            System.out.println("------------");   
        }
	}
    
    
	 // incoming: mono input stream  |   outgoing: stereo output stream
	void monoToStereo(byte[] incoming, byte[] outgoing){
		for (int i = 0; i < incoming.length; i=i+2){
			outgoing[ 2*i ] = incoming[i]; //gauche 1
			outgoing[2*i+1] = incoming[i+1]; //gauche 2
			outgoing[2*i+2] = incoming[i]; // droite 1
			outgoing[2*i+3] = incoming[i+1]; // droite 2
		}
	} 
	 

	// retourne un format audio stereo avec les memes caractéristiques que le mono en entré
	private AudioFormat getStereoFormat(AudioFormat baseFormat) {
		 float sampleRate = baseFormat.getSampleRate();
		 int sampleSizeInBits = baseFormat.getSampleSizeInBits();
		 int channels = 2;
		 boolean bigEndian = false;
	    	
		 return new AudioFormat(sampleRate, sampleSizeInBits, channels, true, bigEndian);
	}
	

	 
    public static void main(String[] args) throws Exception 
	{
    	new StereoSound("250Hz_44100Hz_16bit_30sec.wav");
		//new Sound("mosquito.wav");
	}
}

