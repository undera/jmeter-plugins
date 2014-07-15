package org.jmeterplugins.protocol.http.control;

import org.apache.jmeter.util.JMeterUtils;
import org.junit.*;

public class ServerRunnerTest {

    public ServerRunnerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testRun() throws InstantiationException, IllegalAccessException {
        System.out.println("run");
        ServerRunner.run(HttpSimpleTableServerEmul.class);
    }

    @Test
    public void testExecuteInstance() throws Exception {
        System.out.println("executeInstance");
        HttpSimpleTableServer serv = null;
        ServerRunner.executeInstance(serv);

        serv = new HttpSimpleTableServerEmul(-1, false, JMeterUtils.getJMeterBinDir());
        serv.start();
        ServerRunner.executeInstance(serv);
        serv.stopServer();
        serv = null;
    }

}
