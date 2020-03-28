package com.example.aws1monitor;

import CommandService.CommandGrpc;
import CommandService.CommandOuterClass;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.aws1monitor.AWS.AutopilotMode;
import com.example.aws1monitor.AWS.ControlSource;
import com.example.aws1monitor.AWS.Filter.UIManagerMsg.AISObject;
import com.example.aws1monitor.AWS.Filter.UIManagerMsg.UIManagerMsg;
import com.example.aws1monitor.AWS.RadarControlState;
import com.example.aws1monitor.AWS.RadarState;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, View.OnClickListener, RadioGroup.OnCheckedChangeListener
        , GoogleMap.OnCameraMoveListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {
    static Semaphore semaphore = new Semaphore(1);
    LatLng center;
    float zoomLevel;

    private GoogleMap mMap = null;
    public GoogleMap getGoogleMap(){
        return mMap;
    }

    private UICommand uiCommand = null;
    public UICommand getUICommandObject()
    {
        return uiCommand;
    }
    private MessageWatcher watcher = null;


    private Button btnS, btnP, btnM;
    private Button btnF, btnN, btnB;
    private Button btnRadarConfig, btnNetworkConfig, btnRouteConfig, btnStatus;

    private RadioGroup radioGroupControlMode;

    enum EngState { flas, hfas, slas, dsas, stp, dsah, slah, hfah, flah, nf};
    enum RudState { hap, p20, p10, mds, s10, s20, has };
    int[] cmdRev = { -2000, -1500, -1000, -700, 0, 700, 1200, 2500, 4000, 5500};
    int[] cmdSog = { -8, -6, -4, -2, 0, 2, 5, 10, 15, 20};
    int[] cmdCog = { -20, -10, -5, 0, 5, 10, 20 };
    int[] cmdEng = { 33, 43, 53, 102,  127, 152, 200, 210, 220, 225};
    int[] cmdRud = { 0, 47, 87, 127, 140, 180, 255};
    int rev = 0, sog = 0, cog = 0, eng = 127, rud = 127;
    int revTgt = 0, cogTgt = 0, sogTgt = 0;

    Byte apMode = AutopilotMode.NONE;
    Byte ctrlSource = ControlSource.UI;
    EngState engState=EngState.stp;
    RudState rudState=RudState.mds;

    public void resetButtonControlState()
    {
        EngState newEngState = EngState.nf;
        RudState newRudState = RudState.s20;
        if(ctrlSource == ControlSource.UI){
            for(int istate = 0; istate < cmdEng.length; istate++){
                if(cmdEng[istate] >= eng){
                    if(istate <= EngState.stp.ordinal())
                        newEngState = EngState.values()[istate];
                    else if(eng == cmdEng[istate])
                        newEngState = EngState.values()[istate];
                    else
                        newEngState = EngState.values()[istate-1];
                    break;
                }
            }

            for(int istate = 0; istate < cmdRud.length; istate++){
                if(cmdRud[istate] >= rud) {
                    if (istate <= RudState.mds.ordinal()) {
                        newRudState = RudState.values()[istate];
                    } else if (rud == cmdRud[istate]) {
                        newRudState = RudState.values()[istate];
                    } else {
                        newRudState = RudState.values()[istate - 1];
                    }
                    break;
                }
            }
        }else if(ctrlSource == ControlSource.AP) {
            switch (apMode){
                case AutopilotMode.STB_MAN:
                    for(int istate = 0; istate < cmdRev.length; istate++){
                        if(cmdRev[istate] >= rev){
                            if(istate <= EngState.stp.ordinal())
                                newEngState = EngState.values()[istate];
                            else if(rev == cmdRev[istate])
                                newEngState = EngState.values()[istate];
                            else
                                newEngState = EngState.values()[istate-1];
                            break;
                        }
                    }
                    newRudState = RudState.mds;
                    break;
                case AutopilotMode.WP:
                    for(int istate = 0; istate < cmdSog.length; istate++){
                        if(cmdSog[istate] >= sog){
                            if(istate <= EngState.stp.ordinal())
                                newEngState = EngState.values()[istate];
                            else if(sog == cmdSog[istate])
                                newEngState = EngState.values()[istate];
                            else
                                newEngState = EngState.values()[istate-1];
                            break;
                        }
                    }

                    newRudState = RudState.mds;
                    break;
                case AutopilotMode.STAY:
                    newEngState = EngState.stp;
                    newRudState = RudState.mds;
                    break;
            }
        }

        engState = newEngState;
        btnF.setText(EngState.values()[Math.min(engState.ordinal() + 1, EngState.values().length - 1)].toString());
        btnB.setText(EngState.values()[Math.max(engState.ordinal() - 1, 0)].toString());

        rudState = newRudState;
        btnS.setText(RudState.values()[Math.min(rudState.ordinal() + 1, RudState.values().length - 1)].toString());
        btnP.setText(RudState.values()[Math.max(rudState.ordinal() - 1, 0)].toString());
    }

    boolean mapsync = true;
    Marker ownship = null;
    List<Marker> waypoints=null;
    List<Polyline> waylines = null;
    List<Marker> aisship = null;

    StatusMonitor statusMonitor = null;
    RouteConfig routeConfig = null;
    RadarConfig radarConfig = null;
    NetworkConfig networkConfig = null;

    public void connectAWS1(String host, int port)
    {

        uiCommand = new UICommand();
        watcher = new MessageWatcher();
        uiCommand.init(host, port);
        watcher.init(host, port);
        watcher.start();
    }

    public boolean isConnected()
    {
        if(watcher == null)
            return false;
        return watcher.isAlive();
    }

    public void disconnectAWS1()
    {
        uiCommand = null;
        watcher.disconnect();
        watcher = null;
    }

    class MessageWatcher extends Thread {
        private ManagedChannel chan_msg;
        private CommandGrpc.CommandBlockingStub stub_msg;
        private boolean bQuit = false;
        public boolean init(String host, int port)
        {
            chan_msg = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
            stub_msg = CommandGrpc.newBlockingStub(chan_msg);
            bQuit = false;

            return true;
        }

        public void disconnect(){

            bQuit = true;
            try {
                join();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            chan_msg.shutdownNow();
        }

        public void run() {
            CommandOuterClass.FltrMsgReq req = CommandOuterClass.FltrMsgReq.newBuilder()
                    .setInstName("ui_manager")
                    .setPeriod(0.5)
                    .build();
            Iterator<CommandOuterClass.FltrMsg> msgs;
            msgs = stub_msg.watchFltrMsg(req);

            bQuit = false;
            while (msgs.hasNext() && bQuit == false) {
                CommandOuterClass.FltrMsg msg = msgs.next();

                // 10 *  2 ^ (21.0 - zl) = range
                // log2(range / 10) = 21 - zl
                // zl = 21 - log2(range/10)
                UIManagerMsg fmsg =
                        UIManagerMsg.getRootAsUIManagerMsg(msg.getMessage().asReadOnlyByteBuffer());

                double lat = fmsg.map().center().lat() * 180.0 / Math.PI;
                double lon = fmsg.map().center().lon() * 180.0 / Math.PI;
                try {
                    semaphore.acquire();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mMap != null) {
                                try {
                                    if(mapsync) {
                                        center = new LatLng(lat, lon);
                                        zoomLevel = (float) (21.0 - Math.log(fmsg.map().range()/10.0) / Math.log(2.0));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));
                                    }

                                    if(ownship == null)
                                        ownship = mMap.addMarker(new MarkerOptions()
                                                .title("Own Ship")
                                                .position(new LatLng(fmsg.position().lat(), fmsg.position().lon()))
                                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ore))
                                        );
                                    else
                                        ownship.setPosition(new LatLng(fmsg.position().lat(), fmsg.position().lon()));
                                    ownship.setRotation(fmsg.attitude().yaw());

                                } catch (IllegalStateException e) {
                                    e.printStackTrace();
                                }

                                int yaw = (int)fmsg.attitude().yaw();
                                int cog = (int) fmsg.velocity().cog();
                                if(yaw < 0)
                                    yaw += 360;
                                if(cog < 0)
                                    cog += 360;

                                rev = (int)fmsg.control().rev();
                                sog = (int)fmsg.control().sog();
                                cog = (int)fmsg.control().cog();
                                eng = (int)fmsg.control().eng();
                                rud = (int)fmsg.control().rud();
                                ctrlSource = fmsg.control().ctrlsrc();
                                apMode = fmsg.control().apmode();

                                resetButtonControlState();

                                String information = String.format("C%03d H%03d S%02.1fkts R%04drpm W%03d %02.1fm/s, D%03.1fm",
                                        cog, yaw, fmsg.velocity().sog(), (int)fmsg.engine().rev(),
                                        (int)fmsg.weather().dirwnd(), fmsg.weather().spdwnd(), fmsg.depth());
                                String radarInformation = String.format("%s Rg%d G%s,%d R%s,%d S%s,%d I%d Sp%d",
                                        RadarState.name(fmsg.radar().state()),
                                        fmsg.radar().range(),
                                        RadarControlState.name(fmsg.radar().gainmode()), fmsg.radar().gain(),
                                        RadarControlState.name(fmsg.radar().rainmode()), fmsg.radar().rain(),
                                        RadarControlState.name(fmsg.radar().seamode()), fmsg.radar().sea(),
                                        fmsg.radar().ifr(), fmsg.radar().spd());


                                for (int i = 0; i < fmsg.waypointsLength(); i++){
                                    LatLng pos = new LatLng(fmsg.waypoints(i).lat() * 180.0/Math.PI, fmsg.waypoints(i).lon() *180.0/Math.PI);
                                    if(i < waypoints.size()) {
                                        waypoints.get(i).setPosition(pos);
                                        if(i > 0){
                                            LatLng pos0 = waypoints.get(i-1).getPosition();
                                            List<LatLng> points = waylines.get(i-1).getPoints();
                                            points.set(0, pos0);
                                            points.set(1, pos);
                                            waylines.get(i-1).setPoints(points);
                                        }
                                    }else{
                                        String title = String.format("%dth WP", i);
                                        waypoints.add(mMap.addMarker(new MarkerOptions()
                                                .title(title)
                                                .position(pos)
                                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.michi))));
                                        if(i > 0) {
                                            LatLng pos0 = waypoints.get(i - 1).getPosition();
                                            waylines.add(mMap.addPolyline(new PolylineOptions().add(pos0, pos).color(Color.BLUE).width(5)));
                                        }
                                    }
                                }

                                for(int i = waypoints.size()-1; i >= fmsg.waypointsLength(); i--){
                                    waypoints.get(i).remove();
                                    waypoints.remove(i);

                                    if(i>=1) {
                                        waylines.get(i - 1).remove();
                                        waylines.remove(i - 1);
                                    }
                                }

                                for(int i = 0; i < fmsg.aisobjectsLength(); i++){
                                    AISObject obj = fmsg.aisobjects(i);
                                    String title = String.format("%d %03.0f, %02.1fkts ", obj.mmsi(), obj.cog(), obj.sog());
                                    LatLng pos = new LatLng(obj.lat(), obj.lon());
                                    if(i < aisship.size()){
                                        aisship.get(i).setPosition(pos);
                                        aisship.get(i).setTitle(title);
                                    }else{
                                        aisship.add(mMap.addMarker(new MarkerOptions()
                                                .title(title)
                                                .position(pos)
                                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.kare))));
                                    }
                                    aisship.get(i).setRotation(obj.cog());
                                }
                                for (int i = aisship.size()-1; i >= fmsg.aisobjectsLength(); i--){
                                    aisship.get(i).remove();
                                    aisship.remove(i);
                                }

                                statusMonitor.setInformation(information);
                                radarConfig.setInformation(radarInformation);
                            }
                            // update information view
                        }
                    });

                    semaphore.release();
                }catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnS = (Button) findViewById(R.id.ruds);
        btnM = (Button) findViewById(R.id.rudm);
        btnP = (Button) findViewById(R.id.rudp);
        btnF = (Button) findViewById(R.id.engf);
        btnN = (Button) findViewById(R.id.engn);
        btnB = (Button) findViewById(R.id.engb);
        btnN.setText(EngState.stp.toString());
        btnM.setText(RudState.mds.toString());
        btnF.setText(EngState.values()[Math.min(engState.ordinal()+1, EngState.values().length-1)].toString());
        btnB.setText(EngState.values()[Math.max(engState.ordinal()-1, 0)].toString());
        btnS.setText(RudState.values()[Math.min(rudState.ordinal()+1, RudState.values().length-1)].toString());
        btnP.setText(RudState.values()[Math.max(rudState.ordinal()-1, 0)].toString());

        btnRadarConfig =(Button) findViewById(R.id.config_radar);
        btnNetworkConfig = (Button) findViewById(R.id.config_network);
        btnRouteConfig = (Button) findViewById(R.id.config_route);
        btnStatus = (Button) findViewById(R.id.status_monitor);
        //textInfo = (TextView) findViewById(R.id.information);
        radioGroupControlMode = (RadioGroup) findViewById(R.id.control_mode);

        btnS.setOnClickListener(this);
        btnM.setOnClickListener(this);
        btnP.setOnClickListener(this);
        btnF.setOnClickListener(this);
        btnN.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnRadarConfig.setOnClickListener(this);
        btnNetworkConfig.setOnClickListener(this);
        btnRouteConfig.setOnClickListener(this);
        btnStatus.setOnClickListener(this);
        radioGroupControlMode.setOnCheckedChangeListener(this);
        radioGroupControlMode.check(R.id.manual);

        if(statusMonitor == null)
            statusMonitor = new StatusMonitor();

        waypoints = new ArrayList<Marker>();
        aisship = new ArrayList<Marker>();
        waylines = new ArrayList<Polyline>();

        if (routeConfig == null)
            routeConfig = RouteConfig.createInstance(this, waypoints, waylines);
        if(networkConfig == null)
            networkConfig = NetworkConfig.createInstance(this);
        if(radarConfig == null)
            radarConfig = RadarConfig.createInstance(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.gp_container, networkConfig);
        transaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        Button btn = (Button) view;
        switch(btn.getId()) {
            case R.id.engf:
                engState = EngState.values()[Math.min(engState.ordinal() + 1, EngState.values().length - 1)];
                uiCommand.controlByCommand(engState.ordinal(), rudState.ordinal());
                break;
            case R.id.engn:
                engState = EngState.stp;
                uiCommand.controlByCommand(engState.ordinal(), rudState.ordinal());
                break;
            case R.id.engb:
                engState = EngState.values()[Math.max(engState.ordinal() - 1, 0)];
                uiCommand.controlByCommand(engState.ordinal(), rudState.ordinal());
                break;
            case R.id.ruds:
                rudState = RudState.values()[Math.min(rudState.ordinal() + 1, RudState.values().length - 1)];
                uiCommand.controlByCommand(engState.ordinal(), rudState.ordinal());
                break;
            case R.id.rudm:
                rudState = RudState.mds;
                uiCommand.controlByCommand(engState.ordinal(), rudState.ordinal());
                break;
            case R.id.rudp:
                rudState = RudState.values()[Math.max(rudState.ordinal() - 1, 0)];
                uiCommand.controlByCommand(engState.ordinal(), rudState.ordinal());
                break;
            case R.id.config_network: {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.gp_container, networkConfig);
                transaction.commit();
            }
            break;
            case R.id.config_radar: {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.gp_container, radarConfig);
                transaction.commit();
            }
            break;
            case R.id.config_route: {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.gp_container, routeConfig);
                transaction.commit();
            }
            break;
            case R.id.status_monitor: {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.gp_container, statusMonitor);
                transaction.commit();
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(uiCommand != null) {
            switch (i) {
                case R.id.manual:
                    uiCommand.setControlMode(ControlSource.UI, AutopilotMode.STAY);
                    break;
                case R.id.semiauto:
                    uiCommand.setControlMode(ControlSource.AP, AutopilotMode.STB_MAN);
                    break;
                case R.id.followwps:
                    uiCommand.setControlMode(ControlSource.AP, AutopilotMode.WP);
                    break;
                case R.id.stay:
                    uiCommand.setControlMode(ControlSource.AP, AutopilotMode.STAY);
                    break;
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnCameraMoveListener(this);
    }

    @Override
    public void onCameraMove() {
        if(!mapsync) {
            CameraPosition pos = mMap.getCameraPosition();
            center = new LatLng(pos.target.latitude, pos.target.longitude);
            zoomLevel = pos.zoom;
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        uiCommand.addWaypointAbs(waypoints.size(), latLng.latitude, latLng.longitude);
    }


    @Override
    public void onMapClick(LatLng latLng)
    {
        if(mapsync) {
            mapsync = false;
        }else{
            mapsync = true;
            uiCommand.setMapCenterAbs(center.latitude, center.longitude);
            int range = (int) (Math.pow(2, (21.0 - zoomLevel) )* 10.0);
            uiCommand.setMapRange(range);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        marker.showInfoWindow();

        if(ownship.equals(marker)){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.gp_container, statusMonitor);
            transaction.commit();
            uiCommand.setMapCenterOwnShip();
            mapsync = true;
            return true;
        }

        for (int iselected = 0; iselected < waypoints.size(); iselected++){
            if(waypoints.get(iselected).equals(marker)) {
                routeConfig.setSelected(iselected);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.gp_container, routeConfig);
                transaction.commit();
                mapsync = false;
                return true;
            }
        }
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
    }

}
