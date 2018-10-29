import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import me.chill.SpotifyUser;

public class PreviewWindow {
	@FXML private Label accessToken;

	private SpotifyUser user;

	@FXML public void initialize() {
		Platform.runLater(this::loadDetails);
	}

	private void loadDetails() {
		user.startTimer();
		accessToken.setText(user.getAccessToken());
	}

	void setUser(SpotifyUser user) {
		this.user = user;
	}
}
