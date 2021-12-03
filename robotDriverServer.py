import time
import socket
import gopigo
import fcntl
import struct
import os

from di_sensors.distance_sensor import DistanceSensor

def get_ip_address(ifname):
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    return socket.inet_ntoa(fcntl.ioctl(
        s.fileno(),
        0x8915,  # SIOCGIFADDR
        struct.pack('256s', ifname[:15])
    )[20:24])



# Listen on localhost at port 1111
TCP_IP = get_ip_address('wlan0')
TCP_PORT = 1111
BUFFER_SIZE = 40
ds = DistanceSensor()
ds.start_continuous()
i = 1

cmd = '/home/pi/test/Robot-Radar/ustream &'
os.system(cmd)

print(TCP_IP + "\n")
# Create a TCP socket and bind the server to the port
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((TCP_IP, TCP_PORT))
print("Server Started")



while i == 1:
	gopigo.servo(90)
	gopigo.set_left_speed(72)
	gopigo.set_right_speed(150)

	#Wait for incomming connections
	s.listen(1)
	
	#Accept an incoming connection
	conn, addr = s.accept()
	
	print ('\nConnection address:', addr)
	while i==1:
		#Check the data
		data = conn.recv(BUFFER_SIZE)
		if not data: break	
		print("received data:", data)
		
		if data[data.find("moveForward"):data.find("}")-1] == "moveForward":
			gopigo.fwd()
			conn.sendall("Moving forward\n")

		elif data[data.find("halt"):data.find("}")-1] == "halt":
			gopigo.stop()
			conn.sendall("Stopping\n")

		elif data[data.find("moveBackward"):data.find("}")-1]=="moveBackward":
			gopigo.bwd()
			conn.send("Moving back\n")

		elif data[data.find("rotateLeft"):data.find("}")-1]=="rotateLeft":
			gopigo.left()
			conn.send("Turning left\n")

		elif data[data.find("rotateRight"):data.find("}")-1]=="rotateRight":
			gopigo.right()
			conn.send("Turning right\n")

		elif data[data.find("increaseSpeed"):data.find("}")-1]=="increaseSpeed": # additional command the could be implemted on GUI
			gopigo.increase_speed()
 			conn.send("Increase speed\n")

		elif data[data.find("decreaseSpeed"):data.find("}")-1]=="decreaseSpeed": # additional command the could be implemted on GUI 
			gopigo.decrease_speed()
			conn.send("Decrease speed\n")
			conn.close()

		elif data[data.find("resetSpeed"):data.find("}")-1]=="resetSpeed": # additional command the could be implemted on GUI
			gopigo.set_speed(100)
			conn.send("Speed reset\n")

		elif data[data.find("turnScan"):data.find(",")-1] == "turnScan":
			temp = "0"
			temp1 = "{'Dot':{'distance':"
			temp2 = ",'angle':"
			temp3 = "}}\n"
			tempAngle = data[data.rfind(":")+1:data.find("}")]
			if(int(tempAngle)<175 and int(tempAngle)>5):
				gopigo.servo(int(tempAngle))
			time.sleep(.5)
			read_distance = ds.read_range_continuous()
			temp = str(read_distance)
			conn.sendall(temp1 + temp + temp2 + str(tempAngle) + temp3)

		elif data[data.find("resetHead"):data.find(",")-1] == "resetHead":
			gopigo.servo(90)
			conn.send("resetting head\n")

		elif data[data.find("scan"):data.find("}")-1] == "scan":
			t = 6
			temp = "0"
			temp1 = "{'Dot':{'distance':"
			temp2 = ",'angle':"
			temp3 = "}}\n"
			dotlist = []
			while t <= 174 :
				gopigo.servo(t)
				read_distance = ds.read_range_continuous()
				temp = str(read_distance)
				conn.sendall(temp1 + temp + temp2 + str(t) + temp3)
				t = t+2
			while t >= 6 :
				gopigo.servo(t)
				read_distance = ds.read_range_continuous()
				temp = str(read_distance)
				conn.sendall(temp1 + temp + temp2 + str(t) + temp3)
				t = t-2
			temp = ''.join(dotlist)
			gopigo.servo(90)

		elif data[data.find("stopServer"):data.find("}")-1] == "stopServer":
			i = 0
			conn.send("stopping server\n")
		else:
			conn.send("Invalid command\n")
	conn.close()
