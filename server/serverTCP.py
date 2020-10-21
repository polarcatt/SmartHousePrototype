import socket
import pygattt

print ('started')
sock = socket.socket()

port = 11815

sock.bind(('localhost', port))
sock.listen(1)
conn, addr = sock.accept()
print ('connected: ', addr)
state = 0

adapter = pygattt.GATTToolBackend()
token = ""
mac = ""
uuid = ""

packageId = 1
st = b'U'
con = b'\xaa'

def turnOn():
	device = adapter.connect(mac)
	device.write_char(uuid, st + bytes([packageId]) + bytes[(255)] + token + con)
	print("SentBle", st + bytes([packageId]) + bytes[(255)] + token + con)
	device.write_char(uuid, st + bytes([packageId]) + bytes[(3)] + con)
	print("SentBle", st + bytes([packageId]) + bytes[(3)] + token + con)

def turnOn():
	device = adapter.connect(mac)
	device.write_char(uuid, st + bytes([packageId]) + bytes[(255)] + token + con)
	print("SentBle", st + bytes([packageId]) + bytes[(255)] + token + con)
	device.write_char(uuid, st + bytes([packageId]) + bytes[(4)] + con)
	print("SentBle", st + bytes([packageId]) + bytes[(4)] + token + con)

while True:
    data = conn.recv(1024)
    print("Received", data)
    if data == b'turnOn':
    	if state == 0:
    		turnOn()
    		state = 1
    		conn.send("turnedOn")
    	else:
    		conn.send("No")
    elif data == b'turnOff':
		if state == 0:
    		turnOff()
    		state = 0
    		conn.send("turnedOff")
    	else:
    		conn.send("No")
    elif not data:
        break
    
print("Closing Server")
conn.close()         