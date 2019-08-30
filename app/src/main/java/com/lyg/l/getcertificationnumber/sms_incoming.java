package com.lyg.l.getcertificationnumber;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by L on 2016-02-25.
 */
public class sms_incoming extends BroadcastReceiver{

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Boolean isVertifysms = false;

                    //"인증" 문자인지 확인
                    if (message.contains("인증")) {
                        isVertifysms = true;
                    } else {
                        //do nothing
                    }

                    //"인증"이 포함된 문자일 경우
                    if(isVertifysms == true) {
                        String CertifyNum = message.replaceAll("[^0-9]", "");

                        //2017-04-01 "인증"문자에 4자리 이상의 숫자 유무 확인 추가 - 숫자없으면 작동하지 않도록 수정.
                        //숫자가 포함된 문자일 경우 - 포함된 숫자가 4자리 이상
                        if (CertifyNum.length() > 3) {

                            //2017-04-01 TTS 기능 추가 - 화면이 꺼져있을 때만 TTS 기능 실행.
                            //화면 on/off
                            PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
                            boolean isScreenOn = pm.isScreenOn();

                            //화면이 off 일 때
                            if(isScreenOn == false) {
                                //TTS
                                MainActivity.speak_Certification_Number(CertifyNum);
                            }
                            //숫자만 잘라서 클립보드에 복사
                            ClipboardManager clipboard = (ClipboardManager)context.getSystemService(context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("label", CertifyNum);
                            clipboard.setPrimaryClip(clip);

                            //인증번호 출력 및 복사완료 문구 출력
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, "인증번호 : " + CertifyNum +"\n인증번호가 클립보드에 복사되었습니다", duration);
                            toast.show();
                        }
                        else{ //4자리 미만
                            //do nothing
                        }
                    }
                    //"인증" 포함 X
                    else{
                        //do nothing
                    }
                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
        /* 시스템 기본 app을 포함한 다른 모든 app에서 문자 수신을 못하게 됨. ==> 수신함, 수정필요?? */
        this.abortBroadcast();
    }
}