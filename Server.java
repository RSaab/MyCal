
//Multithreaded Server Implementation

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.mail.*;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

//import javax.mail.*;
//import javax.mail.PasswordAuthentication;
//import javax.mail.internet.*;

public class Server {

	private static final int port = 7698 ;
	private ServerSocket serverSocket;
	protected Database serverDatabase;
	private DesEncrypter encrypter;
	private Log_Handler log;
	protected final String user; // ENTER YOUR OWN EMAIL
	protected final String pass; // ENTER YOUR OWN PASSWORD
	protected final String from; // ENTER YOUR OWN EMAIL
	protected final String fromDisplayName = "MyCal";
	protected String to;
	protected Properties props;
	//private Database serverDatabase;//used to connect with the database
	//array of users and ports

	//Constructor Class
	public Server(){
		serverDatabase = new Database();
		encrypter= new DesEncrypter();
		try {
			log = new Log_Handler();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insertLogEntry(String msg){
		log.addAction("", msg);
	}

	public void acceptConnections(){
		try{
			serverSocket = new ServerSocket(port);
			System.out.println("Server Now Running");			
		}
		catch(IOException ioe){
			System.err.print("ERROR" + ioe);
		}
		//loop forever, assign every connection to a thread and start running it
		while(true){
			try{
				Socket connectionSocket = serverSocket.accept();
				System.out.println("accepted connection");
				ServerThread st = new ServerThread(connectionSocket);
				new Thread(st).start();
			}
			catch(IOException ioe){
				System.err.println("Error" + ioe.getMessage());
			}

		}
	}

	class ServerThread implements Runnable {
		private Socket socket;
		private ObjectOutputStream objectOut;
		private ObjectInputStream objectIn;
		public ServerThread(Socket socket){
			this.socket = socket;
		}
		//This executes when a connection is accepted
		public void run(){
			//Create the Data Input/Output Streams
			try
			{
				objectOut = new ObjectOutputStream(socket.getOutputStream());
				objectIn = new ObjectInputStream(socket.getInputStream());
			} 
			catch (IOException e)
			{
				System.out.println("Error Creating Streams");
				e.printStackTrace();
				return;
			}


			//First Read the Command
			String cmd = "";
			try{cmd = (String) objectIn.readObject() ;}
			catch(Exception e){ System.err.println("Error" + e);}
			String c[] = cmd.split(";") ;  
			String[] acct ; 
			//System.out.println(c[0]);
			//Now Service the cmd 
			//Here the First Word Specifies the type of request received by this server
			switch(c[0]){

			case "SignUp":
				// needed info abt user
				//System.out.println("Server:SignUp Request Recieved");
				log.addAction(c[0], "Server:SignUp Request Received");
				SignUp();
				break;

			case "LogIn": 
				//System.out.println("Server:LogIn Request Recieved");
				log.addAction(c[0], "LogIn Request Received");
				acct= encrypter.decodeAccount(c[1]);
				SignIn(acct[0], acct[1]);
				break;
			case "NewEvent":
				//System.out.println("Server: NewEvent has been saved in the database");
				log.addAction(c[0], "NewEvent has been saved in the database");
				acct= encrypter.decodeAccount(c[1]);
				NewEvent(acct[0]); // calling the function with username as operand 
				break;
			case "UpdateAccount":
				log.addAction(c[0], "Account have been Updated");
				//System.out.println("Sever: Account have been Updated");
				acct= encrypter.decodeAccount(c[1]);
				UpdateAccount(acct[0]); // calling the function with username as operand
				break;
			case "GetEvents":
				log.addAction(c[0], "Client going to events have been fetched from the database");
				//	System.out.println("Server: Client going to events have been fetched from the database");
				acct= encrypter.decodeAccount(c[1]);
				GetEvents(acct[0]); // calling the function with username as operand
				break;
			case "GetInvitedEvents":
				log.addAction(c[0], "Client invited to events have been fetched from the database");
				//System.out.println("Server: Client invited to events have been fetched from the database");
				acct= encrypter.decodeAccount(c[1]);
				GetInvitedEvents(acct[0]); // calling the function with username as operand
				break;
			case "AcceptEvent":
				log.addAction(c[0], "Client accepted an event");
				//System.out.println("Server: Client accepted an event");
				acct= encrypter.decodeAccount(c[1]);
				AcceptEvent(acct[0]); // calling the function with username as operand
				break;
			case "RejectEvent":
				//System.out.println("Server: Client rejected an event");
				acct= encrypter.decodeAccount(c[1]);
				RejectEvent(acct[0]); // calling the function with username as operand
				log.addAction(c[0], "Client rejected an event");
				break;	
			case "SignOut":
				//System.out.println("Server: Signing Out");
				acct= encrypter.decodeAccount(c[1]);
				SignOut(acct[0]); // 
				log.addAction(c[0], "Signing Out");
				break;
			case "LoadOnlineFriends":
				//System.out.println("Server: Loading Online Friends");
				acct= encrypter.decodeAccount(c[1]);
				LoadOnlineFriends(acct[1]);//
				log.addAction(c[0], "Loading Online Friends");
				break;
			case "LoadFriends":
				//System.out.println("Server: Loading Friends List");
				acct= encrypter.decodeAccount(c[1]);
				LoadFriends(acct[0]); // 
				log.addAction(c[0], "Loading Friends List");
				break;

			case "LoadUsernames":	//send encrypted account
				//	System.out.println("Server: Loading Usernames on the database");
				acct= encrypter.decodeAccount(c[1]);
				LoadUsernames(acct[0]); 
				log.addAction(c[0], "Loaded Usernames from the database");
				break;

			case "AddFriend"://username and username of added friend
				//System.out.println("Server: Adding a friend to the client friendslist");
				acct= encrypter.decodeAccount(c[1]);
				AddFriend(acct[0],c[2]); 
				log.addAction(c[0], "Adding a friend to the client friendslist");
				break;
				// instead we will add a boolean IsMine to the event Class if 0 not my event if 1 my event   	
				/*case "GetMyEvents"://return Events I created
				System.out.println("Server: Loading the events that he created");
				acct= encrypter.decodeAccount(c[1]);
				GetMyEvents(acct[0]); 
				break;*/

			case "UpdateEvent"://when a user edits his event
				//System.out.println("Server:Editing the event sent by the client");
				acct= encrypter.decodeAccount(c[1]);
				UpdateEvent(acct[0]); 
				log.addAction(c[0], "Editing the event sent by the client");
				//send me back "Send Me New Event Info\n"
				break;

			case "GetFriendRequests"://return my friend reuests
				//	System.out.println("Server: Loading Friends Requests");
				acct= encrypter.decodeAccount(c[1]);
				GetFriendRequests(acct[0]); 
				log.addAction(c[0], "Loading Friends Requests");
				break;

			case "AcceptFriendRequest":
				//	System.out.println("Server: Accepting a Friend Request");
				acct= encrypter.decodeAccount(c[1]);
				AcceptFriendRequest(acct[0],c[2]); 
				log.addAction(c[0], "Accepting a Friend Request");
				break;

			case "RejectFriendRequest":
				//System.out.println("Server: Rejecting a Friend Request");
				acct= encrypter.decodeAccount(c[1]);
				RejectFriendRequest(acct[0],c[2]); 
				log.addAction(c[0], "Rejecting a Friend Request");
				break;

			case "DeleteEvent":
				acct= encrypter.decodeAccount(c[1]);
				DeleteEvent(acct[0]);
				log.addAction(c[0], "Server Deleting Event");
				break;

			case "DeleteAccount":
				acct= encrypter.decodeAccount(c[1]);
				DeleteAccount(acct[0]);
				log.addAction(c[0], "Server Deleting Event");
				break;


				/*

				case "ReScheduleEvent":
					System.out.println("ReScheduleEvent");
                	ReScheduleEvent(c[1], c[2]);
					break;
				case "DeleteAccount":
					System.out.println("Delete Account");
					DeleteAccount(c[1], c[2]); //
					break;
				case "LoadInvitations":
					System.out.println("LoadInvitations");
					LoadInvitations(c[1], c[2]); //
					break;
				case "LoadFriendRequests":
					System.out.println("LoadFriendRequests");
					LoadFriendRequests(c[1], c[2]); //
					break;*/
			default:
				System.out.println("Invalid Command");
				log.addAction(c[0], "Invalid Command");
				break;
			}//end switch
		}

		//ServerThread Methods
		public void LoadUsernames(String username){
			try{										
				ArrayList<String> flag  = serverDatabase.LoadUsernames(username);						
				ArrayList<String> re  = serverDatabase.GetFriendRequests2(username); // i added them 
				ArrayList<String> lo  = serverDatabase.GetFriendRequests(username); // they added me 
				ArrayList<String> fr =serverDatabase.retrievefriends(username);	
				for(int k=0 ; k<re.size();k++){
					flag.remove(re.get(k));
				}
				for(int k=0 ; k<fr.size();k++){
					flag.remove(fr.get(k));
				}
				for(int k=0 ; k<lo.size();k++){
					flag.remove(lo.get(k));
				}
				removeDuplicates(flag);
				objectOut.writeObject(flag);
				objectOut.flush();					
			}
			catch(IOException ioe){
				System.err.println("Error Loading Usernames");}

		}
		public void AddFriend(String username,String friendname){
			try{

				boolean flag  = serverDatabase.AddFriend(username,friendname);						
				if(flag){	
					objectOut.writeObject("AddFriend Successful"+'\n');
					objectOut.flush();
				}else {
					objectOut.writeObject("Failed to AddFriend");
					objectOut.flush();
				}				
			}
			catch(IOException ioe){
				System.err.println("Error Adding the Friend");}

		}
		public void UpdateEvent(String username){
			// add ability to invite more friends
			try{
				objectOut.writeObject("Send Me New Event Info\n");
				Event current = (Event) objectIn.readObject();
				objectOut.writeObject("Send Me Previous Event Info\n");
				Event previous = (Event) objectIn.readObject();

				boolean flag  = serverDatabase.UpdateEvent(username,previous,current);						
				if(flag){	
					objectOut.writeObject("Event Update Successful"+'\n');
					objectOut.flush();
				}else {
					objectOut.writeObject("Failed to Update Event");
					objectOut.flush();
				}				
			}
			catch(IOException  | ClassNotFoundException ioe){
				System.err.println("Error Updating the Event");}					  
		}
		public void DeleteEvent(String username){
			try{
				objectOut.writeObject("Send Me Event That Should Be Deleted \n");
				Event current = (Event) objectIn.readObject();		
				boolean flag  = serverDatabase.DeleteEvent(username,current);						
				if(flag){	
					objectOut.writeObject("Event Delete Successful \n");
					objectOut.flush();
				}else {
					objectOut.writeObject("Failed to Delete Event");
					objectOut.flush();
				}				
			}
			catch(IOException  | ClassNotFoundException ioe){
				System.err.println("Error Deleting Event");}					  
		}
		public void DeleteAccount(String username){
			try{
				objectOut.writeObject("Send Me Account That Should Be Deleted \n");
				Account current = (Account) objectIn.readObject();		
				boolean flag  = serverDatabase.DeleteAccount(username,current);						
				if(flag){	
					objectOut.writeObject("Account Delete Successful"+'\n');
					objectOut.flush();
				}else {
					objectOut.writeObject("Failed to Delete Account");
					objectOut.flush();
				}				
			}
			catch(IOException  | ClassNotFoundException ioe){
				System.err.println("Error Deleting Account");}					  
		}


		public void GetFriendRequests(String username){
			try{										
				ArrayList<String> flag  = serverDatabase.GetFriendRequests(username);
				removeDuplicates(flag);
				objectOut.writeObject(flag);
				objectOut.flush();					
			}
			catch(IOException ioe){
				System.err.println("Error Getting Friends Requests");}

		}
		public void AcceptFriendRequest(String username, String friendname){
			try{

				boolean flag  = serverDatabase.AcceptFriendRequest(username,friendname);						
				if(flag){	
					objectOut.writeObject("Accepting Friend Request Successful"+'\n');
					objectOut.flush();
				}else {
					objectOut.writeObject("Failed to Accept Friend Request");
					objectOut.flush();
				}				
			}
			catch(IOException  ioe){
				System.err.println("Error Accepting Friend Request");}

		}
		public void RejectFriendRequest(String username, String friendname){
			try{

				boolean flag  = serverDatabase.AcceptFriendRequest(username,friendname);						
				if(flag){	
					objectOut.writeObject("Rejecting Friend Request Successful"+'\n');
					objectOut.flush();
				}else {
					objectOut.writeObject("Failed to Reject Friend Request");
					objectOut.flush();
				}				
			}
			catch(IOException  ioe){
				System.err.println("Error Rejecting Friend Request");}

		}
		public void SignUp() {

			try{
				objectOut.writeObject("send me new user account\n");
				Account ac = (Account) objectIn.readObject();
				boolean flag  = serverDatabase.SignUp(ac);
				if(flag){	
					objectOut.writeObject("successful"+'\n');
					objectOut.flush();


					to = ac.getEmail();
					Properties props = new Properties();
					props.put("mail.smtp.auth", "true");
					props.put("mail.smtp.starttls.enable", "true");
					props.put("mail.smtp.host", "smtp.gmail.com");
					props.put("mail.smtp.port", "587");

					Session session = Session.getInstance(props,new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {return new PasswordAuthentication(user, pass);
						}
					});

					Message message = new MimeMessage(session);

					message.setFrom(new InternetAddress(from,fromDisplayName));
					message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
					message.setSubject("Welcome to MyCal !");
					message.setText("Dear " + ac.getUserName() +"," + "\n"
							+ "\n"
							+ "Thanks for signing up for MyCal!" + "\n"
							+ "Organizing your schedule shouldn’t be a burden. With MyCal calendar, it’s easy to keep track of life’s important events all in one place" + "\n"
							+ "You can share your events with your friends,invite other people to events,and get your calendar on the go." + "\n"
							+ "Never forget another event !!!" + "\n"
							+ "MyCal is for free." +"\n"
							+ "\n"
							+ "For feedback, comments, or complaints, please contact us at MyCalTeam@gmail.com." + "\n"
							+ "\n"
							+ "Best," + "\n"
							+ "The MyCal Team");

					Transport.send(message);


				}else {
					objectOut.writeObject("Failed to Sign Up");
				}
			}

			catch(Exception e ){     
				System.err.println("Error Signing Up");
			}
		}
		public void SignIn(String username , String password){
			Account temp = serverDatabase.SignIn(username, password);
			try{

				objectOut.writeObject(temp);
				objectOut.flush();
			}
			catch(IOException ioe){System.err.println("Unable to Write");}
		}

		public void UpdateAccount(String username ){

			try{
				objectOut.writeObject("send me updated account\n");
				Account ac = (Account) objectIn.readObject();
				boolean flag  = serverDatabase.UpdateAccount(ac,username);
				if(flag){	
					objectOut.writeObject("UpdateAccount Successful"+'\n');
					objectOut.flush();
				}else {
					objectOut.writeObject("Failed to UpdateAccount");
				}
			}
			catch(IOException | ClassNotFoundException ioe){
				System.err.println("Error Updating Account");
			}
		}
		public void NewEvent(String username){

			try{
				objectOut.writeObject("send me new event details\n");
				Event ev = (Event) objectIn.readObject();
				boolean flag  = serverDatabase.NewEvent(ev,username);

				if(flag){	
					objectOut.writeObject("NewEvent Successful"+'\n');
					objectOut.flush();
				}else {
					objectOut.writeObject("Failed to create a NewEvent");
					objectOut.flush();
				}

			}
			catch(IOException | ClassNotFoundException ioe){
				System.err.println("Error creating a NewEvent ");
			}						  
		}
		public void GetEvents(String username ){
			try{										
				ArrayList<Event> flag  = serverDatabase.GetEvents(username);						
				objectOut.writeObject(flag);
				objectOut.flush();					
			}
			catch(IOException ioe){
				System.err.println("Error Getting the going to Events");}					  
		}
		public void GetInvitedEvents(String username ){
			try{										
				ArrayList<Event> flag  = serverDatabase.GetInvitedEvents(username);						
				objectOut.writeObject(flag);
				objectOut.flush();					
			}
			catch(IOException ioe){
				System.err.println("Error Getting the Invited to Events");}					  
		}
		public void AcceptEvent(String username ){
			try{
				objectOut.writeObject("Send Me Accepted Event\n");
				Event ev = (Event) objectIn.readObject();
				boolean flag  = serverDatabase.AcceptEvent(username,ev);						
				if(flag){	
					objectOut.writeObject("Event Accept Successful"+'\n');
					objectOut.flush();
				}else {
					objectOut.writeObject("Failed to Accept Event");
					objectOut.flush();
				}					
			}
			catch(IOException | ClassNotFoundException  ioe){
				System.err.println("Error Accepting Event");}					  
		}
		public void RejectEvent(String username ){
			try{
				objectOut.writeObject("Send Me Rejected Event\n");
				Event ev = (Event) objectIn.readObject();
				boolean flag  = serverDatabase.RejectEvent(username,ev);						
				if(flag){	
					objectOut.writeObject("Event Reject Successful"+'\n');
					objectOut.flush();
				}else {
					objectOut.writeObject("Failed to Reject Event");
					objectOut.flush();
				}				
			}
			catch(IOException  | ClassNotFoundException ioe){
				System.err.println("Error Rejecting the Event");}					  
		}



		public void LoadFriends(String username){

			try{										
				ArrayList<String> flag0 =serverDatabase.retrievefriends(username);					
				removeDuplicates(flag0);
				objectOut.writeObject(flag0);
				objectOut.flush();					
			}
			catch(IOException ioe){
				System.err.println("Error Getting the Events");
			}		

		}

		public void LoadOnlineFriends(String username){

			try{										
				ArrayList<String> flag0 =serverDatabase.retrievefriends(username);
				ArrayList<String> flag1  = serverDatabase.retrieveOnlineFriends(username,flag0);						
				removeDuplicates(flag1);
				objectOut.writeObject(flag1);
				objectOut.flush();					
			}
			catch(IOException ioe){
				System.err.println("Error Getting the Events");
			}		

		}
		public void SignOut(String username){
			try{										
				ArrayList<Event> flag  = serverDatabase.GetEvents(username);						
				objectOut.writeObject(flag);
				objectOut.flush();					
			}
			catch(IOException ioe){
				System.err.println("Error Getting the Events");
			}		


		}



	}//end ServerThread

	public void removeDuplicates(ArrayList<String> strings) {

	    int size = strings.size();
	    int duplicates = 0;
	    for (int i = 0; i < size - 1; i++) {	  
	        for (int j = i + 1; j < size; j++) {	           
	            if (!strings.get(j).equals(strings.get(i)))
	                continue;
	            duplicates++;
	            strings.remove(j);	           
	            j--;	           
	            size--;
	        } 
	    } 
	   
	}

	public static void main(String[] args){
		Server myServer = new Server();//create a server
		SimpleTrigger.main(args);
		myServer.acceptConnections();//accept connections
		
	}
}






