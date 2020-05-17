import converter.Converter;
import converter.Parser;
import converter.json.JsonObject;
import converter.xml.Xml2JsonConverter;
import converter.xml.XmlElement;
import converter.xml.XmlParser;
import org.junit.Assert;
import org.junit.Test;

public class MyTest {
    private String plain(String str) {
        return str.replaceAll("\\s+", "");
    }

    private void doTestXml2Json(String expectedJson, String xmlInput) {
        final Parser<XmlElement> parser = new XmlParser();
        final XmlElement xml = parser.parse(xmlInput);
        Assert.assertEquals("Parse", plain(xmlInput.replace('\'', '\"')), plain(xml.toString()));
        final Converter<XmlElement, JsonObject> converter = new Xml2JsonConverter();
        final JsonObject json = converter.convert(xml);
        final String expected = plain(expectedJson);
        final String actualJson = json.toPretty(0);
        final String actual = plain(actualJson);
        if (!expected.equals(actual)) {
            Assert.assertEquals("Convert", expectedJson, actualJson);
        }
    }

    @Test
    public void test4SimpleTypes() {
        doTestXml2Json("{\n" +
                "  \"root\": {\n" +
                "    \"transaction\": {\n" +
                "      \"id\": \"6753322\",\n" +
                "      \"number\": {\n" +
                "        \"@region\": \"Russia\",\n" +
                "        \"#number\": \"8-900-000-000\"\n" +
                "      },\n" +
                "      \"special1\": \"false\",\n" +
                "      \"special2\": \"true\",\n" +
                "      \"empty1\": null,\n" +
                "      \"empty2\": \"\",\n" +
                "      \"array1\": [\n" +
                "        null,\n" +
                "        null\n" +
                "      ],\n" +
                "      \"array2\": [\n" +
                "        \"\",\n" +
                "        null,\n" +
                "        \"123\",\n" +
                "        \"123.456\",\n" +
                "        {\n" +
                "          \"key1\": \"value1\",\n" +
                "          \"key2\": {\n" +
                "            \"@attr\": \"value2\",\n" +
                "            \"#key2\": \"value3\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"@attr2\": \"value4\",\n" +
                "          \"#element\": \"value5\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"attr3\": \"value4\",\n" +
                "          \"elem\": \"value5\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"deep\": {\n" +
                "            \"@deepattr\": \"deepvalue\",\n" +
                "            \"#deep\": [\n" +
                "              \"1\",\n" +
                "              \"2\",\n" +
                "              \"3\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}", "" +
                "<root>\n" +
                "    <transaction>\n" +
                "        <id>6753322</id>\n" +
                "        <number region='Russia'>8-900-000-000</number>\n" +
                "        <special1>false</special1>\n" +
                "        <special2>true</special2>\n" +
                "        <empty1/>\n" +
                "        <empty2></empty2>\n" +
                "        <array1>\n" +
                "            <element/>\n" +
                "            <element/>\n" +
                "        </array1>\n" +
                "        <array2>\n" +
                "            <element></element>\n" +
                "            <element/>\n" +
                "            <element>123</element>\n" +
                "            <element>123.456</element>\n" +
                "            <element>\n" +
                "                <key1>value1</key1>\n" +
                "                <key2 attr=\"value2\">value3</key2>\n" +
                "            </element>\n" +
                "            <element attr2='value4'>value5</element>\n" +
                "            <element>\n" +
                "                <attr3>value4</attr3>\n" +
                "                <elem>value5</elem>\n" +
                "            </element>\n" +
                "            <element>\n" +
                "                <deep deepattr=\"deepvalue\">\n" +
                "                    <element>1</element>\n" +
                "                    <element>2</element>\n" +
                "                    <element>3</element>\n" +
                "                </deep>\n" +
                "            </element>\n" +
                "        </array2>\n" +
                "    </transaction>\n" +
                "</root>");
    }

    @Test
    public void test4InnerTypes() {
        doTestXml2Json("" +
                "{\n" +
                "  \"root\": {\n" +
                "    \"transaction\": {\n" +
                "      \"inner1\": {\n" +
                "        \"inner2\": {\n" +
                "          \"inner3\": {\n" +
                "            \"key1\": \"value1\",\n" +
                "            \"key2\": \"value2\"\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      \"inner4\": {\n" +
                "        \"inner4\": \"value3\"\n" +
                "      },\n" +
                "      \"inner5\": {\n" +
                "        \"attr1\": \"123.456\",\n" +
                "        \"inner4\": \"value4\"\n" +
                "      },\n" +
                "      \"inner6\": {\n" +
                "        \"@attr2\": \"789.321\",\n" +
                "        \"#inner6\": \"value5\"\n" +
                "      },\n" +
                "      \"inner7\": \"value6\",\n" +
                "      \"inner8\": {\n" +
                "        \"attr3\": \"value7\"\n" +
                "      },\n" +
                "      \"inner9\": {\n" +
                "        \"attr4\": \"value8\",\n" +
                "        \"inner9\": \"value9\",\n" +
                "        \"something\": \"value10\"\n" +
                "      },\n" +
                "      \"inner10\": {\n" +
                "        \"@attr5\": \"\",\n" +
                "        \"#inner10\": null\n" +
                "      },\n" +
                "      \"inner11\": {\n" +
                "        \"@attr11\": \"value11\",\n" +
                "        \"#inner11\": {\n" +
                "          \"inner12\": {\n" +
                "            \"@attr12\": \"value12\",\n" +
                "            \"#inner12\": {\n" +
                "              \"inner13\": {\n" +
                "                \"@attr13\": \"value13\",\n" +
                "                \"#inner13\": {\n" +
                "                  \"inner14\": \"v14\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      \"inner15\": \"\",\n" +
                "      \"inner16\": {\n" +
                "        \"somekey\": \"keyvalue\",\n" +
                "        \"inner16\": \"notnull\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}", "" +
                "<root>\n" +
                "    <transaction>\n" +
                "        <inner1>\n" +
                "            <inner2>\n" +
                "                <inner3>\n" +
                "                    <key1>value1</key1>\n" +
                "                    <key2>value2</key2>\n" +
                "                </inner3>\n" +
                "            </inner2>\n" +
                "        </inner1>\n" +
                "        <inner4>\n" +
                "            <inner4>value3</inner4>\n" +
                "        </inner4>\n" +
                "        <inner5>\n" +
                "            <attr1>123.456</attr1>\n" +
                "            <inner4>value4</inner4>\n" +
                "        </inner5>\n" +
                "        <inner6 attr2=\"789.321\">value5</inner6>\n" +
                "        <inner7>value6</inner7>\n" +
                "        <inner8>\n" +
                "            <attr3>value7</attr3>\n" +
                "        </inner8>\n" +
                "        <inner9>\n" +
                "            <attr4>value8</attr4>\n" +
                "            <inner9>value9</inner9>\n" +
                "            <something>value10</something>\n" +
                "        </inner9>\n" +
                "        <inner10 attr5=''/>\n" +
                "        <inner11 attr11=\"value11\">\n" +
                "            <inner12 attr12=\"value12\">\n" +
                "                <inner13 attr13=\"value13\">\n" +
                "                    <inner14>v14</inner14>\n" +
                "                </inner13>\n" +
                "            </inner12>\n" +
                "        </inner11>\n" +
                "        <inner15></inner15>\n" +
                "        <inner16>\n" +
                "            <somekey>keyvalue</somekey>\n" +
                "            <inner16>notnull</inner16>\n" +
                "        </inner16>\n" +
                "    </transaction>\n" +
                "</root>");
    }

    @Test
    public void test4CrazyAttributes() {
        doTestXml2Json("" +
                "{\n" +
                "  \"root\": {\n" +
                "    \"transaction\": {\n" +
                "      \"crazyattr1\": {\n" +
                "        \"@attr1\": \"123\",\n" +
                "        \"#crazyattr1\": \"v15\"\n" +
                "      },\n" +
                "      \"crazyattr2\": {\n" +
                "        \"@attr1\": \"123.456\",\n" +
                "        \"#crazyattr2\": \"v16\"\n" +
                "      },\n" +
                "      \"crazyattr3\": {\n" +
                "        \"@attr1\": \"\",\n" +
                "        \"#crazyattr3\": \"v17\"\n" +
                "      },\n" +
                "      \"crazyattr9\": {\n" +
                "        \"attr1\": {\n" +
                "          \"key\": \"4\"\n" +
                "        },\n" +
                "        \"crazyattr9\": \"v23\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}", "" +
                "<root>\n" +
                "    <transaction>\n" +
                "        <crazyattr1 attr1='123'>v15</crazyattr1>\n" +
                "        <crazyattr2 attr1=\"123.456\">v16</crazyattr2>\n" +
                "        <crazyattr3 attr1=''>v17</crazyattr3>\n" +
                "        <crazyattr9>\n" +
                "            <attr1>\n" +
                "                <key>4</key>\n" +
                "            </attr1>\n" +
                "            <crazyattr9>v23</crazyattr9>\n" +
                "        </crazyattr9>\n" +
                "    </transaction>\n" +
                "</root>");
    }
}
