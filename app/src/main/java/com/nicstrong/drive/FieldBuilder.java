package com.nicstrong.drive;

import java.util.ArrayList;
import java.util.List;

public class FieldBuilder {
    private List<String> items;
    private List<String> fields;

    public FieldBuilder() {
        items = new ArrayList<String>();
        fields = new ArrayList<String>();
    }

    public FieldBuilder withItem(String item) {
        items.add(item);
        return this;
    }

    public FieldBuilder withItems(String... items) {
        for (String item : items) {
            this.items.add(item);
        }
        return this;
    }

    public FieldBuilder withEtag() {
        fields.add("etag");
        return this;
    }

    public FieldBuilder withKind() {
        fields.add("kind");
        return this;
    }

    public FieldBuilder withNextLink() {
        fields.add("nextLink");
        return this;
    }

    public FieldBuilder withNextPageToken() {
        fields.add("nextPageToken");
        return this;
    }

    public FieldBuilder withSelfLink() {
        fields.add("selfLink");
        return this;
    }

    public String buildItems() {
        StringBuilder itemBuilder = new StringBuilder();
        for (String item : items) {
            appendField(itemBuilder, item);
        }
        return itemBuilder.toString();
    }

    public String build() {
        StringBuilder builder = new StringBuilder();

        for (String field : fields) {
            appendField(builder, field);
        }
        if (items.size() > 0) {
            appendField(builder, "items(" + buildItems() + ")");
        }

        return builder.toString();
    }

    private void appendField(StringBuilder builder, String name) {
        if (builder.length() > 0) {
            builder.append(',');
        }
        builder.append(name);
    }

    public static FieldBuilder create() {
        return new FieldBuilder();
    }
}
