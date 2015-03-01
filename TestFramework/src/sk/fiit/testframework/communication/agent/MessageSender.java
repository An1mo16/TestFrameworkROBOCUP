package sk.fiit.testframework.communication.agent;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageSender{
	Socket requestSocket;
    ObjectOutputStream out;

    String message;
    MessageSender(){}
    public void run()
    {
        try{
            //1. creating a socket to connect to the server
            requestSocket = new Socket("localhost", 3072);
            System.out.println("Connected to localhost in port 3072");
            //2. get Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();

        }
        catch(UnknownHostException unknownHost){
            System.err.println("You are trying to connect to an unknown host!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
      
    }
    void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("client>" + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    void close(){
    	  try {
    		 //sendMessage("close");
			out.close();
			requestSocket.close();
    	  } catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
    }
}
