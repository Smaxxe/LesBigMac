package projetLejos;

import lejos.hardware.port.Port;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class TestSensorUS {

	public static void main(String[] args) {
		
		Port p = LocalEV3.get().getPort("S1");
		
		int res = SenseurDistance.detect(p);
		
		if(res == 0) {
			for(int i = 0 ; i < 300 ; i++) {
				System.out.println("Un palet !");
			}
		} else if(res == 1) {
			for(int i = 0 ; i < 300 ; i++) {
				System.out.println("Repositionnement !");
			}
		} else if (res == 2) {
			for(int i = 0 ; i < 300 ; i++) {
				System.out.println("Un mur !");
			}
		} else {
			for(int i = 0 ; i < 300 ; i++) {
				System.out.println("Erreur !");
			}
		}
	}

}
