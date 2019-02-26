
package de.tuchemnitz.tomkr.meta.codegen.gen;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;


/**
 * Metadata
 * <p>
 * Abstract metadata header for assets like images, videos, etc.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "reference",
    "type",
    "source",
    "raw",
    "tags"
})
@Document(indexName = "meta")
public class MetaData {

    /**
     * Absolute path to the referred asset (image, video, etc.)
     * (Required)
     * 
     */
    @JsonProperty("reference")
    @JsonPropertyDescription("Absolute path to the referred asset (image, video, etc.)")
    @Field
    private String reference;
    /**
     * Type of the included metadata values
     * (Required)
     * 
     */
    @JsonProperty("type")
    @JsonPropertyDescription("Type of the included metadata values")
    @Field
    private MetaData.Type type;
    /**
     * Source of the metadata values; e.g. framework name and version
     * 
     */
    @JsonProperty("source")
    @JsonPropertyDescription("Source of the metadata values; e.g. framework name and version")
    @Field
    private String source;
    /**
     * Free text field for storing raw and additional data
     * 
     */
    @JsonProperty("raw")
    @JsonPropertyDescription("Free text field for storing raw and additional data")
    @Field
    private String raw;
    /**
     * Automatically generated search tags; will concatenate fields marked with tag true
     * 
     */
    @JsonProperty("tags")
    @JsonPropertyDescription("Automatically generated search tags; will concatenate fields marked with tag true")
    @Field
    private String tags;

    /**
     * Absolute path to the referred asset (image, video, etc.)
     * (Required)
     * 
     */
    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    /**
     * Absolute path to the referred asset (image, video, etc.)
     * (Required)
     * 
     */
    @JsonProperty("reference")
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Type of the included metadata values
     * (Required)
     * 
     */
    @JsonProperty("type")
    public MetaData.Type getType() {
        return type;
    }

    /**
     * Type of the included metadata values
     * (Required)
     * 
     */
    @JsonProperty("type")
    public void setType(MetaData.Type type) {
        this.type = type;
    }

    /**
     * Source of the metadata values; e.g. framework name and version
     * 
     */
    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    /**
     * Source of the metadata values; e.g. framework name and version
     * 
     */
    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Free text field for storing raw and additional data
     * 
     */
    @JsonProperty("raw")
    public String getRaw() {
        return raw;
    }

    /**
     * Free text field for storing raw and additional data
     * 
     */
    @JsonProperty("raw")
    public void setRaw(String raw) {
        this.raw = raw;
    }

    /**
     * Automatically generated search tags; will concatenate fields marked with tag true
     * 
     */
    @JsonProperty("tags")
    public String getTags() {
        return tags;
    }

    /**
     * Automatically generated search tags; will concatenate fields marked with tag true
     * 
     */
    @JsonProperty("tags")
    public void setTags(String tags) {
        this.tags = tags;
    }

    public enum Type {

        EXIF("exif"),
        LOCATION("location"),
        OBJECTS("objects");
        private final String value;
        private final static Map<String, MetaData.Type> CONSTANTS = new HashMap<String, MetaData.Type>();

        static {
            for (MetaData.Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static MetaData.Type fromValue(String value) {
            MetaData.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
