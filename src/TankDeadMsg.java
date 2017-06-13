import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;


public class TankDeadMsg implements Msg{
	private final int type = Msg.TankDEADMSG;
	
	private int tankId;
	private TankClient tc;
	
	
	public TankDeadMsg(TankClient tc) {
		this.tc = tc;
	}

	public TankDeadMsg(int tankId) {
		this.tankId = tankId;
	}

	@Override
	public void send(DatagramSocket ds, String ip, int port) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(type);
			dos.writeInt(tankId);

			System.out.println(" One TankDeadMsg Sended");
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		} finally{
			/*Not Sure Whether this part for Closing the Streams would close any problems*/
			try {
				if(dos!=null) dos.close();
				if(baos!=null) baos.close();
			} catch (IOException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			}
		}
		try {
			byte [] arr = baos.toByteArray();
			DatagramPacket dp = new DatagramPacket(arr, arr.length, new InetSocketAddress(ip, port));
			ds.send(dp);
		} catch(SocketException exception){
			exception.printStackTrace();
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
	}

	@Override
	public void parse(DataInputStream dis) {
		// TODO Auto-generated method stub
		try {
			int tankId = dis.readInt();
			if(tankId==tc.getMyTank().getId()){
				return;
			}

			for(int i =0;i<tc.getEnemies().size();i++){
				if(tc.getEnemies().get(i).id==tankId){
					tc.getEnemies().get(i).setAlive(false);
					break;
				}
			}
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
	}

}
