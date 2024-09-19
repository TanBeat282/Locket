package com.tandev.locket.fragment.live_camera;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.IOUtils;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.locket.R;
import com.tandev.locket.api.ApiCaller;
import com.tandev.locket.api.MomentApiService;
import com.tandev.locket.api.client.LoginApiClient;
import com.tandev.locket.bottomsheet.BottomSheetFriend;
import com.tandev.locket.bottomsheet.BottomSheetInfo;
import com.tandev.locket.helper.ImageUtils;
import com.tandev.locket.helper.ResponseUtils;
import com.tandev.locket.model.login.error.LoginError;
import com.tandev.locket.model.login.response.LoginResponse;
import com.tandev.locket.model.moment.Moment;
import com.tandev.locket.model.user.AccountInfo;
import com.tandev.locket.sharedfreferences.SharedPreferencesUser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveCameraFragment extends Fragment {
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_CODE_PICK_IMAGE = 10001;

    private PreviewView camera_view;

    private RelativeLayout layout_img_view;
    private ImageView img_view;
    private EditText edt_add_message;


    private LinearLayout linear_controller_media;
    private ImageView img_library;
    private RoundedImageView img_capture;
    private ImageView img_camera_switch;

    private LinearLayout linear_controller_send;
    private ImageView img_cancel;
    private LinearLayout layout_send;
    private ImageView img_send;
    private LottieAnimationView lottie_check;
    private ProgressBar progress_bar;
    private ImageView img_save_image;
    private LinearLayout linear_history;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private CameraSelector cameraSelector;
    private boolean isBackCamera = false; // Flag to check current camera
    private LoginResponse loginResponse;
    private MomentApiService momentApiService;
    private byte[] bytes;
    private ImageCapture imageCapture;
    private String edt_message;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        initViews(view);
        onCLick();
        conFigViews();
        checkCameraPermission();
        getDataUser();
    }

    private void getDataUser() {
        loginResponse = SharedPreferencesUser.getLoginResponse(requireContext());
    }

    private void initViews(View view) {
        camera_view = view.findViewById(R.id.camera_view);
        layout_img_view = view.findViewById(R.id.layout_img_view);
        img_view = view.findViewById(R.id.img_view);
        edt_add_message = view.findViewById(R.id.edt_add_message);

        linear_controller_media = view.findViewById(R.id.linear_controller_media);
        img_library = view.findViewById(R.id.img_library);
        img_capture = view.findViewById(R.id.img_capture);
        img_camera_switch = view.findViewById(R.id.img_camera_switch);

        linear_controller_send = view.findViewById(R.id.linear_controller_send);
        img_cancel = view.findViewById(R.id.img_cancel);
        layout_send = view.findViewById(R.id.layout_send);
        img_send = view.findViewById(R.id.img_send);
        lottie_check = view.findViewById(R.id.lottie_check);
        progress_bar = view.findViewById(R.id.progress_bar);
        img_save_image = view.findViewById(R.id.img_save_image);
        linear_history = view.findViewById(R.id.linear_history);
    }

    private void conFigViews() {
        edt_add_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thực hiện gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Không cần thực hiện gì ở đây
            }

            @Override
            public void afterTextChanged(Editable s) {
                edt_message = s.toString().trim();
            }
        });
    }

    private void onCLick() {
        img_camera_switch.setOnClickListener(v -> switchCamera());

        img_library.setOnClickListener(view -> {
            openGallery();
        });
//        img_cancel.setOnClickListener(view -> {
//            bytes = null;
//            edt_message = "";
//            edt_add_message.setText("");
//            relative_profile.setVisibility(View.VISIBLE);
//            relative_send_friend.setVisibility(View.GONE);
//
//            layout_img_view.setVisibility(View.GONE);
//            camera_view.setVisibility(View.VISIBLE);
//            linear_controller_media.setVisibility(View.VISIBLE);
//            linear_controller_send.setVisibility(View.GONE);
//        });

        img_capture.setOnClickListener(view -> capturePicture());

        layout_send.setOnClickListener(view -> sendImage(bytes, edt_message));

    }

    // Open gallery
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            cameraProviderFuture.addListener(this::startCamera, ContextCompat.getMainExecutor(requireContext()));
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    private void startCamera() {
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(isBackCamera ? CameraSelector.LENS_FACING_BACK : CameraSelector.LENS_FACING_FRONT)
                        .build();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(camera_view.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder().build();

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Camera initialization failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }


    private void switchCamera() {
        isBackCamera = !isBackCamera; // Toggle the flag
        startCamera(); // Restart camera with the new selection
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraProviderFuture.addListener(this::startCamera, ContextCompat.getMainExecutor(requireContext()));
            } else {
                // Handle the case where the user denied the permission
                Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Đọc ảnh từ Uri và chuyển thành byte[]
                try {
                    Uri compressedImageUri = ImageUtils.processImage(requireContext(), selectedImageUri, 50);
                    img_view.setImageURI(compressedImageUri);

                    InputStream inputStream = requireContext().getContentResolver().openInputStream(compressedImageUri);
                    bytes = IOUtils.toByteArray(inputStream);

//                    relative_profile.setVisibility(View.GONE);
//                    relative_send_friend.setVisibility(View.VISIBLE);

                    layout_img_view.setVisibility(View.VISIBLE);
                    camera_view.setVisibility(View.GONE);
                    linear_controller_media.setVisibility(View.GONE);
                    linear_controller_send.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void capturePicture() {
        if (imageCapture == null) {
            return;
        }
        File photoFile = new File(requireContext().getExternalFilesDir(null), System.currentTimeMillis() + ".jpg");

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Uri savedUri = Uri.fromFile(photoFile);
                try {
                    Uri compressedImageUri = ImageUtils.processImage(requireContext(), savedUri, 50);
                    img_view.setImageURI(compressedImageUri);


                    InputStream inputStream = requireContext().getContentResolver().openInputStream(compressedImageUri);
                    bytes = IOUtils.toByteArray(inputStream);

//                    relative_profile.setVisibility(View.GONE);
//                    relative_send_friend.setVisibility(View.VISIBLE);

                    layout_img_view.setVisibility(View.VISIBLE);
                    camera_view.setVisibility(View.GONE);
                    linear_controller_media.setVisibility(View.GONE);
                    linear_controller_send.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                // Xử lý lỗi khi chụp ảnh
                exception.printStackTrace();
            }
        });
    }

    private void sendImage(byte[] imageData, String message) {
        progress_bar.setVisibility(View.VISIBLE);
        img_cancel.setVisibility(View.GONE);
        img_send.setVisibility(View.GONE);
        img_save_image.setVisibility(View.GONE);
        linear_history.setVisibility(View.GONE);
        edt_add_message.setEnabled(false);

        ApiCaller apiCaller = new ApiCaller(false);
        apiCaller.postImage(loginResponse.getLocalId(), loginResponse.getIdToken(), message, imageData, (url, success) -> {
            if (success) {
                Log.d("TAG", "Image posted successfully: " + url);
                lottie_check.setVisibility(View.VISIBLE);
                lottie_check.playAnimation();
                progress_bar.setVisibility(View.GONE);

                new Handler().postDelayed(() -> {
                    bytes = null;
                    edt_message = "";
                    edt_add_message.setText("");

                    edt_add_message.setEnabled(true);
//                    relative_profile.setVisibility(View.VISIBLE);
//                    relative_send_friend.setVisibility(View.GONE);

                    layout_img_view.setVisibility(View.GONE);
                    camera_view.setVisibility(View.VISIBLE);
                    linear_controller_media.setVisibility(View.VISIBLE);
                    linear_controller_send.setVisibility(View.GONE);
                    progress_bar.setVisibility(View.GONE);
                    img_cancel.setVisibility(View.VISIBLE);
                    img_send.setVisibility(View.VISIBLE);
                    lottie_check.setVisibility(View.GONE);
                    img_save_image.setVisibility(View.VISIBLE);
                    linear_history.setVisibility(View.VISIBLE);
                }, 3000);
            } else {
                Log.e("TAG", "Image posting failed");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCamera(); // Hàm để unbind camera khi fragment tạm dừng
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopCamera(); // Đảm bảo camera dừng hẳn khi view bị hủy
    }

    private void stopCamera() {
        if (cameraProviderFuture != null) {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll(); // Dừng tất cả các binding liên quan đến camera
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkCameraPermission();
    }
}