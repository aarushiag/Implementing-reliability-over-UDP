# Implementing-reliability-over-UDP


# Problem Statement

1. Create a socket connection using UDP. You should be able to send a number of packets (e.g. 1000) and on the receiving side, you should be able to show the received messages as well as lost messages? (3 marks). You may use the code submitted in assignment 1 and modify it. 
2. Implement reliable data communication on top of this basic UDP communication. As a result, the receiver must receive all the packets and in the order that they have beens sent. You will have to implement:
a) Timeouts (2 marks)
b) Acknowledgements ( 2 marks)
c) Retransmissions (2 marks)
d) Bufferring at receiver's end for in-order delivery (1 mark)
e) Receiver window to manage the flow (2 marks)
f) It must not be a "hold and wait" protocol i.e. only one message is being sent and next message is sent only after first message has been received correctly. The Window size should be "n" and taken as parameter in the beginning of running your program (3 marks)
