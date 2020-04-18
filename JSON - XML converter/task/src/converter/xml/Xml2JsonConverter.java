package converter.xml;

import converter.Converter;
import converter.json.*;

import java.util.ArrayList;
import java.util.List;

public class Xml2JsonConverter implements Converter<XmlElement, JsonObject> {
    @Override
    public JsonObject convert(XmlElement xml) {
        final List<JsonEntity> values = new ArrayList<>();
        if (xml.value instanceof XmlSimpleValue) {
            values.add(new JsonEntity(xml.name, convertValue(xml)));
        } else if (xml.value instanceof XmlElement) {
            final XmlElement element = (XmlElement) xml.value;
            values.add(new JsonEntity(element.name, convertValue(element)));
        } else if (xml.value instanceof XmlElements) {
            final XmlElements elements = (XmlElements) xml.value;
            for (XmlElement element : elements.values) {
                values.add(new JsonEntity(element.name, convertValue(element)));
            }
        }
        if (xml.attributes == null) {
            return new JsonObject(xml.name, new JsonObject(values));
        }
        final List<JsonEntity> attributes = convertAttributes(xml.attributes);
        final List<JsonEntity> entities = new ArrayList<>(attributes);
        final JsonEntity value = new JsonEntity(getValueName(xml), new JsonObject(values));
        entities.add(value);
        return new JsonObject(xml.name, new JsonObject(entities));
    }

    private String getValueName(XmlElement xml) {
        return "#" + xml.name;
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
        return new JsonEntity("@" + attribute.name, new JsonString(attribute.value));
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
            final ArrayList<JsonEntity> entities = new ArrayList<>();
            for (XmlElement child : children.values) {
                entities.add(new JsonEntity(child.name, convertValue(child)));
            }
            value = new JsonObject(entities);
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
}
