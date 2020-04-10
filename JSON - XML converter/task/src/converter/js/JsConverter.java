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
            if ("".equals(entity.name) || "@".equals(entity.name) || "#".equals(entity.name)) continue;
            final String name;
            if (entity.name.startsWith("@") || entity.name.startsWith("#")) {
                name = entity.name.substring(1);
                if (entities.stream().map(e -> e.name).anyMatch(name::equals)) continue;
            } else {
                name = entity.name;
            }
            final JsValue value = entity.value;
            if (value.isSimple()) {
                if (value instanceof JsNull) {
                    elements.add(new XElement(name));
                } else {
                    elements.add(new XElement(name, new XSimpleValue(value.toString())));
                }
            } else if (value instanceof JsObject) {
                final JsObject object = (JsObject) value;
                final boolean isXml = isXml(name, object);
                if (isXml) {
                    final boolean hasXmlAttributes = hasAttributes(object);
                    if (hasXmlAttributes) {
                        final XAttributes xmlAttributes = getXmlAttributes(object);
                        final XValue xmlValue = getXmlValue(name, object);
                        if (xmlValue == null) {
                            elements.add(new XElement(name, xmlAttributes));
                        } else {
                            elements.add(new XElement(name, xmlAttributes, xmlValue));
                        }
                    } else {
                        final XValue xmlValue = getXmlValue(name, object);
                        if (xmlValue == null) {
                            elements.add(new XElement(name));
                        } else {
                            elements.add(new XElement(name, xmlValue));
                        }
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

    private boolean isXml(String name, JsObject object) {
        final boolean isValid = object.values
                .stream()
                .allMatch(o -> o.value.isSimple()
                        && o.name.length() > 1 && (o.name.startsWith("@") || o.name.startsWith("#")));
        if (!isValid) {
            return false;
        }
        final List<String> values = object.values
                .stream()
                .map(e -> e.name)
                .filter(s -> s.startsWith("#"))
                .collect(Collectors.toList());
        return values.size() == 1 && ("#" + name).equals(values.get(0));
    }

    private boolean hasAttributes(JsObject object) {
        return object.values.stream().map(e -> e.name).anyMatch(n -> n.startsWith("@") && n.length() > 1);
    }

    private XAttributes getXmlAttributes(JsObject object) {
        return new XAttributes(object.values.stream()
                .filter(e -> e.name.startsWith("@") && e.name.length() > 1)
                .map(this::getXmlAttribute)
                .collect(Collectors.toList()));
    }

    private XAttribute getXmlAttribute(JsEntity entity) {
        final String name = entity.name.substring(1);
        final String value;
        if (entity.value instanceof JsNull) {
            value = "";
        } else {
            value = entity.value.toString();
        }
        return new XAttribute(name, value);
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
