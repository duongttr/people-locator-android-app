package com.example.waud.loginslider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.waud.MapsActivity;
import com.example.waud.MyLocation;
import com.example.waud.R;
import com.example.waud.User;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Objects;

public class SecondFragment extends Fragment{
    Context context;
    DatabaseReference ref;
    StorageReference storageRef;

    private final String TAG = "SecondFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = inflater.getContext();
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    ImageView ivProfilePicture;
    TextView tvName;
    TextInputEditText edtNickname, edtPhoneNumber, edtFbId;
    FloatingActionButton btnSubmit;

    Calendar calendar = Calendar.getInstance();

    String photoUrl, displayName, UID;

    TextInputLayout tilFbId;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        ivProfilePicture = view.findViewById(R.id.imageView_profile_picture);
        tvName = view.findViewById(R.id.textView_name);
        edtNickname = view.findViewById(R.id.textInput_nickname);
        edtPhoneNumber = view.findViewById(R.id.textInput_phone_number);
        edtFbId = view.findViewById(R.id.textInput_FB_Id);
        btnSubmit = view.findViewById(R.id.button_submit);
        tilFbId = view.findViewById(R.id.til_Fb_Id);
        tilFbId.setEndIconOnClickListener(OnGetFacebookIdListener);

        edtNickname.addTextChangedListener(watcher);
        edtPhoneNumber.addTextChangedListener(watcher);
        edtFbId.addTextChangedListener(watcher);



        // Set information
        photoUrl = SecondFragmentArgs.fromBundle(getArguments()).getPhotoUrl();
        displayName = SecondFragmentArgs.fromBundle(getArguments()).getDisplayName();
        UID = SecondFragmentArgs.fromBundle(getArguments()).getUID();

        // Firebase Database, Storage
        ref = FirebaseDatabase.getInstance().getReference().child(UID);
        storageRef = FirebaseStorage.getInstance().getReference();

        Glide.with(context).load(photoUrl).centerCrop().into(ivProfilePicture);
        tvName.setText(displayName);



        btnSubmit.setOnClickListener(v -> {
                MyLocation initLoc = new MyLocation();

            CustomTarget<Bitmap> target = new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    byte[] imageData = BitmapToByteArray(resource);
                    UploadTask task = storageRef.child(UID + ".png").putBytes(imageData);

                    task.addOnCompleteListener(task1 -> storageRef.child(UID + ".png").getDownloadUrl().addOnCompleteListener(task11 -> {
                        User u = new User(
                                displayName,
                                edtNickname.getText().toString(),
                                edtPhoneNumber.getText().toString(),
                                task11.getResult().toString(),
                                initLoc,
                                edtFbId.getText().toString()

                        );
                        ref.setValue(u, (error, ref) -> {
                            if(error == null){
                                startMapActivity();
                            }else{
                                Log.e(TAG, "CompletionListener: " + error.getMessage());
                            }
                        });
                    }).addOnFailureListener(e -> {
                        User u = new User(
                                displayName,
                                edtNickname.getText().toString(),
                                edtPhoneNumber.getText().toString(),
                                "",
                                initLoc,
                                edtFbId.getText().toString()
                        );
                        ref.setValue(u, (error, ref) -> {
                            if(error == null){
                                startMapActivity();
                            }else{
                                Log.e(TAG, "CompletionListener: " + error.getMessage());
                            }
                        });
                    })).addOnFailureListener(e -> {
                        User u = new User(
                                displayName,
                                edtNickname.getText().toString(),
                                edtPhoneNumber.getText().toString(),
                                "",
                                initLoc,
                                edtFbId.getText().toString()
                        );
                        ref.setValue(u, (error, ref) -> {
                            if(error == null){
                                startMapActivity();
                            }else{
                                Log.e(TAG, "CompletionListener: " + error.getMessage());
                            }
                        });
                    });

                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    Log.e(TAG, "onBitmapFailed");
                    User user = new User(
                            displayName,
                            edtNickname.getText().toString(),
                            edtPhoneNumber.getText().toString(),
                            "",
                            initLoc,
                            edtFbId.getText().toString()
                    );

                    ref.setValue(user, (error, ref) -> {
                        if(error == null){
                            startMapActivity();
                        }else{
                            Log.e(TAG, "CompletionListener: " + error.getMessage());
                        }
                    });
                }

                @Override
                public void onLoadStarted(@Nullable Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    Snackbar.make(requireView(), "Fortunately, it is correct!", 7000).show();
                }
            };
            Glide.with(context).asBitmap().load(photoUrl).centerCrop().into(target);
        });
    }

    private View.OnClickListener OnGetFacebookIdListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        (object, response) -> {
                            //Log.i("GraphRequest", object.toString());
                            Log.i("GraphRequest", response.toString());
                            try {
                                edtFbId.setText(object.getString("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id");
                request.setParameters(parameters);
                request.executeAsync();

            } catch (Exception e) {
                Log.d("ERROR", e.toString());
            }
        }
    };

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nickname = Objects.requireNonNull(edtNickname.getText()).toString().trim();
            String phone_number = Objects.requireNonNull(edtPhoneNumber.getText()).toString().trim();
            String fbuid = Objects.requireNonNull(edtFbId.getText()).toString().trim();

            btnSubmit.setEnabled(!nickname.isEmpty() && !phone_number.isEmpty() && !fbuid.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void startMapActivity() {
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        intent.putExtra("UID", UID);
        startActivity(intent);
    }

    private byte[] BitmapToByteArray(Bitmap bmp) {

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 50, bao);
        return bao.toByteArray();
    }



}