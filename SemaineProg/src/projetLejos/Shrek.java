package projetLejos;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Shrek {

	//Déclaration des ports qu'on va utiliser
	private static final String PORTUS = "S1";
	private static final String PORTOUCH = "S2";
	private static final String PORTCOLOR = "S3";
	private static final String PORTROUEG = "A";
	private static final String PORTROUED = "B";
	private static final String PORTPINCES = "C";
	
	//Objets qui vont nous servir tout le long
	private Senseurs sens;
	//private Actions act;
	
	//Etat qui va diriger le switch
	private static int ETAT;
	
	public Shrek() {
		Port us = LocalEV3.get().getPort(PORTUS);
		Port touch = LocalEV3.get().getPort(PORTOUCH);
		Port color = LocalEV3.get().getPort(PORTCOLOR);
		sens = new Senseurs(us, touch, color);
		
		Port roueG = LocalEV3.get().getPort(PORTROUEG);
		Port roueD = LocalEV3.get().getPort(PORTROUED);
		Port pinces = LocalEV3.get().getPort(PORTPINCES);
		//act = new Actions(roueG, roueD, pinces);

		ETAT = 0;
	}
	
	public static void main(String[] args) {
		
		//Test en dur pour la première action
		Wheel roueG = WheeledChassis.modelWheel(Motor.A, 55).offset(1.0);
		Wheel roueD = WheeledChassis.modelWheel(Motor.B, 55).offset(-1.0);
		Chassis chassis = new WheeledChassis(new Wheel[]{roueG, roueD}, WheeledChassis.TYPE_DIFFERENTIAL); 
		MovePilot pilote = new MovePilot(chassis);
		pilote.setLinearSpeed(100);
		pilote.setAngularSpeed(100);
		
		
		
		while(Button.ENTER.isUp()) {
			
			switch(ETAT) {
			
			case 0 :
				System.out.println("Début de partie, lancement de la première action en dur");
				pilote.travel(400);
				pilote.stop();
				
//				for (int i = 0; i < 2000; i++) {
//					Motor.C.forward();
//				}
//				Motor.C.close();
//				pilote.travel(150);
//				pilote.stop();
				
				for (int i = 0; i < 20000; i++) {
					Motor.C.backward();
				}
				Motor.C.stop();
				pilote.travel(100);
				
				ETAT = 1;
				break;
				
			case 1 :
				System.out.println("Fin du test");
			}
		}
	}

}
