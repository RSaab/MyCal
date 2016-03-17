import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Event implements Serializable{
	private String title;
	private String location;
	private String date;
	private String time;
	private String comment;
	private String username;
	private Boolean IsMine ; 
	private ArrayList<String> invited = new ArrayList<String>();
	private ArrayList<String> accepted = new ArrayList<String>();
	private int ID;
	private int rate;
	private boolean canEdit;
	

	/**
	* Constructor for objects of classEvent
	*/
	public Event() {
	}
	
	public Event(int ID ,String username, String date,String title,String comment,String location, String time,int rate ,Boolean IsMine) {
		this.title=title;
		this.location=location;
		this.date=date;
		this.time=time;
		this.username=username;
		this.rate=rate ; 
		this.comment=comment ; 
		this.ID=ID ; 
		this.IsMine=IsMine ; 
	}

	/**
	* copy constructor
	*/
	public Event(Event s) {
		this.title=s.title;
		this.location=s.location;
		this.date=s.date;
		this.time=s.time;
		this.username=s.username;
		this.invited=s.invited;
		this.ID=s.ID;
		this.rate=s.rate ; 
		this.comment=s.comment ; 
		this.IsMine=s.IsMine;
	}

	/**
	* returns the title of the Event
	*/
	public String getTitle() {
		return title;
	}
	public Boolean getIsMine() {
		return IsMine;
	}

	/**
	* mutator method to set the title of the Event
	*@param String the value of type String
	*/
	public void setTitle(String title) {
		this.title=title;
	}
	public void setIsMine(Boolean IsMine) {
		this.IsMine=IsMine;
	}
	public void setcomment(String comment) {
		this.comment=comment;
	}

	/**
	* returns the location of the Event
	*/
	public String getLocation() {
		return location;
	}
	public String getcomment() {
		return comment;
	}

	/**
	* mutator method to set the location of the Event
	*@param String the value of type String
	*/
	public void setLocation(String location) {
		this.location=location;
	}

	/**
	* returns the date of the Event
	*/
	public String getDate() {
		return date;
	}

	/**
	* mutator method to set the date of the Event
	*@param String the value of type String
	*/
	public void setDate(String date) {
		this.date=date;
	}

	/**
	* returns the time of the Event
	*/
	public String getTime() {
		return time;
	}

	/**
	* mutator method to set the time of the Event
	*@param String the value of type String
	*/
	public void setTime(String time) {
		this.time=time;
	}

	/**
	* returns the username of the Event
	*/
	public String getusername() {
		return username;
	}

	/**
	* mutator method to set the username of the Event
	*@param String the value of type String
	*/
	public void setusername(String username) {
		this.username=username;
	}

	/**
	* returns the invited of the Event
	*/
	public ArrayList<String> getInvited() {
		return invited;
	}
	
	public void addInvited(String name){
		if (invited == null){
			invited = new ArrayList<String>();
		}
		invited.add(name);
	}

	/**
	* mutator method to set the invited of the Event
	*@param ArrayList<String> the value of type ArrayList<String>
	*/
	public void setInvited(ArrayList<String> invited) {
		this.invited=invited;
	}

	/**
	* returns the ID of the Event
	*/
	public int getID() {
		return ID;
	}
	public int getrate() {
		return rate;
	}

	/**
	* mutator method to set the ID of the Event
	*@param int the value of type int
	*/
	public void setID(int ID) {
		this.ID=ID;
	}

	public void setrate(int rate) {
		this.rate=rate;
	}

	public ArrayList<String> getaccepted() {
		return accepted;
	}
	
	public void addaccepted(String name){
		if (accepted == null){
			accepted = new ArrayList<String>();
		}
		accepted.add(name);
	}


	public void setaccepted(ArrayList<String> accepted) {
		this.accepted=accepted;
	}
	
	
	/**
	* Returns a string object representing this Event value.
	* The result is a string of 7 line(s) where each line is in turn
	* the string representation of the corresponding instance field.
	* @return a string representation of this object.
	*/
	public String toString() {
		String s= "";
		s+= "Title: " + title;
		s+= "\nLocation: " + location;
		s+= "\nDate: " + date;
		s+= "\nTime: " + time;
		s+= "\nUsername: " + username;
		s+= "\nNumber of invited: " + invited.size();
		s+= "\nRating: "+rate;
		s+= "\nComment: "+comment;
		return s;
	}
	
	public String toStringBrief() {
		String s= title +" on "+date+" @ "+time;
		return s;
	}

}
