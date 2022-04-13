package projetLejos;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public class Shrek {

	// Déclaration des ports qu'on va utiliser
	private static final String PORTUS = "S1";
	private static final String PORTOUCH = "S2";
	private static final String PORTCOLOR = "S3";
	
	//Variable d'angle pour 360 et 180 degrés
	private static final int ANGLE360 = 300;
	private static final int ANGLE180 = 150;

	// Objets qui vont nous servir tout le long
	private Senseurs sensor;
	private Actions act;

	// Etat qui va diriger le switch
	private static int ETAT;

	public Shrek() {

		// instanciation d'un objet qui contrôle les senseurs
		Port us = LocalEV3.get().getPort(PORTUS);
		Port touch = LocalEV3.get().getPort(PORTOUCH);
		Port color = LocalEV3.get().getPort(PORTCOLOR);
		sensor = new Senseurs(us, touch, color);

		// Instanciation d'un objet qui contrôle les moteurs
		act = new Actions();

		// Déclaration de l'état initial
		ETAT = 1;
	}

	public static void main(String[] args) {
		Shrek shrek = new Shrek();

		System.out.println("En attente de lancement");
		Button.waitForAnyPress();
		
		//pour stopper le robot on appuie sur le bouton de retour
		while (Button.ESCAPE.isUp()) {

			switch (ETAT) {

			// Tout premier cas, on code un comportement pour attraper le premier palet et
			// le ramener
			case 0:
				System.out.println("Début de partie, lancement de la première action en dur");

				ETAT = shrek.premierPaletPositionADroite();
				break;

			// Cas où le robot va tourner pour trouver un angle vers lequel aller
			case 1:
				System.out.println("Recherche d'un palet par rotation");
				ETAT = shrek.rotationInformation();
				break;

			// Cas où le robot va aller vers un palet en vérifiant que tout se passe bien
			case 2:
				System.out.println("En direction d'un élément proche");
				ETAT = shrek.avancerVers();
				
				break;
				// Cas où le robot doit se repositionner en face du palet
			case 3:
				System.out.println("Repositionnement nécessaire");
				break;
				// Cas où le robot vient de choper un palet pour l'amener dans le camp en face
			case 4:
				System.out.println("Test");
				break;
				// Cas où le robot vient de franchir une ligne blanche du camp adverse, dépôt du
				// palet
			case 5:
			}
		}
	}

	/** Méthode appelée au tout début de la partie, récupère en dur deux palets 
	 * 
	 * @return l'état vers lequel aller ensuite
	 */
	private int premierPaletPositionADroite() {
		this.act.mouvement(400, false);
		this.act.choperPalet(); // il attrape le 1er palet
		
		// il évite le palet
		this.act.tourne(-55, false);
		this.act.mouvement(500, false);
		this.act.tourne(50, false);
		
		//Il va vers le camp adverse
		this.act.mouvement(1500, false);
		this.act.ouvrirPinces(false);
		this.act.mouvement(-200, false);
		this.act.fermerPinces(false);

		this.act.tourne(360, false); // Tour au moment de retourner chercher le 2 palet
		this.act.ouvrirPinces(false);
		this.act.mouvement(300, false);
		this.act.fermerPinces(false); // A chopé le 2e palet

		this.act.tourne(-380, false);
		this.act.mouvement(400, false); // Va vers la ligne adverse
		this.act.lacherPalet();

		return 1;
	}
	
	/** Tourne sur lui-même et récupère des infos puis se tourne vers l'élément le plus proche
	 * 
	 * @return
	 */
	private int rotationInformation() {
		this.act.tourne(ANGLE360, true);
		int angle = this.sensor.anglePosition(ANGLE360, this.sensor.prendreMesures(act));
		this.act.tourne(angle, false);
		return 2;
	}
	
	/** Avance vers l'élément le plus proche en vérifiant que c'est pas un mur ou qu'il ne passe
	 * pas à côté du palet
	 * 
	 * @return l'état suivant selon les paramètres
	 */
	private int avancerVers() {
		this.act.mouvement(2000, true);
		
		int status = this.sensor.detectPalet();
		if(status == 0) {
			this.act.stopPilote();
			this.act.choperPalet();
		} else if( status == 1) {
			this.act.stopPilote();
			return 3;
		} else {
			this.act.stopPilote();
			this.act.tourne180(false);
		}
		
		return 4;
	}
}
