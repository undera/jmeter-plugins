package com.blazemeter.api;

import com.blazemeter.api.BlazemeterReport;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BlazemeterReportTest {

    @Test
    public void test() throws Exception {
        BlazemeterReport report = new BlazemeterReport();

        String project = "project";
        report.setProject(project);
        assertEquals(project, report.getProject());

        String title = "title";
        report.setTitle(title);
        assertEquals(title, report.getTitle());

        String workspace = "workspace";
        report.setWorkspace(workspace);
        assertEquals(workspace, report.getWorkspace());

        String token = "token";
        report.setToken(token);
        assertEquals(token, report.getToken());

        report.setAnonymousTest(true);
        assertTrue(report.isAnonymousTest());

        report.setShareTest(true);
        assertTrue(report.isShareTest());
    }
}