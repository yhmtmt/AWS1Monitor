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

public class RadarConfig extends Fragment implements View.OnClickListener {
    private MapsActivity app = null;
    private Button btnOn, btnOff, btnRange, btnGain, btnRain, btnSea;
    private EditText editValue;
    private TextView textInformation = null;
    private String radarInformation = null;
    static public RadarConfig createInstance(MapsActivity app_)
    {
        RadarConfig radarConfig =  new RadarConfig();
        radarConfig.app = app_;
        return radarConfig;
    }

    public void setInformation(String radarInformation_)
    {
        radarInformation = radarInformation_;
        if(textInformation != null)
            textInformation.setText(radarInformation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.radar_config, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnOn = (Button)view.findViewById(R.id.radar_on);
        btnOff = (Button)view.findViewById(R.id.radar_off);
        btnRange = (Button)view.findViewById(R.id.set_range);
        btnGain = (Button)view.findViewById(R.id.set_gain);
        btnRain = (Button)view.findViewById(R.id.set_rain);
        btnSea = (Button)view.findViewById(R.id.set_sea);
        btnOn.setOnClickListener(this);
        btnOff.setOnClickListener(this);
        btnRange.setOnClickListener(this);
        btnGain.setOnClickListener(this);
        btnRain.setOnClickListener(this);
        btnSea.setOnClickListener(this);

        textInformation = (TextView) view.findViewById(R.id.radar_information);
        editValue = (EditText) view.findViewById(R.id.edit_radar_range);
        editValue.setText("0");
    }

    @Override
    public void onClick(View view) {
        Button btn = (Button)view;
        SpannableStringBuilder editor = (SpannableStringBuilder) editValue.getText();
        int value = Integer.parseInt(editor.toString());

        switch(btn.getId()){
            case R.id.radar_on:
                app.getUICommandObject().turnOnRadar();
                break;
            case R.id.radar_off:
                app.getUICommandObject().turnOffRadar();
                break;
            case R.id.set_range:
                app.getUICommandObject().setRadarRange(value);
                break;
            case R.id.set_gain:
                if(value > 100)
                    app.getUICommandObject().setRadarGainAuto();
                else
                    app.getUICommandObject().setRadarGain(value);
                break;
            case R.id.set_rain:
                if(value < 0)
                    app.getUICommandObject().setRadarRainOff();
                else
                    app.getUICommandObject().setRadarRain(value);
                break;
            case R.id.set_sea:
                if(value < 0)
                    app.getUICommandObject().setRadarSeaOff();
                else if(value > 100)
                    app.getUICommandObject().setRadarSeaAuto();
                else
                    app.getUICommandObject().setRadarSea(value);
        }
    }
}
