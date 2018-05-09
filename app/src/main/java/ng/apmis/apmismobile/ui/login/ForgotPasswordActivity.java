package ng.apmis.apmismobile.ui.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import butterknife.BindView;
import ng.apmis.apmismobile.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    ImageButton backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
    }
}
