package kg.apc.jmeter;

import kg.apc.emulators.TestJMeterUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PluginsCMDWorkerTest {

    private PluginsCMDWorker instance;
    private final String basedir;

    public PluginsCMDWorkerTest() {
        String file = this.getClass().getResource("short.jtl").getPath();
        basedir = file.substring(0, file.lastIndexOf("/"));
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Before
    public void setUp() {
        instance = new PluginsCMDWorker();
    }

    @Test
    public void testAddExportMode() {
        System.out.println("addExportMode");
        int mode = 0;
        instance.addExportMode(mode);
    }

    @Test
    public void testSetInputFile() {
        System.out.println("setInputFile");
        String string = "";
        instance.setInputFile(string);
    }

    @Test
    public void testSetOutputCSVFile() {
        System.out.println("setOutputCSVFile");
        String string = "";
        instance.setOutputCSVFile(string);
    }

    @Test
    public void testSetOutputPNGFile() {
        System.out.println("setOutputPNGFile");
        String string = "";
        instance.setOutputPNGFile(string);
    }

    @Test
    public void testSetPluginType() {
        System.out.println("setPluginType");
        String string = "ResponseTimesOverTime";
        instance.setPluginType(string);
    }

    @Test
    public void testDoJob() throws IOException {
        System.out.println("doJob 1");
        instance.setInputFile(basedir + "/short.jtl");
        File pngfile = File.createTempFile("test", ".png");
        instance.setOutputPNGFile(pngfile.getAbsolutePath());
        File csvfile = File.createTempFile("test", ".csv");
        instance.setOutputCSVFile(csvfile.getAbsolutePath());
        instance.setPluginType("ResponseTimesOverTime");
        instance.addExportMode(PluginsCMDWorker.EXPORT_PNG);
        instance.addExportMode(PluginsCMDWorker.EXPORT_CSV);
        int result = instance.doJob();
        int expResult = 0;
        assertEquals(expResult, result);
        System.out.println(csvfile.length());
        System.out.println(pngfile.length());
        assertTrue(110 < csvfile.length()); // win/linux diff
        assertTrue(16000 < pngfile.length()); // win/linux different
    }

    @Test
    public void testDoJob_png() throws IOException {
        System.out.println("doJob 2");
        instance.setInputFile(basedir + "/short.jtl");
        instance.setOutputPNGFile(File.createTempFile("test", ".png").getAbsolutePath());
        instance.setPluginType("ResponseTimesOverTime");
        instance.addExportMode(PluginsCMDWorker.EXPORT_PNG);
        int result = instance.doJob();
        int expResult = 0;
        assertEquals(expResult, result);
    }

    @Test
    public void testDoJob_csv() throws IOException {
        System.out.println("doJob 3");
        instance.setInputFile(basedir + "/short.jtl");
        instance.setOutputCSVFile(File.createTempFile("test", ".csv").getAbsolutePath());
        instance.setPluginType("ResponseTimesOverTime");
        instance.addExportMode(PluginsCMDWorker.EXPORT_CSV);
        int result = instance.doJob();
        int expResult = 0;
        assertEquals(expResult, result);
    }

    @Test
    public void testDoJob_csv_createdir() throws IOException {
        System.out.println("doJob 4");
        instance.setInputFile(basedir + "/short.jtl");
        instance.setOutputCSVFile(TestJMeterUtils.getTempDir().concat(File.separator).concat("testFile.csv"));
        instance.setPluginType("ResponseTimesOverTime");
        instance.addExportMode(PluginsCMDWorker.EXPORT_CSV);
        int result = instance.doJob();
        int expResult = 0;
        assertEquals(expResult, result);
    }

    @Test
    public void testDoJob_csv_filtered() throws IOException {
        System.out.println("doJob fil1");
        File csvfile = File.createTempFile("test", ".csv");
        //PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setInputFile(basedir + "/short.jtl");
        instance.setOutputCSVFile(csvfile.getAbsolutePath());
        instance.setPluginType("ResponseTimesOverTime");
        instance.addExportMode(PluginsCMDWorker.EXPORT_CSV);
        instance.setIncludeLabels("test");
        int result = instance.doJob();
        int expResult = 0;
        assertEquals(expResult, result);
        //windows = 22, linux = 13
        assertTrue(csvfile.length() == 13 || csvfile.length() == 14);
    }

    /**
     * Test of setGraphWidth method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetGraphWidth() {
        System.out.println("setGraphWidth");
        int i = 0;
        //PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setGraphWidth(i);
    }

    /**
     * Test of setGraphHeight method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetGraphHeight() {
        System.out.println("setGraphHeight");
        int i = 0;
        instance.setGraphHeight(i);
    }

    @Test
    public void testSetAggregate() {
        System.out.println("setAggregate");
        //PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setAggregate(0);
    }

    /**
     * Test of setZeroing method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetZeroing() {
        System.out.println("setZeroing");
        int logicValue = 0;
        //PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setZeroing(logicValue);
    }

    /**
     * Test of setPreventOutliers method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetPreventOutliers() {
        System.out.println("setPreventOutliers");
        int logicValue = 0;
        //PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setPreventOutliers(logicValue);
    }

    /**
     * Test of setRowsLimit method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetRowsLimit() {
        System.out.println("setRowsLimit");
        int parseInt = 0;
        //PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setRowsLimit(parseInt);
    }

    /**
     * Test of setForceY method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetForceY() {
        System.out.println("setForceY");
        int parseInt = 0;
        //PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setForceY(parseInt);
    }

    /**
     * Test of setHideLowCounts method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetHideLowCounts() {
        System.out.println("setHideLowCounts");
        int parseInt = 0;
        //PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setHideLowCounts(parseInt);
    }

    /**
     * Test of setGranulation method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetGranulation() {
        System.out.println("setGranulation");
        int parseInt = 0;
        //PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setGranulation(parseInt);
    }

    /**
     * Test of setRelativeTimes method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetRelativeTimes() {
        System.out.println("setRelativeTimes");
        int logicValue = 0;
        //PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setRelativeTimes(logicValue);
    }

    /**
     * Test of setGradient method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetGradient() {
        System.out.println("setGradient");
        int logicValue = 0;
        //PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setGradient(logicValue);
    }

    /**
     * Test of setIncludeLabels method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetIncludeLabels() {
        System.out.println("setIncludeLabels");
        String string = "";
        instance.setIncludeLabels(string);
    }

    /**
     * Test of setExcludeLabels method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetExcludeLabels() {
        System.out.println("setExcludeLabels");
        String string = "";
        instance.setExcludeLabels(string);
    }

    /**
     * Test of setIncludeSamplesWithRegex method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetIncludeSamplesWithRegex() {
        System.out.println("setIncludeSamplesWithRegex");
        int logicValue = 0;
        instance.setIncludeSamplesWithRegex(logicValue);
    }

    /**
     * Test of setExcludeSamplesWithRegex method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetExcludeSamplesWithRegex() {
        System.out.println("setExcludeSamplesWithRegex");
        int logicValue = 0;
        instance.setExcludeSamplesWithRegex(logicValue);
    }

    /**
     * Test of setStartOffset method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetStartOffset() {
        System.out.println("setStartOffset");
        String string = "";
        instance.setStartOffset(string);
    }

    /**
     * Test of setEndOffset method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetEndOffset() {
        System.out.println("setEndOffset");
        String string = "";
        instance.setEndOffset(string);
    }

    /**
     * Test of setAutoScaleRows method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetAutoScaleRows() {
        System.out.println("setAutoScaleRows");
        int logicValue = 0;
        instance.setAutoScaleRows(logicValue);
    }

    /**
     * Test of setLineWeight method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetLineWeight() {
        System.out.println("setLineWeight");
        float parseInt = 0.0F;
        instance.setLineWeight(parseInt);
    }

    /**
     * Test of setSuccessFilter method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetSuccessFilter() {
        System.out.println("setSuccessFilter");
        int logicValue = 0;
        PluginsCMDWorker instance = new PluginsCMDWorker();
        instance.setSuccessFilter(logicValue);
    }

    @Test
    public void testGetJMeterHomeFromCP() {
        System.out.println("getJMeterHomeFromCP");
        String classpathLinuxSTR = ":/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_jms.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_jdbc.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/hbase-0.90.1-cdh3u0.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/jackson-core-asl-1.5.2.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/json-lib-2.4-jdk15.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_core.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_report.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/jmeter-plugins-1.0.1-RC1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_monitors.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_components.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_native.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/hadoop-core-0.20.2-cdh3u0.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_functions.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_junit.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_ftp.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_ldap.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ezmorph-1.0.6.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_java.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_http.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/qpid-client-0.12.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/spring-core-2.5.3.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/dnsjava-2.0.6.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/CMDRunner.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/commons-beanutils-1.8.0.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/jackson-mapper-asl-1.5.2.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/json-smart-1.1.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/spring-jms-2.5.3.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/qpid-common-0.12.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/zookeeper-3.3.3-cdh3u0.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_mail.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/json-path-0.8.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/ext/ApacheJMeter_tcp.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/htmlparser-2.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/commons-collections-3.2.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/httpclient-4.2.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/xercesImpl-2.9.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/activation-1.1.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/xmlpull-1.1.3.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/htmllexer-2.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/commons-lang-2.6.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/jtidy-r938.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/excalibur-datasource-1.1.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/jdom-1.1.2.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/commons-io-2.2.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/commons-jexl-2.1.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/rhino-1.7R3.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/jodd-core-3.4.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/httpcore-4.2.2.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/commons-jexl-1.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/commons-httpclient-3.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/tika-core-1.3.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/httpclient-4.2.3.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/tika-parsers-1.3.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/xml-apis-1.3.04.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/bshclient.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/httpmime-4.2.3.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/jsoup-1.7.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/serializer-2.7.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/geronimo-jms_1.1_spec-1.1.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/httpcore-4.2.3.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/xalan-2.7.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/commons-logging-1.1.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/junit-4.10.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/commons-net-3.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/httpmime-4.2.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/slf4j-api-1.7.2.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/bsh-2.0b5.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/commons-codec-1.6.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/logkit-2.0.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/xmlgraphics-commons-1.3.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/excalibur-pool-1.2.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/xstream-1.4.2.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/avalon-framework-4.1.4.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/soap-2.3.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/bsf-api-3.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/commons-lang3-3.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/js-1.7R2.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/excalibur-logger-1.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/rhino-1.7R4.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/xpp3_min-1.1.4c.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/mail-1.4.4.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/excalibur-instrument-1.0.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/bsf-2.4.0.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/jcharts-0.7.5.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/oro-2.0.8.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/jodd-lagarto-3.4.1.jar:/home/undera/NetBeansProjects/JMeter/trunk/lib/jorphan.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/jmeter-plugins-1.0.1-RC1.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/CMDRunner.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/original-jmeter-plugins-1.0.1-RC1.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/qpid-client-0.20.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/hbase-0.94.5.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/json-lib-2.4-jdk15.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/qpid-common-0.20.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/hadoop-core-1.1.2.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/ezmorph-1.0.6.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/spring-core-2.5.6.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/zookeeper-3.4.5.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/spring-jms-2.5.6.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/jackson-mapper-asl-1.8.8.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/dnsjava-2.0.6.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/cmdrunner-1.0.1.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/commons-beanutils-1.8.0.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/perfmon-2.2.1.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/json-smart-1.1.1.jar:/home/undera/NetBeansProjects/jmeter-plugins/target/lib/json-path-0.8.1.jar";
        String classpathWinSTR = "D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_jms.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_jdbc.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\hbase-0.90.1-cdh3u0.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\jackson-core-asl-1.5.2.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\json-lib-2.4-jdk15.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_core.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_report.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\jmeter-plugins-1.0.1-RC1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_monitors.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_components.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_native.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\hadoop-core-0.20.2-cdh3u0.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_functions.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_junit.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_ftp.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_ldap.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ezmorph-1.0.6.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_java.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_http.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\qpid-client-0.12.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\spring-core-2.5.3.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\dnsjava-2.0.6.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\CMDRunner.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\commons-beanutils-1.8.0.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\jackson-mapper-asl-1.5.2.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\json-smart-1.1.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\spring-jms-2.5.3.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\qpid-common-0.12.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\zookeeper-3.3.3-cdh3u0.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_mail.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\json-path-0.8.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\ext\\ApacheJMeter_tcp.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\htmlparser-2.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\commons-collections-3.2.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\httpclient-4.2.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\xercesImpl-2.9.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\activation-1.1.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\xmlpull-1.1.3.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\htmllexer-2.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\commons-lang-2.6.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\jtidy-r938.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\excalibur-datasource-1.1.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\jdom-1.1.2.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\commons-io-2.2.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\commons-jexl-2.1.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\rhino-1.7R3.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\jodd-core-3.4.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\httpcore-4.2.2.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\commons-jexl-1.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\commons-httpclient-3.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\tika-core-1.3.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\httpclient-4.2.3.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\tika-parsers-1.3.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\xml-apis-1.3.04.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\bshclient.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\httpmime-4.2.3.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\jsoup-1.7.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\serializer-2.7.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\geronimo-jms_1.1_spec-1.1.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\httpcore-4.2.3.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\xalan-2.7.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\commons-logging-1.1.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\junit-4.10.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\commons-net-3.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\httpmime-4.2.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\slf4j-api-1.7.2.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\bsh-2.0b5.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\commons-codec-1.6.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\logkit-2.0.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\xmlgraphics-commons-1.3.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\excalibur-pool-1.2.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\xstream-1.4.2.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\avalon-framework-4.1.4.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\soap-2.3.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\bsf-api-3.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\commons-lang3-3.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\js-1.7R2.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\excalibur-logger-1.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\rhino-1.7R4.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\xpp3_min-1.1.4c.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\mail-1.4.4.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\excalibur-instrument-1.0.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\bsf-2.4.0.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\jcharts-0.7.5.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\oro-2.0.8.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\jodd-lagarto-3.4.1.jar;D:\\Java\\NetBeansProjects\\JMeter\\trunk\\lib\\jorphan.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\jmeter-plugins-1.0.1-RC1.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\CMDRunner.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\original-jmeter-plugins-1.0.1-RC1.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\qpid-client-0.20.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\hbase-0.94.5.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\json-lib-2.4-jdk15.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\qpid-common-0.20.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\hadoop-core-1.1.2.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\ezmorph-1.0.6.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\spring-core-2.5.6.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\zookeeper-3.4.5.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\spring-jms-2.5.6.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\jackson-mapper-asl-1.8.8.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\dnsjava-2.0.6.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\cmdrunner-1.0.1.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\commons-beanutils-1.8.0.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\perfmon-2.2.1.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\json-smart-1.1.1.jar;D:\\Java\\NetBeansProjects\\jmeter-plugins\\target\\lib\\json-path-0.8.1.jar";

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            String expWinResult = "D:\\Java\\NetBeansProjects\\JMeter\\trunk";
            String resultWin = JMeterPluginsUtils.getJMeterHomeFromCP(classpathWinSTR);
            assertEquals(expWinResult, resultWin);
        } else {
            String expLinuxResult = "/home/undera/NetBeansProjects/JMeter/trunk";
            String resultLinux = JMeterPluginsUtils.getJMeterHomeFromCP(classpathLinuxSTR);
            assertEquals(expLinuxResult, resultLinux);
        }
    }
}
