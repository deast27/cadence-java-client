/*
 *  Modifications copyright (C) 2017 Uber Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"). You may not
 *  use this file except in compliance with the License. A copy of the License is
 *  located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 *  or in the "license" file accompanying this file. This file is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *  express or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package com.uber.cadence.converter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Duration;

/**
 * Serializes {@link Duration} as {@code {"seconds": s, "nanos": n}}, which is the representation
 * Gson produced through reflection over the private fields of {@link Duration}. That reflection is
 * rejected by the module system starting with JDK 16, so the format is reproduced explicitly here
 * to stay wire compatible.
 */
class DurationTypeAdapter extends TypeAdapter<Duration> {

  private static final String SECONDS_FIELD_NAME = "seconds";
  private static final String NANOS_FIELD_NAME = "nanos";

  @Override
  public void write(JsonWriter out, Duration value) throws IOException {
    out.beginObject();
    out.name(SECONDS_FIELD_NAME).value(value.getSeconds());
    out.name(NANOS_FIELD_NAME).value(value.getNano());
    out.endObject();
  }

  @Override
  public Duration read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.STRING) {
      return Duration.parse(in.nextString());
    }
    long seconds = 0;
    int nanos = 0;
    in.beginObject();
    while (in.hasNext()) {
      String name = in.nextName();
      switch (name) {
        case SECONDS_FIELD_NAME:
          seconds = in.nextLong();
          break;
        case NANOS_FIELD_NAME:
          nanos = in.nextInt();
          break;
        default:
          in.skipValue();
      }
    }
    in.endObject();
    return Duration.ofSeconds(seconds, nanos);
  }
}
