package position;

public class Point {
	private double x;
	private double z;
	private double v;
	private Point suivant;
	
	public Point(String fichier){
		initialisation(fichier);
	}

	private void initialisation(String fichier) {
		x = 0;
		z = 0;
		v = 0;
		suivant = null;	
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

	public Point getSuivant(){
		return suivant;
	}
	
	public boolean hasNext(){
		return suivant!=null;
	}
}
