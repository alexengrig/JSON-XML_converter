package converter.js;

import converter.x.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsConverter {
    public List<XElement> convert(JsObject json) {
        final List<XElement> elements = new ArrayList<>();
        final List<JsEntity> entities = json.values;
        for (JsEntity entity : entities) {
            final String name = entity.name;
            final JsValue value = entity.value;
            if (value.isSimple()) {
                elements.add(new XElement(name, new XSimpleValue(value.toString())));
            } else if (value instanceof JsObject) {
                final JsObject object = (JsObject) value;
                final boolean hasXmlAttributes = hasAttributes(object);
                final boolean hasXmlValue = hasValue(name, object);
                if (hasXmlAttributes && hasXmlValue) {
                    final XAttributes xmlAttributes = getXmlAttributes(object);
                    final XValue xmlValue = getXmlValue(name, object);
                    elements.add(new XElement(name, xmlAttributes, xmlValue));
                } else if (hasXmlAttributes) {
                    final XAttributes xmlAttributes = getXmlAttributes(object);
                    elements.add(new XElement(name, xmlAttributes));
                } else if (hasXmlValue) {
                    final XValue xmlValue = getXmlValue(name, object);
                    elements.add(new XElement(name, xmlValue));
                } else {
                    final List<XElement> children = convert(object);
                    final XElements xmlElements = new XElements(children);
                    elements.add(new XElement(name, xmlElements));
                }
            } else {
                throw new UnsupportedOperationException("Unsupported JSON value type: " + value);
            }
        }
        return elements;
//        final JsEntity entity = entities.get(0);
//        if (entity.value == null) {
//            return new XElement(entity.name);
//        } else if (entity.value instanceof JsSimpleValue) {
//            final JsSimpleValue jsonValue = (JsSimpleValue) entity.value;
//            return new XElement(entity.name, jsonValue.value);
//        } else {
//            final JsElementValue attributeValue = (JsElementValue) entity.value;
//            final List<XAttribute> attributes = new ArrayList<>();
//            String elementValue = null;
//            for (JsEntity valueEntity : attributeValue.value.entities) {
//                final String entityName = valueEntity.name;
//                final JsSimpleValue entityValue = (JsSimpleValue) valueEntity.value;
//                if (entityName.startsWith("@")) {
//                    attributes.add(new XAttribute(entityName.substring(1), entityValue.value));
//                } else if (entityName.startsWith("#")) {
//                    elementValue = entityValue.value;
//                }
//            }
//            return new XElement(entity.name, elementValue, attributes);
//        }
    }

    private boolean hasAttributes(JsObject object) {
        return object.values.stream().map(e -> e.name).anyMatch(n -> n.startsWith("@") && n.length() > 1);
    }

    private boolean hasValue(String name, JsObject object) {
        return object.values.stream().map(e -> e.name).anyMatch(("#" + name)::equals);
    }

    private XAttributes getXmlAttributes(JsObject object) {
        return new XAttributes(object.values.stream()
                .filter(e -> e.name.startsWith("@") && e.name.length() > 1)
                .map(this::getXmlAttribute)
                .collect(Collectors.toList()));
    }

    private XAttribute getXmlAttribute(JsEntity entity) {
        return new XAttribute(entity.name.substring(1), entity.value.toString());
    }

    private XValue getXmlValue(String name, JsObject object) {
        return object.values.stream()
                .filter(e -> ("#" + name).equals(e.name))
                .map(e -> new XSimpleValue(e.value.toString()))
                .findFirst()
                .orElseGet(XSimpleValue::new);
    }
}
