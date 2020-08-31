package comms1;

public class Model {

	public static void main(String[] args) {
		Port port = new Port("Cork", true);
		Ship ship1 = new Ship(false, "HMS Pinafore", 120000, 12000, ShipMode.DRY_BULK, 100000);
		Ship ship2 = new Ship(false, "HThe Bounty", 100000, 9000, ShipMode.DRY_BULK, 80000);
		
		
		System.out.println(port.toString());
		System.out.println(ship1.toString());
		System.out.println(ship2.toString());
		
    	System.out.println("Port listening...");
		//port.start();
			
		ship1.setPortToCall(port);
		System.out.println("sending request to port.");
		ship1.transmit(Message.REGISTER_REQUEST, "");
		
		ship1.transmit(Message.REGISTER_REQUEST, "");	
		
		ship1.transmit(Message.UNREGISTER, "");
		ship1.transmit(Message.UNREGISTER, "");
		
		ship1.transmit(Message.REGISTER_REQUEST, "");	
	}

}
