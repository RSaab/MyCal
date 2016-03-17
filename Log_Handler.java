import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Log_Handler {

	private static PrintStream out;
	private static int entryNumber = 1;
	private String format="| %-10s | %-25s| %-30s | %-80s |";
	private DateFormat dateFormat;
	private Date date;
	
	public  Log_Handler() throws SQLException{
		printHeader();
		/****** Get Current Date to create a new file with that date *************/
		dateFormat = new SimpleDateFormat("dd/mm/yyyy @ HH:mm:ss");
		//get current date time with Date()
		/**********************************************************************/
	}


	public void printHeader() {
		dateFormat = new SimpleDateFormat("dd-mm-yyyy HH-mm-ss");
		date = new Date();
		File status=new File("Log of Run @ "+ dateFormat.format(date) +".txt");
		try {
			out=new PrintStream(status);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		out.printf(format,"Entry Number","DATE & TIME","CLIENT REQUEST", "SERVER REPLY");
		out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------");		
	}
	
	public void addAction(String ClientMsg, String serverReply){
		format = "| %-12d | %-25s| %-30s | %-80s |";
		out.printf(format, entryNumber, dateFormat.format(date),ClientMsg,serverReply);
		out.println();
		entryNumber++;
	}

	public static void main(String[] arg) throws SQLException{
		Log_Handler log= new Log_Handler();
		log.addAction("Cleint msg", "Server Reply");
		log.addAction("Cleint msg 2", "Server Reply 2");
	}
}


