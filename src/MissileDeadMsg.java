import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;


public class MissileDeadMsg implements Msg{
	private final int type = Msg.MissileDEADMSG;
	private int tankId;
	private int missileId;
	
	private TankClient tc;
	public MissileDeadMsg(TankClient tc) {

		this.tc = tc;
	}

	public MissileDeadMsg(int tankId, int missileId) {

		this.tankId = tankId;
		this.missileId = missileId;
	}

	@Override
	public void send(DatagramSocket ds, String ip, int port) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(type);
			dos.writeInt(tankId);
			dos.writeInt(missileId);// changed!!!
			
			System.out.println(" One MissileDEADMSG Sended");
		} catch (IOException exception) {
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
			int missileId = dis.readInt(); //changed!!!
			List<Missile> missiles = tc.getMissiles();
			for(int i =0;i<missiles.size();i++){
				Missile m = missiles.get(i);
				if(m.getTankId()==tankId&&missileId==m.getId()){
					m.setAlive(false);
					System.out.println(" Set Missile To Be False ");
					tc.getExplosions().add(new Explosion(m.getX(), m.getY(), tc));
					break;
				}
			}
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
	}
	
}
