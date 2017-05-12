package main;

import son.LectureSound;
import son.Sound;
import traitement.Traitement;
import transitionsDonnees.MonoDatas;
import transitionsDonnees.StereoBytes;

/**
 * Lit un fichier wav 16bits MONO pour le transformer en STEREO qui donne une impression de déplacement. 
 * 
 * !!!!!!!!! Le fichier doit être un MONO codé en 16 BITS !!!!!!!!!!!!!!!!!!!!!
 * 				(sinon vos oreilles vont souffrir)
 * */

public class Main {
	
	public static void main(String[] args) throws Exception {
		System.out.println("Wait ...\n");
    	
		//String son = "250Hz_44100Hz_16bit_30sec.wav";
		//String son = "guitar.wav";
		String son = "Tiger.wav";
		
		String chemin = "test";
		
		////////////////////////////////////////////////////////
		
		Sound sound = new Sound("sons/"+son);	
		MonoDatas datas = new MonoDatas(sound.getMonoData());	
		Traitement t = new Traitement(datas.getDatas(),sound.getTime(),chemin);
		StereoBytes stereo = new StereoBytes(t.getGauche(),t.getDroite());	
		
		new  LectureSound(sound.getAudioFormat(),stereo.getStereoData());
		
		////////////////////////////////////////////////////////
	}
}
