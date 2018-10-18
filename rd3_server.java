//client to Server objects reaching along with seq num
package cn;

 
import java.io.ByteArrayInputStream;
//Java program to illustrate Server side 
//Implementation using DatagramSocket 
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Scanner; 

 

class object implements Serializable
{
	String message;
	int seq_num;
	int window_size;
	public object(String message,int seq_num,int window_size)
	{
		this.message=message;
		this.seq_num=seq_num;
		this.window_size = window_size;
	}

}

public class rd3_server
{ 
 
static int pointer =0;
static int window_size =0;
 
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

         
    	 	try {
	    		 ds.receive(DpReceive);
	    		 ByteArrayInputStream in = new ByteArrayInputStream(receive);
	    		 ObjectInputStream is = new ObjectInputStream(in);
	    		 
	    		object packet = (object)is.readObject();   
	    		String data = packet.message;
	    		window_size = packet.window_size;
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
    	 	 catch (ClassNotFoundException e) 
    	 	 {
    	 		 e.printStackTrace();
    		 }
//    		 String data = new String(receive);
//    		 data = data.trim();   		 
    		 
    	      
    	 
     }
     
     ds.setSoTimeout(0);
     while (true) 
     { 
 
    	 DpReceive = new DatagramPacket(receive, receive.length); 
    	 
    	 try 
    	 {
    		ds.receive(DpReceive);
    		ByteArrayInputStream in = new ByteArrayInputStream(receive);
    		ObjectInputStream is = new ObjectInputStream(in);
    		 
    		object packet = (object)is.readObject();   
    		String data = packet.message;
    		
    		if (data.equals("bye")) 
    	    { 
    	        System.out.println("Client EXITING"); 
    	        break; 
    	    } 
    		
    		System.out.println("Client:-" + data);
    		             
    		receive = new byte[2048]; 
    		 
		 }
	 	 
	 	 catch (ClassNotFoundException e) 
	 	 {
	 		 e.printStackTrace();
		 }
    	 
     } 
 } 

} 