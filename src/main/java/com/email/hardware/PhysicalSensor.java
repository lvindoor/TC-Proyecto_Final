package com.email.hardware;

public class PhysicalSensor {
	
	public void on(String message) {

		try {
			System.out.println("Corriendo archivo -> " + message + "...");

			// Valida el sensor a controlar
			switch (message) {
			
				case "READ-TAG":
					Runtime.getRuntime().exec("python3 hardware/read.py");
					break;
	
				case "OPEN-BOX":
					Runtime.getRuntime().exec("python3 hardware/servo.py");
					break;
					
				case "VALID":
					Runtime.getRuntime().exec("python3 hardware/green.py");
					break;
					
				case "INVALID":
					Runtime.getRuntime().exec("python3 hardware/red.py");
					break;
	
				case "INSUFFICIENT":
					Runtime.getRuntime().exec("python3 hardware/yellow.py");
					break;
					
				default:
					System.out.println("The message : " + message + ". Not exits");
			}

			// Se corren estos archivos para habilitar pines de GPIO en raspberry
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception ocurred: " + e.getMessage());
		}

	}

}
