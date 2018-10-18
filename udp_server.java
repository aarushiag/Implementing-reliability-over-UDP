package cn;

 
//Java program to illustrate Server side 
//Implementation using DatagramSocket 
import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Scanner; 

 

public class udp_server
{ 
 
static int pointer =0;
 
 public static void main(String[] args) throws IOException 
 { 
     // Step 1 : Create a socket to listen at specified port 
	 System.out.println("Enter the port number you want to listen at :- ");
	 Scanner obj = new Scanner(System.in);
	 int port = obj.nextInt();
     DatagramSocket ds = new DatagramSocket(port); 
     byte[] receive = new byte[2048]; 
     
     DatagramPacket DpReceive = null; 
     
     for(int i=0;i<20000;i++)
     {
    	 
    	 DpReceive = new DatagramPacket(receive, receive.length); 

         // Step 3 : revieve the data in byte buffer. 
    	 
    	     // do something
    	 	try {
    		 ds.receive(DpReceive);
    		 String data = new String(receive);
    		 data = data.trim();
    		 if(Integer.parseInt(data)==pointer)
             {
            	 System.out.println("Client:-" + data);
            	 pointer++;
             }
             else
             {
            	 
            	 for(int j=0;j<Integer.parseInt(data)-pointer;j++)
            	 {System.out.println("Packet lost! :(((");}
            	 System.out.println("Client:-" + data); 
            	 pointer=Integer.parseInt(data)+1;
             }  
             
             receive = new byte[2048]; 
             ds.setSoTimeout(10000);
    	 	}
    	 	
    	 	catch(SocketTimeoutException e)
    	 	{
    	 		for(int j=pointer;j<20000;j++)
    	   		{
    	   			 System.out.println("Packet lost! :(((");
    	   		}
    	 		break;
    	 	}
    	      
    	 
     }
     
     ds.setSoTimeout(0);
     while (true) 
     { 

         // Step 2 : create a DatgramPacket to receive the data. 
         DpReceive = new DatagramPacket(receive, receive.length); 

         // Step 3 : revieve the data in byte buffer. 
         ds.receive(DpReceive); 
         String data = new String(receive);
		 data = data.trim();
         System.out.println("Client:-" + data); 

         // Exit the server if the client sends "bye" 
         if (data.equals("bye")) 
         { 
             System.out.println("Client EXITING"); 
             break; 
         } 

         // Clear the buffer after every message. 
         receive = new byte[2048]; 
     } 
 } 

} 