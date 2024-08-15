package com.tandev.locket.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class ResponseUtils {

    public static String getResponseBody(InputStream inputStream, String contentEncoding) throws IOException {
        if ("gzip".equalsIgnoreCase(contentEncoding)) {
            try (GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)) {
                return convertStreamToString(gzipInputStream);
            }
        } else {
            return convertStreamToString(inputStream);
        }
    }

    private static String convertStreamToString(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }
}

