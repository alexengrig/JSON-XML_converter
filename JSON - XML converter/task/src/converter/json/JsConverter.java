package converter.json;

import converter.x.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsConverter {
    public XElement convert(JsObject json) {
        final List<XElement> children = convertChildren(json);
        if (children.size() == 1) {
            return children.get(0);
        } else {
            return new XElement("root", new XElements(children));
        }
    }

    private List<XElement> convertChildren(JsObject json) {
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
                    final boolean hasXmlAttributes = hasXmlAttributes(object);
                    if (hasXmlAttributes && hasXmlValue(name, object)) {
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
                    final List<XElement> children = convertChildren(object);
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
                .allMatch(o -> o.name.length() > 1
                        && ((o.name.startsWith("@") && o.value.isSimple()) || o.name.startsWith("#")));
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

    private boolean hasXmlAttributes(JsObject object) {
        return object.values.stream().map(e -> e.name).anyMatch(n -> n.startsWith("@") && n.length() > 1);
    }

    private boolean hasXmlValue(String name, JsObject object) {
        return object.values.stream().anyMatch(e -> ("#" + name).equals(e.name));
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
                .limit(1)
                .filter(e -> !(e.value instanceof JsNull))
                .map(e -> {
                    if (e.value.isSimple()) {
                        return new XSimpleValue(e.value.toString());
                    } else {
                        final List<XElement> children = convertChildren((JsObject) e.value);
                        if (children.isEmpty()) {
                            return new XSimpleValue();
                        } else {
                            return new XElements(children);
                        }
                    }
                })
                .findFirst()
                .orElse(null);
    }
}
