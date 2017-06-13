import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public interface Msg {
	public static final int TankNEWMSG = 1;
	public static final int TankMOVEMSG = 2;
	public static final int MissileNEWMSG = 3;
	public static final int TankDEADMSG = 4;
	public static final int MissileDEADMSG = 5;
	public static final int TankAlreadyExistMsg = 6;
	public void send(DatagramSocket ds,String ip,int port);
	public void parse(DataInputStream dis);
}
