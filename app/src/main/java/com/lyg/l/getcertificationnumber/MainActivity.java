package com.lyg.l.getcertificationnumber;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
//TTS
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    //sms list
    private ArrayList<Sms> smses;
    private sms_adapter adapter;
    private ListView mainpage_listview_sms;
    private ImageButton mainpage_imagebtn_refresh;

    //TTS
    private static TextToSpeech textToSpeech;
    private static float SpeachPitch = 1f;
    private static float SpeachRate = 0.5f;

    //permission
    private boolean is_permission_checked = false;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readSMS();
                    is_permission_checked = true;
                } else {
                    is_permission_checked = false;
                    Toast.makeText(MainActivity.this, "SMS 권한 사용 거부\n" +
                            " 어플리케이션이 정상적으로 동작하지 않습니다.\n" +
                            "재실행 후 권한 사용 동의 해주세요.", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TTS
        textToSpeech = new TextToSpeech(this, ttsInitListener);

        //sms
        smses = new ArrayList<Sms>();
        adapter = new sms_adapter(getApplicationContext(), R.layout.sms, smses);
        mainpage_listview_sms = (ListView)this.findViewById(R.id.Mainpage_listview_sms);
        mainpage_imagebtn_refresh = (ImageButton) this.findViewById(R.id.Mainpage_imagebtn_refresh);
        mainpage_listview_sms.setAdapter(adapter);


        //Permission
        if(is_permission_checked == false) {
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    is_permission_checked = true;
                } else {
                    is_permission_checked = false;
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_SMS},
                            1);
                }
            } else {
                readSMS();
            }
        }

        mainpage_imagebtn_refresh.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(is_permission_checked == true) {
                    readSMS();
                    mainpage_listview_sms.smoothScrollToPosition(0);
                }
                else{
                    Toast.makeText(MainActivity.this, "권한 사용 거부로 인해 동작하지 않습니다.\n" +
                            "재실행 후 권한 사용 동의 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(is_permission_checked ==true) {
            readSMS();
        }
    }

    //sms inbox read - 전화번호와 문자내용읽기
    private void readSMS() {

        //초기화 하고 다시 시작 - 중복 add 방지
        smses.clear();

        String phoneNumber = null;
        String text = null;

        ContentResolver contentResolver = this.getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = cursor.getColumnIndex("body");
        int indexAddr = cursor.getColumnIndex("address");

        //수신 문자메시지 함에서 문자메시지 검색
        if (indexBody < 0 || !cursor.moveToFirst()) return;
        do {
            phoneNumber = cursor.getString(indexAddr);
            text = cursor.getString(indexBody);
            //문자 메시지 내용이 "인증"을 포함할 경우
            if (text.contains("인증")) {
                Sms m = new Sms();
                String CertifyNum;

                //2017-04-01 "인증"문자에 4자리 이상의 숫자 유무 확인 추가 - 숫자없으면 작동하지 않도록 수정.
                //"인증"문자에서 4자리 이상의 숫자가 있는지 확인
                CertifyNum = text.replaceAll("[^0-9]", "");
                //4자리 이상의 숫자가 있으면,
                if(CertifyNum.length() > 3) {

                //발신 전화번호에 따라 각각의 회사명 출력
                    // switch문으로 변경 필 - 속도 향상
                if (phoneNumber.equals("114")) {
                    phoneNumber = "통신사";
                }
                else if (phoneNumber.equals("15771006")) {
                    phoneNumber = "SCI 평가정보";
                }
                else if (phoneNumber.equals("18994134")) {
                    phoneNumber = "드림시큐리티";
                }
                else if (phoneNumber.equals("123")) {
                    phoneNumber = "한전";
                }
                else if (phoneNumber.equals("101")) {
                    phoneNumber = "FREE U+zone";
                }
                else if (phoneNumber.equals("16001522")) {
                    phoneNumber = "NICE";
                }
                else if (phoneNumber.equals("0220338500")) {
                    phoneNumber = "한국모바일인증(주)";
                }
                else if (phoneNumber.equals("028204495")) {
                    phoneNumber = "서울지방병무청";
                }
                else if (phoneNumber.equals("15663355")) {
                    phoneNumber = "모바일결제";
                }
                else if (phoneNumber.equals("15996745")) {
                    phoneNumber = "SK플래닛";
                }
                else if (phoneNumber.equals("15882486")) {
                    phoneNumber = "NICE지키미";
                }
                else if (phoneNumber.equals("16000523")) {
                    phoneNumber = "모바일결제";
                }
                else if (phoneNumber.equals("027081000")) {
                    phoneNumber = "병무청";
                }
                else {
                    //do nothing
                }
                    m.setTel(phoneNumber);
                    m.setText(CertifyNum);
                    smses.add(0, m);
                }
            } else {
                //do nothing
            }
        }while (cursor.moveToNext());

        //역순 -> 최근순서대로 정렬되게.
        Collections.reverse(smses);
        adapter.notifyDataSetChanged();
    }


    //2017-04-01 TTS 기능 추가
    //TTS
    private TextToSpeech.OnInitListener ttsInitListener = new TextToSpeech.OnInitListener() {
        public void onInit(int version) {
            textToSpeech.setLanguage(Locale.KOREAN);
            textToSpeech.setPitch(SpeachPitch);
            textToSpeech.setSpeechRate(SpeachRate);
        }
    };

    public static void speak_Certification_Number(String sms)
    {
        textToSpeech.speak(sms, 0, null);
    }

    @Override
    protected void onDestroy() {
        if(textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
