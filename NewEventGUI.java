import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.List;

import javax.swing.JFrame;
import java.awt.Window.Type;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

import java.awt.Color;
import javax.swing.SwingConstants;


@SuppressWarnings("serial")
public class NewEventGUI extends JFrame implements ActionListener{

	private JFrame frame;
	protected JComboBox cmbEventDay;
	protected JComboBox cmbEventMonth;
	protected JComboBox cmbEventYear;
	protected JComboBox cmbHour;
	protected JComboBox cmbMinutes;
	protected JComboBox cmbAmPm;
	protected JLabel lblHh;
	protected JLabel lblMm;
	protected JButton btnCancel;
	protected JButton btnSave;
	protected JFormattedTextField txtFieldEventTitle;
	protected JFormattedTextField txtFieldLocation;
	private TCPClient_CXN_Manager MyCalClient;
	private Account user;
	private JList<JCheckBox> listInviteFrnds;
	private JCheckBox[] friends;
	private JLabel lblNewLabel;
	private DesEncrypter encrypter = new DesEncrypter();
	private MyCalGUI gui;
	protected JButton btnSaveChanges;
	private ArrayList<String> myFriends;
	private ArrayList<String> invited;
	private JFormattedTextField txtFieldDescription;
	protected Event oldEvent;
	private JList listSelectedFriends;
	private JComboBox cmbRating;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewEventGUI window = new NewEventGUI(null, null, null);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NewEventGUI(TCPClient_CXN_Manager CalClient, Account user, MyCalGUI gui) {
		MyCalClient = CalClient;
		invited = new ArrayList<String>();
		this.user=user;
		this.gui = gui;
		initialize();

		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("New Event");
		frame.setResizable(false);
		frame.setType(Type.POPUP);
		frame.setBounds(100, 100, 580, 341);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnSaveChanges = new JButton("Save Changes");
		btnSaveChanges.addActionListener(this);
		
		JLabel lblRating = new JLabel("Rating:");
		lblRating.setForeground(new Color(255, 255, 224));
		lblRating.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRating.setBounds(10, 248, 47, 17);
		frame.getContentPane().add(lblRating);
		
		cmbRating = new JComboBox();
		cmbRating.setToolTipText("Event Rating");
		cmbRating.setBounds(67, 248, 46, 20);
		cmbRating.addActionListener(this);
		frame.getContentPane().add(cmbRating);

		JLabel lblSelectedFriends = new JLabel("Selected Friends");
		lblSelectedFriends.setHorizontalAlignment(SwingConstants.TRAILING);
		lblSelectedFriends.setBackground(Color.CYAN);
		lblSelectedFriends.setForeground(Color.WHITE);
		lblSelectedFriends.setFont(new Font("Dialog", Font.BOLD, 18));
		lblSelectedFriends.setBounds(284, 166, 268, 14);
		frame.getContentPane().add(lblSelectedFriends);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(284, 186, 268, 116);
		frame.getContentPane().add(scrollPane_1);

		listSelectedFriends = new JList<JCheckBox>();
		scrollPane_1.setViewportView(listSelectedFriends);

		lblHh = new JLabel("HH");
		lblHh.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblHh.setForeground(new Color(255, 255, 224));
		lblHh.setBounds(10, 220, 26, 17);
		frame.getContentPane().add(lblHh);

		JLabel lblComments = new JLabel("Comments");
		lblComments.setForeground(new Color(255, 255, 224));
		lblComments.setFont(new Font("Dialog", Font.BOLD, 14));
		lblComments.setBounds(10, 107, 117, 14);
		frame.getContentPane().add(lblComments);

		txtFieldDescription = new JFormattedTextField();
		txtFieldDescription.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		txtFieldDescription.setBounds(10, 125, 229, 20);
		frame.getContentPane().add(txtFieldDescription);
		btnSaveChanges.setEnabled(false);
		btnSaveChanges.setBounds(10, 279, 101, 23);
		frame.getContentPane().add(btnSaveChanges);

		JLabel lblEventTitle = new JLabel("Title");
		lblEventTitle.setFont(new Font("Dialog", Font.BOLD, 14));
		lblEventTitle.setForeground(new Color(255, 255, 224));
		lblEventTitle.setBounds(10, 11, 117, 14);
		frame.getContentPane().add(lblEventTitle);

		JLabel lblLocation = new JLabel("Location");
		lblLocation.setFont(new Font("Dialog", Font.BOLD, 14));
		lblLocation.setForeground(new Color(255, 255, 224));
		lblLocation.setBounds(10, 58, 117, 14);
		frame.getContentPane().add(lblLocation);

		JLabel lblDateAndTime = new JLabel("Date and Time");
		lblDateAndTime.setFont(new Font("Dialog", Font.BOLD, 14));
		lblDateAndTime.setForeground(new Color(255, 255, 224));
		lblDateAndTime.setBounds(10, 164, 117, 14);
		frame.getContentPane().add(lblDateAndTime);

		txtFieldEventTitle = new JFormattedTextField();
		txtFieldEventTitle.setBounds(10, 27, 229, 20);
		txtFieldEventTitle.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		frame.getContentPane().add(txtFieldEventTitle);

		txtFieldLocation = new JFormattedTextField();
		txtFieldLocation.setBounds(10, 76, 229, 20);
		txtFieldLocation.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		frame.getContentPane().add(txtFieldLocation);

		cmbEventDay = new JComboBox();
		cmbEventDay.setBounds(114, 186, 53, 20);
		cmbEventDay.setToolTipText(" Day (DD)");
		frame.getContentPane().add(cmbEventDay);
		cmbEventDay.addActionListener(this);

		cmbEventMonth = new JComboBox();
		cmbEventMonth.setBounds(10, 186, 94, 20);
		cmbEventMonth.setToolTipText("Month (MM)");
		frame.getContentPane().add(cmbEventMonth);
		cmbEventMonth.addActionListener(this);

		cmbEventYear = new JComboBox();
		cmbEventYear.setBounds(176, 186, 63, 20);
		cmbEventYear.setToolTipText("Year (YYYY)");
		frame.getContentPane().add(cmbEventYear);
		cmbEventYear.addActionListener(this);

		cmbHour = new JComboBox();
		cmbHour.setBounds(34, 217, 53, 20);
		frame.getContentPane().add(cmbHour);

		cmbMinutes = new JComboBox();
		cmbMinutes.setBounds(124, 217, 54, 20);
		frame.getContentPane().add(cmbMinutes);

		cmbAmPm = new JComboBox();
		cmbAmPm.setBounds(186, 217, 53, 20);
		frame.getContentPane().add(cmbAmPm);

		//Populate table
		GregorianCalendar cal = new GregorianCalendar(); //Create calendar
		int realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
		int realMonth = cal.get(GregorianCalendar.MONTH); //Get month
		int realYear = cal.get(GregorianCalendar.YEAR); //Get year
		//System.out.println(realDay + " "+realMonth+" "+realYear);

		cmbEventDay.addItem(realDay); // to avoid a null pointer expression when we first populate and there is nothing selected
		cmbEventDay.setSelectedItem(realDay);

		for (int i=2015-100; i<=2015+100; i++){
			cmbEventYear.addItem(String.valueOf(i));
		}
		String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		for (int i=0; i<=11; i++){
			cmbEventMonth.addItem(months[i]);
		}

		cmbEventYear.setSelectedItem(String.valueOf(realYear));
		cmbEventMonth.setSelectedIndex(realMonth);
		cmbEventDay.setSelectedItem(realDay);
		
		cmbRating.addItem(1);
		cmbRating.addItem(2);
		cmbRating.addItem(3);
		cmbRating.addItem(4);
		cmbRating.addItem(5);
		

		lblMm = new JLabel("MM");
		lblMm.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMm.setForeground(new Color(255, 255, 224));
		lblMm.setBounds(97, 220, 20, 14);
		frame.getContentPane().add(lblMm);

		btnSave = new JButton("Save");
		btnSave.setBounds(150, 245, 89, 23);
		frame.getContentPane().add(btnSave);
		btnSave.addActionListener(this);

		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(150, 279, 89, 23);
		frame.getContentPane().add(btnCancel);
		btnCancel.addActionListener(this);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(284, 35, 268, 110);
		frame.getContentPane().add(scrollPane);

		listInviteFrnds = new JList<JCheckBox>();
		listInviteFrnds.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//if mouse clicked add the selected username to an string arraylist when save is pressed just give this list to new event
				if(myFriends.size()!=0){
					int index = listInviteFrnds.getSelectedIndex();
					String selectedUSer  = myFriends.get(index);
					if(!invited.contains(selectedUSer)){
						invited.add(selectedUSer);
					}else{
						invited.remove(selectedUSer);
					}
					displaySelectedUsersToInvite();
				}
			}

		});
		scrollPane.setViewportView(listInviteFrnds);

		JLabel lblInviteFriends = new JLabel("Select friends to invite");
		lblInviteFriends.setFont(new Font("Dialog", Font.BOLD, 18));
		lblInviteFriends.setForeground(new Color(255, 255, 224));
		lblInviteFriends.setBounds(284, 11, 268, 14);
		frame.getContentPane().add(lblInviteFriends);

		lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(NewEventGUI.class.getResource("/img/background 4.jpg")));
		lblNewLabel.setBounds(0, 2, 575, 311);
		frame.getContentPane().add(lblNewLabel);

		populateHourAndMinutes();
		DisplayFriends();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComboBox){
			JComboBox cmb=(JComboBox) e.getSource();
			if(cmb == cmbEventMonth || cmb==cmbEventYear){
				populateMonthDays(cmbEventMonth.getSelectedIndex(), Integer.parseInt((String) cmbEventYear.getSelectedItem()));
			}
		}else if (e.getSource() instanceof JButton){
			JButton btn = (JButton)e.getSource();

			if(btn==btnSave){
				Event newEvent = createEvent();
				String reply = (String) MyCalClient.connectToServer("New Event", newEvent, encrypter.encodeAccount(user.getUserName(), user.getPassword()), null);
				if (reply.equals("NewEvent Successful"+'\n')){
					JOptionPane.showMessageDialog(null, "Event Saved");
					gui.DisplayAllEvents();
					gui.DisplayEventsInvitedT0();
					gui.DisplayMyEvents();					
				}else {
					JOptionPane.showMessageDialog(null, "NOT SAVED");
				}

				frame.dispose();
			}else if( btn == btnCancel){
				frame.dispose();
			}

			else if(btn == btnSaveChanges){
				String reply = (String) MyCalClient.connectToServer("UpdateEvent", createEvent() , encrypter.encodeAccount(user.getUserName(), user.getPassword()), oldEvent);
				if (reply.equals("Event Update Successful"+'\n')){
					JOptionPane.showMessageDialog(null, "Event Updated");
					this.frame.dispose();
					gui.DisplayAllEvents();
					gui.DisplayEventsInvitedT0();
					gui.DisplayMyEvents();

				}
			}

		}

	}

	protected Event createEvent(){
		Event ev = new Event();
		ev.setTitle(txtFieldEventTitle.getText());
		ev.setLocation(txtFieldLocation.getText());
		ev.setDate(cmbEventDay.getSelectedItem()+"/"+cmbEventMonth.getSelectedItem()+"/"+cmbEventYear.getSelectedItem());
		ev.setTime(cmbHour.getSelectedItem()+":"+cmbMinutes.getSelectedItem()+":"+cmbAmPm.getSelectedItem());
		ev.setInvited(invited);
		ev.setcomment(txtFieldDescription.getText());
		ev.setrate((int) cmbRating.getSelectedItem());
		return ev;
	}


	private void populateHourAndMinutes(){
		for (int i=1; i<=12; i++){
			cmbHour.addItem(i);
		}
		for (int i=00; i<=55; i=i+5){
			cmbMinutes.addItem(i);
		}
		cmbAmPm.addItem("Am");
		cmbAmPm.addItem("Pm");
	}

	private void populateMonthDays(int month, int year){

		int selectedDay= (int) cmbEventDay.getSelectedItem();
		cmbEventDay.removeAllItems();
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		int nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		for (int i=1; i<=nod; i++){
			cmbEventDay.addItem(i);
		}
		try {
			cmbEventDay.setSelectedItem(selectedDay);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}

	}

	public void displaySelectedUsersToInvite(){
		JLabel[] labels;
		if (invited!=null && invited.size()!=0){
			int n= invited.size();
			labels = new JLabel[n];
			for(int i=0; i<n; i++){
				labels[i]= new JLabel(invited.get(i));
			}
		}else {
			labels = new JLabel[1];
			labels[0]=new JLabel("No Selected Users");		
		}
		listSelectedFriends.setListData(labels);
		listSelectedFriends.setCellRenderer(new ChatListRenderer());
		listSelectedFriends.repaint();
	}

	@SuppressWarnings("unchecked")
	public void DisplayFriends(){
		myFriends= (ArrayList<String>) MyCalClient.connectToServer("LoadFriends",  encrypter.encodeAccount(user.getUserName(), user.getPassword()), null, null);
		int n = myFriends.size();


		if (myFriends!=null && myFriends.size()!=0){
			friends = new JCheckBox[n];
			for(int i=0; i<n; i++){
				friends[i]= new JCheckBox(myFriends.get(i));
			}	
			listInviteFrnds.setListData(friends);
			listInviteFrnds.setCellRenderer(new  CheckboxListCellRenderer<JCheckBox> ());
			listInviteFrnds.repaint();
		} else {
			JCheckBox[] label = new JCheckBox[1];
			label[0]=new JCheckBox("No Freinds to Display");
			label[0].setEnabled(false);
			listInviteFrnds.setListData(label);
			listInviteFrnds.setCellRenderer(new CheckboxListCellRenderer<JCheckBox> ());
			listInviteFrnds.repaint();
		}



		//labels[0].setIcon(new ImageIcon("C:\\Users\\Rashad\\Desktop\\AUB\\SPRING 2015\\EECE 450 - Computer Networks\\Project\\MyCal\\bin\\MindView_icon.png"));


	}

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
}


