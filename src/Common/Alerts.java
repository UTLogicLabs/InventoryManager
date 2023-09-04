package Common;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import java.util.Optional;

public class Alerts {
    private static Alert alert;
    public enum confirmTypes { EXIT, CANCEL, DELETE}

    private static final String[] confirmTitle = {
            "Exit Inventory Manager",
            "Cancel Item",
            "Delete Item",
    };
    private static final String[] confirmContent = {
            "Are you sure you want to exit?",
            "All information will be lost. Do you want to continue?",
            "This change cannot be undone. Do you want to continue?"
    };

    public static boolean confirmation(confirmTypes confirmType) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(confirmTitle[confirmType.ordinal()]);
        alert.setContentText((confirmContent[confirmType.ordinal()]));
        Optional<ButtonType> result = alert.showAndWait();

        return (result.isPresent() && result.get() == ButtonType.OK);
    }

    public static void error() {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Invalid DataType");
        alert.setContentText("Please Enter Valid Data for All Fields.");
        alert.show();
    }

    public static void information(String message) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.show();
    }
}
