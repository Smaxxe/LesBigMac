package projetLejos;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
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
		
		//instanciation d'un objet qui contrôle les senseurs
		Port us = LocalEV3.get().getPort(PORTUS);
		Port touch = LocalEV3.get().getPort(PORTOUCH);
		Port color = LocalEV3.get().getPort(PORTCOLOR);
		sensor = new Senseurs(us, touch, color);

		//Instanciation d'un objet qui contrôle les moteurs
		act = new Actions();

		//Déclaration de l'état initial
		ETAT = 2;
	}

	public static void main(String[] args) {

		Shrek shrek = new Shrek();

		while (Button.ENTER.isUp()) {

			switch (ETAT) {

			// Tout premier cas, on code un comportement pour attraper le premier palet et
			// le ramener
			case 0:
				System.out.println("Début de partie, lancement de la première action en dur");

//				pilote.travel(150, true);
//				for (int i = 0; i < 500; i++) {
//					Motor.C.forward();
//				}
//				Delay.msDelay(200);
//				Motor.C.stop();
//
//				for (int i = 0; i < 500; i++) {
//					Motor.C.backward();
//
//				}
//				Delay.msDelay(200);
//				Motor.C.stop();
//				
//				pilote.rotate(50);
//				pilote.travel(450);
//				pilote.rotate(-50);
//				pilote.travel(1000);
//				
//				for (int i = 0; i < 500; i++) {
//					Motor.C.forward();
//				}
//				Delay.msDelay(200);
//				Motor.C.stop();
//				
//				pilote.travel(-150);
//
//				for (int i = 0; i < 500; i++) {
//					Motor.C.backward();
//
//				}
//				Delay.msDelay(300);
//				Motor.C.stop();
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

//				
				ETAT = 1;
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
				shrek.act.mouvement(4000, true);

				if (shrek.sensor.detectPalet() == 0) {
					System.out.println("Detection d'un palet");
					shrek.act.stopPilote();

					shrek.act.choperPalet();
					
					shrek.act.tourne180(false);
					shrek.act.mouvement(5000, true);
				
				}
				
				if(shrek.sensor.detectPalet() == 2) {
					shrek.act.stopPilote();

					shrek.act.lacherPalet();
					
					ETAT = 1;
					break;
				}
				

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

}
