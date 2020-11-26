import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BOARD)
GPIO.setup(15,GPIO.OUT)

GPIO.output(15,True)
time.sleep(2)
GPIO.output(15,False)
time.sleep(2)

GPIO.cleanup()
