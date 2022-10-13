package application;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.sound.sampled.LineUnavailableException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

public class Main extends Application {

	public static HashMap<Character, String> codes = new HashMap<>();

	@FXML
	private TextArea inputToTranslate;
	@FXML
	private TextArea inputToMorse;

	private final MorseSound morseSound = new MorseSound();

	@FXML
	public void initialize() {
		Platform.runLater(() -> inputToTranslate.requestFocus());
	}

	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

			Scene scene = new Scene(root, 640, 400);
			stage.setTitle("Text to Morse");
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String convertCharacterToMorse(char character) {
		if (codes.get(character) != null) {
			return codes.get(character);
		}

		throw new IllegalArgumentException("Character " + character + " is not supported");
	}

	public String convertWordToMorse(String word) {
		// Convert the string to uppercase so the char lookup won't fail
		word = word.toUpperCase();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			char currentCharacter = word.charAt(i);

			if (sb.length() != 0 && sb.toString().charAt(sb.length() - 1) != ' ') {
				sb.append(" ");
			}

			sb.append(convertCharacterToMorse(currentCharacter));
		}

		return sb.toString();
	}

	public String convertSentenceToMorse(String sentence) {
		StringBuilder sb = new StringBuilder();

		sentence = sentence.trim();
		String[] allWords = sentence.split(" ");
		for (String allWord : allWords) {
			sb.append(convertWordToMorse(allWord));

			if (sb.length() != 0 && sb.toString().charAt(sb.length() - 1) != ' ') {
				sb.append("  ");
			}
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		codes.put('A', ".-");
		codes.put('B', "-...");
		codes.put('C', "-.-.");
		codes.put('D', "-..");
		codes.put('E', ".");
		codes.put('F', "..-.");
		codes.put('G', "--.");
		codes.put('H', "....");
		codes.put('I', "..");
		codes.put('J', ".---");
		codes.put('K', "-.-");
		codes.put('L', ".-..");
		codes.put('M', "--");
		codes.put('N', "-.");
		codes.put('O', "---");
		codes.put('P', ".--.");
		codes.put('Q', "--.-");
		codes.put('R', ".-.");
		codes.put('S', "...");
		codes.put('T', "-");
		codes.put('U', "..-");
		codes.put('V', "...-");
		codes.put('W', ".--");
		codes.put('X', "-..-");
		codes.put('Y', "-.--");
		codes.put('Z', "--..");
		codes.put('1', ".----");
		codes.put('2', "..---");
		codes.put('3', "...--");
		codes.put('4', "....-");
		codes.put('5', ".....");
		codes.put('6', "-....");
		codes.put('7', "--...");
		codes.put('8', "---..");
		codes.put('9', "----.");
		codes.put('0', "-----");

		launch(args);
	}

	@FXML
	public void translateToMorse(MouseEvent event) {
		if (inputToTranslate.getText() == null || inputToTranslate.getText().trim().isEmpty()) {
			showAlert("No input", "Please insert some text to translate into Morse");
			return;
		}

		String convertedOutput = convertSentenceToMorse(inputToTranslate.getText()).trim();

		System.out.println("Conversion result: " + inputToTranslate.getText() + " -> " + convertedOutput);

		inputToMorse.clear();
		inputToMorse.setText(convertedOutput);

		try {
			playSound(inputToMorse.getText());
		} catch (InterruptedException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(AlertType.ERROR);

		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();
	}

	public void playSound(String morseSentence) throws InterruptedException, LineUnavailableException {
		for (int i = 0; i < morseSentence.length(); i++) {
			char c = morseSentence.charAt(i);

			switch (c) {
				case ' ' -> Thread.sleep(300);
				case '.' -> this.morseSound.tone(550, 110);
				case '-' -> this.morseSound.tone(550, 150);
			}
		}
	}
}
