package com.email;

import java.util.concurrent.ExecutorService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	// Servicio para el Daemon
	private ExecutorService executor;

	public static void main(String[] args) {
		/* Corre Servicios */
		Walker walker = new Walker();
		walker.run(); /* Start */
		
	}

	/** Metodos para Daemon **/

	public void start() {
		// Run Daemon
		Walker demon = new Walker();
		demon.run(); /* Start */
	}

	public boolean shutDown() {
		executor.shutdown();
		return true;
	}

}
