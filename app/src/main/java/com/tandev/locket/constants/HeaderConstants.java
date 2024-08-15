package com.tandev.locket.constants;

import java.util.HashMap;
import java.util.Map;

public class HeaderConstants {

    public static Map<String, String> getStartUploadHeaders(String idToken, int imageSize, boolean isVideo) {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json; charset=UTF-8");
        headers.put("authorization", "Bearer " + idToken);
        headers.put("x-goog-upload-protocol", "resumable");
        headers.put("accept", "*/*");
        headers.put("x-goog-upload-command", "start");
        headers.put("x-goog-upload-content-length", String.valueOf(imageSize));
        headers.put("accept-language", "vi-VN,vi;q=0.9");
        headers.put("x-firebase-storage-version", "ios/10.13.0");
        headers.put("user-agent", "com.locket.Locket/1.43.1 iPhone/17.3 hw/iPhone15_3 (GTMSUF/1)");
        headers.put("x-goog-upload-content-type", isVideo ? "video/mp4" : "image/webp");
        headers.put("x-firebase-gmpid", "1:641029076083:ios:cc8eb46290d69b234fa609");
        return headers;
    }

    public static Map<String, String> getUploadImageHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/octet-stream");
        headers.put("x-goog-upload-protocol", "resumable");
        headers.put("x-goog-upload-offset", "0");
        headers.put("x-goog-upload-command", "upload, finalize");
        headers.put("upload-incomplete", "?0");
        headers.put("upload-draft-interop-version", "3");
        headers.put("user-agent", "com.locket.Locket/1.43.1 iPhone/17.3 hw/iPhone15_3 (GTMSUF/1)");
        return headers;
    }

    public static Map<String, String> getPostHeaders(String idToken) {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("authorization", "Bearer " + idToken);
        return headers;
    }
}
