package transitionsDonnees;

public class MonoDatas {

	int[] datas;
	
	public MonoDatas(byte[] monoData){	
 		datas = getDataFromSamples(monoData);
	}
	
	// on assemble les 2x8bits dans un tableau de long
	private int[] getDataFromSamples(byte[] monoData){
		
		int [] datas = new int[monoData.length/2];
		byte[] sample = new byte[2];
		int data;
		
		for (int i = 0; i < monoData.length; i=i+2){
			sample[0] = monoData[i]; //poids faible
			sample[1] = monoData[i+1]; //poids fort
			
			data = byteArrayToInt(sample);
			
			data = data-32768;
			if(data<0){
				data = data + 65536;
			}
			
			datas[i/2]=data;
		}
		
		return datas;
	}
	
	// convert byte array to long
    private static int byteArrayToInt(byte[] b) {
        int result = 0;
        for (int i = b.length-1; i >=0; i--) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

	public int[] getDatas() {
		return datas;
	}
}
