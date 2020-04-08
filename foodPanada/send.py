
import socket
import json

def sent(jsondata):
    gg = json.dumps(jsondata)
    ss = json.dumps(jsondata, indent=4)
    print(ss)

    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)   
    sock.connect(('localhost',5010))
    print('Client start!')

    sock.send(bytes(gg+"\n",encoding='utf-8'))
    print('sent!')
    # str  -> bytes ，send to java

    orderid = str(sock.recv(1024),encoding = 'utf-8')
    print(orderid)
    # bytes  -> str ，get from java

    sock.close() 
    print('Client end!')

    return orderid
