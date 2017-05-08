package traitement;

import position.Point;

public class Traitement {
	
	int[] gauche;
	int[] droite;
	double time;
	double pas; 
	
	static final int INFINI = 2147483647;
	
    public Traitement(int[] datas,double time,Point deplacement){
    	gauche = new int[datas.length];
    	droite = new int[datas.length];
    	this.time = time;
    	pas = time/datas.length;
    	System.out.println(time+"sec");
    	
    	// à faire: calculs...
    	
    	etape(datas);    	
    }
    
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
	}
	
	public int[] getGauche(){
		return gauche;
	}
	
	public int[] getDroite(){
		return droite;
	}
}
