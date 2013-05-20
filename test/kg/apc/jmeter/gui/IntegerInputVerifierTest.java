package kg.apc.jmeter.gui;

import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.JTextField;
import java.awt.Color;


/**
 * Unit tests for IntegerInputVerifier.
 */
public class IntegerInputVerifierTest {
    static Color background = Color.WHITE;
    static Color warningBackground = Color.YELLOW;

    /**
     * Test getMin() and getMax().
     */
    @Test
    public void testMinMax() {
        int min = 0;
        int max = 100;
        IntegerInputVerifier verifier = new IntegerInputVerifier(min, max, background, warningBackground);

        assertEquals("min differs", min, verifier.getMin());
        assertEquals("max differs", max, verifier.getMax());
    }

    /**
     * Test shouldYieldFocus() with valid input.
     */
    @Test
    public void testShouldYieldFocusValidInput() throws Exception {
        int min = 0;
        int max = 100;
        IntegerInputVerifier verifier = new IntegerInputVerifier(min, max, background, warningBackground);
        JTextField input = new JTextField(Integer.toString(min+5));
        input.setBackground(warningBackground);

        assertTrue(verifier.shouldYieldFocus(input));
        assertEquals("background was not reset", background, input.getBackground());
    }

    /**
     * Test shouldYieldFocus() with valid invalid input.
     */
    @Test
    public void testShouldYieldFocusValidInvalidInput() throws Exception {
        int min = 0;
        int max = 100;
        IntegerInputVerifier verifier = new IntegerInputVerifier(min, max, background, warningBackground);
        JTextField input = new JTextField("a");
        input.setBackground(background);

        assertFalse(verifier.shouldYieldFocus(input));
        assertEquals("background was not set to warning color", warningBackground, input.getBackground());
    }

    /**
     * Test verify() with a non-integer value.
     */
    @Test
    public void testVerifyNonInteger() throws Exception {
        int min = 0;
        int max = 100;
        IntegerInputVerifier verifier = new IntegerInputVerifier(min, max, background, warningBackground);
        JTextField input = new JTextField("A");

        assertFalse(verifier.verify(input));
    }

    /**
     * Test verify() with am integer == min.
     */
    @Test
    public void testVerifyMin() throws Exception {
        int min = 0;
        int max = 100;
        IntegerInputVerifier verifier = new IntegerInputVerifier(min, max, background, warningBackground);
        JTextField input = new JTextField(Integer.toString(min));

        assertTrue(verifier.verify(input));
    }

    /**
     * Test verify() with am integer == max.
     */
    @Test
    public void testVerifyMax() throws Exception {
        int min = 0;
        int max = 100;
        IntegerInputVerifier verifier = new IntegerInputVerifier(min, max, background, warningBackground);
        JTextField input = new JTextField(Integer.toString(max));

        assertTrue(verifier.verify(input));
    }

    /**
     * Test verify() with am integer < min.
     */
    @Test
    public void testVerifyLessThanMin() throws Exception {
        int min = 0;
        int max = 100;
        IntegerInputVerifier verifier = new IntegerInputVerifier(min, max, background, warningBackground);
        JTextField input = new JTextField(Integer.toString(min-1));

        assertFalse(verifier.verify(input));
    }

    /**
     * Test verify() with am integer > max.
     */
    @Test
    public void testVerifyGreaterThanMax() throws Exception {
        int min = 0;
        int max = 100;
        IntegerInputVerifier verifier = new IntegerInputVerifier(min, max, background, warningBackground);
        JTextField input = new JTextField(Integer.toString(Integer.MAX_VALUE));

        assertFalse(verifier.verify(input));
    }

}
