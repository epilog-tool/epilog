package org.epilogtool.notification;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Notification {

	private static SimpleDateFormat df = new SimpleDateFormat("[zzz yyyy-MM-dd HH:mm:ss]");
	private Date date;
	private NotificationType type;
	protected String location;
	private String message;

	/**
	 * 
	 * @param graph
	 *            the Graph the notification is associated to
	 * @param message
	 *            the message of the notification
	 * @param type
	 *            the type of the notification. See constants on Notification
	 *            class.
	 */
	public Notification(String topic, String message, NotificationType type) {
		this.date = new Date();
		this.type = type;
		this.location = topic;
		this.message = message;
		if (type == null) {
			this.type = NotificationType.ERROR;
		}
	}

	/**
	 * Return the location the notification is related to
	 * 
	 * @return the location the notification is related to
	 */
	public Object getLocation() {
		return this.location;
	}

	public String getDate() {
		return df.format(this.date);
	}

	/**
	 * Get the message describing this notification.
	 * 
	 * @return the message describing this notification
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "[" + df.format(this.date) + "] " + this.getMessage();
	}

	/**
	 * @return the type of this notification.
	 */
	public NotificationType getType() {
		return type;
	}
}