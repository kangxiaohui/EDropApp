package net.edrop.edrop_user.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.utils.SystemTransUtil;

public class RubbishDesc02Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(RubbishDesc02Activity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubbish_desc02);
        ImageView imageView = findViewById(R.id.iv_rubbish02);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
