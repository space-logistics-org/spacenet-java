package edu.mit.spacenet.io.gson.scenario;

import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeParseException;

import org.threeten.extra.PeriodDuration;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public final class PeriodDurationTypeAdpater extends TypeAdapter<PeriodDuration> {

  @Override
  public void write(JsonWriter out, PeriodDuration duration) throws IOException {
    if (duration == null) {
      out.nullValue();
    } else {
      String value = duration.toString();
      out.value(value);
    }
  }

  @Override
  public PeriodDuration read(JsonReader in) throws IOException {
    try {
      switch (in.peek()) {
        case NULL:
          in.nextNull();
          return null;
        default:
          String duration = in.nextString();
          try {
            return PeriodDuration.parse(duration);
          } catch (DateTimeParseException e) {
            return PeriodDuration.of(Duration.ofMillis((long) Float.parseFloat(duration) * 1000));
          }
      }
    } catch (DateTimeParseException e) {
      throw new JsonParseException(e);
    }
  }
}
