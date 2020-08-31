package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import comms1.Message;
import comms1.Port;
import comms1.RegisterState;
import comms1.Ship;
import comms1.ShipDirection;
import comms1.ShipMode;
import comms1.ShipState;

public class RegisterTests {

	@Test
	public void registrationsOneShip() {
		System.out.println("Test: registrationsOneShip");

		Port port = new Port("Cork", true);
		Ship ship1 = new Ship(false, "HMS Pinafore", 120000, 12000, ShipMode.DRY_BULK, 100000);
		
		
		System.out.println(port.toString());
		System.out.println(ship1.toString());
		
    	System.out.println("Port listening...");
			
		ship1.setPortToCall(port);

		ship1.setRxMsg(null);		// clear message
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 reg ok");
		
		ship1.setRxMsg(null);		// clear message
		ship1.setRxText(null);		// clear text
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship1.getRxMsg(), "Ship 1 reg ok repeat");
		assertEquals("Already registered", ship1.getRxText(),  "Ship 1 reg ok text msg");
		
		ship1.setRxMsg(null);		// clear message
		ship1.transmit(Message.UNREGISTER, "");
		assertEquals( Message.ACK, ship1.getRxMsg(),"Ship 1 unreg");
		
		ship1.setRxMsg(null);		// no message expected
		ship1.transmit(Message.UNREGISTER, "");
		assertEquals(null, ship1.getRxMsg(), "Ship 1 unreg repeat - no response");
		
		ship1.setRxMsg(null);		// clear message
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals( Message.ACK, ship1.getRxMsg(), "Ship 1 reg ok after unregister");
		
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is outside port limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - dont care");
		System.out.println(ship1.toString());
		
		port = null;
		ship1 = null;
		
		System.out.println("===========================================================================================");
		
	}

	@Test
	public void registrationsTwoShips() {
		
    	System.out.println("Test: registrationsTwoShips");

		Port port = new Port("Cork", true);
		Ship ship1 = new Ship(false, "HMS Pinafore", 120000, 12000, ShipMode.DRY_BULK, 100000);
		Ship ship2 = new Ship(false, "The Bounty", 100000, 9000, ShipMode.DRY_BULK, 80000);
		
		
		System.out.println(port.toString());
		System.out.println(ship1.toString());
		System.out.println(ship2.toString());
			
		ship1.setPortToCall(port);
		ship2.setPortToCall(port);

		ship1.setRxMsg(null);		// clear message
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 1 reg ok");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is outside port limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - dont care");

		
		ship2.setRxMsg(null);		// clear message
		ship2.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship1.getRxMsg(),  "Ship 2 reg ok");
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship2.getShipsState(), "Ship 2 is outside port limits");
		assertEquals(ShipDirection.DONTCARE, ship2.getShipsDirection(), "Ship 2 direction - dont care");

		
		ship1.setRxMsg(null);		// clear message
		ship1.setRxText(null);		// clear text
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship1.getRxMsg(), "Ship 1 reg ok repeat");
		assertEquals("Already registered", ship1.getRxText(),  "Ship 1 reg ok text msg");
		
		ship2.setRxMsg(null);		// clear message
		ship2.transmit(Message.UNREGISTER, "");
		assertEquals( Message.ACK, ship2.getRxMsg(),"Ship 2 unreg");
		assertEquals(RegisterState.UNREGISTERED, ship2.getRegisterState(), "Ship 2 is unregistered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship2.getShipsState(), "Ship 2 is outside port limits");
		assertEquals(ShipDirection.DONTCARE, ship2.getShipsDirection(), "Ship 2 direction - dont care");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is stll registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is outside port limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - dont care");
			
		
		ship2.setRxMsg(null);		// no message expected
		ship2.transmit(Message.UNREGISTER, "");
		assertEquals(null, ship2.getRxMsg(), "Ship 2 unreg repeat - no response");
		
		ship1.setRxMsg(null);		// clear message
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals( Message.ACK, ship1.getRxMsg(), "Ship 1 reg ok after unregister");
		
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is outside port limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - dont care");
		
		System.out.println(ship1.toString());
		System.out.println(ship2.toString());
		
		port = null;
		ship1 = null;
		ship2 = null;
		
		System.out.println("===========================================================================================");
		

	}
	
	@Test
	public void registrationsOneShipErrorUseCases() {
		System.out.println("Test: registrationsOneShipErrorUseCases");

		Port port = new Port("Cork", true);
		Ship ship1 = new Ship(false, "Too Long", 1200000, 12000, ShipMode.DRY_BULK, 100000);
		Ship ship2 = new Ship(false, "Too Deep", 120000, 14000, ShipMode.DRY_BULK, 100000);
		Ship ship3 = new Ship(false, "Too Short", 4000, 10000, ShipMode.DRY_BULK, 100000);
		
		
		System.out.println(port.toString());
		System.out.println(ship1.toString());
		System.out.println(ship2.toString());
		System.out.println(ship3.toString());
		
    	System.out.println("Port listening...");
			
		ship1.setPortToCall(port);
		ship2.setPortToCall(port);
		ship3.setPortToCall(port);
		
		ship1.setRxMsg(null);		// clear message
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.NACK, ship1.getRxMsg(),  "Ship 1 too long");
		
		ship2.setRxMsg(null);		// clear message
		ship2.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.NACK, ship2.getRxMsg(),  "Ship 2 too deep");
		
		ship3.setRxMsg(null);		// clear message
		ship3.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.NACK, ship3.getRxMsg(),  "Ship 3 too short");
		
		port = null;
		ship1 = null;
		ship2 = null;
		ship3 = null;
		
		System.out.println("===========================================================================================");
		
	}
}
