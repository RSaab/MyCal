/**
 * @auothor Rashad Saab
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ChatFrame implements ActionListener, Job {

	JFrame chatFrame;
	JButton b_send;
	JButton b_reset;
	JMenuBar mb;
	JMenu m1;
	JMenuItem m11, m12, m2;
	JTextField msgBox;
	JTextField sentText;

	public ChatFrame()
	{
		chatFrame=new JFrame("Chat Frame");
		chatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		chatFrame.setSize(300, 300);
		generateBottomPanel();
		generateMenuBar();
		sentText=new JTextField();
		chatFrame.add(BorderLayout.CENTER, sentText);
		chatFrame.setVisible(true);

	}
	public void generateBottomPanel(){
		JPanel bottomPanel=new JPanel(new GridLayout(1,4));
		b_send=new JButton("SEND");
		b_reset=new JButton("RESET");
		bottomPanel.add(new JLabel("Enter Text"));
		msgBox=new JTextField();
		bottomPanel.add(msgBox);
		bottomPanel.add(b_send);
		bottomPanel.add(b_reset);
		b_send.addActionListener(this);
		b_reset.addActionListener(this);
		chatFrame.add(BorderLayout.SOUTH, bottomPanel);
	}

	public void generateMenuBar()
	{
		mb=new JMenuBar();
		m1=new JMenu("FILE");
		m2=new JMenuItem("HELP");
		m11=new JMenuItem("OPEN");
		m12=new JMenuItem("SAVE AS");
		m1.add(m11);	m1.add(m12);
		mb.add(m1); 	mb.add(m2);
		m11.addActionListener(this);
		m12.addActionListener(this);
		m2.addActionListener(this);
		chatFrame.add(BorderLayout.NORTH, mb);
	}

	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton)
		{
			JButton b=(JButton)e.getSource();
			if(b==b_send)
			{
				JOptionPane.showConfirmDialog(b, "You clicked send", "button click event handler", JOptionPane.PLAIN_MESSAGE);
				
				sentText.setText(msgBox.getText());
			}else if(b==b_reset)
			{
				JOptionPane.showConfirmDialog(b, "You clicked reset", "button click event handler", JOptionPane.PLAIN_MESSAGE);
			}
		}else if(e.getSource() instanceof JMenuItem)
		{
			JMenuItem m=(JMenuItem) e.getSource();
			if(m==m11)
			{
				JOptionPane.showConfirmDialog(m, "You clicked open", "button click event handler", JOptionPane.PLAIN_MESSAGE);
			}else if(m==m12)
			{
				JOptionPane.showConfirmDialog(m, "You clicked save as", "button click event handler", JOptionPane.PLAIN_MESSAGE);
			}else if(m==m2)
			{
				JOptionPane.showConfirmDialog(m, "You clicked help", "button click event handler", JOptionPane.PLAIN_MESSAGE);
			}
		}

	}
	
	/*test main*/
	public static void main(String[] args)
	{
		ChatFrame app=new ChatFrame();
	}
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
	}
}