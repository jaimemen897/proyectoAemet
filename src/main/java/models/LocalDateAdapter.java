package models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class LocalDateAdapter implements JsonSerializer<Weather> {
    @Override
    public JsonElement serialize(Weather weatherType, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("localidad", weatherType.getLocalidad());
        jsonObject.addProperty("provincia", weatherType.getProvincia());
        jsonObject.addProperty("tempMax", weatherType.getTempMax());
        jsonObject.addProperty("horaTempMax", weatherType.getHoraTempMax().toString());
        jsonObject.addProperty("tempMin", weatherType.getTempMin());
        jsonObject.addProperty("horaTempMin", weatherType.getHoraTempMin().toString());
        jsonObject.addProperty("precipitacion", weatherType.getPrecipitacion());
        jsonObject.addProperty("day", weatherType.getDay().toString());
        return jsonObject;
    }
}
