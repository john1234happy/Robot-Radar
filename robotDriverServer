
import time
import socket
import gopigo
from di_sensors.distance_sensor import DistanceSensor


# Listen on localhost at port 5005
TCP_IP = '192.168.1.11'
TCP_PORT = 1111
BUFFER_SIZE = 40
ds = DistanceSensor()
ds.start_continuous()
i = 1

# Create a TCP socket and bind the server to the port
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((TCP_IP, TCP_PORT))
print("Server Started")

while i == 1:
	#Wait for incomming connections
	s.listen(1)
	
	#Accept an incoming connection
	conn, addr = s.accept()
	
	print ('\nConnection address:', addr)
	while 1:
		#Check the data
		data = conn.recv(BUFFER_SIZE)
		if not data: break	
		print("received data:", data)
		#print "data[data.find('moveForward'):data.find('}')-1]: ", data[data.find("moveForward"):data.find("}")-1]



		if data[data.find("moveForward"):data.find("}")-1] == "moveForward":
			gopigo.fwd()
			conn.send("Moving forward")

		elif data[data.find("halt"):data.find("}")-1] == "halt":
			gopigo.stop()
			conn.send("Stopping")

		elif data[data.find("moveBackward"):data.find("}")-1]=="moveBackward":
			gopigo.bwd()
			conn.send("Moving back")

		elif data[data.find("rotateLeft"):data.find("}")-1]=="rotateLeft":
			gopigo.left()
			conn.send("Turning left")

		elif data[data.find("rotateRight"):data.find("}")-1]=="rotateRight":
			gopigo.right()
			conn.send("Turning right")

		elif data[data.find("increaseSpeed"):data.find("}")-1]=="increaseSpeed":
			gopigo.increase_speed()
 			conn.send("Increase speed")

		elif data[data.find("decreaseSpeed"):data.find("}")-1]=="decreaseSpeed":
			gopigo.decrease_speed()
			conn.send("Decrease speed")
		
		elif data[data.find("resetSpeed"):data.find("}")-1]=="resetSpeed":
			gopigo.set_speed(100)
			conn.send("Speed reset")

		elif data[data.find("turnHead"):data.find(",")-1] == "turnHead":
			tempAngle = data[data.rfind(":")+1:data.find("}")]
			gopigo.servo(int(tempAngle))
			conn.send("turning head")

		elif data[data.find("resetHead"):data.find(",")-1] == "resetHead":
			gopigo.servo(90)
			conn.send("resetting head")

		elif data[data.find("scan"):data.find("}")-1] == "scan":
			t = 0
			temp = "0"
			dotlist = []
			while t <= 180 :
				gopigo.servo(t)
				t = t+1
				read_distance = ds.read_range_continuous()
				temp = str(read_distance)
				dotlist.append(temp)
				dotlist.append(":")
				print("distance Sensor Reading: {} mm: ".format(read_distance))
			while t >= 0 :
				gopigo.servo(t)
				t = t-1
				read_distance = ds.read_range_continuous()
				#conn.send(str(read_distance))
				print("distance Sensor Reading: {} mm: ".format(read_distance))
			temp = ''.join(dotlist)
			print(temp)
			conn.send("scan complete. Distances in mm: " + temp)
			gopigo.servo(90)
		elif data[data.find("stopServer"):data.find("}")-1] == "stopServer":
			print i
			i = 0
			print i
			#conn.send("stopping server")
		else:
			print("Invalid command")
			conn.send("Invalid command")
	conn.close()
