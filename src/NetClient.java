import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class NetClient {
	public int getUdpPort() {
		return udpPort;
	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	private int udpPort;
	private TankClient tc;
	private DatagramSocket ds = null;
	private String ip;
	public NetClient(TankClient tc) {
		this.tc = tc;
	}

	public void send(Msg msg) {
		msg.send(ds, ip, TankServer.UDP_PORT);
	}

	public void connect(String ip, int port) {
		Socket s = null;
		this.ip = ip;
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}

		try {
			s = new Socket(ip, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int id = dis.readInt();
			if(id%2==0){
				tc.getMyTank().setEnemy(false);
			}else{
				tc.getMyTank().setEnemy(true);
			}
			tc.getMyTank().id = id;
			System.out.println("A Client Connecting to Server");
			TankNewMsg msg = new TankNewMsg(tc.getMyTank());
			send(msg);
			new Thread(new UDPReceiveThread()).start();
			new SyncronizationThread().start();
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
			try {
				if (s != null) {
					s.close();
				}
			} catch (IOException exception1) {
				// TODO Auto-generated catch block
				exception1.printStackTrace();
			}
		}
	}
	private class SyncronizationThread  extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
System.out.println("Sync Starts id: "+tc.getMyTank().getId());
			while(tc.getMyTank().isAlive()){
				try {
					SyncronizationThread.sleep(3000);
					sendSyncronizationMsg();
				} catch (InterruptedException exception) {
					// TODO Auto-generated catch block
					exception.printStackTrace();
				}
			}
		}
		
		public void sendSyncronizationMsg(){
			Msg msg = new TankAlreadyExistMsg(tc.getMyTank());
			NetClient.this.send(msg);	
		}
		
	}
	private class UDPReceiveThread implements Runnable {
		private byte[] arr = new byte[1024];

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while (ds != null) {
					DatagramPacket dp = new DatagramPacket(arr, arr.length);
					ds.receive(dp);
					parse(dp);
					System.out.println("A Packted Received From Server ");
				}
			} catch (IOException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			}
		}

		public void parse(DatagramPacket dp) {

			ByteArrayInputStream bais = new ByteArrayInputStream(arr, 0,
					dp.getLength());
			DataInputStream dis = new DataInputStream(bais);
			try {
				int msgType = dis.readInt();
				switch (msgType) {
				case Msg.TankMOVEMSG:
					TankMoveMsg msg = new TankMoveMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				case Msg.TankNEWMSG:
					TankNewMsg msg2 = new TankNewMsg(NetClient.this.tc);
					msg2.parse(dis);
					break;
				case Msg.MissileNEWMSG:
					MissileNewMsg msg3 = new MissileNewMsg(NetClient.this.tc);
					msg3.parse(dis);
					break;
				case Msg.TankDEADMSG:
					TankDeadMsg msg4 = new TankDeadMsg(NetClient.this.tc);
					msg4.parse(dis);
					break;
				case Msg.MissileDEADMSG:
					MissileDeadMsg msg5 = new MissileDeadMsg(NetClient.this.tc);
					msg5.parse(dis);
					break;
				case Msg.TankAlreadyExistMsg:
					TankAlreadyExistMsg msg6 = new TankAlreadyExistMsg(NetClient.this.tc);
					msg6.parse(dis);
					break;
				}
			} catch (IOException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			}

		}
	}
}
