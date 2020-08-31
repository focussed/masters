package comms1;

/*
 * Messages used by protocol
 */
public enum Message {
	REGISTER_REQUEST,
	UNREGISTER,
	REQUEST_TO_BERTH,
	ANCHORED,
	PROCEED_TO_ANCHORAGE_AREA,
	PROCEED_TO_PILOT_PICKUP_AREA,
	AT_PILOT_PICKUP_AREA,
	PILOT_READY_TO_BOARD,
	NAVIGATING_UNDER_PILOT,
	BERTHED,
	DEBERTHED,
	LEFT_PORT,
	ACK,
	NACK, 
	SIGNAL_FOR_ATTENTION
}