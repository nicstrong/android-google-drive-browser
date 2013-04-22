package com.nicstrong.drive;

import com.nicstrong.android.util.UriMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class UriMatcherTest {
    @Test(expected = IllegalArgumentException.class)
    public void testWildcardMustBeLast() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI("authority", "file/%/x", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotAddAfterWildcard() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI("authority", "file/%", 1);
        matcher.addURI("authority", "file/id", 2);
    }

    @Test
    public void testCanAddWildcardThenExact() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI("authority", "file/id", 1);
        matcher.addURI("authority", "file/%", 2);
    }
}
