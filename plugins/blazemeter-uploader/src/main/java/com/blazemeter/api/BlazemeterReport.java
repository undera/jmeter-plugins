package com.blazemeter.api;

public class BlazemeterReport {

    protected boolean isAnonymousTest;
    protected boolean isShareTest;
    protected String workspace;
    protected String project;
    protected String title;
    protected String token;

    public boolean isAnonymousTest() {
        return isAnonymousTest;
    }

    public void setAnonymousTest(boolean anonymousTest) {
        isAnonymousTest = anonymousTest;
    }

    public boolean isShareTest() {
        return isShareTest;
    }

    public void setShareTest(boolean shareTest) {
        isShareTest = shareTest;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
