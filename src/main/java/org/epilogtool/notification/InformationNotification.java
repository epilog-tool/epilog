package org.epilogtool.notification;

public class InformationNotification extends Notification {

	public InformationNotification(String location, String message) {
		super(location, message, NotificationType.INFORMATION);
	}

}