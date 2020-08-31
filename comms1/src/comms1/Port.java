package comms1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Port extends Thread {
	private static final int MIN_SHIP_LENGTH_FOR_REGISTER = 50000;
	private static final int MAX_SHIP_LENGTH = 400000;
	private static final int MAX_PORT_DEPTH = 13500;

	public Port(String name, boolean logstate) {
		super();
		this.name = name;
		this.logging = logstate;
		log.setLevel(Level.CONFIG);
		System.out.println("Loggin is set to " + log.getLevel() + " logging is " + log.toString());
		loadBerths(); // setup the berths for this port
	}

	private Logger log = Logger.getLogger(Port.class.getName());
	private String name;
	private Ship shipToCall; // used in transmit
	private boolean logging = false;
	private boolean rxedMsg = false; // message received from ship
	private ArrayList<Ship> shiplist = new ArrayList<Ship>();
	private ArrayList<Berth> berthlist = new ArrayList<Berth>();
	private Ship currentShip;
	private Message currentMessage;
	private String currentText;

	/*
	 * Load up the available berths and their limits for the port
	 */
	private void loadBerths() {
		Berth berth1 = new Berth("Whitegate", false, ShipMode.LIQUID_BULK, 250000, 13250);
		Berth berth2 = new Berth("Tivoli A", false, ShipMode.DRY_BULK, 130000, 12000);
		Berth berth3 = new Berth("Tivoli B", false, ShipMode.LIFTON_LIFTOFF, 130000, 12000);
		Berth berth4 = new Berth("Ringaskiddy A", false, ShipMode.DRY_BULK, 170000, 13250);
		Berth berth5 = new Berth("Ringaskiddy B", false, ShipMode.LIFTON_LIFTOFF, 170000, 13250);
		Berth berth6 = new Berth("Ringaskiddy C", false, ShipMode.ROLLON_ROLLOFF, 170000, 13250);
		Berth berth7 = new Berth("Cobh", false, ShipMode.CRUISE, 370000, 13250);
		berthlist.add(berth1);
		berthlist.add(berth2);
		berthlist.add(berth3);
		berthlist.add(berth4);
		berthlist.add(berth5);
		berthlist.add(berth6);
		berthlist.add(berth7);
	}

	/*
	 * Check if a suitable berth is free and allocate to the ship if so
	 * 
	 * Returns the berth allocated or null
	 */
	private Berth testAndSetBerthAvailable(Ship ship) {
		;
		Berth availableBerth = null;

		int i = 0;
		while (i < berthlist.size()) {
			Berth b = berthlist.get(i);
			if ((b.isInUse() == false) && (b.getMode() == ship.getMode()) && (b.getMaxLength() >= ship.getDraught())
					&& (b.getMaxLength() >= ship.getLength())) {
				availableBerth = b;
				b.setInUse(true); // berth now in use
				b.setShip(ship); // mark ship for berth
				log.config("Ship " + ship.getName() + " allocated berth in " + b.getName());
				// System.out.println("Ship " + ship.getName() + " allocated berth in " +
				// b.getName());
				break;
			}
			i++;
		}

		return availableBerth;
	}

	/*
	 * deallocate berth in use by ship
	 */
	private void deallocateBerth(Ship ship) {
		boolean found = false;
		int i = 0;

		while (i < berthlist.size()) {
			Berth b = berthlist.get(i);
			if (b.getShip() == ship) {
				b.setInUse(false);
				b.setShip(null);
				log.config("Ship " + ship.getName() + " deallocated berth in " + b.getName());
				found = true;
				// System.out.println("Ship " + ship.getName() + " deallocated berth in " +
				// b.getName());
				break;
			}
			i++;
		}
		if (found == false) { // issue warning
			log.config("Ship " + ship.getName() + " not found in any berth");
		}
	}

	public Ship getCurrentShip() {
		return currentShip;
	}

	public void setCurrentShip(Ship currentShip) {
		this.currentShip = currentShip;
	}

	public Message getCurrentMessage() {
		return currentMessage;
	}

	public void setCurrentMessage(Message currentMessage) {
		this.currentMessage = currentMessage;
	}

	public String getCurrentText() {
		return currentText;
	}

	public void setCurrentText(String currentText) {
		this.currentText = currentText;
	}

	public String getPortName() {
		return name;
	}

	public void setPortName(String name) {
		this.name = name;
	}

	public Ship getShipToCall() {
		return shipToCall;
	}

	public void setShipToCall(Ship shipToCall) {
		this.shipToCall = shipToCall;
	}

	@Override
	public String toString() {
		return "Port [name=" + name + ", shipToCall=" + shipToCall + ", logging=" + logging + "]";
	}

	public boolean isLogging() {
		return logging;
	}

	public void setLogging(boolean logging) {
		this.logging = logging;
	}

	/*
	 * Receive a message from a ship
	 */
	public void receive(Ship ship, Message msg, String text) {
		LogPortMsg(MsgDirection.RX, ship, msg, text);
		this.currentMessage = msg;
		this.currentShip = ship;
		this.currentText = text;

		/*
		 * The port has received a message from a ship. Respond to the message based on
		 * the ship's state
		 */
		if (shiplist.contains(this.currentShip) == false) {
			shiplist.add(this.currentShip);
		}
		log.config("Received " + this.currentMessage + " from " + currentShip.getName());
		/*
		 * Dont allow any communication if this is not a register request
		 */
		if ((currentShip.getRegisterState() == RegisterState.UNREGISTERED)
				&& (!(this.currentMessage == Message.REGISTER_REQUEST || this.currentMessage == Message.UNREGISTER))) {
			this.transmit(currentShip, Message.NACK, "Not registered, cannot enter port. Please register");
			// consider removing this to prevent DoS attacks

		} else {
			switch (this.currentMessage) {
			case REGISTER_REQUEST:
				if (currentShip.getRegisterState() == RegisterState.UNREGISTERED) {
					if (currentShip.getDraught() > MAX_PORT_DEPTH) {
						this.transmit(currentShip, Message.NACK, "Ship draught too deep for port");
					} else if (currentShip.getLength() > MAX_SHIP_LENGTH) {
						this.transmit(currentShip, Message.NACK, "Ship length too long for port");
					} else if (currentShip.getLength() < MIN_SHIP_LENGTH_FOR_REGISTER) {
						this.transmit(currentShip, Message.NACK, "Ship does not need to register due to length");
					} else {
						currentShip.setRegisterState(RegisterState.REGISTERED);
						this.transmit(currentShip, Message.ACK, "");
					}
				} else if (currentShip.getRegisterState() == RegisterState.REGISTERED) {
					this.transmit(currentShip, Message.ACK, "Already registered");
				}
				// reset various states
				currentShip.setShipsDirection(ShipDirection.DONTCARE);
				currentShip.setShipsState(ShipState.OUTSIDE_PORT_LIMITS);
				break;
			case UNREGISTER:
				if (currentShip.getRegisterState() == RegisterState.REGISTERED) {
					currentShip.setRegisterState(RegisterState.UNREGISTERED);
					currentShip.setShipsDirection(ShipDirection.DONTCARE);
					currentShip.setShipsState(ShipState.OUTSIDE_PORT_LIMITS);
					this.transmit(currentShip, Message.ACK, "");
					shiplist.remove(currentShip);
				}
				// do not respond if unregistered, could allow for DoS attack
				break;
			case REQUEST_TO_BERTH:
				// ships direction will be set to approaching
				Berth berth = null;
				if (SeaConditions.AreSeaConditionsOK() == false) {
					currentShip.setShipsDirection(ShipDirection.APPROACHING);
					currentShip.setShipsState(ShipState.OUTSIDE_PORT_LIMITS);
					this.transmit(currentShip, Message.PROCEED_TO_ANCHORAGE_AREA,
							"Conditions not ok.  Anchor and await further instructions");
				} else if ((berth = testAndSetBerthAvailable(currentShip)) == null) {
					currentShip.setShipsDirection(ShipDirection.APPROACHING);
					currentShip.setShipsState(ShipState.ENROUTE_ANCHORAGE);
					this.transmit(currentShip, Message.PROCEED_TO_ANCHORAGE_AREA,
							"Berth not available.  Anchor and await further instructions");
				} else {
					currentShip.setShipsDirection(ShipDirection.APPROACHING);
					currentShip.setShipsState(ShipState.ENROUTE_PILOT_PICKUP_AREA);
					this.transmit(currentShip, Message.PROCEED_TO_PILOT_PICKUP_AREA,
							"Allocated berth is " + berth.getName());
				}
				break;
			case AT_PILOT_PICKUP_AREA:
				if (currentShip.getShipsState() == ShipState.ENROUTE_PILOT_PICKUP_AREA) {
					currentShip.setShipsState(ShipState.NAVIGATING);
					this.transmit(currentShip, Message.PILOT_READY_TO_BOARD, "");
				} else {
					this.transmit(currentShip, Message.NACK, "This is not allowed.  Your ship is should be "
							+ currentShip.getShipsState() + ".  Wait for further instructions");
				}
				break;
			case NAVIGATING_UNDER_PILOT:
				if ((currentShip.getShipsState() == ShipState.AT_PILOT_PICKUP_AREA)
						|| (currentShip.getShipsState() == ShipState.NAVIGATING)) {
					this.transmit(currentShip, Message.ACK, ""); // acknowledge
					currentShip.setShipsState(ShipState.NAVIGATING);
				} else {
					this.transmit(currentShip, Message.NACK, "This is not allowed.  Your ship is should be "
							+ currentShip.getShipsState() + ".  Wait for further instructions");
				}
				break;
			case BERTHED:
				if (currentShip.getShipsState() == ShipState.NAVIGATING) {
					currentShip.setShipsState(ShipState.BERTHED);
					currentShip.setShipsDirection(ShipDirection.APPROACHING);
					this.transmit(currentShip, Message.ACK, ""); // acknowledge
				} else {
					this.transmit(currentShip, Message.NACK, "This op is not allowed.  Your ship is should be "
							+ currentShip.getShipsState() + ".  Wait for further instructions");
				}
				break;
			case DEBERTHED:
				if (currentShip.getShipsState() == ShipState.BERTHED) {
					currentShip.setShipsState(ShipState.NAVIGATING);
					currentShip.setShipsDirection(ShipDirection.LEAVING);
					deallocateBerth(currentShip);
					this.transmit(currentShip, Message.ACK, ""); // acknowledge
					// check for ships at anchor which might suit this now free berth
					if (SeaConditions.AreSeaConditionsOK() == true) {
						Berth b = null;
						int i = 0;
						while (i < shiplist.size()) {
							Ship s = shiplist.get(i);
							if ((s.getShipsState() == ShipState.ANCHORED)
									|| (s.getShipsState() == ShipState.ENROUTE_ANCHORAGE)) {

								if ((b = testAndSetBerthAvailable(s)) != null) {
									s.setShipsDirection(ShipDirection.APPROACHING);
									s.setShipsState(ShipState.ENROUTE_PILOT_PICKUP_AREA);
									this.transmit(s, Message.PROCEED_TO_PILOT_PICKUP_AREA,
											"Allocated berth is " + b.getName());
								}
								// log.config("Ship " + s.getName() + " allocated berth in " + b.getName());
								// System.out.println("Ship " + ship.getName() + " allocated berth in " +
								// b.getName());

							}
							i++;
						}
					}
				} else {
					this.transmit(currentShip, Message.NACK, "This op is not allowed.  Your ship is should be "
							+ currentShip.getShipsState() + ".  Wait for further instructions");
				}
				break;
			case ANCHORED:
				if (currentShip.getShipsState() == ShipState.ENROUTE_ANCHORAGE) {
					currentShip.setShipsState(ShipState.ANCHORED);
					this.transmit(currentShip, Message.ACK, ""); // acknowledge
				} else {
					this.transmit(currentShip, Message.NACK, "This op is not allowed.  Your ship is should be "
							+ currentShip.getShipsState() + ".  Wait for further instructions");
				}
				break;
			case LEFT_PORT:
				if (currentShip.getShipsState() == ShipState.NAVIGATING) {
					currentShip.setShipsState(ShipState.OUTSIDE_PORT_LIMITS);
					currentShip.setShipsDirection(ShipDirection.DONTCARE);
					this.transmit(currentShip, Message.ACK, ""); // acknowledge
				} else {
					this.transmit(currentShip, Message.NACK, "This op is not allowed.  Your ship is should be "
							+ currentShip.getShipsState() + ".  Wait for further instructions");
				}
				break;
			case ACK:
				if (text.equals("Proceeding to pilot pickup area")) {
					currentShip.setShipsState(ShipState.ENROUTE_PILOT_PICKUP_AREA);
					currentShip.setShipsDirection(ShipDirection.APPROACHING);
				} else if (text.equals("Proceeding to anchorage area")) {
					currentShip.setShipsState(ShipState.ENROUTE_ANCHORAGE);
					currentShip.setShipsDirection(ShipDirection.APPROACHING);
				} else if (text.equals("Pilot on board")) {
					currentShip.setShipsState(ShipState.NAVIGATING);
					currentShip.setShipsDirection(ShipDirection.APPROACHING);
				} else {
					// ignore message
				}
				break;
			case NACK:
				System.out.println("NACK received from ship: " + currentShip.getName() + 
						", Message text:[" + text + "]");
				break;
			case SIGNAL_FOR_ATTENTION:
				System.out.println("Signal received from ship: " + currentShip.getName() + 
						", Message text:[" + text + "]");
				this.transmit(currentShip, Message.ACK, ""); // acknowledge
				break;
			default:
				break;
			}
		}
		// System.out.println("Ship: " + currentShip.getName() + ":
		// RegState="+currentShip.getRegisterState() +
		// ", ShipState=" + currentShip.getShipsState() +
		// ", ShipDirection=" + currentShip.getShipsDirection());
	}

	/*
	 * Send message to a ship
	 */
	public void transmit(Ship ship, Message msg, String text) {
		LogPortMsg(MsgDirection.TX, ship, msg, text);
		ship.receive(msg, text);
	}

	private void LogPortMsg(MsgDirection dir, Ship ship, Message msg, String text) {
		if (this.isLogging()) {
			// SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd
			// HH:mm:ss.SSS");
			SimpleDateFormat date_format = new SimpleDateFormat("HH:mm:ss.SSS");
			Date resultdate = new Date();
			String dd = date_format.format(resultdate);
			String s1 = null;
			if (dir == MsgDirection.TX) {
				// sending a message from the port
				s1 = String.format("%s:Port %-20s to %-20s:%-30s :Info=[%s]", dd, this.name, ship.getName(), msg, text);
				System.err.println(s1);
			} else {
				// Received a message - print in different colour
				s1 = String.format("%s:Ship %-20s to %-20s:%-30s :Info=[%s]", dd, ship.getName(), this.name, msg, text);
				System.out.println(s1);
			}

		}
	}

	public static class SeaConditions {

		private static boolean tideHeightOK = true;
		private static boolean waveHeightOK = true;
		private static boolean weatherConditionsOK = true;
		private static boolean currentConditionsOK = true;

		public static boolean isTideHeightOK() {
			return tideHeightOK;
		}

		public static void setTideHeightOK(boolean tideHeightOK) {
			SeaConditions.tideHeightOK = tideHeightOK;
		}

		public static boolean isWaveHeightOK() {
			return waveHeightOK;
		}

		public static void setWaveHeightOK(boolean waveHeightOK) {
			SeaConditions.waveHeightOK = waveHeightOK;
		}

		public static boolean isWeatherConditionsOK() {
			return weatherConditionsOK;
		}

		public static void setWeatherConditionsOK(boolean weatherConditionsOK) {
			SeaConditions.weatherConditionsOK = weatherConditionsOK;
		}

		public static boolean isCurrentConditionsOK() {
			return currentConditionsOK;
		}

		public static void setCurrentConditionsOK(boolean currentConditionsOK) {
			SeaConditions.currentConditionsOK = currentConditionsOK;
		}

		public static boolean AreSeaConditionsOK() {
			boolean decision = false;
			if (tideHeightOK && waveHeightOK && weatherConditionsOK && currentConditionsOK) {
				// all are good
				decision = true;
			}
			return (decision);
		}
	}

}
