package org.loadosophia.jmeter;

public class LoadosophiaUploadResults {

    private String redirect = "";
    private int testID;
    private int queueID;

    public String getRedirectLink() {
        return redirect;
    }

    public void setRedirectLink(String string) {
        redirect = string;
    }

    public void setQueueID(int aQueueID) {
        queueID = aQueueID;
    }

    public void setTestID(int aTestID) {
        testID = aTestID;
    }

    public int getTestID() {
        return testID;
    }

    public int getQueueID() {
        return queueID;
    }
}
