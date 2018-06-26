package org.epilogtool.notification;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class NotificationManager {

	private List<Notification> lNotifOld;
	private List<Notification> lNotifNew;
	// The static instance of the manager
	private static NotificationManager instance;

	private NotificationManager() {
		this.lNotifNew = new ArrayList<Notification>();
		this.lNotifOld = new ArrayList<Notification>();
	}

	private static NotificationManager getManager() {
		if (instance == null) {
			instance = new NotificationManager();
		}
		return instance;
	}

	private void register(Notification notif) {
		synchronized (this.lNotifNew) {
			this.lNotifNew.add(notif);
		}
	}

	public static void error(String location, String message) {
		getManager();
		NotificationManager.clearMessages();
		if (location != null && message != null) {
			getManager().register(new ErrorNotification(location, message));
			getManager();
			NotificationManager.dispatchDialogError(true, true);
		}
	}

	public static void information(String location, String message) {
		if (location != null && message != null) {
			getManager().register(new InformationNotification(location, message));
		}
	}

	public static void warning(String location, String message) {
		if (location != null && message != null) {
			getManager().register(new WarningNotification(location, message));
			getManager();
			NotificationManager.dispatchDialogWarning(true, true);
		}
	}

	private static void clearMessages() {
		getManager().lNotifOld.addAll(getManager().lNotifNew);
		getManager().lNotifNew.clear();
	}

	public static void dispatchDialogWarning(boolean bTime, boolean bLocation) {
		dispatchDialog(bTime, bLocation, JOptionPane.WARNING_MESSAGE);
	}

	public static void dispatchDialogError(boolean bTime, boolean bLocation) {
		dispatchDialog(bTime, bLocation, JOptionPane.ERROR_MESSAGE);
	}

	private static void dispatchDialog(boolean bTime, boolean bLocation, int type) {
		if (NotificationManager.getManager().lNotifNew.isEmpty())
			return;
		String msg = "";
		for (Notification n : NotificationManager.getManager().lNotifNew) {
			if (bTime) {
				msg += n.getDate() + " ";
			}
			if (bLocation) {
				msg += n.getLocation() + " ";
			}
			msg += n.getMessage() + "\n";
		}
		NotificationManager.clearMessages();
		JOptionPane.showMessageDialog(null, msg, "Notifications", type);
	}
}
