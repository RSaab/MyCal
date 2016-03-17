
import javax.swing.JFrame.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
//import javax.swing.JList;

import org.omg.CORBA.portable.InputStream;

public class TCPClient_CXN_Manager  extends javax.swing.JFrame  {

	// a client socket //
	Socket clientSocket = null;
	private Object server_reply;
	private ObjectOutputStream outToServer;
	private ObjectInputStream inFromServer;
	private int port = 7698;
	
	
	// ******* Two constructors***********

	public TCPClient_CXN_Manager(){
		//MyCalGUI calGUI=new MyCalGUI(); 
	}

	// **********  The handler function that send requests to the server based on the GUI input *****************


	@SuppressWarnings("unused")
	public Object connectToServer(String request, Object M1, String M2, Object M3){
		try {
			/**
			 * construct the client socket, specify the IP address which is here
			 * localhost for the loopback device and the port number at which a
			 * connection needs to be initialized.
			 */
			clientSocket = new Socket("localhost", port);
			// These streams are for the same purpose as in the server side //
			outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			inFromServer = new ObjectInputStream(clientSocket.getInputStream());
			
			if(request.equals("LogIn")){
				//M1=ecrypted username and password
				outToServer.writeObject("LogIn;"+M1+"\n");	
				//last reply from the server will be :
			} 
			
			else if (request.equalsIgnoreCase("SignUp")){
				outToServer.writeObject("SignUp");
				getServerReply();
				if(server_reply.equals("send me new user account\n")){
					outToServer.writeObject(M1);
					//last reply from the server will be :
				}
				
			}else if (request.equals("Update Account")){
				outToServer.writeObject("UpdateAccount;"+M2); // **** Send me just (M1) : ecrypted username and password
				getServerReply();
				if(server_reply.equals("send me updated account\n")){
					outToServer.writeObject(M1);
					//last reply from the server will be : "UpdateAccount Successful" // "Failed to UpdateAccount"
				}
			}
			
			else if (request.equalsIgnoreCase("New Event")){
				outToServer.writeObject("NewEvent;"+M2); // **** Send me just (M1) : ecrypted username and password
				getServerReply();
				if(server_reply.equals("send me new event details\n")){
					outToServer.writeObject(M1);
					//last reply from the server will be : "NewEvent Successful" // "Failed to create a NewEvent"
				}
			}
			
			else if (request.equalsIgnoreCase("Get Events")){  
				outToServer.writeObject("GetEvents;"+M1); // **** Send me just (M1) : ecrypted username and password
				//last reply from the server will be : an array list <Event> containing the events that the client accepted and is going to
				// each event contain the list of friends that are invited and going 
			}
			else if (request.equalsIgnoreCase("GetInvitedEvents")){  
				outToServer.writeObject("GetInvitedEvents;"+M1); // **** Send me just (M1) : ecrypted username and password
				//last reply from the server will be : an array list <Event> containing the events that the client is invited to 
				// each event contain the list of friends that are invited and going
			}
			
			else if(request.equals("AcceptEvent")){  // send the server an event that should be accepted 
				outToServer.writeObject("AcceptEvent;"+M2); //M2 is encrypted username and pass
				getServerReply();
				if( server_reply.equals("Send Me Accepted Event\n")){
					outToServer.writeObject(M1);
				}
				// reply from server : Event Accept Successful // Failed to Accept Event
			}
			
			else if(request.equals("RejectEvent")){  // send the server an event that should be rejected 
				outToServer.writeObject("RejectEvent;"+M2); //M2 is encrypted username and pass
				getServerReply();
				if( server_reply.equals("Send Me Rejected Event\n")){
					outToServer.writeObject(M1);
				}
				//reply from server : Event Reject Successful // Failed to Reject Event
			}
			
			else if(request.equals("LoadFriends")){
				outToServer.writeObject("LoadFriends;"+M1); //M1 is encrypted username and pass
				outToServer.flush();
			}
			
			else if(request.equals("LoadUsernames")){
				outToServer.writeObject("LoadUsernames;"+M1); //M1 is encrypted username and pass
				outToServer.flush();
			}
			
			else if(request.equals("GetFriendRequests")){
				outToServer.writeObject("GetFriendRequests;"+M1); //M1 is encrypted username and pass
				outToServer.flush();
			}
			
			
			else if (request.equals("UpdateEvent")){
				outToServer.writeObject("UpdateEvent;"+M2); //M2 is encrypted username and pass
				getServerReply();
				if( server_reply.equals("Send Me New Event Info\n")){
					outToServer.writeObject(M1);
				}
				getServerReply();
				if( server_reply.equals("Send Me Previous Event Info\n")){
					outToServer.writeObject(M3);
				}
			}
			
			else if (request.equals("AddFriend")){
				outToServer.writeObject("AddFriend;"+M1+";"+M2); //M1 is encrypted username and pass and M2 is username of friend i want to add
			}
			
			else if (request.equals("AcceptFriendRequest")){
				outToServer.writeObject("AcceptFriendRequest;"+M1+";"+M2); //M1 is encrypted username and pass and M2 is username of friend I accepted
				outToServer.flush();
			}
			
			else if (request.equals("RejectFriendRequest")){
				outToServer.writeObject("RejectFriendRequest;"+M1+";"+M2); //M1 is encrypted username and pass and M2 is username of friend I accepted
				outToServer.flush();
			}
			
			else if (request.equals("DeleteEvent")){
				outToServer.writeObject("DeleteEvent;"+M2); //M2 is encrypted username and pass
				getServerReply();
				if( server_reply.equals("Send Me Event That Should Be Deleted \n")){
					outToServer.writeObject(M1);//M1 is event to be deleted
				}

			}
			
			else if (request.equals("DeleteAccount")){
				outToServer.writeObject("DeleteAccount;"+M2); //M2 is encrypted username and pass
				getServerReply();
				if( server_reply.equals("Send Me Account That Should Be Deleted \n")){
					outToServer.writeObject(M1);//M1 is Account to be deleted
				}

			}
			
			
			
			else {
				server_reply= "Invalid request";
			}
			
			
			
			/**get the last reply from server, close connection and return to calling method**/
			
			try {
				server_reply = inFromServer.readObject();
				clientSocket.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			
		} catch (Exception ex) {// catch the exceptions //
			ex.printStackTrace();
		}
		return server_reply;
	}
	
	private void getServerReply(){
		try {
			server_reply = inFromServer.readObject();
			//System.out.println(server_reply);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
