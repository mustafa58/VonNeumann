import java.awt.EventQueue;

public class Main {
	public Main() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Arayuz window = new Arayuz();
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
