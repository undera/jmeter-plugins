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
package kg.apc.jmeter.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.junit.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChooseRandomTest {

    public ChooseRandomTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        SampleResult previousResult = null;
        Sampler currentSampler = null;
        Collection<CompoundVariable> parameters = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            parameters.add(new CompoundVariable(String.valueOf(i)));

        }
        parameters.add(new CompoundVariable("4.3346"));
        parameters.add(new CompoundVariable("5.3346"));
        ChooseRandom instance = new ChooseRandom();
        instance.setParameters(parameters);
        String result1 = instance.execute(null, null);
        String result2 = instance.execute(null, null);
        Assert.assertTrue(!result1.equals(result2));
    }

    @Test
    public void testSetParameters() throws Exception {
        System.out.println("setParameters");
        Collection<CompoundVariable> parameters = new ArrayList<>();
        parameters.add(new CompoundVariable("1.256"));
        parameters.add(new CompoundVariable("4.3346"));
        parameters.add(new CompoundVariable("5.3346"));
        ChooseRandom instance = new ChooseRandom();
        instance.setParameters(parameters);
    }

    @Test
    public void testGetReferenceKey() {
        System.out.println("getReferenceKey");
        ChooseRandom instance = new ChooseRandom();
        String expResult = "__chooseRandom";
        String result = instance.getReferenceKey();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testGetArgumentDesc() {
        System.out.println("getArgumentDesc");
        ChooseRandom instance = new ChooseRandom();
        List result = instance.getArgumentDesc();
        Assert.assertEquals(2, result.size());
    }
}
