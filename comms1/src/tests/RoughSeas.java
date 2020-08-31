package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import comms1.Message;
import comms1.Port;
import comms1.Ship;
import comms1.ShipMode;
import comms1.Port.SeaConditions;

class RoughSeas {

	@Test
	public void RequestToBerthShip1RoughSea() {
		System.out.println("Test: RequestToBerthShip1RoughSea");

		Port port = new Port("Cork", true);
		SeaConditions.setCurrentConditionsOK(false);	// not ok conditions
		
		Ship ship1 = new Ship(true, "HMS Pinafore", 120000, 12000, ShipMode.DRY_BULK, 100000);
		
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
		
		port = null;
		ship1 = null;
		
		System.out.println("===========================================================================================");
		
	}
	

}
