package kg.apc.jmeter.gui;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import java.awt.Color;

/**
 * Verifies whether text input via a JTextField is an integer that falls within a specified set of bounds.
 */
public class IntegerInputVerifier extends InputVerifier {
    Color background, warningBackground;
    private int min, max;

    /**
     * Initialize the input verifier with an integer range and background colors.
     *
     * @param min the minimum permissible integer value
     * @param max the maximum permissible integer value
     * @param background the background color to set on the JComponent if the input is valid
     * @param warningBackground the background color to set on the JComponent if the input is invalid
     */
    public IntegerInputVerifier(int min, int max, Color background, Color warningBackground) {
        this.min = min;
        this.max = max;
        this.background = background;
        this.warningBackground = warningBackground;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    /**
     * Verifies the input with the side effect that the background color of {@code input} to {@code background} if {@see verify(JComponent)}
     * returns {@code true}, or {@code warningBackground} otherwise
     * @param input
     * @return
     */
    public boolean shouldYieldFocus(JComponent input) {
        boolean isValidInput = verify(input);
        if (isValidInput) {
            input.setBackground(background);
        } else {
            input.setBackground(warningBackground);
        }
        return isValidInput;
    }

    /**
     * Verifies that the input's text value can be converted to an integer that is >= {@code min} and <= {@code max}.
     *
     * @param input a JTextField instance
     * @return {@code true} if an only if the text input contains a value that
     */
    public boolean verify(JComponent input) {
        JTextField tf = (JTextField) input;
        try {
            int inputInt = Integer.parseInt(tf.getText());
            return (inputInt >= min && inputInt <= max);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}