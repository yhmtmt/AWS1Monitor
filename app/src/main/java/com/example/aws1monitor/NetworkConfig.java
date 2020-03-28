package com.example.aws1monitor;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class NetworkConfig extends Fragment {
    String host = "192.168.100.192";
    int port = 50051;

    private MapsActivity app;

    private Button btnConnect = null;
    private EditText editHost;
    private EditText editPort;
    static public NetworkConfig createInstance(MapsActivity app_)
    {
        NetworkConfig networkConfig =  new NetworkConfig();
        networkConfig.app = app_;
        return networkConfig;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.network_config, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnConnect = (Button) view.findViewById(R.id.connect);
        editHost = (EditText) view.findViewById(R.id.editHost);
        editPort = (EditText) view.findViewById(R.id.editPort);
        editHost.setText(host);
        editPort.setText(Integer.toString(port));
        if(app.isConnected()){
            btnConnect.setText("Disconnect");
        }else{
            btnConnect.setText("Connect");
        }
        btnConnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(!app.isConnected()){
                    SpannableStringBuilder editor = (SpannableStringBuilder) editHost.getText();
                    host = editor.toString();
                    editor = (SpannableStringBuilder) editPort.getText();
                    port = Integer.parseInt(editor.toString());
                    app.connectAWS1(host, port);
                    btnConnect.setText("Disconnect");
                }else{
                    app.disconnectAWS1();
                    btnConnect.setText("Connect");
                }
            }
        });
    }
}
