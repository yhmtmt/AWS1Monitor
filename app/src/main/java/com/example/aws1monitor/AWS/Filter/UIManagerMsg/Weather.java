package com.example.aws1monitor.AWS.Filter.UIManagerMsg;// automatically generated by the FlatBuffers compiler, do not modify


import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Weather extends Struct {
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public Weather __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public float bar() { return bb.getFloat(bb_pos + 0); }
  public float temp() { return bb.getFloat(bb_pos + 4); }
  public float hmdr() { return bb.getFloat(bb_pos + 8); }
  public float dew() { return bb.getFloat(bb_pos + 12); }
  public float spdwnd() { return bb.getFloat(bb_pos + 16); }
  public float dirwnd() { return bb.getFloat(bb_pos + 20); }

  public static int createWeather(FlatBufferBuilder builder, float bar, float temp, float hmdr, float dew, float spdwnd, float dirwnd) {
    builder.prep(4, 24);
    builder.putFloat(dirwnd);
    builder.putFloat(spdwnd);
    builder.putFloat(dew);
    builder.putFloat(hmdr);
    builder.putFloat(temp);
    builder.putFloat(bar);
    return builder.offset();
  }

  public static final class Vector extends BaseVector {
    public Vector __assign(int _vector, int _element_size, ByteBuffer _bb) { __reset(_vector, _element_size, _bb); return this; }

    public Weather get(int j) { return get(new Weather(), j); }
    public Weather get(Weather obj, int j) {  return obj.__assign(__element(j), bb); }
  }
}

