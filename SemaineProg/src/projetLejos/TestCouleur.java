package projetLejos;

import javax.sound.sampled.Port;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class TestCouleur {

	public static void main(String args[]) {
		
		System.out.println("Test du senseur de distance ");
		// get a port instance
		lejos.hardware.port.Port port = LocalEV3.get().getPort("S2");

		// Get an instance of the Ultrasonic EV3 sensor
		SensorModes sensor = new EV3UltrasonicSensor( (lejos.hardware.port.Port) port);

		// get an instance of this sensor in measurement mode
		SampleProvider distance = sensor.getMode("Distance");

		// initialize an array of floats for fetching samples.
		// Ask the SampleProvider how long the array should be
		float[] sample = new float[distance.sampleSize()];

		// fetch a sample
		int test = 40;
		while (test > 0.05) {
			distance.fetchSample(sample, 0);
			test = distance.sampleSize();
			System.out.println("Bjr " + test);
		}
		
		System.exit(1);

	}
}
