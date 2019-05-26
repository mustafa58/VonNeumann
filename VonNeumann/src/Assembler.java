import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import errorHandling.invalidAddressException;
import errorHandling.regOverflowException;
import errorHandling.unknownCommandException;


public class Assembler {
	
	public boolean validate(List<Command> comlist) {
		for(Command com: comlist) {
			switch(com) {
			case LDA:
				if(com.getArg()>=100 || com.getArg()<0)
					System.out.println("hata");//return false;
				break;
			case STO:
				if(com.getArg()>=100 || com.getArg()<0)
					return false;
				break;
			case ADD:
				if(com.getArg()>=100 || com.getArg()<0)
					return false;
				break;
			case SUB:
				if(com.getArg()>=100 || com.getArg()<0)
					return false;
				break;
			case IN:
				if(com.getArg() != 0)
					return false;
				break;
			case OUT:
				if(com.getArg() != 0)
					return false;
				break;
			case BRZ:
				if(com.getArg()>=100 || com.getArg()<0)
					return false;
				break;
			case BR:
				if(com.getArg()>=100 || com.getArg()<0)
					return false;
				break;
			case BRP:
				if(com.getArg()>=100 || com.getArg()<0)
					return false;
				break;
			case NOP:
				if(com.getArg()>=100 || com.getArg()<0)
					return false;
				break;
			case HLT:
				if(com.getArg()>=100 || com.getArg()<0)
					return false;
				break;
			default:
				return false;
			}	
		}
		
		return true;
	}
	
	
	/*public List<Command> parse(String text, RAM ram) throws invalidAddressException {
		text = text.split("hlt")[0] + "hlt\n";
		int size = text.length();
		List<Command> liste = new LinkedList<Command>();
		int instNum = 0;
		text = text.toUpperCase(new Locale("tr-TR"));
		String com = "";
		Command c = null;
		char tmp;
		int arg;
		for(int i=0;i<size;i++) {
			tmp = text.charAt(i);
			if(tmp == '/' && text.charAt(++i) == '/') {
				tmp = text.charAt(i);
				while(tmp != '\n') {
					//System.out.print("pass");
					i++;
					tmp = text.charAt(i);
				}
			}
			else if(tmp == '\n' || tmp == ' ' || tmp == '\t')
			{
				switch(com) {
					case "LDA":
						c = Command.LDA;
						com = "";
						tmp = text.charAt(i);
						while(tmp == ' ' || tmp == '\t') {
							i++;
							tmp = text.charAt(i);
						}
						arg = 0;
						while(tmp >= '0' && tmp <= '9') {
							arg = arg*10 + (tmp-'0');
							i++;
							tmp = text.charAt(i);
						}
						c.setArg(arg);
						break;
					case "STO":
						c = Command.STO;
						com = "";
						tmp = text.charAt(i);
						while(tmp == ' ' || tmp == '\t') {
							i++;
							tmp = text.charAt(i);
						}
						arg = 0;
						while(tmp >= '0' && tmp <= '9') {
							arg = arg*10 + (tmp-'0');
							i++;
							tmp = text.charAt(i);
						}
						c.setArg(arg);
						break;
					case "ADD":
						c = Command.ADD;
						com = "";
						tmp = text.charAt(i);
						while(tmp == ' ' || tmp == '\t') {
							i++;
							tmp = text.charAt(i);
						}
						arg = 0;
						while(tmp >= '0' && tmp <= '9') {
							arg = arg*10 + (tmp-'0');
							i++;
							tmp = text.charAt(i);
						}
						c.setArg(arg);
						break;
					case "SUB":
						c = Command.SUB;
						com = "";
						tmp = text.charAt(i);
						while(tmp == ' ' || tmp == '\t') {
							i++;
							tmp = text.charAt(i);
						}
						arg = 0;
						while(tmp >= '0' && tmp <= '9') {
							arg = arg*10 + (tmp-'0');
							i++;
							tmp = text.charAt(i);
						}
						c.setArg(arg);
						break;
					case "IN":
						c = Command.IN;
						com = "";
						break;
					case "OUT":
						c = Command.OUT;
						com = "";
						break;
					case "BRZ":
						c = Command.BRZ;
						com = "";
						tmp = text.charAt(i);
						while(tmp == ' ' || tmp == '\t') {
							i++;
							tmp = text.charAt(i);
						}
						arg = 0;
						while(tmp >= '0' && tmp <= '9') {
							arg = arg*10 + (tmp-'0');
							i++;
							tmp = text.charAt(i);
						}
						c.setArg(arg);
						break;
					case "BRP":
						c = Command.BRP;
						com = "";
						tmp = text.charAt(i);
						while(tmp == ' ' || tmp == '\t') {
							i++;
							tmp = text.charAt(i);
						}
						arg = 0;
						while(tmp >= '0' && tmp <= '9') {
							arg = arg*10 + (tmp-'0');
							i++;
							tmp = text.charAt(i);
						}
						c.setArg(arg);
						break;
					case "BR":
						c = Command.BR;
						com = "";
						tmp = text.charAt(i);
						while(tmp == ' ' || tmp == '\t') {
							i++;
							tmp = text.charAt(i);
						}
						arg = 0;
						while(tmp >= '0' && tmp <= '9') {
							arg = arg*10 + (tmp-'0');
							i++;
							tmp = text.charAt(i);
						}
						c.setArg(arg);
						break;
					case "NOP":
						c = Command.NOP;
						com = "";
						break;
					case "HLT":
						c = Command.HLT;
						com = "";
						break;
					default:break;
				}
				while(tmp == ' ' || tmp == '\t') {
					i++;
					tmp = text.charAt(i);
				}
				System.out.println(c.toString() +'\t'+ c.getArg());
				liste.add(c);
				//System.out.println(c.getObjCode());
				ram.read(c.getObjCode(), instNum++);
			}
			else {
				com = com + tmp;
			}
		}return liste;
	}*/
	
	public int[] parse(String text) throws invalidAddressException, unknownCommandException {
		char[] array;
		int[] prog;
		int i,n,arg;
		int k = 0;
		String komut;
		text = text.split("[*]")[0];
		prog = new int[text.length()];
		int lineNumber = 1;
		for(String line : text.split("\n")) {
			line = line.split("//")[0];
			n = line.length();
			array = line.toUpperCase(new Locale("tr-TR")).toCharArray();
			komut = "";
			arg = 0;
			i = 0;
			//System.out.println(line);
			while( i<n && (array[i] == ' ' || array[i] == '\t') )
				i++;
			while( i<n && array[i]>='A' && array[i]<='Z') {
				komut = komut + array[i++];
			}
			/*while( i<n && (array[i] == ' ' || array[i] == '\t') )
				i++;
			while( i<n && array[i]>='0' && array[i]<='9') {
				arg = arg*10 + (array[i++]-'0');
			}*/
			switch(komut) {
			case "":
				break;
			case "LDA":
				while( i<n && (array[i] == ' ' || array[i] == '\t') )
					i++;
				while( i<n && array[i]>='0' && array[i]<='9') {
					arg = arg*10 + (array[i++]-'0');
				}
				if(arg>=0 && arg<100) {
					prog[k++] = 500 + arg;
				}
				else {
					JOptionPane.showMessageDialog(null, lineNumber + " numaralý satýrdaki adrese eriþilemez.", "Hata", JOptionPane.ERROR_MESSAGE);
					throw new invalidAddressException(lineNumber + " numaralý satýrdaki adrese eriþilemez.");
				}
				break;
			case "STO":
				while( i<n && (array[i] == ' ' || array[i] == '\t') )
					i++;
				while( i<n && array[i]>='0' && array[i]<='9') {
					arg = arg*10 + (array[i++]-'0');
				}
				if(arg>=0 && arg<100) {
					prog[k++] = 300 + arg;
				}
				else {
					JOptionPane.showMessageDialog(null, lineNumber + " numaralý satýrdaki adrese eriþilemez.", "Hata", JOptionPane.ERROR_MESSAGE);
					throw new invalidAddressException(lineNumber + " numaralý satýrdaki adrese eriþilemez.");
				}
				break;
			case "ADD":
				while( i<n && (array[i] == ' ' || array[i] == '\t') )
					i++;
				while( i<n && array[i]>='0' && array[i]<='9') {
					arg = arg*10 + (array[i++]-'0');
				}
				if(arg>=0 && arg<100) {
					prog[k++] = 100 + arg;
				}
				else {
					JOptionPane.showMessageDialog(null, lineNumber + " numaralý satýrdaki adrese eriþilemez.", "Hata", JOptionPane.ERROR_MESSAGE);
					throw new invalidAddressException(lineNumber + " numaralý satýrdaki adrese eriþilemez.");
				}
				break;
			case "SUB":
				while( i<n && (array[i] == ' ' || array[i] == '\t') )
					i++;
				while( i<n && array[i]>='0' && array[i]<='9') {
					arg = arg*10 + (array[i++]-'0');
				}
				if(arg>=0 && arg<100) {
					prog[k++] = 200 + arg;
				}
				else {
					JOptionPane.showMessageDialog(null, lineNumber + " numaralý satýrdaki adrese eriþilemez.", "Hata", JOptionPane.ERROR_MESSAGE);
					throw new invalidAddressException(lineNumber + " numaralý satýrdaki adrese eriþilemez.");
				}
				break;
			case "IN":
				prog[k++] = 901;
				break;
			case "OUT":
				prog[k++] = 902;
				break;
			case "BRZ":
				while( i<n && (array[i] == ' ' || array[i] == '\t') )
					i++;
				while( i<n && array[i]>='0' && array[i]<='9') {
					arg = arg*10 + (array[i++]-'0');
				}
				if(arg>=0 && arg<100) {
					prog[k++] = 700 + arg;
				}
				else {
					JOptionPane.showMessageDialog(null, lineNumber + " numaralý satýrdaki adrese eriþilemez.", "Hata", JOptionPane.ERROR_MESSAGE);
					throw new invalidAddressException(lineNumber + " numaralý satýrdaki adrese eriþilemez.");
				}
				break;
			case "BRP":
				while( i<n && (array[i] == ' ' || array[i] == '\t') )
					i++;
				while( i<n && array[i]>='0' && array[i]<='9') {
					arg = arg*10 + (array[i++]-'0');
				}
				if(arg>=0 && arg<100) {
					prog[k++] = 800 + arg;
				}
				else {
					JOptionPane.showMessageDialog(null, lineNumber + " numaralý satýrdaki adrese eriþilemez.", "Hata", JOptionPane.ERROR_MESSAGE);
					throw new invalidAddressException(lineNumber + " numaralý satýrdaki adrese eriþilemez.");
				}
				break;
			case "BR":
				while( i<n && (array[i] == ' ' || array[i] == '\t') )
					i++;
				while( i<n && array[i]>='0' && array[i]<='9') {
					arg = arg*10 + (array[i++]-'0');
				}
				if(arg>=0 && arg<100) {
					prog[k++] = 600 + arg;
				}
				else {
					JOptionPane.showMessageDialog(null, lineNumber + " numaralý satýrdaki adrese eriþilemez.", "Hata", JOptionPane.ERROR_MESSAGE);
					throw new invalidAddressException(lineNumber + " numaralý satýrdaki adrese eriþilemez.");
				}
				break;
			case "NOP":
				while( i<n && (array[i] == ' ' || array[i] == '\t') )
					i++;
				while( i<n && array[i]>='0' && array[i]<='9') {
					arg = arg*10 + (array[i++]-'0');
				}
				prog[k++] = 400;
				break;
			case "HLT":
				while( i<n && (array[i] == ' ' || array[i] == '\t') )
					i++;
				while( i<n && array[i]>='0' && array[i]<='9') {
					arg = arg*10 + (array[i++]-'0');
				}
				prog[k++] = 0;
				break;
			default:
				JOptionPane.showMessageDialog(null, lineNumber + " numaralý satýrdaki komut tanýmlý deðil.", "Hata", JOptionPane.ERROR_MESSAGE);
				throw new unknownCommandException();
			}
			lineNumber++;
		}
		return prog;
	}
	
	public void parseDat(String text, RAM ram) throws invalidAddressException, regOverflowException {
		text = text.replaceAll("//.*", "");
		String str;
		try {
			str = text.split("[*]")[1].split("\n")[0];
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Uyarý! (geçerli bir hafýza bloðu tanýmýna rastlanmadý)");
			throw new invalidAddressException();
		}
		Matcher m = Pattern.compile("[0-9]{1,3}").matcher(str);
		m.find();
		int offset = Integer.valueOf(m.group());
		for(String line: text.split("\n")) {
			if(line.contains("dat")) {
				m = Pattern.compile("[0-9]{1,3}").matcher(line);
				m.find();
				ram.read(Integer.valueOf(m.group()), offset++);
			}
		}
		/*for(String line : text.split("\n")) {
			char[] array = line.toUpperCase().toCharArray();
			int n = line.length();
			int i = 0;
			int j = 1;
			int val = 0;
			while( i<n && (array[i]==' ' || array[i]=='\t' || array[i]=='\n') ) {
				i++;
			}
			if (array[i++]=='D' && array[i++]=='A' && array[i++]=='T') {
				while( i<n && (array[i]==' ' || array[i]=='\t') ) {
					i++;
				}
				if( array[i]=='-' ) {
					j = -1;
				}
				while( array[i]>'0' && array[i]<'9' ) {
					val = val*10 + (array[i]-'0');
					i++;
				}
				val = val * j;
				if (val>=0 && val<1000) {
					ram.read(val, offset++);
				}
				else {
					JOptionPane.showMessageDialog(null, "Hafýza verilen deðeri saklayamaz.", "Hata", JOptionPane.ERROR_MESSAGE);
					throw new regOverflowException();
				}
			}
			else
				JOptionPane.showMessageDialog(null, "Tanýnmayan ifade.", "Hata", JOptionPane.ERROR_MESSAGE);
		}*/
		
	}
}
