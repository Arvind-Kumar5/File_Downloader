package com.example.filedownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    class ExampleRunnable implements Runnable{

        @Override
        public void run() {
            mockFileDownloader();
        }
    }

    private Button startButton;
    private static final String TAG = "MainActivity";
    private volatile boolean stopThread = false;
    private TextView downloadText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.button);
        downloadText = findViewById(R.id.textView);
    }

    public void mockFileDownloader(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Downloading...");
            }
        });

        for(int downloadProgress = 0; downloadProgress < 100; downloadProgress=downloadProgress+10) {
            if(stopThread){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setText("Start");
                        downloadText.setText("");
                    }
                });

                return;
            }

            if(stopThread){
                break;
            }

            int finalDownloadProgress = downloadProgress;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(stopThread) {
                        downloadText.setText("");
                        return;
                    }

                    downloadText.setText("Download Progress: " + String.valueOf(finalDownloadProgress) + "%");
                    Log.d(TAG, "Download Progress" + finalDownloadProgress + "%");
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Start");
                downloadText.setText("");
            }
        });
    }

    public void startDownload(View view){
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View view){
        stopThread = true;
    }
}