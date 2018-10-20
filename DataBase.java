package Core;
/**
 * 计算器2.0更新
 * 10/17/2018
 * 引入一个状态变量，控制各个按钮
 * 解决了数字开头可能为0的问题
 * 修复了科学记数法无法从字符串转为浮点数的bug
 * 优化了除零错误的判断
 * 异常处理更加人性化
 * 完善了答案的保存，便于再次升级开发
 */
import java.util.ArrayList;

public class DataBase {

	//主屏幕显示
	private String mainText;
	int maxTextLength = 29;
	ArrayList<Double> arrayList = new ArrayList<>();
	String syn;
	double ans;

	/**
	 * 计算器状态
	 * 1. 归零  可输入数字（2），可按等号（5）
	 * 2. 正在输入单个数字  可继续输入（2），可输入二元符号（3），可按等号（5）
	 * 3. 已输入单个数字与单个二元运算符，等待数字，输入数字后（4）
	 * 4. 正在输入第二个数字，可继续输入（3），可输入等号（5）
	 * 5. 得出结果  可输入数字（1->2），可输入二元符号（3），可按等号（5）
	 * 6. 出现异常，只能按归零（1）
	 * 任何情况下可归零
	 */
	int state;
	
	public DataBase() {
		reboot();
	}
	
	public void reboot() {
		syn = "null";
		setMainText("0");
		arrayList.clear();
		ans = 0;
		state = 1;
	}
	
	//显示部分
	boolean fullScreen() {
		if(mainText.length() > maxTextLength)return true;
		return false;
	}
	
	void clearScreen() {
		setMainText("");
	}
	
	boolean willFull(String text) {
		if(mainText.length() + text.length() > maxTextLength)return true;
		return false;
	}
	
	public void addText(String text) {
		if(state == 1||state ==5) {
			clearScreen();
			state = 2;
		} else if(state == 6) {
			return;
		} else if (state == 3){
			clearScreen();
			state = 4;
		}
		if(willFull(text))return;
		mainText += text;
	}
	
	public void addDot() {
		if(willFull("."))return;
		if(state == 1||state ==5) {
			clearScreen();
			setMainText("0");
			state = 2;
		} else if(state == 6) {
			return;
		} else if (state == 3){
			clearScreen();
			setMainText("0");
			state = 4;
		}
		if(!getMainText().contains("."))mainText += ".";
	}
	
	public void addZero() {
		if(willFull("0"))return;
		if(state == 1||state ==5) {
			clearScreen();
			setMainText("0");
			state = 1;
		} else if(state == 6) {
			return;
		} else if (state == 3){
			clearScreen();
			setMainText("0");
			state = 4;
		}
		if(!getMainText().substring(0, 1).contains("0")||getMainText().contains("."))mainText += "0";
	}
	
	public void delText() {
		if(state == 3||state == 6)return;
		if(state == 5) {
			clearScreen();
			setMainText("0");
			state = 2;
		}
		int length = mainText.length();
		if(length == 1) {
			setMainText("0");
		} else {
			setMainText(mainText.substring(0, length-1));
		}
		if(getMainText().length()==1&&getMainText().substring(0,1)=="0")state = 1;
	}
	
	public String getMainText() {
		return mainText;
	}

	public void setMainText(String mainText) {
		this.mainText = mainText;
	}
	
	public void pai() {
		if(state ==3||state ==6)return;
		setMainText("3.141592653");
	}
	//计算部分
	public void equal() {
		arrayList.add(StringToNum(mainText));
		//确定是单元运算还是多元运算
		int stringNum = arrayList.size();
		//number==0||1  仅输入一个数并按下=
		if(stringNum == 0 || stringNum == 1) {
			ans = StringToNum(mainText);
			setMainText(NumToString(ans));
			state = 5;
		} 
		//number==1  二元运算
		else if(stringNum == 2) {
			double num1 = arrayList.get(0);
			double num2 = arrayList.get(1);
			double tempAns;
			switch(syn) {
			case "+":
				tempAns = num1 + num2;
				if(isNOF(tempAns))NOF();
				else {
					ans = tempAns;
					setMainText(NumToString(tempAns));
					state = 5;
				}
				break;
			case "-":
				tempAns = num1 - num2;
				if(isNOF(tempAns))NOF();
				else {
					ans = tempAns;
					setMainText(NumToString(tempAns));
					state = 5;
				}
				break;	
			case "/":
				if(Math.abs(num2) < Math.pow(10, -28))DBZ();
				else {
					tempAns = num1 / num2;
					if(isNOF(tempAns))NOF();
					else {
						ans = tempAns;
						setMainText(NumToString(tempAns));
						state = 5;
					}
				}
				break;
			case "X":
				tempAns = num1 * num2;
				if(isNOF(tempAns))NOF();
				else {
					ans = tempAns;
					setMainText(NumToString(tempAns));
					state = 5;
				}
				break;
			default:
				IO();
				break;
			}
		}
		syn = "null";
		arrayList.clear();
	}
	
	public void twoSyn(String twosyn) {
		if(state ==3||state == 6) return;
		syn = twosyn;
		arrayList.add(StringToNum(mainText));
		state = 3;
	}
	
	public void oneSyn(String onesyn) {
		if(state == 3||state == 6)return;
		double num = StringToNum(mainText);
		double tempAns;
		switch(onesyn) {
		case "%":
			tempAns = num*100;
			if(isNOF(tempAns))NOF();
			else {
				ans = tempAns;
				state = 5;
				setMainText(NumToString(tempAns));
			}
			break;
		case "√":
			tempAns = Math.pow(num, 0.5);
			if(isNOF(tempAns))NOF();
			else {
				ans = tempAns;
				state = 5;
				setMainText(NumToString(tempAns));
			}
			break;
		case "x^2":
			tempAns = Math.pow(num, 2);
			if(isNOF(tempAns))NOF();
			else {
				ans = tempAns;
				state = 5;
				setMainText(NumToString(tempAns));
			}
			break;
		case "1/x":
			if(Math.abs(num) < Math.pow(10, -28)) {
				DBZ();
				break;
			}
			tempAns = Math.pow(num, -1);
			if(isNOF(tempAns))NOF();
			else {
				ans = tempAns;
				state = 5;
				setMainText(NumToString(tempAns));
			}
			break;
		case "+/-":
			if(Math.abs(num) > Math.pow(10, -28)) {
				ans = -num;
				state = 5;
				setMainText(NumToString(-num));
			}
			break;
		default:
			IO();
			return;
		}
	}
	
	double StringToNum(String string) {
		//解决了科学计数法转化的问题
		if(string.contains("E")) {
			int E = string.indexOf("E");
			double num1 = Double.valueOf(string.substring(0, E));
			double num2 = Double.valueOf(string.substring(E+1));
			double ans = num1*Math.pow(10, num2);
			return ans;
		}
		if(string.contains("-")) {
			string = string.substring(1);
			return -Double.valueOf(string);
		}
		return Double.valueOf(string);
	}
	
	String NumToString(double num) {
		String temp = String.valueOf(num);
		if(temp.length() > maxTextLength-1)temp = temp.substring(0, maxTextLength-1);
		return temp;
	}
	
	//异常处理部分
	void NOF() {
		reboot();
		state = 6;
		setMainText("Num Over Flow, press C");
	}
	void DBZ() {
		reboot();
		state = 6;
		setMainText("Division By Zero, press C");
	}
	void IO() {
		reboot(); 
		state = 6;
		setMainText("Illegal Operation, press C");
	}
	
	boolean isNOF(double num) {
		if(Math.abs(num) > Math.pow(10, maxTextLength))return true;
		return false;
	}

}
