import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {
	public void setPtDirection(Direction ptDirection) {
		this.ptDirection = ptDirection;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOldX() {
		return oldX;
	}

	public void setOldX(int oldX) {
		this.oldX = oldX;
	}

	public int getOldY() {
		return oldY;
	}

	public void setOldY(int oldY) {
		this.oldY = oldY;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public boolean isEnemy() {
		return isEnemy;
	}

	public void setEnemy(boolean isEnemy) {
		this.isEnemy = isEnemy;
	}

	public Direction getPtDirection() {
		return ptDirection;
	}

	private Random r = new Random();
	private static final int X_SPEED = 5;
	private static final int Y_SPEED = 5;
	private static final int WIDTH = 30;
	private static final int HEIGHT = 30;
	private Direction ptDirection = Direction.DOWN;
	private boolean isEnemy = false;
	private TankClient tc;
	private int step = 0;

	public Tank(TankClient tc, int x, int y, boolean isEnemy) {
		this(x, y, isEnemy);
		this.tc = tc;

	}

	private boolean isAlive = true;

	private int x = 30;
	private int y = 30;

	public Tank(int x, int y, boolean isEnemy) {
		this.x = x;
		this.y = y;
		this.isEnemy = isEnemy;
	}

	private Direction dir = Direction.STOP;
	private Direction oldDir = Direction.STOP;
	private int oldX;
	private int oldY;
	private boolean a = false;
	private boolean w = false;
	private boolean s = false;
	private boolean d = false;
	int id;

	public void draw(Graphics g) {
		if (!isAlive) {
			if(isEnemy){
				tc.getEnemies().remove(this);
			}
			return;
		}
		Color c = g.getColor();
		if (isEnemy)
			g.setColor(Color.BLUE);
		else
			g.setColor(Color.RED);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.drawString("id:" + id, x, y-10);
		g.setColor(c);
		move();
		switch (ptDirection) {
		case UP:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH
					/ 2, y);
			break;
		case DOWN:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH
					/ 2, y + Tank.HEIGHT);
			break;
		case DOWNLEFT:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y
					+ Tank.HEIGHT);
			break;
		case DOWNRIGHT:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH,
					y + Tank.HEIGHT);
			break;
		case UPRIGHT:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH,
					y);
			break;
		case UPLEFT:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y);
			break;
		case LEFT:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y
					+ Tank.HEIGHT / 2);
			break;
		case RIGHT:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH,
					y + Tank.HEIGHT / 2);
			break;
		default:
			break;
		}
//		if (isEnemy) {
//			if (step == 0) {
//				Direction[] directions = Direction.values();
//				step = r.nextInt(7) + 3;
//				int n = r.nextInt(directions.length);
//				dir = directions[n];
//			}
//			step--;
//			if (r.nextInt(40) > 38) {
//				this.fire(x, y);
//			}
//		}

	}

	public void stay() {
		x = oldX;
		y = oldY;
	}

	public boolean collideWithTanks(List<Tank> tanks) {
		for (Tank tank : tanks) {
			if (isAlive && tank.isAlive
					&& this.getRect().intersects(tank.getRect())) {
				if (this != tank) {
					this.stay();
					tank.stay();
					return true;
				}
			}
		}
		return false;
	}

	public void move() {
		oldX = x;
		oldY = y;
		switch (dir) {
		case UP:
			y -= Y_SPEED;
			break;
		case DOWN:
			y += Y_SPEED;
			break;
		case DOWNLEFT:
			y += Y_SPEED;
			x -= X_SPEED;
			break;
		case DOWNRIGHT:
			x += X_SPEED;
			y += Y_SPEED;
			break;
		case UPRIGHT:
			y -= Y_SPEED;
			x += X_SPEED;
			break;
		case UPLEFT:
			y -= Y_SPEED;
			x -= X_SPEED;
			break;
		case LEFT:
			x -= X_SPEED;
			break;
		case RIGHT:
			x += X_SPEED;
			break;
		default:
			break;
		}
		if (x - Tank.WIDTH / 2 < 0)
			x = Tank.WIDTH / 2;
		if (y - Tank.HEIGHT / 2 < 30)
			y = 30 + Tank.HEIGHT / 2;
		if (y + Tank.HEIGHT > TankClient.HEIGHT)
			y = TankClient.HEIGHT - Tank.HEIGHT;
		if (x + Tank.WIDTH > TankClient.WIDTH)
			x = TankClient.WIDTH - Tank.WIDTH;
		if (dir != Direction.STOP) {
			ptDirection = dir;
		}
	}

	public Tank(TankClient tc, int x, int y, Direction dir, int id) {
		this.tc = tc;
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.id = id;
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_D:
			d = true;
			break;
		case KeyEvent.VK_A:
			a = true;
			break;
		case KeyEvent.VK_W:
			w = true;
			break;
		case KeyEvent.VK_S:
			s = true;
			break;
		}
		setLocation();
	}

	private Missile fire(int x, int y) {
		// TODO Auto-generated method stub
		if(!isAlive) return null;
		int x1 = x + WIDTH / 2 - Missile.WIDTH / 2;
		int y1 = y + HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile missile = new Missile(id, x1, y1, tc, ptDirection, isEnemy);
		tc.getMissiles().add(missile);
		
		MissileNewMsg msg = new MissileNewMsg(missile);
		tc.nc.send(msg);
		return missile;
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_CONTROL:
			fire(x, y);
			break;
		case KeyEvent.VK_D:
			d = false;
			break;
		case KeyEvent.VK_A:
			a = false;
			break;
		case KeyEvent.VK_W:
			w = false;
			break;
		case KeyEvent.VK_S:
			s = false;
			break;
		}
		setLocation();
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public void setLocation() {
		oldDir = this.dir;
		if (d && !a && !w && !s)
			dir = Direction.RIGHT;
		if (!d && a && !w && !s)
			dir = Direction.LEFT;
		if (!d && !a && w && !s)
			dir = Direction.UP;
		if (!d && !a && !w && s)
			dir = Direction.DOWN;
		if (d && !a && w && !s)
			dir = Direction.UPRIGHT;
		if (!d && a && w && !s)
			dir = Direction.UPLEFT;
		if (d && !a && !w && s)
			dir = Direction.DOWNRIGHT;
		if (!d && a && !w && s)
			dir = Direction.DOWNLEFT;
		if (!d && !a && !w && !s)
			dir = Direction.STOP;
		if(oldDir!=dir){
			Msg moveMsg = new TankMoveMsg(id, x, y, dir,ptDirection);
			tc.nc.send(moveMsg);
		}
	}
}
