package converter.json;

import converter.Converter;
import converter.xml.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Json2XmlConverter implements Converter<JsonObject, XmlElement> {
    @Override
    public XmlElement convert(JsonObject json) {
        final List<XmlElement> children = convertChildren(json);
        if (children.size() == 1) {
            return children.get(0);
        } else {
            return new XmlElement("root", new XmlElements(children));
        }
    }

    private List<XmlElement> convertChildren(JsonObject json) {
        final List<XmlElement> elements = new ArrayList<>();
        final List<JsonEntity> entities = json.values;
        for (JsonEntity entity : entities) {
            if ("".equals(entity.name) || "@".equals(entity.name) || "#".equals(entity.name)) continue;
            final String name;
            if (entity.name.startsWith("@") || entity.name.startsWith("#")) {
                name = entity.name.substring(1);
                if (entities.stream().map(e -> e.name).anyMatch(name::equals)) continue;
            } else {
                name = entity.name;
            }
            final JsonValue value = entity.value;
            if (value.isSimple()) {
                if (value instanceof JsonNull) {
                    elements.add(new XmlElement(name));
                } else {
                    elements.add(new XmlElement(name, new XmlSimpleValue(value.toString())));
                }
            } else if (value instanceof JsonObject) {
                final JsonObject object = (JsonObject) value;
                final boolean isXml = isXml(name, object);
                if (isXml) {
                    final boolean hasXmlAttributes = hasXmlAttributes(object);
                    if (hasXmlAttributes && hasXmlValue(name, object)) {
                        final XmlAttributes xmlAttributes = getXmlAttributes(object);
                        final XmlValue xmlValue = getXmlValue(name, object);
                        if (xmlValue == null) {
                            elements.add(new XmlElement(name, xmlAttributes));
                        } else {
                            elements.add(new XmlElement(name, xmlAttributes, xmlValue));
                        }
                    } else {
                        final XmlValue xmlValue = getXmlValue(name, object);
                        if (xmlValue == null) {
                            elements.add(new XmlElement(name));
                        } else {
                            elements.add(new XmlElement(name, xmlValue));
                        }
                    }
                } else {
                    final List<XmlElement> children = convertChildren(object);
                    if (children.isEmpty()) {
                        elements.add(new XmlElement(name, new XmlSimpleValue()));
                    } else {
                        final XmlElements xmlElements = new XmlElements(children);
                        elements.add(new XmlElement(name, xmlElements));
                    }
                }
            } else {
                throw new UnsupportedOperationException("Unsupported JSON value type: " + value);
            }
        }
        return elements;
    }

    private boolean isXml(String name, JsonObject object) {
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

    private boolean hasXmlAttributes(JsonObject object) {
        return object.values.stream().map(e -> e.name).anyMatch(n -> n.startsWith("@") && n.length() > 1);
    }

    private boolean hasXmlValue(String name, JsonObject object) {
        return object.values.stream().anyMatch(e -> ("#" + name).equals(e.name));
    }

    private XmlAttributes getXmlAttributes(JsonObject object) {
        return new XmlAttributes(object.values.stream()
                .filter(e -> e.name.startsWith("@") && e.name.length() > 1)
                .map(this::getXmlAttribute)
                .collect(Collectors.toList()));
    }

    private XmlAttribute getXmlAttribute(JsonEntity entity) {
        final String name = entity.name.substring(1);
        final String value;
        if (entity.value instanceof JsonNull) {
            value = "";
        } else {
            value = entity.value.toString();
        }
        return new XmlAttribute(name, value);
    }

    private XmlValue getXmlValue(String name, JsonObject object) {
        return object.values.stream()
                .filter(e -> ("#" + name).equals(e.name))
                .limit(1)
                .filter(e -> !(e.value instanceof JsonNull))
                .map(e -> {
                    if (e.value.isSimple()) {
                        return new XmlSimpleValue(e.value.toString());
                    } else {
                        final List<XmlElement> children = convertChildren((JsonObject) e.value);
                        if (children.isEmpty()) {
                            return new XmlSimpleValue();
                        } else {
                            return new XmlElements(children);
                        }
                    }
                })
                .findFirst()
                .orElse(null);
    }
}
