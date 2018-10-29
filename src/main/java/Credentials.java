import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.stream.Collectors;

public class Credentials {
	@FXML private TextField clientId;
	@FXML private TextField clientSecret;
	@FXML private Button start;
	@FXML private Button reset;
	@FXML private GridPane scopes;

	@FXML public void initialize() {
		reset.setOnMouseClicked(ev -> {
			clientId.clear();
			clientSecret.clear();
			checkAllScopes();
		});
	}

	private void checkAllScopes() {
		scopes.getChildren()
			.stream()
			.filter(n -> n instanceof CheckBox)
			.map(n -> (CheckBox) n)
			.forEach(cb -> cb.setSelected(true));
	}

	private List<String> getCheckedScopes() {
		return scopes.getChildren()
			.stream()
			.filter(n -> n instanceof CheckBox)
			.map(n -> (CheckBox) n)
			.filter(CheckBox::isSelected)
			.map(Labeled::getText)
			.collect(Collectors.toList());
	}
}
