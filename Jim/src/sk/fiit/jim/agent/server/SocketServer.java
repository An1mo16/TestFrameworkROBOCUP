package sk.fiit.jim.agent.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import sk.fiit.jim.Settings;
import sk.fiit.jim.agent.communication.Communication;

public class SocketServer implements Runnable {
	ServerSocket providerSocket;
    Socket connection = null;
    ObjectInputStream in;
    String message;
    public SocketServer(){}
    public void run()
    {
        try{
         
            providerSocket = new ServerSocket(3072, 10);
            
            System.out.println("Waiting for connection");
            connection = providerSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
           
         
            in = new ObjectInputStream(connection.getInputStream());

            
            do{
                try{
                    message = (String)in.readObject();
                    System.out.println("client>" + message);
                    String[] args = message.split("\\n");
                	Settings.parseCommandLine(args);
            		Settings.setCommandLineOverrides();
            		Communication.getInstance().addToMessage("(beam " + Double.toString(0.0) + " " + Double.toString(0.0) + " 0.0)");
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }while(!message.equals("close"));
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            
            try{
                in.close();
                providerSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
}
