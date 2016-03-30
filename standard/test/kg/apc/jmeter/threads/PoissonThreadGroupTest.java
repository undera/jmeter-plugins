package kg.apc.jmeter.threads;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Random;

import kg.apc.emulators.TestJMeterUtils;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jorphan.collections.HashTree;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class PoissonThreadGroupTest {
    /**
     *
     */
    public PoissonThreadGroupTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }
    
    /**
     * Test of getLambda method, of class PoissonThreadGroup.
     */
    @Test
    public void testgetLambda() {
        System.out.println("getLambda");
        PoissonThreadGroup instance = new PoissonThreadGroup();
        String expResult = "";
        String result = instance.getLambda();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of setRandomSeed method, of class PoissonThreadGroup.
     */
    @Test
    public void testsetLambda() {
        System.out.println("setLambda");
        String execute = "";
        PoissonThreadGroup instance = new PoissonThreadGroup();
        instance.setLambda(execute);
    }
    
    /**
     * Test of getRandomSeed method, of class PoissonThreadGroup.
     */
    @Test
    public void testgetRandomSeed() {
        PoissonThreadGroup instance = new PoissonThreadGroup();
        String expResult = "";
        String result = instance.getRandomSeed();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of setRandomSeed method, of class PoissonThreadGroup.
     */
    @Test
    public void testsetRandomSeed() {
        String execute = "";
        PoissonThreadGroup instance = new PoissonThreadGroup();
        instance.setRandomSeed(execute);
    }

    /**
     * Test of setRampUp method, of class PoissonThreadGroup.
     */
    @Test
    public void testsetRampUp() {
        System.out.println("setRampUp");
        String execute = "";
        PoissonThreadGroup instance = new PoissonThreadGroup();
        instance.setRampUp(execute);
    }
    
    /**
     * Test of getRampUp method, of class PoissonThreadGroup.
     */
    @Test
    public void testgetRampUp() {
        System.out.println("getRampUp");
        PoissonThreadGroup instance = new PoissonThreadGroup();
        String expResult = "";
        String result = instance.getRampUp();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of scheduleThread method, of class PoissonThreadGroup.
     */
    @Test
    public void testScheduleThread() {
        HashTree hashtree = new HashTree();
        hashtree.add(new LoopController());
        JMeterThread thread = new JMeterThread(hashtree, null, null);
        PoissonThreadGroup instance = new PoissonThreadGroup();
        instance.setRampUp("10");
        instance.setLambda("1");
        instance.setRandomSeed("1");
        
        long s2;
        ArrayList<Long> lambdaSchedule =instance.getLambdaThreadSchedules();
        int numThreads = lambdaSchedule.size();
		for (int n = 0; n < numThreads; n++) {
	    	thread.setThreadNum(n);
	        thread.setThreadName(Integer.toString(n));
	        instance.scheduleThread(thread);
	        s2 =thread.getStartTime();
	        assertTrue(s2 >= 0);
	    }
    }
    
    /**
     * Test of getLambdaThreadSchedules method, of class PoissonThreadGroup.
     */
    @Test
    public void testgetLambdaThreadSchedules() {
        ArrayList<Long> threadStartTimes = new ArrayList<Long>();
        
		Random oRandom = new Random();
        PoissonThreadGroup instance = new PoissonThreadGroup();
		instance.setRampUp("10");
        instance.setLambda("1");
        instance.setRandomSeed("1");
        
    	long rampUp = Long.parseLong(instance.getRampUp());
    	double lambdaRate = Double.parseDouble(instance.getLambda());
    	long rndSeed = Long.parseLong(instance.getRandomSeed());
    	oRandom.setSeed(rndSeed);
    	long currentTime = System.currentTimeMillis();
    	long totalTime = currentTime + (rampUp * 1000);
    	long TotalDelaySinceFirst = 0;
	
    	while (currentTime < totalTime)
    	{
    		double nextTime = -Math.log(1.0 - oRandom.nextDouble()) / lambdaRate;
    		long nextTimeinMilli =  (long) (nextTime * 1000);
    		if ((currentTime + nextTimeinMilli) < totalTime)
    		{
    			currentTime += nextTimeinMilli;
    			TotalDelaySinceFirst += nextTimeinMilli;
    			threadStartTimes.add(TotalDelaySinceFirst);
    		}
    		else
    		{
    			break;
    		}
    	}
        assertTrue(threadStartTimes.size() >= 7);
    }
}
