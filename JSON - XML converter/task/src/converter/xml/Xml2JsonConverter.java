package converter.xml;

import converter.Converter;
import converter.json.*;

import java.util.ArrayList;
import java.util.List;

public class Xml2JsonConverter implements Converter<XmlElement, JsonObject> {
    private static final String ATTRIBUTE_PREFIX = "@";
    private static final String VALUE_PREFIX = "#";

    @Override
    public JsonObject convert(XmlElement xml) {
        return new JsonObject(xml.name, convertValue(xml));
    }

    private JsonEntity convertEntity(XmlElement xml) {
        return new JsonEntity(xml.name, convertValue(xml));
    }

    private JsonValue convertValue(XmlElement xml) {
        JsonValue value;
        if (xml.value == null) {
            value = new JsonNull();
        } else if (xml.value instanceof XmlSimpleValue) {
            XmlSimpleValue simpleValue = (XmlSimpleValue) xml.value;
            value = new JsonString(simpleValue.value);
        } else if (xml.value instanceof XmlElement) {
            final XmlElement element = (XmlElement) xml.value;
            value = convert(element);
        } else if (xml.value instanceof XmlElements) {
            final XmlElements elements = (XmlElements) xml.value;
            if (isArray(elements)) {
                final List<JsonValue> array = new ArrayList<>();
                for (XmlElement element : elements.values) {
                    array.add(convertValue(element));
                }
                value = new JsonArray(array);
            } else {
                final List<JsonEntity> entities = new ArrayList<>();
                for (XmlElement element : elements.values) {
                    entities.add(convertEntity(element));
                }
                value = new JsonObject(entities);
            }
        } else {
            throw new IllegalArgumentException("Unknown Xml value type: " + xml.value);
        }
        if (xml.attributes == null) {
            return value;
        }
        final List<JsonEntity> entities = new ArrayList<>(convertAttributes(xml.attributes));
        entities.add(new JsonEntity(VALUE_PREFIX + xml.name, value));
        return new JsonObject(entities);
    }

    private List<JsonEntity> convertAttributes(XmlAttributes attributes) {
        final List<JsonEntity> entities = new ArrayList<>();
        for (XmlAttribute attribute : attributes.values) {
            entities.add(convertAttribute(attribute));
        }
        return entities;
    }

    private JsonEntity convertAttribute(XmlAttribute attribute) {
        if (attribute.value == null) {
            new JsonNull();
        }
        return new JsonEntity(ATTRIBUTE_PREFIX + attribute.name, new JsonString(attribute.value));
    }

    private boolean isArray(XmlElements elements) {
        return !elements.values.isEmpty() && elements.values
                .stream()
                .map(e -> e.name)
                .allMatch(elements.values.get(0).name::equals);
    }
}
