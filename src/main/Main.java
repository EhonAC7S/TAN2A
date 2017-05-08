package main;

import position.Point;
import son.LectureSound;
import son.Sound;
import traitement.Traitement;
import transitionsDonnees.MonoDatas;
import transitionsDonnees.StereoBytes;

public class Main {
	
	public static void main(String[] args) throws Exception {
		System.out.println("Début !!!");
    	
		Point deplacement = new Point("aaa");
		
		Sound sound = new Sound("250Hz_44100Hz_16bit_30sec.wav");
		//SoundData sound = new SoundData("guitar.wav");
		//SoundData sound = new SoundData("Tiger.wav");
		
		MonoDatas datas = new MonoDatas(sound.getMonoData());	
		Traitement t = new Traitement(datas.getDatas(),sound.getTime(),deplacement);
		StereoBytes stereo = new StereoBytes(t.getGauche(),t.getDroite());	
		
		new  LectureSound(sound.getAudioFormat(),stereo.getStereoData());
		
		System.out.println("Fin !!!");
	}
}
