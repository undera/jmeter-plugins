package org.jmeterplugins.repository;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.*;
import java.net.URI;

public class Downloader {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private String filename;
    private GenericCallback<String> callback;

    public Downloader(GenericCallback<String> statusChanged) {
        callback = statusChanged;
    }

    public String download(final String id, URI url) throws IOException {
        log.info("Downloading: " + url);
        callback.notify("Downloading " + id + "...");
        HttpClient httpClient = new SystemDefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        HttpContext context = new BasicHttpContext();
        HttpResponse response = httpClient.execute(httpget, context);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new IOException(response.getStatusLine().toString());
        }

        HttpEntity entity = response.getEntity();

        File tempFile = File.createTempFile(id, ".jar");

        final long size = entity.getContentLength();

        InputStream inputStream = entity.getContent();
        OutputStream outputStream = new FileOutputStream(tempFile);
        copyLarge(inputStream, outputStream, new GenericCallback<Long>() {
            @Override
            public void notify(Long progress) {
                callback.notify(String.format("Downloading %s: %d%%", id, 100 * progress / size));
            }
        });
        outputStream.close();
        callback.notify("Downloaded " + id + "...");

        Header cd = response.getLastHeader("Content-Disposition");
        if (cd != null) {
            filename = cd.getValue().split(";")[1].split("=")[1];
        } else {
            HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
            HttpHost currentHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            String currentUrl = (currentReq.getURI().isAbsolute()) ? currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI());
            filename = FilenameUtils.getName(currentUrl);
        }

        return tempFile.getPath();
    }

    public String getFilename() {
        return filename;
    }

    private long copyLarge(InputStream input, OutputStream output, GenericCallback<Long> progressCallback) throws IOException {
        byte[] buffer = new byte[4096];
        long count = 0L;

        int n;
        for (; -1 != (n = input.read(buffer)); count += (long) n) {
            output.write(buffer, 0, n);
            progressCallback.notify(count);
        }

        return count;
    }

}
