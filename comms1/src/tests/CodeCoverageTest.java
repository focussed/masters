package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comms1.Message;
import comms1.Port;
import comms1.RegisterState;
import comms1.Ship;
import comms1.ShipDirection;
import comms1.ShipMode;
import comms1.ShipState;
import comms1.Port.SeaConditions;


public class CodeCoverageTest {
	private Port port;
	private Ship ship1, ship2, ship3, ship4, ship5, ship6, ship7, ship8, ship9;
		

@Before
public void setUp() throws Exception {

	port = new Port("Cork", true);
	ship1 = new Ship(false, "HMS Pinafore", 120000, 12000, ShipMode.DRY_BULK, 100000);
	ship2 = new Ship(false, "The Bounty", 100000, 9000, ShipMode.DRY_BULK, 80000);
	ship3 = new Ship(false, "Too Long", 1200000, 12000, ShipMode.DRY_BULK, 100000);
	ship4 = new Ship(false, "Too Deep", 120000, 14000, ShipMode.DRY_BULK, 100000);
	ship5 = new Ship(false, "Too Short", 4000, 10000, ShipMode.DRY_BULK, 100000); 
	ship6 = new Ship(false, "The Black Pig", 120000, 12000, ShipMode.DRY_BULK, 100000);
	ship7 = new Ship(false, "Asgard", 120000, 12000, ShipMode.DRY_BULK, 100000);	
	ship8 = new Ship(false, "Sea Princess", 320000, 12000, ShipMode.CRUISE, 500000);
	ship9 = new Ship(false, "Titanic", 320000, 12000, ShipMode.CRUISE, 500000);
}

@After
public void tearDown() throws Exception {
	port = null;
	ship1 = null;
	ship2 = null;
	ship3 = null;
	ship4 = null;
	ship5 = null;
	ship6 = null;
	ship7 = null;
	ship8 = null;
	ship9 = null;

}

	@Test
	public void registrationsOneShip() {
		System.out.println("Test: registrationsOneShip");
			
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
		
		System.out.println("===========================================================================================");
		
	}

	@Test
	public void registrationsTwoShips() {
		
    	System.out.println("Test: registrationsTwoShips");
	
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
		
		System.out.println("===========================================================================================");
		

	}
	
	@Test
	public void registrationsOneShipErrorUseCases() {
		System.out.println("Test: registrationsOneShipErrorUseCases");		
		
		System.out.println(port.toString());
		System.out.println(ship1.toString());
		System.out.println(ship2.toString());
		System.out.println(ship3.toString());
		
    	System.out.println("Port listening...");
			
		ship3.setPortToCall(port);
		ship4.setPortToCall(port);
		ship5.setPortToCall(port);
		
		ship3.setRxMsg(null);		// clear message
		ship3.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.NACK, ship3.getRxMsg(),  "Ship 3 too long");
		
		ship4.setRxMsg(null);		// clear message
		ship4.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.NACK, ship4.getRxMsg(),  "Ship 4 too deep");
		
		ship5.setRxMsg(null);		// clear message
		ship5.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.NACK, ship5.getRxMsg(),  "Ship 5 too short");
		
		
		System.out.println("===========================================================================================");
		
	}
	
	@Test
	public void RogueBerthandDeberth() {
		System.out.println("Test: RogueBerthandDeberth");
		
		System.out.println(port.toString());
		System.out.println(ship1.toString());
		
    	System.out.println("Port listening...");
			
		ship1.setPortToCall(port);
		
		
		ship1.setRxMsg(null);		// clear message
		ship1.setRxText(null);		// clear text
		
		ship1.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(RegisterState.UNREGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.NACK, ship1.getRxMsg(),  "Ship 1 ok to Berth");		
		
		ship1.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.UNREGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.NACK, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship1.getRxMsg(), "Ship 1 reg ok");

		// out of sequence messages - did not request to berth
		
		ship1.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship1.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship1.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.NACK, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship1.transmit(Message.ANCHORED, "");
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
		
		System.out.println("===========================================================================================");
		
	}
	
	@Test
	public void RequestToBerthShip1RoughSea() {
		System.out.println("Test: RequestToBerthShip1RoughSea");

		SeaConditions.setCurrentConditionsOK(false);	// not ok conditions
				
		System.out.println(port.toString());
		System.out.println(ship1.toString());
		
    	System.out.println("Port listening...");
			
		ship1.setPortToCall(port);
		
		ship1.setRxMsg(null);		// clear message
		ship1.setRxText(null);		// clear text
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship1.getRxMsg(), "Ship 1 reg ok");
		
		ship1.setRxMsg(null);		// clear message
		ship1.setRxText(null);		// clear text
		ship1.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_ANCHORAGE_AREA, ship1.getRxMsg(), "Ship 1 to anchor");
		;
		System.out.println(ship1.toString());
		
		ship1.transmit(Message.ANCHORED, "");
		assertEquals(Message.ACK, ship1.getRxMsg(), "Ship 1 anchored");
		
		System.out.println("===========================================================================================");
		
	}
	
	@Test
	public void OneShipBerthandDeberth() {
		System.out.println("Test: OneShipBerthandDeberth");
	
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
		
		System.out.println("===========================================================================================");
		
	}

	@Test
	public void FourShipsBerthandDeberth() {
		System.out.println("Test: Full cycle LeavePort4Ships");
		
		System.out.println(port.toString());
		System.out.println(ship1.toString());
		System.out.println(ship2.toString());
		System.out.println(ship6.toString());
		System.out.println(ship7.toString());
		
    	System.out.println("Port listening...");
			
		ship1.setPortToCall(port);
		ship2.setPortToCall(port);
		ship6.setPortToCall(port);
		ship7.setPortToCall(port);
		
		ship1.setRxMsg(null);		// clear message
		ship1.setRxText(null);		// clear text
		ship1.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship1.getRxMsg(), "Ship 1 reg ok");
		
		ship2.setRxMsg(null);		// clear message
		ship2.setRxText(null);		// clear text
		ship2.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship2.getRxMsg(), "Ship 2 reg ok");
		
		ship6.setRxMsg(null);		// clear message
		ship6.setRxText(null);		// clear text
		ship6.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship6.getRxMsg(), "Ship 3 reg ok");

		ship7.setRxMsg(null);		// clear message
		ship7.setRxText(null);		// clear text
		ship7.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship7.getRxMsg(), "Ship reg ok");

		ship1.setRxMsg(null);		// clear message
		ship1.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_PILOT_PICKUP_AREA, ship1.getRxMsg(),  "Ship 1 ok to Berth");
		
		assertEquals(RegisterState.REGISTERED, ship1.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.ENROUTE_PILOT_PICKUP_AREA, ship1.getShipsState(), "Ship 1 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship1.getShipsDirection(), "Ship 1 direction - approaching");

		
		ship2.setRxMsg(null);		// clear message
		ship2.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_PILOT_PICKUP_AREA, ship2.getRxMsg(),  "Ship 2 ok to Berth");
		
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.ENROUTE_PILOT_PICKUP_AREA, ship2.getShipsState(), "Ship 2 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship2.getShipsDirection(), "Ship 2 direction - approaching");
		
		ship6.setRxMsg(null);		// clear message
		ship6.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_ANCHORAGE_AREA, ship6.getRxMsg(),  "Ship 3 ok to Berth");
		
		assertEquals(RegisterState.REGISTERED, ship6.getRegisterState(), "Ship 3 is registered");
		assertEquals(ShipState.ENROUTE_ANCHORAGE, ship6.getShipsState(), "Ship 3 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship6.getShipsDirection(), "Ship 3 direction - approaching");

		ship7.setRxMsg(null);		// clear message
		ship7.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_ANCHORAGE_AREA, ship7.getRxMsg(),  "Ship 1 ok to Berth");
		
		assertEquals(RegisterState.REGISTERED, ship7.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.ENROUTE_ANCHORAGE, ship7.getShipsState(), "Ship 1 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship7.getShipsDirection(), "Ship 1 direction - approaching");

		ship6.transmit(Message.ANCHORED, "");
		assertEquals(RegisterState.REGISTERED, ship6.getRegisterState(), "Ship 3 is registered");
		assertEquals(ShipState.ANCHORED, ship6.getShipsState(), "Ship 3 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship6.getShipsDirection(), "Ship 3 direction - approaching");
		assertEquals(Message.ACK, ship6.getRxMsg(),  "Ship 3 anchored");

		ship7.transmit(Message.ANCHORED, "");
		assertEquals(RegisterState.REGISTERED, ship7.getRegisterState(), "Ship 3 is registered");
		assertEquals(ShipState.ANCHORED, ship7.getShipsState(), "Ship 4 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship7.getShipsDirection(), "Ship 4 direction - approaching");
		assertEquals(Message.ACK, ship7.getRxMsg(),  "Ship 4 anchored");


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
		assertEquals(RegisterState.REGISTERED, ship2.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship2.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship2.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.PILOT_READY_TO_BOARD, ship2.getRxMsg(),  "Ship 1 ok to Berth");
		
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
		System.out.println(ship2.toString());
		System.out.println(ship6.toString());
		System.out.println(ship7.toString());
		
		System.out.println("===========================================================================================");
		
	}
	
	@Test
	public void OneCruiserBerthandDeberth() {
		System.out.println("Test: OneCruiserBerthandDeberth");
		
		System.out.println(port.toString());
		System.out.println(ship8.toString());
		
    	System.out.println("Port listening...");
			
		ship8.setPortToCall(port);
		
		ship8.setRxMsg(null);		// clear message
		ship8.setRxText(null);		// clear text
		ship8.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship8.getRxMsg(), "Ship 1 reg ok");
		
		ship8.setRxMsg(null);		// clear message
		ship8.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_PILOT_PICKUP_AREA, ship8.getRxMsg(),  "Ship 1 ok to Berth");
		
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.ENROUTE_PILOT_PICKUP_AREA, ship8.getShipsState(), "Ship 1 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship8.getShipsDirection(), "Ship 1 direction - approaching");
		
		ship8.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship8.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.PILOT_READY_TO_BOARD, ship8.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship8.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship8.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship8.transmit(Message.BERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.BERTHED, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship8.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship8.transmit(Message.DEBERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship8.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 deberthed");
		
		ship8.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship8.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 navigating port to leave");
		
		ship8.transmit(Message.LEFT_PORT, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship8.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 left port");
	
		ship8.transmit(Message.UNREGISTER, "");
		assertEquals(RegisterState.UNREGISTERED, ship8.getRegisterState(), "Ship 1 is deregistered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship8.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 gone");
		

		System.out.println(ship8.toString());
	
		
		System.out.println("===========================================================================================");
		
	}
		
	@Test
	public void ship8Berthship9MustWait() {
		System.out.println("Test: ship8Berthship9MustWait");
		
		System.out.println(port.toString());
		System.out.println(ship8.toString());
		System.out.println(ship9.toString());
		
    	System.out.println("Port listening...");
			
		ship8.setPortToCall(port);
		ship9.setPortToCall(port);
		
		ship8.setRxMsg(null);		// clear message
		ship8.setRxText(null);		// clear text
		ship8.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship8.getRxMsg(), "Ship 1 reg ok");
		
		ship9.setRxMsg(null);		// clear message
		ship9.setRxText(null);		// clear text
		ship9.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship9.getRxMsg(), "Ship 2 reg ok");
		
		
		ship8.setRxMsg(null);		// clear message
		ship8.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_PILOT_PICKUP_AREA, ship8.getRxMsg(),  "Ship 1 ok to Berth");
		
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.ENROUTE_PILOT_PICKUP_AREA, ship8.getShipsState(), "Ship 1 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship8.getShipsDirection(), "Ship 1 direction - approaching");

		
		ship9.setRxMsg(null);		// clear message
		ship9.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_ANCHORAGE_AREA, ship9.getRxMsg(),  "Ship 2 told to anchor");	
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.ENROUTE_ANCHORAGE, ship9.getShipsState(), "Ship 2 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship9.getShipsDirection(), "Ship 2 direction - approaching");
		
		ship9.setRxMsg(null);		// clear message
		ship9.transmit(Message.ANCHORED, "");
		assertEquals(Message.ACK, ship9.getRxMsg(),  "Ship 2 told to anchor");	
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.ANCHORED, ship9.getShipsState(), "Ship 2 is anchored");
		assertEquals(ShipDirection.APPROACHING, ship9.getShipsDirection(), "Ship 2 direction - approaching");
				
		
		ship8.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship8.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.PILOT_READY_TO_BOARD, ship8.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship8.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship8.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship8.transmit(Message.BERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.BERTHED, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship8.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 ok to Berth");
		
		// Ship 1 leaves first
		
		ship8.transmit(Message.DEBERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship8.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 deberthed");
		
		ship8.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship8.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 navigating port to leave");
		
		ship8.transmit(Message.LEFT_PORT, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship8.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 left port");
	
		ship8.transmit(Message.UNREGISTER, "");
		assertEquals(RegisterState.UNREGISTERED, ship8.getRegisterState(), "Ship 1 is deregistered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship8.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 gone");
		
		// Ship 2 leaves second
		
		ship9.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.NAVIGATING, ship9.getShipsState(), "Ship 2 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship9.getShipsDirection(), "Ship 2 direction - approaching");
		assertEquals(Message.PILOT_READY_TO_BOARD, ship9.getRxMsg(),  "Ship 2 ok to Berth");
		
		ship9.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship9.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship9.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship9.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship9.transmit(Message.BERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.BERTHED, ship9.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship9.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship9.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship9.transmit(Message.DEBERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship9.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship9.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship9.getRxMsg(),  "Ship 1 deberthed");
		
		ship9.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship9.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship9.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship9.getRxMsg(),  "Ship 1 navigating port to leave");
		
		ship9.transmit(Message.LEFT_PORT, "");
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship9.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship9.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship9.getRxMsg(),  "Ship 1 left port");
	
		ship9.transmit(Message.UNREGISTER, "");
		assertEquals(RegisterState.UNREGISTERED, ship9.getRegisterState(), "Ship 1 is deregistered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship9.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship9.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship9.getRxMsg(),  "Ship 1 gone");
		

		
		System.out.println(ship8.toString());
		System.out.println(ship9.toString());
			
		System.out.println("===========================================================================================");
		
	}
	
	@Test
	public void Ship1BerthShip2AttemptsToCircumvent() {
		System.out.println("Test: Full cycle LeavePort2Ships");

		System.out.println(port.toString());
		System.out.println(ship8.toString());
		System.out.println(ship9.toString());
		
    	System.out.println("Port listening...");
			
		ship8.setPortToCall(port);
		ship9.setPortToCall(port);
		
		ship8.setRxMsg(null);		// clear message
		ship8.setRxText(null);		// clear text
		ship8.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship8.getRxMsg(), "Ship 1 reg ok");
		
		ship9.setRxMsg(null);		// clear message
		ship9.setRxText(null);		// clear text
		ship9.transmit(Message.REGISTER_REQUEST, "");
		assertEquals(Message.ACK, ship9.getRxMsg(), "Ship 2 reg ok");
		
		
		ship8.setRxMsg(null);		// clear message
		ship8.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_PILOT_PICKUP_AREA, ship8.getRxMsg(),  "Ship 1 ok to Berth");
		
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.ENROUTE_PILOT_PICKUP_AREA, ship8.getShipsState(), "Ship 1 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship8.getShipsDirection(), "Ship 1 direction - approaching");

		
		ship9.setRxMsg(null);		// clear message
		ship9.transmit(Message.REQUEST_TO_BERTH, "");
		assertEquals(Message.PROCEED_TO_ANCHORAGE_AREA, ship9.getRxMsg(),  "Ship 2 told to anchor");	
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.ENROUTE_ANCHORAGE, ship9.getShipsState(), "Ship 2 is outside port limits");
		assertEquals(ShipDirection.APPROACHING, ship9.getShipsDirection(), "Ship 2 direction - approaching");
		
		ship9.setRxMsg(null);		// clear message
		ship9.transmit(Message.ANCHORED, "");
		assertEquals(Message.ACK, ship9.getRxMsg(),  "Ship 2 told to anchor");	
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.ANCHORED, ship9.getShipsState(), "Ship 2 is anchored");
		assertEquals(ShipDirection.APPROACHING, ship9.getShipsDirection(), "Ship 2 direction - approaching");
				
		
		ship8.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship8.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.PILOT_READY_TO_BOARD, ship8.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship8.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship8.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship8.transmit(Message.BERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.BERTHED, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship8.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 ok to Berth");
		
		/////////
	
		ship9.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.ANCHORED, ship9.getShipsState(), "Ship 2 is anchored");
		assertEquals(ShipDirection.APPROACHING, ship9.getShipsDirection(), "Ship 2 direction - approaching");
		assertEquals(Message.NACK, ship9.getRxMsg(),  "Ship 1 is not ok to move here");
		
		// Ship 1 leaves first
		
		ship8.transmit(Message.DEBERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship8.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 deberthed");
		
		ship8.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship8.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 navigating port to leave");
		
		ship8.transmit(Message.LEFT_PORT, "");
		assertEquals(RegisterState.REGISTERED, ship8.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship8.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 left port");
	
		ship8.transmit(Message.UNREGISTER, "");
		assertEquals(RegisterState.UNREGISTERED, ship8.getRegisterState(), "Ship 1 is deregistered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship8.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship8.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship8.getRxMsg(),  "Ship 1 gone");
		
		// Ship 2 leaves second
		
		ship9.transmit(Message.AT_PILOT_PICKUP_AREA, "");
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 2 is registered");
		assertEquals(ShipState.NAVIGATING, ship9.getShipsState(), "Ship 2 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship9.getShipsDirection(), "Ship 2 direction - approaching");
		assertEquals(Message.PILOT_READY_TO_BOARD, ship9.getRxMsg(),  "Ship 2 ok to Berth");
		
		ship9.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship9.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship9.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship9.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship9.transmit(Message.BERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.BERTHED, ship9.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.APPROACHING, ship9.getShipsDirection(), "Ship 1 direction - approaching");
		assertEquals(Message.ACK, ship9.getRxMsg(),  "Ship 1 ok to Berth");
		
		ship9.transmit(Message.DEBERTHED, "");
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship9.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship9.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship9.getRxMsg(),  "Ship 1 deberthed");
		
		ship9.transmit(Message.NAVIGATING_UNDER_PILOT, "");
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.NAVIGATING, ship9.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.LEAVING, ship9.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship9.getRxMsg(),  "Ship 1 navigating port to leave");
		
		ship9.transmit(Message.LEFT_PORT, "");
		assertEquals(RegisterState.REGISTERED, ship9.getRegisterState(), "Ship 1 is registered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship9.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship9.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship9.getRxMsg(),  "Ship 1 left port");
	
		ship9.transmit(Message.UNREGISTER, "");
		assertEquals(RegisterState.UNREGISTERED, ship9.getRegisterState(), "Ship 1 is deregistered");
		assertEquals(ShipState.OUTSIDE_PORT_LIMITS, ship9.getShipsState(), "Ship 1 is inside limits");
		assertEquals(ShipDirection.DONTCARE, ship9.getShipsDirection(), "Ship 1 direction - leaving");
		assertEquals(Message.ACK, ship9.getRxMsg(),  "Ship 1 gone");
		

		
		System.out.println(ship8.toString());
		System.out.println(ship9.toString());
		
		
		System.out.println("===========================================================================================");
		
	}
	
	@Test
	public void signalTest() {

	System.out.println("Test: Signal test");

	SeaConditions.setCurrentConditionsOK(true);	// ok conditions
	
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
	
	ship1.setRxMsg(null);		// clear message
	ship1.setRxText(null);		// clear text
	ship1.transmit(Message.SIGNAL_FOR_ATTENTION, "We have an issue with our propulsion, request help");
	assertEquals(Message.ACK, ship1.getRxMsg(), "Ship 1 to anchor");
	
	System.out.println(ship1.toString());
	
	ship1.setRxMsg(null);		// clear message
	ship1.setRxText(null);		// clear text

	ship1.transmit(Message.NACK, "We cannot comply");
	assertEquals(null, ship1.getRxMsg(), "no response");
	
	System.out.println("===========================================================================================");
	
}
	
}
