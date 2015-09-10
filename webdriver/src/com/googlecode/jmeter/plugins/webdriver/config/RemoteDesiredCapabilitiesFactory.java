package com.googlecode.jmeter.plugins.webdriver.config;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;


public class RemoteDesiredCapabilitiesFactory {
  public static DesiredCapabilities build(RemoteCapability capability){
	  DesiredCapabilities desiredCapabilities;
	  if(RemoteCapability.CHROME.equals(capability)){
		  ChromeOptions options = new ChromeOptions();
	      desiredCapabilities = DesiredCapabilities.chrome();
	      desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
		  return desiredCapabilities;
	  } else if (RemoteCapability.FIREFOX.equals(capability)){
		  FirefoxProfile profile = new FirefoxProfile();
		  desiredCapabilities = DesiredCapabilities.firefox();
		  desiredCapabilities.setCapability(FirefoxDriver.PROFILE, profile);
		  return desiredCapabilities;
	  } else if (RemoteCapability.INTERNET_EXPLORER.equals(capability)){
		  desiredCapabilities = DesiredCapabilities.internetExplorer();
		  return desiredCapabilities;
	  } else if (RemoteCapability.PHANTOMJS.equals(capability)){
		  desiredCapabilities = DesiredCapabilities.phantomjs();
		  return desiredCapabilities;
	  }
	  throw new IllegalArgumentException("No such capability");
  }
}
