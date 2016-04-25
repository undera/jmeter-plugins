/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.jmstools;

import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.qpid.client.AMQAnyDestination;
import org.apache.qpid.client.AMQConnectionFactory;
import org.apache.qpid.url.AMQBindingURL;
import org.springframework.jms.core.JmsTemplate;

public class JmsUtil implements JavaSamplerClient{
	
	@Override
	public Arguments getDefaultParameters() {

		Arguments args = new Arguments();
		args.addArgument("connection.url", "");
		args.addArgument("binding.url", "");
		args.addArgument("message", "");
		args.addArgument("direction", "");
		args.addArgument("header.properties", "");
		args.addArgument("header.property.reference", "");
		args.addArgument("message.selector", "");
		args.addArgument("receive.timeout", "");
		return args;
	}
	
	@Override
	public SampleResult runTest(JavaSamplerContext ctx) {
		
		
		SampleResult result = new SampleResult();
		result.setContentType("plain/text");
		result.setDataType(SampleResult.TEXT);
		result.setDataEncoding(SampleResult.DEFAULT_HTTP_ENCODING);
		
		
		String connectionUrl = ctx.getParameter("connection.url");
		String bindingUrl = ctx.getParameter("binding.url");
		String message = ctx.getParameter("message");
		
		
		if(connectionUrl == null || "".equals(connectionUrl)){
			result.setSuccessful(false);
			result.setResponseMessage("Connection URL cannot be empty.");
			result.setResponseCode("0xDEAD");
		}else{
			if(bindingUrl == null || "".equals(bindingUrl)){
				result.setSuccessful(false);
				result.setResponseMessage("Binding URL cannot be empty.");
				result.setResponseCode("0xDEAD");
			}else{
				try{
					ConnectionFactory connectionFactory = new AMQConnectionFactory(connectionUrl);
					AMQBindingURL burl = new AMQBindingURL(bindingUrl);
					
					Destination destinationProducer = new AMQAnyDestination(burl);
					JmsTemplate sender = new JmsTemplate();
					sender.setConnectionFactory(connectionFactory);
					sender.setDefaultDestination(destinationProducer);
					BinaryMessageConverter bmc = new BinaryMessageConverter();
					sender.setMessageConverter(bmc);
					
					BinaryMessagepostProcessor postProcessor = new BinaryMessagepostProcessor();
					
					sender.setDeliveryMode(2);
					int rt = 30000;
					try{
					rt = Integer.valueOf(ctx.getParameter("receive.timeout"));
					}catch(Exception e){
					}
					
					sender.setReceiveTimeout(rt);
					
					String direction = ctx.getParameter("direction");
					if(direction == null || "".equals(direction)){
						direction = "send";
					}
					if(direction.toLowerCase().equals("send")){
						Map<String, String> mp = getMessageProperties(ctx.getParameter("header.properties"));
						postProcessor.setMessageProperties(mp);
						sender.convertAndSend((Object)message, postProcessor);
						result.setSuccessful(true);
						result.setResponseMessage("Message sent.");
					}else{
						if(direction.toLowerCase().equals("receive")){
							
							System.out.println("Receive");
							String messageSelector = ctx.getParameter("message.selector");
							System.out.println("Selector: " + messageSelector);
							Object obj = null;
							if(messageSelector != null && !"".equals(messageSelector)){
								obj = sender.receiveSelectedAndConvert(messageSelector);
							}else{
								obj = sender.receiveAndConvert();
							}
							
							if(obj != null){
								result.setSuccessful(true);
								result.setResponseData(obj.toString().getBytes());
								String paramName = ctx.getParameter("header.property.reference");
								if(paramName != null && !"".equals(paramName))
									JMeterUtils.setProperty(paramName, concatProperties(bmc.getMessageProperties()));
							}else{
								result.setSuccessful(false);
								result.setResponseData("Conection timeout".getBytes());
								
							}
							
						}else{
							result.setSuccessful(false);
							result.setResponseMessage("Unknown direction.");
							
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					result.setSuccessful(false);
					result.setResponseMessage("Exception");
					result.setResponseData(e.getMessage().getBytes());
				}
			}
		}
		
		return result;
	}
	
	private String concatProperties(Map<String, String> messageProperties) {
		String ret = "";
		if(messageProperties != null){
			for(String key : messageProperties.keySet()){
				ret += key + "=" + messageProperties.get(key) + "&&";
			}
		}
		return ret;
	}

	private Map<String, String> getMessageProperties(String parameter) {
		if(parameter != null && !"".equals(parameter)){
			Map<String, String> ret = new HashMap<String, String>();
			String[] pairs = parameter.split("&&");
			for(String pair : pairs){
				String[] parts = pair.split("=");
				if(parts.length >=2){
					ret.put(parts[0], parts[1]);
				}
			}
			return ret;
		}else{
			return new HashMap<String, String>();
		}

	}

	@Override
	public void setupTest(JavaSamplerContext arg0) {
	}
	
	@Override
	public void teardownTest(JavaSamplerContext arg0) {
	}
}
