package java_final;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.UnknownHostException;
import java.util.*;
import java.util.Timer;
import javax.swing.*;

public class Single extends JFrame 
		implements ActionListener,MouseListener{
	JMenuItem newGame = new JMenuItem("�s�C��");
	JMenuItem back = new JMenuItem("�^�D���");
	JMenuItem saveGame = new JMenuItem("�C���s��");
	JMenuItem openGame = new JMenuItem("�~��W��");
	JMenuItem undo = new JMenuItem("����");
	JMenuItem set = new JMenuItem("�]�w��Үɶ�");
	
	JTextArea msg = new JTextArea(25,20);
	int num_b = 2,num_w = 2;
	JTextArea text = new JTextArea(25,20);
	private JTextArea text_b = new JTextArea();
	private JTextArea text_w = new JTextArea();
	JTextArea status = new JTextArea();
	JTextArea text_time = new JTextArea();
	
	int x_adj=55, y_adj=55;
	boolean timeSetted=false,canSetTime=true;
	boolean pause=false;
	Timer t;
	Limit timeLimit;
	int second;
	int time;
	boolean undo_conti;
	
	boolean isBlack = true;
	int xVal = 0, yVal = 0;
	short[][] chess = new short[8][8];
	ArrayList<String>forUndo = new ArrayList<>();
	int undo_b=0,undo_w=0;
	JPanel board = new getBoard(x_adj,y_adj);
	
	public static void main(String[] args){
		Single demo = new Single();
		demo.go();
	}
	
	void go(){
		setTitle("�¥մ�");
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		ImageIcon bg = new ImageIcon("wood.jpg");
		JLabel label = new JLabel(bg);
		label.setBounds(225, 0, bg.getIconWidth()-250, bg.getIconHeight()-100);
		getLayeredPane().add(label,new Integer(-30001));
		
		ImageIcon bg_main = new ImageIcon("bg.jpg");
		JLabel label_main = new JLabel(bg_main);
		label_main.setBounds(0, 0, bg_main.getIconWidth(), bg_main.getIconHeight());
		getLayeredPane().add(label_main,new Integer(-30002));
		
		JPanel cp = (JPanel)getContentPane();
		cp.setOpaque(false);
		
		cp.setLayout(new BorderLayout());
		board.setOpaque(false);
		cp.add("Center",board);
		cp.add("East",getInfo());
		cp.add("West",getMsg());
		
		JMenuBar bar = new JMenuBar();
		bar.add(newGame);bar.add(saveGame);
		bar.add(openGame);bar.add(undo);
		bar.add(set);bar.add(back);
		setJMenuBar(bar);		
		
		setSize(1000, 600);
		
		newGame.addActionListener(this);
		saveGame.addActionListener(this);
		openGame.addActionListener(this);
		undo.addActionListener(this);
		set.addActionListener(this);
		back.addActionListener(this);
		
		forUndo.add(new String());
		forUndo.add(new String());
		
		chess[3][4]=1;chess[4][3]=1;
		chess[3][3]=2;chess[4][4]=2;
		valid();
		((getBoard)board).input(chess);
		
		addMouseListener(this);
	}
	
	JPanel getInfo(){
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(5, 2));
		
		text_b.setFont(new Font("�з���", Font.PLAIN, 36));
		text_w.setFont(new Font("�з���", Font.PLAIN, 36));
		text_b.setForeground(Color.WHITE);
		text_w.setForeground(Color.WHITE);
		status.setForeground(new Color(255, 107, 248));
		status.setFont(new Font("�L�n������", Font.PLAIN, 36));
		text.setFont(new Font("�з���", Font.PLAIN, 20));
		text.setOpaque(false);
		text.setForeground(Color.YELLOW);		
		
		setNumText();
		if(isBlack)status.setText("����¦�");
		else status.setText("����զ�");
		
		text.setEditable(false);
		text_b.setEditable(false);
		text_w.setEditable(false);
		status.setEditable(false);
		
		text_b.setOpaque(false);
		text_w.setOpaque(false);
		status.setOpaque(false);
		
		JScrollPane scrollPane = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		
		p.add(status);
		p.add(text_b);
		p.add(text_w);
		p.add(scrollPane);
		p.setOpaque(false);
		return p;
	}
	
	JPanel getMsg(){
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3, 1));
		msg.setFont(new Font("�з���", Font.PLAIN, 20));
		text_time.setFont(new Font("Harrington", Font.PLAIN, 60));
		msg.setOpaque(false);
		text_time.setOpaque(false);
		msg.setForeground(Color.YELLOW);
		text_time.setForeground(Color.LIGHT_GRAY);
		msg.setEditable(false);
		text_time.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(msg, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
	
		p.add(text_time);
		p.add(scrollPane);
		p.setOpaque(false);
		return p;
	}
	
	public void flipover(){
		int i;
		if(isBlack){
			i=1;
			while(xVal+i<8 && chess[xVal+i][yVal]==2){
				i++;
			}
			if(xVal+i<8 && chess[xVal+i][yVal]==1){
				while(i>1){
					i--;
					chess[xVal+i][yVal]=1;
					num_b++;
					num_w--;
				}
			}
			
			i=1;
			while(xVal-i>=0 && chess[xVal-i][yVal]==2){
				i++;
			}
			if(xVal-i>=0 && chess[xVal-i][yVal]==1){
				while(i>1){
					i--;
					chess[xVal-i][yVal]=1;
					num_b++;
					num_w--;
				}
			}
			
			i=1;
			while(yVal+i<8 && chess[xVal][yVal+i]==2){
				i++;
			}
			if(yVal+i<8 && chess[xVal][yVal+i]==1){
				while(i>1){
					i--;
					chess[xVal][yVal+i]=1;
					num_b++;
					num_w--;
				}
			}			
			
			i=1;
			while(yVal-i>=0 && chess[xVal][yVal-i]==2){
				i++;
			}
			if(yVal-i>=0 && chess[xVal][yVal-i]==1){
				while(i>1){
					i--;
					chess[xVal][yVal-i]=1;
					num_b++;
					num_w--;
				}
			}	
			
			i=1;
			while(xVal+i<8 && yVal+i<8 && chess[xVal+i][yVal+i]==2){
				i++;
			}
			if(xVal+i<8 && yVal+i<8 && chess[xVal+i][yVal+i]==1){
				while(i>1){
					i--;
					chess[xVal+i][yVal+i]=1;
					num_b++;
					num_w--;
				}
			}		
			
			i=1;
			while(xVal-i>=0 && yVal-i>=0 && chess[xVal-i][yVal-i]==2){
				i++;
			}
			if(xVal-i>=0 && yVal-i>=0 && chess[xVal-i][yVal-i]==1){
				while(i>1){
					i--;
					chess[xVal-i][yVal-i]=1;
					num_b++;
					num_w--;
				}
			}				
			
			i=1;
			while(xVal-i>=0 && yVal+i<8 && chess[xVal-i][yVal+i]==2){
				i++;
			}
			if(xVal-i>=0 && yVal+i<8 && chess[xVal-i][yVal+i]==1){
				while(i>1){
					i--;
					chess[xVal-i][yVal+i]=1;
					num_b++;
					num_w--;
				}
			}	
			
			i=1;
			while(xVal+i<8 && yVal-i>=0 && chess[xVal+i][yVal-i]==2){
				i++;
			}	
			if(xVal+i<8 && yVal-i>=0 && chess[xVal+i][yVal-i]==1){
				while(i>1){
					i--;
					chess[xVal+i][yVal-i]=1;
					num_b++;
					num_w--;
				}
			}				
		}
		else{
			i=1;
			while(xVal+i<8 && chess[xVal+i][yVal]==1){
				i++;
			}
			if(xVal+i<8 && chess[xVal+i][yVal]==2){
				while(i>1){
					i--;
					chess[xVal+i][yVal]=2;
					num_b--;
					num_w++;
				}
			}
			
			i=1;
			while(xVal-i>=0 && chess[xVal-i][yVal]==1){
				i++;
			}
			if(xVal-i>=0 && chess[xVal-i][yVal]==2){
				while(i>1){
					i--;
					chess[xVal-i][yVal]=2;
					num_b--;
					num_w++;
				}
			}
			
			i=1;
			while(yVal+i<8 && chess[xVal][yVal+i]==1){
				i++;
			}
			if(yVal+i<8 && chess[xVal][yVal+i]==2){
				while(i>1){
					i--;
					chess[xVal][yVal+i]=2;
					num_b--;
					num_w++;
				}
			}			
			
			i=1;
			while(yVal-i>=0 && chess[xVal][yVal-i]==1){
				i++;
			}
			if(yVal-i>=0 && chess[xVal][yVal-i]==2){
				while(i>1){
					i--;
					chess[xVal][yVal-i]=2;
					num_b--;
					num_w++;
				}
			}	
			
			i=1;
			while(xVal+i<8 && yVal+i<8 && chess[xVal+i][yVal+i]==1){
				i++;
			}
			if(xVal+i<8 && yVal+i<8 && chess[xVal+i][yVal+i]==2){
				while(i>1){
					i--;
					chess[xVal+i][yVal+i]=2;
					num_b--;
					num_w++;
				}
			}		
			
			i=1;
			while(xVal-i>=0 && yVal-i>=0 && chess[xVal-i][yVal-i]==1){
				i++;
			}
			if(xVal-i>=0 && yVal-i>=0 && chess[xVal-i][yVal-i]==2){
				while(i>1){
					i--;
					chess[xVal-i][yVal-i]=2;
					num_b--;
					num_w++;
				}
			}				
			
			i=1;
			while(xVal-i>=0 && yVal+i<8 && chess[xVal-i][yVal+i]==1){
				i++;
			}
			if(xVal-i>=0 && yVal+i<8 && chess[xVal-i][yVal+i]==2){
				while(i>1){
					i--;
					chess[xVal-i][yVal+i]=2;
					num_b--;
					num_w++;
				}
			}	
			
			i=1;
			while(xVal+i<8 && yVal-i>=0 && chess[xVal+i][yVal-i]==1){
				i++;
			}	
			if(xVal+i<8 && yVal-i>=0 && chess[xVal+i][yVal-i]==2){
				while(i>1){
					i--;
					chess[xVal+i][yVal-i]=2;
					num_b--;
					num_w++;
				}
			}				
		}
	}

	public void clearRed(){
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				if(chess[i][j]==3){
					chess[i][j]=0;
				}
			}
		}		
	}
	
	public void setTime(){
		String s;
		s = JOptionPane.showInputDialog(board,"�@�⪺��Үɶ�?(��)");
		try{
			if(s != null){
				second = Integer.parseInt(s);
				if(second == 0){
					JOptionPane.showMessageDialog(board, "�����J0");
				}
				else{
					timeSetted = true;
					canSetTime = false;
					t = new Timer();
					time = second;			
				}
			}
		}catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(board, "�п�J���");
		}
	}
	
	public boolean valid(){
		int i, j, k;
		boolean canMove=false;
		clearRed();
		for(i=0;i<8;i++){
			for(j=0;j<8;j++){
				if(isBlack){
					if(chess[i][j]==1){
						k=1;
						while(i+k<7 && chess[i+k][j]==2){
							if(i+k<7)k++;
						}
						if(k>1&&chess[i+k][j]==0){
							chess[i+k][j]=3;
							canMove = true;
						}
							
						
						k=1;
						while(i-k>0 && chess[i-k][j]==2){
							if(i-k>0)k++;
						}
						if(k>1&&chess[i-k][j]==0){
							chess[i-k][j]=3;
							canMove = true;
						}
						
						k=1;
						while(j+k<7&&chess[i][j+k]==2){
							if(j+k<7)k++;
						}
						if(k>1&&chess[i][j+k]==0){
							chess[i][j+k]=3;
							canMove = true;
						}	
						
						k=1;
						while(j-k>0&&chess[i][j-k]==2){
							if(j-k>0)k++;
						}
						if(k>1&&chess[i][j-k]==0){
							chess[i][j-k]=3;
							canMove = true;
						}
						
						k=1;
						while(i+k<7&&j+k<7&&chess[i+k][j+k]==2){
							if(i+k<7&&j+k<7)k++;
						}
						if(k>1&&chess[i+k][j+k]==0){
							chess[i+k][j+k]=3;
							canMove = true;
						}	
						
						k=1;
						while(i-k>0&&j-k>0&&chess[i-k][j-k]==2){
							if(i-k>0&&j-k>0)k++;
						}
						if(k>1&&chess[i-k][j-k]==0){
							chess[i-k][j-k]=3;
							canMove = true;
						}
						
						k=1;
						while(i+k<7&&j-k>0&&chess[i+k][j-k]==2){
							if(i+k<7&&j-k>0)k++;
						}
						if(k>1&&chess[i+k][j-k]==0){
							chess[i+k][j-k]=3;
							canMove = true;
						}
						
						k=1;
						while(i-k>0&&j+k<7&&chess[i-k][j+k]==2){
							if(i-k>0&&j+k<7)k++;
						}
						if(k>1&&chess[i-k][j+k]==0){
							chess[i-k][j+k]=3;
							canMove = true;
						}
					}
				}
				else{
					if(chess[i][j]==2){
						k=1;
						while(i+k<7&&chess[i+k][j]==1){
							if(i+k<7)k++;
						}
						if(k>1&&chess[i+k][j]==0){
							chess[i+k][j]=3;
							canMove = true;
						}
							
						
						k=1;
						while(i-k>0&&chess[i-k][j]==1){
							if(i-k>0)k++;
						}
						if(k>1&&chess[i-k][j]==0){
							chess[i-k][j]=3;
							canMove = true;
						}
						
						k=1;
						while(j+k<7&&chess[i][j+k]==1){
							if(j+k<7)k++;
						}
						if(k>1&&chess[i][j+k]==0){
							chess[i][j+k]=3;
							canMove = true;
						}	
						
						k=1;
						while(j-k>0&&chess[i][j-k]==1){
							if(j-k>0)k++;
						}
						if(k>1&&chess[i][j-k]==0){
							chess[i][j-k]=3;
							canMove = true;
						}
						
						k=1;
						while(i+k<7&&j+k<7&&chess[i+k][j+k]==1){
							if(i+k<7&&j+k<7)k++;
						}
						if(k>1&&chess[i+k][j+k]==0){
							chess[i+k][j+k]=3;
							canMove = true;
						}	
						
						k=1;
						while(i-k>0&&j-k>0&&chess[i-k][j-k]==1){
							if(i-k>0&&j-k>0)k++;
						}
						if(k>1&&chess[i-k][j-k]==0){
							chess[i-k][j-k]=3;
							canMove = true;
						}
						
						k=1;
						while(i+k<7&&j-k>0&&chess[i+k][j-k]==1){
							if(i+k<7&&j-k>0)k++;
						}
						if(k>1&&chess[i+k][j-k]==0){
							chess[i+k][j-k]=3;
							canMove = true;
						}
						
						k=1;
						while(i-k>0&&j+k<7&&chess[i-k][j+k]==1){
							if(i-k>0&&j+k<7)k++;
						}
						if(k>1&&chess[i-k][j+k]==0){
							chess[i-k][j+k]=3;
							canMove = true;
						}														
					}					
				}
			}
		}
		return canMove;
	}	
	
	public void setNumText(){
		text_b.setText("�¡G");
		text_b.append(Integer.toString(num_b));
		text_w.setText("�աG");
		text_w.append(Integer.toString(num_w));			
	}
	
	public void win(){
		if(num_b>num_w)msg.append("�¦��\n");
		else if(num_w>num_b)msg.append("�զ��\n");
		else msg.append("����\n");	
	}
	
	public void actionPerformed(ActionEvent e1){
		Object obj = e1.getSource();
		
		if(obj == newGame){
			int ans=0;
			if(!canSetTime){
				pause = true;
				ans = JOptionPane.showConfirmDialog(board, "�T�w��󦹧�?",
						"",JOptionPane.YES_NO_OPTION);
			}
			if(canSetTime || 
					(!canSetTime && ans==JOptionPane.YES_OPTION)){
				if(timeSetted){
					t.cancel();
					t.purge();
				}
				for(int i=0;i<8;i++){
					for(int j=0;j<8;j++)chess[i][j]=0;
					chess[3][4]=1;chess[4][3]=1;
					chess[3][3]=2;chess[4][4]=2;
					num_b=2;num_w=2;
					undo_b=0;undo_w=0;
					isBlack = true;
					text.setText("");
					msg.setText("");
					text_time.setText("");
					setNumText();
					status.setText("����¦�");
					timeSetted = false;
					canSetTime = true;
					valid();
					((getBoard)board).input(chess);
					repaint();
				}				
			}
			pause = false;

		}
		if(obj == saveGame){
			try {
				pause = true;
				String name = JOptionPane.showInputDialog("��J�ɦW");
				if(name != null && name.length()>0){
					PrintStream his = new PrintStream(new File(name+".txt"));
					for(int i=0;i<8;i++){
						for(int j=0;j<8;j++)his.print(chess[i][j]);
						his.println();
					}
					his.print(num_b+" ");his.print(num_w+" ");
					his.print(undo_b+" ");his.println(undo_w);
					his.println(Boolean.toString(timeSetted));
					his.print(second+" ");his.println(time);
					his.println(Boolean.toString(isBlack));
					String s = text.getText().replaceAll("\n","\r\n");
					his.println(s);
					his.close();
					msg.append("�C���w�x�s\n");						
				}

				pause = false;	
			} catch (Exception e) {
				msg.append("�s�ɥ���\n");
			}
		}
		if(obj == openGame){
			if(timeSetted){
				t.cancel();
				t.purge();
			}
			canSetTime = false;
			text.setText("");
			
			try {
				pause = true;
				String name = JOptionPane.showInputDialog("��J�s�ɮɪ��ɦW");
				Scanner in = new Scanner(new File(name+".txt"));
				String s = in.nextLine();
				for(int i=0;i<8;i++){
					for(int j=0;j<8;j++){
						chess[i][j]=(short)(s.charAt(j)-'0');
					}
					s = in.nextLine();
				}
				String[] his_num = s.split(" ");
				num_b = Integer.parseInt(his_num[0]);
				num_w = Integer.parseInt(his_num[1]);
				undo_b = Integer.parseInt(his_num[2]);
				undo_w = Integer.parseInt(his_num[3]);
				
				s = in.nextLine();
				timeSetted = Boolean.parseBoolean(s);
				s = in.nextLine();
				if(timeSetted){
					String[] time_num = s.split(" ");
					second = Integer.parseInt(time_num[0]);
					time = Integer.parseInt(time_num[1]);
					t = new Timer();
					timeLimit = new Limit(this);
					text_time.setText(" "+Integer.toString(time));
					t.schedule(timeLimit, 1000l,1000l);
				}
				s = in.nextLine();
				isBlack = Boolean.parseBoolean(s);
				while(in.hasNextLine()){
					s = in.nextLine();
					text.append(s+"\n");
				}
				in.close();
				pause = false;
				
			} catch (Exception e) {
				msg.append("�L�k�}���ɮ� \n");
			}

			setNumText();	
			((getBoard)board).input(chess);
			repaint();
		}
		if(obj == undo){
			if(num_b+num_w<6){
				msg.append("�ѳ��٨S�U�A���ѥ���\n");
			}
			else if((isBlack && undo_b<2)||(!isBlack && undo_w<2)){
				if(timeSetted){
					t.cancel();
					t.purge();
					t = new Timer();	
					time = second;
					timeLimit = new Limit(this);
					text_time.setText(" "+Integer.toString(time));					
					//System.out.println(new Date()+" 4");
					t.schedule(timeLimit,1000l,1000l);
				}
				
				String s;
				
				if(isBlack){
					undo_b++;
					s = forUndo.get(1);
				}
				else {
					undo_w++;
					s = forUndo.get(0);
				}
				String [] undo = s.split(" ");
				for(int i=0;i<8;i++){
					for(int j=0;j<8;j++)
						chess[i][j]=(short)(s.charAt(i*8+j)-'0');
				}
				num_b = Integer.parseInt(undo[1]);
				num_w = Integer.parseInt(undo[2]);
				
				setNumText();
				text.append("����\n");
				((getBoard)board).input(chess);
				repaint();
				//System.out.println(undo_b+" "+undo_w);
			}
			else {
				pause = true;
				JOptionPane.showMessageDialog(board, "�u�஬�Ѩ⦸��");
				pause = false;
			}
		}
		if(obj == set){
			if(canSetTime){
				setTime();
				if(second>0){
					text_time.setText(" "+Integer.toString(time));
					timeLimit = new Limit(this);
					//System.out.println(new Date()+" 1");
					t.schedule(timeLimit,1000l,1000l);					
				}
			}				
			else{
				msg.append("�{�b������ɶ��]�w�A�е��U���A��\n");
			}
		}
		if(obj == back){
			int ans;
			ans = JOptionPane.showConfirmDialog(board, "��󦹧�?",
					"",JOptionPane.YES_NO_OPTION);
			if(ans == JOptionPane.YES_OPTION){
				dispose();
				FinalProj re = new FinalProj();
				try {
					re.go();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			}
		}
	}
	
	public void mouseClicked(MouseEvent e2){
		int i, j;
		boolean canMove,isFull;
		int isEnd=0;
		//System.out.println(e2.getX() + " " +e2.getY());

		
		if(e2.getX()>280 && e2.getX()<670 && 
			e2.getY()>105 && e2.getY()<505){

			xVal = (e2.getX()-280)/50;
			yVal = (e2.getY()-105)/50;
			if(chess[xVal][yVal]==3){
				undo_conti = false;
				String s="";
				for(i=0;i<8;i++){
					for(j=0;j<8;j++)s = s+Integer.toString(chess[i][j]);
				}
				s = s+" "+Integer.toString(num_b)+" " +Integer.toString(num_w);				
				if(isBlack){
					forUndo.remove(1);
					forUndo.add(1,s);					
					chess[xVal][yVal]=1;
					num_b++;
					text.append("�¡G"+Character.toString((char)(xVal+'A'))
						+Character.toString((char)(yVal+'1'))+"\n");
				}
					
				else{
					forUndo.remove(0);
					forUndo.add(0,s);					
					chess[xVal][yVal]=2;
					num_w++;
					text.append("�աG"+Character.toString((char)(xVal+'A'))
						+Character.toString((char)(yVal+'1'))+"\n");					
				}
				
				//System.out.println(xVal + " " + yVal);
				flipover();
				setNumText();
				
				isBlack = !isBlack;
				if(isBlack)status.setText("����¦�");
				else status.setText("����զ�");
				valid();
				((getBoard)board).input(chess);
				repaint();
				if(canSetTime)canSetTime = false;	
				if(timeSetted){
					t.cancel();
					t.purge();						
					t = new Timer();
					time = second;
					text_time.setText(" "+Integer.toString(time));					
					timeLimit = new Limit(this);
					//System.out.println(new Date()+" 2");
					t.schedule(timeLimit,1000l,1000l);
				}
			}
			
			for(i=0,isFull=true;i<8;i++){
				if(!isFull)break;
				for(j=0;j<8;j++){
					if(chess[i][j]==0||chess[i][j]==3)isFull=false;
				}
			}
			if(!isFull){
				for(i=0,canMove=false;i<8;i++){
					if(canMove)break;
					for(j=0;j<8;j++){
						if(chess[i][j]==3){
							canMove=true;
							isEnd=0;
						}
					}
				}
				if(!canMove){
					text.append("�L�k�ʼu�A���v\n");
					isEnd++;
					if(timeSetted){
						t.cancel();
						t.purge();
						t = new Timer();
						time = second;
						text_time.setText(" "+Integer.toString(time));						
						timeLimit = new Limit(this);					
						//System.out.println(new Date()+"3");
						t.schedule(timeLimit,1000l,1000l);
					}
					else{
						isBlack = !isBlack;
						if(isBlack)status.setText("����¦�");
						else status.setText("����զ�");						
						valid();
						((getBoard)board).input(chess);
						repaint();						
					}
				}				
			}

			if(isEnd==2||isFull){
				msg.append("�C������\n");
				win();
				if(timeSetted){
					t.cancel();
					t.purge();
				}
			}
		}
	}
	
	public void mouseEntered(MouseEvent e2){}	
	public void mouseExited(MouseEvent e2){}	
	public void mousePressed(MouseEvent e2){}	
	public void mouseReleased(MouseEvent e2){}		
}
class getBoard extends JPanel{
	short[][]chess;
	int x_adj, y_adj;
	
	public getBoard(int x_adj, int y_adj){
		super();
		setVisible(true);
		setSize(500, 500);
		this.x_adj = x_adj;
		this.y_adj = y_adj;		
	}
	
	public void input(short[][]chess){
		this.chess = chess;
	}
	
	public void paintComponent(Graphics g){
		int i,j;
		for(i=0;i<8;i++){
			for(j=0;j<8;j++){
				if(chess[i][j]==1){
					g.setColor(Color.BLACK);
					g.fillOval(i*50+x_adj, j*50+y_adj, 40, 40);
				}
				else if(chess[i][j]==2){
					g.setColor(Color.WHITE);
					g.fillOval(i*50+x_adj, j*50+y_adj, 40, 40);					
				}
				else if(chess[i][j]==3){
					g.setColor(Color.RED);
					g.fillOval(50*i+70, 50*j+75, 8, 8);
				}				
			}
		}
		
		Graphics2D d = (Graphics2D) g;
		d.setColor(Color.BLACK);
		Stroke stroke=new BasicStroke(2.0f);
		d.setStroke(stroke);
		
		g.drawRect(50, 50, 400, 400);
		g.setFont(new Font("Courier", Font.PLAIN, 36));
		g.drawString("A", 60, 35); g.drawString("B", 110, 35);
		g.drawString("C", 160, 35);g.drawString("D", 210, 35);
		g.drawString("E", 260, 35);g.drawString("F", 310, 35);
		g.drawString("G", 360, 35);g.drawString("H", 410, 35);
		g.drawString("1", 20, 85); g.drawString("2", 20, 135);
		g.drawString("3", 20, 185);g.drawString("4", 20, 235);
		g.drawString("5", 20, 285);g.drawString("6", 20, 335);
		g.drawString("7", 20, 385);g.drawString("8", 20, 435);
		for(i=0;i<7;i++){
			g.drawLine(50, 100+i*50, 450, 100+i*50);
			g.drawLine(100+i*50, 50, 100+i*50, 450);			
		}			
	}
}

class Limit extends TimerTask{
	Single single;
	int t;
	public Limit(Single single) {
		this.single = single;
	}
	public void run() {
		if(!single.pause){
			single.time--;
			single.text_time.setText(" "+Integer.toString(single.time));			
		}
		
		//System.out.println(new Date()+" in task");
		if(single.time == 0){
			single.text.append("��ҹL�[�A���v\n");
			single.isBlack = !single.isBlack;
			if(single.isBlack)single.status.setText("����¦�");
			else single.status.setText("����զ�");		
			single.valid();
			((getBoard)single.board).input(single.chess);
			single.repaint();
			single.time = single.second;
			single.text_time.setText(" "+Integer.toString(single.time));
		}

		
	}	
}
