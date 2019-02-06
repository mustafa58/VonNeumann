import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Color;
import javax.swing.JEditorPane;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import java.awt.SystemColor;
import java.awt.FlowLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.border.EtchedBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import errorHandling.invalidAddressException;
import errorHandling.regOverflowException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;



public class YeniPencere extends JFrame {

	/**
	 * innerClass
	 */
	
	public class Moment {
		private int insCycle;
		private int insAdr;
		private String ins;
		private int valAcc;
		public int getInsCycle() {
			return insCycle;
		}
		public void setInsCycle(int insCycle) {
			this.insCycle = insCycle;
		}
		public int getInsAdr() {
			return insAdr;
		}
		public Moment(int insCycle, int insAdr, String ins, int valAcc) {
			super();
			this.insCycle = insCycle;
			this.insAdr = insAdr;
			this.ins = ins;
			this.valAcc = valAcc;
		}
		public void setInsAdr(int insAdr) {
			this.insAdr = insAdr;
		}
		public String getIns() {
			return ins;
		}
		public void setIns(String ins) {
			this.ins = ins;
		}
		public int getValAcc() {
			return valAcc;
		}
		public void setValAcc(int valAcc) {
			this.valAcc = valAcc;
		}
	}
	
	
	
	private static final long serialVersionUID = -3151430061368571784L;
	private JPanel contentPane;
	private int befAdr;
	private int befIns;
	private Timer timer;
	private int insCounter;
	private Queue<Moment> sira = new LinkedList<Moment>();
	private JTextPane textAcc;
	private JTextPane textPrgCnt;
	private JTextPane textInst1;
	private JTextPane textInst2;
	private JTextPane txtpnIns;
	private JTextPane txtpnInstructionCycle;
	private JCheckBox chckbxSes;
	private int clock;
	private int wait = 500;
	private boolean runFlag = true;
	private JTable table;
	private RAM ram;
	private Processor cpu;
	private JTable traceView;
	
	int say = 0;
	long top = 0;
	
	/**
	 * Create the frame.
	 */
	
	
	public String threeDigit(int val) {
		String str;
		if(val <= -10)
			str = "" + val;
		else if(val<0 && val>-10)
			str = "-0" + -1*val;
		else if(val < 10)
			str = "00" + val;
		else if(val < 100)
			str = "0" + val;
		else
			str = "" + val;
		return str;
	}
	
	public String instDecode(int ins) {
		if (ins < 100) {
			return "  HLT  ";
		}
		else if (ins < 200) {
			return "  ADD  ";
		}
		else if (ins < 300) {
			return "  SUB  ";
		}
		else if (ins < 400) {
			return "  STO  ";
		}
		else if (ins < 500) {
			return "  NOP  ";
		}
		else if (ins < 600) {
			return "  LDA  ";
		}
		else if (ins < 700) {
			return "  BR  ";
		}
		else if (ins < 800) {
			return "  BRZ  ";
		}
		else if (ins < 900) {
			return "  BRP  ";
		}
		else if (ins == 901) {
			return "  IN  ";
		}
		else if (ins == 902) {
			return "  OUT  ";
		}
		else 
			return null;
	}
	
	public void traceList(Moment an) {
		if(insCounter < 16) {
			sira.add(an);
			insCounter++;
		}
		else
		{
			sira.remove();
			sira.add(an);
		}
	}
	
	
	public void exec() {
		timer = new Timer(wait, new ActionListener() {
	        public void actionPerformed(ActionEvent e1) {
	        	try {
	        		long start = System.nanoTime();
		        	cpu.execute();
		        	traceList(new Moment(clock, befAdr, instDecode(befIns), cpu.getAccumulator().getValue()));
					textPrgCnt.setText(threeDigit(cpu.getProgramCounter().getValue()));
					textAcc.setText(threeDigit(cpu.getAccumulator().getValue()));
					txtpnIns.setText(instDecode(befIns));
					textInst1.setText("" + befIns/100);
					if(chckbxSes.isSelected())
						java.awt.Toolkit.getDefaultToolkit().beep();
					if(befIns%100 < 10)
						textInst2.setText("0" + befIns%100);
					else
						textInst2.setText("" + befIns%100);
					txtpnInstructionCycle.setText(clock++ + " ");
					befAdr = cpu.getProgramCounter().getValue();
					befIns = ram.write(cpu.getProgramCounter().getValue());
					int i = 0;
					for(Moment m: sira) {
						traceView.setValueAt(m.getInsCycle(), i, 0);
						traceView.setValueAt(m.getIns(), i, 1);
						traceView.setValueAt(m.getInsAdr(), i, 2);
						traceView.setValueAt(m.getValAcc(), i, 3);
						i++;
					}
					long end = System.nanoTime();
					say++;
					top = top + (end-start);
					//System.out.println("Execution time is " + (end-start));
					System.out.println(say + " " + top);
					if(!runFlag || befIns<100)
						timer.stop();
	        	} catch (invalidAddressException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(getOwner(), "geçersiz bir adres kullanýldý", "Hata", JOptionPane.ERROR_MESSAGE);
					timer.stop();
	        	} catch (regOverflowException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(getOwner(), "register mevcut deðeri saklamak için yetersiz", "Hata", JOptionPane.ERROR_MESSAGE);
					timer.stop();
	        	} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					timer.stop();
				}
	        }
	    });
		timer.start();
	}
	
	public YeniPencere(RAM ram) {
		this.ram = ram;
		this.cpu = new Processor(ram);
		try {
			befIns = ram.write(0);
		} catch (invalidAddressException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1040, 696);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.LIGHT_GRAY);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.LIGHT_GRAY);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.LIGHT_GRAY);
		
		JSlider slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				wait = slider.getValue() * 10;
			}
		});
		slider.setSnapToTicks(true);
		panel_4.add(slider);
		
		JSeparator separator = new JSeparator();
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.LIGHT_GRAY);
		
		JButton button = new JButton("Run");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exec();
			}
		});
		panel_5.add(button);
		
		JButton button_1 = new JButton("Stop");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runFlag = false;
			}
		});
		panel_5.add(button_1);
		
		JButton button_2 = new JButton("Step");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					cpu.execute();
		        	traceList(new Moment(clock, befAdr, instDecode(befIns), cpu.getAccumulator().getValue()));
					textPrgCnt.setText(threeDigit(cpu.getProgramCounter().getValue()));
					textAcc.setText(threeDigit(cpu.getAccumulator().getValue()));
					txtpnIns.setText(instDecode(befIns));
					textInst1.setText("" + befIns/100);
					if(chckbxSes.isSelected())
						java.awt.Toolkit.getDefaultToolkit().beep();
					if(befIns%100 < 10)
						textInst2.setText("0" + befIns%100);
					else
						textInst2.setText("" + befIns%100);
					txtpnInstructionCycle.setText(clock++ + " ");
					befAdr = cpu.getProgramCounter().getValue();
					befIns = ram.write(cpu.getProgramCounter().getValue());
					int i = 0;
					for(Moment m: sira) {
						traceView.setValueAt(m.getInsCycle(), i, 0);
						traceView.setValueAt(m.getIns(), i, 1);
						traceView.setValueAt(m.getInsAdr(), i, 2);
						traceView.setValueAt(m.getValAcc(), i, 3);
						i++;
					}
				} catch (invalidAddressException eq) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(getOwner(), "geçersiz bir adres kullanýldý", "Hata", JOptionPane.ERROR_MESSAGE);
				} catch (regOverflowException eq) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(getOwner(), "register mevcut deðeri saklamak için yetersiz", "Hata", JOptionPane.ERROR_MESSAGE);
				} catch (InterruptedException eq) {
					// TODO Auto-generated catch block
					eq.printStackTrace();
				}
			}
		});
		panel_5.add(button_2);
		
		JButton button_3 = new JButton("Restart");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					cpu.getProgramCounter().update(0);
					textPrgCnt.setText(threeDigit(cpu.getProgramCounter().getValue()));
				} catch (regOverflowException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(getOwner(), "register mevcut deðeri saklamak için yetersiz", "Hata", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_5.add(button_3);
		
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 321, Short.MAX_VALUE)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
						.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGap(0, 172, Short.MAX_VALUE)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
					.addGap(1)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JCheckBox chckbxKaynakKoduGster = new JCheckBox("Kaynak kodu göster");
		chckbxKaynakKoduGster.setBackground(Color.LIGHT_GRAY);
		panel_4.add(chckbxKaynakKoduGster);
		
		chckbxSes = new JCheckBox("Ses");
		chckbxSes.setBackground(Color.LIGHT_GRAY);
		panel_4.add(chckbxSes);
		panel_3.setLayout(gl_panel_3);
		
		JPanel panel_10 = new JPanel();
		panel_10.setLayout(null);
		panel_10.setBackground(Color.LIGHT_GRAY);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setText("test\r\n");
		editorPane.setForeground(Color.WHITE);
		editorPane.setFont(new Font("Consolas", Font.PLAIN, 14));
		editorPane.setEditable(false);
		editorPane.setBackground(new Color(0, 128, 0));
		editorPane.setBounds(12, 13, 299, 266);
		panel_10.add(editorPane);
		
		JLabel label_20 = new JLabel("I/O Log");
		label_20.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_20.setBounds(12, 0, 56, 16);
		panel_10.add(label_20);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 347, Short.MAX_VALUE)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
							.addGap(121))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 321, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(panel_10, GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 521, Short.MAX_VALUE)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_10, GroupLayout.PREFERRED_SIZE, 292, GroupLayout.PREFERRED_SIZE)
					.addGap(0)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
					.addGap(0)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(18, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		JPanel tab1 = new JPanel();
		tabbedPane.addTab("Donaným", null, tab1, null);
		tabbedPane.setEnabledAt(0, true);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JPanel ramPanel = new JPanel();
		ramPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GroupLayout gl_tab1 = new GroupLayout(tab1);
		gl_tab1.setHorizontalGroup(
			gl_tab1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_tab1.createSequentialGroup()
					.addContainerGap(20, Short.MAX_VALUE)
					.addGroup(gl_tab1.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(ramPanel, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_tab1.setVerticalGroup(
			gl_tab1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tab1.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ramPanel, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		table = new JTable() {
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if(isRowSelected(row) && isColumnSelected(column))
                	c.setBackground(Color.RED);
                else
                	c.setBackground(Color.BLACK);
                return c;
           }
		};
		table.setRowSelectionAllowed(false);
		table.setFont(new Font("Monospaced", Font.BOLD, 15));
		table.setForeground(Color.WHITE);
		table.setBackground(Color.BLACK);
		table.setBounds(48, 31, 350, 200);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, "", null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null},
			},
			new String[] {
				"_0", "_1", "_2", "_3", "_4", "_5", "_6", "_7", "_8", "_9"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(35);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(35);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(2).setPreferredWidth(35);
		table.getColumnModel().getColumn(3).setResizable(false);
		table.getColumnModel().getColumn(3).setPreferredWidth(35);
		table.getColumnModel().getColumn(4).setResizable(false);
		table.getColumnModel().getColumn(4).setPreferredWidth(35);
		table.getColumnModel().getColumn(5).setResizable(false);
		table.getColumnModel().getColumn(5).setPreferredWidth(35);
		table.getColumnModel().getColumn(6).setResizable(false);
		table.getColumnModel().getColumn(6).setPreferredWidth(35);
		table.getColumnModel().getColumn(7).setResizable(false);
		table.getColumnModel().getColumn(7).setPreferredWidth(35);
		table.getColumnModel().getColumn(8).setResizable(false);
		table.getColumnModel().getColumn(8).setPreferredWidth(35);
		table.getColumnModel().getColumn(9).setResizable(false);
		table.getColumnModel().getColumn(9).setPreferredWidth(35);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		ramPanel.setLayout(null);
		table.setRowHeight(20);
		ram.setTable(table);
		ramPanel.add(table);
		
		JLabel label = new JLabel("_0");
		label.setBounds(60, 13, 28, 17);
		ramPanel.add(label);
		
		JLabel label_1 = new JLabel("_1");
		label_1.setBounds(96, 13, 34, 17);
		ramPanel.add(label_1);
		
		JLabel label_2 = new JLabel("_2");
		label_2.setBounds(127, 13, 28, 16);
		ramPanel.add(label_2);
		
		JLabel label_3 = new JLabel("_3");
		label_3.setBounds(167, 13, 28, 16);
		ramPanel.add(label_3);
		
		JLabel label_4 = new JLabel("_4");
		label_4.setBounds(201, 13, 28, 16);
		ramPanel.add(label_4);
		
		JLabel label_5 = new JLabel("_5");
		label_5.setBounds(232, 13, 34, 16);
		ramPanel.add(label_5);
		
		JLabel label_6 = new JLabel("_6");
		label_6.setBounds(268, 13, 39, 17);
		ramPanel.add(label_6);
		
		JLabel label_7 = new JLabel("_7");
		label_7.setBounds(303, 13, 34, 17);
		ramPanel.add(label_7);
		
		JLabel label_8 = new JLabel("_8");
		label_8.setBounds(340, 13, 28, 17);
		ramPanel.add(label_8);
		
		JLabel label_9 = new JLabel("_9");
		label_9.setBounds(370, 13, 28, 16);
		ramPanel.add(label_9);
		
		JLabel label_10 = new JLabel("0_");
		label_10.setBounds(12, 32, 28, 16);
		ramPanel.add(label_10);
		
		JLabel label_11 = new JLabel("1_");
		label_11.setBounds(12, 55, 28, 16);
		ramPanel.add(label_11);
		
		JLabel label_12 = new JLabel("2_");
		label_12.setBounds(12, 75, 28, 16);
		ramPanel.add(label_12);
		
		JLabel label_13 = new JLabel("3_");
		label_13.setBounds(12, 94, 28, 16);
		ramPanel.add(label_13);
		
		JLabel label_14 = new JLabel("4_");
		label_14.setBounds(12, 115, 28, 16);
		ramPanel.add(label_14);
		
		JLabel label_15 = new JLabel("5_");
		label_15.setBounds(12, 134, 28, 16);
		ramPanel.add(label_15);
		
		JLabel label_16 = new JLabel("6_");
		label_16.setBounds(12, 155, 28, 16);
		ramPanel.add(label_16);
		
		JLabel label_17 = new JLabel("7_");
		label_17.setBounds(12, 175, 28, 16);
		ramPanel.add(label_17);
		
		JLabel label_18 = new JLabel("8_");
		label_18.setBounds(12, 194, 28, 16);
		ramPanel.add(label_18);
		
		JLabel label_19 = new JLabel("9_");
		label_19.setBounds(12, 215, 28, 16);
		ramPanel.add(label_19);
		
		JLabel lblRam = new JLabel("RAM");
		lblRam.setBounds(12, 0, 56, 16);
		ramPanel.add(lblRam);
		
		
		
		JPanel panel_7 = new JPanel();
		panel_7.setBackground(SystemColor.textHighlight);
		
		JLabel lblAccumulator = new JLabel("Accumulator");
		lblAccumulator.setForeground(Color.WHITE);
		panel_7.add(lblAccumulator);
		
		textAcc = new JTextPane();
		textAcc.setEditable(false);
		panel_7.add(textAcc);
		textAcc.setText("000");
		cpu.setTextAcc(textAcc);
		
		JPanel panel_8 = new JPanel();
		panel_8.setBackground(SystemColor.textHighlight);
		
		JLabel lblProgrCnt = new JLabel("Progr. Cnt");
		lblProgrCnt.setForeground(Color.WHITE);
		panel_8.add(lblProgrCnt);
		
		textPrgCnt = new JTextPane();
		textPrgCnt.setEditable(false);
		textPrgCnt.setText("000");
		cpu.setTextProgCount(textPrgCnt);
		panel_8.add(textPrgCnt);
		
		JPanel panel_9 = new JPanel();
		panel_9.setBackground(SystemColor.textHighlight);
		
		txtpnInstructionCycle = new JTextPane();
		txtpnInstructionCycle.setText("0 ");
		
		JLabel lblIntructionCycle = new JLabel("Intruction Cycle:");
		
		JLabel lblProcessor = new JLabel("PROCESSOR");
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addGap(62)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblIntructionCycle)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(txtpnInstructionCycle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(53))
				.addGroup(gl_panel_6.createSequentialGroup()
					.addComponent(lblProcessor)
					.addContainerGap(309, Short.MAX_VALUE))
		);
		gl_panel_6.setVerticalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addComponent(lblProcessor)
					.addGap(25)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
							.addGap(42)
							.addComponent(panel_8, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblIntructionCycle)
								.addComponent(txtpnInstructionCycle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
							.addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)))
					.addGap(42))
		);
		panel_9.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		Component horizontalStrut_4 = Box.createHorizontalStrut(20);
		panel_9.add(horizontalStrut_4);
		
		JLabel lblInstrReg = new JLabel("Instr. Reg");
		lblInstrReg.setForeground(Color.WHITE);
		panel_9.add(lblInstrReg);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		panel_9.add(horizontalStrut_1);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel_9.add(horizontalStrut);
		
		textInst1 = new JTextPane();
		textInst1.setEditable(false);
		panel_9.add(textInst1);
		textInst1.setText("" + befIns/100);
		
		textInst2 = new JTextPane();
		textInst2.setEditable(false);
		panel_9.add(textInst2);
		textInst1.setText("" + befIns/100);
		if(befIns%100 < 10)
			textInst2.setText("0" + befIns%100);
		else
			textInst2.setText("" + befIns%100);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		panel_9.add(horizontalStrut_2);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		panel_9.add(horizontalStrut_3);
		
		txtpnIns = new JTextPane();
		txtpnIns.setEditable(false);
		panel_9.add(txtpnIns);
		txtpnIns.setText(instDecode(befIns));
		
		Component horizontalStrut_5 = Box.createHorizontalStrut(20);
		panel_9.add(horizontalStrut_5);
		panel_6.setLayout(gl_panel_6);
		tab1.setLayout(gl_tab1);
		
		JPanel tab2 = new JPanel();
		tabbedPane.addTab("Yaz\u0131l\u0131m", null, tab2, null);
		tab2.setLayout(new BorderLayout(0, 0));
		
		traceView = new JTable();
		traceView.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			},
			new String[] {
				"Instrcution Cycle", "Instruction", "Instruction Addr", "Accumulator"
			}
		));
		traceView.setRowHeight(30);
		tab2.add(traceView, BorderLayout.CENTER);
		tab2.add(traceView.getTableHeader(), BorderLayout.NORTH);
		tabbedPane.setEnabledAt(1, true);
		tabbedPane.setSelectedIndex(0);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(72)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(63))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(26)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(553, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		contentPane.add(panel);
	}
}
