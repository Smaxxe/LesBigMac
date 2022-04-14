package projetLejos;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Senseurs {

	private final static int PALET = 0;
	private final static int REPO = 1;
	private final static int MUR = 2;
	private final static int RIEN = -1;

	private EV3UltrasonicSensor SenseurUS;
	private EV3TouchSensor SenseurTouch;
	private EV3ColorSensor SenseurColor;

	public Senseurs(Port us, Port touch, Port color) {
		SenseurUS = new EV3UltrasonicSensor(us);
		SenseurTouch = new EV3TouchSensor(touch);
		SenseurColor = new EV3ColorSensor(color);
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

	/**
	 * Analyse les distances pendant un mouvement
	 * 
	 * @return une arraylist de float avec toutes les valeurs chopées pendant le
	 *         mouvement
	 */
	public List<Float> prendreMesures(Actions act) {
		SampleProvider distance = SenseurUS.getMode("Distance");

		float[] sample = new float[distance.sampleSize()];

		List<Float> valeurs = new ArrayList<Float>();

		while (act.isMovingPilote()) {
			Delay.msDelay(100);
			distance.fetchSample(sample, 0);
			valeurs.add(sample[0]);
		}

		return valeurs;
	}

	/**
	 * Prend une arraylist en paramètres et analyse ses valeurs pour renvoyer un
	 * angle de placement du robot le paramètre angle permet de savoir l'angle de
	 * rotation du robot
	 * 
	 * @return l'angle de déplacement nécessaire pour se positionner devant la
	 *         distance la plus courte
	 */
	public int anglePosition360(int angle, List<Float> valeurs) {

		float val;
		boolean test = false;
		float stock = 500;

		ListIterator<Float> it = valeurs.listIterator();

		while (it.hasNext()) {
			while (test == false) {
				try {
					val = it.next();

					if (val < stock) {
						stock = val;
					}

					test = true;
				} catch (Error e) {
					it.next();
				}
			}

			test = false;
		}

		// On passe au calcul de l'angle à renvoyer en se basant sur l'indice de la plus
		// petite valeur récupérée

		int sampleDegres = angle / valeurs.size(); // Calcul de l'angle de chaque donnée
		int angleTot = sampleDegres * valeurs.indexOf(stock); // Calcul de l'angle absolu à prendre

		if (angleTot <= (angle / 2)) { // si on est dans la première moitié du tour
			return (-angleTot); // on renvoie l'angle calculé
		} else { // Sinon on renvoie la valeur pour aller dans l'autre sens
			return (angle - angleTot);
		}
	}

	public int anglePosition180(int angle, List<Float> valeurs) {

		float val;
		boolean test = false;
		float stock = 500;

		ListIterator<Float> it = valeurs.listIterator();

		while (it.hasNext()) {
			while (test == false) {
				try {
					val = it.next();

					if (val < stock) {
						stock = val;
					}

					test = true;
				} catch (Error e) {
					it.next();
				}
			}

			test = false;
		}

		// On passe au calcul de l'angle à renvoyer en se basant sur l'indice de la plus
		// petite valeur récupérée

		int sampleDegres = angle / valeurs.size(); // Calcul de l'angle de chaque donnée
		int angleTot = sampleDegres * valeurs.indexOf(stock); // Calcul de l'angle absolu à prendre

		return (angle - angleTot); // on renvoie l'angle calculé
	}

	public boolean isPressed() {
		float[] sample = new float[1];
		SenseurTouch.fetchSample(sample, 0);

		return sample[0] != 0;
	}

	/**
	 * Delays all action as long as the touch sensor is not pressed
	 * 
	 */

	private void waitForTouch() {
		System.out.println("Waiting for press on Touch Sensor");

		while (!isPressed()) {
			Delay.msDelay(100);
		}

		System.out.println("Touch Sensor pressed.");
	}

	/**
	 * Tourne tant que le robot ne détecte pas la couleur blanc
	 * 
	 * @return true quand c'est blanc, false sinon
	 */
	public boolean isWhite() {
		SampleProvider colorProvider = SenseurColor.getRGBMode();
		float[] colorSample = new float[colorProvider.sampleSize()];

		SenseurColor.fetchSample(colorSample, 0);
		Delay.msDelay(250);

		if (colorSample[0] > 0.18 && colorSample[1] > 0.18 && colorSample[2] > 0.18) {
			Delay.msDelay(250);
			return true;
		}

		return false;
	}

}