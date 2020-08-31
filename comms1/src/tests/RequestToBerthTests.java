package tests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;
import comms1.Message;
import comms1.Port;
import comms1.Port.SeaConditions;
import comms1.RegisterState;
import comms1.Ship;
import comms1.ShipDirection;
import comms1.ShipMode;
import comms1.ShipState;

public class RequestToBerthTests {

	@Test
	public void RequestToBerthShip1() {
		System.out.println("Test: RequestToBerthShip1");

		Port port = new Port("Cork", true);
		Ship ship1 = new Ship(false, "HMS Pinafore", 120000, 12000, ShipMode.DRY_BULK, 100000);
		
		System.out.println(port.toString());
		System.out.println(ship1.toString());
		
    	System.out.println("Port listening...");
			
		ship1.setPortToCall(port);
		
		ship1.setRxMsg(null);		// clear message
		ship1.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.NACK, ship1.getRxMsg(),  "Ship 1 not registered");
		
		ship1.setRxMsg(null);		// clear message
		ship1.setRxText(null);		// clear text
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship1.getRxMsg(), "Ship 1 reg ok");
		
		ship1.setRxMsg(null);		// clear message
		ship1.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_PILOT_PICKUP_AREA, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.ENROUTE_PILOT_PICKUP_AREA, ship1.getShipsState(), "Ship 1 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		System.out.println(ship1.toString());
		
		port = null;
		ship1 = null;
		
		System.out.println("===========================================================================================");
		
	}
	

	@Test
	public void BerthShip1() {
		System.out.println("Test: BerthShip1");

		Port port = new Port("Cork", true);
		Ship ship1 = new Ship(false, "HMS Pinafore", 120000, 12000, ShipMode.DRY_BULK, 100000);
		
		System.out.println(port.toString());
		System.out.println(ship1.toString());
		
    	System.out.println("Port listening...");
			
		ship1.setPortToCall(port);
		
		ship1.setRxMsg(null);		// clear message
		ship1.setRxText(null);		// clear text
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship1.getRxMsg(), "Ship 1 reg ok");
		
		ship1.setRxMsg(null);		// clear message
		ship1.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_PILOT_PICKUP_AREA, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.ENROUTE_PILOT_PICKUP_AREA, ship1.getShipsState(), "Ship 1 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		
		ship1.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		
		ship1.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		
		ship1.transmit(Message.BERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.BERTHED, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		
		System.out.println(ship1.toString());
		
		port = null;
		ship1 = null;
		
		System.out.println("===========================================================================================");
		
	}
}
