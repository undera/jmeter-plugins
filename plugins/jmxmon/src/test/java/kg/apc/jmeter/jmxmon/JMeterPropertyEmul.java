package kg.apc.jmeter.jmxmon;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.JMeterProperty;

public class JMeterPropertyEmul implements JMeterProperty {

	private String value;
	
	
	public JMeterPropertyEmul(String value) {
		super();
		this.value = value;
	}

	@Override
	public int compareTo(JMeterProperty o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JMeterProperty clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getBooleanValue() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getDoubleValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloatValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIntValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLongValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObjectValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringValue() {
		return value;
	}

	@Override
	public boolean isRunningVersion() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void mergeIn(JMeterProperty arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recoverRunningVersion(TestElement arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setName(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectValue(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRunningVersion(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

}
