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

public class RogueCommands {
		
	@Test
	public void BerthandDeberth() {
		System.out.println("Test: BerthandDeberth");

		Port port = new Port("Cork", true);
		Ship ship1 = new Ship(false, "HMS Pinafore", 120000, 12000, ShipMode.DRY_BULK, 100000);
		
		System.out.println(port.toString());
		System.out.println(ship1.toString());
		
    	System.out.println("Port listening...");
			
		ship1.setPortToCall(port);
		
		ship1.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.UNREGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.NACK, ship1.getRxMsg(),  "Ship 1 ok to Berth");
	
		ship1.setRxMsg(null);		// clear message
		ship1.setRxText(null);		// clear text
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship1.getRxMsg(), "Ship 1 reg ok");

		// out of sequence messages - did not request to berth
		
		ship1.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.NACK, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship1.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.NACK, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship1.transmit(Message.BERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.NACK, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship1.transmit(Message.DEBERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.NACK, ship1.getRxMsg(),  "Ship 1 deberthed");
		
		ship1.transmit(Message.ANCHORED, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.NACK, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship1.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.NACK, ship1.getRxMsg(),  "Ship 1 navigating port to leave");
		
		ship1.transmit(Message.LEFT_PORT, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.NACK, ship1.getRxMsg(),  "Ship 1 left port");
	
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
}
