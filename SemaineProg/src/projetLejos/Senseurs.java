package projetLejos;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class Senseurs {

	private final static int PALET = 0;
	private final static int REPO = 1;
	private final static int MUR = 2;
	private final static int RIEN = -1;

	private SensorModes SenseurUS;
	private SensorModes SenseurTouch;

	public Senseurs(Port us, Port touch, Port color) {
		SenseurUS = new EV3UltrasonicSensor(us);
		SenseurTouch = new EV3TouchSensor(touch);
	}

	/**
	 * Renvoie un int différent selon ce qui se trouvre en face
	 * 
	 * @return 0 si le palet est pile en face, 1 si besoin de repositionner, 2 si
	 *         c'est un mur et -1 tant qu'on ne trouve rien
	 */
	public int detectPalet() {

		SampleProvider distance = SenseurUS.getMode("Distance");

		float[] sample = new float[distance.sampleSize()];

		float distPrec = 40;
		distance.fetchSample(sample, 0);
		while (distPrec > 0.05) {
			// Boucle qui renvoie des valeurs de distance tout le temps
			distPrec = sample[0];
			distance.fetchSample(sample, 0);
			System.out.println("Distance :  " + distPrec);
			
			// Test pour voir si on est juste en face d'un palet
			if ((distPrec > 0.285 && distPrec < 0.330) && (sample[0] > 0.340)) {
				// On va lancer le code d'attrapage
				return PALET;
			}

			// Le palet est pas directement en face, donc on il disparait avant d'être au
			// plus proche
			// Donc c'est le cas où la distance précédente est supérieure à la dernière
			// distance stockée
			if (sample[0] > distPrec + 0.5) {
				// On va lancer le code de balayage pour se retrouver de nouveau en face du
				// palet
				return REPO;
			}

			// détection d'un mur
			if (sample[0] < 0.26) {

				return MUR;
			}
		}
		return RIEN;
	}
}
