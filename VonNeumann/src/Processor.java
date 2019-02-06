import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import java.lang.System;
import errorHandling.*;

public class Processor {
	public class Register {
		//private String name;
		private int value;
		private JTextPane textAcc;
		
		public void setPanel(JTextPane panel) {
			textAcc = panel;
		}
		
		public void update(int value) throws regOverflowException {
			if(value>=1000 || value<=-1000) {
				throw new regOverflowException();
			}
			this.value = value;
			/*if(textAcc != null) {
				textAcc.setText("" + value);
			}*/
		}
		public int getValue() {
			return value;
		}
		
		public Register(String name, int value) {
			//this.name = name;
			try {
				update(value);
			}
			catch(regOverflowException e) {
				e.printStackTrace();
			}
		}
		
		public Register(String name) {
			//this.name = name;
			value = new Random().nextInt();
			value = value % 1000;
		}
		
		public void increment() {
			try {
				update(value+1);
			} catch (regOverflowException e) {
				e.printStackTrace();
			}
		}
	}
	
	private RAM ram;
	private int wait = 1000;
	private Register accumulator = new Register("AX", 0);
	private Register programCounter = new Register("PC", 0);
	
	public void setWait(int wait) {
		this.wait = wait;
	}

	public Processor(RAM ram) {
		this.ram = ram;
	}
	
	public void write(int value, int address) {
		try {
			ram.read(value, address);
		} catch (invalidAddressException e) {
			e.printStackTrace();
		}
	}
	public void write(Register reg, int address) throws invalidAddressException {
		ram.read(reg.getValue(), address);
	}
	
	public void setTextAcc(JTextPane panel) {
		accumulator.setPanel(panel);
	}
	
	public void setTextProgCount(JTextPane panel) {
		programCounter.setPanel(panel);
	}
	
	public int read(int address) throws invalidAddressException {
		return ram.write(address);
	}
	public void read(Register reg, int address) {
		try {
			reg.update(ram.write(address));
		}
		catch(regOverflowException er) {
			er.printStackTrace();
		}
		catch(invalidAddressException e) {
			e.printStackTrace();
		}
	}
	
	public void execute(Command com, int arg) throws invalidAddressException, regOverflowException {
		int tmp;
		switch(com) {
			case LDA:
				read(accumulator, arg);
				programCounter.increment();
				break;
			case STO:
				write(accumulator, arg);
				programCounter.increment();
				break;
			case ADD:
				tmp = accumulator.getValue() + read(arg);
				write(accumulator, tmp);
				break;
			case SUB:
				tmp = accumulator.getValue() - read(arg);
				write(accumulator, tmp);
				programCounter.increment();
				break;
			case BRZ:
				if(accumulator.getValue() == 0) {
					programCounter.update(programCounter.getValue() + arg);
				}
				break;
			case BRP:
				if(accumulator.getValue() >= 0) {
					programCounter.update(programCounter.getValue() + arg);
				}
				break;
			case BR:
				programCounter.update(programCounter.getValue() + arg);
				break;
			default:break;
		}
		
	}
	
	
	public void execute() throws invalidAddressException, regOverflowException, InterruptedException {
		int com = ram.write(programCounter.getValue());
		int tmp;
		
		if(com>=500 && com<600) {
			read(accumulator, com-500);
			programCounter.increment();
			//execute();
		}
		else if(com>=300 && com<400) {
			write(accumulator, com-300);
			programCounter.increment();
			//execute();
		}
		else if(com>=100 && com<200) {
			accumulator.update(accumulator.getValue() + read(com-100));
			programCounter.increment();
			//execute();
		}
		else if(com>=200 && com<300) {
			accumulator.update(accumulator.getValue() - read(com-200));
			programCounter.increment();
			//execute();
		}
		else if(com>=700 && com<800) {
			if(accumulator.getValue() == 0) {
				programCounter.update(com-700);
			}
			else programCounter.increment();
			//execute();
		}
		else if(com>=800 && com<900) {
			if(accumulator.getValue() >= 0) {
				programCounter.update(com-800);
			}
			else programCounter.increment();
			//execute();
		}
		else if(com>=600 && com<700) {
			programCounter.update(com-600);
			//execute();
		}
		else if(com == 901) {
			accumulator.update(Integer.valueOf(JOptionPane.showInputDialog("Giriþ yapýnýz: ")));
			programCounter.increment();
			//execute();
		}
		else if(com == 902) {
			JOptionPane.showMessageDialog(null, "" + accumulator.getValue());
			programCounter.increment();
			//execute();
		}
		else if(com>=400 && com<500) {
			programCounter.increment();
			//execute();
		}
		else if(com>=0 && com<100) {
			//do nothing
		}
		else {
			System.out.println("Error!");
		}
	}
	
	
	public Register getAccumulator() {
		return accumulator;
	}

	public Register getProgramCounter() {
		return programCounter;
	}

	public void exucute(Command com) throws regOverflowException {
		switch(com) {
			case IN:
				System.out.println("Giriþ yapýnýz: ");
				Scanner inp = new Scanner(System.in);
				accumulator.update(inp.nextInt());
				inp.close();
				programCounter.increment();
				break;
			case OUT:
				System.out.println(accumulator.getValue());
				programCounter.increment();
				break;
			case NOP:
				programCounter.increment();
				break;
			case HLT:
				System.exit(0);
			default:break;
		}
	}
	
	public void loadProgram(List<Command> prog) throws invalidAddressException {
		for(int i=0; i<prog.size(); i++) {
			ram.read(prog.get(i).getObjCode(), i);
		}
		
	}
}
