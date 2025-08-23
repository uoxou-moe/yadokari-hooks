package moe.uoxou.yadokari_hooks.send;

public enum Event {
	SERVER_START     ("server_start"),
	SERVER_STOP      ("server_stop"),
	PLAYER_JOIN      ("player_join"),
	PLAYER_LEAVE     ("player_leave"),
	PLAYER_CHAT      ("player_chat"),
	COMMAND_EXECUTED ("command_executed");

	private final String value;

	Event(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
