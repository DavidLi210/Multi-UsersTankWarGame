import java.awt.Color;
import java.awt.Graphics;


public class Explosion {
	public boolean isAlive() {
		return isAlive;
	}
	private int x;
	private int y;
	private int [] diameters = new int[]{5,10,23,28,33,38,40,32,27,23,10,2};
	private boolean isAlive = true;
	private TankClient tc;
	private int step = 0;
	public Explosion(int x, int y,TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!isAlive){
			tc.getExplosions().remove(this);
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.YELLOW);
		g.fillOval(x, y, diameters[step], diameters[step]);
		g.setColor(c);
		
		step++; // Changed!!!!!!!!!!!!
		if(step==diameters.length){
			//step = 0;
			isAlive = false;
		}
	}
}
