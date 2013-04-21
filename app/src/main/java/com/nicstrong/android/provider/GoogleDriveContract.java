package com.nicstrong.android.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class GoogleDriveContract {
    public static final String AUTHORITY = "com.nicstrong.android.googledrive";
    private static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public interface DriveFileColumns extends BaseColumns {
        public static final String KIND = "kind";
        public static final String ETAG = "etag";
        public static final String TITLE = "title";
        public static final String MIMETYPE = "mimeType";
        public static final String DESCRIPTION = "description";

        public static final String SELF_LINK = "selfLink";
        public static final String WEB_CONTENT_LINK = "webContentLink";
        public static final String WEB_VIEW_LINK = "webViewLink";
        public static final String ALTERNATE_LINK = "alternateLink";
        public static final String EMBED_LINK = "embedLink";
        public static final String ICON_LINK = "iconLink";
        public static final String THUMBNAIL_LINK = "thumbnailLink";

        public static final String STARRED = "starred";
        public static final String HIDDEN = "hidden";
        public static final String TRASHED = "trashed";
        public static final String RESTICTED = "restricted";
        public static final String VIEWED = "viewed";

        public static final String CREATED_DATE = "createdDate";
        public static final String MODIFIED_DATE = "modifiedDate";
        public static final String MODIFIED_BY_ME_DATE = "modifiedByMeDate";
        public static final String SHARAED_WITH_ME_DATE = "sharedWithMeDate";

        public static final String DOWNLOAD_URL = "downloadUrl";
        public static final String INDEXABLE_TEXT = "indexableText";
        public static final String ORIGINAL_FILENAME = "originalFilename";
        public static final String FILE_EXTENSION = "fileExtension";
        public static final String MD5_CHECKSUM = "md5Checksum";
        public static final String FILE_SIZE = "fileSize";
        public static final String QUOTA_BYTES_USED = "quotaBytesUsed";
        public static final String OWNER_NAMES = "ownerNames";

        public static final String LAST_MODIFYING_USER_NAME = "lastModifyingUserName";

        public static final String EDITABLE = "editable";
        public static final String WRITERS_CAN_SHARE = "writersCanShare";
        public static final String SHARED = "shared";
        public static final String EXPLICITLY_TRASHED = "explicitlyTrashed";
        public static final String APP_DATA_CONTENTS = "appDataContents";
    }

    public static final class File implements DriveFileColumns {
        public static final String CONTENT_DIRECTORY = "files";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);

        public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.nicstrong.googledrive.file";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.nicstrong.googledrive.file";

        public static final class Thumbnail {

        }

        public static final class Parent {

        }

        public static final class Permission {

        }

        public static final class LastModfiyingUser {

        }

        public static final class ImageMediaMetadata {

        }
    }

}
