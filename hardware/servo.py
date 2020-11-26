import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BOARD)
GPIO.setup(11,GPIO.OUT)

servo = GPIO.PWM(11,50)

servo.start(2)
print("Espere ...")
time.sleep(1)

#duty = 2
#while duty <= 12:
servo.ChangeDutyCycle(7)
	#duty = duty + 1
time.sleep(2)

servo.stop()
GPIO.cleanup()
