package kg.apc.jmeter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class EndOfFileExceptionTest {

    public EndOfFileExceptionTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInst()
    {
        EndOfFileException i = new EndOfFileException("");
        assertNotNull(i);
    }

}