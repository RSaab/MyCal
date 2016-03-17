/* Rashad Saab, 201301697, rms78 */

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class LogInGUI extends JFrame implements ActionListener {

	public JFrame frmMycal;
	private JPasswordField passwordField;
	private static LogInGUI window;
	private JButton btnLogIn ;
	private JButton btnChangePassword;
	private JTextField txtFieldUserEmail;
	private TCPClient_CXN_Manager server_com;
	private DesEncrypter encrypter;
	private JTextField txtNewUserEmail;
	private JPasswordField passwordField_1;
	private JTextField txtNewUserUsername;
	private JLabel lblBirthdate;
	private JComboBox cmbBirthDay;
	private JComboBox cmbBirthMonth;
	private JComboBox cmbBirthYear;
	private JPasswordField passwordField_2;
	private JButton btnSignMeUp;
	private JLabel lblGender;
	private JComboBox cmbGender;
	private JLabel lblMobile;
	private JTextField txtMobilePhone;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new LogInGUI();
					window.frmMycal.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LogInGUI() {
		server_com = new TCPClient_CXN_Manager();
		encrypter = new DesEncrypter();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMycal = new JFrame();
		frmMycal.setIconImage(Toolkit.getDefaultToolkit().getImage(LogInGUI.class.getResource("/img/loginPic.png")));
		frmMycal.setTitle("MyCal");
		frmMycal.setResizable(false);
		frmMycal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 43));
		frmMycal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMycal.getContentPane().setBackground(SystemColor.inactiveCaption);
		frmMycal.getContentPane().setLayout(null);
		frmMycal.setBounds(0, 0, 632, 437);
		frmMycal.setLocation(450, 250);

		passwordField = new JPasswordField();
		passwordField.setBackground(new Color(204, 255, 102));
		passwordField.setBounds(80, 122, 195, 36);
		frmMycal.getContentPane().add(passwordField);
		passwordField.addActionListener(this);

		btnLogIn = new JButton("Log In");
		btnLogIn.setFont(new Font("Pristina", Font.BOLD, 20));
		btnLogIn.setBounds(96, 165, 129, 23);
		frmMycal.getContentPane().add(btnLogIn);
		btnLogIn.addActionListener(this);

		btnChangePassword = new JButton("Forgot My Pass!");
		btnChangePassword.setFont(new Font("Pristina", Font.BOLD, 15));
		btnChangePassword.setToolTipText("OMG!!!");
		btnChangePassword.setBounds(96, 199, 129, 23);
		frmMycal.getContentPane().add(btnChangePassword);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		lblUsername.setForeground(Color.BLACK);
		lblUsername.setBackground(new Color(0, 0, 0));
		lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUsername.setBounds(0, 76, 79, 36);
		frmMycal.getContentPane().add(lblUsername);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		lblPassword.setForeground(Color.BLACK);
		lblPassword.setBackground(new Color(0, 0, 0));
		lblPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPassword.setBounds(0, 123, 79, 36);
		frmMycal.getContentPane().add(lblPassword);

		txtFieldUserEmail = new JTextField();
		txtFieldUserEmail.setFont(new Font("High Tower Text", Font.PLAIN, 20));
		txtFieldUserEmail.setBackground(new Color(204, 255, 102));
		txtFieldUserEmail.setBounds(80, 76, 195, 36);
		frmMycal.getContentPane().add(txtFieldUserEmail);
		txtFieldUserEmail.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBounds(297, 11, 319, 376);
		frmMycal.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewUserSign = new JLabel("New User? SIGN UP!");
		lblNewUserSign.setForeground(Color.RED);
		lblNewUserSign.setFont(new Font("MV Boli", Font.BOLD, 15));
		lblNewUserSign.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewUserSign.setBounds(10, 11, 268, 26);
		panel.add(lblNewUserSign);

		JLabel label = new JLabel("Email:");
		label.setHorizontalAlignment(SwingConstants.TRAILING);
		label.setForeground(Color.RED);
		label.setFont(new Font("MV Boli", Font.PLAIN, 12));
		label.setBackground(Color.BLACK);
		label.setBounds(10, 48, 100, 31);
		panel.add(label);

		txtNewUserEmail = new JTextField();
		txtNewUserEmail.setFont(new Font("High Tower Text", Font.PLAIN, 20));
		txtNewUserEmail.setBackground(Color.CYAN);
		txtNewUserEmail.setColumns(10);
		txtNewUserEmail.setBounds(114, 48, 185, 31);
		panel.add(txtNewUserEmail);

		JLabel label_1 = new JLabel("Password:");
		label_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label_1.setForeground(Color.RED);
		label_1.setFont(new Font("MV Boli", Font.PLAIN, 12));
		label_1.setBackground(Color.BLACK);
		label_1.setBounds(10, 128, 100, 31);
		panel.add(label_1);

		passwordField_1 = new JPasswordField();
		passwordField_1.setFont(new Font("High Tower Text", Font.PLAIN, 20));
		passwordField_1.setBackground(Color.CYAN);
		passwordField_1.setToolTipText("Password should match the following attributes:\r\n-Atleast 8 characters long\r\n-One or more digits\r\n-One or more special character\r\n-At least one upper case letter and one lower case letter\n-At least one upper case letter and one lower case letter");
		passwordField_1.setBounds(114, 132, 185, 31);
		panel.add(passwordField_1);

		JLabel lblUserName = new JLabel("User Name:");
		lblUserName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUserName.setForeground(Color.RED);
		lblUserName.setFont(new Font("MV Boli", Font.PLAIN, 12));
		lblUserName.setBackground(Color.BLACK);
		lblUserName.setBounds(10, 86, 100, 31);
		panel.add(lblUserName);

		txtNewUserUsername = new JTextField();
		txtNewUserUsername.setFont(new Font("High Tower Text", Font.PLAIN, 20));
		txtNewUserUsername.setBackground(Color.CYAN);
		txtNewUserUsername.setColumns(10);
		txtNewUserUsername.setBounds(114, 90, 185, 31);
		panel.add(txtNewUserUsername);

		lblBirthdate = new JLabel("Birthdate:");
		lblBirthdate.setHorizontalAlignment(SwingConstants.TRAILING);
		lblBirthdate.setForeground(Color.RED);
		lblBirthdate.setFont(new Font("MV Boli", Font.PLAIN, 12));
		lblBirthdate.setBackground(Color.BLACK);
		lblBirthdate.setBounds(10, 212, 100, 31);
		panel.add(lblBirthdate);

		btnSignMeUp = new JButton("Sign Me Up!!");
		btnSignMeUp.addActionListener(this);
		btnSignMeUp.setForeground(Color.BLUE);
		btnSignMeUp.setFont(new Font("Minya Nouvelle", Font.BOLD, 14));
		btnSignMeUp.setBounds(169, 338, 132, 23);
		panel.add(btnSignMeUp);

		cmbBirthDay = new JComboBox();
		cmbBirthDay.setBackground(new Color(127, 255, 212));
		cmbBirthDay.setBounds(114, 212, 41, 31);
		panel.add(cmbBirthDay);
		cmbBirthDay.addActionListener(this);

		cmbBirthMonth = new JComboBox();
		cmbBirthMonth.setBackground(new Color(127, 255, 212));
		cmbBirthMonth.setBounds(161, 212, 73, 31);
		panel.add(cmbBirthMonth);
		cmbBirthMonth.addActionListener(this);

		cmbBirthYear = new JComboBox();
		cmbBirthYear.setBackground(new Color(127, 255, 212));
		cmbBirthYear.setBounds(243, 212, 66, 31);
		panel.add(cmbBirthYear);
		cmbBirthYear.addActionListener(this);

		btnChangePassword.addActionListener(this);

		//Populate table
		GregorianCalendar cal = new GregorianCalendar(); //Create calendar
		int realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
		int realMonth = cal.get(GregorianCalendar.MONTH); //Get month
		int realYear = cal.get(GregorianCalendar.YEAR); //Get year
		//System.out.println(realDay + " "+realMonth+" "+realYear);

		cmbBirthDay.addItem(realDay); // to avoid a null pointer expression when we first populate and there is nothing selected
		cmbBirthDay.setSelectedItem(realDay);

		for (int i=2015-100; i<=2015+100; i++){
			cmbBirthYear.addItem(String.valueOf(i));
		}
		String[] months =  {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
		for (int i=0; i<=11; i++){
			cmbBirthMonth.addItem(months[i]);
		}

		populateMonthDays(realMonth, realYear);

		cmbBirthYear.setSelectedItem(String.valueOf(realYear));
		cmbBirthMonth.setSelectedIndex(realMonth);
		cmbBirthDay.setSelectedItem(realDay);

		passwordField_2 = new JPasswordField();
		passwordField_2.setFont(new Font("High Tower Text", Font.PLAIN, 20));
		passwordField_2.setBackground(Color.CYAN);
		passwordField_2.setBounds(114, 174, 185, 31);
		panel.add(passwordField_2);

		JLabel lblRetypePassword = new JLabel("Retype Password:");
		lblRetypePassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblRetypePassword.setForeground(Color.RED);
		lblRetypePassword.setFont(new Font("MV Boli", Font.PLAIN, 12));
		lblRetypePassword.setBackground(Color.BLACK);
		lblRetypePassword.setBounds(10, 170, 100, 31);
		panel.add(lblRetypePassword);

		lblGender = new JLabel("Gender:");
		lblGender.setHorizontalAlignment(SwingConstants.TRAILING);
		lblGender.setForeground(Color.RED);
		lblGender.setFont(new Font("MV Boli", Font.PLAIN, 12));
		lblGender.setBackground(Color.BLACK);
		lblGender.setBounds(10, 254, 100, 31);
		panel.add(lblGender);

		cmbGender = new JComboBox();
		cmbGender.setBackground(new Color(127, 255, 212));
		cmbGender.setBounds(114, 254, 66, 31);
		panel.add(cmbGender);
		cmbGender.addItem("Male");
		cmbGender.addItem("Female");

		lblMobile = new JLabel("Mobile Phone:");
		lblMobile.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMobile.setForeground(Color.RED);
		lblMobile.setFont(new Font("MV Boli", Font.PLAIN, 12));
		lblMobile.setBackground(Color.BLACK);
		lblMobile.setBounds(10, 296, 100, 31);
		panel.add(lblMobile);

		txtMobilePhone = new JTextField();
		txtMobilePhone.setFont(new Font("High Tower Text", Font.PLAIN, 20));
		txtMobilePhone.setBackground(Color.CYAN);
		txtMobilePhone.setColumns(10);
		txtMobilePhone.setBounds(114, 296, 185, 31);
		panel.add(txtMobilePhone);

		JLabel lblBackground = new JLabel("background");

		lblBackground.setIcon(new ImageIcon(LogInGUI.class.getResource("/img/loginPic.png")));
		
		lblBackground.setBounds(0, 0, 626, 401);
		//lblBackground.setIcon(setBackgroundImg("/img/loginPic.png", lblBackground));
		//setBackgroundImg("/img/background1.jpg", lblBackground);
		frmMycal.getContentPane().add(lblBackground);
		cmbGender.addActionListener(this);
		txtFieldUserEmail.setCursor(getCursor());



	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton){
			JButton b=(JButton) e.getSource();
			if(b==btnLogIn){
				btnLogInPress();
			}else if(b==btnChangePassword){		
				// forgot pass method
			}else if(b == btnSignMeUp){
				/*
				 * 1-check if valid email
				 * 2- check if passwords match and not empty and username not empty
				 * 3- check password strength
				 */
				if (isValidEmail(txtNewUserEmail.getText())){
					if(passwordField_1.getText().equals(passwordField_2.getText())  && passwordField_1.getText()!=null  && !txtNewUserUsername.getText().isEmpty()){
						PasswordChecker passCheck = new PasswordChecker();
						if (passCheck.isGood(passwordField_1.getText())){
							registerNewUser();
						}else {
							JOptionPane.showMessageDialog(this, "Password should match the following attributes:\n-Atleast 8 characters long\n-One or more digits\n-One or more special character");
						}					
					}else {
						JOptionPane.showMessageDialog(this, "Passwords Do Not Match or you did not enter a UserName");
					}
				}else {
					JOptionPane.showMessageDialog(this, "Invalid Email");
				}

			}
		} else if(e.getSource() instanceof JTextField)
		{
			btnLogInPress();
		}else if (e.getSource() instanceof JComboBox){
			JComboBox cmb=(JComboBox) e.getSource();
			if(cmb == cmbBirthMonth || cmb==cmbBirthYear){
				populateMonthDays(cmbBirthMonth.getSelectedIndex(), Integer.parseInt((String) cmbBirthYear.getSelectedItem()));
			}
		}
	}

	private ImageIcon setBackgroundImg(String imgName, JLabel label){
		ImageIcon icon = new ImageIcon(LogInGUI.class.getResource(imgName));
		Image img = icon.getImage();
		
		BufferedImage bi = new BufferedImage(img.getWidth(this), img.getHeight(this), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		
		g.drawImage(img, 0, 0, label.WIDTH, label.HEIGHT, null);

		return new ImageIcon(bi);
		/*ImageIcon myIcon2 = new ImageIcon(imgName);
		Image img = myIcon2.getImage();
		Image newimg = img.getScaledInstance(label.WIDTH, label.HEIGHT,  java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);*/
	}

	@SuppressWarnings("deprecation")

	private void btnLogInPress(){
		String userName_pass_encrypted = encrypter.encodeAccount(txtFieldUserEmail.getText(), passwordField.getText());
		Account authenticate = (Account) server_com.connectToServer("LogIn", userName_pass_encrypted, null, null);
		if (authenticate.isValidAccount() && !txtFieldUserEmail.equals("") && !passwordField.equals("")){
			//get user info
			MyCalGUI homepage = new MyCalGUI(server_com, authenticate);
			this.frmMycal.dispose();
		}else {
			JOptionPane.showMessageDialog(this, "Username or Password Incorrect!");
			passwordField.setText("");
		}
	}

	private void registerNewUser(){
		Account newUser = new Account();
		newUser.setEmail(txtNewUserEmail.getText());
		newUser.setUserName(txtNewUserUsername.getText());
		newUser.setPassword(passwordField_1.getText());
		newUser.setBirthday(cmbBirthDay.getSelectedItem().toString()+"/"+cmbBirthMonth.getSelectedItem().toString()+"/"+cmbBirthYear.getSelectedItem().toString());
		newUser.setGender(cmbGender.getSelectedItem().toString());
		newUser.setMobilePhone(txtMobilePhone.getText());
		String userInfo_enctrypted=null;
		String userInfo_decrypted=null;
		String reply = (String) server_com.connectToServer("SignUp", newUser, null, null);
		System.out.println(reply);
		if (reply.equals("successful"+'\n')){
			loadMainGUI(newUser);
		}else {
			JOptionPane.showMessageDialog(this, "Sign Up not successfull");
		}
	}

	private void loadMainGUI(Account user){
		MyCalGUI newUserCal= new MyCalGUI(server_com, user);	
		this.frmMycal.dispose();
	}

	private boolean isValidEmail(String email){
		boolean isValid=false;
		if((email.contains(".com") || email.contains(".edu")) && email.contains("@")){
			isValid=true;
		}
		return isValid;
	}

	private void populateMonthDays(int month, int year){

		int selectedDay= (int) cmbBirthDay.getSelectedItem();
		cmbBirthDay.removeAllItems();

		GregorianCalendar cal = new GregorianCalendar(year, month, 1);

		int nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		for (int i=1; i<=nod; i++){
			cmbBirthDay.addItem(i);
		}
		try {
			cmbBirthDay.setSelectedItem(selectedDay);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}

	}
}