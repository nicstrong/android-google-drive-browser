package com.nicstrong.drive;

import com.nicstrong.android.drive.QueryBuilder;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class QueryBuilderTest {
	@Test
	public void testWithTitle() {
		QueryBuilder builder = QueryBuilder.create().withTitle("testTitle");
		assertThat(builder.build(), is("title='testTitle'"));
	}

	@Test
	public void testWithMimeType() {
        QueryBuilder builder = QueryBuilder.create().withMimeType("test/mimetype");
		assertThat(builder.build(), is("mimeType='test/mimetype'"));
	}

    @Test
    public void testWithMimeTypeNot() {
        QueryBuilder builder = QueryBuilder.create().withMimeTypeNot("test/mimetype");
        assertThat(builder.build(), is("mimeType!='test/mimetype'"));
    }

	@Test
	public void testWithParent() {
        QueryBuilder builder = QueryBuilder.create().withParent("12345");
		assertThat(builder.build(), is("'12345' in parents"));
	}


    @Test
    public void testWithNotTrashed() {
        QueryBuilder builder = QueryBuilder.create().notTrashed();
        assertThat(builder.build(), is("trashed=false"));
    }

	@Test
	public void testCompositeBuilder() {
        QueryBuilder builder = QueryBuilder.create().withTitle("testTitle").withMimeType("abcde");
		assertThat(builder.build(), is("mimeType='abcde' and title='testTitle'"));
	}
}
