// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the server.
   */
  	ChatIF serverUI; 
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI)
  {
    super(port);
    this.serverUI = serverUI;
  }
  

  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    serverUI.display("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
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
    	  } 
    	else 
    	  { 
    		serverUI.display(message);
    		sendToAllClients("SERVER MSG>" + message);}
    }
    catch(IOException e)
    {
      serverUI.display
        ("Could not send message.");
    }
  }
  
  private void handleCommands(String cmd) throws IOException {
	  if(cmd.equals("#quit")) {
		  this.close();
		  //i would guess quit() would also work here
	  }
	  else if(cmd.equals("#stop")){
		  this.stopListening();
	  }
	  else if(cmd.equals("close")) {
		  this.stopListening();
		  this.close();
	  }
	  else if (cmd.startsWith("#setport")) {
		  //check if client is disconnected
		  //account for exception 
		  String[] instruction = cmd.split(" ");
		  try {
			  this.setPort(Integer.parseInt(instruction[1]));
		  }
		  catch (Exception e ) {
			  serverUI.display("Port value invalid");
		  }
		  
	  }
	  else if(cmd.equals("#start")) {
		  if(!this.isListening()) {
			  try {
				  this.listen();
			  } catch (IOException e) {
				  serverUI.display("Error occurred listening for clients");
			  }
		  } else {
			  serverUI.display("Already started.");
		  }
	  }
	  else if(cmd.equals("#getport")) {
		  serverUI.display("Port: " + Integer.toString(this.getPort()));
	  }
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    serverUI.display
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    serverUI.display
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  /**
   * The implementation of the hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) 
  {
	  serverUI.display("New client connected.");
  }

  /**
   * The implementation of the method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  @Override
  synchronized protected void clientDisconnected(
    ConnectionToClient client) 
  {
	  serverUI.display("Client has disconnected.");		
  }

  
}
//End of EchoServer class
