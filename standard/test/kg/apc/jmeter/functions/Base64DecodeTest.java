package kg.apc.jmeter.functions;

import junit.framework.TestCase;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Base64DecodeTest extends TestCase {
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    public void testExecute() throws Exception {
        JMeterContext context = JMeterContextService.getContext();
        context.setVariables(new JMeterVariables());

        Collection<CompoundVariable> parameters = new ArrayList<CompoundVariable>();
        parameters.add(new CompoundVariable("dGVzdCBzdHJpbmc="));
        parameters.add(new CompoundVariable("b64dec_res"));
        Base64Decode instance = new Base64Decode();
        instance.setParameters(parameters);

        String res = instance.execute(null, null);
        assertEquals("test string", res);
        assertNotNull(context.getVariables().get("b64dec_res"));
    }

    public void testGetReferenceKey() throws Exception {
        System.out.println("getReferenceKey");
        Base64Decode instance = new Base64Decode();
        String expResult = "__base64Decode";
        String result = instance.getReferenceKey();
        assertEquals(expResult, result);
    }

    public void testGetArgumentDesc() throws Exception {
        System.out.println("getArgumentDesc");
        Base64Decode instance = new Base64Decode();
        List result = instance.getArgumentDesc();
        assertEquals(2, result.size());
    }
}