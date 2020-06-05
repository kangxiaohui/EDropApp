package net.edrop.edrop_user.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.utils.SystemTransUtil;

public class CopyrightInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copyright_information);
        findViewById(R.id.iv_copyright_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
