package kg.apc.charting;

import kg.apc.charting.DateTimeRenderer;
import java.util.Calendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apc
 */
public class DateTimeRendererTest
{

    private static final String HHMMSS = "HH:mm:ss";

    /**
     *
     */
    public DateTimeRendererTest()
    {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass()
            throws Exception
    {
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass()
            throws Exception
    {
    }

    /**
     *
     */
    @Before
    public void setUp()
    {
    }

    /**
     *
     */
    @After
    public void tearDown()
    {
    }

    /**
     * Test of setValue method, of class DateTimeRenderer.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("setValue");
        DateTimeRenderer instance = new DateTimeRenderer(HHMMSS);

        Calendar test = Calendar.getInstance();

        test.set(Calendar.HOUR_OF_DAY, 3);
        test.set(Calendar.MINUTE, 16);
        test.set(Calendar.SECOND, 40);
        test.set(Calendar.MILLISECOND, 0);

        instance.setValue(test.getTimeInMillis());
        String text = instance.getText();
        assertEquals("03:16:40", text);
    }

    @Test
    public void testSetValue_double()
    {
        System.out.println("setValue");
        DateTimeRenderer instance = new DateTimeRenderer(HHMMSS);

        Calendar test = Calendar.getInstance();

        test.set(Calendar.HOUR_OF_DAY, 0);
        test.set(Calendar.MINUTE, 0);
        test.set(Calendar.SECOND, 0);
        test.set(Calendar.MILLISECOND, 0);

        instance.setValue(test.getTimeInMillis()+0.5);

        String text = instance.getText();
        assertEquals("00:00:00", text);
    }
    /**
     *
     */
    @Test
    public void testConstructor_null()
    {
        DateTimeRenderer instance = new DateTimeRenderer();
        instance.setValue(null);
        assertEquals("", instance.getText());
    }

    @Test
    public void testConstructor2()
    {
        DateTimeRenderer instance = new DateTimeRenderer(HHMMSS);
        instance.setValue(System.currentTimeMillis());
        assertNotNull(instance.getText());
    }

    @Test
    public void testConstructor3()
    {
        System.out.println("relTime");
        long start = System.currentTimeMillis();
        DateTimeRenderer instance = new DateTimeRenderer(HHMMSS, start);

        instance.setValue(start + 600000);
        String text = instance.getText();
        System.out.println(text);
        assertEquals("00:10:00", text);
    }

}
