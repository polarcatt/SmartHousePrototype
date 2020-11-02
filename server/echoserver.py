import select
import socket
import sys
import time

port = 11815
host = "192.168.1.62"

time.sleep(1)
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind((host, port))
sock.listen(10)
print("Started on", host + ',', "port:", port)
rlist = [sock]
wlist = []
addrs = {}

i = 0
state = 0


try:
    while True:
        i += 1
        if i > 100:
            i = 0
            state = 1 - state
            print("Changed state!", state)
        toread, towrite, exc = select.select(rlist, wlist, rlist)
        #print(rlist)
        for r in toread:
            if r is sock:
                conn, addr = r.accept()
                print("New connection from " + str(addr))
                addrs[conn] = addr
                rlist.append(conn)
                if conn not in wlist:
                    wlist.append(conn)
            else:
                data = r.recv(1024)
                print("!", data)
                if len(data) != 0:
                    print("Received: " + str(data), "from", addrs[r])
                else:   
                    rlist.remove(r)
                    if r in wlist:
                        wlist.remove(r)
                    print("Connection with", "smb", "ended")
                    r.close()

        for w in towrite:
            if state:
                w.sendall(b"1")
            else:
                w.sendall(b"0")

        for e in exc:
            print("!!!")
            if e in rlist:
                rlist.remove(e)
            if e in wlist:
                wlist.remove(e)
            e.close()
        time.sleep(0.001)


except KeyboardInterrupt:
    for s in rlist:
        s.close()
    print("\nServer closed")