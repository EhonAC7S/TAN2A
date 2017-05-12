package traitement;

import position.Point;

/**
*Applique le deplacement de l'objet sonore 
* 
**/

public class Traitement {
	
	int[] gauche;
	int[] droite;
	double pas;
	Point chemin;
	
	static final double D = 0.14;
	static final double Cair = 340;
	static final double K = 1;
	
	//static final int INFINI = 2147483647;
	
    public Traitement(int[] datas,double time,String nomChemin){
    	gauche = new int[datas.length];
    	droite = new int[datas.length];
    	pas = time/datas.length;
    	chemin = new Point("deplacements/"+nomChemin);
    	
    	distribution(datas);    	
    }
    
    // on traites les données sonore
   	private void distribution(int[] datas) {
   		
   		double x;
   		double z;
   		double t;
   		double dr;
   		double dl;
   		double tpoint=0;
   		Point p = chemin;
   		
   		//System.out.println("Passage point : "+p.getX()+" "+p.getZ());
   		for (int i = 0; i < datas.length; i++){
       		
   			// durée passée entre 2 points
   			t = i*pas-tpoint;
   			
   			// si on passe sur un point
   			if(t*p.getV()>=p.getL()){
   				tpoint += p.getL()/p.getV();
   				t = i*pas-tpoint;
   				p = p.getSuivant();	
   				//System.out.println("Passage point : "+p.getX()+" "+p.getZ());
   			}
   			
   			//calcul des coordonnées
   			x = calculX(p,t);
	   		z = calculZ(p,t);
	   		
	   		// calcul des distances
	   		dl = calculDL(x,z);
	   		dr = calculDR(x,z);
	   		
	   		
	   		// calcul des son retardés
	   		if(dl>0.07){
	   			gauche[i] = (int) (K*son(i,dl,datas)/dl);
	   		}
	   		else{
	   			gauche[i]=65535;
	   		}
	   		
	   		if(dr>0.07){
	   			droite[i] = (int) (K*son(i,dr,datas)/dr);
	   		}
	   		else{
	   			droite[i]=65535;
	   		}

   	    	// seuils
       		if(gauche[i]>65535){
       			gauche[i]=65535;
       		}
       		if(droite[i]>65535){
       			droite[i]=65535;
       		}
       	}
   	}


	private int son(int i, double d, int[] datas) {
		double t = i*pas - d/Cair; // t - to
   		int j = i;
   		while(j*pas>t){
   			j--;
   		}
   		if(j>=0){
   			double t1 = j*pas;
   	   		double t2= (j+1)*pas;
   	   		int st1 = datas[j];
   	   		int st2 = datas[j+1];
   	   		
   	   		return (int)(st1 + (t-t1)*((st2-st1)/(t2-t1)));
   		}
   		else{
   			return 0;
   		}
   		
	}

	private double calculX(Point p, double t) {
		double x1 = p.getX();
		double x2 = p.getSuivant().getX();
		double d = t*p.getV();
		double l = p.getL();
		return (x2-x1)*d/l + x1;
	}
   	
    private double calculZ(Point p, double t) {
    	double z1 = p.getZ();
		double z2 = p.getSuivant().getZ();
		double d = t*p.getV();
		double l = p.getL();
		return (z2-z1)*d/l + z1;
	}

    private double calculDR(double x, double z) {
    	double dX = x - D/2;

		return Math.sqrt(dX*dX + z*z);
	}
    
    private double calculDL(double x, double z) {
    	double dX = x + D/2;

		return Math.sqrt(dX*dX + z*z);
	}

	

	/*
    // on traites les données sonore
	private void etape(int[] datas) {
		
		
		for (int i = 0; i < datas.length; i++){
    		
    		gauche[i]= datas[i];
	    	droite[i]= datas[i];

	    	// seuils
    		if(gauche[i]>65535){
    			gauche[i]=65535;
    		}
    		if(droite[i]>65535){
    			droite[i]=65535;
    		}
    	}
	}*/
	
	public int[] getGauche(){
		return gauche;
	}
	
	public int[] getDroite(){
		return droite;
	}
}
