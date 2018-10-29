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
		exit.setOnMouseClicked(e -> exitGenerator());
	}

	private void exitGenerator() {
		final int selection = JOptionPane.showConfirmDialog(null, "Are you sure you wish to exit?");
		if (selection == JOptionPane.YES_OPTION) {
			user.disableTimer();
			Stage stage = (Stage) clipboard.getScene().getWindow();
			stage.close();
		}
	}

	private void copyToClipboard() {
		copyText();
		editLabel();
	}

	private void editLabel() {
		title.setText("Access Token (Copied to clipboard):");
		final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> title.setText("Access Token:")));
		timeline.play();
	}

	private void copyText() {
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(accessToken.getText());
		clipboard.setContent(content);
	}

	private void loadDetails() {
		user.startTimer();
		accessToken.setText(user.getAccessToken());

	}

	void setUser(SpotifyUser user) {
		this.user = user;
	}
}
