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
		ETAT = 0;
	}

	public static void main(String[] args) {

////		MovePilot pilote = new MovePilot(55, 55, Motor.A, Motor.B);
//		pilote.setLinearSpeed(2000);
//		
//		pilote.travel(2000);

		Shrek shrek = new Shrek();

		while (Button.ENTER.isUp()) {

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
				break;
//				Actions.tourne360();
//				int angle = Senseurs.recherche();
//				
//				if(angle == -1) {
//					System.out.println("Passage dans l'état de recherche aléatoire");
//					ETAT = ;
//					break;
//				} else {
//					Actions.tourne(angle);
//					System.out.println("Passage dans l'état d'attrapage d'un palet");
//					ETAT = ;
//					break;
//				}

			// Cas où le robot va aller vers un palet en vérifiant que tout se passe bien
			case 2:

				// Cas où le robot doit se repositionner en face du palet
			case 3:

				// Cas où le robot vient de choper un palet pour l'amener dans le camp en face
			case 4:

				// Cas où le robot vient de franchir une ligne blanche du camp adverse, dépôt du
				// palet
			case 5:
			}
		}
	}

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
}
