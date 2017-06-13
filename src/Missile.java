import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;



public class Missile {

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTankId() {
		return tankId;
	}

	public void setTankId(int tankId) {
		this.tankId = tankId;
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

	public boolean isEnemy() {
		return isEnemy;
	}

	public void setEnemy(boolean isEnemy) {
		this.isEnemy = isEnemy;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	private int tankId;
	private int x;
	private int y;
	private TankClient tc;
	private boolean isAlive = true;
	private boolean isEnemy;
	private int id;
	private static int ID =1;
	public Missile(int tankId,int x, int y, TankClient tc, Direction dir,boolean isEnemy) {
		this(tankId, x, y, dir);
		this.tc = tc;
		this.isEnemy = isEnemy;
		id = ID++;
	}

	public static final int HEIGHT= 10;
	public static final int WIDTH= 10;
	private static final int XSPEED = 10;
	private static final int YSPEED = 10;
	Direction dir;
	public Missile(int tankId, int x, int y, Direction dir) {
		this.tankId = tankId;
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	public void draw(Graphics g){
		if(!isAlive){
			tc.getMissiles().remove(this);
			return;
		}
		Color c = g.getColor();
		if(isEnemy) g.setColor(Color.BLACK);
		else g.setColor(Color.RED);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		move(dir);
	}
	
	public boolean hitEnemy(Tank t){
		if(isAlive&&t.getRect().intersects(getRect())&&t.isAlive()&&(this.isEnemy!=t.isEnemy())){// Remember That The Enemy Should be Alive to be Hitted
			isAlive = false;
			t.setAlive(false);
			Explosion explosion = new Explosion(x, y, tc);
			tc.getExplosions().add(explosion);
			return true;
		}
		return false;
	}
	
	private void move(Direction dir){
		switch(dir){
			case UP:
				y-=YSPEED;
				break;
			case DOWN:
				y+=YSPEED;
				break;
			case LEFT:
				x-=XSPEED;
				break;
			case RIGHT:
				x+=XSPEED;
				break;
			case UPLEFT:
				y-=YSPEED;
				x-=XSPEED;
				break;
			case UPRIGHT:
				y-=YSPEED;
				x+= XSPEED;
				break;
			case DOWNLEFT:
				y+=YSPEED;
				x-=XSPEED;
				break;
			case DOWNRIGHT:
				x+=XSPEED;
				y+=YSPEED;
				break;
			default:
				break;
		}
		
		if(x<0||y<0||x>TankClient.WIDTH||y>TankClient.HEIGHT){
			isAlive = false;
		}
	}

	public boolean hitEnemies(List<Tank> enemies) {
		// TODO Auto-generated method stub
		for(int i =0;i<enemies.size();i++){
			Tank t = enemies.get(i);
			if(this.hitEnemy(t)) return true;
		}
		return false;
	}
}
