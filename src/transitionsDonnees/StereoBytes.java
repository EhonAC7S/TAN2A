package transitionsDonnees;

/**
* Recupere les données en int mofifiées pour chaque oreille du son,
* les transforment et les stockent dans un tableau de bytes. 
* 
**/

public class StereoBytes {
	
	byte[] stereoData;

	public StereoBytes(int[] gauche,int[] droite){
		stereoData = getSamplesFromData(gauche,droite);
	}
	
	
	// on désassemble les tableau de long pour en faire un tableau de byte pour lecture stereo
	private byte[] getSamplesFromData(int[] gauche,int[] droite){
		
		byte[] stereoData = new byte[4*gauche.length];
		byte[] sample = new byte[2];
		int g,d;

		for (int i=0; i < gauche.length; i++){
			
			//gauche
			g = gauche[i];
			
			g = g + 32768;
			if(g >= 65536){
				g = g - 65536;
			}
			sample = intToByteArray(g);
			
			stereoData[ 4*i ] = sample[0]; //gauche 1
			stereoData[4*i+1] = sample[1]; //gauche 2
			
			//droite
			d = droite[i];
			
			d = d + 32768;
			if(d >= 65536){
				d = d - 65536;
			}
			sample = intToByteArray(d);
			
			stereoData[4*i+2] = sample[0]; // droite 1
			stereoData[4*i+3] = sample[1]; // droite 2	
		}
		
		return stereoData;
	}
	    
    // convert long to byte array
    private static byte[] intToByteArray(int l) {
    	byte[] result = new byte[2];
        for (int i = 0; i <= result.length-1; i++) {
            result[i] = (byte)(l & 0xFF);
            l >>= 8;
        }
        return result;
    }
    
    public byte[] getStereoData(){
    	return stereoData;
    }
    
    public int getSize(){
    	return stereoData.length;
    }
}
