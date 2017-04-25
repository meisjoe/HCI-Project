import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.Dimension;

import javax.swing.*;

/**
 * A custom {@link java.applet.Applet Applet} that sets-up a JavaFX environment.
 */
public class FXApplet extends JApplet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 683209693365959287L;
	/**
     * The JavaFX scene.
     * Guaranteed to be <code>non-null</code> when {@link #initApplet()} is called.
     */
    protected Scene scene;
    /**
     * The JavaFX scene's root node.
     * Guaranteed to be <code>non-null</code> when {@link #initApplet()} is called.
     */
    protected Group root;

    final JFXPanel fxPanel = new JFXPanel();

    /**
     * This method is declared final and sets-up the JavaFX environment.
     * In order to add initialization code, override {@link #initApplet()}.
     * <p></p>
     * <b>Original Description:</b> <br>
     * {@inheritDoc}
     */
    @Override
    public final void start() { // This method is invoked when applet is loaded
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initSwing();
            }
        });
    }

    private void initSwing() { // This method is invoked on Swing thread
        add(fxPanel);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
                initApplet();
                rootPane.setMinimumSize(new Dimension(1200, 800));
            }
        });
    }

    private void initFX(JFXPanel fxPanel) { // This method is invoked on JavaFX thread
        root = new Group();
        scene = new Scene(root);
        fxPanel.setScene(scene);
    }

    /**
     * Add custom initialization code here. <br>
     * This method is called by FXApplet once the {@link java.applet.Applet#init() init} method was invoked (in order
     * to signal that the applet has been loaded) and
     * the {@link #scene scene} & {@link #root root} fields has been set-up.
     * @see java.applet.Applet#init()
     */
    public void initApplet() {
    }
}