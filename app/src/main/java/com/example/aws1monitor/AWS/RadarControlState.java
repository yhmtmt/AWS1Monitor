package com.example.aws1monitor.AWS;// automatically generated by the FlatBuffers compiler, do not modify

public final class RadarControlState {
  private RadarControlState() { }
  public static final byte OFF = -1;
  public static final byte MANUAL = 0;
  public static final byte AUTO_1 = 1;
  public static final byte AUTO_2 = 2;

  public static final String[] names = { "OFF", "MANUAL", "AUTO_1", "AUTO_2", };

  public static String name(int e) { return names[e - OFF]; }
}
