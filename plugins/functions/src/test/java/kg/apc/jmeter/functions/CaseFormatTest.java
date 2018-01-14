package kg.apc.jmeter.functions;

import static org.junit.Assert.assertEquals;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.*;

import java.util.Collection;
import java.util.LinkedList;
/**
 * 
 * Test {@link CaseFormat} Function
 * 
 * @see CaseFormat
 *
 */
public class CaseFormatTest {
	
	private SampleResult result;

    private Collection<CompoundVariable> params;

	private CaseFormat changeCase;

    @Before
    public void setUp() {
    	changeCase = new CaseFormat();
        result = new SampleResult();
        JMeterContext jmctx = JMeterContextService.getContext();
        String data = "dummy data";
        result.setResponseData(data, null);
        JMeterVariables vars = new JMeterVariables();
        jmctx.setVariables(vars);
        jmctx.setPreviousResult(result);
        params = new LinkedList<>();
    }

    @Test
    public void testLowerCaseOnlyMandatory() throws Exception {
    	params.add(new CompoundVariable("ab-àÀè_È é É ù Ù ì Ì ò Ò ñ ÑäöüßCD   eF"));
    	changeCase.setParameters(params);
    	changeCase.setParameters(params);
    	String returnValue = changeCase.execute(result, null);
    	assertEquals("abÀàèÈÉÉÙÙÌÌÒÒÑÑäöüßcdEf", returnValue);
    }
    
    @Test
    public void testInvalidCase() throws Exception {
    	params.add(new CompoundVariable("ab- eF"));
    	params.add(new CompoundVariable("INVALID_CASE"));
    	changeCase.setParameters(params);
    	changeCase.setParameters(params);
    	String returnValue = changeCase.execute(result, null);
    	assertEquals("ab- eF", returnValue);
    }
    
    @Test
    public void testDefaultCase() throws Exception {
    	params.add(new CompoundVariable("ab- eF"));
    	params.add(new CompoundVariable("LOWER_CAMEL_CASE"));
    	changeCase.setParameters(params);
    	changeCase.setParameters(params);
    	String returnValue = changeCase.execute(result, null);
    	assertEquals("abEf", returnValue);
    }
    
    @Test
    public void testLowerCaseNonEnglish() throws Exception {
    	params.add(new CompoundVariable("ab-àÀè_È é É ù Ù ì Ì ò Ò ñ ÑäöüßCD   eF"));
    	params.add(new CompoundVariable("LOWER_CAMEL_CASE"));
    	changeCase.setParameters(params);
    	changeCase.setParameters(params);
    	String returnValue = changeCase.execute(result, null);
    	assertEquals("abÀàèÈÉÉÙÙÌÌÒÒÑÑäöüßcdEf", returnValue);
    }
    
    @Test
    public void testUpperCaseNonEnglish() throws Exception {
    	params.add(new CompoundVariable("ab-àÀè_È é É ù Ù ì Ì ò Ò ñ ÑäöüßCD   eF"));
    	params.add(new CompoundVariable("UPPER_CAMEL_CASE"));
    	changeCase.setParameters(params);
    	changeCase.setParameters(params);
    	String returnValue = changeCase.execute(result, null);
    	assertEquals("AbÀàèÈÉÉÙÙÌÌÒÒÑÑäöüßcdEf", returnValue);
    }
    
    @Test
    public void testChangeCaseKebabCase() throws Exception {
    	params.add(new CompoundVariable("ab-àÀè_È é É ù Ù ì Ì ò Ò ñ ÑäöüßCD   eF"));
    	params.add(new CompoundVariable("KEBAB_CASE"));
    	changeCase.setParameters(params);
    	changeCase.setParameters(params);
    	String returnValue = changeCase.execute(result, null);
    	assertEquals("ab-ààè-è-é-é-ù-ù-ì-ì-ò-ò-ñ-ñäöüßcd-ef", returnValue);
    }
    
    @Test
    public void testChangeCaseSnakeCase() throws Exception {
    	params.add(new CompoundVariable("ab-àÀè_È é É ù Ù ì Ì ò Ò ñ ÑäöüßCD   eF"));
    	params.add(new CompoundVariable("SNAKE_CASE"));
    	changeCase.setParameters(params);
    	changeCase.setParameters(params);
    	String returnValue = changeCase.execute(result, null);
    	assertEquals("ab_ààè_è_é_é_ù_ù_ì_ì_ò_ò_ñ_ñäöüßcd_ef", returnValue);
    }
    
	@Test
    public void testChangeCaseTrainCase() throws Exception {
    	params.add(new CompoundVariable("ab-àÀè_È é É ù Ù ì Ì ò Ò ñ ÑäöüßCD   eF"));
    	params.add(new CompoundVariable("TRAIN_CASE"));
    	changeCase.setParameters(params);
    	changeCase.setParameters(params);
    	String returnValue = changeCase.execute(result, null);
    	assertEquals("AB-ÀÀÈ-È-É-É-Ù-Ù-Ì-Ì-Ò-Ò-Ñ-ÑÄÖÜSSCD-EF", returnValue);
    }
}
