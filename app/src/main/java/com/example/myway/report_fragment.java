package com.example.myway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class report_fragment extends Fragment {
    EditText report;
    Button send;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.report_fragment, container, false);
        report=view.findViewById(R.id.report_fragment_et);
        send=view.findViewById(R.id.report_fragment_send_btn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        return view;
    }
    protected void sendEmail() {
        String[] TO = {"lironhamo8@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MY WAY REPORT");
        emailIntent.putExtra(Intent.EXTRA_TEXT, report.getText().toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.d("TAGLIRON","MAIL SENT");
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }
}
