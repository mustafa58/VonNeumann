import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.util.Random;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import errorHandling.invalidAddressException;
import errorHandling.invalidRAMsizeException;
import errorHandling.regOverflowException;
import errorHandling.unknownCommandException;

import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Box;
import javax.swing.JTextPane;
import java.awt.Component;

import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.UIManager;


public class Arayuz {

	private JFrame frame;
	private String text;
	private JCheckBox chckBox;
	private JTextArea console;
	private JEditorPane editor;
	protected GroupLayout groupLayout;
	protected JScrollPane scrollPane = new JScrollPane();
	private RAM ram;
	private int[] prog;
	
	/**
	 * Launch the application.
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Create the application.
	 */
	public Arayuz() {
		initialize();
	}
	
	public String readFromFile(String pathToFile) {
		String text = "";
		try {
			File file = new File(pathToFile);
			FileInputStream inp = new FileInputStream(file);
			int size = inp.available();
			for(int i=0;i<size;i++) {
				text = text + (char)inp.read();
			}
			inp.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
	public String readFromFile(File file) {
		String text = "";
		try {
			FileInputStream inp = new FileInputStream(file);
			int size = inp.available();
			for(int i=0;i<size;i++) {
				text = text + (char)inp.read();
			}
			inp.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
	
	public void updateConsl() {
		int i = 0;
		console.setText("MMN:\tARG:\tOBJCODE:");
		while(prog[i] != 0) {
			if (prog[i] < 100) {
				console.setText(console.getText() + "\nHLT" + "\t-\t000");
			}
			else if (prog[i] < 200) {
				console.setText(console.getText() +'\n'+ "ADD"+ '\t' + (prog[i]-100) + '\t'+ prog[i]);
			}
			else if (prog[i] < 300) {
				console.setText(console.getText() +'\n'+ "SUB"+ '\t' + (prog[i]-200) + '\t'+ prog[i]);
			}
			else if (prog[i] < 400) {
				console.setText(console.getText() +'\n'+ "STO"+ '\t' + (prog[i]-300) + '\t'+ prog[i]);
			}
			else if (prog[i] < 500) {
				console.setText(console.getText() + "\nNOP" + "\t-\t"+ prog[i]);
			}
			else if (prog[i] < 600) {
				console.setText(console.getText() +'\n'+ "LDA"+ '\t' + (prog[i]-500) + '\t'+ prog[i]);
			}
			else if (prog[i] < 700) {
				console.setText(console.getText() +'\n'+ "BR"+ '\t' + (prog[i]-600) + '\t'+ prog[i]);
			}
			else if (prog[i] < 800) {
				console.setText(console.getText() +'\n'+ "BRZ"+ '\t' + (prog[i]-700) + '\t'+ prog[i]);
			}
			else if (prog[i] < 900) {
				console.setText(console.getText() +'\n'+ "BRP"+ '\t' + (prog[i]-800) + '\t'+ prog[i]);
			}
			else if (prog[i] == 901) {
				console.setText(console.getText() + "\nIN" + "\t-\t"+ prog[i]);
			}
			else if (prog[i] == 902) {
				console.setText(console.getText() +'\n'+ "OUT" + "\t-\t"+ prog[i]);
			}
			i++;
			/*case IN:
				console.setText(console.getText() +'\n'+ c.toString()+ "\t-\t"+ c.getObjCode());
				break;
			case OUT:
				console.setText(console.getText() +'\n'+ c.toString()+ "\t-\t"+ c.getObjCode());
				break;
			case NOP:
				console.setText(console.getText() +'\n'+ c.toString()+ "\t-\t"+ c.getObjCode());
				break;
			case HLT:
				console.setText(console.getText() +'\n'+ c.toString()+ "\t-\t000");
				break;
			default:
				console.setText(console.getText() +'\n'+ c.toString()+ '\t' +c.getArg() + '\t'+ c.getObjCode());
				break;
			}*/
		}
		console.setText(console.getText() + "\nHLT" + "\t-\t000");
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1173, 769);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Box horizontalBox = Box.createHorizontalBox();
		
		JPanel altpanel = new JPanel();
		
		editor = new JEditorPane();
		editor.setText("test");
		editor.setFont(new Font("Tahoma", Font.PLAIN, 15));
		editor.setText("test");
		editor.setForeground(Color.BLACK);
		editor.setBackground(UIManager.getColor("activeCaption"));
		
		
		groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(horizontalBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(altpanel, GroupLayout.PREFERRED_SIZE, 440, GroupLayout.PREFERRED_SIZE)
					.addGap(26)
					.addComponent(editor, GroupLayout.PREFERRED_SIZE, 636, GroupLayout.PREFERRED_SIZE)
					.addGap(23))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(horizontalBox, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)
							.addGap(224))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
									.addGap(3)
									.addComponent(editor, GroupLayout.PREFERRED_SIZE, 667, GroupLayout.PREFERRED_SIZE))
								.addComponent(altpanel, GroupLayout.PREFERRED_SIZE, 595, Short.MAX_VALUE))
							.addContainerGap())))
		);
		
		JTextPane txtpnBaslik = new JTextPane();
		txtpnBaslik.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtpnBaslik.setContentType("text/html");
		txtpnBaslik.setEditable(false);
		txtpnBaslik.setText("<html><center><h2><font size=\"8\">ESMS<br />Düzenleyici<br />Ekraný</font></h2><BR><center>");
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox_1.add(horizontalGlue);
		
		JButton btnValidate = new JButton("Validate");
		btnValidate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Assembler assmb = new Assembler();
					prog = assmb.parse(editor.getText());
					updateConsl();
					JOptionPane.showMessageDialog(frame, "Derleme sýrasýnda herhangi bir sorun çýkmadý");
				} catch (invalidAddressException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (unknownCommandException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//if(prog!=null && new Assembler().validate(prog)) {
					
				//}
				//else JOptionPane.showMessageDialog(frame, "Derleme hatasý");
			}
		});
		btnValidate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnValidate.setHorizontalAlignment(SwingConstants.LEFT);
		horizontalBox_1.add(btnValidate);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		horizontalBox_1.add(horizontalGlue_2);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int i = 0;
				try {
					if(chckBox.isSelected())
						ram = new RAM(new int[100], 100);
					else
						ram = new RAM(new Random(), 100);
					Assembler assmb = new Assembler();
					while(prog[i] != 0) {
						ram.read(prog[i], i);
						i++;
					}
					ram.read(prog[i], i);
					assmb.parseDat(editor.getText(), ram);
				} catch (invalidRAMsizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (invalidAddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (regOverflowException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				YeniPencere y = new YeniPencere(ram, editor.getText());
				y.setVisible(true);
			}
		});
		btnLoad.setFont(new Font("Tahoma", Font.PLAIN, 16));
		horizontalBox_1.add(btnLoad);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalBox_1.add(horizontalGlue_1);
		
		Box horizontalBox_2 = Box.createHorizontalBox();
		
		Component horizontalGlue_4 = Box.createHorizontalGlue();
		horizontalBox_2.add(horizontalGlue_4);
		
		chckBox = new JCheckBox("Yüklemeden Önce Hafýzayý Sýfýrla");
		chckBox.setSelected(true);
		chckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		horizontalBox_2.add(chckBox);
		
		Component horizontalGlue_3 = Box.createHorizontalGlue();
		horizontalBox_2.add(horizontalGlue_3);
		
		JButton btnYardm = new JButton("Yardým");
		btnYardm.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton btnk = new JButton("Çýkýþ");
		btnk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnk.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GroupLayout gl_altpanel = new GroupLayout(altpanel);
		gl_altpanel.setHorizontalGroup(
			gl_altpanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_altpanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_altpanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_altpanel.createSequentialGroup()
							.addComponent(btnYardm)
							.addGap(84)
							.addComponent(btnk)
							.addGap(119))
						.addGroup(gl_altpanel.createSequentialGroup()
							.addComponent(horizontalBox_2, GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_altpanel.createSequentialGroup()
							.addComponent(horizontalBox_1, GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_altpanel.createSequentialGroup()
							.addComponent(txtpnBaslik, GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_altpanel.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 416, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())))
		);
		gl_altpanel.setVerticalGroup(
			gl_altpanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_altpanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 237, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtpnBaslik, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(horizontalBox_1, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(horizontalBox_2, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_altpanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnk)
						.addComponent(btnYardm))
					.addContainerGap(30, Short.MAX_VALUE))
		);
		
		console = new JTextArea();
		scrollPane.setViewportView(console);
		console.setText("MMN:\tARG:\tOBJCODE:");
		console.setForeground(Color.WHITE);
		console.setFont(new Font("Lucida Console", Font.BOLD, 17));
		console.setEditable(false);
		console.setBackground(Color.BLACK);
		altpanel.setLayout(gl_altpanel);
		
		frame.getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnDosya = new JMenu("File");
		menuBar.add(mnDosya);
		
		JMenuItem mnitmYeni = new JMenuItem("New");
		mnitmYeni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editor.setText("");
				console.setText("MMN:\tARG:\tOBJCODE:");
				prog = null;
			}
		});
		mnDosya.add(mnitmYeni);
		
		JMenuItem mnitmAc = new JMenuItem("Open");
		mnitmAc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();     
		        File file = new File(System.getProperty("user.dir"));
		        jfc.setCurrentDirectory(file);
		        FileNameExtensionFilter typefilter = new FileNameExtensionFilter("vvm files (*.vvm)", "vvm");
		        jfc.setFileFilter(typefilter);
		        jfc.setDialogTitle("Open Source File:");
		        jfc.showOpenDialog(frame);
		        File source = jfc.getSelectedFile();
				text = readFromFile(source);
				editor.setText(text);
			}
		});
		mnDosya.add(mnitmAc);
		
		JMenuItem mnitmKaydet = new JMenuItem("Save");
		mnitmKaydet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();     
		        File file = new File(System.getProperty("user.dir"));
		        jfc.setCurrentDirectory(file);
		        FileNameExtensionFilter typefilter = new FileNameExtensionFilter("vvm files (*.vvm)", "vvm");
		        jfc.setFileFilter(typefilter);
		        jfc.setApproveButtonText("Save");
		        jfc.setDialogTitle("Save Source File:");
		        jfc.showOpenDialog(frame);
				try(FileWriter fw = new FileWriter(jfc.getSelectedFile())) {
				    fw.write(editor.getText());
				    fw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnDosya.add(mnitmKaydet);
		
		JMenuItem mnitmFarklKaydet = new JMenuItem("Save As");
		mnitmFarklKaydet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();     
		        File file = new File(System.getProperty("user.dir"));
		        jfc.setCurrentDirectory(file);
		        jfc.setApproveButtonText("Save");
		        jfc.setDialogTitle("Save Source File:");
		        jfc.showOpenDialog(frame);
				try(FileWriter fw = new FileWriter(jfc.getSelectedFile())) {
				    fw.write(editor.getText());
				    fw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnDosya.add(mnitmFarklKaydet);
		
		JMenuItem mnitmExit = new JMenuItem("Exit");
		mnitmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnDosya.add(mnitmExit);
	}
}
