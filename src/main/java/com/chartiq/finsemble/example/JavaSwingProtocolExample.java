package com.chartiq.finsemble.example;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;
import java.util.stream.Collectors;

public class JavaSwingProtocolExample extends JFrame implements WindowListener {
    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(JavaSwingExample.class.getName());

    private final List<String> launchArgs;

    private Container contentPane;
    private JLabel mnemonicLabel;
    private JTextField mnemonicTextField;
    private JLabel securityLabel;
    private JTextField securityTextField;
    private JLabel tailsLabel;
    private JTextField tailsTextField;
    private JButton transmitButton;
    private JTextArea logMessages;

    /**
     * Initializes a new instance of the JavaSwingExample class.
     *
     * @param args The arguments passed to the Java application from the command line
     */
    private JavaSwingProtocolExample(List<String> args) {
        LOGGER.addHandler(new MessageHandler(logMessages));

        LOGGER.info(String.format(
                "Finsemble Java Example starting with arguments:\n\t%s", String.join("\n\t", args)));
        LOGGER.info("Initiating Finsemble connection");

        launchArgs = args;

        createForm();
    }

    private void createForm() {
        setTitle("Java Swing Protocol Example");
        setBounds(0,0,300, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets= new Insets(10,10,0,0);
        constraints.fill = GridBagConstraints.BOTH;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        mnemonicLabel = new JLabel("mnemonic",JLabel.CENTER );
        contentPane.add(mnemonicLabel, constraints);

        mnemonicTextField = new JTextField("DES", 10);
        mnemonicTextField.setEditable(true);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        contentPane.add(mnemonicTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        securityLabel = new JLabel("security",JLabel.CENTER );
        contentPane.add(securityLabel, constraints);

        securityTextField = new JTextField("TSLA", 10);
        securityTextField.setEditable(true);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        contentPane.add(securityTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        tailsLabel = new JLabel("tails",JLabel.CENTER );
        contentPane.add(tailsLabel, constraints);

        tailsTextField = new JTextField("3", 10);
        tailsTextField.setEditable(true);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        contentPane.add(tailsTextField, constraints);

        transmitButton = new JButton("Transmit");
        transmitButton.addActionListener(e -> transmit());
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        contentPane.add(transmitButton, constraints);

        logMessages = new JTextArea(7, 25);
        logMessages.setEditable(false);
        constraints.ipady = 0;       //reset to default
        constraints.insets = new Insets(30,0,0,0);  //top padding
        constraints.gridx = 0;
        constraints.gridwidth = 3;
        constraints.gridy = 4;
        constraints.anchor = constraints.PAGE_END;
        JScrollPane scroll = new JScrollPane ( logMessages );
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        contentPane.add(scroll, constraints);
    }

    /**
     * Adds a message to the message box.
     *
     * @param s The message to add.
     */
    private void appendMessage(String s) {
        if (logMessages == null) {
            return;
        }

        try {
            Document doc = logMessages.getDocument();
            doc.insertString(0, String.format("%s\n", s), null);
        } catch (BadLocationException exc) {
            LOGGER.severe(exc.getMessage());
        }
    }

    /**
     * The main function of the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        final List<String> argList = new ArrayList<>(Arrays.asList(args));

        initLogging(argList);

        launchForm(argList);

        // the following statement is used to log any messages
        LOGGER.info(String.format("Starting JavaSwingExample: %s", Arrays.toString(args)));
    }

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

    /**
     * Launches the example form based on the command line arguments.
     *
     * @param args The command line arguments.
     */
    private static void launchForm(List<String> args) {
        final JavaSwingProtocolExample example = new JavaSwingProtocolExample(args);
        example.addWindowListener(example);
        example.setVisible(true);
    }

    private void transmit() {
        final String mnemonic = mnemonicTextField.getText();
        final String security = securityTextField.getText();
        final String tails = tailsTextField.getText();
        if (mnemonic == null || mnemonic.equals("")) {
            mnemonicTextField.setText("DES");
            return;
        }
        if (security == null || security.equals("")) {
            securityTextField.setText("TSLA");
            return;
        }
        if (tails == null || tails.equals("")) {
            tailsTextField.setText("3");
            return;
        }


        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                URI uri = new URI(String.format("fsbl://custom/runBloombergCommand?mnemonic=%s&security=%s&tails=%s",mnemonic, security,tails));
                desktop.browse(uri);
                LOGGER.info(String.format("Opening: %s", uri.toString()));
            } catch (Exception e) {
                LOGGER.severe(e.getMessage());
            }
        } else {
            LOGGER.info(String.format("Not supported"));
        }
    }

    /**
     * Invoked the first time a window is made visible.
     *
     * @param e The window event
     */
    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * Invoked when the user attempts to close the window
     * from the window's system menu.
     *
     * @param e The window event
     */
    @Override
    public void windowClosing(WindowEvent e) {

    }

    /**
     * Invoked when a window has been closed as the result
     * of calling dispose on the window.
     *
     * @param e The window event
     */
    @Override
    public void windowClosed(WindowEvent e) {

    }

    /**
     * Invoked when a window is changed from a normal to a
     * minimized state. For many platforms, a minimized window
     * is displayed as the icon specified in the window's
     * iconImage property.
     *
     * @param e The window event
     * @see Frame#setIconImage
     */
    @Override
    public void windowIconified(WindowEvent e) {

    }

    /**
     * Invoked when a window is changed from a minimized
     * to a normal state.
     *
     * @param e The window event
     */
    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    /**
     * Invoked when the Window is set to be the active Window. Only a Frame or
     * a Dialog can be the active Window. The native windowing system may
     * denote the active Window or its children with special decorations, such
     * as a highlighted title bar. The active Window is always either the
     * focused Window, or the first Frame or Dialog that is an owner of the
     * focused Window.
     *
     * @param e The window event
     */
    @Override
    public void windowActivated(WindowEvent e) {

    }

    /**
     * Invoked when a Window is no longer the active Window. Only a Frame or a
     * Dialog can be the active Window. The native windowing system may denote
     * the active Window or its children with special decorations, such as a
     * highlighted title bar. The active Window is always either the focused
     * Window, or the first Frame or Dialog that is an owner of the focused
     * Window.
     *
     * @param e The window event
     */
    @Override
    public void windowDeactivated(WindowEvent e) {

    }
    //endregion

    /**
     * Handler to write log messages to the message area of the form.
     */
    private class MessageHandler extends Handler {
        private final JTextArea messages;

        MessageHandler(JTextArea messages) {
            this.messages = messages;
        }

        /**
         * Publish a <tt>LogRecord</tt>.
         * <p>
         * The logging request was made initially to a <tt>Logger</tt> object,
         * which initialized the <tt>LogRecord</tt> and forwarded it here.
         * <p>
         * The <tt>Handler</tt>  is responsible for formatting the message, when and
         * if necessary.  The formatting should include localization.
         *
         * @param record description of the log event. A null record is
         *               silently ignored and is not published
         */
        @Override
        public void publish(LogRecord record) {
            final Throwable throwable = record.getThrown();

            String stackTrace = "";
            if (throwable != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                throwable.printStackTrace(pw);

                sw.append("\n");
                stackTrace = sw.toString();
            }

            final String message = String.format(
                    "%s: %s %s%s",
                    record.getLevel(),
                    record.getLoggerName(),
                    record.getMessage(),
                    stackTrace);

            appendMessage(message);
        }

        /**
         * Flush any buffered output.
         */
        @Override
        public void flush() {

        }

        /**
         * Close the <tt>Handler</tt> and free all associated resources.
         * <p>
         * The close method will perform a <tt>flush</tt> and then close the
         * <tt>Handler</tt>.   After close has been called this <tt>Handler</tt>
         * should no longer be used.  Method calls may either be silently
         * ignored or may throw runtime exceptions.
         *
         * @throws SecurityException if a security manager exists and if
         *                           the caller does not have <tt>LoggingPermission("control")</tt>.
         */
        @Override
        public void close() throws SecurityException {

        }
    }
}
