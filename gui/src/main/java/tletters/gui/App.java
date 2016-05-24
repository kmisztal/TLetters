package tletters.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.dialog.FontSelectorDialog;
import tletters.featureextraction.Zoning;
import tletters.glyph.Glyph;
import tletters.glyph.LanguageType;
import tletters.glyphclassification.GlyphClassifier;
import tletters.glyphextraction.GlyphExtractor;
import tletters.imagegeneration.ImageGenerator;
import tletters.imagescaling.ImageScaler;
import tletters.knnclassification.EuclideanDistanceMeter;
import tletters.knnclassification.KNNClassifier;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class App extends Application implements Initializable {
    private Stage stage;
    private Font font;

    @FXML
    private TextField textField;
    @FXML
    private Label outputLabel;
    @FXML
    private Label fontLabel;

    private void updateFont(Font font) {
        this.font = font;
        fontLabel.setText(font.getName());
    }

    private java.awt.Font convertFontToAWT(Font font) {
        int awtFontStyle = 0;

        String style = font.getStyle().toLowerCase();

        if (style.contains("regular"))
            awtFontStyle |= java.awt.Font.PLAIN;
        if (style.contains("bold"))
            awtFontStyle |= java.awt.Font.BOLD;
        if (style.contains("italic"))
            awtFontStyle |= java.awt.Font.ITALIC;

        return new java.awt.Font(font.getName(), awtFontStyle, (int) Math.round(font.getSize()));
    }

    private List<Glyph> getGlyphs(String letters, java.awt.Font font, ImageGenerator imageGenerator,
                                  GlyphExtractor extractor, ImageScaler scaler, Zoning zoning) {
        int[] i = {0};
        imageGenerator.generateImage(font, 48, letters, 0);
        extractor.setImage(imageGenerator.getGeneratedImage());
        return extractor.scalpel()
                .stream()
                .map(scaler::scalImage)
                .map(image -> new Glyph(zoning.extractFeatures(image), LanguageType.GENERAL_PL, letters.charAt(i[0]++)))
                .collect(Collectors.toList());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateFont(Font.getDefault());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("stage_main.fxml"));
        root.getStylesheets().addAll(
                getClass().getResource("light.css").toExternalForm(),
//                getClass().getResource("dark.css").toExternalForm(),
                getClass().getResource("style.css").toExternalForm());
        Scene scene = new Scene(root);

        primaryStage.setTitle("TLetters");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    private void handleChooseFontAction(ActionEvent actionEvent) {
        new FontSelectorDialog(Font.getDefault())
                .showAndWait()
                .ifPresent(this::updateFont);
    }

    @FXML
    private void handleProcessAction() throws IOException {
        ImageGenerator imageGenerator = new ImageGenerator();
        GlyphExtractor extractor = new GlyphExtractor();
        ImageScaler scaler = new ImageScaler();
        Zoning zoning = new Zoning();
        GlyphClassifier classifier = new GlyphClassifier(
                new KNNClassifier<Double>(2),
                new EuclideanDistanceMeter<Double>(),
                getGlyphs("abcdefghijklmnoprstuwyzABCDEFGHIJKLMNOPRSTUWYZ",
                        convertFontToAWT(font), imageGenerator, extractor, scaler, zoning)
        );

        imageGenerator.generateImage(convertFontToAWT(font), (float) font.getSize(), textField.getText(), 0);
        extractor.setImage(imageGenerator.getGeneratedImage());
        String output = extractor.scalpel().stream()
                .map(scaler::scalImage)
                .map(zoning::extractFeatures)
                .map(classifier::classify)
                .map(String::valueOf)
                .collect(Collectors.joining());

        outputLabel.setText(String.format("Output: %s", output));
    }
}
