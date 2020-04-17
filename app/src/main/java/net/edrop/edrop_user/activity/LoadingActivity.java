package net.edrop.edrop_user.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.utils.SystemTransUtil;
import net.edrop.edrop_user.utils.WaitLoadingView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static net.edrop.edrop_user.utils.Constant.BASE_URL;

public class LoadingActivity extends AppCompatActivity {
    private WaitLoadingView loadingView;
    private final int REQUEST_CAMERA = 1;//请求相机权限的请求码
    private OkHttpClient okHttpClient;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(LoadingActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        initViews();

    }
    private void initViews() {
        loadingView = findViewById(R.id.loading);
        loadingView.start();
        okHttpClient=new OkHttpClient();


        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(LoadingActivity.this,
                    android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(LoadingActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            } else {
                //说明已经获取到摄像头权限了
                Intent intent1 = new Intent();
                intent1.setAction(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1, 100);
            }
        } else {
            //这个说明系统版本在6.0之下，不需要动态获取权限。
            Intent intent1 = new Intent();
            intent1.setAction(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent1, 100);

        }

    }
    //拍照成功回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
//            Log.e("拍照测试", "拍照成功");
            bitmap = (Bitmap) data.getExtras().get("data");//获取拍取的照片

            //BitMap转成文件
            File f = convertBitmapToFile(bitmap);
            postFile(f);

        }

    }

    private byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private File convertBitmapToFile(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        File file = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            int x = 0;
            byte[] b = new byte[1024 * 100];
            while ((x = is.read(b)) != -1) {
                fos.write(b, 0, x);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    //通过okhttp传图片给服务器
    private void postFile(File f) {
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/octet-stream"), f);
        Request.Builder builder = new Request.Builder();
        Request request = builder.post(requestBody).url(BASE_URL + "indentify").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String success = response.body().string();
//                Log.e("11111", success);
                byte[] buf = new byte[1024];
                buf=Bitmap2Bytes(bitmap);
                Intent intent = new Intent(LoadingActivity.this,RecognitionResultActivity.class);
                intent.putExtra("photo_bmp",buf);
                intent.putExtra("json",success);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            Intent intent1 = new Intent();
            intent1.setAction(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            Log.e("权限获取成功", "onRequestPermissionsResult: ");
            startActivityForResult(intent1, 100);
        }
    }
}
