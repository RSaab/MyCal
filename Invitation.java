public class Invitation{
	private String Username;
	private Boolean IsRead;
	private String InvitationTo;
	private Boolean Accepted;
	private String EventComment;
	private String EventTitle;
	private String EventLocation;
	private String EventTime;
	private String EventDate;
	private int EventRate;

	/**
	* Constructor for objects of classInvitation
	*/
	public Invitation() {
	}

	/**
	* copy constructor
	*/
	public Invitation(Invitation s) {
		this.Username=s.Username;
		this.IsRead=s.IsRead;
		this.InvitationTo=s.InvitationTo;
		this.Accepted=s.Accepted;
		this.EventComment=s.EventComment;
		this.EventTitle=s.EventTitle;
		this.EventLocation=s.EventLocation;
		this.EventTime=s.EventTime;
		this.EventDate=s.EventDate;
		this.EventRate=s.EventRate;
	}

	/**
	* returns the Username of the Invitation
	*/
	public String getUsername() {
		return Username;
	}

	/**
	* mutator method to set the Username of the Invitation
	*@param String the value of type String
	*/
	public void setUsername(String Username) {
		this.Username=Username;
	}

	/**
	* returns the IsRead of the Invitation
	*/
	public Boolean getIsRead() {
		return IsRead;
	}

	/**
	* mutator method to set the IsRead of the Invitation
	*@param Boolean the value of type Boolean
	*/
	public void setIsRead(Boolean IsRead) {
		this.IsRead=IsRead;
	}

	/**
	* returns the InvitationTo of the Invitation
	*/
	public String getInvitationTo() {
		return InvitationTo;
	}

	/**
	* mutator method to set the InvitationTo of the Invitation
	*@param String the value of type String
	*/
	public void setInvitationTo(String InvitationTo) {
		this.InvitationTo=InvitationTo;
	}

	/**
	* returns the Accepted of the Invitation
	*/
	public Boolean getAccepted() {
		return Accepted;
	}

	/**
	* mutator method to set the Accepted of the Invitation
	*@param Boolean the value of type Boolean
	*/
	public void setAccepted(Boolean Accepted) {
		this.Accepted=Accepted;
	}

	/**
	* returns the EventComment of the Invitation
	*/
	public String getEventComment() {
		return EventComment;
	}

	/**
	* mutator method to set the EventComment of the Invitation
	*@param String the value of type String
	*/
	public void setEventComment(String EventComment) {
		this.EventComment=EventComment;
	}

	/**
	* returns the EventTitle of the Invitation
	*/
	public String getEventTitle() {
		return EventTitle;
	}

	/**
	* mutator method to set the EventTitle of the Invitation
	*@param String the value of type String
	*/
	public void setEventTitle(String EventTitle) {
		this.EventTitle=EventTitle;
	}

	/**
	* returns the EventLocation of the Invitation
	*/
	public String getEventLocation() {
		return EventLocation;
	}

	/**
	* mutator method to set the EventLocation of the Invitation
	*@param String the value of type String
	*/
	public void setEventLocation(String EventLocation) {
		this.EventLocation=EventLocation;
	}

	/**
	* returns the EventTime of the Invitation
	*/
	public String getEventTime() {
		return EventTime;
	}

	/**
	* mutator method to set the EventTime of the Invitation
	*@param String the value of type String
	*/
	public void setEventTime(String EventTime) {
		this.EventTime=EventTime;
	}

	/**
	* returns the EventDate of the Invitation
	*/
	public String getEventDate() {
		return EventDate;
	}

	/**
	* mutator method to set the EventDate of the Invitation
	*@param String the value of type String
	*/
	public void setEventDate(String EventDate) {
		this.EventDate=EventDate;
	}

	/**
	* returns the EventRate of the Invitation
	*/
	public int getEventRate() {
		return EventRate;
	}

	/**
	* mutator method to set the EventRate of the Invitation
	*@param int the value of type int
	*/
	public void setEventRate(int EventRate) {
		this.EventRate=EventRate;
	}


	/**
	* Returns a string object representing this Invitation value.
	* The result is a string of 10 line(s) where each line is in turn
	* the string representation of the corresponding instance field.
	* @return a string representation of this object.
	*/
	public String toString() {
		String s= "";
		s+= "\nUsername = " + Username;
		s+= "\nIsRead = " + IsRead;
		s+= "\nInvitationTo = " + InvitationTo;
		s+= "\nAccepted = " + Accepted;
		s+= "\nEventComment = " + EventComment;
		s+= "\nEventTitle = " + EventTitle;
		s+= "\nEventLocation = " + EventLocation;
		s+= "\nEventTime = " + EventTime;
		s+= "\nEventDate = " + EventDate;
		s+= "\nEventRate = " + EventRate;
		return s;
	}

}
