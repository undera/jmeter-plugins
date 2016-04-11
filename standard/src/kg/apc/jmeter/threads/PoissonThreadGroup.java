package kg.apc.jmeter.threads;


import java.util.ArrayList;
import java.util.Random;
import java.io.Serializable;

import org.apache.log.Logger;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jorphan.logging.LoggingManager;

public class PoissonThreadGroup
        extends AbstractSimpleThreadGroup 
        implements Serializable{

    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String THREAD_LAMBDA = "Lambda";
    private static final String RAMPUP = "rampUp";
    private static final String RANDOMSEED = "Random";
    private static Random oRandom = new Random();
    private static ArrayList<Long> threadStartTimes = new ArrayList<Long>();
    private int threadsToSchedule = 0;
  
    public PoissonThreadGroup() {
        super();
        threadsToSchedule = 0;
    }

    public String getLambda() {
        return getPropertyAsString(THREAD_LAMBDA);
    }

    public void setLambda (String lambdaVal) {
        setProperty(THREAD_LAMBDA, lambdaVal);
    }
    
    public void setRandomSeed(String randomSeed) {
    	setProperty(RANDOMSEED, randomSeed);
    }
    
    public String getRandomSeed() {
        return getPropertyAsString(RANDOMSEED);
    }
    
    public String getRampUp() {
        return getPropertyAsString(RAMPUP);
    }

    public void setRampUp(String delay) {
        setProperty(RAMPUP, delay);
    }
    
    //Generates the schedule of threads and the time at which they will execute. 
    protected ArrayList<Long> getLambdaThreadSchedules()
    {
    	threadStartTimes = new ArrayList<Long>();
    	try
    	{
	    	long rampUp = Long.parseLong(getRampUp());
	    	double lambdaRate = Double.parseDouble(getLambda());
	    	long rndSeed = Long.parseLong(getRandomSeed());
	    	oRandom.setSeed(rndSeed);
	    	long currentTime = System.currentTimeMillis();
	    	long totalTime = currentTime + (rampUp * 1000);
	    	long TotalDelaySinceFirst = 0;
	    	
	    	if (lambdaRate > 0)
	    	{
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
	    	}
	    	return threadStartTimes;
    	}
    	catch (Exception ex)
    	{
    		log.error("getLambdaThreadSchedules: " + ex.getMessage());
    	}
		return threadStartTimes;
    }
    
	@Override
	protected void scheduleThread(JMeterThread thread, long tgstartTime) {
		//log.debug("Scheduling thread: " + thread.getThreadName() + "\t startTime \t" + tgstartTime);
		if (threadsToSchedule <= threadStartTimes.size())
		{
			long startTime = tgstartTime + threadStartTimes.get(threadsToSchedule);
			thread.setStartTime(startTime);
			thread.setScheduled(true);
			threadsToSchedule++;
		}
	}
}