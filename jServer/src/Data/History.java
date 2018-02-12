/* ---------------------------------------------------------------------
 * History.java
 *
 * Author:  0xEval
 * Project: jChat - TCP/UDP based multi-threaded chat application w/ GUI
 * Licence: GPLv3
 * GitHub:  https://github.com/0xEval/jChat
 *
 * Description:
 *
 *  Provides functions to logs the chat history in a JSON format.
 *
 * --------------------------------------------------------------------- */

package Data;

// No JSON API is not bundled by default in Java (See: https://javaee.github.io/jsonp/).
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import java.util.HashMap;
import java.util.Map;

public class History {

    public static String prettyPrint(JsonStructure json) {
        return jsonFormat(json, JsonGenerator.PRETTY_PRINTING);
    }

    public static String jsonFormat(JsonStructure json, String... options) {
        StringWriter stringWriter = new StringWriter();
        Map<String, Boolean> config = buildConfig(options);
        JsonWriterFactory writerFactory = Json.createWriterFactory(config);
        JsonWriter jsonWriter = writerFactory.createWriter(stringWriter);

        jsonWriter.write(json);
        jsonWriter.close();

        return stringWriter.toString();
    }

    private static Map<String, Boolean> buildConfig(String... options) {
        Map<String, Boolean> config = new HashMap<String, Boolean>();

        if (options != null) {
            for (String option : options) {
                config.put(option, true);
            }
        }

        return config;
    }

    public static void createHistory() {
        try (FileWriter writer = new FileWriter(new File("/tmp/history.json"))) {
            JsonObjectBuilder builder = Json.createObjectBuilder()
                    .add("message", Json.createArrayBuilder().build());
            JsonObject obj = builder.build();
            writer.write(prettyPrint(obj));
            writer.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void logMessage(Message msg) {

        try (FileReader reader = new FileReader("/tmp/history.json")) {

            JsonReader jsonReader      = Json.createReader(reader);
            JsonObject jsonObject      = jsonReader.readObject();
            JsonObjectBuilder builder  = Json.createObjectBuilder();
            JsonBuilderFactory factory = Json.createBuilderFactory(null);
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            for (Map.Entry<String, JsonValue> entry : jsonObject.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
                for (JsonValue oldValue : jsonObject.getJsonArray(entry.getKey())) {
                    arrayBuilder.add(oldValue);
                    builder.add("message", oldValue);
                }
            }
            JsonArray newValue = arrayBuilder.add(factory.createObjectBuilder()
                    .add("sender", msg.getSender())
                    .add("date", msg.getTimestamp())
                    .add("content", msg.getData())
            ).build();
            builder.add("message", newValue);
            jsonObject = builder.build();

            try (FileWriter writer = new FileWriter("/tmp/history.json")) {
                writer.write(prettyPrint(jsonObject));
            }

        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Message testMessage1 = new Message(HeaderList.MSG, "Billie", "Is not");
        Message testMessage2 = new Message(HeaderList.MSG, "Jean", "my lover");

        createHistory();
        logMessage(testMessage1);
        logMessage(testMessage2);

    }

}
