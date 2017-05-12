package son;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * Recupere le fichier wav et en extrait les informations utiles pour la suite. 
 * 
 * */

public class Sound {
	
	private String filename;
	private File soundFile;
	private AudioInputStream audioStream;
	private AudioFormat audioFormat;
	private byte[] monoData;
	private int dataSize;
	private double time;
	
	public Sound(String filename) {
		this.filename = filename;
		audioFile();
		audioStream();
		getStereoFormat();
		dataSize = ((int) audioStream.getFrameLength() )*audioFormat.getSampleSizeInBits()/8;
		lectureData();
		time = ((int) audioStream.getFrameLength() )/audioFormat.getSampleRate();
	}

	// on ouvre le fichier
	private void audioFile() {
		try {
			soundFile = new File(filename);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	// on ouvre un flux audio
	private void audioStream() {
		
        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
        	e.printStackTrace();
            System.exit(1);
        }
	}

	// retourne un format audio stereo avec les memes caractéristiques que le mono en entrée
	private void getStereoFormat() {
		AudioFormat baseFormat = audioStream.getFormat();
		
		AudioFormat.Encoding encoding = baseFormat.getEncoding();
		float sampleRate = baseFormat.getSampleRate();
		int sampleSizeInBits = baseFormat.getSampleSizeInBits();
		int channels = 2;
		int frameSize = sampleSizeInBits * channels/8;
		float frameRate = baseFormat.getFrameRate();
		boolean bigEndian = false;
		
		audioFormat =  new AudioFormat(encoding, sampleRate, sampleSizeInBits,channels, frameSize, frameRate, bigEndian);
	}
	
	private void lectureData(){
		// on initialise les tableaux de bytes
		monoData = new byte[dataSize];
		
		// on lit les bytes de musique en mono
 		try {
 			audioStream.read(monoData, 0, dataSize);
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	public AudioFormat getAudioFormat(){
		return audioFormat;
	}
	
	public byte[] getMonoData(){
		return monoData;
	}
	
	public int getDataSize(){
		return dataSize;
	}
	
	public double getTime(){
		return time;
	}
}
