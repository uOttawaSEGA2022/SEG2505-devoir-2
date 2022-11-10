// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI;
  
  String loginid;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginid, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginid = loginid;
    openConnection();
    this.sendToServer("#login" + loginid);
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if(message.startsWith("#")){
    		handleCommands(message);
    	  } else { sendToServer(message);}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommands(String cmd) throws IOException {
	  if(cmd.equals("#quit")) {
		  this.closeConnection();
		  clientUI.display("The client will quit.");
		  quit();
	  }
	  else if (cmd.equals("#logoff")) {
		  if(this.isConnected()) { this.closeConnection();}
		  else {
			  clientUI.display("Client is already disconnected");
		  }
	  }
	  
	  else if (cmd.startsWith("#setport")) {
		  //check if client is disconnected
		  //account for exception 
		  String[] instruction = cmd.split(" ");
		  try {
			  this.setPort(Integer.parseInt(instruction[1]));
		  }
		  catch (Exception e ) {
			  clientUI.display("Port value invalid");
		  }
		  
	  }
	  
	  else if (cmd.startsWith("#sethost")) {
		  //see above
		  String[] instruction = cmd.split(" ");
		  try {
			  this.setHost(instruction[1]);
		  }
		  catch (Exception e ) {
			  clientUI.display("Host value invalid");
		  }
	  }
	  
	  else if (cmd.equals("#login")) {
		  //checks if client isn't connected already
		  //else error msg
		  if(!this.isConnected()) {
			  this.openConnection();
		  }else {
			  clientUI.display("The client is already connected to the server!");
		  }
	  }
	  
	  
	  else if (cmd.equals("#getport")) {
		  clientUI.display(Integer.toString(getPort()));
	  }
	  else if (cmd.equals("#gethost")) {
		  clientUI.display(getHost());
	  }
  }
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  /**
	 * Implementation of the hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  @Override
	protected void connectionClosed() {
	  clientUI.display("The connection has been closed.");
	}

	/**
	 * Implementation of the hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  @Override
	protected void connectionException(Exception exception) {
	  exception.printStackTrace();
	  clientUI.display("The server has shut down.");
	  System.exit(0);
  }
  
 
  
  
  
  
}
//End of ChatClient class
