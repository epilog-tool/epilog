package org.epilogtool.notification;

public class ErrorNotification extends Notification {

	public ErrorNotification(String location, String message) {
		super(location, message, NotificationType.ERROR);
	}

}