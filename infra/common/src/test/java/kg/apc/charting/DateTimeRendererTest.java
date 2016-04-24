package kg.apc.charting;

import java.util.Calendar;

import org.junit.*;

import static org.junit.Assert.*;

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
        Assert.assertEquals("03:16:40", text);
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
        Assert.assertEquals("00:00:00", text);
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
        Assert.assertNotNull(instance.getText());
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
        Assert.assertEquals("00:10:00", text);
    }

}
