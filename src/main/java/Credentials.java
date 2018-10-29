import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import me.chill.authentication.SpotifyAuthenticationHelper;
import me.chill.authentication.SpotifyAuthorizationFlow;
import me.chill.authentication.SpotifyScope;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Credentials {
	@FXML private TextField clientId;
	@FXML private TextField clientSecret;
	@FXML private TextField redirectUrl;
	@FXML private Button start;
	@FXML private Button reset;
	@FXML private GridPane scopes;

	@FXML public void initialize() {
		reset.setOnMouseClicked(e -> resetDefaults());
		start.setOnMouseClicked(e -> authorizeUser());
	}

	private void authorizeUser() {
		String id = clientId.getText().trim();
		String secret = clientSecret.getText().trim();
		String url = redirectUrl.getText().trim();

		if (id.isEmpty() || secret.isEmpty() || url.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please fill in the client id, client secret and redirect url");
			return;
		}

		List<String> selectedScopes = getSelectedScopes();
		List<SpotifyScope> scopes = Arrays
			.stream(SpotifyScope.values())
			.filter(scope -> selectedScopes.contains(scope.getScopeName()))
			.collect(Collectors.toList());

		SpotifyAuthenticationHelper helper = new SpotifyAuthenticationHelper.Builder()
			.setClientId(id)
			.setClientSecret(secret)
			.setRedirectUrl(url)
			.setShowDialog(true)
			.setScopes(scopes)
			.build();

		SpotifyAuthorizationFlow flow = new SpotifyAuthorizationFlow(helper);

		String authorizationUrl = flow.generateAuthorizationUrl().toString();
	}

	private void resetDefaults() {
		clientId.clear();
		clientSecret.clear();
		redirectUrl.clear();
		checkAllScopes();
	}

	private void checkAllScopes() {
		scopes.getChildren()
			.stream()
			.filter(n -> n instanceof CheckBox)
			.map(n -> (CheckBox) n)
			.forEach(cb -> cb.setSelected(true));
	}

	private List<String> getSelectedScopes() {
		return scopes.getChildren()
			.stream()
			.filter(n -> n instanceof CheckBox)
			.map(n -> (CheckBox) n)
			.filter(CheckBox::isSelected)
			.map(Labeled::getText)
			.collect(Collectors.toList());
	}
}
