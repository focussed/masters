package comms1;

/*
 * Ship's state throughout protocol
 */

public enum ShipState {
	OUTSIDE_PORT_LIMITS,
	ENROUTE_ANCHORAGE,
	ANCHORED,
	ENROUTE_PILOT_PICKUP_AREA,
	AT_PILOT_PICKUP_AREA,
	NAVIGATING,
	BERTHED
}
