import java.sql.*;
import java.util.ArrayList;

public class Database {
	private Connection connection = null;

	public Database() {
		PreparedStatement sql_query;

		//build database if doesnt exist
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "root");
			sql_query = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS mycal");
			sql_query.execute();
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mycal", "root", "root");
			sql_query.execute();
			//sql_query = connection.prepareStatement("CREATE TABLE IF NOT EXISTS accounts ( ID INT NULL AUTO_INCREMENT, Email VARCHAR(254) NOT NULL, Username VARCHAR(100) NULL, Password VARCHAR(100) NULL, FullName VARCHAR(200) NULL, Birthdate DATE NULL, displaypic VARCHAR(254) NULL, lastAccess DATETIME NULL,  PRIMARY KEY (Email), UNIQUE INDEX Email_UNIQUE (Email ASC))");
			//sql_query.execute();
			sql_query = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Accounts (Username VARCHAR (20) NOT NULL PRIMARY KEY, Password VARCHAR(20), gender VARCHAR (20), Email VARCHAR (30)  UNIQUE KEY, Birthday VARCHAR (30), DisplayPic VARCHAR(255), mobilephone VARCHAR(20) ,newFrndRequests INT ,newMsgs INT ,newEventInvites INT,nbrOfFriends INT );");
			sql_query.execute();
			sql_query = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Status (StatusID INT NOT NULL PRIMARY KEY AUTO_INCREMENT, Username VARCHAR (20) NOT NULL UNIQUE,Online BOOLEAN, FOREIGN KEY(Username) REFERENCES Accounts(Username) ON DELETE CASCADE ON UPDATE CASCADE);");
			sql_query.execute();
			sql_query = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Friends (FriendsID INT NOT NULL PRIMARY KEY AUTO_INCREMENT, Username VARCHAR (20) NOT NULL ,friendname VARCHAR (30) ,RequestAccepted BOOLEAN,RequestRejected BOOLEAN, FOREIGN KEY(Username) REFERENCES Accounts(Username) ON DELETE CASCADE ON UPDATE CASCADE);");
			sql_query.execute();
			sql_query = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Invitations (InvitationID INT NOT NULL PRIMARY KEY AUTO_INCREMENT, Username VARCHAR (20) NOT NULL, Rejected BOOLEAN, InvitationTo VARCHAR(20), Accepted BOOLEAN ,EventComment VARCHAR(100),EventTitle VARCHAR(50),EventLocation VARCHAR(50),EventTime VARCHAR(50),EventDate VARCHAR(50),EventRate INT, IsMine BOOLEAN , FOREIGN KEY (Username) REFERENCES Accounts (Username) ON DELETE CASCADE ON UPDATE CASCADE);");
			sql_query.execute();

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}



	public Account SignIn(String username, String password) //return Account 
	{
		try {
			Account temp = new Account();
			int i ; 
			if(authenticate(username,password))
			{	
				PreparedStatement sql_query = connection.prepareStatement("SELECT Email,gender, Birthday, DisplayPic,mobilephone,  Username, password FROM Accounts WHERE Username = ?;");
				sql_query.setString(1, username);

				ResultSet rs = sql_query.executeQuery();
				rs.next();

				temp.setUserName(rs.getString(6));
				temp.setPassword(rs.getString(7));
				temp.setBirthday(rs.getString(3));
				temp.setdisplaypic(rs.getString(4));
				temp.setEmail(rs.getString(1));
				temp.setGender(rs.getString(2));
				temp.setMobilePhone(rs.getString(5));			
				sql_query = connection.prepareStatement("UPDATE Status SET  Online = true WHERE Username = ?;");				
				sql_query.setString(1, username);
				sql_query.executeUpdate();

				////////////////////////////nbrOfFriends
				i =0 ;
				sql_query = connection.prepareStatement("Select friendname FROM Friends WHERE   Username=? AND RequestAccepted = ? AND RequestRejected=? ;");
				sql_query.setString(1,username); // I added someone and he accepted -- my username is the username
				sql_query.setBoolean(2,true);
				sql_query.setBoolean(3,false);
				ResultSet rs2 = sql_query.executeQuery();
				
				while(rs2.next()){
					i++ ; 
				}
				sql_query = connection.prepareStatement("Select username FROM Friends WHERE friendname=? AND RequestAccepted = ? AND RequestRejected=? ;");
				sql_query.setString(1,username); // someone addedme and he accepted -- my username is friendname
				sql_query.setBoolean(2,true);
				sql_query.setBoolean(3,false);
				ResultSet rs7 = sql_query.executeQuery();
				
				while(rs7.next()){
					i++ ; 
				}
				
				
				
				
				
				temp.setNbrOfFriends(i);
				///////////////////////////////////newFrndRequests
				i = 0 ;
				sql_query = connection.prepareStatement("Select username FROM Friends WHERE  friendname =? AND RequestRejected = ? AND RequestAccepted =? ;");
				sql_query.setString	(1,username); // someone added me -- my username is friendname
				sql_query.setBoolean(2,false);
				sql_query.setBoolean(3,false);
				ResultSet rs3 = sql_query.executeQuery();
				
				while(rs3.next()){
					i++ ; }
				temp.setNewFrndRequests(i);
				
				///////////////////////////////newEventInvites
				i=0 ; 
				sql_query = connection.prepareStatement("Select InvitationTo FROM Invitations WHERE   InvitationTo=? AND Accepted = ? AND Rejected = ? ;");
				sql_query.setString(1,username);
				sql_query.setBoolean(2,false);
				sql_query.setBoolean(3,false);
				ResultSet rs4 = sql_query.executeQuery();
				while(rs4.next()){
					i++ ; }
				temp.setNewEventInvites(i);
				/////////////////////////////////////////////////,newMsgs, **************** have to do it 
				temp.setNewMsgs(0);//we didnt fix it yet 
				return temp;

			}
			else
				temp.setValidAccount(false);
			return temp;
		}
		catch(Exception exc) {
			exc.printStackTrace();
			return null;
		}	 
	}


	public boolean SignUp(Account account)	//create new Account, return true if successful, false if not successful
	{
		try {
			if(!checkExisting(account.getUserName()))
			{
				PreparedStatement sql_query = connection.prepareStatement("INSERT INTO Accounts (Username, Password,  Email,gender, Birthday, DisplayPic,mobilephone,newFrndRequests,newMsgs,newEventInvites,nbrOfFriends ) VALUES (?,?,?,?,?,?,?,?,?,?,?);");
				sql_query.setString(1,account.getUserName());
				sql_query.setString(2,account.getPassword());
				sql_query.setString(4,account.getGender());
				sql_query.setString(3,account.getEmail());
				sql_query.setString(5, account.getBirthday());
				sql_query.setString(6, account.getdisplaypic());
				sql_query.setString(7, account.getMobilePhone());
				sql_query.setInt(8, account.getNewFrndRequests());
				sql_query.setInt(9, account.getNewMsgs());
				sql_query.setInt(10, account.getNewEventInvites());
				sql_query.setInt(11, account.getNbrOfFriends());
				sql_query.executeUpdate();

				sql_query = connection.prepareStatement("INSERT INTO Status (Username,Online) Values(?,false)");
				sql_query.setString(1,account.getUserName());
				sql_query.executeUpdate();
				return true;
			}
			else
				return false;
		}
		catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
	}


	public boolean checkExisting(String username)	//Check if an username exists in the Database
	{
		try {
			PreparedStatement sql_query = connection.prepareStatement("SELECT * FROM Accounts WHERE Username = ?");
			sql_query.setString(1,username);
			ResultSet rs = sql_query.executeQuery();
			return rs.next();
		}
		catch(Exception exc) {
			exc.printStackTrace();
			return true;
		}
	}
	private boolean authenticate(String username, String password)	//check that username and password match
	{
		try {

			PreparedStatement sql_query = connection.prepareStatement("SELECT Password FROM Accounts WHERE Username = ?");
			sql_query.setString(1,username);
			ResultSet rs = sql_query.executeQuery();
			if(rs.next()){
				return rs.getString(1).equals(password);
			}
			else{
				return false;
			}
		}
		catch(Exception exc) {
			exc.printStackTrace();
			return false;
		}
	}

	public boolean UpdateAccount(Account account, String username)
	{
		if( account.getUserName().equals(username)) 
		{
			try{
				PreparedStatement sql_query = connection.prepareStatement("UPDATE Accounts SET gender = ?, Email = ?, Birthday = ?, DisplayPic = ? , mobilephone=? WHERE Username = ?;");
				sql_query.setString(1, account.getGender());
				sql_query.setString(2, account.getEmail());
				sql_query.setString(3, account.getBirthday());
				sql_query.setString(4, account.getdisplaypic());
				sql_query.setString(5, account.getMobilePhone());
				sql_query.setString(6, username);
				sql_query.executeUpdate();

				return true ; 

			}
			catch(Exception exc){
				exc.printStackTrace();
				return false;
			}
		}
		else
			return false;
	}

	public boolean NewEvent(Event event, String username)	//return same Topic with automatically generated key (topicID) and updated DateCreated
	{	
		try{
			PreparedStatement sql_query = connection.prepareStatement("INSERT INTO Invitations (Username,Rejected,InvitationTo,Accepted,EventComment,EventTitle,EventLocation,EventTime,EventDate,EventRate,IsMine) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			sql_query.setString(1, username);
			sql_query.setBoolean(2, false);
			sql_query.setString(3, username);
			sql_query.setBoolean(4, true);
			sql_query.setString(5, event.getcomment());
			sql_query.setString(6, event.getTitle());
			sql_query.setString(7, event.getLocation());
			sql_query.setString(8, event.getTime());
			sql_query.setString(9, event.getDate());
			sql_query.setInt(10, event.getrate());
			sql_query.setBoolean(11, true);
			sql_query.executeUpdate();

			// ********** deal with the invites + change the account number of invitations accordingly
			for(int k=0; k< event.getInvited().size(); k++) {
				try{
					PreparedStatement sql_query2 = connection.prepareStatement("INSERT INTO Invitations (Username,Rejected,InvitationTo,Accepted,EventComment,EventTitle,EventLocation,EventTime,EventDate,EventRate,IsMine) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
					sql_query2.setString(1, username);
					sql_query2.setBoolean(2, false);
					sql_query2.setString(3, event.getInvited().get(k));
					sql_query2.setBoolean(4, false);
					sql_query2.setString(5, null);
					sql_query2.setString(6, event.getTitle());
					sql_query2.setString(7, event.getLocation());
					sql_query2.setString(8, event.getTime());
					sql_query2.setString(9, event.getDate());
					sql_query2.setInt(10, 0);
					sql_query2.setBoolean(11, false);
					sql_query2.executeUpdate();
				}catch(Exception exc){
					exc.printStackTrace();
					return false;
				}
			}

			return true;
		}
		catch(Exception exc){
			exc.printStackTrace();
			return false;
		}


	}
	public boolean AcceptEvent(String username, Event ev){
		try{
			PreparedStatement sql_query = connection.prepareStatement("UPDATE Invitations SET Rejected = ?, Accepted = ? WHERE InvitationTo = ? AND EventTitle= ? AND EventLocation =? ;");
			sql_query.setBoolean(1,false );
			sql_query.setBoolean(2, true);
			sql_query.setString(3, username);
			sql_query.setString(4, ev.getTitle());
			sql_query.setString(5, ev.getLocation());
			sql_query.executeUpdate();

			return true ; 

		}
		catch(Exception exc){
			exc.printStackTrace();
			return false;
		}

	}
	public boolean RejectEvent(String username, Event ev){

		try{
			PreparedStatement sql_query = connection.prepareStatement("UPDATE Invitations SET Rejected = ?, Accepted = ? WHERE InvitationTo = ? AND EventTitle= ? AND EventLocation =?;");
			sql_query.setBoolean(1,true );
			sql_query.setBoolean(2, false);
			sql_query.setString(3, username);
			sql_query.setString(4, ev.getTitle());
			sql_query.setString(5, ev.getLocation());
			sql_query.executeUpdate();

			return true ; 

		}
		catch(Exception exc){
			exc.printStackTrace();
			return false;
		}

	}

	public ArrayList<Event> GetEvents(String username)	//return the events of the Username calling the function + accepted invitations parsed into events  
	{// a client events are when he is invited to an event && accepted the invitation 

		ArrayList<Event> event = new ArrayList<Event>();
		try{
			PreparedStatement sql_query = connection.prepareStatement("Select * FROM Invitations WHERE  InvitationTo = ? AND Accepted=? ;");
			sql_query.setString(1,username);
			sql_query.setBoolean(2,true);
			ResultSet rs = sql_query.executeQuery();
			while(rs.next())
			{
				Event ev = new Event() ;
				ev.setID(rs.getInt(1));
				ev.setusername(rs.getString(2));
				ev.setDate(rs.getString(10)) ;
				ev.setTitle(rs.getString(7));
				ev.setcomment(rs.getString(6));
				ev.setLocation(rs.getString(8));
				ev.setTime(rs.getString(9));
				ev.setrate(rs.getInt(11));
				ev.setIsMine(rs.getBoolean(12));

				// poeple invited for the event are : InvitationTo when not accepted and eventtitle =.... 	
				// people attending the event are : accepted and eventtitle = ev.gettitle ... 
				try{
					PreparedStatement sql_query2 = connection.prepareStatement("Select InvitationTo FROM Invitations WHERE  EventTitle=? AND EventLocation=? AND Accepted!=? ;");
					sql_query2.setString(1,ev.getTitle());
					sql_query2.setString(2,ev.getLocation());
					sql_query2.setBoolean(3,true);
					ResultSet rs2 = sql_query2.executeQuery();
					while(rs2.next())
					{
						if(rs2.getString(1)!=username){
							ev.addInvited(rs2.getString(1));}
					}
					PreparedStatement sql_query3 = connection.prepareStatement("Select InvitationTo FROM Invitations WHERE  EventTitle=? AND EventLocation=?  AND Accepted=?  ;");
					sql_query3.setString(1,ev.getTitle());
					sql_query3.setString(2,ev.getLocation());
					sql_query3.setBoolean(3,true);
					ResultSet rs3 = sql_query3.executeQuery();
					while(rs3.next())
					{
						if(rs3.getString(1)!=username){
							ev.addaccepted(rs3.getString(1));}
					}
				}
				catch(Exception exc){
					exc.printStackTrace();
					return null;
				}

				event.add(ev);
			}
			return event;
		}
		catch(Exception exc){
			exc.printStackTrace();
			return null;
		}
	}


	public ArrayList<String> LoadUsernames(String username){

		ArrayList<String> users = new ArrayList<String>(); // users other than friends 
		ArrayList<String> friends = new ArrayList<String>(); // all username friends 
		try{
			
			PreparedStatement sql_query = connection.prepareStatement("Select Username FROM Accounts ;");
			ResultSet rs = sql_query.executeQuery();
			while(rs.next())
			{						
					if(  !username.equals(rs.getString(1))        ){
					users.add(rs.getString(1));
					}			
			}
			return users;
		}
		catch(Exception exc){
			exc.printStackTrace();
			return null;
		}

	}	

	public Boolean AddFriend(String username,String friendname){

		try{
			PreparedStatement sql_query = connection.prepareStatement("INSERT INTO Friends (Username,friendname,RequestAccepted,RequestRejected) VALUES (?,?,?,?)");
			sql_query.setString(1, username);
			sql_query.setString(2, friendname);
			sql_query.setBoolean(3, false);
			sql_query.setBoolean(4, false);
			sql_query.executeUpdate();
			return true;
		}
		catch(Exception exc){
			exc.printStackTrace();
			return false;
		}
	}
	public Boolean UpdateEvent(String username,Event previous,Event current){

		try{
			PreparedStatement sql_query = connection.prepareStatement("UPDATE Invitations SET EventComment= ?, EventTitle = ?, EventLocation = ?, EventTime = ?, EventDate = ?, EventRate = ? WHERE EventTitle = ? AND EventLocation = ? AND EventDate=? ;");								
			sql_query.setString(1, current.getcomment());
			sql_query.setString(2, current.getTitle());
			sql_query.setString(3, current.getLocation());
			sql_query.setString(4, current.getTime());
			sql_query.setString(5, current.getDate());
			sql_query.setInt(6, current.getrate());
			sql_query.setString(7, previous.getTitle());
			sql_query.setString(8, previous.getLocation());
			sql_query.setString(9, previous.getDate());
			sql_query.executeUpdate();
			return true ; 
		}
		catch(Exception exc){
			exc.printStackTrace();
			return false;
		}


	}
	public ArrayList<String> GetFriendRequests(String username){
		ArrayList<String> friend = new ArrayList<String>();
		try{
			PreparedStatement sql_query = connection.prepareStatement("Select Username FROM Friends WHERE  friendname = ? AND RequestAccepted=? AND  RequestRejected=? ;");
			sql_query.setString(1,username);
			sql_query.setBoolean(2,false);
			sql_query.setBoolean(3,false);
			ResultSet rs = sql_query.executeQuery();
			while(rs.next())
			{
				friend.add(rs.getString(1));
			}
			return friend;
		}
		catch(Exception exc){
			exc.printStackTrace();
			return null;
		}
	}
	public ArrayList<String> GetFriendRequests2(String username){
		ArrayList<String> friend = new ArrayList<String>();
		try{
			PreparedStatement sql_query = connection.prepareStatement("Select friendname FROM Friends WHERE  Username = ? AND RequestAccepted=? AND  RequestRejected=? ;");
			sql_query.setString(1,username);
			sql_query.setBoolean(2,false);
			sql_query.setBoolean(3,false);
			ResultSet rs = sql_query.executeQuery();
			while(rs.next())
			{
				friend.add(rs.getString(1));
			}
			return friend;
		}
		catch(Exception exc){
			exc.printStackTrace();
			return null;
		}
	}
	public boolean AcceptFriendRequest(String username,String friendname){
		try{
			PreparedStatement sql_query = connection.prepareStatement("UPDATE Friends SET RequestRejected = ?, RequestAccepted = ? WHERE Username = ? AND friendname= ?;");
			sql_query.setBoolean(1,false );
			sql_query.setBoolean(2, true);
			sql_query.setString(3, friendname);
			sql_query.setString(4, username);
			sql_query.executeUpdate();
			return true ; 
		}
		catch(Exception exc){
			exc.printStackTrace();
			return false;
		}
	}
	public boolean RejectFriendRequest(String username,String friendname){
		try{
			PreparedStatement sql_query = connection.prepareStatement("UPDATE Friends SET RequestRejected = ?, RequestAccepted = ? WHERE Username = ? AND friendname= ?;");
			sql_query.setBoolean(1,true );
			sql_query.setBoolean(2, false);
			sql_query.setString(3, friendname);
			sql_query.setString(4, username);
			sql_query.executeUpdate();
			return true ; 
		}
		catch(Exception exc){
			exc.printStackTrace();
			return false;
		}
	}

	public ArrayList<Account>	reminder1(){

				ArrayList<Account> acc = new ArrayList<Account>();
				try{
					PreparedStatement sql_query = connection.prepareStatement("Select * FROM Accounts ;");					
					ResultSet rs = sql_query.executeQuery();
					while(rs.next())
					{
						Account ac = new Account() ;
						ac.setUserName(rs.getString(1));
						ac.setPassword(rs.getString(2));
						ac.setGender(rs.getString(3));
						ac.setEmail(rs.getString(4));
						ac.setBirthday(rs.getString(5));
					 	ac.setdisplaypic(rs.getString(6));
					 	ac.setMobilePhone(rs.getString(7));
					acc.add(ac) ;
					}
					return acc ; 
				}
				catch(Exception exc){
					exc.printStackTrace();
					return null;
				}
			
		
	}
public ArrayList<Event>	reminder2(String username){
	ArrayList<Event> ev = new ArrayList<Event>();
				try{
					
					
					
					
					
					
					
					return ev ; 
				}
				catch(Exception exc){
					exc.printStackTrace();
					return null;
				}
			
		
	}
	public ArrayList<Event> GetInvitedEvents(String username)	//return the events of the Username calling the function + invited invitations parsed into events  
	{// a client events are when he is invited to an event && accepted the invitation 

		ArrayList<Event> event = new ArrayList<Event>();
		try{
			PreparedStatement sql_query = connection.prepareStatement("Select * FROM Invitations WHERE  InvitationTo = ? AND Accepted=? AND Rejected=?;");
			sql_query.setString(1,username);
			sql_query.setBoolean(2,false);
			sql_query.setBoolean(3,false);
			ResultSet rs = sql_query.executeQuery();
			while(rs.next())
			{
				Event ev = new Event() ;
				ev.setID(rs.getInt(1));
				ev.setusername(rs.getString(2));
				ev.setDate(rs.getString(10)) ;
				ev.setTitle(rs.getString(7));
				ev.setcomment(rs.getString(6));
				ev.setLocation(rs.getString(8));
				ev.setTime(rs.getString(9));
				ev.setrate(rs.getInt(11));

				// poeple invited for the event are : InvitationTo when not accepted and eventtitle =.... 	
				// people attending the event are : accepted and eventtitle = ev.gettitle ... 
				try{
					PreparedStatement sql_query2 = connection.prepareStatement("Select InvitationTo FROM Invitations WHERE  EventTitle=? AND EventLocation=? AND Accepted=? AND Rejected=? ;");
					sql_query2.setString(1,ev.getTitle());
					sql_query2.setString(2,ev.getLocation());
					sql_query2.setBoolean(3,false);
					sql_query2.setBoolean(4,false);
					ResultSet rs2 = sql_query2.executeQuery();
					while(rs2.next())
					{
						if(rs2.getString(1)!=username){
							ev.addInvited(rs2.getString(1));}
					}
					PreparedStatement sql_query3 = connection.prepareStatement("Select InvitationTo FROM Invitations WHERE  EventTitle=? AND EventLocation=?  AND Accepted=? AND Rejected=? ;");
					sql_query3.setString(1,ev.getTitle());
					sql_query3.setString(2,ev.getLocation());
					sql_query3.setBoolean(3,true);
					sql_query3.setBoolean(4,false);
					ResultSet rs3 = sql_query3.executeQuery();
					while(rs3.next())
					{
						if(rs3.getString(1)!=username){
							ev.addaccepted(rs3.getString(1));}
					}
				}
				catch(Exception exc){
					exc.printStackTrace();
					return null;
				}

				event.add(ev);
			}
			return event;
		}
		catch(Exception exc){
			exc.printStackTrace();
			return null;
		}
	}


	public boolean SignOut(String username, String password) //set online (in Status) to false
	{
		try {

			if(authenticate(username,password))
			{	 
				PreparedStatement sql_query = connection.prepareStatement("UPDATE Status SET Online = false WHERE Username = ?;");
				sql_query.setString(1, username);
				sql_query.executeUpdate();
				return true;
			}
			else
				return false;
		}
		catch(Exception exc) {
			exc.printStackTrace();
			return false;
		}	 
	}

	public static void main(String[] args)
	{
		Database db = new Database();
	}


	public ArrayList<String> retrievefriends(String username)	//return an ArrayList of online users excluding the user calling the function 
	{

		ArrayList<String> friend = new ArrayList<String>();
		try{
			PreparedStatement sql_query = connection.prepareStatement("Select friendname FROM Friends WHERE  Username = ? AND RequestAccepted= ? ;");
			sql_query.setString(1,username); // I added someone and he accepted -- my username is the username
			sql_query.setBoolean(2,true);
			ResultSet rs = sql_query.executeQuery();
			while(rs.next())
			{
				friend.add(rs.getString(1));
			}
			
			sql_query = connection.prepareStatement("Select username FROM Friends WHERE friendname=? AND RequestAccepted = ? AND RequestRejected=? ;");
			sql_query.setString(1,username); // someone addedme and I accepted -- my username is friendname
			sql_query.setBoolean(2,true);
			sql_query.setBoolean(3,false);
			ResultSet rs7 = sql_query.executeQuery();
			while(rs7.next())
			{
				
					friend.add(rs7.getString(1));
			}
			
			
			
			
			
			return friend;
		}
		catch(Exception exc){
			exc.printStackTrace();
			return null;
		}

	}


	public ArrayList<String> retrieveOnlineFriends(String username, ArrayList<String> Friends)	//return an ArrayList of online users excluding the user calling the function 
	{// it takes an arraylist of the user friends and return array list of who is online  

		ArrayList<String> onlinelist = new ArrayList<String>();
		for(int k=0; k<Friends.size(); k++) {


			try{
				PreparedStatement sql_query = connection.prepareStatement("Select Username FROM Status WHERE Online = true AND Username = ?;");
				sql_query.setString(1,Friends.get(k));
				ResultSet rs = sql_query.executeQuery();
				while(rs.next())
				{
					if(!username.equals(  rs.getString(1)  ) )
					onlinelist.add(rs.getString(1));
				}

			}
			catch(Exception exc){
				exc.printStackTrace();
				return null;
			}
		}
		return onlinelist;
	}

	public boolean DeleteAccount(String username, Account acc)	//deletes account and returns boolean
	{

		try{
			PreparedStatement sql_query = connection.prepareStatement("DELETE FROM Accounts WHERE Username = ?");
			sql_query.setString(1,username);
			if(sql_query.executeUpdate()>=1)
				return true;
			else
				return false;
		}
		catch(Exception exc){
			exc.printStackTrace();
			return false;
		}

	}

public boolean DeleteEvent(String username, Event ev)	//deletes event and returns boolean
	{

		try{
			PreparedStatement sql_query = connection.prepareStatement("DELETE FROM Invitations WHERE Username = ? AND EventTitle=? AND EventLocation=? AND EventDate=? ");
			sql_query.setString(1,username);
			sql_query.setString(2,ev.getTitle());
			sql_query.setString(3,ev.getLocation());
			sql_query.setString(4,ev.getDate());
			
			if(sql_query.executeUpdate()>=1)
				return true;
			else
				return false;
		}
		catch(Exception exc){
			exc.printStackTrace();
			return false;
		}

	}




}
