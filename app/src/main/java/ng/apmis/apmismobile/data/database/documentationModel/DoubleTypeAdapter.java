package ng.apmis.apmismobile.data.database.documentationModel;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by mofeejegi-apmis.<br/>
 * TypeAdapter class used for converting Java Double objects to and from JSON
 */
public class DoubleTypeAdapter extends TypeAdapter<Double> {
    @Override
    public void write(JsonWriter jsonWriter, Double doubled) throws IOException {
        if (doubled == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(doubled);
    }

    @Override
    public Double read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        try {
            String value = jsonReader.nextString();
            if ("".equals(value)) {
                return 0.0;
            }
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }
}
