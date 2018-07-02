package application;

import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

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
	private TextArea input_totranslate;
	@FXML
	private TextArea input_tomorse;

	public enum CodeType {
		DOT, DASH;
	}

	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

			Scene scene = new Scene(root, 640, 400);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					input_totranslate.requestFocus();
				}
			});

			stage.setTitle("Text to Morse");
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String convertCharacterToMorse(char character) {
		System.out.println(character);
		if (codes.get(character) != null) {
			return codes.get(character);
		} else {
			return "";
		}
	}

	public String convertWordToMorse(String word) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			char currentCharacter = word.charAt(i);
			System.out.println(currentCharacter);

			if (sb.length() != 0 && sb.toString().charAt(sb.length() - 1) != ' ') {
				sb.append(" ");
			}

			System.out.println(convertCharacterToMorse(currentCharacter));
			sb.append(convertCharacterToMorse(currentCharacter));
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	public String convertSentenceToMorse(String sentence) {
		StringBuilder sb = new StringBuilder();

		sentence = sentence.trim();
		String[] allWords = sentence.split(" ");
		for (int i = 0; i < allWords.length; i++) {
			sb.append(convertWordToMorse(allWords[i]));

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
		if (input_totranslate.getText() == null || input_totranslate.getText().trim().isEmpty()) {
			showAlert("No input", "Please insert some text to translate into Morse");
			return;
		}

		System.out.println(convertSentenceToMorse(input_totranslate.getText()));

		input_tomorse.clear();
		input_tomorse.setText(convertSentenceToMorse(input_totranslate.getText().toUpperCase()));

		try {
			playSound(input_tomorse.getText());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
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
				case ' ':
					Thread.sleep(300);
					break;
				case '.':
					new MorseSound().tone(550, 110);
					break;
				case '-':
					new MorseSound().tone(550, 150);
					break;
			}
		}

	}
}
