package projetLejos;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;



public class Actions {

	private static final NXTRegulatedMotor PORTROUEG = Motor.A;
	private static final NXTRegulatedMotor PORTROUED = Motor.B;
//	private static final Port PORTPINCES = LocalEV3.get().getPort("C");
	
	MovePilot pilote;
	RegulatedMotor pince;

	// constructeur, instancie 3 moteurs ( attributs)
	public Actions() {
		
		this.pilote = new MovePilot(55, 55, 145, PORTROUED, PORTROUEG, false); // marche bien
		this.pilote.setLinearSpeed(300);
		this.pilote.setAngularSpeed(200);
		
		this.pince = Motor.C; 
		this.pince.setSpeed(2000);
		

	}

	// Les méthodes
	public void mouvement(int distance, boolean enCours) {
		pilote.travel(distance, enCours);
		// 2e paramètre enCours permet de mettre true/false selon si on veut faire une
		// autre action en même temps qu'avancer
	}
	
	public void tourne360(boolean enCours) {
		pilote.rotate(300, enCours);
	}

	public void tourne180(boolean enCours) {
		pilote.rotate(150, enCours);
	}

	public void tourne(int angle, boolean enCours) {
		pilote.rotate(angle, enCours);
	}
	
	public void ouvrirPinces(boolean enCours) {
		pince.rotate(1000, enCours);
	}

	// faire deux méthodes : fermeture avec palet et sans palet

	public void fermerPinces(boolean enCours) {
		pince.rotate(-1000, enCours);
	}

	public void stopPilote() {
		pilote.stop();
	}
	
	public void choperPalet() {
		ouvrirPinces(false);
		mouvement(180, false);
		fermerPinces(false);
	}
	
	/** Méthode qui permet de lacher complètement un palet, donc ouvrir, reculer et fermer
	 * Puis se tourner
	 * 
	 */
	public void lacherPalet() {
		ouvrirPinces(false);
		mouvement(-200, false);
		fermerPinces(false);
		tourne180(false);
	}

	public boolean isMovingPilote() {
		return pilote.isMoving();
	}
}