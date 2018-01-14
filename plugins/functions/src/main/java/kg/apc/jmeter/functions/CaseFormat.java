package kg.apc.jmeter.functions;

import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Case Formating Function
 * 
 * Support String manipulations of:
 * <ul>
 * <li>Lower and upper camel case</li>
 * <li>as: lowerCamelCase and UpperCamelCase</li>
 * 
 * <li>Snake/lower underscore case</li>
 * <li>as: snake_lower_underscore_case</li>
 * 
 * <li>Lisp/kebab/spinal/lower hyphen case</li>
 * <li>as: lis-keba-spinal-lower-case</li>
 * 
 * <li>upper underscore case</li>
 * <li>as: UPPER_UNDERSCORE_CASE</li>
 * 
 * <li>Train case</li>
 * <li>as: TRAIN-CASE</li>
 * <li></li>
 * </ul>
 * 
 * Support similar to Guava case options
 * https://google.github.io/guava/releases/19.0/api/docs/com/google/common/base/CaseFormat.html
 * 
 * Support similar to Stack Exchange answers
 * https://softwareengineering.stackexchange.com/questions/104468/if-this-is-camelcase-what-is-this
 * 
 */
public class CaseFormat extends AbstractFunction {

	private static final String CHANGE_CASE_MODE = "Change case mode"; //$NON-NLS-1$
	private static final String STRING_TO_MODIFY = "String to modify"; //$NON-NLS-1$
	private static final String HYPHEN_MINUS = "-"; //$NON-NLS-1$
	private static final String UNDERSCORE = "_"; //$NON-NLS-1$
	private static final Pattern NOT_ALPHANUMERIC_REGEX = Pattern.compile("[\\s\\-_]+"); //$NON-NLS-1$
	private static final Logger LOGGER = LoggerFactory.getLogger(CaseFormat.class);
	private static final List<String> DESC = new LinkedList<>();
	private static final String KEY = "__caseFormat"; //$NON-NLS-1$

	private static final int MIN_PARAMETER_COUNT = 1;
	private static final int MAX_PARAMETER_COUNT = 3;
	static {
		DESC.add(STRING_TO_MODIFY);
		DESC.add(CHANGE_CASE_MODE);
		DESC.add(JMeterUtils.getResString("function_name_paropt")); //$NON-NLS-1$
	}

	private CompoundVariable[] values;

	@Override
	public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
		String originalString = values[0].execute();
		String mode = null; // default
		if (values.length > 1) {
			mode = values[1].execute();
		}
		if (StringUtils.isEmpty(mode)) {
			mode = CaseFormatMode.LOWER_CAMEL_CASE.getName(); // default
		}
		String targetString = changeCase(originalString, mode);
		if (values.length > 2) {
			addVariableValue(targetString, values[2].execute().trim());
		}
		return targetString;
	}

	/**
	 * Change case options
	 * 
	 * @param originalString
	 * @param mode
	 * @return string after change case
	 */
	protected String changeCase(String originalString, String mode) {
		String targetString = originalString;
		// mode is case insensitive, allow upper for example
		CaseFormatMode changeCaseMode = CaseFormatMode.typeOf(mode.toUpperCase());
		if (changeCaseMode != null) {
			switch (changeCaseMode) {
			case LOWER_CAMEL_CASE:
				targetString = camelFormat(originalString, false);
				break;
			case UPPER_CAMEL_CASE:
				targetString = camelFormat(originalString, true);
				break;
			case SNAKE_CASE:
			case LOWER_UNDERSCORE:
				targetString = caseFormatWithDelimiter(originalString, UNDERSCORE, false, true);
				break;
			case KEBAB_CASE:
			case LISP_CASE:
			case SPINAL_CASE:
			case LOWER_HYPHEN:
				targetString = caseFormatWithDelimiter(originalString, HYPHEN_MINUS, false, true);
				break;
			case TRAIN_CASE:
				targetString = caseFormatWithDelimiter(originalString, HYPHEN_MINUS, true, false);
				break;
			case UPPER_UNDERSCORE:
				targetString = caseFormatWithDelimiter(originalString, UNDERSCORE, true, false);
				break;
			default:
				// default not doing nothing to originalString
			}
		} else {
			LOGGER.error("Unknown mode {}, returning {} unchanged", mode, targetString);
		}
		return targetString;
	}

	@Override
	public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
		checkParameterCount(parameters, MIN_PARAMETER_COUNT, MAX_PARAMETER_COUNT);
		values = parameters.toArray(new CompoundVariable[parameters.size()]);
	}

	@Override
	public String getReferenceKey() {
		return KEY;
	}

	@Override
	public List<String> getArgumentDesc() {
		return DESC;
	}

	/**
	 * Camel format string
	 * 
	 * @param str
	 * @param isFirstCapitalized
	 * @return String in camelFormat
	 */
	private static String camelFormat(String str, boolean isFirstCapitalized) {
		StringBuilder builder = new StringBuilder(str.length());
		String[] tokens = NOT_ALPHANUMERIC_REGEX.split(str);
		for (int i = 0; i < tokens.length; i++) {
			String lowerCased = StringUtils.lowerCase(tokens[i]);
			if (i == 0) {
				builder.append(isFirstCapitalized ? StringUtils.capitalize(lowerCased) : lowerCased);
			} else {
				builder.append(StringUtils.capitalize(lowerCased));
			}
		}
		return builder.toString();
	}

	/**
	 * Change case using delimiter between words
	 * 
	 * @param str
	 * @param delimiter
	 * @param isAllUpper
	 * @param isAllLower
	 * @return string after change case
	 */
	private static String caseFormatWithDelimiter(String str, String delimiter, boolean isAllUpper, boolean isAllLower) {
		StringBuilder builder = new StringBuilder(str.length());
		String[] tokens = NOT_ALPHANUMERIC_REGEX.split(str);
		boolean shouldAddDelimiter = StringUtils.isNotEmpty(delimiter);
		for (int i = 0; i < tokens.length; i++) {
			String currentToken = tokens[i];
			builder.append(currentToken);
			boolean hasNextToken = i + 1 != tokens.length;
			if (hasNextToken && shouldAddDelimiter) {
				builder.append(delimiter);
			}
		}
		String outputString = builder.toString();
		if (isAllLower) {
			return StringUtils.lowerCase(outputString);
		} else if (isAllUpper) {
			return StringUtils.upperCase(outputString);
		}
		return outputString;
	}

	/**
	 * ChangeCase Modes
	 * 
	 * Modes for different cases
	 *
	 */
	public enum CaseFormatMode {
		UPPER_CAMEL_CASE("UPPER_CAMEL_CASE"), LOWER_CAMEL_CASE("LOWER_CAMEL_CASE"), SNAKE_CASE("SNAKE_CASE"), LISP_CASE("LISP_CASE"), KEBAB_CASE("KEBAB_CASE"), SPINAL_CASE(
				"SPINAL_CASE"), LOWER_HYPHEN("LOWER_HYPHEN"), LOWER_UNDERSCORE("LOWER_UNDERSCORE"), UPPER_UNDERSCORE("UPPER_UNDERSCORE"), TRAIN_CASE("TRAIN_CASE"); //$NON-NLS-1$

		private String mode;

		private CaseFormatMode(String mode) {
			this.mode = mode;
		}

		public String getName() {
			return this.mode;
		}

		/**
		 * Get CamelCaseMode by mode
		 * 
		 * @param mode
		 * @return relevant CamelCaseMode
		 */
		public static CaseFormatMode typeOf(String mode) {
			EnumSet<CaseFormatMode> caseFormatModes = EnumSet.allOf(CaseFormatMode.class);
			for (CaseFormatMode caseFormatMode : caseFormatModes) {
				if (caseFormatMode.getName().equals(mode)) {
					return caseFormatMode;
				}
			}
			return null;
		}
	}

	/**
	 * Store value in a variable
	 * 
	 * @param value value of variable to update
	 * @param String variable name
	 */
	private final void addVariableValue(String value, String variableName) {
		if (StringUtils.isNotEmpty(variableName)) {
			JMeterVariables vars = getVariables();
			if (vars != null) {
				vars.put(variableName, value);
			}
		}
	}
}
