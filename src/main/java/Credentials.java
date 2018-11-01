import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.chill.authentication.SpotifyAuthenticationHelper;
import me.chill.authentication.SpotifyAuthorizationFlow;
import me.chill.authentication.SpotifyScope;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Credentials {
	@FXML private TextField clientId;
	@FXML private TextField clientSecret;
	@FXML private TextField redirectUrl;
	@FXML private Button start;
	@FXML private Button reset;
	@FXML private GridPane scopes;
	@FXML private MenuItem loadConfiguration;

	private List<CheckBox> scopeOptions = new ArrayList<>();

	@FXML public void initialize() {
		loadInformation(new File("config/config.json"));
		scopeOptions.addAll(scopes
			.getChildren()
			.stream()
			.filter(n -> n instanceof CheckBox)
			.map(n -> (CheckBox) n)
			.collect(Collectors.toList()));

		reset.setOnMouseClicked(e -> resetFields());
		start.setOnMouseClicked(e -> authorizeUser());
		loadConfiguration.setOnAction(e -> loadConfigurationFromFile());
	}

	private void loadConfigurationFromFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Configuration File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Configuration Files (*.json)", "*.json"));
		File file = fileChooser.showOpenDialog(start.getScene().getWindow());

		if (file != null) loadInformation(file);
	}

	private void authorizeUser() {
		String id = clientId.getText().trim();
		String secret = clientSecret.getText().trim();
		String url = redirectUrl.getText().trim();

		if (id.isEmpty() || secret.isEmpty() || url.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please fill in the client id, client secret and redirect url");
			return;
		}

		launchAuthorizationWindow(
			new SpotifyAuthorizationFlow(
				new SpotifyAuthenticationHelper.Builder()
					.clientId(id)
					.clientSecret(secret)
					.redirectUrl(url)
					.showDialog(true)
					.setScopes(matchSelectedScopes())
					.build()
			)
		);
	}

	private void launchAuthorizationWindow(SpotifyAuthorizationFlow flow) {
		try {
			Stage stage = (Stage) this.scopes.getScene().getWindow();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("authorization_window.fxml"));
			Parent root = loader.load();
			AuthorizationWindow controller = loader.getController();
			controller.setFlow(flow);

			stage.setScene(new Scene(root));
			stage.centerOnScreen();
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to launch authorization window");
		}
	}

	private List<SpotifyScope> matchSelectedScopes() {
		List<String> selectedScopes = getSelectedScopes();
		return Arrays
			.stream(SpotifyScope.values())
			.filter(scope -> selectedScopes.contains(scope.getScopeName()))
			.collect(Collectors.toList());
	}

	private void loadInformation(File file) {
		if (file.exists()) {
			try {
				JsonObject configDetails = new Gson().fromJson(new FileReader(file), JsonObject.class);
				if (!configDetails.has("id") || !configDetails.has("url") || !configDetails.has("secret")) {
					System.out.println("Missing fields in config.json, ensure you have id, secret and url");
					return;
				}

				clientId.setText(configDetails.get("id").getAsString());
				clientSecret.setText(configDetails.get("secret").getAsString());
				redirectUrl.setText(configDetails.get("url").getAsString());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Failed to read config file");
			}
		}
	}

	private void resetFields() {
		clientId.clear();
		clientSecret.clear();
		redirectUrl.clear();
		selectAllScopes();
	}

	private void selectAllScopes() {
		scopeOptions.forEach(cb -> cb.setSelected(true));
	}

	private List<String> getSelectedScopes() {
		return scopeOptions
			.stream()
			.filter(CheckBox::isSelected)
			.map(Labeled::getText)
			.collect(Collectors.toList());
	}
}
