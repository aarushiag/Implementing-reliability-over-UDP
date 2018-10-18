package cn;


//Java program to illustrate Client side 
//Implementation using DatagramSocket 
import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.util.Scanner; 

public class udp_client
{ 
 public static void main(String args[]) throws IOException 
 { 
     Scanner sc = new Scanner(System.in); 
     
     System.out.println("Enter the port number you want to connect at :- ");
	 int port = sc.nextInt();
	 sc.nextLine();
	 System.out.println("Enter the ip address you want to connect at :- ");
	 String ip_addr = sc.nextLine();
 
     DatagramSocket ds = new DatagramSocket(); 


     InetAddress ip = InetAddress.getByName(ip_addr); 
     


    // loop while user not enters "bye" 
     for (int i=0;i<20000;i++)
	 {
		 String inp = Integer.toString(i);
		 byte buf[] = inp.getBytes(); 
         DatagramPacket DpSend = 
               new DatagramPacket(buf, buf.length, ip, port); 
         ds.send(DpSend); 
         
	 }
     while (true) 
     { 
         String inp = sc.nextLine(); 
         byte buf[] = inp.getBytes(); 
         DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, port);  
         ds.send(DpSend); 

         if (inp.equals("bye")) 
             break; 
     } 
 } 
} 