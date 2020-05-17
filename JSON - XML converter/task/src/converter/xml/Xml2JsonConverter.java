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
        final JsonValue value;
        if (xml.value instanceof XmlSimpleValue) {
            value = convertValue(xml);
        } else if (xml.value instanceof XmlElement) {
            final XmlElement element = (XmlElement) xml.value;
            value = new JsonObject(element.name, convertValue(element));
        } else if (xml.value instanceof XmlElements) {
            final XmlElements elements = (XmlElements) xml.value;
            if (isArray(elements)) {
                final List<JsonValue> array = new ArrayList<>();
                for (XmlElement element : elements.values) {
                    array.add(convertValue(element));
                }
                value = new JsonArray(array);
            } else {
                final List<JsonEntity> values = new ArrayList<>();
                for (XmlElement element : elements.values) {
                    values.add(new JsonEntity(element.name, convertValue(element)));
                }
                value = new JsonObject(values);
            }
        } else {
            throw new IllegalArgumentException("Unknown Xml value type: " + xml.value);
        }
        if (xml.attributes == null) {
            return new JsonObject(xml.name, value);
        }
        final List<JsonEntity> attributes = convertAttributes(xml.attributes);
        final List<JsonEntity> entities = new ArrayList<>(attributes);
        entities.add(new JsonEntity(getValueName(xml), value));
        return new JsonObject(xml.name, new JsonObject(entities));
    }

    private String getValueName(XmlElement xml) {
        return VALUE_PREFIX + xml.name;
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

    private JsonValue convertValue(XmlElement element) {
        final JsonValue value;
        if (element.value == null) {
            value = new JsonNull();
        } else if (element.value instanceof XmlSimpleValue) {
            final XmlSimpleValue simpleValue = (XmlSimpleValue) element.value;
            value = new JsonString(simpleValue.value);
        } else if (element.value instanceof XmlElement) {
            final XmlElement child = (XmlElement) element.value;
            value = convertValue(child);
        } else if (element.value instanceof XmlElements) {
            final XmlElements children = (XmlElements) element.value;
            if (isArray(children)) {
                final List<JsonValue> array = new ArrayList<>();
                for (XmlElement child : children.values) {
                    array.add(convertValue(child));
                }
                value = new JsonArray(array);
                if (element.attributes == null) {
                    return value;
                }
                final List<JsonEntity> attributes = convertAttributes(element.attributes);
                final List<JsonEntity> entities = new ArrayList<>(attributes);
                entities.add(new JsonEntity(getValueName(element), value));
                return new JsonObject(element.name, new JsonObject(entities));
            } else {
                final ArrayList<JsonEntity> entities = new ArrayList<>();
                for (XmlElement child : children.values) {
                    entities.add(new JsonEntity(child.name, convertValue(child)));
                }
                value = new JsonObject(entities);
            }
        } else {
            throw new IllegalArgumentException("Unknown element value: " + element.value);
        }
        if (element.attributes == null) {
            return value;
        }
        final List<JsonEntity> attributes = convertAttributes(element.attributes);
        final List<JsonEntity> entities = new ArrayList<>(attributes);
        entities.add(new JsonEntity(getValueName(element), value));
        return new JsonObject(entities);
    }

    private boolean isArray(XmlElements elements) {
        return !elements.values.isEmpty() && elements.values
                .stream()
                .map(e -> e.name)
                .allMatch(elements.values.get(0).name::equals);
    }
}
