package org.epilogtool.notification;

public class WarningNotification extends Notification {

	public WarningNotification(String location, String message) {
		super(location, message, NotificationType.WARNING);
	}

}