package in.net.maitri.xb.registration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle b1 = intent.getExtras();
        Object[] pdu;
        SmsMessage[] msg ;
        String inNumber,inMsg;

        try{
            if (b1 != null) {

                pdu = (Object[]) b1.get("pdus");
                assert pdu != null;
                msg = new SmsMessage[pdu.length];

                for (int i = 0; i < pdu.length; i++) {

                    msg[i] = SmsMessage.createFromPdu((byte[]) pdu[i]);
                    inNumber = msg[i].getDisplayOriginatingAddress();
                    inMsg = msg[i].getDisplayMessageBody();
                    inMsg = inMsg.substring(inMsg.length()-7, inMsg.length()-1);

                    if (inNumber.contains("MISPLB")) {
                        OtpVerification.mOtpField.setText(inMsg);
                    }
                }
            }
        }catch(Exception e){

            e.printStackTrace();
        }

    }
}
