import com.sun.org.apache.bcel.internal.generic.NEW;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.chill.SpotifyUser;

import javax.swing.*;

public class PreviewWindow {
	@FXML private TextField accessToken;
	@FXML private Button clipboard;
	@FXML private Button exit;
	@FXML private Label title;

	private SpotifyUser user;

	@FXML public void initialize() {
		Platform.runLater(this::loadDetails);

		clipboard.setOnMouseClicked(e -> copyToClipboard());
		exit.setOnMouseClicked(e -> {
			final int selection = JOptionPane.showConfirmDialog(null, "Are you sure you wish to exit?");
			if (selection == JOptionPane.YES_OPTION) exitGenerator();
		});
	}

	private void exitGenerator() {
		user.disableTimer();
		Stage stage = (Stage) clipboard.getScene().getWindow();
		stage.close();
	}

	private void copyToClipboard() {
		copyText();
		editLabel("Copied to clipboard", 3);
	}

	private void editLabel(String addOn, int duration) {
		title.setText(String.format("Access Token (%s):", addOn));
		final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(duration), e -> title.setText("Access Token:")));
		timeline.play();
	}

	private void copyText() {
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(accessToken.getText());
		clipboard.setContent(content);
	}

	private void loadDetails() {
		accessToken.setText(user.getAccessToken());
		if (user.getExpiryDuration() == null) {
			System.out.println("Unable to refresh tokens due to some unknown error with the expiry duration");
			return;
		}
		final Timeline timeline = new Timeline(
			new KeyFrame(Duration.seconds(user.getExpiryDuration() + 1),
			e -> {
				JOptionPane.showMessageDialog(null, "Access Token was refreshed");
				editLabel("Refreshed", 5);
				accessToken.setText(user.getAccessToken());
			}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	void setUser(SpotifyUser user) {
		this.user = user;
	}
}
