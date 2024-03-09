package com.example.social_media_using_blockchain;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

public class MainActivity1 extends AppCompatActivity implements PaymentResultWithDataListener {

    TabLayout tab;
    ViewPager viewPager;
    Button btnEditProfile;
    ImageView token;
    TextView transactionDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1_main);
        transactionDetailsTextView = findViewById(R.id.transactionDetailsTextview);
        token = findViewById(R.id.token);
        Checkout.preload(getApplicationContext());
        Checkout co = new Checkout();
        co.setKeyID("rzp_test_SGzZ0R9mXF616B");
        token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPayment();
            }
        });
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");

        TextView usernameView = findViewById(R.id.username);
        usernameView.setText(username);
        // Display the username in the TextView
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        usernameTextView.setText(username);
          loadUserProfileData();

        tab = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewPager);

        ViewPagerProfileUIAdapter adapter = new ViewPagerProfileUIAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        tab.setupWithViewPager(viewPager);
        tab.getTabAt( 0).setIcon(R.drawable.reels_ic);
        tab.getTabAt(1).setIcon(R.drawable.ic_favorite);
        ImageView lockImageView = findViewById(R.id.lockImageView);
        ImageView homeimageview =findViewById(R.id.homeImageView);
        lockImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout user and navigate to LoginActivity
                logoutUser();
            }
        });
        btnEditProfile=findViewById(R.id.btnEditProfile);
        // MainActivity.java
// Inside the onClick listener for "Edit Profile" button
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfileIntent = new Intent(MainActivity1.this, EditProfileActivity.class);
                editProfileIntent.putExtra("USER_ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                editProfileIntent.putExtra("USERNAME", username);
                startActivity(editProfileIntent);
            }
        });
        homeimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity1.this, HomeActivity.class);
                startActivity(intent);
            }
        });




    }
    private void loadUserProfileData() {
        // Get the user ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Load profile picture from Firebase Storage
        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference()
                .child("profile_pics/" + userId + ".jpg");

        ImageView profilePicImageView = findViewById(R.id.imageViewProfilePic);
        // Use your preferred image loading library to load the image into ImageView
        // Example using Glide:
        // Glide.with(this).load(profilePicRef).into(profilePicImageView);

        // Load the username from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Retrieve username and set it to TextView
                    String username = documentSnapshot.getString("username");
                    TextView usernameTextView = findViewById(R.id.usernameTextView);
                    usernameTextView.setText(username);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure
                // You may want to log the error or provide a default value for username
            }
        });

        // Example using Glide to load profile picture (replace it with your preferred library)
        Glide.with(this).load(profilePicRef).into(profilePicImageView);
    }



    public static class ViewPagerProfileUIAdapter extends FragmentPagerAdapter {

        public ViewPagerProfileUIAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return new ReelsFragment();
            } else  {
                return new FavoriteFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;//no of tabs
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
                return null;
        }

    }

    public static class FavoriteFragment extends Fragment {


        public FavoriteFragment() {
            // Required empty public constructor
        }

        public static FavoriteFragment newInstance() {

            Bundle args = new Bundle();

            FavoriteFragment fragment = new FavoriteFragment();
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_favorite, container, false);
        }
    }

    public static class ProfileMainActivity extends AppCompatActivity {

        TabLayout tab;
        ViewPager viewPager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            tab = findViewById(R.id.tab);
            viewPager = findViewById(R.id.viewPager);

            ViewPagerProfileUIAdapter adapter = new ViewPagerProfileUIAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);


           tab.setupWithViewPager(viewPager);
           tab.getTabAt( 0).setIcon(R.drawable.reels_ic);
           tab.getTabAt(1).setIcon(R.drawable.ic_favorite);
        }


    }

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link ReelsFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public static class ReelsFragment extends Fragment {


        public ReelsFragment() {
            // Required empty public constructor
        }

        public static ReelsFragment newInstance() {

            Bundle args = new Bundle();

            ReelsFragment fragment = new ReelsFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_reels, container, false);
        }
    }
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity1.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    private void initPayment() {
        Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "CoCreate");
            options.put("description", "Demoing Charges");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            options.put("theme.color", "#FF1515");
            options.put("currency", "INR");
            options.put("amount", "50000");

            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            JSONObject prefill = new JSONObject();
            prefill.put("email", "pandeyabhishek11052002@gmail.com");
            prefill.put("contact", "7045933043");

            options.put("prefill", prefill);
            co.open(this, options);
        } catch (Exception e) {
            Toast.makeText(this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        String transactionDetails =  s;
        transactionDetailsTextView.setText(transactionDetails);
        Toast.makeText(this, "Payment successful" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        String transactionDetails =  s;
        transactionDetailsTextView.setText(transactionDetails);
        Toast.makeText(this, "Error" + s , Toast.LENGTH_SHORT).show();
    }
}