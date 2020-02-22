package converter.json;

import converter.xml.XmlAttribute;
import converter.xml.XmlElement;

import java.util.ArrayList;
import java.util.List;

public class JsonConverter {
    public XmlElement convert(JsonElement element) {
        final List<JsonEntity> entities = element.entities;
        final JsonEntity entity = entities.get(0);
        if (entity.value == null) {
            return new XmlElement(entity.name);
        } else if (entity.value instanceof JsonSimpleValue) {
            final JsonSimpleValue jsonValue = (JsonSimpleValue) entity.value;
            return new XmlElement(entity.name, jsonValue.value);
        } else {
            final JsonElementValue attributeValue = (JsonElementValue) entity.value;
            final List<XmlAttribute> attributes = new ArrayList<>();
            String elementValue = null;
            for (JsonEntity valueEntity : attributeValue.value.entities) {
                final String entityName = valueEntity.name;
                final JsonSimpleValue entityValue = (JsonSimpleValue) valueEntity.value;
                if (entityName.startsWith("@")) {
                    attributes.add(new XmlAttribute(entityName.substring(1), entityValue.value));
                } else if (entityName.startsWith("#")) {
                    elementValue = entityValue.value;
                }
            }
            return new XmlElement(entity.name, elementValue, attributes);
        }
    }
}
