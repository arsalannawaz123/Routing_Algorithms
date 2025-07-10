import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class DialogUtil {

    /**
     * Shows a dialog to ask the user for an edge weight.
     * Keeps asking until the user enters a valid number between 0.1 and 10000,
     * or cancels the dialog.
     *
     * @return Optional<Double> with the weight entered, or empty if the user cancelled.
     */
    public static Optional<Double> showEdgeWeightInputDialog() {
        while (true) {
            // Create a dialog with a default value of "1.0"
            TextInputDialog dialog = new TextInputDialog("1.0");
            dialog.setTitle("Edge Weight");  // Title of the window
            dialog.setHeaderText("Enter weight for the edge"); // Message above the text field
            dialog.setContentText("Weight (0.1 - 10000):");    // Label next to input

            // Show the dialog and wait for user to type something or cancel
            Optional<String> result = dialog.showAndWait();

            if (!result.isPresent()) {
                // If the user pressed cancel or closed the dialog, return empty
                return Optional.empty();
            }

            try {
                // Try to convert the input string to a double
                double val = Double.parseDouble(result.get());
                if (val >= 0.1 && val <= 10000) {
                    // If within allowed range, return the value
                    return Optional.of(val);
                } else {
                    // Otherwise, show an error and ask again
                    showErrorDialog("Invalid Weight", "Weight must be between 0.1 and 10000.");
                }
            } catch (NumberFormatException e) {
                // If the input was not a number, show an error and ask again
                showErrorDialog("Invalid Input", "Please enter a valid numeric weight.");
            }
        }
    }

    /**
     * Shows an error dialog with a given title and message.
     *
     * @param title   the title of the error window
     * @param message the message to display to the user
     */
    public static void showErrorDialog(String title, String message) {
        // Create and show an error pop-up with the given message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);        // Title of the error window
        alert.setHeaderText(null);    // No header text
        alert.setContentText(message); // Error message
        alert.showAndWait();          // Wait for the user to close it
    }
}
