package converter.x;

import converter.js.*;

import java.util.ArrayList;
import java.util.List;

public class XConverter {
    public JsObject convert(XElement xml) {
        final ArrayList<JsEntity> values = new ArrayList<>();
        if (xml.value instanceof XSimpleValue) {
            final XSimpleValue simpleValue = (XSimpleValue) xml.value;
            if (xml.attributes == null) {
                final JsEntity entity = new JsEntity(xml.name, convertSimpleValue(simpleValue));
                values.add(entity);
            } else {
                final List<JsEntity> entities = new ArrayList<>(convertAttributes(xml.attributes));
                entities.add(new JsEntity("#" + xml.name, convertSimpleValue(simpleValue)));
                values.add(new JsEntity(xml.name, new JsObject(entities)));
            }
        } else if (xml.value instanceof XElement) {
            final XElement element = (XElement) xml.value;
            values.add(new JsEntity(element.name, convert(element)));
        } else if (xml.value instanceof XElements) {
            final XElements elements = (XElements) xml.value;
            for (XElement element : elements.values) {
                final JsValue value;
                if (element.value == null) {
                    value = new JsNull();
                } else if (element.value instanceof XSimpleValue) {
                    final XSimpleValue simple = (XSimpleValue) element.value;
                    value = new JsString(simple.value);
                } else if (element.value instanceof XElement) {
                    final XElement child = (XElement) element.value;
                    value = convert(child);
                } else if (element.value instanceof XElements) {
                    final XElements children = (XElements) element.value;
                    final ArrayList<JsEntity> target = new ArrayList<>();
                    for (XElement child : children.values) {
                        target.add(new JsEntity(child.name, convert(child)));
                    }
                    value = new JsObject(target);
                } else {
                    throw new IllegalArgumentException("Unknown element value: " + element.value);
                }
                values.add(new JsEntity(element.name, value));
            }
        }
        return new JsObject(values);
    }

    private JsValue convertSimpleValue(XSimpleValue simple) {
        return new JsString(simple.value);
    }

    private List<JsEntity> convertAttributes(XAttributes attributes) {
        final List<JsEntity> entities = new ArrayList<>();
        for (XAttribute attribute : attributes.values) {
            final JsValue value;
            if (attribute.value == null) {
                value = new JsNull();
            } else {
                value = new JsString(attribute.value);
            }
            entities.add(new JsEntity("@" + attribute.name, value));
        }
        return entities;
    }
}
