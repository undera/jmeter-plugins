# Custom JMeter Functions

<span class=''>[<i class='fa fa-download'></i> Download](/?search=jpgc-functions)</span>

In addition to standard [JMeter functions](http://jakarta.apache.org/jmeter/usermanual/functions.html) we provide:

## chooseRandom<sup><font color=gray size="1">since 1.0.1</font></sup>

This function choose single random value from the list of its arguments. The 
last argument is not taken into choice, it is interpreted as variable name
to store the result.

Parameters:
  1. First value to choose from
  1. Second value to choose from
  1. ... any number of further choices ...
  1. Mandatory variable to store result

Example, choosing random color and assigning it to randomColor variable:

```
${__chooseRandom(red,green,blue,orange,violet,magenta,randomColor)}
```

## doubleSum<sup><font color=gray size="1">since 0.4.2</font></sup>

This function used to compute the sum of two or more floating point values.

Parameters:
  1. First value to sum - required
  1. Second value to sim - required
  1. More values to sum - optional
  1. Last argument - variable name to store the result

Example, returning 8.3 and also saving it to variable `sumVariable`:

```
${__doubleSum(3.5, 4.7, sumVariable)}
```

## env<sup><font color=gray size="1">since 1.2.0</font></sup>

This function used to get a value of environment variable. 
Returns value of environment variable if variable was defined, variable name instead (or default value) .

Parameters:
  1. First value is a name of environment variable - required
  1. Second argument - variable name to store the result - optional
  1. Third argument - default value if environment variable is not set - optional

Example, returning value of ENV_VAR and also saving it to variable someVariable (or defaultValue if ENV_VAR is not set):

```
${__env(ENV_VAR, someVariable, defaultValue)}
```

## isDefined<sup><font color=gray size="1">since 0.4.2</font></sup>

This function used to determine if variable was already defined. 
Returns 1 if variable was defined, 0 instead.

Parameters:
  1. First value is string constant, variable, or function call - required

Example, getting defined status for variable name 'testVar':

```
${__isDefined(testVar)}
```

## MD5<sup><font color=gray size="1">since 0.4.2</font></sup>

This function used to calculate MD5 hash of constant string or variable value.

Parameters:
  1. First value is string constant, variable, or function call - required
  1. Second argument - variable name to store the result

Example, calculating MD5 for 'test':

```
${__MD5(test)}
```

## base64Encode<sup><font color=gray size="1">since 1.2.0</font></sup>

This function used to encode a constant string or variable value using Base64 algorithm.

Parameters:
  1. First value is string constant, variable, or function call - required
  1. Second argument - variable name to store the result

Example, encoding 'test string':

```
${__base64Encode(test string)}
```

## base64Decode<sup><font color=gray size="1">since 1.2.0</font></sup>

This function used to decode a constant string or variable value from Base64 into string.

Parameters:
  1. First value is string constant, variable, or function call - required
  1. Second argument - variable name to store the result

Example, decoding that returns 'test string':

```
${__base64Decode(dGVzdCBzdHJpbmc=)}
```

## strLen<sup><font color=gray size="1">since 0.4.2</font></sup>

This function used to compute the length of constant string or variable value.

Parameters:
  1. First value is string constant, variable, or function call - required
  1. Second argument - variable name to store the result

Example, returning 11 and also saving it to variable `lenVariable`:

```
${__strLen(test string, lenVariable)}
```

Example, returning length of variable `varName`:

```
${__strLen(${varName})}
```

## substring<sup><font color=gray size="1">since 0.4.2</font></sup>

This function is wrapper for Java [String.substring](http://download.oracle.com/javase/6/docs/api/java/lang/String.html?is-external=true#substring) method.

Parameters:
  1. First value is string constant, variable, or function call - required
  1. Begin index
  1. End index
  1. Optional variable to store result

Example, getting 'str' from 'test string':

```
${__substring(test string, 5, 8)}
```

## strReplace<sup><font color=gray size="1">since 1.4.0</font></sup>

This function is wrapper for Java [String.replace](http://docs.oracle.com/javase/7/docs/api/java/lang/String.html#replace(java.lang.CharSequence,%20java.lang.CharSequence)) method.

Parameters:
  1. First value is string constant, variable, or function call - required
  1. Search substring
  1. Replacement
  1. Optional variable to store result

Example, getting 'banana dog orange' from 'banana apple orange':

```
${__strReplace(banana apple orange,apple,dog)}
```

## strReplaceRegex<sup><font color=gray size="1">since 1.4.0</font></sup>

This function is wrapper for Java [String.replaceAll](http://docs.oracle.com/javase/7/docs/api/java/lang/String.html#replaceAll(java.lang.String,%20java.lang.String)) method.

Parameters:
  1. First value is string constant, variable, or function call - required
  1. Search regular expression
  1. Replacement (can contain references to matched string parts, see [Matcher.replaceAll](http://docs.oracle.com/javase/7/docs/api/java/util/regex/Matcher.html#replaceAll(java.lang.String)))
  1. Optional variable to store result

Example, getting `banana-apple-orange` from `banana apple orange`:

```
${__strReplaceRegex(banana apple orange,' ',-)}
```

## uppercase and lowercase<sup><font color=gray size="1">since 0.4.2</font></sup>

This functions used to transform the case of constant string or variable value.

Parameters:
  1. First value is string constant, variable, or function call - required
  1. Second argument - variable name to store the result

Example, transforming 'test' into 'TEST' and also saving it to variable `someVariable`:

```
${__uppercase(test, someVariable)}
```

## iterationNum<sup><font color=gray size="1">since 1.2.1</font></sup>

Function returns the number of current iteration in thread group.

Function has no parameters.

## if<sup><font color=gray size="1">since 1.3.0</font></sup>

This function provides a == b ? a : b syntax.

Parameters:
  1. Actual value
  1. Expected value
  1. if condition true value
  1. if condition false value
  1. Optional variable to store result

Example, gettig different values for jmeter variable:

```
${__if(${size}, 5, ok, invalid)}
```

## caseFormat<sup><font color=gray size="1">since 2.0</font></sup>

This function provides changing string case format.

Parameters:
  1. Original string value  - required
  1. Expected case mode (case insenstive), Default is LOWER_CAMEL_CASE (if not valid mode will return string as is)     
  1. Optional variable to store result

Example, save in myString Variable value "my-string"

${__caseFormat("my String", "LOWER_HYPHEN", myString);

Case modes available:

1. LOWER_CAMEL_CASE - Lower camel case, output as: lowerCamelCase 

2. UPPER_CAMEL_CASE - Upper camel case, output as: UpperCamelCase 

3. SNAKE_CASE/LOWER_UNDERSCORE Snake or lower underscore case, output as: snake_lower_underscore_case

4. KEBAB_CASE/LISP_CASE/SPINAL_CASE/LOWER_HYPHEN - Lisp or kebab or spinal or lower hyphen case, output as: lis-keba-spinal-lower-case

5. TRAIN_CASE - Train case, output as: TRAIN-CASE

6. UPPER_UNDERSCORE - Upper underscore case, output as: UPPER_UNDERSCORE_CASE