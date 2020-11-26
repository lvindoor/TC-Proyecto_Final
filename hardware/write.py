import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522

reader = SimpleMFRC522()

try:
	text = input('Nueva Informacion: ')
	print("Coloque la etiqueta para grabar")
	reader.write(text)
	print("Grabado")
finally:
	GPIO.cleanup()
