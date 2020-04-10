package converter.js;

import converter.x.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
            } else if (value instanceof JsNull) {
                elements.add(new XElement(name));
            } else if (value instanceof JsObject) {
                final JsObject object = (JsObject) value;
                final boolean hasXmlAttributes = hasAttributes(object);
                final boolean hasXmlValue = hasValue(name, object);
                if (hasXmlAttributes && hasXmlValue) {
                    final XAttributes xmlAttributes = getXmlAttributes(object);
                    final XValue xmlValue = getXmlValue(name, object);
                    if (xmlValue == null) {
                        elements.add(new XElement(name, xmlAttributes));
                    } else {
                        elements.add(new XElement(name, xmlAttributes, xmlValue));
                    }
                } else if (hasXmlAttributes) {
                    final XAttributes xmlAttributes = getXmlAttributes(object);
                    elements.add(new XElement(name, xmlAttributes));
                } else if (hasXmlValue) {
                    final XValue xmlValue = getXmlValue(name, object);
                    if (xmlValue == null) {
                        elements.add(new XElement(name));
                    } else {
                        elements.add(new XElement(name, xmlValue));
                    }
                } else {
                    final List<XElement> children = convert(object);
                    if (children.isEmpty()) {
                        elements.add(new XElement(name, new XSimpleValue()));
                    } else {
                        final XElements xmlElements = new XElements(children);
                        elements.add(new XElement(name, xmlElements));
                    }
                }
            } else {
                throw new UnsupportedOperationException("Unsupported JSON value type: " + value);
            }
        }
        return elements;
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
                .map(e -> e.value)
                .filter(Objects::nonNull)
                .filter(v -> !(v instanceof JsNull))
                .map(Objects::toString)
                .filter(s -> !s.isEmpty())
                .map(XSimpleValue::new)
                .findFirst()
                .orElse(null);
    }
}
