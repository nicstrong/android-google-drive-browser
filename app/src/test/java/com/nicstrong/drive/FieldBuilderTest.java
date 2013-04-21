package com.nicstrong.drive;

import com.nicstrong.android.drive.FieldBuilder;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FieldBuilderTest {
    @Test
    public void testWithKind() {
        FieldBuilder builder = FieldBuilder.create().withKind();
        assertThat(builder.build(), is("kind"));
    }

    @Test
    public void testWithEtag() {
        FieldBuilder builder = FieldBuilder.create().withEtag();
        assertThat(builder.build(), is("etag"));
    }

    @Test
    public void testWithNextLink() {
        FieldBuilder builder = FieldBuilder.create().withNextLink();
        assertThat(builder.build(), is("nextLink"));
    }

    @Test
    public void testWithNextPageToken() {
        FieldBuilder builder = FieldBuilder.create().withNextPageToken();
        assertThat(builder.build(), is("nextPageToken"));
    }

    @Test
    public void testWithSelfLink() {
        FieldBuilder builder = FieldBuilder.create().withSelfLink();
        assertThat(builder.build(), is("selfLink"));
    }

    @Test
    public void testWithMultipleFieds() {
        FieldBuilder builder = FieldBuilder.create().withSelfLink().withEtag().withKind();
        assertThat(builder.build(), is("selfLink,etag,kind"));
    }

    @Test
    public void testWithSingleItem() {
        FieldBuilder builder = FieldBuilder.create().withItem("title");
        assertThat(builder.build(), is("items(title)"));
    }

    @Test
     public void testWithMultipleItems() {
        FieldBuilder builder = FieldBuilder.create().withItem("title").withItem("modifiedDate");
        assertThat(builder.build(), is("items(title,modifiedDate)"));
    }

    @Test
    public void testBuildItems1() {
        FieldBuilder builder = FieldBuilder.create().withItem("title").withItem("modifiedDate");
        assertThat(builder.buildItems(), is("title,modifiedDate"));
    }

    @Test
    public void testBuildItems2() {
        FieldBuilder builder = FieldBuilder.create().withItems("title", "modifiedDate");
        assertThat(builder.buildItems(), is("title,modifiedDate"));
    }


    @Test
    public void testWithMultipleItemsIterable() {
        FieldBuilder builder = FieldBuilder.create().withItems("title", "modifiedDate");
        assertThat(builder.build(), is("items(title,modifiedDate)"));
    }

    @Test
    public void testWithMultipleItemsAndFields() {
        FieldBuilder builder = FieldBuilder.create().withEtag().withItems("title", "modifiedDate").withKind();
        assertThat(builder.build(), is("etag,kind,items(title,modifiedDate)"));
    }
}
