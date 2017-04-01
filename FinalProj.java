package java_final;

import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;	

public class FinalProj extends JFrame 
		implements ActionListener{
	JButton single = new JButton("單機兩人對戰");
	JButton server = new JButton("連線對戰：server");
	JButton client = new JButton("連線對戰：client");
	public static void main(String[] args) throws UnknownHostException{
		FinalProj demo = new FinalProj();
		demo.go();
	}
	void go() throws UnknownHostException{
		setTitle("黑白棋主選單");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ImageIcon bg = new ImageIcon("menu.jpg");
		JLabel label = new JLabel(bg);
		label.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
		getLayeredPane().add(label,new Integer(-30001));		
		
		JPanel cp = (JPanel)getContentPane();
		cp.setOpaque(false);
		JPanel blank = new JPanel();
		blank.setOpaque(false);
		cp.setLayout(new GridLayout(2,1));
		cp.add(blank);
		cp.add(getButton());
		setSize(bg.getIconWidth(), 600);
		System.out.println(InetAddress.getLocalHost());
	}
	
	public void actionPerformed(ActionEvent e){
		Object obj = e.getSource();
		if(obj == single){
			Single single = new Single();
			single.go();
			dispose();
		}
		if(obj == server){
			WithWeb web_server = new WithWeb("127.0.0.1",2000, true);
			web_server.go();
			dispose();
		}
		if(obj == client){
			String s = JOptionPane.showInputDialog(this,"欲連線IP");
			if(s!= null){
				WithWeb web_client = new WithWeb(s,2000, false);
				web_client.go();
				dispose();				
			}

		}		
	}
	
	JPanel getButton(){
		JPanel p = new JPanel();
		JLabel blank1 = new JLabel();
		JLabel blank2 = new JLabel();
		p.setLayout(new GridLayout(5, 1));
		p.add(single);
		p.add(blank1);
		p.add(server);
		p.add(blank2);
		p.add(client);
		single.setContentAreaFilled(false);
		server.setContentAreaFilled(false);
		client.setContentAreaFilled(false);
		single.addActionListener(this);
		server.addActionListener(this);
		client.addActionListener(this);
		p.setOpaque(false);
		return p;
	}
}
