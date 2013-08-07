package pt.igc.nmd.epilog.gui;

public enum MsgStatus {
	STATUS("<font color='green'>Status:</font>"), WARNING(
			"<font color='yellow'>Warning:</font>"), ERROR(
			"<font color='red'>Error:</font>");
	private final String status;

	private MsgStatus(String status) {
		this.status = status;
	}

	public String getMessage(String message) {
		return "<html>" + this.status +" " + message + "</html>";
	}
}
