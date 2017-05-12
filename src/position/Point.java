package position;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


/**
* Boucle de points qui forme le déplacement
* 
**/

public class Point {
	private double x;
	private double z;
	private double v;
	private double l;
	private Point suivant;
	
	public Point(String fichier){
		initialisation(fichier);
	}
	
	public Point(double x, double z, double v){
		this.x = x;
		this.z = z;
		this.v = v;
	}

	// initialisation des points
	private void initialisation(String fichier) {

		FileReader flot;
		BufferedReader filtre1 ;
			
		try {
			flot = new FileReader(fichier) ;
			filtre1 = new BufferedReader(flot);
			
			// on passe la premiere ligne
			filtre1.readLine();
			
			// on lance la création récursive	
			Scanner filtre2 = new Scanner(filtre1.readLine());
			filtre2.useDelimiter(";");
			this.x = filtre2.nextDouble();
			this.z = filtre2.nextDouble();
			this.v = filtre2.nextDouble();
			filtre2.close();

			this.suivant = ini(filtre1,filtre1.readLine());
			
			// on ferme
			filtre1.close();
			flot.close();	
		}
		catch (IOException e){
			System.out.println("error : " + e);
		}	
		
		// on cree récursivement les longeurs entre les points
		calculL(this);	
	}
	
	// initialisation recurcive
	private Point ini(BufferedReader filtre1, String ligne) throws IOException{
		if(ligne==null){
			return this;
		}
		else{
			
			Scanner filtre2 = new Scanner(ligne);
			filtre2.useDelimiter(";");
			Point point = new Point(filtre2.nextDouble(),filtre2.nextDouble(),filtre2.nextDouble());
			filtre2.close();

			point.suivant = ini(filtre1,filtre1.readLine());
			
			return point;
		}
	}
	
	// calcul recurcif des distances
	private void calculL(Point p) {
		if(p.suivant==this){
			// on revient au debut
			double dX = p.x-this.x;
			double dZ = p.z-this.z;
			p.l = Math.sqrt(dX*dX + dZ*dZ);
		}
		else{
			double dX = p.x-p.suivant.getX();
			double dZ = p.z-p.suivant.getZ();
			p.l = Math.sqrt(dX*dX + dZ*dZ);
			
			calculL(p.suivant);
		}	
	}

	
	public double getX(){
		return x;
	}
	
	public double getZ(){
		return z;
	}

	public double getV(){
		return v;
	}

	public double getL(){
		return l;
	}
	public Point getSuivant(){
		return suivant;
	}
	
}
