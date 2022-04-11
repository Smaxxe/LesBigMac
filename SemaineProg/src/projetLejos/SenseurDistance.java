package projetLejos;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class SenseurDistance {
	
	private final static int PALET = 0;
	private final static int REPO = 1;
	private final static int MUR = 2;
	private final static int ERROR = 3;

	/**
	 * Renvoie un int différent selon ce qui se trouvre en face
	 * 
	 * @return 0 si le palet est pile en face, 1 si besoin de repositionner, 2 si c'est un mur
	 */
	public static int detect(Port p) {
		SensorModes sensor = new EV3UltrasonicSensor(p);
		
		SampleProvider distance = sensor.getMode("Distance");
		
		float[] sample = new float[distance.sampleSize()];
		
		float distPrec = 40;
		distance.fetchSample(sample, 0);
		//Boucle qui renvoie des valeurs de distance tout le temps
		while (distPrec > 0.05) {
			distPrec = sample[0];
			distance.fetchSample(sample, 0);
			System.out.println("Distance :  " + distPrec);
			
			//Test pour voir si on est juste en face d'un palet
			if(distPrec < 0.330 && (sample[0] > 0.340)) {
				//On va lancer le code d'attrapage
				
				return PALET;
			}
			
			//Le palet est pas directement en face, donc on il disparait avant d'être au plus proche
			//Donc c'est le cas où la distance précédente est supérieure à la dernière distance stockée
			if(sample[0] > distPrec + 0.5) {
				//On va lancer le code de balayage pour se retrouver de nouveau en face du palet
				return REPO;
			}
			
			//détection d'un mur
			if(sample[0] < 0.26) {

				return MUR;
			}
			
		}
		//Si on est sortis de la boucle c'est que quelque chose a heurté le senseur
		//On peur relancer la recherche direct
		return ERROR;
	}
}
