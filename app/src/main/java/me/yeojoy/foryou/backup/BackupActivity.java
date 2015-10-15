package me.yeojoy.foryou.backup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import me.yeojoy.foryou.BaseActivity;
import me.yeojoy.foryou.R;

public class BackupActivity extends BaseActivity {

    private static final String TAG = BackupActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        // 버튼 객체
        Button btnSendEmail = (Button) findViewById(R.id.btn_send_email);

        // 전송할 파일의 경로
        String szSendFilePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/test.jpg";
        File f = new File(szSendFilePath);
        if (!f.exists()) {
            Toast.makeText(this, "파일이 없습니다.", Toast.LENGTH_SHORT).show();
        }

        // File객체로부터 Uri값 생성
        final Uri fileUri = Uri.fromFile(f);

        /** 첨부파일 이메일 보내기 */
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_SEND);
                it.setType("plain/text");

                // 수신인 주소 - tos배열의 값을 늘릴 경우 다수의 수신자에게 발송됨
                String[] tos = {"test@gmail.com"};
                it.putExtra(Intent.EXTRA_EMAIL, tos);

                it.putExtra(Intent.EXTRA_SUBJECT, "[혈압.혈당앱] 자료 백업 email.");
                it.putExtra(Intent.EXTRA_TEXT, "고객님께서 저장하신 혈압.혈당 기록을 보내드립니다" +
                        ".\n");

                // 파일첨부
                it.putExtra(Intent.EXTRA_STREAM, fileUri);

                startActivity(it);
            }
        });
    }

}
