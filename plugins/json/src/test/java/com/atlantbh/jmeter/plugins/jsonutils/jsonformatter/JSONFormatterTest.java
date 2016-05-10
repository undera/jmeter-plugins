/*
 * Copyright 2013 undera.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.atlantbh.jmeter.plugins.jsonutils.jsonformatter;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.junit.Assert;
import org.junit.Test;

public class JSONFormatterTest {

    @Test
    public void testProcess_dict() {
        System.out.println("process dict");
        JSONFormatter instance = new JSONFormatter();
        JMeterContext threadContext = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        res.setResponseData("{\"a\":1,\"b\":2}", "UTF8");
        threadContext.setPreviousResult(res);
        instance.process();
        Assert.assertEquals("{\n    \"a\": 1,\n    \"b\": 2\n}", res.getResponseDataAsString());
    }

    @Test
    public void testProcess_array() {
        System.out.println("process array");
        JSONFormatter instance = new JSONFormatter();
        JMeterContext threadContext = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        res.setResponseData("[]", "UTF8");
        threadContext.setPreviousResult(res);
        instance.process();
    }

    @Test
    public void testProcess_string() {
        System.out.println("process str");
        JSONFormatter instance = new JSONFormatter();
        JMeterContext threadContext = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        res.setResponseData("\"str\"", "UTF8");
        threadContext.setPreviousResult(res);
        instance.process();
    }

    @Test
    public void testProcess_float() {
        System.out.println("process float");
        JSONFormatter instance = new JSONFormatter();
        JMeterContext threadContext = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        res.setResponseData("3.14", "UTF8");
        threadContext.setPreviousResult(res);
        instance.process();
    }

    @Test
    public void testProcess_null() {
        System.out.println("process null");
        JSONFormatter instance = new JSONFormatter();
        JMeterContext threadContext = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        res.setResponseData("null", "UTF8");
        threadContext.setPreviousResult(res);
        instance.process();
    }

    @Test
    public void testProcess_Failure() {
        System.out.println("process");
        JSONFormatter instance = new JSONFormatter();
        JMeterContext threadContext = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        res.setResponseData("<html>", "UTF8");
        threadContext.setPreviousResult(res);
        instance.process();
    }
}
