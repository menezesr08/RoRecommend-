package com.example.android.top10downloadedappnew.main_classes;
/**
 * This class calls the Home Fragment
 * @author Rohan Menezes
 **/
import android.content.res.Resources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.TypedValue;

import com.example.android.top10downloadedappnew.R;
import com.example.android.top10downloadedappnew.fragment.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        // update the main content by replacing fragments
        Fragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commitAllowingStateLoss();


    }


    public int dpToPx(int dp) {
        Resources r = getResources();

        //getDisplayMetrics - retrieves info about the screeen such as density, pixel length, width etc
        //TypedValue.Complex_UNIT_DP - a constant number which is a case in the switch statement for the applyDimension method.
        //the return value for calculates: dp * metrics.density (where metrics is a table of information of the screen)
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}









