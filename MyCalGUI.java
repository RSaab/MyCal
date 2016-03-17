/*Contents of CalendarProgran.class */

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;

import javax.swing.border.TitledBorder;

import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.EtchedBorder;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

@SuppressWarnings("serial")
public class MyCalGUI extends JFrame implements ActionListener {
	
	/* *********************************** Variable declaration *******************/
	
	static JLabel lblMonth, lblYear;
	static JButton btnPrev, btnNext;
	static JTable tblCalendar;
	static JComboBox cmbYear;
	static JFrame frmMain;
	static Container pane;
	static DefaultTableModel mtblCalendar; //Table model
	static JScrollPane stblCalendar; //The scrollpane
	static JPanel pnlCalendar;
	static int realYear, realMonth, realDay, currentYear, currentMonth;
	private  JPanel panel;
	private  JLabel lblBirthday;
	private  JLabel lblGender;
	private  JLabel lblNumberOfFriends;
	private  JPanel panel_1;
	private  JTabbedPane tabbedPane;
	private  JPanel panel_3;
	private  JPanel panel_4;
	private  JLabel lblEmail;
	private  JButton btnNewEvent;
	private  JButton btnViewAllEvents;
	private  JButton btnChat;
	private  JButton btnMessages;
	private  JButton btnFriendRequests;
	private  JButton btnEventInvitations;
	private JButton btnEditInfo;
	private JButton btnUploadchangePhoto;
	private NewEventGUI newEventPopUp;
	private TCPClient_CXN_Manager server_com;
	private Account user;
	private JButton btnSignOut;
	private JLabel lblOnlineFriends;
	private JScrollPane scrollPane;
	private JList listOnlineUsers;
	private JLabel lblUsername;
	private JLabel lblBackground;
	private JTextPane textPaneQuotes ;
	private JLabel lblProfilePic;
	private JLabel lblMobilePhone;
	private JComboBox cmbChangeBackground;	
	private JList listEvents;
	private JLabel lblAllEvents;
	private JLabel lblBackgroundEvents;
	private JLabel[] labels;
	private JScrollPane scrollPane_2;
	private JLabel lblEventDetails;
	private JTextPane txtPaneEventDetails;
	private JLabel lblInvited;
	private JScrollPane scrollPane_3;
	private JList listEventInvitees;
	private JButton btnAccept;
	private JButton btnReject;
	private JList listNewInvites;
	private JList listMyEvents;
	private DesEncrypter encrypter;
	private String encryptedAccount;
	private ArrayList<Event> accepedtEvents;
	private ArrayList<Event> events_invitedTo;
	private ArrayList<Event> myEvents;
	private JButton btnEditEvent;
	private JLabel lblAddFriends;
	private JCheckBox[] users;
	private JScrollPane scrollPane_6;
	private JList listAllUSers;
	private JButton btnSendFriendRequest;
	private ArrayList<String> allUsers;
	private JLabel lblFriendRequests;
	private JScrollPane scrollPane_7;
	private JList listFriendRequests;
	private ArrayList<String> frndRequests;
	private JLabel[] friendRequests_labels;
	private JButton btnAddFriend;
	private JButton btnDeclineFriendRequest;
	private JLabel lblBackgroundFriends;
	private JList listGoing;
	private ArrayList<String> selectedToSendFrndReq;
	private JList listSelectedUsers;
	private JButton btnDeleteEvent;
	private JLabel lblBackgroundDeleteAcc;
	private JButton btnDeleteYourOrganized;
	private JButton btnStayOrg;
	private JTextField textTwitPin;
	private JButton btnLogIn;
	private JInternalFrame internalFrame_1;
	private twitter4j.Twitter twitter;
	private static final String CONSUMER_KEY		= "DNX4n1GVPqMdYIJV2wfaM0QlB"; // fill in with values from dev.twitter.com
	private static final String CONSUMER_SECRET 	= "k57UsqfpZr0SYMXtwQZkmHeqi0e91UvO5oZYwCnAMGMAZ2N7mA";
	private JLabel lblScreenName;
	private JTextArea txtTweets;
	private RequestToken requestToken;
	private JTextField txtTweet;
	private JButton btnTweet;
	private JButton btnRefreshTweets;
	private JButton btnShareEventOn;

	/*********************************** end of  Variable declaration *******************/

	@SuppressWarnings({ "static-access", "unchecked" })
	public MyCalGUI (TCPClient_CXN_Manager cal_client, Account u){
		user = u;

		encrypter = new DesEncrypter();
		encryptedAccount = encrypter.encodeAccount(user.getUserName(), user.getPassword());
		server_com = cal_client;
		accepedtEvents = (ArrayList<Event>) server_com.connectToServer("Get Events" , encryptedAccount, null, null);
		selectedToSendFrndReq = new ArrayList<String>();
		initialize();
	}

	//initialize GUI
	public void initialize(){

		//Look and feel
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch (ClassNotFoundException e) {}
		catch (InstantiationException e) {}
		catch (IllegalAccessException e) {}
		catch (UnsupportedLookAndFeelException e) {}

		//Prepare frame
		frmMain = new JFrame ("Gestionnaire de clients");
		frmMain.setResizable(false);
		frmMain.setIconImage(Toolkit.getDefaultToolkit().getImage(MyCalGUI.class.getResource("/img/loginPic.png")));
		frmMain.setBackground(UIManager.getColor("TextField.selectionBackground"));
		frmMain.getContentPane().setEnabled(false);
		frmMain.setTitle("MyCal");
		frmMain.setSize(1124, 701); 
		pane = frmMain.getContentPane();
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close when X is clicked
		mtblCalendar = new DefaultTableModel() {public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
		frmMain.setVisible(true);

		//Get real month/year
		GregorianCalendar cal = new GregorianCalendar(); //Create calendar
		realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
		realMonth = cal.get(GregorianCalendar.MONTH); //Get month
		realYear = cal.get(GregorianCalendar.YEAR); //Get year
		//System.out.println("realDay: "+ realDay+"\nrealMonth: "+realMonth + "\nrealYear: "+realYear);
		currentMonth = realMonth; //Match month and year
		currentYear = realYear;

		//Add headers
		String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
		for (int i=0; i<7; i++){
			mtblCalendar.addColumn(headers[i]);
		}
		frmMain.getContentPane().setLayout(null);

		//Creating Tabs
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 1122, 673);
		tabbedPane.setForeground(new Color(0, 0, 128));
		tabbedPane.setBackground(new Color(153, 204, 204));
		tabbedPane.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.BLUE, null, null, null));
		frmMain.getContentPane().add(tabbedPane);

		panel_3 = new JPanel();
		tabbedPane.addTab("Home", null, panel_3, null);
		panel_3.setLayout(null);

		panel_4 = new JPanel();
		tabbedPane.addTab("Events", null, panel_4, null);

		cmbChangeBackground = new JComboBox();
		cmbChangeBackground.setToolTipText("Change Backgroung Image");
		cmbChangeBackground.setBounds(864, 613, 140, 23);
		panel_3.add(cmbChangeBackground);
		cmbChangeBackground.addActionListener(this);


		lblProfilePic = new JLabel("Profile Pic");
		lblProfilePic.setBackground(Color.BLUE);
		lblProfilePic.setHorizontalAlignment(SwingConstants.CENTER);
		lblProfilePic.setBounds(10, 28, 145, 129);
		panel_3.add(lblProfilePic);

		//customizing user profile
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setForeground(new Color(153, 255, 204));
		panel.setBounds(563, 48, 256, 129);
		panel_3.add(panel);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Personal Info", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GREEN));
		panel.setLayout(null);
		panel.setOpaque(false);

		lblEmail = new JLabel();
		lblEmail.setFont(new Font("Garamond", Font.BOLD, 13));
		lblEmail.setText("Email: "+user.getEmail());
		lblEmail.setBounds(10, 16, 236, 14);
		panel.add(lblEmail);

		lblBirthday = new JLabel();
		lblBirthday.setFont(new Font("Garamond", Font.BOLD, 13));
		lblBirthday.setBounds(10, 38, 236, 14);
		lblBirthday.setText("Birthday: "+user.getBirthday());
		panel.add(lblBirthday);

		lblGender = new JLabel("Gender: "+user.getGender());
		lblGender.setFont(new Font("Garamond", Font.BOLD, 13));
		lblGender.setBounds(10, 60, 236, 14);
		panel.add(lblGender);

		lblNumberOfFriends = new JLabel("Number Of Friends: "+user.getNbrOfFriends());
		lblNumberOfFriends.setFont(new Font("Garamond", Font.BOLD, 13));
		lblNumberOfFriends.setBounds(10, 104, 153, 14);
		panel.add(lblNumberOfFriends);

		btnEditInfo = new JButton();
		btnEditInfo.setIcon(new ImageIcon(MyCalGUI.class.getResource("/img/edit info.png")));
		btnEditInfo.setBounds(221, 93, 25, 25);
		panel.add(btnEditInfo);

		lblMobilePhone = new JLabel();
		lblMobilePhone.setText("Mobile Phone: "+user.getMobilePhone());
		lblMobilePhone.setFont(new Font("Garamond", Font.BOLD, 13));
		lblMobilePhone.setBounds(10, 85, 236, 14);
		panel.add(lblMobilePhone);



		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel_2.setForeground(new Color(153, 255, 204));
		panel_2.setBounds(364, 48, 187, 129);
		panel_3.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Actions", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GREEN));
		panel_2.setLayout(null);
		panel_2.setOpaque(false);
		btnNewEvent = new JButton("New Event");
		btnNewEvent.setBackground(UIManager.getColor("FormattedTextField.selectionBackground"));
		btnNewEvent.setBounds(24, 16, 139, 23);
		panel_2.add(btnNewEvent);

		btnViewAllEvents = new JButton("View All Events");
		btnViewAllEvents.setBackground(UIManager.getColor("FormattedTextField.selectionBackground"));
		btnViewAllEvents.setBounds(24, 51, 139, 23);
		panel_2.add(btnViewAllEvents);

		btnChat = new JButton("Chat");
		btnChat.setBackground(UIManager.getColor("FormattedTextField.selectionBackground"));
		btnChat.setBounds(24, 86, 139, 23);
		panel_2.add(btnChat);

		//Create controls
		lblMonth = new JLabel ("January");
		lblMonth.setForeground(Color.CYAN);
		lblMonth.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblMonth.setBackground(new Color(153, 255, 204));
		lblMonth.setHorizontalAlignment(SwingConstants.CENTER);
		lblYear = new JLabel ("Change year:");
		lblYear.setForeground(Color.CYAN);
		lblYear.setFont(new Font("Tahoma", Font.BOLD, 12));
		cmbYear = new JComboBox();
		btnPrev = new JButton ("<<");
		btnNext = new JButton (">>");
		tblCalendar = new JTable(mtblCalendar);
		stblCalendar = new JScrollPane(tblCalendar);
		pnlCalendar = new JPanel(null);
		pnlCalendar.setBounds(10, 188, 809, 438);
		panel_3.add(pnlCalendar);

		//Set border
		pnlCalendar.setBorder(null);
		//pnlCalendar.setBorder(new TitledBorder(new LineBorder(null), "Calendar", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		pnlCalendar.add(lblMonth);
		pnlCalendar.add(lblYear);
		pnlCalendar.add(cmbYear);
		pnlCalendar.add(btnPrev);
		pnlCalendar.add(btnNext);
		pnlCalendar.add(stblCalendar);
		//lblMonth.setBounds(112, 25, 392, 25);
		lblYear.setBounds(10, 407, 80, 20);
		cmbYear.setBounds(100, 407, 80, 20);
		btnPrev.setBounds(10, 25, 50, 25);
		btnNext.setBounds(741, 25, 50, 25);
		stblCalendar.setBounds(10, 55, 789, 348);
		pnlCalendar.setOpaque(false);

		tblCalendar.getParent().setBackground(tblCalendar.getBackground()); //Set background

		//No resize/reorder
		tblCalendar.getTableHeader().setResizingAllowed(true);
		tblCalendar.getTableHeader().setReorderingAllowed(false);

		//Single cell selection
		tblCalendar.setColumnSelectionAllowed(true);
		tblCalendar.setRowSelectionAllowed(true);
		tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//Set row/column count
		tblCalendar.setRowHeight(54);

		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setForeground(new Color(153, 255, 204));
		panel_1.setBounds(165, 48, 187, 129);
		panel_3.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Notifications", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GREEN));
		panel_1.setLayout(null);
		panel_1.setOpaque(false);

		btnMessages = new JButton("Messages ("+user.getNewMsgs()+")");
		btnMessages.setBackground(UIManager.getColor("FormattedTextField.selectionBackground"));
		btnMessages.setBounds(17, 16, 153, 23);
		panel_1.add(btnMessages);

		btnFriendRequests = new JButton("Friend Requests ("+user.getNewFrndRequests()+")");
		btnFriendRequests.setBackground(UIManager.getColor("FormattedTextField.selectionBackground"));
		btnFriendRequests.setBounds(17, 51, 153, 23);
		panel_1.add(btnFriendRequests);

		btnEventInvitations = new JButton("Event Invitations ("+user.getNewEventInvites()+")");
		btnEventInvitations.setBackground(UIManager.getColor("FormattedTextField.selectionBackground"));
		btnEventInvitations.setBounds(17, 86, 153, 23);
		panel_1.add(btnEventInvitations);

		btnUploadchangePhoto = new JButton("Upload/Change Photo");
		btnUploadchangePhoto.setBounds(10, 168, 145, 19);
		panel_3.add(btnUploadchangePhoto);
		btnUploadchangePhoto.setFont(new Font("Tahoma", Font.PLAIN, 9));

		lblUsername = new JLabel(user.getUserName());
		lblUsername.setFont(new Font("Modern No. 20", Font.BOLD | Font.ITALIC, 31));
		lblUsername.setForeground(Color.RED);
		lblUsername.setBounds(165, 11, 387, 35);
		panel_3.add(lblUsername);


		//frmMain.getContentPane().add(listOnlineUsers);

		lblOnlineFriends = new JLabel("Friends");
		lblOnlineFriends.setBounds(841, 158, 258, 43);
		panel_3.add(lblOnlineFriends);
		lblOnlineFriends.setBackground(new Color(255, 255, 255));
		lblOnlineFriends.setForeground(new Color(0, 0, 0));
		lblOnlineFriends.setFont(new Font("Matisse ITC", Font.BOLD | Font.ITALIC, 34));
		lblOnlineFriends.setHorizontalAlignment(SwingConstants.CENTER);

		//text pane to diplay random quotes on home screen
		textPaneQuotes = new JTextPane();
		textPaneQuotes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				displayQuote();
			}
		});
		textPaneQuotes.setEditable(false);
		textPaneQuotes.setFont(new Font("Baskerville Old Face", Font.BOLD, 18));
		textPaneQuotes.setBounds(841, 11, 257, 136);
		panel_3.add(textPaneQuotes);
		textPaneQuotes.setForeground(Color.WHITE);
		textPaneQuotes.setBackground(UIManager.getColor("Label.foreground"));
		textPaneQuotes.setOpaque(false);
		scrollPane = new JScrollPane();
		scrollPane.setBounds(850, 206, 248, 396);
		panel_3.add(scrollPane);

		listOnlineUsers = new JList();
		scrollPane.setViewportView(listOnlineUsers);

		btnSignOut = new JButton("Sign Out");
		btnSignOut.setBounds(1014, 613, 89, 23);
		panel_3.add(btnSignOut);

		lblBackground = new JLabel("background");
		//lblBackground.setIcon(new ImageIcon(MyCalGUI.class.getResource("/img/background 3.jpg")));
		lblBackground.setBounds(0, -12, 1121, 651);
		panel_3.add(lblBackground);
		btnSignOut.addActionListener(this);

		//Register action listeners
		btnPrev.addActionListener(new btnPrev_Action());
		btnNext.addActionListener(new btnNext_Action());
		cmbYear.addActionListener(new cmbYear_Action());
		btnNewEvent.addActionListener(this);
		btnViewAllEvents.addActionListener(this);
		btnChat.addActionListener(this);
		btnMessages.addActionListener(this);
		btnFriendRequests.addActionListener(this);
		btnEventInvitations.addActionListener(this);
		btnUploadchangePhoto.addActionListener(this);
		btnEditInfo.addActionListener(this);


		mtblCalendar.setColumnCount(7);
		mtblCalendar.setRowCount(6);

		//Populate year combobox
		for (int i=realYear-100; i<=realYear+100; i++){
			cmbYear.addItem(String.valueOf(i));
		}

		//Refresh calendar
		refreshCalendar (realMonth, realYear); //Refresh calendar


		/*************************** Events Tab Components ***************************************************/
		panel_4.setLayout(null);
		
		btnShareEventOn = new JButton("Share Event On Twitter");
		btnShareEventOn.addActionListener(this);
		btnShareEventOn.setBounds(87, 605, 264, 23);
		panel_4.add(btnShareEventOn);
		btnShareEventOn.setEnabled(false);

		JLabel lblGoing = new JLabel("Going");
		lblGoing.setForeground(Color.CYAN);
		lblGoing.setFont(new Font("Matisse ITC", Font.BOLD | Font.ITALIC, 25));
		lblGoing.setBackground(Color.WHITE);
		lblGoing.setBounds(563, 371, 120, 30);
		panel_4.add(lblGoing);

		JScrollPane scrollPane_8 = new JScrollPane();
		scrollPane_8.setBounds(563, 406, 120, 198);
		panel_4.add(scrollPane_8);

		listGoing = new JList();
		listGoing.setFont(new Font("Calisto MT", Font.PLAIN, 14));
		scrollPane_8.setViewportView(listGoing);

		JScrollPane scrollPane_5 = new JScrollPane();
		scrollPane_5.setBounds(31, 406, 320, 198);
		panel_4.add(scrollPane_5);

		btnDeleteEvent = new JButton("Delete Event");
		btnDeleteEvent.addActionListener(this);
		scrollPane_5.setColumnHeaderView(btnDeleteEvent);

		btnEditEvent = new JButton();
		scrollPane_5.setRowHeaderView(btnEditEvent);
		btnEditEvent.setIcon(new ImageIcon(MyCalGUI.class.getResource("/img/edit info.png")));

		listMyEvents = new JList();
		scrollPane_5.setViewportView(listMyEvents);
		listMyEvents.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int index = listMyEvents.getSelectedIndex();
				DisplayEventDetails(index, 3);
			}
		});
		btnEditEvent.addActionListener(this);

		JLabel lblMyevents = new JLabel("My Events");
		lblMyevents.setHorizontalAlignment(SwingConstants.CENTER);
		lblMyevents.setForeground(Color.GREEN);
		lblMyevents.setFont(new Font("Matisse ITC", Font.BOLD | Font.ITALIC, 34));
		lblMyevents.setBackground(Color.WHITE);
		lblMyevents.setBounds(31, 364, 249, 36);
		lblMyevents.setLayout(null);
		panel_4.add(lblMyevents);

		scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(433, 406, 120, 198);

		panel_4.add(scrollPane_3);

		listEventInvitees = new JList();
		listEventInvitees.setFont(new Font("Calisto MT", Font.PLAIN, 14));
		scrollPane_3.setViewportView(listEventInvitees);

		btnReject = new JButton("Reject");
		btnReject.setBounds(160, 337, 120, 23);
		panel_4.add(btnReject);
		btnReject.addActionListener(this);

		btnAccept = new JButton("Accept");
		btnAccept.setBounds(31, 337, 120, 23);
		panel_4.add(btnAccept);
		btnAccept.addActionListener(this);

		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(31, 83, 320, 253);
		panel_4.add(scrollPane_4);

		listNewInvites = new JList();
		listNewInvites.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = listNewInvites.getSelectedIndex();
				DisplayEventDetails(index, 2);
			}
		});
		scrollPane_4.setViewportView(listNewInvites);

		JLabel lblNewInvites = new JLabel("New Invites");
		lblNewInvites.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewInvites.setBounds(31, 26, 249, 36);
		lblNewInvites.setForeground(Color.GREEN);
		lblNewInvites.setFont(new Font("Matisse ITC", Font.BOLD | Font.ITALIC, 34));
		lblNewInvites.setBackground(Color.WHITE);
		panel_4.add(lblNewInvites);
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(755, 92, 329, 512);
		panel_4.add(scrollPane_1);

		listEvents = new JList();
		scrollPane_1.setViewportView(listEvents);
		listEvents.setFont(new Font("Calisto MT", Font.PLAIN, 14));
		listEvents.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int index = listEvents.getSelectedIndex();
				DisplayEventDetails(index, 1);
			}
		});

		lblEventDetails = new JLabel("Event Details");
		lblEventDetails.setBounds(433, 92, 249, 36);
		lblEventDetails.setForeground(Color.CYAN);
		lblEventDetails.setFont(new Font("Matisse ITC", Font.BOLD | Font.ITALIC, 34));
		lblEventDetails.setBackground(Color.WHITE);
		panel_4.add(lblEventDetails);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(433, 133, 249, 227);
		panel_4.add(scrollPane_2);

		txtPaneEventDetails = new JTextPane();
		txtPaneEventDetails.setEditable(false);
		txtPaneEventDetails.setFont(new Font("Calisto MT", Font.PLAIN, 16));
		scrollPane_2.setViewportView(txtPaneEventDetails);

		lblAllEvents = new JLabel("All Events");
		lblAllEvents.setBounds(755, 26, 329, 55);
		lblAllEvents.setHorizontalAlignment(SwingConstants.CENTER);
		lblAllEvents.setForeground(Color.GREEN);
		lblAllEvents.setFont(new Font("Matisse ITC", Font.BOLD | Font.ITALIC, 34));
		lblAllEvents.setBackground(Color.WHITE);
		panel_4.add(lblAllEvents);

		lblInvited = new JLabel("Invited");
		lblInvited.setBounds(433, 371, 120, 30);
		lblInvited.setForeground(Color.CYAN);
		lblInvited.setFont(new Font("Matisse ITC", Font.BOLD | Font.ITALIC, 25));
		lblInvited.setBackground(Color.WHITE);
		panel_4.add(lblInvited);




		lblBackgroundEvents = new JLabel("background2");
		lblBackgroundEvents.setBounds(0, -11, 1122, 665);
		lblBackgroundEvents.setIcon(new ImageIcon(MyCalGUI.class.getResource("/img/background 1.jpg")));
		panel_4.add(lblBackgroundEvents);


		/*************************** end of Events Tab Components ***************************************************/


		/*************************** Friends and Messages Tab ***************************************************/

		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("Friends & Messages", null, panel_5, null);
		panel_5.setLayout(null);

		JLabel lblSelectedUsers = new JLabel("Selected Users");
		lblSelectedUsers.setForeground(Color.CYAN);
		lblSelectedUsers.setFont(new Font("Matisse ITC", Font.BOLD | Font.ITALIC, 23));
		lblSelectedUsers.setBackground(Color.WHITE);
		lblSelectedUsers.setBounds(781, 61, 320, 30);
		panel_5.add(lblSelectedUsers);

		internalFrame_1 = new JInternalFrame("Twitter");
		internalFrame_1.setBounds(10, 291, 1091, 337);
		panel_5.add(internalFrame_1);
		internalFrame_1.getContentPane().setLayout(null);

		lblScreenName = new JLabel("");
		lblScreenName.setForeground(Color.RED);
		lblScreenName.setFont(new Font("Matisse ITC", Font.BOLD | Font.ITALIC, 30));
		lblScreenName.setBackground(Color.WHITE);
		lblScreenName.setBounds(10, 11, 320, 36);
		internalFrame_1.getContentPane().add(lblScreenName);

		JScrollPane scrollPane_10 = new JScrollPane();
		scrollPane_10.setBounds(10, 85, 1065, 212);
		internalFrame_1.getContentPane().add(scrollPane_10);

		txtTweets = new JTextArea();
		scrollPane_10.setViewportView(txtTweets);

		btnLogIn = new JButton("Log In to Twitter");
		btnLogIn.setBounds(371, 11, 194, 23);
		internalFrame_1.getContentPane().add(btnLogIn);
		btnLogIn.addActionListener(this);
		btnLogIn.setFont(new Font("Pristina", Font.BOLD, 20));

		textTwitPin = new JTextField();
		textTwitPin.setBounds(575, 8, 195, 23);
		internalFrame_1.getContentPane().add(textTwitPin);
		textTwitPin.setHorizontalAlignment(SwingConstants.CENTER);
		textTwitPin.setText("Enter Pin");
		textTwitPin.setEnabled(false);
		textTwitPin.setFont(new Font("High Tower Text", Font.PLAIN, 20));
		textTwitPin.setColumns(10);
		textTwitPin.setBackground(new Color(204, 255, 102));

		txtTweet = new JTextField();
		txtTweet.setBounds(10, 54, 555, 20);
		internalFrame_1.getContentPane().add(txtTweet);
		txtTweet.setColumns(10);
		txtTweet.setVisible(false);

		btnTweet = new JButton("Tweet");
		btnTweet.addActionListener(this);
		btnTweet.setBounds(575, 51, 63, 23);
		internalFrame_1.getContentPane().add(btnTweet);
		btnTweet.setVisible(false);
		
		btnRefreshTweets = new JButton("Refresh Tweets");
		btnRefreshTweets.addActionListener(this);
		btnRefreshTweets.setBounds(950, 53, 115, 23);
		internalFrame_1.getContentPane().add(btnRefreshTweets);
		textTwitPin.addActionListener(this);
		internalFrame_1.setVisible(true);
		btnRefreshTweets.setVisible(false);

		JScrollPane scrollPane_9 = new JScrollPane();
		scrollPane_9.setBounds(781, 102, 320, 106);
		panel_5.add(scrollPane_9);

		listSelectedUsers = new JList();
		scrollPane_9.setViewportView(listSelectedUsers);

		lblAddFriends = new JLabel("Add Friends");
		lblAddFriends.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddFriends.setForeground(Color.CYAN);
		lblAddFriends.setFont(new Font("Matisse ITC", Font.BOLD | Font.ITALIC, 34));
		lblAddFriends.setBackground(Color.WHITE);
		lblAddFriends.setBounds(398, 11, 320, 36);
		panel_5.add(lblAddFriends);

		scrollPane_6 = new JScrollPane();
		scrollPane_6.setBounds(398, 58, 320, 180);
		panel_5.add(scrollPane_6);

		listAllUSers = new JList();
		listAllUSers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(allUsers.size()!=0){
					String selectedUSer  = allUsers.get(listAllUSers.getSelectedIndex());
					if(!selectedToSendFrndReq.contains(selectedUSer)){
						selectedToSendFrndReq.add(selectedUSer);
					}else{
						selectedToSendFrndReq.remove(selectedUSer);
					}
					displaySelectedUsersToSendFrndReq();
				}
			}

		});
		scrollPane_6.setViewportView(listAllUSers);

		btnSendFriendRequest = new JButton("Send Friend Request");
		btnSendFriendRequest.addActionListener(this);
		btnSendFriendRequest.setBounds(781, 215, 172, 23);
		panel_5.add(btnSendFriendRequest);

		lblFriendRequests = new JLabel("Friend Requests");
		lblFriendRequests.setHorizontalAlignment(SwingConstants.CENTER);
		lblFriendRequests.setForeground(Color.CYAN);
		lblFriendRequests.setFont(new Font("Matisse ITC", Font.BOLD | Font.ITALIC, 34));
		lblFriendRequests.setBackground(Color.WHITE);
		lblFriendRequests.setBounds(10, 11, 320, 36);
		panel_5.add(lblFriendRequests);

		scrollPane_7 = new JScrollPane();
		scrollPane_7.setBounds(10, 58, 320, 188);
		panel_5.add(scrollPane_7);

		listFriendRequests = new JList();
		scrollPane_7.setViewportView(listFriendRequests);

		btnAddFriend = new JButton("Add Friend");
		btnAddFriend.setBounds(20, 257, 140, 23);
		panel_5.add(btnAddFriend);
		btnAddFriend.addActionListener(this);

		btnDeclineFriendRequest = new JButton("Decline Request");
		btnDeclineFriendRequest.setBounds(170, 257, 140, 23);
		panel_5.add(btnDeclineFriendRequest);

		lblBackgroundFriends = new JLabel("New label");
		lblBackgroundFriends.setBounds(0, -11, 1122, 661);
		panel_5.add(lblBackgroundFriends);

		/*************************** end of Friends and Messages Tab ***************************************************/
		
		/* ************************** Delete Account Tab ***************************************************/
		
		JPanel panel_6 = new JPanel();
		tabbedPane.addTab("Delete Account", null, panel_6, null);
		panel_6.setLayout(null);

		btnDeleteYourOrganized = new JButton("Delete Your Organized Calender Forever....");
		btnDeleteYourOrganized.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.RED, null));
		btnDeleteYourOrganized.addActionListener(this);
		btnDeleteYourOrganized.setBackground(new Color(255, 0, 0));
		btnDeleteYourOrganized.setForeground(Color.RED);
		btnDeleteYourOrganized.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 17));
		btnDeleteYourOrganized.setBounds(323, 243, 467, 50);
		panel_6.add(btnDeleteYourOrganized);

		btnStayOrg = new JButton("Stay Organized");
		btnStayOrg.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.GREEN, null));
		btnStayOrg.setBackground(new Color(0, 255, 0));
		btnStayOrg.setForeground(Color.BLACK);
		btnStayOrg.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 17));
		btnStayOrg.setBounds(396, 339, 320, 50);
		btnStayOrg.addActionListener(this);
		panel_6.add(btnStayOrg);

		lblBackgroundDeleteAcc = new JLabel("Background");
		lblBackgroundDeleteAcc.setBounds(0, -11, 1125, 663);
		panel_6.add(lblBackgroundDeleteAcc);
		btnDeclineFriendRequest.addActionListener(this);

		/*************************** end of Delete Account Tab ***************************************************/

		/*************************** Functions to display the user info on the GUI ***************************************************/
		DisplayOnlineUsers();
		displayQuote();
		displayProfilePic();
		addBackgrounds();
		DisplayAllEvents();
		DisplayEventsInvitedT0();
		DisplayMyEvents();
		DisplayAllUsers();
		DisplayFriendRequests();		
		/*************************** end of Functions to display the user info on the GUI ***************************************************/
	}

	/* **********************************************************************************************************************************************/
	/*****************************************************         METHODS USED IN THE APP             **********************************************/
	/* **********************************************************************************************************************************************/
	
	// diplays the selected ppl that a user has selected to end freind requests
	public void displaySelectedUsersToSendFrndReq(){
		if (selectedToSendFrndReq!=null && selectedToSendFrndReq.size()!=0){
			int n= selectedToSendFrndReq.size();
			labels = new JLabel[n];
			for(int i=0; i<n; i++){
				labels[i]= new JLabel(selectedToSendFrndReq.get(i));
			}
		}else {
			labels = new JLabel[1];
			labels[0]=new JLabel("No Selected Users");		
		}
		listSelectedUsers.setListData(labels);
		listSelectedUsers.setCellRenderer(new ChatListRenderer());
		listSelectedUsers.repaint();
	}

	//displays all users registered in the database
	@SuppressWarnings("unchecked")
	public void DisplayAllUsers(){
		allUsers = (ArrayList<String>) server_com.connectToServer("LoadUsernames", encryptedAccount, null, null);
		int n= allUsers.size();
		users = new JCheckBox[n];
		for(int i=0; i<n; i++){
			users[i]= new JCheckBox(allUsers.get(i));
		}
		//labels[0].setIcon(new ImageIcon("C:\\Users\\Rashad\\Desktop\\AUB\\SPRING 2015\\EECE 450 - Computer Networks\\Project\\MyCal\\bin\\MindView_icon.png"));

		if(n==0){
			JCheckBox[] label = new JCheckBox[1];
			label[0]=new JCheckBox("No Users to Display");
			listAllUSers.setListData(label);
		}else {
			listAllUSers.setListData(users);
		}
		listAllUSers.setCellRenderer(new CheckboxListCellRenderer<JCheckBox>());
		listAllUSers.repaint();
	}

	
	//displays friend requests sent to the user
	public void DisplayFriendRequests(){
		frndRequests = (ArrayList<String>) server_com.connectToServer("GetFriendRequests", encryptedAccount, null, null);
		int n= frndRequests.size();
		JLabel[] labels = new JLabel[n];
		for(int i=0; i<n; i++){
			labels[i]= new JLabel(frndRequests.get(i));
		}

		//labels[0].setIcon(new ImageIcon("C:\\Users\\Rashad\\Desktop\\AUB\\SPRING 2015\\EECE 450 - Computer Networks\\Project\\MyCal\\bin\\MindView_icon.png"));
		if(n==0){
			JLabel[] label = new JLabel[1];
			label[0]=new JLabel("No Freind Requests :(");
			listFriendRequests.setListData(label);
		}else {
			listFriendRequests.setListData(labels);
		}
		listFriendRequests.setCellRenderer(new ChatListRenderer());
		listFriendRequests.repaint();
	}


	//add defined available backgrounds that the user can select from
	private void addBackgrounds(){
		cmbChangeBackground.addItem("background 1");
		cmbChangeBackground.addItem("background 2");
		cmbChangeBackground.addItem("background 3");
	}

	//set the number of message, friend requests and event invites on the home GUI
	private void loadInfoIntoGUI(){
		btnMessages.setText("Messages ("+user.getNewMsgs()+")");
		btnFriendRequests.setText("Friend Requests ("+user.getNewFrndRequests()+")");
		btnEventInvitations.setText("Event Invitations ("+user.getNewEventInvites()+")");
	}

	//displays a random quote on the top right corner of the home GUI
	public void displayQuote(){
		String[] quotes={"However difficult life may seem, there is always something you can do and succeed at.\n   --Stephen Hawking", 
				"Life would be tragic if it weren't funny.\n--Stephen Hawking", 
				"Our greatest weakness lies in giving up. The most certain way to succeed is always to try just one more time.\n   --Thomas A. Edison", 
				"If you can dream it, you can do it.\n   --Walt Disney",
		"Start wide, expand further, and never look back.\n   --Arnold Schwarzenegger"};
		int n = (int)(Math.random()*quotes.length-1);
		textPaneQuotes.setText(quotes[n]);
	}

	//displays online friends
	@SuppressWarnings("unchecked")
	public void DisplayOnlineUsers(){
		ArrayList<String> friends= (ArrayList<String>) server_com.connectToServer("LoadFriends", encryptedAccount, null, null);
		int n=friends.size();
		JLabel[] labels = new JLabel[n];
		for(int i=0; i<n; i++){
			labels[i]= new JLabel(friends.get(i));
		}


		//labels[0].setIcon(new ImageIcon("C:\\Users\\Rashad\\Desktop\\AUB\\SPRING 2015\\EECE 450 - Computer Networks\\Project\\MyCal\\bin\\MindView_icon.png"));
		if(n==0){
			JLabel[] label = new JLabel[1];
			label[0]=new JLabel("No Freinds to Display");
			listOnlineUsers.setListData(label);
		}else {
			listOnlineUsers.setListData(labels);
		}
		listOnlineUsers.setCellRenderer(new ChatListRenderer());
		listOnlineUsers.repaint();
	}


	//displays all events accepted by the user
	@SuppressWarnings("unchecked")
	public void DisplayAllEvents(){ 
		listEvents.removeAll();
		accepedtEvents = (ArrayList<Event>) server_com.connectToServer("Get Events", encryptedAccount, null, null);
		if (accepedtEvents!=null && accepedtEvents.size()!=0){
			int n= accepedtEvents.size();
			labels = new JLabel[n];
			for(int i=0; i<n; i++){
				labels[i]= new JLabel(accepedtEvents.get(i).toStringBrief());
			}
		}else {
			labels = new JLabel[1];
			labels[0]=new JLabel("No Events to Display");		
		}
		listEvents.setListData(labels);
		listEvents.setCellRenderer(new ChatListRenderer());
		listEvents.repaint();
	}


	//displays all events invitations of the user
	public void DisplayEventsInvitedT0(){
		listNewInvites.removeAll();
		events_invitedTo = (ArrayList<Event>) server_com.connectToServer("GetInvitedEvents", encryptedAccount, null , null);
		if(events_invitedTo!=null && events_invitedTo.size()!=0 ){
			int n= events_invitedTo.size();
			labels = new JLabel[n];
			for(int i=0; i<n; i++){
				labels[i]= new JLabel(events_invitedTo.get(i).toStringBrief());
			}
		}else {
			labels = new JLabel[1];
			labels[0]=new JLabel("No Events Invitations to Display");
		}
		listNewInvites.setListData(labels);
		listNewInvites.setCellRenderer(new ChatListRenderer());
		listNewInvites.repaint();
	}

	//displays events the user is hosting
	@SuppressWarnings("unchecked")
	public void DisplayMyEvents(){
		listMyEvents.removeAll();
		myEvents = (ArrayList<Event>) server_com.connectToServer("Get Events", encryptedAccount, null , null);
		if(myEvents!=null && myEvents.size()!=0 ){
			int n= myEvents.size();
			labels = new JLabel[n];
			for(int i=0; i<n; i++){
				boolean isMine = myEvents.get(i).getIsMine();
				if(isMine){
					labels[i]= new JLabel(myEvents.get(i).toStringBrief());
				}

			}
		}else {
			labels = new JLabel[1];
			labels[0]=new JLabel("You haven't created any events yet");
		}
		listMyEvents.setListData(labels);
		listMyEvents.setCellRenderer(new ChatListRenderer());
		listMyEvents.repaint();
	}

	//displays a selected event's details
	private void DisplayEventDetails(int index, int listNumber){
		if (listNumber == 1){
			txtPaneEventDetails.setText(accepedtEvents.get(index).toString());
			DisplayEventInvitees(accepedtEvents.get(index));
			DisplayEventGoing(accepedtEvents.get(index));
		}else if (listNumber == 2){
			txtPaneEventDetails.setText(events_invitedTo.get(index).toString());
			DisplayEventInvitees(events_invitedTo.get(index));
			DisplayEventGoing(events_invitedTo.get(index));
		}else if (listNumber == 3){
			try {
				txtPaneEventDetails.setText(myEvents.get(index).toString());
				DisplayEventInvitees(myEvents.get(index));
				DisplayEventGoing(myEvents.get(index));
			}
			catch (Exception e){

			}
		}

	}

	//displays ppl invited to the event you pass to it
	public void DisplayEventInvitees(Event event){
		ArrayList<String> invitees= event.getInvited();
		int n = invitees.size();
		JLabel[] labels = new JLabel[n];
		for(int i=0; i< n; i++){
			labels[i]= new JLabel(invitees.get(i));
		}

		if(n==0){
			JLabel[] label = new JLabel[1];
			label[0]=new JLabel("No Invitees to Display");
			listEventInvitees.setListData(label);
		}else {
			listEventInvitees.setListData(labels);
		}
		listEventInvitees.setCellRenderer(new ChatListRenderer());
		listEventInvitees.repaint();
	}

	// displays ppl going to the event you pass to it
	public void DisplayEventGoing(Event event){
		ArrayList<String> going= event.getaccepted();
		int n = going.size();
		JLabel[] labels = new JLabel[n];
		for(int i=0; i< n; i++){
			labels[i]= new JLabel(going.get(i));
		}

		if(n==0){
			JLabel[] label = new JLabel[1];
			label[0]=new JLabel("No Invitees to Display");
			listGoing.setListData(label);
		}else {
			listGoing.setListData(labels);
		}
		listGoing.setCellRenderer(new ChatListRenderer());
		listGoing.repaint();
	}

	//displays the profile pic of the user
	public void displayProfilePic(){
		if(user.getdisplaypic()!=null)
			try{
				lblProfilePic.setIcon(new ImageIcon(user.getdisplaypic()));
			} catch(Exception e){
				JOptionPane.showMessageDialog(this, "profile pic moved or renamed, please re-upload");
			}

	}

	//refreshes calendar to allow changing month and years
	public void refreshCalendar(int month, int year){
		//Variables
		String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		int nod, som; //Number Of Days, Start Of Month

		//Allow/disallow buttons
		btnPrev.setEnabled(true);
		btnNext.setEnabled(true);
		if (month == 0 && year <= realYear-100){btnPrev.setEnabled(false);} //Too early
		if (month == 11 && year >= realYear+100){btnNext.setEnabled(false);} //Too late
		lblMonth.setText(months[month]); //Refresh the month label (at the top)
		lblMonth.setBounds(78, 25, 653, 25); //Re-align label with calendar
		cmbYear.setSelectedItem(String.valueOf(year)); //Select the correct year in the combo box

		//Clear table
		for (int i=0; i<6; i++){
			for (int j=0; j<7; j++){
				mtblCalendar.setValueAt(null, i, j);
			}
		}

		//Get first day of month and number of days
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);
		//System.out.println(nod +" "+som);
		//Draw calendar
		for (int i=1; i<=nod; i++){
			int row = new Integer((i+som-2)/7);
			int column  =  (i+som-2)%7;
			//String txt = i + " Event Title";
			//JButton btn = new JButton(i + " Event Title");
			String eventsToday = getEventsToday(i, month, year);
			mtblCalendar.setValueAt(i+""+eventsToday, row, column);
		}

		//Apply renderers
		tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(0), new tblCalendarRenderer());

	}

	//returns string of the titles of all events on the date passed to it
	private String getEventsToday(int day, int month, int year){
		String str="";
		for(int i=0; i< accepedtEvents.size(); i++){
			String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
			Event ev = accepedtEvents.get(i);
			if(ev.getDate().equals(day+"/"+months[month]+"/"+year)){
				str = str + " --" + ev.getTitle();
			}
		}
		return str;
	}

	//Renderer for displaying the calendar
	static class tblCalendarRenderer extends DefaultTableCellRenderer{
		public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
			super.getTableCellRendererComponent(table, value, selected, focused, row, column);
			if (column == 0 || column == 6){ //Week-end
				setBackground(new Color(255, 220, 220));
			}
			else{ //Week
				setBackground(new Color(255, 255, 255));
			}
			if (value != null){
				int day = Integer.parseInt(value.toString().split(" ")[0]);
				if (day == realDay && currentMonth == realMonth && currentYear == realYear){ //Today

					setBackground(new Color(200, 200, 255));
				}
			}
			setBorder(null);
			setForeground(Color.BLACK);
			return this;  
		}
	}

	/* **********************************************************************************************************************************************/
	/*****************************************************         END OF METHODS USED IN THE APP             ***************************************/
	/* **********************************************************************************************************************************************/
	
	/* ************************************ calendar button actions ***********************/
	class btnPrev_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currentMonth == 0){ //Back one year
				currentMonth = 11;
				currentYear -= 1;
			}
			else{ //Back one month
				currentMonth -= 1;
			}
			refreshCalendar(currentMonth, currentYear);
		}
	}
	class btnNext_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currentMonth == 11){ //Foward one year
				currentMonth = 0;
				currentYear += 1;
			}
			else{ //Foward one month
				currentMonth += 1;
			}
			refreshCalendar(currentMonth, currentYear);
		}
	}
	class cmbYear_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (cmbYear.getSelectedItem() != null){
				String b = cmbYear.getSelectedItem().toString();
				currentYear = Integer.parseInt(b);
				refreshCalendar(currentMonth, currentYear);
			}
		}
	}
	/**************************** end of calendar button actions ***********************/
	
	/* ****** Define Actions for various user interactions with the GUI **************/
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton){
			JButton btn = (JButton) e.getSource();

			// user wants to create a new event
			if( btn == btnNewEvent){
				newEventPopUp = new NewEventGUI(server_com, user, this);
			}

			//for implimentation of chat feature, currently just displays a dummy chat frame.
			else if(btn == btnChat){
				ChatFrame chat=new ChatFrame();
			}

			//directs the user to the events tab
			else if(btn == btnViewAllEvents ){
				tabbedPane.setSelectedIndex(1);
				//JOptionPane.showConfirmDialog(this,"View all events!! OMG!!");
			}

			//displays the user's messages
			else if(btn == btnMessages ){
				tabbedPane.setSelectedIndex(2);
			}
			
			// directs the user to the friends and messages tab
			else if(btn == btnFriendRequests ){
				tabbedPane.setSelectedIndex(2);
			}

			//directs the user to th events tab
			else if(btn == btnEventInvitations){
				tabbedPane.setSelectedIndex(1);
			}

			//allows the user to change his email and phone number only
			else if(btn == btnEditInfo ){
				JTextField email = new JTextField();
				email.setText(user.getEmail());
				JTextField mobilePhone = new JTextField();
				mobilePhone.setText(user.getMobilePhone());
				Object[] message = {
						"Email:", email,
						"Mobile Phone:", mobilePhone
				};

				JOptionPane.showConfirmDialog(null, message, "Edit Info", JOptionPane.PLAIN_MESSAGE);
				if (isValidEmail(email.getText())){

					user.setEmail(email.getText());
					user.setMobilePhone(mobilePhone.getText());
					server_com.connectToServer("Update Account", user, encryptedAccount, null);
					lblEmail.setText("Email: "+email.getText());
					lblMobilePhone.setText("Mobile Phone: "+mobilePhone.getText());
				}

			}

			//allows the user to upload a profile pic that is stored on his computer
			//the app only stores its location on his PC in it database
			else if(btn == btnUploadchangePhoto){
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					String absolutePath=chooser.getSelectedFile().getAbsolutePath();
					lblProfilePic.setIcon(new ImageIcon(absolutePath));
					user.setdisplaypic(absolutePath);
					server_com.connectToServer("Update Account", user, encryptedAccount, null);
				}

			//Accept the selected event invite
			}else if(btn == btnAccept){
				int n = listNewInvites.getSelectedIndex();
				Event ev = events_invitedTo.get(n);
				//accepedtEvents.add(ev);
				//events_invitedTo.remove(ev);
				server_com.connectToServer("AcceptEvent", ev, encryptedAccount, null);
				DisplayEventsInvitedT0();
				DisplayAllEvents();
				user.setNewEventInvites(user.getNewEventInvites()-1);
				btnEventInvitations.setText("Event Invitations ("+user.getNewEventInvites()+")");

			}
			
			//Reject the selected event invite
			else if(btn == btnReject){
				int n = listNewInvites.getSelectedIndex();
				Event ev = events_invitedTo.get(n);
				//events_invitedTo.remove(ev);
				server_com.connectToServer("RejectEvent", ev, encryptedAccount, null);
				DisplayEventsInvitedT0();
				DisplayEventsInvitedT0();
				user.setNewEventInvites(user.getNewEventInvites()-1);
				btnEventInvitations.setText("Event Invitations ("+user.getNewEventInvites()+")");
			}

			//Signs the user out of his account and displays the login sreen
			else if(btn == btnSignOut){
				//tell server to close everything
				server_com.connectToServer("Update Account", user, encryptedAccount, null);
				this.frmMain.dispose();
				LogInGUI logIn=new LogInGUI();
				logIn.frmMycal.setVisible(true);

			}
			
			//allows the user to edit his event's info
			else if (btn == btnEditEvent){
				int index = listMyEvents.getSelectedIndex();
				Event ev = myEvents.get(index);
				NewEventGUI editEvent = new NewEventGUI(server_com, user, this);
				editEvent.txtFieldEventTitle.setText(ev.getTitle());
				editEvent.txtFieldLocation.setText(ev.getLocation());
				String date = ev.getDate();
				String[] dates = date.split("/");
				editEvent.cmbEventDay.setSelectedItem(dates[0]);
				editEvent.cmbEventMonth.setSelectedItem(dates[1]);
				editEvent.cmbEventYear.setSelectedItem(dates[2]);
				editEvent.btnSave.setEnabled(false);
				editEvent.btnSaveChanges.setEnabled(true);
				editEvent.oldEvent = ev;
			}

			// send a friend request to the selected users from the list
			else if(btn == btnSendFriendRequest){
				for (int i=0; i < selectedToSendFrndReq.size(); i++){
					String addedUser = selectedToSendFrndReq.get(i);
					server_com.connectToServer("AddFriend", encryptedAccount, addedUser, null);
					DisplayAllUsers();
					JOptionPane.showMessageDialog(this, "Friend Request Sent");
				}
			}				

			//accept a friend request
			else if(btn == btnAddFriend){
				if(frndRequests != null && frndRequests.size()!=0){
					System.out.println (frndRequests.get(listFriendRequests.getSelectedIndex()));
					server_com.connectToServer("AcceptFriendRequest", encryptedAccount, frndRequests.get(listFriendRequests.getSelectedIndex()) , null);
					DisplayFriendRequests();
					DisplayOnlineUsers();
				}
			}

			//decline a friend request
			else if(btn == btnDeclineFriendRequest){
				if(frndRequests != null && frndRequests.size()!=0){
					server_com.connectToServer("RejectFriendRequest", encryptedAccount, frndRequests.get(listFriendRequests.getSelectedIndex()) , null);
					DisplayFriendRequests();
				}

			}

			//delete a user's event
			else if(btn == btnDeleteEvent){
				int index = listMyEvents.getSelectedIndex();
				Event ev =  myEvents.get(index);
				String reply = (String) server_com.connectToServer("DeleteEvent", ev, encryptedAccount, null);
				myEvents.remove(index);
				JOptionPane.showMessageDialog(this, ev.getTitle()+" Deleted.");
				DisplayAllEvents();
				DisplayMyEvents();
				txtPaneEventDetails.removeAll();
				listGoing.removeAll();
				listEventInvitees.removeAll();

			}	
			
			// delete the user's account from the database permanently
			else if(btn == btnDeleteYourOrganized){
				int p = JOptionPane.showConfirmDialog(this, "Are you sure you want to permanently delete your MyCal account?");
				if(p==0){
					String reply = (String) server_com.connectToServer("DeleteAccount", user, encryptedAccount, null);
					this.frmMain.dispose();
					LogInGUI logIn=new LogInGUI();
					logIn.frmMycal.setVisible(true);
				}

			}	

			// directs the user to the home page after he decides not to delete his account anymore
			else if(btn == btnStayOrg){
				JOptionPane.showMessageDialog(this, "You made the right choice, Keep up the Good Work!");
				tabbedPane.setSelectedIndex(0);
			}	

			//login button for twitter
			else if(btn == btnLogIn){
				Twitter();
			}

			//tweet button for twitter
			else if(btn == btnTweet){
				tweet();
			}
			
			//refresh tweets
			else if(btn == btnRefreshTweets){
				displayTweets();
			}
			
			//share an event on twitter via a predefined tweet
			else if(btn == btnShareEventOn){
				int index = listMyEvents.getSelectedIndex();
				Event ev =  myEvents.get(index);
				tweet(ev.toStringBrief());
			}

			
		}else if( e.getSource() instanceof JComboBox){
			JComboBox cmb = (JComboBox) e.getSource();
			//change the GUI background
			if(cmb == cmbChangeBackground){
				String bg=(String) cmbChangeBackground.getSelectedItem();
				lblBackground.setIcon(new ImageIcon(MyCalGUI.class.getResource("/img/"+bg+".jpg")));
				lblBackgroundEvents.setIcon(new ImageIcon(MyCalGUI.class.getResource("/img/"+bg+".jpg")));
				lblBackgroundFriends.setIcon(new ImageIcon(MyCalGUI.class.getResource("/img/"+bg+".jpg")));
				lblBackgroundDeleteAcc.setIcon(new ImageIcon(MyCalGUI.class.getResource("/img/"+bg+".jpg")));
			}

		} else if(e.getSource() instanceof JTextField){
			JTextField txt = (JTextField) e.getSource();
			//check pin and log in to twitter
			if(txt == textTwitPin){
				logInToTwitter(textTwitPin.getText());
			}
		}
	}
	/******* end of Define Actions for various user interactions with the GUI **************/

	//simple check for a valid email
	private boolean isValidEmail(String email){
		boolean isValid=false;
		if((email.contains(".com") || email.contains(".edu")) && email.contains("@")){
			isValid=true;
		}
		return isValid;
	}

	//renderer to display lists of JList type
	class ChatListRenderer extends DefaultListCellRenderer
	{
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index,boolean isSelected, boolean cellHasFocus) 
		{
			JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if(value instanceof JLabel)
			{
				this.setText(((JLabel)value).getText());
				this.setIcon(((JLabel)value).getIcon());
			}
			return this;
		}
	}

	//renderer to display lists of JCheckBox type
	public class CheckboxListCellRenderer<E> extends JCheckBox implements
	ListCellRenderer<E> {

		private static final long serialVersionUID = 3734536442230283966L;

		@Override
		public Component getListCellRendererComponent(JList<? extends E> list,
				E value, int index, boolean isSelected, boolean cellHasFocus) {
			setComponentOrientation(list.getComponentOrientation());

			setFont(list.getFont());
			setText(((JCheckBox) value).getLabel());

			setBackground(list.getBackground());
			setForeground(list.getForeground());

			setSelected(isSelected);
			setEnabled(list.isEnabled());
			((JCheckBox) value).setEnabled(true);
			((JCheckBox) value).setSelected(false);

			return this;
		}

	}

	/* **********************************************************************************************************************************************/
	/*****************************************************         TWITTER FEATURE            *******************************************************/
	/* **********************************************************************************************************************************************/
	
	// Opens the browser and asks the user to authorize MyCal app
	private void Twitter(){
		requestToken = null;
		twitter = new TwitterFactory().getInstance();

		//	Set up the access tokens and keys to get permission to access
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		try
		{
			requestToken = twitter.getOAuthRequestToken();
		}
		catch (Exception e)
		{
			System.out.println("Ah, well, there's an error with Twitter.  Are the consumer key and secret correct?");
			e.printStackTrace();
			System.exit(-1);
		}

		//launch the browser for the user
		//System.out.println("Launching browser...");
		try
		{
			Desktop desktop = Desktop.getDesktop();

			//	Twitter creates a "magic" URL for us that contains a temporary token based upon our consumer info.
			//	The page at that URL will ask the user if they wish to authorize our application (as identified by
			//	the consumer key and secret).  If the user says yes, then a PIN code is generated and displayed
			//	to the user, who should copy it from the web page.
			desktop.browse(new URI(requestToken.getAuthorizationURL()));
		}
		catch (Exception e)
		{
			System.out.println("Problem in launching browser. Copy and paste the following URL into a browser:");
			System.out.println(requestToken.getAuthorizationURL());
		}

		textTwitPin.setEnabled(true);
		textTwitPin.setText("");
	}

	
	// after the user enters his pin and presses enter he is logged in and his timeline is showed
	private void logInToTwitter(String pin){
		AccessToken token  = null;
		try
		{
			token = twitter.getOAuthAccessToken(requestToken, pin);
			twitter.setOAuthAccessToken(token);
		}
		catch (TwitterException e)
		{
			System.out.println("Was there a typo in the PIN you entered?");
			e.printStackTrace();
			System.exit(-1);
		}

		//set the user screen name
		try {
			lblScreenName.setText(twitter.getScreenName());
			txtTweet.setVisible(true);
			btnTweet.setVisible(true);
			btnRefreshTweets.setVisible(true);
			btnShareEventOn.setEnabled(true);
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		displayTweets();

		// display sign out button
		btnLogIn.setVisible(false);
		textTwitPin.setVisible(false);
	}

	//displays the users twitter timeline
	private void displayTweets(){
		txtTweets.setText("");
		//get tweets
		ResponseList<Status> tweets = null;
		try {
			tweets = twitter.getUserTimeline();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//diplay tweets
		for (Status status : tweets) {
			txtTweets.append("@" + status.getUser().getScreenName() + " - " + status.getText()+"\n");
		}

	}

	//posts a tweet to the user's twitter profile
	private void tweet(){
		try {
			twitter.updateStatus(txtTweet.getText()+"\n-- Tweeted via MyCal.");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		txtTweet.setText("");
		displayTweets();
	}
	
	//same as tweet() but used to share an event by passing a prdefined string passed to it internally by the program
	private void tweet(String str){
		try {
			twitter.updateStatus(str+"\n-- Tweeted via MyCal.");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		txtTweet.setText("");
		displayTweets();
	}
	
	/* **********************************************************************************************************************************************/
	/*****************************************************        END OF TWITTER FEATURE            *******************************************************/
	/* **********************************************************************************************************************************************/
	
}// end of Class MyCalGUI
