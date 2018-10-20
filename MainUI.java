package UI;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import Core.DataBase;

public class MainUI extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DataBase dataBase = new DataBase();
	
	int x = 100;
	int y = 30;
	int space = 5;
	int textWidth = 4*x + 3*space;
	int textHeight = 40;
	
	
	int width = 430;
	int height = 330;
	
	JTextField jTextField;
	JButton[][] jButton = new JButton[6][4];
	String[][] buttonString = {{"%","√","x^2","1/x"},{"C","π","<-","/"},{"7","8","9","X"},
			{"4","5","6","-"},{"1","2","3","+"},{"+/-","0",".","="}};
	
	public void start() {
		
		this.setLayout(null);
		//文本框，不可修改，右对齐，样式待修改
		jTextField = new JTextField(dataBase.getMainText());
		jTextField.setEditable(false);
		jTextField.setFont(new Font("宋体", Font.BOLD, 25));
		jTextField.setHorizontalAlignment(JTextField.RIGHT);
		jTextField.setBounds(space, y, textWidth, textHeight);
		this.add(jTextField);
		
		//按钮，样式待修改，使用setIcon函数可设置按钮图片完成自定义按钮
		for(int i = 0; i < 6; i++)
			for(int j = 0; j < 4; j++) {
				jButton[i][j] = new JButton(buttonString[i][j]);
				jButton[i][j].addActionListener(this);
				jButton[i][j].setBounds(space*(j+1)+x*j, y*(i+1)+textHeight+space*(i+1), x, y);
				this.add(jButton[i][j]);
			}
		this.setSize(width, height);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.setLocation((1920- width)/2, (1080-height)/2);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
//		String[][] buttonString = {{"%","√","x^2","1/x"},{"C","π","<-","/"},{"7","8","9","X"},
//				{"4","5","6","-"},{"1","2","3","+"},{"+/-","0",".","="}};
		String temp = arg0.getActionCommand();
		if(temp.length()==1&&temp.charAt(0)<58&&temp.charAt(0)>48) {
			dataBase.addText(temp);
		}else {
			switch(temp) {
			case "0":
				dataBase.addZero();
				break;
			case ".":
				dataBase.addDot();
				break;
			case "C":
				dataBase.reboot();
				break;
			case "<-":
				dataBase.delText();
				break;
			case "/":
			case "X":
			case "+":
			case "-":
				dataBase.twoSyn(temp);
				break;
			case "=":
				dataBase.equal();
				break;
			case "π":
				dataBase.pai();
				break;
			default:
				dataBase.oneSyn(temp);
				break;
			}
		}
		jTextField.setText(dataBase.getMainText());
	}
	
	public static void main(String []args) {
		MainUI mainUI = new MainUI();
		mainUI.start();
	}
}
