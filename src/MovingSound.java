import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Lit un fichier wav 16bits MONO pour le transformer en stereo modifié.
 * 
 * !!!!!!!!! Le fichier doit être codé en 16 BITS !!!!!!!!!!!!!!!!!!!!!
 * 				(sinon vos oreilles vont souffrir)
 * 
 * */

public class  MovingSound {
	
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;
    private int dataSize;
    static final int INFINI = 2147483647;
    
    
	public  MovingSound(String filename){

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

	// retourne un format audio stereo avec les memes caractéristiques que le mono en entrée
	private AudioFormat getStereoFormat(AudioFormat baseFormat) {
		AudioFormat.Encoding encoding = baseFormat.getEncoding();
		float sampleRate = baseFormat.getSampleRate();
		int sampleSizeInBits = baseFormat.getSampleSizeInBits();
		int channels = 2;
		int frameSize = sampleSizeInBits * channels/8;
		float frameRate = baseFormat.getFrameRate();
		boolean bigEndian = false;
		
		return new AudioFormat(encoding, sampleRate, sampleSizeInBits,channels, frameSize, frameRate, bigEndian);
	}
	
	 // incoming: mono input stream  |   outgoing: stereo output stream
	private void monoToStereo(byte[] incoming, byte[] outgoing){
		
		long[] datas = new long[incoming.length/2];
		long[] gauche = new long[outgoing.length/4];
		long[] droite = new long[outgoing.length/4];
		
		// on assemble les 2bytes pour former la donnée complete
		getDataFromSamples(incoming,datas);
		
		// on traite la donnée
		traitement(datas,gauche,droite);
		
		// on reforme les samples à partir des données modifiés
		getSamplesFromData(outgoing,gauche,droite);
	} 
	
	
	// on assemble les 2x8bits dans un tableau de long
	private static void getDataFromSamples(byte[] incoming,long[] datas){
		byte[] sample = new byte[2];
		long data;
		
		for (int i = 0; i < incoming.length; i=i+2){
			sample[0] = incoming[i]; //poids faible
			sample[1] = incoming[i+1]; //poids fort
			
			data = byteArrayToLong(sample);
			
			data = data-32768;
			if(data<0){
				data = data + 65536;
			}
			
			datas[i/2]=data;
		}
	}
	
	// on désassemvle les tableau de long pour en faire un tableau de byte pour lecture stereo
	private static void getSamplesFromData(byte[] outgoing,long[] gauche,long[] droite){
		long g,d;
		byte[] sample = new byte[2];
		
		for (int i=0; i < gauche.length; i++){
			
			//gauche
			g = gauche[i];
			
			g = g + 32768;
			if(g >= 65536){
				g = g - 65536;
			}
			sample = longToByteArray(g);
			
			outgoing[ 4*i ] = sample[0]; //gauche 1
			outgoing[4*i+1] = sample[1]; //gauche 2
			
			//droite
			d = droite[i];
			
			d = d + 32768;
			if(d >= 65536){
				d = d - 65536;
			}
			sample = longToByteArray(d);
			
			outgoing[4*i+2] = sample[0]; // droite 1
			outgoing[4*i+3] = sample[1]; // droite 2
		}
	}
	
	// convert byte array to long
    private static long byteArrayToLong(byte[] b) {
        long result = 0;
        for (int i = b.length-1; i >=0; i--) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }
    
    // convert long to byte array
    private static byte[] longToByteArray(long l) {
    	byte[] result = new byte[2];
        for (int i = 0; i <= result.length-1; i++) {
            result[i] = (byte)(l & 0xFF);
            l >>= 8;
        }
        return result;
    }
    
    // on traites les données sonore
    private void traitement(long[] datas,long[] gauche, long[] droite){
    	
    	double k = 0.5; // si le son est à moins d'un mètre

    	for (int i = 0; i < datas.length; i++){
    		gauche[i]=(long) (k*datas[i]/10);
	    	droite[i]=(long) (k*datas[i]/10);
    		
    		// seuils
    		if(gauche[i]>65535){
    			gauche[i]=65535;
    		}
    		if(droite[i]>65535){
    			droite[i]=65535;
    		}

    	}
    }
    
    
	public static void main(String[] args) throws Exception {
		
    	//new  MovingSound("250Hz_44100Hz_16bit_30sec.wav");
		//new  MovingSound("guitar.wav");
    	new  MovingSound("Tiger.wav");
	}
	
	
}