package com.lyg.l.getcertificationnumber;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

//COPY FUNCTION
import android.content.ClipboardManager;

/**
 * Created by L on 2016-01-17.
 */
public class sms_adapter extends ArrayAdapter<Sms>{
    private ArrayList<Sms> items;
    private Context context;
    private int resId;
    private Smsholder holder = null;

    public sms_adapter(Context context, int textViewResourceId, ArrayList<Sms> objects){
        super(context, textViewResourceId, objects);
        this.context = context;
        resId = textViewResourceId;
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View row = convertView;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(resId, null);
            holder = new Smsholder();
            holder.SMSTelTextView = (TextView) row.findViewById(R.id.Phone_number);
            holder.SMSContentTextView = (TextView) row.findViewById(R.id.Text_content);
            holder.Telinfo = (TextView) row.findViewById(R.id.telinfo);
            holder.Contentinfo = (TextView) row.findViewById(R.id.contentinfo);

            row.setTag(holder);
        }else {
            holder = (Smsholder) row.getTag();
        }
        final Sms sms = items.get(position);
        holder.SMSTelTextView.setText(sms.getTel());
        holder.SMSContentTextView.setText(sms.getText());

        holder.SMSTelTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", sms.getText());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "인증번호(" + sms.getText() + ")가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.SMSContentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ClipboardManager clipboard = (ClipboardManager)getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", sms.getText());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "인증번호(" + sms.getText()+")가 클립보드에 복사되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        holder.Telinfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ClipboardManager clipboard = (ClipboardManager)getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", sms.getText());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "인증번호(" + sms.getText()+")가 클립보드에 복사되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        holder.Contentinfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ClipboardManager clipboard = (ClipboardManager)getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", sms.getText());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "인증번호(" + sms.getText()+")가 클립보드에 복사되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        return row;
    }

    static class Smsholder {
        TextView SMSTelTextView;
        TextView SMSContentTextView;
        TextView Telinfo;
        TextView Contentinfo;
    }
}
