import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522

import psycopg2 as psql

reader = SimpleMFRC522()

try:
	id, text = reader.read()
	print(id)
	print(text)

	try:
		connection = psql.connect( user = "postgres", password = "root", host = "localhost", port = "5432", database = "your-package")
		cursor = connection.cursor()
		print("Conexion Postgresql abierta")
		query = "INSERT INTO history (card_number) VALUES ('%i')" % id
		print(query)
		cursor.execute(query)
		connection.commit()


	except (Exception, psql.DatabaseError) as error:
		print(error)

	finally:
		if(connection):
			cursor.close()
			connection.close()
			print("Conexion Postgresql cerrada")
finally:
	GPIO.cleanup()
