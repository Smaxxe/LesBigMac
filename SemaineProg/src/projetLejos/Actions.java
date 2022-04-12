package projetLeJOS.projet;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;



public class Actions {

	MovePilot pilote;
	RegulatedMotor pince;

	// constructeur, instancie 3 moteurs ( attributs)
	public Actions() {
		/*
		 * Wheel roueG = WheeledChassis.modelWheel(Motor.A, 55).offset(1.0); Wheel roueD
		 * = WheeledChassis.modelWheel(Motor.B, 55).offset(-1.0); Chassis chassis = new
		 * WheeledChassis(new Wheel[]{roueG, roueD}, WheeledChassis.TYPE_DIFFERENTIAL);
		 */
		this.pilote = new MovePilot(55, 55, Motor.A, Motor.B); // marche bien
		this.pince = new EV3MediumRegulatedMotor(MotorPort.C); // ?

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
		pilote.rotate(150, enCours); // 180 marche pas de ouf, 150 fait un 180
	}

	public void tourne(int angle, boolean enCours) {
		pilote.rotate(angle, enCours);
	}

	public void ouvrirPinces() {
		pince.backward();
		Delay.msDelay(500);
		pince.stop();
	}

	// faire deux méthodes : fermeture avec palet et sans palet

	public void fermerPincesSansPalet() {
		pince.forward();
		Delay.msDelay(400);
		pince.stop();
	}

	public void fermerPincesAvecPalet() {
		pince.forward();
		Delay.msDelay(500);
		pince.stop();
	}

	public static void main(String[] args) {
		
	}

}
