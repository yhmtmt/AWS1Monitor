package com.example.aws1monitor;

import CommandService.CommandGrpc;
import CommandService.CommandOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class UICommand {
    private String nameUIManager = "ui_manager";
    private CommandGrpc.CommandBlockingStub stub = null;
    private ManagedChannel chan = null;

    enum UICommandID {
        QUIT,         // Issue quit command
        // Control related command
        SETCTRLMODE, // Set control mode (Manual, semi-auto, auto, anchor)
        SETCTRLV,    // Set engine/rudder control value in manual, semi-auto mode
        SETCTRLC,    // Set engine/rudder control command in manual, semi-auto mode

        // Radar related command
        RDON,        // Radar on
        RDOFF,       // Radar off
        SETRDRANGE,    // Set radar range
        SETRDGAIN,     // Set radar gain
        SETRDSEA,      // Set sea clutter reduction
        SETRDRAIN,     // Set rain clutter reduction
        SETRDIFR,      // Set interference rejection
        SETRDSPD,      // Set scan speed

        // waypoint and route related command
        ADDWP,        // Add waypoint
        DELWP,        // Delete waypoint
        REVWPS,       // Reverse waypoint order
        REFWPS,       // Refresh waypoitns

        // map related command
        SETMAPRANGE,       // Set visible range in main view
        SETMAPCENTER, // Set map center in (lat, lon)
        SETMAPOBJ       // Set visible object in main view};
    }

    String[] cmdString = {"quit",
            "setctrlmode", "setctrlv", "setctrlc",
            "rdon", "rdoff", "setrdrange", "setrdgain", "setrdsea", "setrdrain", "setrdifr", "setspd",
            "addwp", "delwp", "revwps", "refwps",
            "setmaprange", "setmapcenter", "setmapobj"
    };

    enum EngineCommand {
        FULL_ASTERN,
        HALF_ASTERN,
        SLOW_ASTERN,
        DEAD_SLOW_ASTERN,
        STOP,
        DEAD_SLOW_AHEAD,
        SLOW_AHEAD,
        HALF_AHEAD,
        FULL_AHEAD,
        NAVIGATION_FULL
    }

    enum RudderCommand {
        HARD_APORT,
        PORT_20,
        PORT_10,
        MIDSHIP,
        STARBOARD_10,
        STARBOARD_20,
        HARD_ASTARBOARD
    }

    enum CourseCommand {
        PORT_20,
        PORT_10,
        PORT_5,
        COG_0,
        STARBOARD_5,
        STARBOARD_10,
        STARBOARD_20
    }

    public boolean init(String host, int port) {
        return init(host, port, null);
    }

    public boolean init(String host, int port, String nameUIManager_) {
        chan = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        if (chan == null)
            return false;
        stub = CommandService.CommandGrpc.newBlockingStub(chan);
        if (stub == null)
            return false;
        if (nameUIManager_ != null)
            nameUIManager = nameUIManager_;

        return true;
    }

    public boolean sendCommand(int id) {
        CommandOuterClass.FltrInfo.Builder req_builder =
                CommandOuterClass.FltrInfo.newBuilder();

        req_builder.setInstName(nameUIManager);
        CommandOuterClass.FltrParInfo parCmd = CommandOuterClass.FltrParInfo.newBuilder()
                .setName("cmd_id")
                .setVal(cmdString[id])
                .build();
        req_builder.addPars(parCmd);

        CommandOuterClass.Result res = stub.setFltrPar(req_builder.build());
        if (!res.getIsOk()) {
            System.out.println(res.getMessage());
            return false;
        }
        return true;

    }

    public boolean sendCommand(int id, String arg0Type, String arg0) {
        CommandOuterClass.FltrInfo.Builder req_builder =
                CommandOuterClass.FltrInfo.newBuilder();

        req_builder.setInstName(nameUIManager);
        CommandOuterClass.FltrParInfo parCmd = CommandOuterClass.FltrParInfo.newBuilder()
                .setName("cmd_id")
                .setVal(cmdString[id])
                .build();
        CommandOuterClass.FltrParInfo parArg0 = CommandOuterClass.FltrParInfo.newBuilder()
                .setName(arg0Type)
                .setVal(arg0)
                .build();

        req_builder
                .addPars(parCmd)
                .addPars(parArg0);

        CommandOuterClass.Result res = stub.setFltrPar(req_builder.build());
        if (!res.getIsOk()) {
            System.out.println(res.getMessage());
            return false;
        }
        return true;
    }

    public boolean sendCommand(int id, String arg0Type, String arg0, String arg1Type, String arg1) {
        CommandOuterClass.FltrInfo.Builder req_builder =
                CommandOuterClass.FltrInfo.newBuilder();

        req_builder.setInstName(nameUIManager);
        CommandOuterClass.FltrParInfo parCmd = CommandOuterClass.FltrParInfo.newBuilder()
                .setName("cmd_id")
                .setVal(cmdString[id])
                .build();
        CommandOuterClass.FltrParInfo parArg0 = CommandOuterClass.FltrParInfo.newBuilder()
                .setName(arg0Type)
                .setVal(arg0)
                .build();
        CommandOuterClass.FltrParInfo parArg1 = CommandOuterClass.FltrParInfo.newBuilder()
                .setName(arg1Type)
                .setVal(arg1)
                .build();

        req_builder
                .addPars(parCmd)
                .addPars(parArg0)
                .addPars(parArg1);

        CommandOuterClass.Result res = stub.setFltrPar(req_builder.build());
        if (!res.getIsOk()) {
            System.out.println(res.getMessage());
            return false;
        }
        return true;
    }

    public boolean sendCommand(int id,
                               String arg0Type, String arg0, String arg1Type, String arg1,
                               String arg2Type, String arg2, String arg3Type, String arg3) {
        CommandOuterClass.FltrInfo.Builder req_builder =
                CommandOuterClass.FltrInfo.newBuilder();

        req_builder.setInstName(nameUIManager);
        CommandOuterClass.FltrParInfo parCmd = CommandOuterClass.FltrParInfo.newBuilder()
                .setName("cmd_id")
                .setVal(cmdString[id])
                .build();
        CommandOuterClass.FltrParInfo parArg0 = CommandOuterClass.FltrParInfo.newBuilder()
                .setName(arg0Type)
                .setVal(arg0)
                .build();
        CommandOuterClass.FltrParInfo parArg1 = CommandOuterClass.FltrParInfo.newBuilder()
                .setName(arg1Type)
                .setVal(arg1)
                .build();
        CommandOuterClass.FltrParInfo parArg2 = CommandOuterClass.FltrParInfo.newBuilder()
                .setName(arg2Type)
                .setVal(arg2)
                .build();
        CommandOuterClass.FltrParInfo parArg3 = CommandOuterClass.FltrParInfo.newBuilder()
                .setName(arg3Type)
                .setVal(arg3)
                .build();

        req_builder
                .addPars(parCmd)
                .addPars(parArg0)
                .addPars(parArg1)
                .addPars(parArg2)
                .addPars(parArg3);

        CommandOuterClass.Result res = stub.setFltrPar(req_builder.build());
        if (!res.getIsOk()) {
            System.out.println(res.getMessage());
            return false;
        }
        return true;
    }

    public boolean sendCommand(int id, double val0, double val1) {
        return sendCommand(id, "fval0", String.valueOf(val0),
                "fval1", String.valueOf(val1));
    }

    public boolean sendCommand(int id, int val0) {
        return sendCommand(id, "ival0", String.valueOf(val0));
    }

    public boolean sendCommand(int id, int val0, int val1) {
        return sendCommand(id, "ival0", String.valueOf(val0),
                "ival1", String.valueOf(val1));
    }

    public boolean sendCommand(int id, int val0, int val1, double val2, double val3){
        return sendCommand(id,
                "ival0", String.valueOf(val0),
                "ival1", String.valueOf(val1),
                "fval2", String.valueOf(val2),
                "fval3", String.valueOf(val3)
        );
    }

    public boolean setControlMode(int controlSrc, int autopilotMode) {
        return sendCommand(UICommandID.SETCTRLMODE.ordinal(), controlSrc, autopilotMode);
    }

    public boolean controlByValue(float eng, float rud) {
        return sendCommand(UICommandID.SETCTRLV.ordinal(), eng, rud);
    }

    public boolean controlByCommand(int eng, int rud) {
        return sendCommand(UICommandID.SETCTRLC.ordinal(), eng, rud);
    }

    public boolean turnOnRadar() {
        return sendCommand(UICommandID.RDON.ordinal());
    }

    public boolean turnOffRadar() {
        return sendCommand(UICommandID.RDOFF.ordinal());
    }

    public boolean setRadarRange(int range) {
        return sendCommand(UICommandID.SETRDRANGE.ordinal(), range);
    }

    public boolean setRadarGain(int gain) {
        return sendCommand(UICommandID.SETRDGAIN.ordinal(), gain, 0);
    }

    public boolean setRadarGainAuto() {
        return sendCommand(UICommandID.SETRDGAIN.ordinal(), 0, 1);
    }

    public boolean setRadarSea(int sea) {
        return sendCommand(UICommandID.SETRDSEA.ordinal(), 0, sea);
    }

    public boolean setRadarSeaAuto() {
        return sendCommand(UICommandID.SETRDSEA.ordinal(), 1);
    }

    public boolean setRadarSeaOff() {
        return sendCommand(UICommandID.SETRDSEA.ordinal(), -1);
    }

    public boolean setRadarRain(int rain) {
        return sendCommand(UICommandID.SETRDRAIN.ordinal(), 0, rain);
    }

    public boolean setRadarRainOff() {
        return sendCommand(UICommandID.SETRDRAIN.ordinal(), -1);
    }

    public boolean setRadarInterference(int ifr) {
        return sendCommand(UICommandID.SETRDIFR.ordinal(), ifr);
    }

    public boolean setRadarSpeed(int speed){
        return sendCommand(UICommandID.SETRDSPD.ordinal(), speed);
    }

    public boolean addWaypointAbs(int id, double lat, double lon){
        return sendCommand(UICommandID.ADDWP.ordinal(), id, 1, lat, lon);
    }

    public boolean addWaypointRel(int id, double x, double y){
        return sendCommand(UICommandID.ADDWP.ordinal(), id, 0, x, y);
    }

    public boolean delWaypoint(int id)
    {
        return sendCommand(UICommandID.DELWP.ordinal(), id);
    }

    public boolean reverseWaypoint()
    {
        return sendCommand(UICommandID.REVWPS.ordinal());
    }

    public boolean refreshWaypoint()
    {
        return sendCommand(UICommandID.REFWPS.ordinal());
    }

    public boolean setMapRange(int range)
    {
        return sendCommand(UICommandID.SETMAPRANGE.ordinal(), range);
    }

    public boolean setMapCenterAbs(double lat, double lon)
    {
        return sendCommand(UICommandID.SETMAPCENTER.ordinal(), 1, 0, lat, lon);
    }

    public boolean setMapCenterOwnShip()
    {
        return sendCommand(UICommandID.SETMAPCENTER.ordinal(), 0);
    }

    public boolean setMapCenterRel(double x, double y) {
        return sendCommand(UICommandID.SETMAPCENTER.ordinal(), 2, 0, x, y);
    }
}

