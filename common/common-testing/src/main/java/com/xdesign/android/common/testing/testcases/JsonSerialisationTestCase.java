package com.xdesign.android.common.testing.testcases;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import junit.framework.TestCase;

import java.util.Map;

/**
 * {@link TestCase} for any tests performing comparisons on {@link JsonObject}s.
 *
 * @author Alex Macrae
 */
public class JsonSerialisationTestCase extends TestCase {
    
    protected void assertEquals(JsonObject expected, JsonObject actual) {
        for (final Map.Entry<String, JsonElement> expectedEntry : expected.entrySet()) {
            if (!actual.has(expectedEntry.getKey())) {
                fail(expectedEntry.getKey() + " is not in " + actual.toString());
            }

            if (expectedEntry.getValue().isJsonObject()) {
                assertTrue(actual.has(expectedEntry.getKey()));
                assertTrue(actual.get(expectedEntry.getKey()).isJsonObject());
                assertEquals(
                        expectedEntry.getValue().getAsJsonObject(),
                        actual.get(expectedEntry.getKey()).getAsJsonObject());
            } else if (expectedEntry.getValue().isJsonArray()) {
                assertTrue(actual.has(expectedEntry.getKey()));
                assertTrue(actual.get(expectedEntry.getKey()).isJsonArray());
                assertEquals(
                        expectedEntry.getValue().getAsJsonArray(),
                        actual.get(expectedEntry.getKey()).getAsJsonArray());
            } else {
                String errorMessage = String.format(
                        "%s value %s is not equal to actual value %s",
                        expectedEntry.getKey(),
                        expectedEntry.getValue(),
                        actual.get(expectedEntry.getKey()));
                assertTrue(
                        errorMessage,
                        expectedEntry.getValue().equals(
                                actual.get(expectedEntry.getKey())));
            }
        }
    }

    protected void assertEquals(JsonArray expected, JsonArray actual) {
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            final JsonElement expectedElement = expected.get(i);
            
            if(expectedElement.isJsonObject()) {
                assertTrue(actual.get(i).isJsonObject());
                assertEquals(
                        expectedElement.getAsJsonObject(),
                        actual.get(i).getAsJsonObject());
            } else if (expectedElement.isJsonArray()) {
                assertTrue(actual.get(i).isJsonArray());
                assertEquals(
                        expectedElement.getAsJsonArray(),
                        actual.get(i).getAsJsonArray());
            } else {
                assertTrue(expectedElement.equals(actual.get(i)));
            }
        }
    }
}
