import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Account implements Serializable{
	private String UserName;
	private String Email;
	private String password;
	private String birthday;
	private String displaypic;
	private String mobilePhone;
	private int nbrOfFriends;
	private int newFrndRequests;
	private int newMsgs;
	private int newEventInvites;
	private ArrayList<String> friendsList=new ArrayList<String>();
	private String gender;
	private boolean validAccount=true;
	

	/**
	* Constructor for objects of classAccount
	*/
	
	public Account() {
	}

	/**
	* copy constructor
	*/
	public Account(Account s) {
		this.UserName=s.UserName;
		this.gender=s.gender; 
		this.Email=s.Email;
		this.password=s.password;
		this.birthday=s.birthday;
		this.mobilePhone=s.mobilePhone;
		this.newFrndRequests=s.newFrndRequests;
		this.newMsgs=s.newMsgs;
		this.newEventInvites=s.newEventInvites;
		this.friendsList=s.friendsList;
		this.displaypic=s.displaypic ; 
	}

	/**
	* returns the UserName of the Account
	*/
	public String getUserName() {
		return UserName;
	}

	public String getdisplaypic() {
		return displaypic;
	}
	/**
	* mutator method to set the UserName of the Account
	*@param String the value of type String
	*/
	public void setUserName(String UserName) {
		this.UserName=UserName;
	}
	
	public void setdisplaypic(String displaypic) {
		this.displaypic=displaypic;
	}

	/**
	* returns the Email of the Account
	*/
	public String getEmail() {
		return Email;
	}

	/**
	* mutator method to set the Email of the Account
	*@param String the value of type String
	*/
	public void setEmail(String Email) {
		this.Email=Email;
	}

	/**
	* returns the password of the Account
	*/
	public String getPassword() {
		return password;
	}

	/**
	* mutator method to set the password of the Account
	*@param String the value of type String
	*/
	public void setPassword(String password) {
		this.password=password;
	}

	/**
	* returns the birthday of the Account
	*/
	public String getBirthday() {
		return birthday;
	}

	/**
	* mutator method to set the birthday of the Account
	*@param String the value of type String
	*/
	public void setBirthday(String birthday) {
		this.birthday=birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	* returns the mobilePhone of the Account
	*/
	public String getMobilePhone() {
		return mobilePhone;
	}

	/**
	* mutator method to set the mobilePhone of the Account
	*@param int the value of type int
	*/
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone=mobilePhone;
	}

	public int getNbrOfFriends() {
		return nbrOfFriends;
	}

	public void setNbrOfFriends(int nbrOfFriends) {
		this.nbrOfFriends = nbrOfFriends;
	}

	/**
	* returns the newFrndRequests of the Account
	*/
	public int getNewFrndRequests() {
		return newFrndRequests;
	}

	/**
	* mutator method to set the newFrndRequests of the Account
	*@param int the value of type int
	*/
	public void setNewFrndRequests(int newFrndRequests) {
		this.newFrndRequests=newFrndRequests;
	}

	/**
	* returns the newMsgs of the Account
	*/
	public int getNewMsgs() {
		return newMsgs;
	}

	/**
	* mutator method to set the newMsgs of the Account
	*@param int the value of type int
	*/
	public void setNewMsgs(int newMsgs) {
		this.newMsgs=newMsgs;
	}

	/**
	* returns the newEventInvites of the Account
	*/
	public int getNewEventInvites() {
		return newEventInvites;
	}

	/**
	* mutator method to set the newEventInvites of the Account
	*@param int the value of type int
	*/
	public void setNewEventInvites(int newEventInvites) {
		this.newEventInvites=newEventInvites;
	}

	/**
	* returns the friendsList of the Account
	*/
	public ArrayList<String> getFriendsList() {
		return friendsList;
	}

	/**
	* mutator method to set the friendsList of the Account
	*@param ArrayList<String> the value of type ArrayList<String>
	*/
	public void setFriendsList(ArrayList<String> friendsList) {
		this.friendsList=friendsList;
	}


	public boolean isValidAccount() {
		return validAccount;
	}

	public void setValidAccount(boolean validAccount) {
		this.validAccount = validAccount;
	}

	/**
	* Returns a string object representing this Account value.
	* The result is a string of 9 line(s) where each line is in turn
	* the string representation of the corresponding instance field.
	* @return a string representation of this object.
	*/
	public String toString() {
		String s= "";
		s+= "\nUserName = " + UserName;
		s+= "\nEmail = " + Email;
		s+= "\npassword = " + password;
		s+= "\nbirthday = " + birthday;
		s+= "\nGender = "+ gender;
		s+= "\nmobilePhone = " + mobilePhone;
		s+= "\nnewFrndRequests = " + newFrndRequests;
		s+= "\nnewMsgs = " + newMsgs;
		s+= "\nnewEventInvites = " + newEventInvites;
		s+= "\nfriendsList = " + friendsList;
		s+= "\nValid Account: "+validAccount;
		return s;
	}

}
