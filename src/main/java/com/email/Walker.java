package com.email;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.email.hardware.PhysicalSensor;
import com.email.send.SendEmail;

public class Walker extends Thread {

	private int cont = 0; // contador de hilos

	public void run() { 
		
		try {	
			/* Conexion PostgreSQL */
			Connection connection = DriverManager
					.getConnection("jdbc:postgresql://localhost:5432/your-package?user=postgres&password=root");
			Statement stmt = connection.createStatement();
		
			while(true) { // comenzamos el hilo
				
				System.out.println("INICIALIZANDO EL HILO N° " + cont);
				
				/* Esperamos un usuario con Tarjeta */
				PhysicalSensor sensor = new PhysicalSensor(); // instanciamos clase
				sensor.on("READ-TAG"); // le pedimos que encienda el lector de tarjetas
				
				/* Ejecutar consulta */
				ResultSet rs = stmt.executeQuery("SELECT * FROM history");
				
				/* Obtener consulta */
				while ( rs.next() ) {
					
					/* Obtenemos el numero de la tarjeta */
	                String cardNumber = rs.getString("card_number");
	                System.out.println(cardNumber);
	                
	                /* Primer estado de aceptacion */
					if(cardNumber == "") { // No registrada
						sensor.on("INVALID"); // mostramos la luz roja
					
					} else { // Hay registro
						
						/* Ejecutar consulta */
						ResultSet rs0 = stmt.executeQuery("SELECT * FROM users WHERE card_number = '" + cardNumber + "'");
						/* Obtener consulta */
						while ( rs0.next() ) { 
							/* Obtenemos id */
							int id = rs0.getInt("id");
							String name = rs0.getString("firstname");
							String email = rs0.getString("email");
							/* Ejecutar consulta */
							ResultSet rs1 = stmt.executeQuery("SELECT * FROM packages WHERE id_user = " + id + "");
							/* Obtener consulta */
							while ( rs1.next() ) { // obtenemos el primer paquete
								/* Obtenemos datos de paquetes del usuario */
								String idUser = rs1.getString("id_user");
								
								if(idUser == "" ) { // ¿No hay paquetes para el usuario?
									sensor.on("INSUFFICIENT"); // mostramos luz amarilla
								} else {
									sensor.on("VALID"); // mostramos luz verde
									sensor.on("OPEN-BOX"); // abrimos la caja
									/* Mandamos correo de recibido */
									SendEmail sendEmail = new SendEmail(); // instanciamos correo
									
									try { // Intentamos mandar el correo
										/* Borramos el historial */
										stmt.executeQuery("DELETE FROM history");
										sendEmail.run(name,email,rs1);
									} catch (IOException | InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
					}
					
					
					
	               
	            }
			}	
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
