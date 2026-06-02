package com.simplecity.amp_library.http;

import android.util.Log;
import fi.iki.elonen.NanoHTTPD;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    private static final String TAG = "HttpServer";

    private static HttpServer sHttpServer;

    private NanoServer server;

    private String audioFileToServe;
    private byte[] imageBytesToServe;

    private FileInputStream audioInputStream;
    private ByteArrayInputStream imageInputStream;

    private boolean isStarted = false;

    public static HttpServer getInstance() {
        if (sHttpServer == null) {
            sHttpServer = new HttpServer();
        }
        return sHttpServer;
    }

    private HttpServer() {
        server = new NanoServer();
    }

    public void serveAudio(String audioUri) {
        if (audioUri != null) {
            audioFileToServe = audioUri;
        }
    }

    public void serveImage(byte[] imageBytes) {
        if (imageBytes != null) {
            imageBytesToServe = imageBytes;
        }
    }

    public void clearImage() {
        imageBytesToServe = null;
    }

    public void start() {
        if (!isStarted) {
            try {
                server.start();
                isStarted = true;
            } catch (IOException e) {
                Log.e(TAG, "Error starting server: " + e.getMessage());
            }
        }
    }

    public void stop() {
        if (isStarted) {
            server.stop();
            isStarted = false;
            cleanupAudioStream();
            cleanupImageStream();
        }
    }

    private class NanoServer extends NanoHTTPD {

        NanoServer() {
            super(5000);
        }

        @Override
        public Response serve(IHTTPSession session) {

            if (audioFileToServe == null) {
                Log.e(TAG, "Audio file to serve null");
                return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", "File not found");
            }

            String uri = session.getUri();
            if (uri.contains("audio")) {
                try {
                    File file = new File(audioFileToServe);

                    Map<String, String> headers = session.getHeaders();
                    String range = null;
                    for (String key : headers.keySet()) {
                        if ("range".equals(key)) {
                            range = headers.get(key);
                        }
                    }

                    if (range == null) {
                        range = "bytes=0-";
                        session.getHeaders().put("range", range);
                    }

                    long start;
                    long end;
                    long fileLength = file.length();

                    String rangeValue = range.trim().substring("bytes=".length());

                    if (rangeValue.startsWith("-")) {
                        end = fileLength - 1;
                        start = fileLength - 1 - Long.parseLong(rangeValue.substring("-".length()));
                    } else {
                        String[] ranges = rangeValue.split("-");
                        start = Long.parseLong(ranges[0]);
                        end = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileLength - 1;
                    }
                    if (end > fileLength - 1) {
                        end = fileLength - 1;
                    }

                    if (start <= end) {
                        long contentLength = end - start + 1;
                        cleanupAudioStream();
                        audioInputStream = new FileInputStream(file);
                        audioInputStream.skip(start);
                        Response response = newFixedLengthResponse(Response.Status.PARTIAL_CONTENT, getMimeType(audioFileToServe), audioInputStream, contentLength);
                        response.addHeader("Content-Length", contentLength + "");
                        response.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
                        response.addHeader("Content-Type", getMimeType(audioFileToServe));
                        return response;
                    } else {
                        return newFixedLengthResponse(Response.Status.RANGE_NOT_SATISFIABLE, "text/html", range);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error serving audio: " + e.getMessage());
                    e.printStackTrace();
                }
            } else if (uri.contains("image")) {
                if (imageBytesToServe == null) {
                    return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", "Image bytes null");
                }
                cleanupImageStream();
                imageInputStream = new ByteArrayInputStream(imageBytesToServe);
                Log.i(TAG, "Serving image bytes: " + imageBytesToServe.length);
                return newFixedLengthResponse(Response.Status.OK, "image/png", imageInputStream, imageBytesToServe.length);
            }
            Log.e(TAG, "Returning NOT_FOUND response");
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", "File not found");
        }
    }

    void cleanupAudioStream() {
        if (audioInputStream != null) {
            try {
                audioInputStream.close();
            } catch (IOException e) {
                Log.w(TAG, "Error closing audio stream", e);
            }
        }
    }

    void cleanupImageStream() {
        if (imageInputStream != null) {
            try {
                imageInputStream.close();
            } catch (IOException e) {
                Log.w(TAG, "Error closing image stream", e);
            }
        }
    }
    private static Map<String, String> createMimeTypes() {
        Map<String, String> m = new HashMap<>();
        m.put("css", "text/css");
        m.put("htm", "text/html");
        m.put("html", "text/html");
        m.put("xml", "text/xml");
        m.put("java", "text/x-java-source, text/java");
        m.put("md", "text/plain");
        m.put("txt", "text/plain");
        m.put("asc", "text/plain");
        m.put("gif", "image/gif");
        m.put("jpg", "image/jpeg");
        m.put("jpeg", "image/jpeg");
        m.put("png", "image/png");
        m.put("mp3", "audio/mpeg");
        m.put("m3u", "audio/mpeg-url");
        m.put("mp4", "video/mp4");
        m.put("ogv", "video/ogg");
        m.put("flv", "video/x-flv");
        m.put("mov", "video/quicktime");
        m.put("swf", "application/x-shockwave-flash");
        m.put("js", "application/javascript");
        m.put("pdf", "application/pdf");
        m.put("doc", "application/msword");
        m.put("ogg", "application/x-ogg");
        m.put("zip", "application/octet-stream");
        m.put("exe", "application/octet-stream");
        m.put("class", "application/octet-stream");
        return m;
    }

    private final Map<String, String> MIME_TYPES = createMimeTypes();

    String getMimeType(String filePath) {
        return MIME_TYPES.get(filePath.substring(filePath.lastIndexOf(".") + 1));
    }
}