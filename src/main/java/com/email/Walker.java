package com.email;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

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

			while (true) { // comenzamos el hilo

				System.out.println("INICIALIZANDO EL HILO N° " + cont);
				cont++;

				/* Esperamos un usuario con Tarjeta */
				PhysicalSensor sensor = new PhysicalSensor(); // instanciamos clase
				sensor.on("READ-TAG"); // le pedimos que encienda el lector de tarjetas

				/* Descansamos 3 segundos */
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}

				/* Ejecutar consulta */
				ResultSet rs = stmt.executeQuery("SELECT * FROM history");

				/* Obtener consulta */
				while (rs.next()) {

					/* Obtenemos el numero de la tarjeta */
					String cardNumber = rs.getString("card_number");
					System.out.println(cardNumber); // lo imprimimos

					/* Ejecutar consulta */
					ResultSet rs0 = stmt.executeQuery("SELECT * FROM users WHERE card_number = '" + cardNumber + "'");
					
					/* Obtener consulta */
					while (rs0.next()) {
						/* Obtenemos datos del usuario */
						int id = rs0.getInt("id");
						String name = rs0.getString("firstname");
						String email = rs0.getString("email");
						boolean onHold = true;

						/* Ejecutar consulta */
						ResultSet rs1 = stmt.executeQuery("SELECT * FROM packages WHERE id_user = " + id + "");
						
						/* Obtener consulta */
						while (rs1.next()) { // obtenemos el primer paquete

							sensor.on("VALID"); // mostramos luz verde
							
							/* Descansamos 3 segundos */
							try {
								TimeUnit.SECONDS.sleep(3);
							} catch (InterruptedException ie) {
								Thread.currentThread().interrupt();
							}

							sensor.on("OPEN-BOX"); // abrimos la caja

							/* Descansamos 7 segundos */
							try {
								TimeUnit.SECONDS.sleep(7);
							} catch (InterruptedException ie) {
								Thread.currentThread().interrupt();
							}
							
							onHold = false; // Ya no esta a la espera

							try { // Intentamos mandar el correo
								/* Mandamos correo de recibido */
								SendEmail sendEmail = new SendEmail(); // instanciamos correo
								sendEmail.run(name, email, rs1);
								/* Borramos el historial */
								Statement stmt2 = connection.createStatement();
								stmt2.execute("DELETE FROM history");
							} catch (IOException | InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						/* ¿No hay paquetes para el usuario? */
						if (onHold) { // ¿Hay en espera?
							sensor.on("INSUFFICIENT"); // mostramos luz amarilla
							/* Descansamos 3 segundos */
							try {
								TimeUnit.SECONDS.sleep(3);
							} catch (InterruptedException ie) {
								Thread.currentThread().interrupt();
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
