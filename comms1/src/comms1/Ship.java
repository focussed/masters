package comms1;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Ship { 

	public Ship(Boolean logstate, String name, int length, int draught, ShipMode mode, int tonnage) {
		super();
		this.name = name;
		this.length = length;
		this.draught = draught;
		this.mode = mode;
		this.tonnage = tonnage;
		log.setLevel(Level.FINE);
		this.setLogging(logstate);

	}

	@Override
	public String toString() {
		return "Ship [name=" + name + ", portToCall=" + portToCall + ", logging=" + logging + ", registerState="
				+ registerState + ", shipsState=" + shipsState + ", shipsDirection=" + shipsDirection + ", length="
				+ length + ", draught=" + draught + ", mode=" + mode + ", tonnage=" + tonnage + "]";
	}

	private Logger log = Logger.getLogger(Port.class.getName());
	private String name;
	private Port portToCall = null;
	private boolean logging = false;
	private RegisterState registerState = RegisterState.UNREGISTERED;
	private ShipState shipsState = ShipState.OUTSIDE_PORT_LIMITS;
	private ShipDirection shipsDirection = ShipDirection.DONTCARE;
	private int length = 0;
	private int draught = 0;
	private ShipMode mode = null;
	private int tonnage = 0;
	private Message rxMsg;
	private String rxText;

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getDraught() {
		return draught;
	}

	public void setDraught(int draught) {
		this.draught = draught;
	}

	public ShipMode getMode() {
		return mode;
	}

	public void setMode(ShipMode mode) {
		this.mode = mode;
	}

	public int getTonnage() {
		return tonnage;
	}

	public void setTonnage(int tonnage) {
		this.tonnage = tonnage;
	}

	public RegisterState getRegisterState() {
		return registerState;
	}

	public void setRegisterState(RegisterState registerState) {
		this.registerState = registerState;
	}

	public ShipState getShipsState() {
		return shipsState;
	}

	public void setShipsState(ShipState shipsState) {
		this.shipsState = shipsState;
	}

	public ShipDirection getShipsDirection() {
		return shipsDirection;
	}

	public void setShipsDirection(ShipDirection shipsDirection) {
		this.shipsDirection = shipsDirection;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Port getPortToCall() {
		return portToCall;
	}

	public void setPortToCall(Port portToCall) {
		this.portToCall = portToCall;
	}

	public void transmit(Message msg, String text) {
		log.config("Ship Transmits: " + msg + ", text=[" + text + "]");
		this.getPortToCall().receive((Ship) this, msg, text);
	}

	public Message receive(Message msg, String text) {
		log.config("Ship received: " + msg + ", text=[" + text + "]");
		rxMsg = msg;
		rxText = text;

		switch (msg) {
		case PROCEED_TO_PILOT_PICKUP_AREA:
			// acknowledge the message from the port
			this.transmit(Message.ACK, "Proceeding to pilot pickup area");
			break;

		case PROCEED_TO_ANCHORAGE_AREA:
			// acknowledge the message from the port
			this.transmit(Message.ACK, "Proceeding to anchorage area");
			break;
			
		case PILOT_READY_TO_BOARD:
			// acknowledge the message from the port
			this.transmit(Message.ACK, "Pilot on board");
			break;
		}
		return (msg);
	}

	public Message getRxMsg() {
		return rxMsg;
	}

	public void setRxMsg(Message rxMsg) {
		this.rxMsg = rxMsg;
	}

	public String getRxText() {
		return rxText;
	}

	public void setRxText(String rxText) {
		this.rxText = rxText;
	}

	public boolean isLogging() {
		return logging;
	}

	public void setLogging(boolean logging) {
		this.logging = logging;
	}

}
