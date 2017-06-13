import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;


public class MissileNewMsg implements Msg{
	private final int type = Msg.MissileNEWMSG;
	private TankClient tc;
	private Missile m;
	
	
	public MissileNewMsg(Missile m) {
		this.m = m;
	}

	public MissileNewMsg(TankClient tc) {
		this.tc = tc;
	}

	@Override
	public void send(DatagramSocket ds, String ip, int port) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(type);
			dos.writeInt(m.getTankId());
			dos.writeInt(m.getX());
			dos.writeInt(m.getY());
			dos.writeInt(m.getDir().ordinal());
			dos.writeBoolean(m.isEnemy());
			System.out.println(" One MissileNewMsg Sended");
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
			int id = dis.readInt();
			if(id==tc.getMyTank().getId()){
				return;
			}
			
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			boolean isEnemy = dis.readBoolean();
			Missile m = new Missile(id, x, y,tc, dir,isEnemy);			
			tc.getMissiles().add(m);
			
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
	}

}
