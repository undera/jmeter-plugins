package kg.apc.charting.plotters;

import kg.apc.charting.ChartSettings;
import kg.apc.charting.rows.GraphRowAverages;
import org.apache.jorphan.gui.NumberRenderer;
import java.awt.Color;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.emulators.Graphics2DEmul;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stephane Hoblingre
 */
public class CSplineRowPlotterTest {

    public CSplineRowPlotterTest() {
    }

    private AbstractGraphRow testRow;
    private long minXVal, maxXVal;
    private double minYVal, maxYVal;

   @BeforeClass
   public static void setUpClass() throws Exception {
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

    @Before
    public void setUp() {
       testRow =  new GraphRowAverages();
       long now = System.currentTimeMillis();
       testRow.add(now, 1);
       testRow.add(now+5000, 20);
       testRow.add(now+10000, 50);

       testRow.setGranulationValue(500);

       minXVal = now;
       maxXVal = now+10000;
       minYVal = 0;
       maxYVal = 50;
    }

    @After
    public void tearDown() {
    }

   /**
    * Test of processPoint method, of class CSplineRowPlotter.
    */
   @Test
   public void testProcessPoint() {
      System.out.println("processPoint");
      Graphics2DEmul g2d = new Graphics2DEmul();
      CSplineRowPlotter instance = new CSplineRowPlotter(new ChartSettings(), new NumberRenderer());
      instance.setBoundsValues(g2d.getBounds(), minXVal, maxXVal, minYVal, maxYVal);
      instance.processPoint(g2d, 100);
      String expResult = "";
      String result = g2d.getResult();
      assertEquals(expResult, result);
   }

   /**
    * Test of postPaintRow method, of class CSplineRowPlotter.
    */
   @Test
   public void testPostPaintRow() {
      System.out.println("postPaintRow");
      Graphics2DEmul g2d = new Graphics2DEmul();
      ChartSettings chartSettings = new ChartSettings();
      CSplineRowPlotter instance = new CSplineRowPlotter(chartSettings, new NumberRenderer());
      instance.setBoundsValues(g2d.getBounds(), minXVal, maxXVal, minYVal, maxYVal);
      instance.postPaintRow(testRow, g2d);
      String expResult = "drawLine: (0;0) - (10;598)|drawLine: (10;598) - (14;597)|drawLine: (14;597) - (18;595)|drawLine: (18;595) - (22;593)|drawLine: (22;593) - (26;591)|drawLine: (26;591) - (30;589)|drawLine: (30;589) - (34;587)|drawLine: (34;587) - (38;585)|drawLine: (38;585) - (42;583)|drawLine: (42;583) - (46;581)|drawLine: (46;581) - (50;579)|drawLine: (50;579) - (54;577)|drawLine: (54;577) - (58;575)|drawLine: (58;575) - (62;573)|drawLine: (62;573) - (66;571)|drawLine: (66;571) - (70;569)|drawLine: (70;569) - (74;567)|drawLine: (74;567) - (78;565)|drawLine: (78;565) - (82;563)|drawLine: (82;563) - (86;561)|drawLine: (86;561) - (90;559)|drawLine: (90;559) - (94;557)|drawLine: (94;557) - (98;555)|drawLine: (98;555) - (102;553)|drawLine: (102;553) - (106;551)|drawLine: (106;551) - (110;549)|drawLine: (110;549) - (114;547)|drawLine: (114;547) - (118;545)|drawLine: (118;545) - (122;543)|drawLine: (122;543) - (126;541)|drawLine: (126;541) - (130;539)|drawLine: (130;539) - (134;537)|drawLine: (134;537) - (138;535)|drawLine: (138;535) - (142;533)|drawLine: (142;533) - (146;531)|drawLine: (146;531) - (150;529)|drawLine: (150;529) - (154;527)|drawLine: (154;527) - (158;525)|drawLine: (158;525) - (162;523)|drawLine: (162;523) - (166;520)|drawLine: (166;520) - (170;518)|drawLine: (170;518) - (174;516)|drawLine: (174;516) - (178;514)|drawLine: (178;514) - (182;512)|drawLine: (182;512) - (186;510)|drawLine: (186;510) - (190;508)|drawLine: (190;508) - (194;506)|drawLine: (194;506) - (198;503)|drawLine: (198;503) - (202;501)|drawLine: (202;501) - (206;499)|drawLine: (206;499) - (210;497)|drawLine: (210;497) - (214;495)|drawLine: (214;495) - (218;492)|drawLine: (218;492) - (222;490)|drawLine: (222;490) - (226;488)|drawLine: (226;488) - (230;486)|drawLine: (230;486) - (234;484)|drawLine: (234;484) - (238;481)|drawLine: (238;481) - (242;479)|drawLine: (242;479) - (246;477)|drawLine: (246;477) - (250;474)|drawLine: (250;474) - (254;472)|drawLine: (254;472) - (258;470)|drawLine: (258;470) - (262;467)|drawLine: (262;467) - (266;465)|drawLine: (266;465) - (270;463)|drawLine: (270;463) - (274;460)|drawLine: (274;460) - (278;458)|drawLine: (278;458) - (282;456)|drawLine: (282;456) - (286;453)|drawLine: (286;453) - (290;451)|drawLine: (290;451) - (294;448)|drawLine: (294;448) - (298;446)|drawLine: (298;446) - (302;443)|drawLine: (302;443) - (306;441)|drawLine: (306;441) - (310;438)|drawLine: (310;438) - (314;436)|drawLine: (314;436) - (318;433)|drawLine: (318;433) - (322;431)|drawLine: (322;431) - (326;428)|drawLine: (326;428) - (330;426)|drawLine: (330;426) - (334;423)|drawLine: (334;423) - (338;420)|drawLine: (338;420) - (342;418)|drawLine: (342;418) - (346;415)|drawLine: (346;415) - (350;412)|drawLine: (350;412) - (354;410)|drawLine: (354;410) - (358;407)|drawLine: (358;407) - (362;404)|drawLine: (362;404) - (366;402)|drawLine: (366;402) - (370;399)|drawLine: (370;399) - (374;396)|drawLine: (374;396) - (378;393)|drawLine: (378;393) - (382;391)|drawLine: (382;391) - (386;388)|drawLine: (386;388) - (390;385)|drawLine: (390;385) - (394;382)|drawLine: (394;382) - (398;379)|drawLine: (398;379) - (402;376)|drawLine: (402;376) - (406;373)|drawLine: (406;373) - (410;370)|drawLine: (410;370) - (414;368)|drawLine: (414;368) - (418;365)|drawLine: (418;365) - (422;362)|drawLine: (422;362) - (426;359)|drawLine: (426;359) - (430;356)|drawLine: (430;356) - (434;353)|drawLine: (434;353) - (438;349)|drawLine: (438;349) - (442;346)|drawLine: (442;346) - (446;343)|drawLine: (446;343) - (450;340)|drawLine: (450;340) - (454;337)|drawLine: (454;337) - (458;334)|drawLine: (458;334) - (462;331)|drawLine: (462;331) - (466;327)|drawLine: (466;327) - (470;324)|drawLine: (470;324) - (474;321)|drawLine: (474;321) - (478;318)|drawLine: (478;318) - (482;315)|drawLine: (482;315) - (486;311)|drawLine: (486;311) - (490;308)|drawLine: (490;308) - (494;305)|drawLine: (494;305) - (498;301)|drawLine: (498;301) - (502;298)|drawLine: (502;298) - (506;295)|drawLine: (506;295) - (510;291)|drawLine: (510;291) - (514;288)|drawLine: (514;288) - (518;285)|drawLine: (518;285) - (522;281)|drawLine: (522;281) - (526;278)|drawLine: (526;278) - (530;274)|drawLine: (530;274) - (534;271)|drawLine: (534;271) - (538;267)|drawLine: (538;267) - (542;264)|drawLine: (542;264) - (546;260)|drawLine: (546;260) - (550;257)|drawLine: (550;257) - (554;253)|drawLine: (554;253) - (558;250)|drawLine: (558;250) - (562;246)|drawLine: (562;246) - (566;243)|drawLine: (566;243) - (570;239)|drawLine: (570;239) - (574;236)|drawLine: (574;236) - (578;232)|drawLine: (578;232) - (582;228)|drawLine: (582;228) - (586;225)|drawLine: (586;225) - (590;221)|drawLine: (590;221) - (594;218)|drawLine: (594;218) - (598;214)|drawLine: (598;214) - (602;210)|drawLine: (602;210) - (606;207)|drawLine: (606;207) - (610;203)|drawLine: (610;203) - (614;199)|drawLine: (614;199) - (618;195)|drawLine: (618;195) - (622;192)|drawLine: (622;192) - (626;188)|drawLine: (626;188) - (630;184)|drawLine: (630;184) - (634;181)|drawLine: (634;181) - (638;177)|drawLine: (638;177) - (642;173)|drawLine: (642;173) - (646;169)|drawLine: (646;169) - (650;166)|drawLine: (650;166) - (654;162)|drawLine: (654;162) - (658;158)|drawLine: (658;158) - (662;154)|drawLine: (662;154) - (666;150)|drawLine: (666;150) - (670;147)|drawLine: (670;147) - (674;143)|drawLine: (674;143) - (678;139)|drawLine: (678;139) - (682;135)|drawLine: (682;135) - (686;131)|drawLine: (686;131) - (690;128)|drawLine: (690;128) - (694;124)|drawLine: (694;124) - (698;120)|drawLine: (698;120) - (702;116)|drawLine: (702;116) - (706;112)|drawLine: (706;112) - (710;108)|drawLine: (710;108) - (714;104)|drawLine: (714;104) - (718;100)|drawLine: (718;100) - (722;97)|drawLine: (722;97) - (726;93)|drawLine: (726;93) - (730;89)|drawLine: (730;89) - (734;85)|drawLine: (734;85) - (738;81)|drawLine: (738;81) - (742;77)|drawLine: (742;77) - (746;73)|drawLine: (746;73) - (750;69)|drawLine: (750;69) - (754;65)|drawLine: (754;65) - (758;62)|drawLine: (758;62) - (762;58)|drawLine: (762;58) - (766;54)|drawLine: (766;54) - (770;50)|drawLine: (770;50) - (774;46)|drawLine: (774;46) - (778;42)|drawLine: (778;42) - (782;38)|drawLine: (782;38) - (786;34)|drawLine: (786;34) - (790;30)|drawLine: (790;30) - (794;26)|drawLine: (794;26) - (798;22)|drawLine: (798;22) - (802;18)|drawLine: (802;18) - (806;14)|drawLine: (806;14) - (810;10)|";
      String result = g2d.getResult();
      assertEquals(expResult, result);
   }

}