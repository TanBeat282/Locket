package com.tandev.locket.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtils {

    public static Uri processImage(Context context, Uri imageUri, int quality) throws IOException {
        Bitmap bitmap = getBitmapFromUri(context, imageUri);

        if (bitmap == null) {
            throw new IOException("Không thể đọc ảnh từ Uri");
        }

        // Xoay ảnh nếu cần
        Bitmap rotatedBitmap = rotateBitmapIfNeeded(context, imageUri, bitmap);

        // Cắt ảnh từ trung tâm với kích thước 1020x1020 hoặc kích thước nhỏ hơn nếu ảnh nhỏ
        Bitmap croppedBitmap = cropBitmap(rotatedBitmap, 1020, 1020);

        // Lưu ảnh đã được xử lý
        return saveBitmapToCache(context, croppedBitmap, quality);
    }

    private static Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        try (ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
             FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor())) {
            return BitmapFactory.decodeStream(fileInputStream);
        }
    }

    private static Bitmap rotateBitmapIfNeeded(Context context, Uri uri, Bitmap bitmap) throws IOException {
        ExifInterface exif = new ExifInterface(context.getContentResolver().openInputStream(uri));
        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (rotation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateBitmap(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateBitmap(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateBitmap(bitmap, 270);
            default:
                return bitmap;
        }
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static Bitmap cropBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();

        // Xác định kích thước cắt dựa trên kích thước ảnh gốc
        int cropWidth = Math.min(targetWidth, originalWidth);
        int cropHeight = Math.min(targetHeight, originalHeight);

        // Tính toán vị trí bắt đầu cắt từ trung tâm
        int x = (originalWidth - cropWidth) / 2;
        int y = (originalHeight - cropHeight) / 2;

        // Cắt ảnh
        return Bitmap.createBitmap(bitmap, x, y, cropWidth, cropHeight);
    }

    private static Uri saveBitmapToCache(Context context, Bitmap bitmap, int quality) throws IOException {
        File file = new File(context.getCacheDir(), "processed_image.jpg");
        try (OutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        }
        return Uri.fromFile(file);
    }
}
