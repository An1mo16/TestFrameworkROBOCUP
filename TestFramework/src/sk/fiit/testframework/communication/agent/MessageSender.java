package sk.fiit.testframework.communication.agent;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class MessageSender{
	Socket requestSocket;
    ObjectOutputStream out;
    static int port = 3541;
    String message;
    MessageSender(){}
    public void run()
    {
        try{
            requestSocket = new Socket("localhost", port);
            System.out.println("Connected to localhost in port " + port);
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
            Logger.getGlobal().info("client>" + msg);
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
