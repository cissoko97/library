package org.ckCoder.utils;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import static org.ckCoder.utils.NotificationType.*;

public class NotificationUtil {

    public static void showNotiication(String type, String title, String message) {
        Notifications notification = Notifications.create()
                .title(title)
                .text(message)
                .graphic(null)
                .hideAfter(Duration.seconds(8))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(event -> {
                    System.out.printf("Bonjour le monde");
                });

        switch (type) {
            case "ERROR":
                notification.showError();
                break;
            case "SUCCES":
                notification.showInformation();
            default:
                notification.showWarning();
                break;
        }
    }
}
