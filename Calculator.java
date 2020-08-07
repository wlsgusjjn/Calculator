import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

class Calculator{
	StringBuilder values = new StringBuilder();
	boolean rad = true;
	boolean pwr = true;
	boolean spc = true;
	boolean Exp = false;
	boolean inf = false;
	
	Calculator(){
	}
	
	void set(String values) {
		this.values.setLength(0);
		this.values.append(values);
	}
	
	String getResult() {
		return values.toString();
	}
	
	boolean isNotNum(String x) {
		if(x.equals(".") || x.equals("p") || x.equals("e") || x.equals("E"))
			return false;
		try {
	        double d = Double.parseDouble(x);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return true;
	    }
	    return false;
	}
	
	boolean isSpc(String x) {
		if(x.equals("(") || x.equals(")") || x.equals("+") || x.equals("-") || x.equals("*") || x.equals("/") || x.equals("I"))
			return false;
		if(isNotNum(x))
			return true;
	    return false;
	}
	
	String spcVal(char x) {
		double b = 0.0;
		if(x == 'e') {
			b = Math.E;
		} else if(x == 'p') {
			b = Math.PI;
		}
		return Double.toString(b);
	}
	
	double factorial(double a) {
		double f = 1.0; 
		for(int i = (int)a; i > 0; i--) {
			f *= i;
		}
		return f;
	}
	
	String spcOper(char x, double a) {
		double b = 0.0;
		if(x == 'a') {
			b = Math.abs(a);
		} else if(x == 'c') {
			if(rad) {
				if(a % 90 == 0 && a != 0.0) {
					if (((int)a/90)%4 == 1 || ((int)a/90)%4 == 3)	b = 0;
					else if(((int)a/90)%4 == 2)	b = -1;
					else if(((int)a/90)%4 == 0)	b = 1;
				} else b = Math.cos(Math.toRadians(a));
			} else 	b = Math.cos(a);
		} else if(x == 'L') {
			b = Math.log(a);
		} else if(x == 'l') {
			b = Math.log10(a);
		} else if(x == 'R') {
			b = Math.toRadians(a);
		} else if(x == 'r') {
			b = Math.sqrt(a);
		} else if(x == 'C') {
			b = Math.cbrt(a);
		} else if(x == 's') {
			if(rad) {
				if(a % 90 == 0 && a != 0.0) {
					if (((int)a/90)%4 == 0 || ((int)a/90)%4 == 2)	b = 0;
					else if(((int)a/90)%4 == 1)	b = 1;
					else if(((int)a/90)%4 == 3)	b = -1;
				} else	b = Math.sin(Math.toRadians(a));
			} else 	b = Math.sin(a);
		} else if(x == 't') {
			if(rad) {
				if(a % 180 == 0) b = 0;
				else b = Math.tan(Math.toRadians(a));
			}	
			else 	b = Math.tan(a);
		} else if(x == 'b') {
			if(a > 1 || a < -1) return "NaN";
			else if(rad)	b = Math.asin(a) * 57.2957795131;
			else 	b = Math.asin(a);
		} else if(x == 'd') {
			if(a > 1 || a < -1) return "NaN";
			else if(rad)	b = Math.acos(a) * 57.2957795131;
			else 	b = Math.acos(a);
		} else if(x == 'f') {
			if(rad)	b = Math.atan(a) * 57.2957795131;
			else 	b = Math.atan(a);
		} else if(x == 'g') {
			b = Math.sinh(a);
		} else if(x == 'h') {
			b = Math.cosh(a);
		} else if(x == 'i') {
			b = Math.tanh(a);
		} else if(x == 'j') {
			b = factorial(a);
		}
		return Double.toString(b);
	}
	
	void changeSpcVal() {
		for(int i=0;i<values.length();i++) {
			if(values.charAt(i) == 'e' || values.charAt(i) == 'p')
				values.replace(i, i+1, spcVal(values.charAt(i)));
		}
	}
	
	void smallBki() {
		int end = 0;
		ArrayList<Integer> bki = new ArrayList<>();
		for(int i=0;i<values.length();i++) {
			if(values.charAt(i) == '(') {
				bki.add(i);
			}
		}
		for(int i=bki.size()-1;i>-1;i--) {
			for(int j=bki.get(i); j<values.length(); j++) {
				if(values.substring(j, j+1).equals("I")) {
					values.replace(0, values.length(), "Infinity");
					return;
				}
				if(values.substring(j, j+1).equals("N")) {
					values.replace(0, values.length(), "NaN");
					return;
				}
				if(isSpc(values.substring(j, j+1))) {
					end = -1;
					break;
				}
				if(values.charAt(j) == ')') {
					end = j+1;
					break;
				}
			}
			if(end != -1) {
				String rs = basicCal(values.substring(bki.get(i)+1,end-1));
				if(bki.get(i)-1 < 0)
					values.replace(bki.get(i), end, rs);
				else {
					char comp = values.charAt(bki.get(i)-1);
					if(Double.parseDouble(rs) < 0) {
						if(comp == '+')
							values.replace(bki.get(i)-1, end, rs);
						else if(comp == '-') {
							values.replace(bki.get(i)-1, end, rs);
							values.setCharAt(bki.get(i)-1, '+');
						} else
							values.replace(bki.get(i), end, rs);
					}
					else
						values.replace(bki.get(i), end, rs);
				}
			}
		}
	}
	
	void calSpcOper() {
		spc = true;
		if(values.toString().equals("Infinity"))	return;
		int end = 0, check = 0;
		ArrayList<Integer> bki = new ArrayList<>();
		for(int i=0;i<values.length();i++) {
			if(values.charAt(i) == '{') {
				bki.add(i);
			}
		}
		for(int i=bki.size()-1;i>-1;i--) {
			smallBki();
			bki.clear();
			for(int j=0;j<values.length();j++) {
				if(values.charAt(j) == '{') {
					bki.add(j);
				}
			}
			if(values.toString().equals("Infinity")) return;
			if(values.toString().equals("NaN")) return;
			for(int j=bki.get(i); j<values.length(); j++) {
				if(values.charAt(j) == '}') {
					end = j+1;
					break;
				}
			}
			String temp = values.substring(bki.get(i)+1, end-1);
			check = -1;
			double d = 0.0;
			try {
		        d = Double.parseDouble(temp);
		        check = 1;
		    } catch (NumberFormatException | NullPointerException nfe) {
		    	if(temp.equals("(Infinity)")) {
		    		values.replace(0, values.length(), "Infinity");
		    		return;
		    	} else	spc = false;
		    }
			if(check == 1) {
				String rs = spcOper(values.charAt(bki.get(i)-1),d);
				if(bki.get(i) - 2 < 0)
					values.replace(bki.get(i)-1, end, rs);
				else {
					char comp = values.charAt(bki.get(i)-2);
					if(Double.parseDouble(rs) < 0) {
						if(comp == '+')
							values.replace(bki.get(i)-2, end, rs);
						else if(comp == '-') {
							values.replace(bki.get(i)-2, end, rs);
							values.setCharAt(bki.get(i)-2, '+');
						} else
							values.replace(bki.get(i)-1, end, rs);
					}
					else
						values.replace(bki.get(i)-1, end, rs);
				}
			}
		}
	}
	
	void power() {
		pwr = true;
		int start = 0, end = 0, check = 0, count = 0, z = 0;
		for(int i=0;i<values.length();i++)
			if(values.charAt(i) == '^') 
				count++;
		for(int k=0;k<count;k++) {
			smallBki();
			for(int i=values.length()-1;i>-1;i--) {
				if(i >= values.length()) i = values.length()-1;
				if(values.charAt(i) == '^') {
					for(int j=i; j > -1; j--) {
						if(values.charAt(j) == '[') {
							start = j;
							break;
						}
					}
					for(int j=i; j<values.length(); j++) {
						if(values.charAt(j) == ']') {
							end = j+1;
							break;
						}
					}
					String temp1 = values.substring(start+1, i-1);
					String temp2 = values.substring(i+2,end-1);
					double a = 0.0, b = 0.0;
					
					try {
				        a = Double.parseDouble(temp1);
				        b = Double.parseDouble(temp2);
				        check = 1;
				    } catch (NumberFormatException | NullPointerException nfe) {
				    	check = 0;
				    	if(values.substring(start+1, i-1).equals("(Infinity)") || values.substring(i+2, end-1).equals("(Infinity)"))
				    		values.replace(start, end, "Infinity");
				    	else if(values.substring(start+1, i-1).equals("(NaN)") || values.substring(i+2, end-1).equals("(NaN)"))
				    		values.replace(start, end, "NaN");
				    	else {
				    		smallBki();
					    	z++;
				    	}
				    }
					if(check == 1) {
						String rs = Double.toString(Math.pow(a, b));
						values.replace(start, end, rs);
					}
				}
			}
		}
		if(z != 0)	pwr = false;
	}
	
	void lastBreakit() {
		values.insert(0, '(');
		values.append(')');
		int end = 0;
		ArrayList<Integer> bki = new ArrayList<>();
		for(int i = 0; i <values.length(); i++) {
			if(values.charAt(i) == '(') {
				bki.add(i);
			}
		}
		for(int i = bki.size() - 1; i > -1; i--) {
			for(int j = bki.get(i); j<values.length(); j++) {
				if(values.charAt(j) == ')') {
					end = j+1;
					break;
				}
			}
			String rs = basicCal(values.substring(bki.get(i)+1,end-1));
			if(bki.get(i)-1 < 0)
				values.replace(bki.get(i), end, rs);
			else {
				char comp = values.charAt(bki.get(i)-1);
				if(rs.length() == 0)	return;
				else if(Double.parseDouble(rs) < 0) {
					if(comp == '+')
						values.replace(bki.get(i)-1, end, rs);
					else if(comp == '-') {
						values.replace(bki.get(i)-1, end, rs);
						values.setCharAt(bki.get(i)-1, '+');
					} else
						values.replace(bki.get(i), end, rs);
				}
				else {
					values.replace(bki.get(i), end, rs);
				}
			}
		}
	}
	
	String basicCal(String str) {
		StringBuilder temp = new StringBuilder();
		temp.append(str);
		temp.append('D');
		for(int i=0;i<temp.length();i++) {
			if(temp.charAt(i) == '*' || temp.charAt(i) == '/') {
				double x = 0.0, y = 0.0, result = 0.0;
				int start=0,end=0;
				for(int j=i-1;j>=0;j--) {
					if(temp.substring(j, j+1).equals("-"))
						if(j > 0 && temp.substring(j-1, j).equals("E")){
							Exp = true;
							j--;
							continue;
						}
					
					if(temp.substring(j, j+1).equals("y"))	return "Infinity";
					if(temp.substring(j, j+1).equals("N"))	return "NaN";
					
					if(j == 0) {
						x = Double.parseDouble(temp.substring(j,i));
						start = 0;
						break;
					}
					if(isNotNum(temp.substring(j, j+1))){
						if(temp.substring(j, j+1).equals("-") || temp.substring(j, j+1).equals("+")) {
							x = Double.parseDouble(temp.substring(j,i));
							start = j;
							break;
						} else {
							x = Double.parseDouble(temp.substring(j+1,i));
							start = j+1;
							break;
						}
					}
				}
				for(int j=i+1;j<temp.length();j++) {
					if(j == i+1 && temp.charAt(i+1) == '-')	continue;
					if(temp.substring(j, j+1).equals("E"))
						if(temp.substring(j+1, j+2).equals("-")) {
							Exp = true;
							j++;
							continue;
						}
					
					if(temp.substring(j, j+1).equals("I"))	return "Infinity";
					if(temp.substring(j, j+1).equals("N"))	return "NaN";
					
					if(isNotNum(temp.substring(j, j+1))){
						y = Double.parseDouble(temp.substring(i+1,j));
						end = j;
						break;
					}
				}
				if(temp.charAt(i) == '*') {
					result = x*y;
				} else if(temp.charAt(i) == '/'){
					result = x/y;
				}

				if(x < 0 && y < 0)
					temp.replace(start, end, "+" + Double.toString(result));
				else if(x < 0 && y > 0)
					temp.replace(start, end, Double.toString(result));
				else if(x > 0 && y < 0)
					temp.replace(start, end, Double.toString(result));
				else
					temp.replace(start, end, "+" + Double.toString(result));
				
				i = start+1;
			}
		}
		
		for(int i=0;i<temp.length();i++) {
			if(temp.charAt(i) == '+' || temp.charAt(i) == '-') {
				double x = 0.0, y = 0.0, result = 0.0;
				int start=0,end=0;
				if(i > 0 && temp.charAt(i-1) == 'E')	continue;
				
				for(int j=i-1;j>=0;j--) {
					if(temp.substring(j, j+1).equals("-"))
						if(j > 0 && temp.substring(j-1, j).equals("E")){
							Exp = true;
							j--;
							continue;
						}
					
					if(temp.substring(j, j+1).equals("y"))	return "Infinity";
					if(temp.substring(j, j+1).equals("N"))	return "NaN";				
					
					if(j == 0) {
						if(inf)	x = 1;
						x = Double.parseDouble(temp.substring(j,i));
						start = 0;
						break;
					}
					if(isNotNum(temp.substring(j, j+1))){
						if(inf)
						x = Double.parseDouble(temp.substring(j+1,i));
						start = j+1;
						break;
					}
				}
				for(int j=i+1;j<temp.length();j++) {
					if(temp.substring(j, j+1).equals("E"))
						if(temp.substring(j+1, j+2).equals("-")) {
							Exp = true;
							j++;
							continue;
						}
					
					if(temp.substring(j, j+1).equals("I"))	return "Infinity";
					if(temp.substring(j, j+1).equals("N"))	return "NaN";
					
					if(isNotNum(temp.substring(j, j+1))){
						y = Double.parseDouble(temp.substring(i+1,j));
						end = j;
						break;
					}
				}
				if(temp.charAt(i) == '+') {
					result = x+y;
				} else if(temp.charAt(i) == '-'){
					result = x-y;
				}
				temp.replace(start, end, Double.toString(result));
				i = start+1;
			}
		}
		temp.setLength(temp.length() - 1);
		return temp.toString();
	}
	
	void run() {
		changeSpcVal();
		calSpcOper();
		power();
		while(true) {
			if(!spc)		calSpcOper();
			else if(!pwr)	power();
			if(spc && pwr)	break;
		}
		lastBreakit();
	}
	
	static boolean fonts(String x) {
		if(x.length() > 28 + 340)	return true;
		else				return false;
	}
	
	public static void main(String argv[]) {
		StringBuilder sb = new StringBuilder();
		StringBuilder tmpSb = new StringBuilder();
		Calculator t2 = new Calculator();
		
		String[] keys = {"<=> 2nd", "Rad", "¡î", "C", "()", "%", "/",
						"sin", "cos", "tan", "7", "8", "9", "*",
						"ln", "log", "1/x", "4", "5", "6", "-",
						"e^x", "x^2", "x^y", "1", "2", "3", "+",
						"|x|", "¥ð", "e", "<¤Ñ", "0", ".", "="
						};
		String[] val1 = {"s", "c", "t", "L", "l"};
		String[] secd = {"<=> 1st", "3¡îx",
						"asin", "acos", "atan",
						"sinh", "cosh", "tanh",
						"2^x", "x^3", "x!",
						"", "", ""
						};
		String[] val2 = {"b", "d", "f", "g", "h"};
		int[] changeList = {0, 2, 7, 8, 9, 14, 15, 16, 21, 22, 23, 28, 29, 30};
		int[] norm = {6, 13, 20, 27};
		int[] num = {10, 11, 12, 17, 18, 19, 24, 25, 26, 29, 30, 32};
		int[] shortL = {7, 8, 9, 14, 15};
		int[] shortK = {2, 3, 4, 5, 6};
		
		ArrayList<JButton> bl = new ArrayList<>();
		
		JFrame frame = new JFrame("Calculator");
		frame.setPreferredSize(new Dimension(600,400));
		frame.setLocation(500, 400);
		Container contentPane = frame.getContentPane();
		JPanel display = new JPanel();
		display.setLayout(new GridLayout(2,1));
		display.setBackground(Color.WHITE);
		JLabel equ = new JLabel("");
		equ.setHorizontalAlignment(JLabel.RIGHT);
		equ.setPreferredSize(new Dimension(600,50));
		equ.setFont (equ.getFont ().deriveFont (32.0f));
		display.add(equ);
		JLabel rs = new JLabel("");
		rs.setPreferredSize(new Dimension(600,50));
		rs.setHorizontalAlignment(JLabel.RIGHT);
		rs.setVerticalAlignment(JLabel.BOTTOM);
		rs.setFont (equ.getFont ().deriveFont (20.0f));
		rs.setForeground(Color.GRAY);
		display.add(rs);
		contentPane.add(display, BorderLayout.NORTH);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5,7));
		for(int i = 0; i < keys.length; i++) {
			bl.add(new JButton(keys[i]));
			panel.add(bl.get(bl.size() - 1));
		}
		contentPane.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		ArrayList<String> bk = new ArrayList<>();
		
		int z = 0;
		for(int i=0; i<keys.length;i++) {
			int x = i;
			if(z >= norm.length) break;
			if(norm[z] == i) {
				bl.get(i).addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						equ.setForeground(Color.BLACK);
						if(sb.length() != 0) {
							char temp = sb.charAt(sb.length()-1);
							if(!(temp == '+' || temp == '-' || temp == '*' || temp == '/' || temp == '(')) {
								equ.setText(equ.getText()+keys[x]);
								sb.append(keys[x]);
							} else if(temp == '(' && bl.get(x).getText().equals("-")) {
								equ.setText(equ.getText()+keys[x]);
								sb.append(keys[x]);
							}
						}
					}
				});
				z++;
			}
		}
		
		bl.get(0).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equ.setForeground(Color.BLACK);
				int y = 0;
				if(bl.get(0).getText().equals("<=> 2nd")) {
					for(int i = 0; i < changeList.length; i++) {
						bl.get(changeList[i]).setText(secd[y++]);
					}
				}
				else {
					for(int i = 0; i < changeList.length; i++) {
						bl.get(changeList[i]).setText(keys[changeList[i]]);
					}
				}
			}
		});
		
		bl.get(1).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equ.setForeground(Color.BLACK);
				if(bl.get(1).getText().equals("Rad")) {
					bl.get(1).setText("Deg");
					t2.rad = false;
				} else {
					bl.get(1).setText("Rad");
					t2.rad = true;
				}
				tmpSb.setLength(0);
				tmpSb.append(sb.toString());
				for(int i=bk.size()-1;i>-1;i--) {
					tmpSb.append(bk.get(i));
				}
				t2.set(tmpSb.toString());
				t2.run();
				rs.setText(t2.getResult());
			}
		});
		
		bl.get(2).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equ.setForeground(Color.BLACK);
				if(sb.length() != 0) {
					char temp = sb.charAt(sb.length()-1);
					if(!(temp == '+' || temp == '-' || temp == '*' || temp == '/' || temp == '(')) {
						equ.setText(equ.getText()+"*");
						sb.append("*");
					}
				}
				if(bl.get(2).getText().equals("¡î")) {
					equ.setText(equ.getText()+"¡î(");
					sb.append("r{(");		
				} else { 
					equ.setText(equ.getText()+"cbrt(");
					sb.append("C{(");
				}
				bk.add(")}");
				
				if(fonts(equ.toString()))	equ.setFont (equ.getFont ().deriveFont (16.0f));
				else						equ.setFont (equ.getFont ().deriveFont (32.0f));
			}
		});
		
		bl.get(3).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equ.setForeground(Color.BLACK);
				equ.setText("");
				sb.setLength(0);
				rs.setText("");
				bk.clear();
			}
		});

		bl.get(4).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equ.setForeground(Color.BLACK);
				if(sb.length() == 0) {
					equ.setText("(");
					sb.append("(");
					bk.add(")");
				}
				else {
					char temp = sb.charAt(sb.length()-1);
					if(temp == '+' || temp == '-' || temp == '*' || temp == '/' || temp == '(') {
						equ.setText(equ.getText()+"(");
						sb.append("(");
						bk.add(")");
					}
					else if(bk.size() == 0) {
						equ.setText(equ.getText()+"*(");
						sb.append("*(");
						bk.add(")");
					}
					else {
						equ.setText(equ.getText()+")");
						sb.append(bk.get(bk.size()-1));
						bk.remove(bk.size()-1);
						tmpSb.setLength(0);
						tmpSb.append(sb.toString());
						for(int i=bk.size()-1;i>-1;i--) {
							tmpSb.append(bk.get(i));
						}
						t2.set(tmpSb.toString());
						t2.run();
						rs.setText(t2.getResult());
					}
				}
				if(fonts(equ.toString()))	equ.setFont (equ.getFont ().deriveFont (16.0f));
				else						equ.setFont (equ.getFont ().deriveFont (32.0f));
			}
		});
		
		bl.get(5).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equ.setForeground(Color.BLACK);
				if(sb.length() != 0) {
					char temp = sb.charAt(sb.length()-1);
					if(!(temp == '+' || temp == '-' || temp == '*' || temp == '/' || temp == '(')) {
						equ.setText(equ.getText()+"%");
						sb.append("*0.01");
					}
				}
				tmpSb.setLength(0);
				tmpSb.append(sb.toString());
				for(int i=bk.size()-1;i>-1;i--) {
					tmpSb.append(bk.get(i));
				}
				t2.set(tmpSb.toString());
				t2.run();
				rs.setText(t2.getResult());
				if(fonts(equ.toString()))	equ.setFont (equ.getFont ().deriveFont (16.0f));
				else						equ.setFont (equ.getFont ().deriveFont (32.0f));
			}
		});
		
		for(int i = 0; i < shortL.length; i++) {
			int x = shortL[i];
			int w = shortK[i];
			int y = i;
			bl.get(x).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					equ.setForeground(Color.BLACK);
					if(sb.length() == 0) {
						if(bl.get(x).getText().equals(keys[x])) {
							equ.setText(keys[x]+"(");
							sb.append(val1[y]+"{(");
						} else {
							equ.setText(secd[w]+"(");
							sb.append(val2[y]+"{(");
						}
					}
					else {
						char temp = sb.charAt(sb.length()-1);
						if(temp == '+' || temp == '-' || temp == '*' || temp == '/' || temp == '(') {
							if(bl.get(x).getText().equals(keys[x])) {
								equ.setText(equ.getText()+keys[x]+"(");
								sb.append(val1[y]+"{(");
							} else {
								equ.setText(equ.getText()+secd[w]+"(");
								sb.append(val2[y]+"{(");
							}
						}
						else {
							if(bl.get(x).getText().equals(keys[x])) {
								equ.setText(equ.getText()+"*"+keys[x]+"(");
								sb.append("*"+val1[y]+"{(");
							} else {
								equ.setText(equ.getText()+"*"+secd[w]+"(");
								sb.append("*"+val2[y]+"{(");
							}
						}
					}
					bk.add(")}");
					if(fonts(equ.toString()))	equ.setFont (equ.getFont ().deriveFont (16.0f));
					else						equ.setFont (equ.getFont ().deriveFont (32.0f));
				}
			});
		}
		
		bl.get(16).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equ.setForeground(Color.BLACK);
				if(sb.length() == 0) {
					if(bl.get(16).getText().equals("1/x")) {
						equ.setText("1/");
						sb.append("1/");
					} else {
						equ.setText(secd[7]+"(");
						sb.append("i{(");
						bk.add(")}");
					}
				} else {
					char temp = sb.charAt(sb.length()-1);
					if(temp == '+' || temp == '-' || temp == '*' || temp == '/' || temp == '(') {
						if(bl.get(16).getText().equals("1/x")) {
							equ.setText(equ.getText()+"1/");
							sb.append("1/");
						} else {
							equ.setText(equ.getText()+secd[7]+"(");
							sb.append("i{(");
							bk.add(")}");
						}
					}
					else {
						if(bl.get(16).getText().equals("1/x")) {
							equ.setText(equ.getText()+"*1/");
							sb.append("*1/");
						} else {
							equ.setText(equ.getText()+"*"+secd[7]+"(");
							sb.append("*i{(");
							bk.add(")}");
						}
					}
				}
				if(fonts(equ.toString()))	equ.setFont (equ.getFont ().deriveFont (16.0f));
				else						equ.setFont (equ.getFont ().deriveFont (32.0f));
			}
		});
		
		bl.get(21).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equ.setForeground(Color.BLACK);
				if(sb.length() == 0) {
					if(bl.get(21).getText().equals("e^x")) {
						equ.setText("e^(");
						sb.append("[(e)]^[(");
					} else {
						equ.setText("2^(");
						sb.append("[(2)]^[(");
					}
				} else {
					char temp = sb.charAt(sb.length()-1);
					if(temp == '+' || temp == '-' || temp == '*' || temp == '/' || temp == '(') {
						if(bl.get(21).getText().equals("e^x")) {
							equ.setText(equ.getText()+"e^(");
							sb.append("[(e)]^[(");
						} else {
							equ.setText(equ.getText()+"2^(");
							sb.append("[(2)]^[(");
						}
					}
					else {
						if(bl.get(21).getText().equals("e^x")) {
							equ.setText(equ.getText()+"*e^(");
							sb.append("*[(e)]^[(");
						} else {
							equ.setText(equ.getText()+"*2^(");
							sb.append("*[(2)]^[(");
						}
					}
				}
				bk.add(")]");
				if(fonts(equ.toString()))	equ.setFont (equ.getFont ().deriveFont (16.0f));
				else						equ.setFont (equ.getFont ().deriveFont (32.0f));
			}
		});
		
		bl.get(22).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equ.setForeground(Color.BLACK);
				if(sb.length() != 0) {
					int v = 0;
					String w = null;
					if(bl.get(22).getText().equals("x^2"))	w = "2";
					else	w = "3";
					char temp = sb.charAt(sb.length()-1);
					if(!(temp == '+' || temp == '-' || temp == '*' || temp == '/' || temp == '(')) {
						if(temp == ')' || temp == '}' || temp == ']') {
							int open1 = 0, open2 = 0, open3 = 0;
							int close1 = 0, close2 = 0, close3 = 0;
							if(temp == ')')			close1++;
							else if(temp == '}')	close2++;
							else if(temp == ']')	close3++;
							for(int i = sb.length() - 2; i > -1; i--) {
								if(sb.charAt(i) == '(')			open1++;
								else if(sb.charAt(i) == ')')	close1++;
								else if(sb.charAt(i) == '{')	open2++;
								else if(sb.charAt(i) == '}')	close2++;
								else if(sb.charAt(i) == '[')	open3++;
								else if(sb.charAt(i) == ']')	close3++;
								if(open1 == close1 && open2 == close2 && open3 == close3) {
									v = i;
									break;
								}
							}
						} else {
							for(int i = sb.length() - 1; i > -1; i--) {
								if(t2.isNotNum(sb.substring(i, i+1))) {
									v = i+1;
									break;
								}
							}
						}
						equ.setText(equ.getText()+"^("+w+")");
						if(sb.charAt(v) == '{') sb.insert(v-1, "[(");
						else if(sb.charAt(v) == '[') {
							int open1 = 0, open2 = 0, open3 = 0;
							int close1 = 0, close2 = 0, close3 = 1;
							for(int i = v - 3; i > -1; i--) {
								if(sb.charAt(i) == '(')			open1++;
								else if(sb.charAt(i) == ')')	close1++;
								else if(sb.charAt(i) == '{')	open2++;
								else if(sb.charAt(i) == '}')	close2++;
								else if(sb.charAt(i) == '[')	open3++;
								else if(sb.charAt(i) == ']')	close3++;
								if(open1 == close1 && open2 == close2 && open3 == close3) {
									v = i;
									break;
								}
							}
							sb.insert(v, "[(");
						} else sb.insert(v, "[(");
						sb.append(")]^[("+w+")]");
						t2.set(sb.toString());
						t2.run();
						rs.setText(t2.getResult());
					}
				}
				if(fonts(equ.toString()))	equ.setFont (equ.getFont ().deriveFont (16.0f));
				else						equ.setFont (equ.getFont ().deriveFont (32.0f));
			}
		});
		
		bl.get(23).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equ.setForeground(Color.BLACK);
				if(sb.length() != 0) {
					int v = 0;
					char temp = sb.charAt(sb.length()-1);
					if(!(temp == '+' || temp == '-' || temp == '*' || temp == '/' || temp == '(')) {
						if(temp == ')' || temp == '}' || temp == ']') {
							int open1 = 0, open2 = 0, open3 = 0;
							int close1 = 0, close2 = 0, close3 = 0;
							if(temp == ')')			close1++;
							else if(temp == '}')	close2++;
							else if(temp == ']')	close3++;
							for(int i = sb.length() - 2; i > -1; i--) {
								if(sb.charAt(i) == '(')			open1++;
								else if(sb.charAt(i) == ')')	close1++;
								else if(sb.charAt(i) == '{')	open2++;
								else if(sb.charAt(i) == '}')	close2++;
								else if(sb.charAt(i) == '[')	open3++;
								else if(sb.charAt(i) == ']')	close3++;
								if(open1 == close1 && open2 == close2 && open3 == close3) {
									v = i;
									break;
								}
							}
						} else {
							for(int i = sb.length() - 1; i > -1; i--) {
								if(t2.isNotNum(sb.substring(i, i+1))) {
									v = i+1;
									break;
								}
							}
						}
						if(bl.get(23).getText().equals("x^y")) {
							if(sb.charAt(v) == '{') sb.insert(v-1, "[(");
							else if(sb.charAt(v) == '[') {
								int open1 = 0, open2 = 0, open3 = 0;
								int close1 = 0, close2 = 0, close3 = 1;
								for(int i = v - 3; i > -1; i--) {
									if(sb.charAt(i) == '(')			open1++;
									else if(sb.charAt(i) == ')')	close1++;
									else if(sb.charAt(i) == '{')	open2++;
									else if(sb.charAt(i) == '}')	close2++;
									else if(sb.charAt(i) == '[')	open3++;
									else if(sb.charAt(i) == ']')	close3++;
									if(open1 == close1 && open2 == close2 && open3 == close3) {
										v = i;
										break;
									}
								}
								sb.insert(v, "[(");
							} else sb.insert(v, "[(");
							equ.setText(equ.getText()+"^(");
							sb.append(")]^[(");
							bk.add(")]");
						} else {
							if(sb.charAt(v) == '{') sb.insert(v-1, "j{(");
							else if(sb.charAt(v) == '[') {
								int open1 = 0, open2 = 0, open3 = 0;
								int close1 = 0, close2 = 0, close3 = 1;
								for(int i = v - 3; i > -1; i--) {
									if(sb.charAt(i) == '(')			open1++;
									else if(sb.charAt(i) == ')')	close1++;
									else if(sb.charAt(i) == '{')	open2++;
									else if(sb.charAt(i) == '}')	close2++;
									else if(sb.charAt(i) == '[')	open3++;
									else if(sb.charAt(i) == ']')	close3++;
									if(open1 == close1 && open2 == close2 && open3 == close3) {
										v = i;
										break;
									}
								}
								sb.insert(v, "j{(");
							} else sb.insert(v, "j{(");
							equ.setText(equ.getText()+"!");
							sb.append(")}");
							t2.set(sb.toString());
							t2.run();
							rs.setText(t2.getResult());
						}
					}
				}
				if(fonts(equ.toString()))	equ.setFont (equ.getFont ().deriveFont (16.0f));
				else						equ.setFont (equ.getFont ().deriveFont (32.0f));
			}
		});
		
		bl.get(28).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equ.setForeground(Color.BLACK);
				if(bl.get(28).getText().equals("|x|")) {
					if(sb.length() == 0) {
						equ.setText("abs(");
						sb.append("a{(");
					} else {
						char temp = sb.charAt(sb.length()-1);
						if(temp == '+' || temp == '-' || temp == '*' || temp == '/' || temp == '(') {
							equ.setText(equ.getText()+"abs(");
							sb.append("a{(");
						}
						else {
							equ.setText(equ.getText()+"*abs(");
							sb.append("*a{(");
						}
					}
					bk.add(")}");
				}
				if(fonts(equ.toString()))	equ.setFont (equ.getFont ().deriveFont (16.0f));
				else						equ.setFont (equ.getFont ().deriveFont (32.0f));
			}
		});
		
		for(int i=0;i<num.length;i++) {
			int x = num[i];
			bl.get(x).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					equ.setForeground(Color.BLACK);
					if(bl.get(x).getText().equals(keys[x])) {
						if(sb.length() == 0) {
							equ.setText(keys[x]);
							if(bl.get(x).getText().equals("¥ð"))	sb.append("p");
							else sb.append(keys[x]);
						} else {
							char temp = sb.charAt(sb.length()-1);
							if(temp == ')' || temp == '}' || temp == ']' || temp == 'p' || temp == 'e') {
								equ.setText(equ.getText()+"*"+keys[x]);
								if(bl.get(x).getText().equals("¥ð"))	sb.append("*p");
								else sb.append("*"+keys[x]);
							} else if(temp == '.') {
								if(bl.get(x).getText().equals("¥ð") || bl.get(x).getText().equals("e")) {
									equ.setText(equ.getText()+"*"+keys[x]);
									if(bl.get(x).getText().equals("¥ð"))	sb.append("*p");
									else sb.append("*"+keys[x]);
								} else {
									equ.setText(equ.getText()+keys[x]);
									sb.append(keys[x]);
								}
							} else {
								if(!(temp == '+' || temp == '-' || temp == '*' || temp == '/' || temp == '(')) {
									if(bl.get(x).getText().equals("¥ð") || bl.get(x).getText().equals("e")) {
										equ.setText(equ.getText()+"*"+keys[x]);
										if(bl.get(x).getText().equals("¥ð"))	sb.append("*p");
										else sb.append("*"+keys[x]);
									} else {
										equ.setText(equ.getText()+keys[x]);
										sb.append(keys[x]);
									}
								} else {
									equ.setText(equ.getText()+keys[x]);
									if(bl.get(x).getText().equals("¥ð"))	sb.append("p");
									else sb.append(keys[x]);
								}
							}
						}
					}
					tmpSb.setLength(0);
					tmpSb.append(sb.toString());
					for(int i=bk.size()-1;i>-1;i--) {
						tmpSb.append(bk.get(i));
					}
					t2.set(tmpSb.toString());
					t2.run();
					rs.setText(t2.getResult());
					if(fonts(equ.toString()))	equ.setFont (equ.getFont ().deriveFont (16.0f));
					else						equ.setFont (equ.getFont ().deriveFont (32.0f));
				}
			});
		}
		
		bl.get(31).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equ.setForeground(Color.BLACK);
				if(sb.length() != 0) {
					StringBuilder tmpEq = new StringBuilder();
					tmpEq.append(equ.getText());
					char temp = sb.charAt(sb.length()-1);
					if(temp == '}' || temp == ']' || temp == ')') {
						char bu = sb.charAt(sb.length()-2);
						sb.setLength(sb.length()-2);
						tmpEq.setLength(tmpEq.length()-1);
						if(temp == '}') {
							bk.add(")}");
						} else if(temp == ']') {
							bk.add(")]");
						} else if(temp == ')') {
							sb.append(bu);
							bk.add(")");
						}
					} else if(temp == '(') {
						if(sb.length() == 1) {
							sb.setLength(0);
							tmpEq.setLength(0);
							bk.remove(0);
						} else {
							char preTemp = sb.charAt(sb.length()-2);
							if(preTemp == '{') {
								sb.setLength(sb.length()-3);
								int start = 0;
								for(int i = tmpEq.length()-2; i > -1; i--) {
									if(!((int)tmpEq.charAt(i) > 96 && (int)tmpEq.charAt(i) < 123)) {
										start = i + 1;
										break;
									}
								}
								tmpEq.delete(start, tmpEq.length());
							} else if(preTemp == '[') {
								sb.setLength(sb.length()-5);
								for(int i = sb.length()-1; i > -1; i--) {
									if(sb.charAt(i) == '[') {
										sb.delete(i, i+2);
									}
								}
								tmpEq.setLength(tmpEq.length()-2);
							} else {
								sb.setLength(sb.length()-1);
								tmpEq.setLength(tmpEq.length()-1);
							}
							bk.remove(bk.size()-1);
						}
					} else if(tmpEq.charAt(tmpEq.length()-1) == '%') {
						tmpEq.setLength(tmpEq.length()-1);
						sb.setLength(sb.length()-5);
					} else {
						tmpEq.setLength(tmpEq.length()-1);
						sb.setLength(sb.length()-1);						
					}
					equ.setText(tmpEq.toString());
				}
				if(sb.length() == 0) {
					rs.setText("");
				} else {
					char temp = sb.charAt(sb.length()-1);
					if(!(temp == '(' || temp == '+' || temp == '-' || temp == '*' || temp == '/')) {
						tmpSb.setLength(0);
						tmpSb.append(sb.toString());
						for(int i=bk.size()-1;i>-1;i--) {
							tmpSb.append(bk.get(i));
						}
						t2.set(tmpSb.toString());
						t2.run();
						rs.setText(t2.getResult());
					}
				}
				if(fonts(equ.toString()))	equ.setFont (equ.getFont ().deriveFont (16.0f));
				else						equ.setFont (equ.getFont ().deriveFont (32.0f));
			}
		});
		
		bl.get(33).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equ.setForeground(Color.BLACK);
				if(sb.length() == 0) {
					equ.setText("0.");
					sb.append("0.");
				} else {
					char temp = sb.charAt(sb.length()-1);
					if(!(temp == '+' || temp == '-' || temp == '*' || temp == '/' || temp == '(')) {
						if(temp == ')' || temp == '}' || temp == ']' || temp == '%' || temp == 'p' || temp == 'e') {
							equ.setText(equ.getText()+"*0.");
							sb.append("*0.");
						} else {
							boolean dot = true;
							for(int i = sb.length()-1;i>-1;i--) {
								if(t2.isNotNum(sb.substring(i,i+1))) {
									break;
								}
								if(sb.charAt(i) == '.')
									dot = false;
							}
							if(dot) {
								equ.setText(equ.getText()+".");
								sb.append(".");
							}
						}
					} else {
						equ.setText(equ.getText()+"0.");
						sb.append("0.");
					}
				}
				tmpSb.setLength(0);
				tmpSb.append(sb.toString());
				for(int i=bk.size()-1;i>-1;i--) {
					tmpSb.append(bk.get(i));
				}
				t2.set(tmpSb.toString());
				t2.run();
				rs.setText(t2.getResult());
				if(fonts(equ.toString()))	equ.setFont (equ.getFont ().deriveFont (16.0f));
				else						equ.setFont (equ.getFont ().deriveFont (32.0f));
			}
		});
		
		bl.get(34).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				while(bk.size() != 0) {
					equ.setText(equ.getText()+")");
					sb.append(bk.get(bk.size()-1));
					bk.remove(bk.size()-1);
				}
				t2.set(sb.toString());
				t2.run();
				equ.setForeground(Color.BLUE);
				String res = t2.getResult();
				equ.setText(res);
				sb.setLength(0);
				sb.append(t2.getResult());
				rs.setText("");
				if(fonts(equ.toString()))	equ.setFont (equ.getFont ().deriveFont (16.0f));
				else						equ.setFont (equ.getFont ().deriveFont (32.0f));
			}
		});
	}
}