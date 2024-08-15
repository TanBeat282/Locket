package com.tandev.locket.api;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import androidx.annotation.NonNull;

import com.tandev.locket.api.client.UploadApiClient;
import com.tandev.locket.constants.HeaderConstants;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Map;

public class ApiCaller {

    private final UploadApiService uploadApiService;
    private final UploadApiService postApiService;

    public ApiCaller(boolean isVideoUpload) {
        if (isVideoUpload) {
            uploadApiService = UploadApiClient.getUploadVideoRetrofit().create(UploadApiService.class);
        } else {
            uploadApiService = UploadApiClient.getUploadImageRetrofit().create(UploadApiService.class);
        }
        postApiService = UploadApiClient.getPostRetrofit().create(UploadApiService.class);
    }

    public interface UploadCallback {
        void onUploadComplete(String url, boolean success);
    }

    public void startUploadImage(String idUser, String idToken, byte[] image, UploadCallback callback) {
        String nameImg = System.currentTimeMillis() + "_tandev.webp";
        String url = String.format("users%%2F%s%%2Fmoments%%2Fthumbnails%%2F%s?uploadType=resumable&name=users%%2F%s%%2Fmoments%%2Fthumbnails%%2F%s", idUser, nameImg, idUser, nameImg);
        Map<String, String> headers = HeaderConstants.getStartUploadHeaders(idToken, image.length, false);


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), getJsonData(idUser, nameImg));
        Call<ResponseBody> call = uploadApiService.startUploadImage(url, headers, requestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    callback.onUploadComplete(null, false);
                    return;
                }

                String uploadUrl = response.headers().get("x-goog-upload-url");
                uploadImage(uploadUrl, image, idUser, nameImg, idToken, callback);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                Log.e("ApiCaller", "Error in uploadImageToFirebaseStorage", throwable);
                callback.onUploadComplete(null, false);
            }
        });
    }

    private String getJsonData(String idUser, String nameImg) {
        return String.format("{\"name\":\"users/%s/moments/thumbnails/%s\",\"contentType\":\"image/*\",\"bucket\":\"\",\"metadata\":{\"creator\":\"%s\",\"visibility\":\"private\"}}", idUser, nameImg, idUser);
    }

    private void uploadImage(String uploadUrl, byte[] image, String idUser, String nameImg, String idToken, UploadCallback callback) {
        Map<String, String> uploadHeaders = HeaderConstants.getUploadImageHeaders();

        RequestBody imageBody = RequestBody.create(MediaType.parse("image/webp"), image);
        Call<ResponseBody> uploadCall = uploadApiService.uploadImage(uploadUrl, uploadHeaders, imageBody);

        uploadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    callback.onUploadComplete(null, false);
                    return;
                }

                String getUrl = String.format("users%%2F%s%%2Fmoments%%2Fthumbnails%%2F%s", idUser, nameImg);
                Map<String, String> getHeaders = Map.of(
                        "content-type", "application/json; charset=UTF-8",
                        "authorization", "Bearer " + idToken
                );

                getDownloadTokenImage(getUrl, getHeaders, callback);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                Log.e("ApiCaller", "Error in uploadImage", throwable);
                callback.onUploadComplete(null, false);
            }
        });
    }

    private void getDownloadTokenImage(String getUrl, Map<String, String> getHeaders, UploadCallback callback) {
        Call<ResponseBody> getCall = uploadApiService.getDownloadTokenImage(getUrl, getHeaders);

        getCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    callback.onUploadComplete(null, false);
                    return;
                }

                try {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String downloadToken = jsonObject.optString("downloadTokens", null);
                    String finalUrl = call.request().url() + "?alt=media&token=" + downloadToken;
                    callback.onUploadComplete(finalUrl, true);
                } catch (Exception e) {
                    Log.e("ApiCaller", "Error parsing download token", e);
                    callback.onUploadComplete(null, false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                Log.e("ApiCaller", "Error in getDownloadToken", throwable);
                callback.onUploadComplete(null, false);
            }
        });
    }

    public void postImage(String idUser, String idToken, String caption, byte[] image, UploadCallback callback) {
        startUploadImage(idUser, idToken, image, (thumbnailUrl, success) -> {
            if (!success || thumbnailUrl == null) {
                callback.onUploadComplete(null, false);
                return;
            }

            // Xây dựng JSON dữ liệu dựa trên giá trị của caption
            String jsonData;
            if (caption == null || caption.isEmpty()) {
                jsonData = String.format("{\"data\":{\"thumbnail_url\":\"%s\",\"sent_to_all\":true}}", thumbnailUrl);
            } else {
                jsonData = String.format("{\"data\":{\"thumbnail_url\":\"%s\",\"caption\":\"%s\",\"sent_to_all\":true}}", thumbnailUrl, caption);
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);

            Call<ResponseBody> call = postApiService.postImage("postMomentV2", HeaderConstants.getPostHeaders(idToken), body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (!response.isSuccessful()) {
                        callback.onUploadComplete(null, false);
                        return;
                    }
                    callback.onUploadComplete(thumbnailUrl, true);
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                    Log.e("ApiCaller", "Error in postImage", throwable);
                    callback.onUploadComplete(thumbnailUrl, false);
                }
            });
        });
    }


//    //VIDEO
//    public void startUploadVideo(String idUser, String idToken, byte[] video, UploadCallback callback) {
//        String nameVideo = System.currentTimeMillis() + "_tandev.mp4";
//        int videoSize = video.length;
//
//        String url = String.format("users%%2F%s%%2Fmoments%%2Fvideos%%2F%s?uploadType=resumable&name=users%%2F%s%%2Fmoments%%2Fvideos%%2F%s", idUser, nameVideo, idUser, nameVideo);
//
//
//        String jsonData = "{\"name\":\"users/" + idUser + "/moments/videos/" + nameVideo + "\",\"contentType\":\"video/mp4\",\"bucket\":\"\",\"metadata\":{\"creator\":\"" + idUser + "\",\"visibility\":\"private\"}}";
//
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), jsonData);
//        Call<ResponseBody> call = uploadApiService.startUploadVideo(url, HeaderConstants.getStartUploadHeaders(idToken, videoSize, true), requestBody);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                if (!response.isSuccessful()) {
//                    callback.onUploadComplete(null, false);
//                    return;
//                }
//
//                String uploadUrl = response.headers().get("x-goog-upload-url");
//                uploadVideo(uploadUrl, video, idUser, nameVideo, idToken, callback);
//                uploadThumbnailFromVideo(idUser, idToken, uploadUrl, callback);
//
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
//                Log.e("ApiCaller", "Error in uploadImageToFirebaseStorage", throwable);
//                callback.onUploadComplete(null, false);
//            }
//        });
//    }
//
//    private void uploadVideo(String uploadUrl, byte[] video, String idUser, String nameVideo, String idToken, UploadCallback callback) {
//        Map<String, String> uploadHeaders = HeaderConstants.getUploadImageHeaders();
//
//        RequestBody videoBody = RequestBody.create(MediaType.parse("video/mp4"), video);
//        Call<ResponseBody> uploadCall = uploadApiService.uploadImage(uploadUrl, uploadHeaders, videoBody);
//
//        uploadCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                if (!response.isSuccessful()) {
//                    callback.onUploadComplete(null, false);
//                    return;
//                }
//
//                String getUrl = String.format("users%%2F%s%%2Fmoments%%2Fvideos%%2F%s", idUser, nameVideo);
//                Map<String, String> getHeaders = Map.of(
//                        "content-type", "application/json; charset=UTF-8",
//                        "authorization", "Bearer " + idToken
//                );
//
//                getDownloadTokenVideo(getUrl, getHeaders, callback);
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
//                Log.e("ApiCaller", "Error in uploadImage", throwable);
//                callback.onUploadComplete(null, false);
//            }
//        });
//    }
//
//    private void getDownloadTokenVideo(String getUrl, Map<String, String> getHeaders, UploadCallback callback) {
//        Call<ResponseBody> getCall = uploadApiService.getDownloadTokenVideo(getUrl, getHeaders);
//
//        getCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                if (!response.isSuccessful()) {
//                    callback.onUploadComplete(null, false);
//                    return;
//                }
//
//                try {
//                    String responseBody = response.body().string();
//                    JSONObject jsonObject = new JSONObject(responseBody);
//                    String downloadToken = jsonObject.optString("downloadTokens", null);
//                    String finalUrl = call.request().url() + "?alt=media&token=" + downloadToken;
//                    callback.onUploadComplete(finalUrl, true);
//                } catch (Exception e) {
//                    Log.e("ApiCaller", "Error parsing download token", e);
//                    callback.onUploadComplete(null, false);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
//                Log.e("ApiCaller", "Error in getDownloadToken", throwable);
//                callback.onUploadComplete(null, false);
//            }
//        });
//    }
//
//    // Tạo thumbnail từ video và upload lên Firebase Storage
//    private void uploadThumbnailFromVideo(String idUser, String idToken, String videoPath, UploadCallback callback) {
//        try {
//            // Sử dụng MediaMetadataRetriever để lấy thumbnail từ video
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//            retriever.setDataSource(videoPath);
//            Bitmap thumbnail = retriever.getFrameAtTime(1000000); // Lấy khung hình tại 1 giây
//
//            if (thumbnail == null) {
//                throw new Exception("Unable to create thumbnail");
//            }
//
//            // Chuyển đổi thumbnail thành byte[]
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            thumbnail.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
//            byte[] thumbnailBytes = byteArrayOutputStream.toByteArray();
//
//            startUploadImage(idUser, idToken, thumbnailBytes, (thumbnailUrl, success) -> {
//                if (!success || thumbnailUrl == null) {
//                    callback.onUploadComplete(null, false);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void postVideo(String idUser, String idToken, String caption, byte[] video, UploadCallback callback) {
//        startUploadVideo(idUser, idToken, video, (thumbnailUrl, success) -> {
//            if (!success || thumbnailUrl == null) {
//                callback.onUploadComplete(null, false);
//                return;
//            }
//            try {
//                // Upload video lên Firebase Storage và lấy URL
//                String videoUrl = startUploadVideo(idUser, idToken, video);
//                if (videoUrl == null) {
//                    return false;
//                }
//                String thumbnailUrl = uploadThumbnailFromVideo(idUser, idToken, videoUrl);
//                if (thumbnailUrl == null) {
//                    return false;
//                }
//
//                // Tạo JSON object cho postData
//                String jsonData = "{"
//                        + "\"data\":{"
//                        + "\"thumbnail_url\":\"" + thumbnailUrl + "\","
//                        + "\"video_url\":\"" + videoUrl + "\","
//                        + "\"md5\":\"" + videoUrl.hashCode() + "\","
//                        + "\"recipients\":[],"
//                        + "\"analytics\":{"
//                        + "\"experiments\":{"
//                        + "\"flag_4\":{\"@type\":\"type.googleapis.com/google.protobuf.Int64Value\",\"value\":\"43\"},"
//                        + "\"flag_10\":{\"@type\":\"type.googleapis.com/google.protobuf.Int64Value\",\"value\":\"505\"},"
//                        + "\"flag_23\":{\"@type\":\"type.googleapis.com/google.protobuf.Int64Value\",\"value\":\"400\"},"
//                        + "\"flag_22\":{\"value\":\"1203\",\"@type\":\"type.googleapis.com/google.protobuf.Int64Value\"},"
//                        + "\"flag_19\":{\"value\":\"52\",\"@type\":\"type.googleapis.com/google.protobuf.Int64Value\"},"
//                        + "\"flag_18\":{\"@type\":\"type.googleapis.com/google.protobuf.Int64Value\",\"value\":\"1203\"},"
//                        + "\"flag_16\":{\"value\":\"303\",\"@type\":\"type.googleapis.com/google.protobuf.Int64Value\"},"
//                        + "\"flag_15\":{\"@type\":\"type.googleapis.com/google.protobuf.Int64Value\",\"value\":\"501\"},"
//                        + "\"flag_14\":{\"@type\":\"type.googleapis.com/google.protobuf.Int64Value\",\"value\":\"500\"},"
//                        + "\"flag_25\":{\"@type\":\"type.googleapis.com/google.protobuf.Int64Value\",\"value\":\"23\"}"
//                        + "},"
//                        + "\"amplitude\":{"
//                        + "\"device_id\":\"BF5D1FD7-9E4D-4F8B-AB68-B89ED20398A6\","
//                        + "\"session_id\":{\"value\":\"1722437166613\",\"@type\":\"type.googleapis.com/google.protobuf.Int64Value\"}"
//                        + "},"
//                        + "\"google_analytics\":{\"app_instance_id\":\"5BDC04DA16FF4B0C9CA14FFB9C502900\"},"
//                        + "\"platform\":\"ios\""
//                        + "},"
//                        + "\"sent_to_all\":true,"
//                        + "\"caption\":\"" + caption + "\","
//                        + "\"overlays\":[{"
//                        + "\"data\":{"
//                        + "\"text\":\"" + caption + "\","
//                        + "\"text_color\":\"#FFFFFFE6\","
//                        + "\"type\":\"standard\","
//                        + "\"max_lines\":{\"@type\":\"type.googleapis.com/google.protobuf.Int64Value\",\"value\":\"4\"},"
//                        + "\"background\":{\"material_blur\":\"ultra_thin\",\"colors\":[]}"
//                        + "},"
//                        + "\"alt_text\":\"" + caption + "\","
//                        + "\"overlay_id\":\"caption:standard\","
//                        + "\"overlay_type\":\"caption\""
//                        + "}]"
//                        + "}"
//                        + "}";
//                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonData);
//
//                // Gửi yêu cầu và nhận phản hồi
//                Call<ResponseBody> call = uploadApiService.postVideo(HeaderConstants.getPostHeaders(idToken), body);
//                Response<ResponseBody> response = call.execute();
//
//                // Kiểm tra status code
//                return response.isSuccessful();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        });
//
//    }

}
