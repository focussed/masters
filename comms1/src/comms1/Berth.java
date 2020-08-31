package comms1;

public class Berth {
	private String name;
	private boolean inUse = false;
	private ShipMode mode;
	private int maxLength;
	private int maxDraught;
	private Ship ship;			// ship in berth

	public Berth(String name, boolean inUse, ShipMode mode, int maxLength, int maxDraught) {
		super();
		this.name = name;
		this.inUse = inUse;
		this.mode = mode;
		this.maxLength = maxLength;
		this.maxDraught = maxDraught;
		this.ship = null;
	}
	
	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isInUse() {
		return inUse;
	}
	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}
	public ShipMode getMode() {
		return mode;
	}
	public void setMode(ShipMode mode) {
		this.mode = mode;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public int getMaxDraught() {
		return maxDraught;
	}
	public void setMaxDraught(int maxDraught) {
		this.maxDraught = maxDraught;
	}
 
	@Override
	public String toString() {
		return "Berth [name=" + name + ", inUse=" + inUse + ", mode=" + mode + ", maxLength=" + maxLength
				+ ", maxDraught=" + maxDraught + "]";
	}
	
}
