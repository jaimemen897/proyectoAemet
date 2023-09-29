package models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * La clase `LocalDateAdapter` implementa la interfaz `JsonSerializer` para personalizar la serialización de objetos `Weather`
 * a formato JSON. Esta clase se encarga de definir cómo se deben convertir los objetos `Weather` en elementos JSON.
 */
public class LocalDateAdapter implements JsonSerializer<Weather> {
    /**
     * Serializa un objeto `Weather` en formato JSON.
     *
     * @param weatherType  El objeto `Weather` que se va a serializar.
     * @param typeOfSrc    El tipo de objeto de origen (puede ser útil en la serialización).
     * @param context      El contexto de serialización que se utiliza para realizar la serialización.
     * @return Un elemento JSON que representa el objeto `Weather`.
     */
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
