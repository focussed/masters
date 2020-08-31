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

public class CruiserTests {
	
	@Test
	public void OneCruiserBerthandDeberth() {
		System.out.println("Test: OneCruiserBerthandDeberth");

		Port port = new Port("Cork", true);
		Ship ship1 = new Ship(false, "Sea Princess", 320000, 12000, ShipMode.CRUISE, 500000);
		
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
		assertEquals(Message.PILOT_READY_TO_BOARD, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship1.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship1.transmit(Message.BERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.BERTHED, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship1.transmit(Message.DEBERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 deberthed");
		
		ship1.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 navigating port to leave");
		
		ship1.transmit(Message.LEFT_PORT, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 left port");
	
		ship1.transmit(Message.UNREGISTER, "");
		assertEquals(RegisterState.UNREGISTERED, ship1.getRegisterState(), "Ship 1 is deregistered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 gone");
		

		System.out.println(ship1.toString());
		
		port = null;
		ship1 = null;
		
		System.out.println("===========================================================================================");
		
	}
		
	@Test
	public void Ship1BerthShip2MustWait() {
		System.out.println("Test: Ship1BerthShip2MustWait");

		Port port = new Port("Cork", true);
		Ship ship1 = new Ship(false, "Sea Princess", 320000, 12000, ShipMode.CRUISE, 500000);
		Ship ship2 = new Ship(false, "Titanic", 320000, 12000, ShipMode.CRUISE, 500000);
		
		System.out.println(port.toString());
		System.out.println(ship1.toString());
		System.out.println(ship2.toString());
		
    	System.out.println("Port listening...");
			
		ship1.setPortToCall(port);
		ship2.setPortToCall(port);
		
		ship1.setRxMsg(null);		// clear message
		ship1.setRxText(null);		// clear text
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship1.getRxMsg(), "Ship 1 reg ok");
		
		ship2.setRxMsg(null);		// clear message
		ship2.setRxText(null);		// clear text
		ship2.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship2.getRxMsg(), "Ship 2 reg ok");
		
		
		ship1.setRxMsg(null);		// clear message
		ship1.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_PILOT_PICKUP_AREA, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.ENROUTE_PILOT_PICKUP_AREA, ship1.getShipsState(), "Ship 1 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");

		
		ship2.setRxMsg(null);		// clear message
		ship2.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_ANCHORAGE_AREA, ship2.getRxMsg(),  "Ship 2 told to anchor");	
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.ENROUTE_ANCHORAGE, ship2.getShipsState(), "Ship 2 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship2.getShipsDirection(), "Ship 2 direction - approaching");
		
		ship2.setRxMsg(null);		// clear message
		ship2.transmit(Message.ANCHORED, "");
		assertEquals(Message.ACK, ship2.getRxMsg(),  "Ship 2 told to anchor");	
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.ANCHORED, ship2.getShipsState(), "Ship 2 is anchored");
		assertEquals(ShipDirection.APPROACHING, ship2.getShipsDirection(), "Ship 2 direction - approaching");
				
		
		ship1.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.PILOT_READY_TO_BOARD, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship1.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship1.transmit(Message.BERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.BERTHED, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		// Ship 1 leaves first
		
		ship1.transmit(Message.DEBERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 deberthed");
		
		ship1.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 navigating port to leave");
		
		ship1.transmit(Message.LEFT_PORT, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 left port");
	
		ship1.transmit(Message.UNREGISTER, "");
		assertEquals(RegisterState.UNREGISTERED, ship1.getRegisterState(), "Ship 1 is deregistered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 gone");
		
		// Ship 2 leaves second
		
		ship2.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.NAVIGATING, ship2.getShipsState(), "Ship 2 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship2.getShipsDirection(), "Ship 2 direction - approaching");
		assertEquals(Message.PILOT_READY_TO_BOARD, ship2.getRxMsg(),  "Ship 2 ok to Berth");
		
		ship2.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship2.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship2.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship2.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship2.transmit(Message.BERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.BERTHED, ship2.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship2.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship2.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship2.transmit(Message.DEBERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship2.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship2.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship2.getRxMsg(),  "Ship 1 deberthed");
		
		ship2.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship2.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship2.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship2.getRxMsg(),  "Ship 1 navigating port to leave");
		
		ship2.transmit(Message.LEFT_PORT, "");
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship2.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship2.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship2.getRxMsg(),  "Ship 1 left port");
	
		ship2.transmit(Message.UNREGISTER, "");
		assertEquals(RegisterState.UNREGISTERED, ship2.getRegisterState(), "Ship 1 is deregistered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship2.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship2.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship2.getRxMsg(),  "Ship 1 gone");
		

		
		System.out.println(ship1.toString());
		
		port = null;
		ship1 = null;
		ship2 = null;
		
		System.out.println("===========================================================================================");
		
	}
	
	@Test
	public void Ship1BerthShip2AttemptsToCircumvent() {
		System.out.println("Test: Full cycle LeavePort2Ships");

		Port port = new Port("Cork", true);
		Ship ship1 = new Ship(false, "Sea Princess", 320000, 12000, ShipMode.CRUISE, 500000);
		Ship ship2 = new Ship(false, "Titanic", 320000, 12000, ShipMode.CRUISE, 500000);
		
		System.out.println(port.toString());
		System.out.println(ship1.toString());
		System.out.println(ship2.toString());
		
    	System.out.println("Port listening...");
			
		ship1.setPortToCall(port);
		ship2.setPortToCall(port);
		
		ship1.setRxMsg(null);		// clear message
		ship1.setRxText(null);		// clear text
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship1.getRxMsg(), "Ship 1 reg ok");
		
		ship2.setRxMsg(null);		// clear message
		ship2.setRxText(null);		// clear text
		ship2.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship2.getRxMsg(), "Ship 2 reg ok");
		
		
		ship1.setRxMsg(null);		// clear message
		ship1.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_PILOT_PICKUP_AREA, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.ENROUTE_PILOT_PICKUP_AREA, ship1.getShipsState(), "Ship 1 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");

		
		ship2.setRxMsg(null);		// clear message
		ship2.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_ANCHORAGE_AREA, ship2.getRxMsg(),  "Ship 2 told to anchor");	
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.ENROUTE_ANCHORAGE, ship2.getShipsState(), "Ship 2 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship2.getShipsDirection(), "Ship 2 direction - approaching");
		
		ship2.setRxMsg(null);		// clear message
		ship2.transmit(Message.ANCHORED, "");
		assertEquals(Message.ACK, ship2.getRxMsg(),  "Ship 2 told to anchor");	
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.ANCHORED, ship2.getShipsState(), "Ship 2 is anchored");
		assertEquals(ShipDirection.APPROACHING, ship2.getShipsDirection(), "Ship 2 direction - approaching");
				
		
		ship1.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.PILOT_READY_TO_BOARD, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship1.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship1.transmit(Message.BERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.BERTHED, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		/////////
	
		ship2.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.ANCHORED, ship2.getShipsState(), "Ship 2 is anchored");
		assertEquals(ShipDirection.APPROACHING, ship2.getShipsDirection(), "Ship 2 direction - approaching");
		assertEquals(Message.NACK, ship2.getRxMsg(),  "Ship 1 is not ok to move here");
		
		// Ship 1 leaves first
		
		ship1.transmit(Message.DEBERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 deberthed");
		
		ship1.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 navigating port to leave");
		
		ship1.transmit(Message.LEFT_PORT, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 left port");
	
		ship1.transmit(Message.UNREGISTER, "");
		assertEquals(RegisterState.UNREGISTERED, ship1.getRegisterState(), "Ship 1 is deregistered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 gone");
		
		// Ship 2 leaves second
		
		ship2.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.NAVIGATING, ship2.getShipsState(), "Ship 2 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship2.getShipsDirection(), "Ship 2 direction - approaching");
		assertEquals(Message.PILOT_READY_TO_BOARD, ship2.getRxMsg(),  "Ship 2 ok to Berth");
		
		ship2.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship2.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship2.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship2.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship2.transmit(Message.BERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.BERTHED, ship2.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship2.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship2.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship2.transmit(Message.DEBERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship2.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship2.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship2.getRxMsg(),  "Ship 1 deberthed");
		
		ship2.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship2.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship2.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship2.getRxMsg(),  "Ship 1 navigating port to leave");
		
		ship2.transmit(Message.LEFT_PORT, "");
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship2.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship2.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship2.getRxMsg(),  "Ship 1 left port");
	
		ship2.transmit(Message.UNREGISTER, "");
		assertEquals(RegisterState.UNREGISTERED, ship2.getRegisterState(), "Ship 1 is deregistered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship2.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship2.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship2.getRxMsg(),  "Ship 1 gone");
		

		
		System.out.println(ship1.toString());
		
		port = null;
		ship1 = null;
		ship2 = null;
		
		System.out.println("===========================================================================================");
		
	}
}
