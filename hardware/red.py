import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BOARD)
GPIO.setup(13,GPIO.OUT)

GPIO.output(13,True)
time.sleep(2)
GPIO.output(13,False)
time.sleep(2)

GPIO.cleanup()
