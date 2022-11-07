import common.ChatIF;
import java.io.*;
import java.util.Scanner;

import client.ChatClient;

public class ServerConsole implements ChatIF {
	
	EchoServer server;
	
	Scanner fromConsole;
	
	  //Constructors ****************************************************

	  /**
	   * Constructs an instance of the ClientConsole UI.
	   *
	   * @param host The host to connect to.
	   * @param port The port to connect on.
	   */
	  public ServerConsole(int port) 
	  {
	    server = new EchoServer(port);
	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
	  }

	  //Instance methods ************************************************
	  
	  /**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the client's message handler.
	   */
	  public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }

	  /**
	   * This method overrides the method in the ChatIF interface.  It
	   * displays a message onto the screen.
	   *
	   * @param message The string to be displayed.
	   */
	  public void display(String message) 
	  {
	    System.out.println("> " + message);
	  }
	  
	  /**
	   * This method handles all data coming from the UI            
	   *
	   * @param message The message from the UI.    
	   */
	  public void handleMessageFromServerUI(String message)
	  {
	    try
	    {
	    	if(message.startsWith("#")){
	    		handleCommands(message);
	    	  } else { server.sendToAllClients("SERVER MSG>" + message);}
	    }
	    catch(IOException e)
	    {
	      display
	        ("Could not send message.");
	    }
	  }
	  
	  private void handleCommands(String cmd) throws IOException {
		  if(cmd.equals("#quit")) {
			  server.close();
		  }
		  else if(cmd.equals("#stop")){
			  server.stopListening();
		  }
	  }
	  

}
//End of ServerConsole class	  