package com.example.aws1monitor.AWS.Filter.UIManagerMsg;// automatically generated by the FlatBuffers compiler, do not modify

import com.example.aws1monitor.AWS.Radar;
import com.google.flatbuffers.BaseVector;
import com.google.flatbuffers.Constants;
import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@SuppressWarnings("unused")
public final class UIManagerMsg extends Table {
  public static void ValidateVersion() { Constants.FLATBUFFERS_1_12_0(); }
  public static UIManagerMsg getRootAsUIManagerMsg(ByteBuffer _bb) { return getRootAsUIManagerMsg(_bb, new UIManagerMsg()); }
  public static UIManagerMsg getRootAsUIManagerMsg(ByteBuffer _bb, UIManagerMsg obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public UIManagerMsg __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public long t() { int o = __offset(4); return o != 0 ? bb.getLong(o + bb_pos) : 0L; }
  public Control control() { return control(new Control()); }
  public Control control(Control obj) { int o = __offset(6); return o != 0 ? obj.__assign(o + bb_pos, bb) : null; }
  public Position position() { return position(new Position()); }
  public Position position(Position obj) { int o = __offset(8); return o != 0 ? obj.__assign(o + bb_pos, bb) : null; }
  public Velocity velocity() { return velocity(new Velocity()); }
  public Velocity velocity(Velocity obj) { int o = __offset(10); return o != 0 ? obj.__assign(o + bb_pos, bb) : null; }
  public Attitude attitude() { return attitude(new Attitude()); }
  public Attitude attitude(Attitude obj) { int o = __offset(12); return o != 0 ? obj.__assign(o + bb_pos, bb) : null; }
  public Engine engine() { return engine(new Engine()); }
  public Engine engine(Engine obj) { int o = __offset(14); return o != 0 ? obj.__assign(o + bb_pos, bb) : null; }
  public float depth() { int o = __offset(16); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public Weather weather() { return weather(new Weather()); }
  public Weather weather(Weather obj) { int o = __offset(18); return o != 0 ? obj.__assign(o + bb_pos, bb) : null; }
  public Map map() { return map(new Map()); }
  public Map map(Map obj) { int o = __offset(20); return o != 0 ? obj.__assign(o + bb_pos, bb) : null; }
  public Radar radar() { return radar(new Radar()); }
  public Radar radar(Radar obj) { int o = __offset(22); return o != 0 ? obj.__assign(o + bb_pos, bb) : null; }
  public Waypoint waypoints(int j) { return waypoints(new Waypoint(), j); }
  public Waypoint waypoints(Waypoint obj, int j) { int o = __offset(24); return o != 0 ? obj.__assign(__vector(o) + j * 16, bb) : null; }
  public int waypointsLength() { int o = __offset(24); return o != 0 ? __vector_len(o) : 0; }
  public Waypoint.Vector waypointsVector() { return waypointsVector(new Waypoint.Vector()); }
  public Waypoint.Vector waypointsVector(Waypoint.Vector obj) { int o = __offset(24); return o != 0 ? obj.__assign(__vector(o), 16, bb) : null; }
  public AISObject aisobjects(int j) { return aisobjects(new AISObject(), j); }
  public AISObject aisobjects(AISObject obj, int j) { int o = __offset(26); return o != 0 ? obj.__assign(__vector(o) + j * 32, bb) : null; }
  public int aisobjectsLength() { int o = __offset(26); return o != 0 ? __vector_len(o) : 0; }
  public AISObject.Vector aisobjectsVector() { return aisobjectsVector(new AISObject.Vector()); }
  public AISObject.Vector aisobjectsVector(AISObject.Vector obj) { int o = __offset(26); return o != 0 ? obj.__assign(__vector(o), 32, bb) : null; }

  public static void startUIManagerMsg(FlatBufferBuilder builder) { builder.startTable(12); }
  public static void addT(FlatBufferBuilder builder, long t) { builder.addLong(0, t, 0L); }
  public static void addControl(FlatBufferBuilder builder, int controlOffset) { builder.addStruct(1, controlOffset, 0); }
  public static void addPosition(FlatBufferBuilder builder, int positionOffset) { builder.addStruct(2, positionOffset, 0); }
  public static void addVelocity(FlatBufferBuilder builder, int velocityOffset) { builder.addStruct(3, velocityOffset, 0); }
  public static void addAttitude(FlatBufferBuilder builder, int attitudeOffset) { builder.addStruct(4, attitudeOffset, 0); }
  public static void addEngine(FlatBufferBuilder builder, int engineOffset) { builder.addStruct(5, engineOffset, 0); }
  public static void addDepth(FlatBufferBuilder builder, float depth) { builder.addFloat(6, depth, 0.0f); }
  public static void addWeather(FlatBufferBuilder builder, int weatherOffset) { builder.addStruct(7, weatherOffset, 0); }
  public static void addMap(FlatBufferBuilder builder, int mapOffset) { builder.addStruct(8, mapOffset, 0); }
  public static void addRadar(FlatBufferBuilder builder, int radarOffset) { builder.addStruct(9, radarOffset, 0); }
  public static void addWaypoints(FlatBufferBuilder builder, int waypointsOffset) { builder.addOffset(10, waypointsOffset, 0); }
  public static void startWaypointsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(16, numElems, 8); }
  public static void addAisobjects(FlatBufferBuilder builder, int aisobjectsOffset) { builder.addOffset(11, aisobjectsOffset, 0); }
  public static void startAisobjectsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(32, numElems, 8); }
  public static int endUIManagerMsg(FlatBufferBuilder builder) {
    int o = builder.endTable();
    return o;
  }
  public static void finishUIManagerMsgBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
  public static void finishSizePrefixedUIManagerMsgBuffer(FlatBufferBuilder builder, int offset) { builder.finishSizePrefixed(offset); }

  public static final class Vector extends BaseVector {
    public Vector __assign(int _vector, int _element_size, ByteBuffer _bb) { __reset(_vector, _element_size, _bb); return this; }

    public UIManagerMsg get(int j) { return get(new UIManagerMsg(), j); }
    public UIManagerMsg get(UIManagerMsg obj, int j) {  return obj.__assign(__indirect(__element(j), bb), bb); }
  }
}
