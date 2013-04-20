package com.nicstrong.drive;

public class QueryBuilder {
	private String title;
	private String mimeType;
    private String excludeMimeType;
	private String parentId;
    private Boolean trashed;

    public QueryBuilder withTitle(String title) {
		this.title = title;
		return this;
	}

	public QueryBuilder withMimeType(String mimeType) {
		this.mimeType = mimeType;
		return this;
	}

	public QueryBuilder withParent(String id) {
		parentId = id;
		return this;
	}

    public QueryBuilder withMimeTypeNot(String mimeType) {
        excludeMimeType = mimeType;
        return this;
    }

    public QueryBuilder notTrashed() {
        trashed = false;
        return this;
    }

    public String build() {
		StringBuilder builder = new StringBuilder();

		appendParam(builder, "mimeType", mimeType, "=", true);
		appendParam(builder, "mimeType", excludeMimeType, "!=", true);
		appendParam(builder, "title", title, "=", true);
        appendParam(builder, "trashed", (trashed != null ? trashed.toString() : null), "=", false);
		if (parentId != null && parentId.length() > 0) {
			appendParam(builder, "'" + parentId + "'", "parents", " in ", false);
		}

		return builder.toString();
	}

	private void appendParam(StringBuilder builder, String name, String value, String op, boolean quote) {
		if (value != null && value.length() > 0) {
			if (builder.length() > 0) {
				builder.append(" and ");
			}
			builder.append(name).append(op);
			if (quote) {
				builder.append("'");
			}
			builder.append(value);
			if (quote) {
				builder.append("'");
			}
		}
	}

	public static QueryBuilder create() {
		return new QueryBuilder();
	}
}
