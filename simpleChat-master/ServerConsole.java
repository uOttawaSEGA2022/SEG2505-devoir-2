import common.ChatIF;
import java.io.*;
import java.util.Scanner;

import client.ChatClient;

public class ServerConsole implements ChatIF {
	
	EchoServer server;
	
	Scanner fromConsole;
	
	final public static int DEFAULT_PORT = 5555;
	 
	  //Constructors ****************************************************

	  /**
	   * Constructs an instance of the ClientConsole UI.
	   *
	   * @param host The host to connect to.
	   * @param port The port to connect on.
	   */
	  public ServerConsole(int port) 
	  {
	    server = new EchoServer(port, this);
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
	        server.handleMessageFromServerUI(message);
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
	  
	  
	  public static void main(String[] args) 
	  {
	    int port = 0;

	    try
	    {
	      port = Integer.parseInt(args[1]);
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      port = DEFAULT_PORT;
	    }
	    catch(NumberFormatException f) //in case someone types a string for port--cant be converted to int
	    {
	      port = DEFAULT_PORT;
	    }
	    
	    ServerConsole sv = new ServerConsole(port);
	    
	    try
	    {
	    	sv.server.listen();
	    }
	    catch (Exception e) {
	    	System.out.println("Cannot listen for clients");
	    	e.printStackTrace();
	    }
	    
	    sv.accept();  //Wait for console data
	  }
	  
}
//End of ServerConsole class	  