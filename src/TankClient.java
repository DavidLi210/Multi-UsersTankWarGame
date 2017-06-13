import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.omg.CORBA.TCKind;

public class TankClient extends Frame { // 2nd Method to create frame,better
										// than create frame object directly

	public Tank getMyTank() {
		return myTank;
	}

	public List<Tank> getEnemies() {
		return enemies;
	}

	public List<Explosion> getExplosions() {
		return explosions;
	}

	public ArrayList<Missile> getMissiles() {
		return missiles;
	}

	@Override
	public void update(Graphics g) {
		// TODO Auto-generated method stub
		if (bufferedImage == null) {
			bufferedImage = this.createImage(WIDTH, HEIGHT);
		}
		Graphics imageGraphics = bufferedImage.getGraphics();
		Color c = imageGraphics.getColor();
		imageGraphics.setColor(Color.GREEN);
		imageGraphics.fillRect(0, 0, WIDTH, HEIGHT);
		imageGraphics.setColor(c);
		paint(imageGraphics);
		g.drawImage(bufferedImage, 0, 0, null);
	}

	private final static int INIT_X = 50;
	private final static int INIT_Y = 50;
	private Tank myTank = new Tank(this, INIT_X, INIT_Y, false);
	private List<Tank> enemies = new ArrayList<>();
	private Image bufferedImage = null;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private List<Explosion> explosions = new ArrayList<>();
	public NetClient nc = new NetClient(this);
	private ConnDialog cd = new ConnDialog();
	private ArrayList<Missile> missiles = new ArrayList<>();
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		g.drawString("missiles count:" + missiles.size(), 10, 50);
		g.drawString("tanks count:" + enemies.size(), 10, 60);
		g.drawString("explosions count:" + explosions.size(), 10, 70);

		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			if(m.hitEnemy(myTank)){
				TankDeadMsg tankDeadMsg = new TankDeadMsg(myTank.getId());
				this.nc.send(tankDeadMsg);
				MissileDeadMsg missleDeadMsg = new MissileDeadMsg(m.getTankId(), m.getId());
				this.nc.send(missleDeadMsg);
			}
			m.draw(g);
		}
		for (int i = 0; i < explosions.size(); i++) {
			Explosion explosion = explosions.get(i);
			explosion.draw(g);
		}
		for (int i = 0; i < enemies.size(); i++) {
			Tank enemy = enemies.get(i);
			enemy.draw(g);
		}
		myTank.draw(g);//changed
	}

	public void launchFrame() {
		/*
		 * for(int i =0;i<10;i++){ enemies.add(new Tank(this, 50+i*60, 100,
		 * true)); }
		 */
		this.setLocation(400, 400);
		this.setSize(WIDTH, HEIGHT);
		this.setVisible(true);
		this.setBackground(Color.GREEN);
		this.setResizable(false);
		this.setTitle("MyTankWar");
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}

		});
		this.addKeyListener(new KeyBoardListener());
		// nc.connect("127.0.0.1", TankServer.TCP_PORT);
		new Thread(new PaintThread()).start();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TankClient().launchFrame();
	}

	class PaintThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				repaint();
				try {
					PaintThread.sleep(100);
				} catch (InterruptedException exception) {
					// TODO Auto-generated catch block
					exception.printStackTrace();
				}
			}
		}

	}

	class KeyBoardListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_C:
				cd.setVisible(true);
				break;
			default:
				myTank.keyReleased(e);
				break;
			}

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			myTank.keyPressed(e);
		}

	}

	class ConnDialog extends Dialog {
		Button button = new Button("Yes");
		TextField ipField = new TextField("127.0.0.1", 12);

		TextField portField = new TextField("" + TankServer.TCP_PORT, 5);
		TextField udpPortField = new TextField("2223", 5);

		public ConnDialog() {
			super(TankClient.this, "Please Input A Valid IP And Port", true);
			this.setLayout(new FlowLayout());
			this.add(new Label("Host IP:"));
			this.add(ipField);
			this.add(new Label("Host Port:"));
			this.add(portField);
			this.add(new Label("Client UDPPort:"));
			this.add(udpPortField);
			this.add(button);
			this.setLocation(300, 300);
			this.pack();
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					// TODO Auto-generated method stub
					setVisible(false);
				}

			});
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String ip = ipField.getText().trim();
					int tcpPort = Integer.parseInt(portField.getText().trim());
					int udpPort = Integer.parseInt(udpPortField.getText()
							.trim());
					System.out.println(+tcpPort + "||" + udpPort);
					nc.setUdpPort(udpPort);
					nc.connect(ip, tcpPort);
					setVisible(false);
				}
			});
		}
	}
}
