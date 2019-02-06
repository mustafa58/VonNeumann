import errorHandling.*;
import java.util.Random;

import javax.swing.JTable;
public class RAM {
	private int size;
	private int[] ram;
	private JTable table;
	
	public RAM(int[] arr, int size) throws invalidRAMsizeException {
		this.size = size;
		if(arr.length == size)
			ram = arr;
		else
			throw new invalidRAMsizeException();
	}
	
	public RAM(Random rnd, int size) {
		this.size = size;
		ram = new int[size];
		for(int i=0;i<size;i++) {
			int tmp = rnd.nextInt();
			if (tmp < 0)
				tmp = -tmp;
			ram[i] = tmp % 1000;
			//table.setValueAt(ram[i], i/10, i%10);
		}
	}
	
	
	public void read(int value, int address) throws invalidAddressException {
		if(address>=0 && address<=size) {
			ram[address] = value;
			if(table != null) {
				String str;
				if(value <= -10)
					str = "" + value;
				else if(value<0 && value>-10)
					str = "-0" + -1*value;
				else if(value < 10)
					str = "00" + value;
				else if(value < 100)
					str = "0" + value;
				else
					str = "" + value;
				table.setValueAt(str, address/10, address%10);
			}	
		}
		else
			throw new invalidAddressException();
	}
	
	public int write(int address) throws invalidAddressException {
		if(address<0 || address>size) {
			throw new invalidAddressException();
		}
		return ram[address];
	}
	
	public void setTable(JTable table) {
		this.table = table;
		for(int i=0;i<size;i++) {
			if (ram[i] < 10)
				table.setValueAt("00"+ram[i], i/10, i%10);
			else if(ram[i] < 100)
				table.setValueAt("0"+ram[i], i/10, i%10);
			else
				table.setValueAt(""+ram[i], i/10, i%10);
		}
	}
}
