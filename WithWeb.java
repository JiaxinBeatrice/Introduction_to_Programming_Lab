package java_final;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collector.Characteristics;

import javax.swing.*;

public class WithWeb extends Single {
	boolean onTurn,noMove,isFull;
	WebLimit webLimit;
	ServerSocket server;
	Socket mySocket;
	BufferedReader is;
	PrintWriter os;
	String line;
	JTextField intputbox = new JTextField();
	JButton send = new JButton("送出");
	
	String IP; int port; boolean isServer;
	
	public WithWeb(String IP, int port, boolean isServer){
		this.IP = IP;
		this.port = port;
		this.isServer = isServer;
	}
	
	void go(){
		goThread thread = new goThread(this);
		thread.start();
		addMouseListener(this);	
		back.addActionListener(this);
		undo.addActionListener(this);	
	}
	
	JPanel getMsg(){
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3, 1));
		msg.setFont(new Font("標楷體", Font.PLAIN, 20));
		text_time.setFont(new Font("Harrington", Font.PLAIN, 60));
		
		msg.setOpaque(false);
		text_time.setOpaque(false);
		
		msg.setForeground(new Color(118, 232, 190));
		text_time.setForeground(Color.LIGHT_GRAY);
		
		msg.setEditable(false);
		text_time.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(msg, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
			
		p.add(text_time);
		p.add(scrollPane);
		p.add(getBox());
		p.setOpaque(false);
		return p;
	}
	
	JPanel getBox(){
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3,1));
		JLabel label = new JLabel("輸入框");
		label.setFont(new Font("標楷體", Font.PLAIN, 20));
		label.setForeground(new Color(118, 232, 190));
		
		intputbox.setFont(new Font("標楷體", Font.PLAIN, 20));
		intputbox.setOpaque(false);
		intputbox.setForeground(new Color(118, 232, 190));
		
		send.setContentAreaFilled(false);
		send.setForeground(new Color(118, 232, 190));
		
		send.addActionListener(this);
		
		p.add(label);
		p.add(intputbox);
		p.add(send);
		p.setOpaque(false);
		return p;
	}
	
	void sendInfo(){
		int i,j;
		os.println("info");
		for(i=0;i<8;i++){
			for(j=0;j<8;j++){
				os.print(chess[i][j]);
				//System.out.print(chess[i][j]);
			}
			os.println();
			//System.out.println();
		}
		os.println(num_b);os.println(num_w);
		os.println(xVal);os.println(yVal);
		os.println(Boolean.toString(noMove));
		os.println(Boolean.toString(isFull));
		
		os.flush();		
	}
	
	public void actionPerformed(ActionEvent e1){
		Object obj = e1.getSource();
		
		if(obj == back){
			int ans;
			ans = JOptionPane.showConfirmDialog(board, "放棄此局?",
					"",JOptionPane.YES_NO_OPTION);
			if(ans == JOptionPane.YES_OPTION){
				if(mySocket != null &&!mySocket.isClosed()){
					try {
						mySocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(isServer){
					try {
						server.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
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
		
		if(obj == undo){
			if(onTurn){				
				int num = forUndo.size();
				if(num<1){
					msg.append("棋都還沒下，悔棋失敗\n");
				}
				else{
					pause = true;
					os.println("undo_confirm");
					os.flush();
					msg.append("等待對方回覆\n");
				}
			}
			else{
				msg.append("還沒輪到您，悔棋失敗\n");
			}
		}
		
		if(obj == send){
			if(intputbox.getText()!= null){
				msg.append("-"+intputbox.getText()+"\n");
				os.println("msg");
				os.println("："+intputbox.getText());
				os.flush();
				intputbox.setText("");
			}
		}
	}
	
	public void mouseClicked(MouseEvent e2){
		int i, j;
		//System.out.println(e2.getX() + " " +e2.getY()+" "+ new Date());
		
		if(e2.getX()>280 && e2.getX()<670 && 
				e2.getY()>105 && e2.getY()<505 && onTurn){
			xVal = (e2.getX()-280)/50;
			yVal = (e2.getY()-105)/50;
			if(chess[xVal][yVal]==3){
				String s="";
				for(i=0;i<8;i++){
					for(j=0;j<8;j++)s = s+Integer.toString(chess[i][j]);
				}
				s = s+" "+Integer.toString(num_b)+" "+Integer.toString(num_w);
				forUndo.add(s);
				if(isBlack){
					chess[xVal][yVal]=1;
					num_b++;
					text.append("黑："+Character.toString((char)(xVal+'A'))
						+Character.toString((char)(yVal+'1'))+"\n");
				}
					
				else{
					chess[xVal][yVal]=2;
					num_w++;
					text.append("白："+Character.toString((char)(xVal+'A'))
						+Character.toString((char)(yVal+'1'))+"\n");					
				}
				
				//System.out.println(xVal + " " + yVal);
				flipover();
				setNumText();
				clearRed();
				((getBoard)board).input(chess);
				repaint();
								
				if(timeSetted){
					text_time.setText("");
					t.cancel();
					t.purge();
				}
				
				for(i=0,isFull=true;i<8;i++){
					if(!isFull)break;
					for(j=0;j<8;j++){
						if(chess[i][j]==0||chess[i][j]==3)isFull=false;
					}
				}
				
				if(isFull){
					msg.append("遊戲結束\n");
					win();
					if(timeSetted){
						text_time.setText("");
						t.cancel();
						t.purge();
					}
					try {
						sendInfo();
						msg.append("連線中斷，請返回主選單繼續遊戲\n");
					} catch (Exception e) {
						JOptionPane.showMessageDialog(board, e);
					}
				}
				onTurn = false;
				status.setText("");
				sendInfo();
				//System.out.println(isServer +" "+ new Date());	
			}
		}	
	}
	
	public void mouseEntered(MouseEvent e2){}	
	public void mouseExited(MouseEvent e2){}	
	public void mousePressed(MouseEvent e2){}	
	public void mouseReleased(MouseEvent e2){}	
}

class goThread extends Thread{
	WithWeb web;
	
	public goThread(WithWeb web) {
		this.web = web;
	}
	
	public void run() {
		web.setTitle("黑白棋");
		
		web.setVisible(true);
		web.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		ImageIcon bg = new ImageIcon("wood.jpg");
		JLabel label = new JLabel(bg);
		label.setBounds(225, 0, bg.getIconWidth()-250, bg.getIconHeight()-100);
		web.getLayeredPane().add(label,new Integer(-30001));
		
		ImageIcon bg_main = new ImageIcon("bg.jpg");
		JLabel label_main = new JLabel(bg_main);
		label_main.setBounds(0, 0, bg_main.getIconWidth(), bg_main.getIconHeight());
		web.getLayeredPane().add(label_main,new Integer(-30002));
		
		
		JPanel cp = (JPanel)web.getContentPane();
		cp.setOpaque(false);
		
		cp.setLayout(new BorderLayout());
		web.board.setOpaque(false);
		cp.add("Center",web.board);
		cp.add("East",web.getInfo());
		cp.add("West",web.getMsg());
		
		JMenuBar bar = new JMenuBar();
		bar.add(web.back);bar.add(web.undo);
		web.setJMenuBar(bar);		
		
		web.onTurn = false;
		web.status.setText("");
		web.chess[3][4]=1;web.chess[4][3]=1;
		web.chess[3][3]=2;web.chess[4][4]=2;
		
		web.setSize(1000, bg_main.getIconHeight());
		
		if(web.isServer){
			web.server = null;
			web.isBlack = false;
			((getBoard)web.board).input(web.chess);
			web.repaint();
			try {
				web.server=new ServerSocket(web.port);
			}catch (Exception e) {
				JOptionPane.showMessageDialog(web.board, "server "+e);
			}
			web.mySocket=null;
			try {
				web.mySocket=web.server.accept();
				web.is=new BufferedReader(new InputStreamReader(web.mySocket.getInputStream()));
				web.os=new PrintWriter(web.mySocket.getOutputStream());	
				
				while(JOptionPane.showConfirmDialog(web.board, "您要設置思考時間限制嗎?",
						"",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					web.setTime();
					if(web.canSetTime)continue;
					else break;
				}
				web.os.println("time");
				web.os.println(Boolean.toString(web.timeSetted));
				if(web.timeSetted)web.os.println(Integer.toString(web.second));
				web.os.flush();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(web.board, "server "+e);
			}
		}
		else{
			web.isBlack = true;
			web.chess[3][2]=3;web.chess[2][3]=3;
			web.chess[5][4]=3;web.chess[4][5]=3;
			((getBoard)web.board).input(web.chess);
			web.repaint();
			try{
				web.mySocket=new Socket(web.IP,web.port);
				web.os=new PrintWriter(web.mySocket.getOutputStream());
				web.is=new BufferedReader(new InputStreamReader(web.mySocket.getInputStream()));
			}catch(Exception e){
				JOptionPane.showMessageDialog(web.board, "client "+e);
			}
		}
		while(true){
			try{
				if(web.is == null){
					break;
				}
				web.line=web.is.readLine();
				if(web.line.equals("info")){
					web.line = web.is.readLine();
					for(int i=0;i<8;i++){
						for(int j=0;j<8;j++){
							web.chess[i][j]=(short)(web.line.charAt(j)-'0');
						}
						web.line=web.is.readLine();
					}
					web.num_b = Integer.parseInt(web.line);
					web.num_w = Integer.parseInt(web.is.readLine());
					int x = Integer.parseInt(web.is.readLine());
					int y = Integer.parseInt(web.is.readLine());
					web.noMove = Boolean.parseBoolean(web.is.readLine());
					web.isFull = Boolean.parseBoolean(web.is.readLine());
					web.os.println(web.num_b);web.os.println(web.num_w);
					web.os.println(web.xVal);web.os.println(web.yVal);
					web.os.println(Boolean.toString(web.noMove));
					web.os.println(Boolean.toString(web.isFull));					
					
					
					if(web.isServer)
						web.text.append("黑："+Character.toString((char)(x+'A'))
								+Character.toString((char)(y+'1'))+"\n");
					else
						web.text.append("白："+Character.toString((char)(x+'A'))
								+Character.toString((char)(y+'1'))+"\n");
					
					if(web.isFull){
						((getBoard)web.board).input(web.chess);
						web.setNumText();
						web.repaint();						
						web.msg.append("遊戲結束\n");
						web.win();
						if(web.timeSetted){
							web.text_time.setText("");
							web.t.cancel();
							web.t.purge();
						}						
						web.mySocket.close();
						if(web.isServer)web.server.close();
						web.msg.append("連線中斷，請返回主選單繼續遊戲\n");
						break;
					}
					if(web.valid()){
						if(web.noMove)web.msg.append("對方無棋可下\n");
						web.noMove = false;
						web.onTurn=true;
						web.status.setText("換您了");
					}						
					else {
						if(web.noMove){
							web.msg.append("遊戲結束\n");
							web.win();
							if(web.timeSetted){
								web.text_time.setText("");
								web.t.cancel();
								web.t.purge();
							}							
							web.sendInfo();
							web.mySocket.close();
							if(web.isServer)web.server.close();
							web.msg.append("連線中斷，請返回主選單繼續遊戲\n");
							break;
						}
						else{
							web.msg.append("無棋可下，棄權\n");
							web.clearRed();
							web.noMove = true;
							web.sendInfo();						
						}
					}
					
					if(web.timeSetted){
						web.t = new Timer();
						web.time = web.second;
						web.text_time.setText(" "+Integer.toString(web.time));
						web.webLimit = new WebLimit(web);
						web.t.schedule(web.webLimit,1000l,1000l);
					}
					((getBoard)web.board).input(web.chess);
					web.setNumText();
					web.repaint();
				}
				else if(web.line.equals("time")){
					web.onTurn = true;
					web.status.setText("換您了");
					JOptionPane.showMessageDialog(web.board, "遊戲開始");
					web.line = web.is.readLine();
					if(Boolean.parseBoolean(web.line)){
						web.t = new Timer();
						web.line = web.is.readLine();
						web.timeSetted = true;
						web.second = Integer.parseInt(web.line);
						web.time = web.second;
						web.text_time.setText(" "+web.line);
						web.webLimit = new WebLimit(web);
						web.t.schedule(web.webLimit, 1000l,1000l);
						web.msg.setText("對方已設置"+web.line+"秒之思考時間\n");
						web.msg.append("現在可以下棋了，請您把握時間\n");
						
					}
					else{
						web.timeSetted = false;
						web.msg.setText("對方沒有設置思考時間限制\n");
						web.msg.append("現在可以下棋了\n");
					}
				}
				else if(web.line.equals("msg")){
					web.line = web.is.readLine();
					web.msg.append(web.line+"\n");

				}
				else if (web.line.equals("undo_confirm")){
					int ans = JOptionPane.showConfirmDialog(web.board, "同意對方悔棋請求嗎?",
							"",JOptionPane.YES_NO_OPTION);
					web.os.println("undo_ans");
					web.os.println(Integer.toString(ans));
					web.os.flush();
					
					if(ans == JOptionPane.YES_OPTION)
						web.forUndo.remove(web.forUndo.size()-1);
				}
				else if (web.line.equals("undo_ans")){
					web.pause = false;
					web.line = web.is.readLine();
					String s;
					int num = web.forUndo.size();
					int undo_ans = Integer.parseInt(web.line);
					if(undo_ans==JOptionPane.YES_OPTION){
						s = web.forUndo.get(num-1);
						String undo[] = s.split(" ");
						for(int i=0;i<8;i++){
							for(int j=0;j<8;j++)
								web.chess[i][j]=(short)(s.charAt(i*8+j)-'0');
						}
						web.num_b = Integer.parseInt(undo[1]);
						web.num_w = Integer.parseInt(undo[2]);
						web.setNumText();
						web.msg.append("悔棋成功\n");
						web.text.append("悔棋\n");
						web.forUndo.remove(num-1);
						((getBoard)web.board).input(web.chess);
						web.repaint();
						
						if(web.timeSetted){
							web.t.cancel();
							web.t.purge();
							web.t = new Timer();
							web.time = web.second;
							web.text_time.setText(" "+Integer.toString(web.time));
							//System.out.println(new Date()+" 4");
							web.webLimit = new WebLimit(web);
							web.t.schedule(web.webLimit,1000l,1000l);
						}							
						
						int i,j;
						web.os.println("undo_show");
						for(i=0;i<8;i++){
							for(j=0;j<8;j++){
								web.os.print(web.chess[i][j]);
								//System.out.print(chess[i][j]);
							}
							web.os.println();
							//System.out.println();
						}
						web.os.println(web.num_b);web.os.println(web.num_w);
						
						web.os.flush();
					}
					else{
						web.msg.append("對方拒絕，悔棋失敗\n");
					}					
				}
				else if (web.line.equals("undo_show")){
					web.line = web.is.readLine();
					for(int i=0;i<8;i++){
						for(int j=0;j<8;j++){
							web.chess[i][j]=(short)(web.line.charAt(j)-'0');
						}
						web.line=web.is.readLine();
					}
					web.num_b = Integer.parseInt(web.line);
					web.num_w = Integer.parseInt(web.is.readLine());
					web.text.append("悔棋\n");
					web.clearRed();
					((getBoard)web.board).input(web.chess);
					web.setNumText();
					web.repaint();					
				}
			}catch (SocketException e) {
					try {
						web.mySocket.close();
						if(web.isServer)web.server.close();
						if(web.timeSetted){
							web.t.cancel();
							web.t.purge();
							web.text_time.setText("");
						}
						web.msg.append("連線中斷，請返回主選單繼續遊戲\n");
						break;
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(web.board, e1);
						break;
					}
				
			}catch (Exception e) {
				web.msg.append("連線中斷，請返回主選單繼續遊戲\n");
				web.onTurn = false;
				//JOptionPane.showMessageDialog(web.board,"remote "+ e);
				break;
			}
		}
	}
}

class WebLimit extends TimerTask{
	WithWeb web;
	int t;
	public WebLimit(WithWeb web) {
		this.web = web;
	}
	public void run() {
		if(!web.pause){
			if(web.time>=0){
				web.time--;
				web.text_time.setText(" "+Integer.toString(web.time));				
			}
			else{
				web.t.cancel();
				web.t.purge();
			}
		}
		//System.out.println(new Date()+" in task");
		if(web.time == 0){
			web.text_time.setText("");
			web.t.cancel();
			web.t.purge();
			
			web.msg.append("思考過久，棄權\n");
			
			web.os.println("msg");
			web.os.println("對方思考過久，棄權");
			web.os.flush();
			
			web.onTurn = false;
			web.status.setText("");
			web.sendInfo();
			web.clearRed();
			((getBoard)web.board).input(web.chess);
			web.repaint();
		}

		
	}	
}