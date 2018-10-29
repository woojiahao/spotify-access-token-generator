import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import me.chill.SpotifyUser;
import me.chill.authentication.SpotifyAuthenticationComponent;
import me.chill.authentication.SpotifyAuthorizationFlow;
import me.chill.exceptions.SpotifyAuthenticationException;

import java.util.Map;

public final class AuthorizationWindow {
	@FXML private WebView authorizationWindow;

	private SpotifyAuthorizationFlow flow;

	@FXML public void initialize() {
		Platform.runLater(this::loadWebpage);
	}

	private void loadWebpage() {
		WebEngine engine = authorizationWindow.getEngine();
		engine.load(flow.generateAuthorizationUrl().toString());
		engine.locationProperty().addListener((o, old, cur) -> {
			if (flow.isFinalRedirectUrl(cur)) performAuthorization(cur);
		});
	}

	private void launchPreviewWindow(SpotifyUser user) {
		try {
			Stage stage = (Stage) authorizationWindow.getScene().getWindow();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("preview_window.fxml"));
			Parent root = loader.load();
			PreviewWindow controller = loader.getController();
			controller.setUser(user);

			stage.setScene(new Scene(root));
			stage.centerOnScreen();
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to launch preview window");
		}
	}

	private void exchangeAccessToken(String authorizationCode) {
		try {
			launchPreviewWindow(
				flow.generateSpotifyUser(
					flow.exchangeAuthorizationCode(authorizationCode)
				)
			);
		} catch (SpotifyAuthenticationException e) {
			e.printStackTrace();
			System.out.println("Unable to exchange token");
		}
	}

	private void performAuthorization(String authorizationUrl) {
		try {
			String authorizationToken = flow
				.parseAuthorizationUrl(authorizationUrl)
				.get(SpotifyAuthorizationFlow.ParseComponent.Code);
			exchangeAccessToken(authorizationToken);
		} catch (SpotifyAuthenticationException e) {
			e.printStackTrace();
			System.out.println("Unable to parse authorization url");
		}
	}

	void setFlow(SpotifyAuthorizationFlow flow) {
		this.flow = flow;
	}
}
