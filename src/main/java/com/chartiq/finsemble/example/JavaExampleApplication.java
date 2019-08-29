package com.chartiq.finsemble.example;

import com.chartiq.finsemble.Finsemble;
import com.chartiq.finsemble.interfaces.ConnectionEventGenerator;
import com.chartiq.finsemble.interfaces.ConnectionListener;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;
import java.util.stream.Collectors;

public class JavaExampleApplication extends Application {
    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(JavaExampleApplication.class.getName());

    /**
     * Arguments passed via the command line
     */
    private static List<String> args;

    /**
     * Initializes a new instance of the JavaExample class.
     */
    public JavaExampleApplication() {
    }

    /**
     * The main function of the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        final List<String> argList = new ArrayList<>(Arrays.asList(args));

        initLogging(argList);

        // the following statement is used to log any messages
        LOGGER.info(String.format("Starting JavaExample: %s", Arrays.toString(args)));

        launch(args);

        // the following statement is used to log any messages
        LOGGER.info("Started JavaExample");
    }

    //region JavaFX Application Implementation
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) {
        LOGGER.info("Start method called");

        // Get arguments from Application
        args = getParameters().getRaw();

        LOGGER.info(String.format(
                "Finsemble Java Example starting with arguments:\n\t%s", String.join("\n\t", args)));
        final URL resource = JavaExampleApplication.class.getResource("JavaExample.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        try {
            final AnchorPane anchorPane = loader.load();
            JavaExample controller = loader.getController();
            controller.setArguments(args);

            LOGGER.info("Parent loaded from resource");
            primaryStage.setTitle("JavaExample");
            Scene scene = new Scene(anchorPane, 265, 400);

            LOGGER.info("Scene created");
            primaryStage.setScene(scene);

            LOGGER.info("Showing window");
            primaryStage.show();

            final Window window = scene.getWindow();
            controller.setWindow(window);
            controller.connect();

            LOGGER.info("Started successfully");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in start", e);
        }
    }
    //endregion

    private static void initLogging(List<String> args) {
        if (System.getProperty("java.util.logging.config.file") != null) {
            // Config file property has been set, no further initialization needed.
            return;
        }

        // Check whether logging file has been specified
        final List<String> properties = args
                .stream()
                .filter(arg -> arg.startsWith("-Djava.util.logging.config.file"))
                .collect(Collectors.toList());

        if ((properties.size() == 0) || !properties.get(0).contains("=")) {
            // No logging properties specified
            return;
        }

        // Get filename from parameter
        final String loggingPropertiesPath = properties.get(0).split("=")[1];
        try {
            final InputStream inputStream = new FileInputStream(loggingPropertiesPath);
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Could not load default logging.properties file", e);
        }
    }
}
