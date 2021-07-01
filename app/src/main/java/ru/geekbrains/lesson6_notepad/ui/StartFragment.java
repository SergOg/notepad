package ru.geekbrains.lesson6_notepad.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import ru.geekbrains.lesson6_notepad.MainActivity;
import ru.geekbrains.lesson6_notepad.Navigation;
import ru.geekbrains.lesson6_notepad.R;

public class StartFragment extends Fragment {

    private static final int RC_SIGN_IN = 40404;
    private static final String TAG = "GoogleAuth";

    private Navigation navigation;

    private GoogleSignInClient googleSignInClient;

    private com.google.android.gms.common.SignInButton buttonSignIn;
    private TextView emailView;
    private MaterialButton continue_;
    private MaterialButton skip;

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
    }

    @Override
    public void onDetach() {
        navigation = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        initGoogleSign();
        initView(view);
        enableSign();
        return view;
    }

    private void initGoogleSign() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }

    private void initView(View view) {
        buttonSignIn = view.findViewById(R.id.sign_in_button);
        skip = view.findViewById(R.id.skip);
        buttonSignIn.setOnClickListener(v -> signIn());
        emailView = view.findViewById(R.id.email);
        continue_ = view.findViewById(R.id.continue_);
        continue_.setOnClickListener(v -> navigation.addFragment(ListNoteFragment.newInstance(), false));
        skip.setOnClickListener(v -> navigation.addFragment(ListNoteFragment.newInstance(), false));
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (account != null) {
            disableSign();
            updateUI(account.getEmail());
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
                }
            });

    private void signIn() {
        someActivityResultLauncher.launch(googleSignInClient.getSignInIntent());
    }
/*    private void signIn() {
        startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
        }
    }*/

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            disableSign();
            updateUI(account.getEmail());
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUI(String email) {
        emailView.setText(email);
    }

    private void enableSign() {
        buttonSignIn.setEnabled(true);
        continue_.setEnabled(false);
    }

    private void disableSign() {
        buttonSignIn.setEnabled(false);
        continue_.setEnabled(true);
    }
}
