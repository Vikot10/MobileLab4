package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraAccessException;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private String cameraId;
    private boolean isActive = false;
    private Button btn;
    static final char[] english = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            ',', '.', '?' };

    static final String[] morse = { ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..",
            ".---", "-.-", ".-..", "--", "-.", "---", ".---.", "--.-", ".-.",
            "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----",
            "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.",
            "-----", "--..--", ".-.-.-", "..--.." };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
                String s = "123";
                try {
                    assert cameraManager != null;
                    cameraId = cameraManager.getCameraIdList()[0];
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                if (!isActive) {
                    try {
                        cameraManager.setTorchMode(cameraId, true);
                        btn.setText("Выключить фонарик");
                        isActive = true;
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        cameraManager.setTorchMode(cameraId, false);
                        btn.setText("Включить фонарик");
                        isActive = false;
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void onClicShifr(View view)
    {
        EditText text = findViewById(R.id.messageInput);
        String message = text.getText().toString();

        String shifrMessage = engToMorse(message);

        EditText text1 = findViewById(R.id.messageOutput);
        text1.setText(shifrMessage);
    }

    public static String engToMorse(String s)
    {
        s = s.toLowerCase();
        char[] chars = s.toCharArray();
        String str = "";
        for (int i = 0; i < chars.length; i++){
            for (int j = 0; j < english.length; j++){
                if (english[j] == chars[i]){
                    str = str + morse[j] + " ";
                    break;
                }
            }
        }
        return str;
    }
    public void onClickMusic(View view)
    {
        EditText text = findViewById(R.id.messageOutput);
        String message = text.getText().toString();

        MorseCodePlayer morseCodePlayer = new MorseCodePlayer(this);
        morseCodePlayer.playMorseCode(message);
    }
    public class MorseCodePlayer {
        private static final long DOT_DURATION = 200; // длительность короткого сигнала в миллисекундах
        private static final long DASH_DURATION = DOT_DURATION * 3; // длительность длинного сигнала в миллисекундах
        private static final long SPACE_DURATION = DOT_DURATION * 3; // длительность паузы между символами в миллисекундах
        private static final long LETTER_SPACE_DURATION = DOT_DURATION * 7; // длительность паузы между словами в миллисекундах

        private final Vibrator vibrator;

        public MorseCodePlayer(Context context) {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }

        public void playMorseCode(String morseCode) {
            new Thread(() -> {
                for (char c : morseCode.toCharArray()) {
                    switch (c) {
                        case '.':
                            playDot();
                            sleep(SPACE_DURATION);
                            break;
                        case '-':
                            playDash();
                            sleep(SPACE_DURATION);
                            break;
                        case ' ':
                            sleep(SPACE_DURATION);
                            break;
                        default:
                            break;
                    }
                }
            }).start();
        }

        private void playDot() {
            vibrator.vibrate(DOT_DURATION);
            sleep(DOT_DURATION);
        }

        private void playDash() {
            vibrator.vibrate(DASH_DURATION);
            sleep(DASH_DURATION);
        }

        private void sleep(long duration) {
            try
            {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}