package kg.apc.jmeter.functions;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Base64EncodeTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
        JMeterContextService.getContext().setVariables(new JMeterVariables());
    }

    @Test
    public void testExecute() throws Exception {
        JMeterContext context = JMeterContextService.getContext();
        context.setVariables(new JMeterVariables());

        Collection<CompoundVariable> parameters = new ArrayList<>();
        parameters.add(new CompoundVariable("test string"));
        parameters.add(new CompoundVariable("b64enc_res"));
        Base64Encode instance = new Base64Encode();
        instance.setParameters(parameters);

        String res = instance.execute(null, null);
        Assert.assertEquals("dGVzdCBzdHJpbmc=", res);
        Assert.assertNotNull(context.getVariables().get("b64enc_res"));
    }

    @Test
    public void testGetReferenceKey() throws Exception {
        System.out.println("getReferenceKey");
        Base64Encode instance = new Base64Encode();
        String expResult = "__base64Encode";
        String result = instance.getReferenceKey();
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testGetArgumentDesc() throws Exception {
        System.out.println("getArgumentDesc");
        Base64Encode instance = new Base64Encode();
        List result = instance.getArgumentDesc();
        Assert.assertEquals(2, result.size());
    }
}