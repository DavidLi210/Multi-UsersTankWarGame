import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;


public class TankAlreadyExistMsg implements Msg {
	private TankClient tc;
	private Tank tank;
	private static final int type = Msg.TankAlreadyExistMsg;
	public TankAlreadyExistMsg(Tank tank) {
		this.tank = tank;
	}

	public TankAlreadyExistMsg(TankClient tc) {
		this.tc = tc;
	}
	public TankAlreadyExistMsg() {
		// TODO Auto-generated constructor stub
	}

	public void send(DatagramSocket ds,String ip,int port){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(Msg.TankAlreadyExistMsg);
			dos.writeInt(tank.getId());
			dos.writeInt(tank.getX());
			dos.writeInt(tank.getY());
			dos.writeInt(tank.getDir().ordinal());
			dos.writeBoolean(tank.isEnemy());
			System.out.println(" One TankAlreadyExistMsg Sended");
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

	public void parse(DataInputStream dis) {
		// TODO Auto-generated method stub
		try {
			int id = dis.readInt();
			if(id==tc.getMyTank().getId()){
				return;
			}
			boolean exist = false;
			for(int i =0;i<tc.getEnemies().size();i++){
				if(tc.getEnemies().get(i).id==id){
					exist = true;
					break;
				}
			}
			if(!exist){
				int x = dis.readInt();
				int y = dis.readInt();
				Direction dir = Direction.values()[dis.readInt()];
				boolean isEnemy = dis.readBoolean();
				Tank t = new Tank(tc, x, y, dir, id);
				t.setEnemy(isEnemy);
				tc.getEnemies().add(t);
			}
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
	}
}
