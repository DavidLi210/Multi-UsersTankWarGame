import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;


public class TankMoveMsg implements Msg {
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	private static final int type = Msg.TankMOVEMSG;
	private int id;
	private int x;
	private int y ;
	private Direction dir;
	private TankClient tc;
	private Direction ptDir;
	public TankMoveMsg(TankClient tc) {// Constructor with tc as input parameter is for send
		this.tc = tc;
	}
	
	public TankMoveMsg(int id, int x, int y, Direction dir,Direction ptDir) {// Constructor without tc as input parameter is for send
		this.id = id;
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.ptDir = ptDir;
	}

	@Override
	public void send(DatagramSocket ds, String ip, int port) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(Msg.TankMOVEMSG);
			dos.writeInt(id);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(dir.ordinal());
			dos.writeInt(ptDir.ordinal());
			System.out.println(" One TankMoveMsg Sended");
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
			boolean exist = false;
			for(int i =0;i<tc.getEnemies().size();i++){
				if(tc.getEnemies().get(i).getId()==id){
					int x = dis.readInt();
					int y = dis.readInt();
					Direction dir = Direction.values()[dis.readInt()];
					Direction ptDir = Direction.values()[dis.readInt()];
					Tank t = tc.getEnemies().get(i);
					t.setX(x);
					t.setY(y);
					t.setDir(dir);
					t.setPtDirection(ptDir);
					
					exist = true;
					break;
				}
			}

		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
	}

}
