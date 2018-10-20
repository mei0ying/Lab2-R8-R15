package Core;
/**
 * ������2.0����
 * 10/17/2018
 * ����һ��״̬���������Ƹ�����ť
 * ��������ֿ�ͷ����Ϊ0������
 * �޸��˿�ѧ�������޷����ַ���תΪ��������bug
 * �Ż��˳��������ж�
 * �쳣����������Ի�
 * �����˴𰸵ı��棬�����ٴ���������
 */
import java.util.ArrayList;

public class DataBase {

	//����Ļ��ʾ
	private String mainText;
	int maxTextLength = 29;
	ArrayList<Double> arrayList = new ArrayList<>();
	String syn;
	double ans;

	/**
	 * ������״̬
	 * 1. ����  ���������֣�2�����ɰ��Ⱥţ�5��
	 * 2. �������뵥������  �ɼ������루2�����������Ԫ���ţ�3�����ɰ��Ⱥţ�5��
	 * 3. �����뵥�������뵥����Ԫ��������ȴ����֣��������ֺ�4��
	 * 4. ��������ڶ������֣��ɼ������루3����������Ⱥţ�5��
	 * 5. �ó����  ���������֣�1->2�����������Ԫ���ţ�3�����ɰ��Ⱥţ�5��
	 * 6. �����쳣��ֻ�ܰ����㣨1��
	 * �κ�����¿ɹ���
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
	
	//��ʾ����
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
	//���㲿��
	public void equal() {
		arrayList.add(StringToNum(mainText));
		//ȷ���ǵ�Ԫ���㻹�Ƕ�Ԫ����
		int stringNum = arrayList.size();
		//number==0||1  ������һ����������=
		if(stringNum == 0 || stringNum == 1) {
			ans = StringToNum(mainText);
			setMainText(NumToString(ans));
			state = 5;
		} 
		//number==1  ��Ԫ����
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
		case "��":
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
		//����˿�ѧ������ת��������
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
	
	//�쳣������
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
