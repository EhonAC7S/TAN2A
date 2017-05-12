package son;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
* Lit le son modifié en stereo 
* 
**/

public class LectureSound {

	private SourceDataLine sourceLine;
	
	public LectureSound(AudioFormat audioFormat, byte[] stereoData){
		iniSourceLine(audioFormat);
		lecture(stereoData);
	}
	
	// initialise le lecteur
	private void iniSourceLine(AudioFormat audioFormat){
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
	private void lecture(byte[] stereoData){
			System.out.println("Début Lecture");
			System.out.println(".");
			System.out.println(".");
			System.out.println(".");
	        sourceLine.start();
	        sourceLine.write(stereoData, 0, stereoData.length);  
	        sourceLine.drain();
	        sourceLine.close();
	        System.out.println("Fin Lecture");
	}
}
