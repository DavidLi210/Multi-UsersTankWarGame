import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;


public class TankServer {
	private static int id =100;
	public static final int TCP_PORT = 8888;
	public static final int UDP_PORT = 6666;
	private List<Client> clients = new ArrayList<>();
	private ServerSocket ss;
	public void start(){
		new Thread(new UDPThread()).start();
		Socket socket = null;
		try {
			ss = new ServerSocket(TCP_PORT);
			while(true){
				socket = ss.accept();
System.out.println(" Server Receive A Connection");
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				int udpPort = dis.readInt();
				String ip = socket.getInetAddress().getHostAddress();
				Client c = new Client(udpPort, ip);
				clients.add(c);
				
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeInt(id++);
System.out.println(" Client Connected : Addr - "+socket.getInetAddress()+" : "+socket.getPort()+"-------UDP PORT: "+udpPort);
			}
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		} finally {
			try {
				if(socket!=null){
					socket.close();
				}
			} catch (IOException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TankServer().start();
	}
	private class UDPThread implements Runnable{
		DatagramSocket ds = null;
		byte [] buf = new byte[1024];
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				ds = new DatagramSocket(UDP_PORT);
System.out.println(" UDP Server Started at "+UDP_PORT);
			} catch (SocketException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			}
			
			while(ds!=null){
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					for(int i = 0;i<clients.size();i++){
						Client c = clients.get(i);
						dp.setSocketAddress(new InetSocketAddress(c.ipAddress, c.udpPort));
						ds.send(dp);
					}
System.out.println(" A Packet Received ");
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
		
	}
	private class Client{
		public int udpPort;
		public String ipAddress;
		public Client(int udpPort, String ipAddress) {
			this.udpPort = udpPort;
			this.ipAddress = ipAddress;
		}
	}
}
