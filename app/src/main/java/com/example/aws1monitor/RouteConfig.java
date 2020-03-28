package com.example.aws1monitor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.aws1monitor.AWS.Filter.UIManagerMsg.Waypoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.List;

import androidx.fragment.app.Fragment;

import static java.lang.Thread.sleep;

public class RouteConfig extends Fragment implements View.OnClickListener {
    int selected = -1;
    List<Marker> waypoints = null;
    List<Polyline> lines = null;
    Button btnClear = null;
    Button btnRefresh = null;
    Button btnDelete = null;
    Button btnReverse = null;
    MapsActivity app = null;

    void setSelected(int selected_)
    {
        selected = selected_;
    }


    public static RouteConfig createInstance(MapsActivity app_, List<Marker> waypoints_, List<Polyline> lines_)
    {
        RouteConfig routeConfig = new RouteConfig();
        routeConfig.app = app_;
        routeConfig.waypoints = waypoints_;
        routeConfig.lines = lines_;
        return routeConfig;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.route_config, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnClear = (Button) view.findViewById(R.id.clr);
        btnDelete = (Button) view.findViewById(R.id.del);
        btnRefresh = (Button) view.findViewById(R.id.ref);
        btnReverse = (Button) view.findViewById(R.id.rev);
        btnClear.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnReverse.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Button btn = (Button)view;
        switch(btn.getId()){
            case R.id.clr:
                for(int i = waypoints.size() - 1; i >= 0; i--){
                    app.getUICommandObject().delWaypoint(i);
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.del:
                if(selected != -1) {
                    app.getUICommandObject().delWaypoint(selected);
                    selected = Math.min(selected, waypoints.size() - 1);
                    if (selected != -1) {
                        app.getGoogleMap().moveCamera(CameraUpdateFactory.newLatLng(waypoints.get(selected).getPosition()));
                        waypoints.get(selected).showInfoWindow();
                    }
                }
                break;
            case R.id.ref:
                app.getUICommandObject().refreshWaypoint();
                break;
            case R.id.rev:
                app.getUICommandObject().reverseWaypoint();
                if (selected != -1) {
                    app.getGoogleMap().moveCamera(CameraUpdateFactory.newLatLng(waypoints.get(selected).getPosition()));
                    waypoints.get(selected).showInfoWindow();
                }
                break;
        }
    }
}
