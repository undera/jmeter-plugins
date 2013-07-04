package kg.apc.jmeter.config;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author APC
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestCsvFileActionTest.class, VariablesFromCSVTest.class, VariablesFromCSVFileBeanInfoTest.class, LockFileGuiTest.class, LockFileTest.class, VariableFromCsvFileReaderTest.class, VariablesFromCSVFileTest.class, VariablesFromCSVGuiTest.class})
public class ConfigSuite {

   @BeforeClass
   public static void setUpClass() throws Exception
   {
   }

   @AfterClass
   public static void tearDownClass() throws Exception
   {
   }

   @Before
   public void setUp() throws Exception
   {
   }

   @After
   public void tearDown() throws Exception
   {
   }

}