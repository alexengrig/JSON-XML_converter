package converter.xml;

import converter.json.*;

import java.util.ArrayList;
import java.util.List;

public class XConverter {
    public JsObject convert(XElement xml) {
        final List<JsEntity> values = new ArrayList<>();
        if (xml.value instanceof XSimpleValue) {
            values.add(new JsEntity(xml.name, convertValue(xml)));
        } else if (xml.value instanceof XElement) {
            final XElement element = (XElement) xml.value;
            values.add(new JsEntity(element.name, convertValue(element)));
        } else if (xml.value instanceof XElements) {
            final XElements elements = (XElements) xml.value;
            for (XElement element : elements.values) {
                values.add(new JsEntity(element.name, convertValue(element)));
            }
        }
        if (xml.attributes == null) {
            return new JsObject(xml.name, new JsObject(values));
        }
        final List<JsEntity> attributes = convertAttributes(xml.attributes);
        final List<JsEntity> entities = new ArrayList<>(attributes);
        final JsEntity value = new JsEntity(getValueName(xml), new JsObject(values));
        entities.add(value);
        return new JsObject(xml.name, new JsObject(entities));
    }

    private String getValueName(XElement xml) {
        return "#" + xml.name;
    }

    private List<JsEntity> convertAttributes(XAttributes attributes) {
        final List<JsEntity> entities = new ArrayList<>();
        for (XAttribute attribute : attributes.values) {
            entities.add(convertAttribute(attribute));
        }
        return entities;
    }

    private JsEntity convertAttribute(XAttribute attribute) {
        if (attribute.value == null) {
            new JsNull();
        }
        return new JsEntity("@" + attribute.name, new JsString(attribute.value));
    }

    private JsValue convertValue(XElement element) {
        final JsValue value;
        if (element.value == null) {
            value = new JsNull();
        } else if (element.value instanceof XSimpleValue) {
            final XSimpleValue simpleValue = (XSimpleValue) element.value;
            value = new JsString(simpleValue.value);
        } else if (element.value instanceof XElement) {
            final XElement child = (XElement) element.value;
            value = convertValue(child);
        } else if (element.value instanceof XElements) {
            final XElements children = (XElements) element.value;
            final ArrayList<JsEntity> entities = new ArrayList<>();
            for (XElement child : children.values) {
                entities.add(new JsEntity(child.name, convertValue(child)));
            }
            value = new JsObject(entities);
        } else {
            throw new IllegalArgumentException("Unknown element value: " + element.value);
        }
        if (element.attributes == null) {
            return value;
        }
        final List<JsEntity> attributes = convertAttributes(element.attributes);
        final List<JsEntity> entities = new ArrayList<>(attributes);
        entities.add(new JsEntity(getValueName(element), value));
        return new JsObject(entities);
    }
}
