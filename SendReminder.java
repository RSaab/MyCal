import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class SendReminder extends Server implements Job{
	protected final String user = "mycalenderteam@gmail.com";
	protected final String pass = "Dr.ZaherDawy";
	protected final String from = "mycalenderteam@gmail.com";
	protected final String fromDisplayName = "MyCal";
	private String to;
	private Properties props;
	private HashMap<Account, ArrayList<Event>> usersEvents;
	
	
	public SendReminder(){
		this.props = new Properties();
		this.props.put("mail.smtp.auth", "true");
		this.props.put("mail.smtp.starttls.enable", "true");
		this.props.put("mail.smtp.host", "smtp.gmail.com");
		this.props.put("mail.smtp.port", "587");
		usersEvents = getDailyEvents();
	}

	public void sendEmailReminder(){
		
		Session session = Session.getInstance(props,new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {return new PasswordAuthentication(user, pass);
			}
		});

		Message message = new MimeMessage(session);

		
		Set<Account> keys=usersEvents.keySet();
		for(Account c : keys){
			to=c.getEmail();
			ArrayList<Event> c_events = usersEvents.get(c);
			try {
				message.setFrom(new InternetAddress(from,fromDisplayName));
				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
				message.setSubject("Daily MyCal Reminder");

				String messageText = "Hi " + c.getUserName() + "," + "\n\nHere are your events for today:\n" +
						"-------------------------------------------------\n\n";
				for (int i = 1; i<c_events.size(); i++){
					messageText = messageText + i + ")  " + c_events.get(i).toStringBrief() + "\n\n";
				}
				
				messageText = messageText
						+ "For feedback, comments, or complaints, please contact us at MyCalTeam@gmail.com." + "\n"
						+ "\n"
						+ "Best," + "\n"
						+ "The MyCal Team";
				message.setText(messageText);
				Transport.send(message);
			}catch(Exception e){

			}
		}
	}
	
	public HashMap<Account, ArrayList<Event>> getDailyEvents() {
		HashMap<Account, ArrayList<Event>> usersEvents = new HashMap<Account, ArrayList<Event>>();

		ArrayList<Account> users=new ArrayList<Account>();
		/*
		 * load all accounts into this ArrayList<Account>
		 */
		 users =serverDatabase.reminder1();	
		
		
		ArrayList<Event> events_i = new ArrayList<Event>(); // to be filled below
		ArrayList<Event> events_i_today = new ArrayList<Event>(); // to be filled below
		
		// for each user do the following:
		for (int i = 0 ; i<users.size(); i++){

			
			/*
			 * fill 'events_i' with the current users events - i.e. 'users.get(i)'  	
			 * %%%events_i =serverDatabase.reminder2(users.get(i).getUserName());
			 */
				
			events_i  = super.serverDatabase.GetEvents(users.get(i).getUserName());
			for (int j=0; j < events_i.size(); j++){
				//if event is today add it to events_i_today
				if(eventIsToday(events_i.get(j))){
					events_i_today.add(events_i.get(j));
				}
			}

			//add to the hashmap
			usersEvents.put(users.get(i), events_i_today);
		}
		return usersEvents;
	}

	private boolean eventIsToday(Event ev){
		boolean isToday = false;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		//get current date time with Date()
		Date date = new Date();
		//System.out.println(dateFormat.format(date));
		String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		String d = dateFormat.format(date);
		String[] dt = d.split("/");
		if(ev.getDate().equals(dt[0]+"/"+months[Integer.parseInt(dt[1])-1]+"/"+dt[2])){
			isToday=true;
		}
		return isToday;
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		SendReminder rem = new SendReminder();
		rem.sendEmailReminder();
		super.insertLogEntry("SERVER SENT DAILY REMINDERS");
	}
	
	public static void main(String[] args){
		
	}

}
