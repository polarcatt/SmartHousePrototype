import socket

sock = socket.socket()
sock.bind(("192.168.1.62", 11815))
sock.listen(1)
print("Started")
con, addr = sock.accept()
print("Connected")
i = 0
state = 0

while 1:
	data = con.recv(1024)
	print(data)
	i += 1
	if i > 5000:
		i = 0
		state = 1 - state
	if state:
		con.send(b"On")
	else:
		con.send(b"Off")