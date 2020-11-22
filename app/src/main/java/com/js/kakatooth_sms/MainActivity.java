package com.js.kakatooth_sms;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import java.sql.BatchUpdateException;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build.VERSION;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;

//import com.kakao.kakaolink.KakaoLink;
//import com.kakao.util.KakaoParameterException;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private DrawerLayout drawerLayout;
    private View drawerView;
//    public KakaoLink kakaoLink;
    Intent intent45;
    SpeechRecognizer mRecognizer;
    Button sttBtn;
    TextView textView;
    TextView textviewc;
    TextView sttview;
    Button tt;
    TextView nameTag;
    final int PERMISSION = 1;

    private static final int REQUEST_ENABLE_BT = 10;
    private BroadcastReceiver kakaoReceiver;
    ArrayAdapter<String> arrayAdapter;
    /* access modifiers changed from: private */
    public AudioManager audioManager;
    private BluetoothAdapter bluetoothAdapter;
    public TextView deviceview;
    private Context mContext;


    BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            if ("android.bluetooth.device.action.ACL_CONNECTED".equals(action)) {
                assert device != null;
                MainActivity.this.deviceview.setText(device.getName());
            } else if ("android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)) {
                MainActivity.this.deviceview.setText("없음");
            }
        }
    };
    /* access modifiers changed from: private */
    private TextView textlength;
    public Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    /* access modifiers changed from: private */
    public Button button6;
    /* access modifiers changed from: private */
    public Button button7;
    /* access modifiers changed from: private */
    public Button button8;
    /* access modifiers changed from: private */
    public Button button9;
    public Button button11;
    /* access modifiers changed from: private */
    private TextView editText;
    ArrayList<String> engineList;
    ArrayList<String> engineSpinnerList;
    private int isTTSReady = -2;
    private MediaPlayer nMediaPlayer;
    /* access modifiers changed from: private */
    public double speed = 1.0d;
    private Spinner spinner;
    private Spinner spinner2;
    private TextToSpeech tts;
    /* access modifiers changed from: private */
    public String ttsengine;
    private SeekBar volumebar;
    long now = System.currentTimeMillis();
    Date mDate = new Date(now);
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String getTime = simpleDate.format(mDate);


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maindisplay);

//        try{
//            kakaoLink = kakaoLink.getKakaoLink(MainActivity.this);
//        }catch (KakaoParameterException e){
//            e.printStackTrace();
//        }

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);
        Button btn_open = (Button)findViewById(R.id.btn_open);

        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        drawerLayout.setDrawerListener(listener1);
        drawerView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        boolean isPermissionAllowed = isNotiPermissionAllowed();
        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }
        nameTag = (TextView)findViewById(R.id.nameTag);
        textView = (TextView)findViewById(R.id.textViewv);
        textviewc = (TextView)findViewById(R.id.textView23);

        sttBtn = (Button) findViewById(R.id.sttBtn);

        intent45=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent45.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        intent45.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        if (!isPermissionAllowed) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }


        this.tts = new TextToSpeech(this, this, this.ttsengine);
        this.editText = (TextView) findViewById(R.id.SMSText);
        this.deviceview = (TextView) findViewById(R.id.deviceview);
        this.sttview=(TextView) findViewById(R.id.sttview);
        this.button1 = (Button) findViewById(R.id.button1);
        this.button2 = (Button) findViewById(R.id.button2);
        this.button3 = (Button) findViewById(R.id.button3);
        this.button4 = (Button) findViewById(R.id.button4);
        this.button5 = (Button) findViewById(R.id.button5);
        this.button6 = (Button) findViewById(R.id.button6);
        this.button7 = (Button) findViewById(R.id.button7);
        this.button8 = (Button) findViewById(R.id.button8);
        this.button9 = (Button) findViewById(R.id.button9);
        this.button11 = (Button) findViewById(R.id.button11);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        this.spinner2 = (Spinner) findViewById(R.id.spinner2);
        this.volumebar = (SeekBar) findViewById(R.id.volumeBar);
        this.audioManager = (AudioManager) getSystemService("audio");
        this.nMediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.textlength = (TextView) findViewById(R.id.textlength);
        mContext = this;


        kakaoR();
        processIntent(getIntent());
        state();

        Regex(this.tts.getEngines().toString());

        String str = "android.permission.RECEIVE_SMS";
        int permission = ContextCompat.checkSelfPermission(this, str);
        if (permission == 0) {
            Toast.makeText(this, "SMS 수신 권한 주어져 있음.", Toast.LENGTH_LONG).show();
            this.button3.setBackgroundResource(R.drawable.on2);

        } else {
            Toast.makeText(this, "SMS 수신 권한 없음.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{str}, 1);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, str)) {
                Toast.makeText(this, "SMS 권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{str}, 1);
            }
        }
        this.button1.setTag(permission);
        this.button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Objects.equals(MainActivity.this.button1.getBackground().getConstantState(), MainActivity.this.getResources().getDrawable(R.drawable.smiling).getConstantState())) {
                    MainActivity.this.button1.setBackgroundResource(R.drawable.smile);
                    PreferenceManager.setBoolean(mContext,"check1",true);
                } else {
                    MainActivity.this.button1.setBackgroundResource(R.drawable.smiling);
                    PreferenceManager.setBoolean(mContext,"check1",false);
                }
            }
        });
        this.button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", MainActivity.this.getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(intent);
            }
        });
        this.button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent("android.settings.BLUETOOTH_SETTINGS"));
            }
        });
        this.button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Objects.equals(MainActivity.this.button6.getBackground().getConstantState(), MainActivity.this.getResources().getDrawable(R.drawable.on2).getConstantState())) {
                    MainActivity.this.button6.setBackgroundResource(R.drawable.off2);
                    PreferenceManager.setBoolean(mContext,"check6",true);
                } else {
                    MainActivity.this.button6.setBackgroundResource(R.drawable.on2);
                    PreferenceManager.setBoolean(mContext,"check6",false);
                }
            }
        });
        this.button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Objects.equals(MainActivity.this.button7.getBackground().getConstantState(), MainActivity.this.getResources().getDrawable(R.drawable.on2).getConstantState())) {
                    MainActivity.this.button7.setBackgroundResource(R.drawable.off2);
                    PreferenceManager.setBoolean(mContext,"check7",true);
                } else {
                    MainActivity.this.button7.setBackgroundResource(R.drawable.on2);
                    PreferenceManager.setBoolean(mContext,"check7",false);
                }
            }
        });
        this.button8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Objects.equals(MainActivity.this.button8.getBackground().getConstantState(), MainActivity.this.getResources().getDrawable(R.drawable.on2).getConstantState())) {
                    MainActivity.this.button8.setBackgroundResource(R.drawable.off2);
                    PreferenceManager.setBoolean(mContext,"check8",true);
                } else {
                    MainActivity.this.button8.setBackgroundResource(R.drawable.on2);
                    PreferenceManager.setBoolean(mContext,"check8",false);
                }
            }
        });
        this.button9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Objects.equals(MainActivity.this.button9.getBackground().getConstantState(), MainActivity.this.getResources().getDrawable(R.drawable.on2).getConstantState())) {
                    MainActivity.this.button9.setBackgroundResource(R.drawable.off2);
                    PreferenceManager.setBoolean(mContext,"check9",true);
                } else {
                    MainActivity.this.button9.setBackgroundResource(R.drawable.on2);
                    PreferenceManager.setBoolean(mContext,"check9",false);
                }
            }
        });
        this.button11.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                stopPlay();

            }
        });
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedItemPosition = parent.getSelectedItemPosition();
                if (selectedItemPosition == 0) {
                    MainActivity.this.speed = 0.5d;
                } else if (selectedItemPosition == 1) {
                    MainActivity.this.speed = 1.0d;
                } else if (selectedItemPosition == 2) {
                    MainActivity.this.speed = 2.0d;
                }

                StringBuilder sb = new StringBuilder();
                sb.append("speed : ");
                sb.append(MainActivity.this.speed);
                Log.d("spinner", sb.toString());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity mainActivity = MainActivity.this;
                mainActivity.ttsengine = (String) mainActivity.engineList.get(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothAdapter bluetoothAdapter2 = this.bluetoothAdapter;
        if (bluetoothAdapter2 != null) {
            if (bluetoothAdapter2.isEnabled()) {
                this.button5.setBackgroundResource(R.drawable.on2);
            } else {
                this.button5.setBackgroundResource(R.drawable.off2);
                startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 10);
            }
        }
        registerReceiver(this.bluetoothReceiver, new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
        registerReceiver(this.bluetoothReceiver, new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"));
        registerReceiver(this.kakaoReceiver, new IntentFilter("android.service.notification.NotificationListenerService"));
        setSeekBar();



        this.sttBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stt();
            }
        });
    }

    DrawerLayout.DrawerListener listener1 = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {
            String message;
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";

                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }
            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            for(int i = 0; i < matches.size() ; i++){
                textView.setText(matches.get(i));
            }
            if(textView.getText().toString().equals(textviewc.getText().toString())){
                stopPlay();

            }else if(textView.getText().toString().equals(sttview.getText().toString())){
                stopPlay();
            }

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 1 || grantResults.length <= 0) {
            return;
        }
        if (grantResults[0] == 0) {
            Toast.makeText(this, "SMS 수신권한을 사용자가 승인함", Toast.LENGTH_LONG).show();
            this.button3.setBackgroundResource(R.drawable.on2);
            this.button1.setBackgroundResource(R.drawable.smiling);
        } else if (grantResults[0] == -1) {
            Toast.makeText(this, "SMS 수신권한을 사용자가 거부함", Toast.LENGTH_LONG).show();
            this.button3.setBackgroundResource(R.drawable.off2);
            this.button1.setBackgroundResource(R.drawable.smile);
        } else {
            Toast.makeText(this, "SMS 수신권한을 부여받지 못함", Toast.LENGTH_LONG).show();
            this.button3.setBackgroundResource(R.drawable.off2);
            this.button1.setBackgroundResource(R.drawable.smile);
        }
    }


    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 0) {
            this.button5.setBackgroundResource(R.drawable.off2);
        } else if (requestCode == 10 && resultCode == -1) {
            this.button5.setBackgroundResource(R.drawable.on2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void processIntent(Intent intent) {
        if (intent != null) {
            String sender = intent.getStringExtra("sender");
            String contents = intent.getStringExtra("contents");
            String date2 = intent.getStringExtra("receivedDate");



            if ("KAKAO".equalsIgnoreCase(intent.getStringExtra("class"))) {
                date2 = getTime;
            }

            if (sender == null || contents == null) {
                this.editText.setText(null);
            } else {
                TextView textView = this.editText;
                StringBuilder sb = new StringBuilder();

                sb.append(sender);
                sb.append(10);
                sb.append(contents);
                sb.append(10);
                sb.append(date2);
                textView.setText(sb.toString());
               /* Intent intent3 = new Intent();
                intent3.setAction(Intent.ACTION_MAIN);
                intent3.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent3);*/



                //moveTaskToBack(true);
            }

            if (!Objects.equals(this.button1.getBackground().getConstantState(), getResources().getDrawable(R.drawable.smiling).getConstantState())) {
                return;
            }

            if (Objects.equals(this.button7.getBackground().getConstantState(), getResources().getDrawable(R.drawable.on2).getConstantState())) {
                if (Objects.equals(this.button8.getBackground().getConstantState(), getResources().getDrawable(R.drawable.on2).getConstantState())) {
                    if (Objects.equals(this.button9.getBackground().getConstantState(), getResources().getDrawable(R.drawable.on2).getConstantState())) {
                        StringBuilder sb2 = new StringBuilder();
                        //sb2.append(sender);
                        assert contents != null;
                        if (contents.length()>=11){
                            sb2.append(contents.substring(0,10));
                            sb2.append(date2);
                            TextToSpeech(sb2.toString());
                            return;

                        }else{

                            sb2.append(contents);
                            sb2.append(date2);
                            TextToSpeech(sb2.toString());

                        }
                    }

                    StringBuilder sb3 = new StringBuilder();
                    assert contents != null;
                    if (contents.length()>=11) {
                        sb3.append(contents.substring(0, 10));

                        TextToSpeech(sb3.toString());
                    }else{
                        sb3.append(contents);

                        TextToSpeech(sb3.toString());
                    }
                   


                } else if (Objects.equals(this.button9.getBackground().getConstantState(), getResources().getDrawable(R.drawable.on2).getConstantState())) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(date2);
                    TextToSpeech(sb4.toString());
                }
            } else if (Objects.equals(this.button8.getBackground().getConstantState(), getResources().getDrawable(R.drawable.on2).getConstantState())) {
                if (Objects.equals(this.button9.getBackground().getConstantState(), getResources().getDrawable(R.drawable.on2).getConstantState())) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(contents);
                    sb5.append(date2);
                    TextToSpeech(sb5.toString());
                    return;
                }
                TextToSpeech(contents);
            } else if (Objects.equals(this.button9.getBackground().getConstantState(), getResources().getDrawable(R.drawable.on2).getConstantState())) {
                TextToSpeech(date2);
            }

        }

    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        String str = "android.permission.RECEIVE_SMS";
        int permission = ContextCompat.checkSelfPermission(this, str);
        if (permission == 0) {
            Toast.makeText(this, "SMS 수신 권한 주어져 있음.", Toast.LENGTH_LONG).show();
            this.button3.setBackgroundResource(R.drawable.on2);
        } else {
            Toast.makeText(this, "SMS 수신 권한 없음.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{str}, 1);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, str)) {
                Toast.makeText(this, "SMS 권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{str}, 1);
            }
        }
        this.button1.setTag(permission);
        this.button1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                if (Objects.equals(MainActivity.this.button1.getBackground().getConstantState(), MainActivity.this.getResources().getDrawable(R.drawable.smiling).getConstantState())) {
                    MainActivity.this.button1.setBackgroundResource(R.drawable.smile);
                } else if ((Integer) view.getTag() == 0) {
                    MainActivity.this.button1.setBackgroundResource(R.drawable.smiling);
                }
            }
        });
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothAdapter bluetoothAdapter2 = this.bluetoothAdapter;
        if (bluetoothAdapter2 == null) {
            this.button5.setBackgroundResource(R.drawable.off2);
        } else if (bluetoothAdapter2.isEnabled()) {
            this.button5.setBackgroundResource(R.drawable.on2);
        } else {
            this.button5.setBackgroundResource(R.drawable.off2);
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 10);
        }
        setSeekBar();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
        super.onNewIntent(intent);

    }

    private boolean isNotiPermissionAllowed() {
        Set<String> Sets = NotificationManagerCompat.getEnabledListenerPackages(this);

        return Sets.contains(getPackageName());
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void TextToSpeech(String str) {
        if (this.isTTSReady == 0) {
            this.tts.setPitch(1.0f);
            this.tts.setSpeechRate((float) this.speed);
            String str2 = "utteranceId";
            if (VERSION.SDK_INT < 21) {
                HashMap<String, String> map = new HashMap<>();
                map.put(str2, "UniqueID");
                if (Objects.equals(this.button6.getBackground().getConstantState(), getResources().getDrawable(R.drawable.on2).getConstantState())) {
                    this.nMediaPlayer.start();
                    SystemClock.sleep(1000);
                    this.tts.speak(str, 0, map);
                    return;
                }
                this.tts.speak(str, 0, map);
            } else if (Objects.equals(this.button6.getBackground().getConstantState(), getResources().getDrawable(R.drawable.on2).getConstantState())) {
                this.nMediaPlayer.start();
                SystemClock.sleep(1000);
                this.tts.speak(str, 0, null, str2);
            } else {
                String str3 = "SMSToSpeech";
                if (this.tts.speak(str, 1, null, str2) >= 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("=====> TextToSpeech LOLLIPOP speak SUCCESS <=====");
                    sb.append(str);
                    Log.d(str3, sb.toString());
                    return;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("=====> TextToSpeech LOLLIPOP speak ERROR <=====");
                sb2.append(str);
                Log.d(str3, sb2.toString());
            }
        }
    }

    private void stt(){
        mRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(intent45);
    }

    private void kakaoR() {
        this.kakaoReceiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase("android.service.notification.NotificationListenerService")) {
                    String head = intent.getExtras().getString("head");
                    String body = intent.getExtras().getString("body");
                    if (body != null || head != null) {
                        Intent intent5 = new Intent();
                        StringBuilder sb = new StringBuilder();

                        assert head != null;
                        if (head.equals(nameTag.getText().toString())){
                            intent5.putExtra("sender", head);
                            intent5.putExtra("contents", body);
                            intent5.putExtra("class", "KAKAO");
                            textView.setText("음성인식");
                            nameTag.setText(head);
                            Handler handler =new Handler();
                            Runnable r = () -> stt();
                            textView.setText("음성인식");

                            Runnable p = new Runnable() {
                                @Override
                                public void run() {
                                    MainActivity.this.processIntent(intent5);
                                }
                            };
                            handler.postDelayed(p,6000);




                        }else{
                            sb.append(head);
                            sb.append("에게서 문자가 왔습니다 받으시겠습니까?");
                            TextToSpeech(sb.toString());
                            tts.playSilence(3000,TextToSpeech.QUEUE_ADD,null);
                            intent5.putExtra("sender", head);
                            intent5.putExtra("contents", body);
                            intent5.putExtra("class", "KAKAO");

                            nameTag.setText(head);
                            Handler handler =new Handler();
                            Runnable r = () -> stt();
                            handler.postDelayed(r,3000);
                            textView.setText("음성인식");

                            Runnable p = new Runnable() {
                                @Override
                                public void run() {
                                        MainActivity.this.processIntent(intent5);
                                }
                            };
                                handler.postDelayed(p,6000);

//                            if(!textView.getText().toString().equals(textviewc.getText().toString())){
//                                textView.setText("음성인식");
//                                MainActivity.this.processIntent(intent5);
//                            }
                        }

                    }
                }

            }
        };


    }


    public void state(){


        boolean boo6 = PreferenceManager.getBoolean(mContext,"check6");
        boolean boo7 = PreferenceManager.getBoolean(mContext,"check7");
        boolean boo8 = PreferenceManager.getBoolean(mContext,"check8");
        boolean boo9 = PreferenceManager.getBoolean(mContext,"check9");


        if (boo6) {
            MainActivity.this.button6.setBackgroundResource(R.drawable.off2);
        } else {
            MainActivity.this.button6.setBackgroundResource(R.drawable.on2);
        }
        if (boo7) {
            MainActivity.this.button7.setBackgroundResource(R.drawable.off2);
        } else {
            MainActivity.this.button7.setBackgroundResource(R.drawable.on2);
        }
        if (boo8) {
            MainActivity.this.button8.setBackgroundResource(R.drawable.off2);
        } else {
            MainActivity.this.button8.setBackgroundResource(R.drawable.on2);
        }
        if (boo9) {
            MainActivity.this.button9.setBackgroundResource(R.drawable.off2);
        } else {
            MainActivity.this.button9.setBackgroundResource(R.drawable.on2);
        }
    }

    public void setSeekBar() {
        int nMax = this.audioManager.getStreamMaxVolume(3);
        int nCurrentVolume = this.audioManager.getStreamVolume(3);
        this.volumebar.setMax(nMax);
        this.volumebar.setProgress(nCurrentVolume);
        this.volumebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MainActivity.this.audioManager.setStreamVolume(3, progress, 0);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void Regex(String str) {
        Matcher m = Pattern.compile("name=[A-za-z.]+").matcher(str);
        this.engineList = new ArrayList<>();
        this.engineSpinnerList = new ArrayList<>();
        int i = 0;
        while (m.find()) {
            this.engineList.add(m.group().substring(5));
            if (m.group().substring(5).equals("com.google.android.tts")) {
                this.engineSpinnerList.add("구글TTS");
            } else if (m.group().substring(5).equals("com.samsung.SMT")) {
                this.engineSpinnerList.add("삼성TTS");
            } else {
                this.engineSpinnerList.add(this.engineList.get(i));
            }
            i++;
        }
        this.arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, this.engineSpinnerList);
        this.spinner2.setAdapter(this.arrayAdapter);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        TextToSpeech textToSpeech = this.tts;
        if (textToSpeech != null) {
            textToSpeech.stop();
            this.tts.shutdown();
            this.tts = null;
        }
    }

    public void stopPlay(){
        TextToSpeech texss = this.tts;
        if(texss !=null) {
            texss.stop();
        }

    }

    @Override
    public void onInit(int status) {
        if (status == 0) {
            int language = this.tts.setLanguage(Locale.KOREAN);
            if (language == -1 || language == -2) {
                this.editText.setText("지원하지 않는 언어입니다.");
            } else {
                this.isTTSReady = status;
            }
        } else {
            this.editText.setText("TTS 작업이 실패하였습니다.");
        }


    }


}

