import socket
import pygattt

print ('started')
sock = socket.socket()

port = 11815
port2 = 11814

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

ans = ""

def turnOn():
	device = adapter.connect(mac)
	device.write_char(uuid, st + bytes([packageId]) + bytes([255]) + token + con)
	device.write_char(uuid, st + bytes([packageId]) + bytes([3]) + con)

def turnOff():
	device = adapter.connect(mac)
    device.write_char(uuid, st + bytes([packageId]) + bytes([255]) + token + con)
    device.write_char(uuid, st + bytes([packageId]) + bytes([4]) + con)

def read_func():
    pass

def getState():
    device = adapter.connect(mac)
    device.write_char(uuid, st + bytes([packageId]) + bytes([255]) + token + con)
    device.write_char(uuid, st + bytes([packageId]) + bytes([6]) + con)
    device.subscribe(read_uuid, callback = read_func)

while True:
    data = conn.recv(1024)
    print("Received", data)
    if data == b'turnOn':
    	turnOn()
    	conn.send("turnedOn")
    elif data == b'turnOff':
    	turnOff()
    	conn.send("turnedOff")
    elif data == b'getState':
        turnOff()
        while len(ans) == 0:
            pass
        conn.send(ans)
        ans = ""
    
print("Closing Server")
conn.close()         