package org.epilogtool.notification;

import java.awt.Color;

public enum NotificationType {

	INFORMATION(3, Color.CYAN),

	WARNING(10, Color.ORANGE),

	ERROR(20, Color.RED);
	
	public final int timeout;
	public final Color color;
	
	private NotificationType(int timeout, Color color) {
		this.timeout = timeout;
		this.color = color;
	};
}
