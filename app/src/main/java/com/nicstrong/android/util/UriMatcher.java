package com.nicstrong.android.util;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 Implementation of UriMatcher that also support % as a matcher.
 % will return the rest of the path. It must be last in the path.
 */
public class UriMatcher
{
    private static final Logger logger = Logger.getLogger(UriMatcher.class.getName());

    public static final int NO_MATCH = -1;

    public UriMatcher(int code)
    {
        mCode = code;
        mWhich = -1;
        mChildren = new ArrayList<UriMatcher>();
        mText = null;
        mTerminated = true;
    }

    private UriMatcher()
    {
        mCode = NO_MATCH;
        mWhich = -1;
        mChildren = new ArrayList<UriMatcher>();
        mText = null;
    }

    public void addURI(String authority, String path, int code)
    {
        if (code < 0) {
            throw new IllegalArgumentException("code " + code + " is invalid: it must be positive");
        }
        String[] tokens = path != null ? PATH_SPLIT_PATTERN.split(path) : null;
        int numTokens = tokens != null ? tokens.length : 0;
        UriMatcher node = this;
        for (int i = -1; i < numTokens; i++) {
            String token = i < 0 ? authority : tokens[i];
            ArrayList<UriMatcher> children = node.mChildren;
            int numChildren = children.size();
            UriMatcher child;
            int j;
            for (j = 0; j < numChildren; j++) {
                child = children.get(j);
                if (token.equals(child.mText)) {
                    node = child;
                    if (node.mTerminated) {
                        throw new IllegalArgumentException("Cannot add to sub path already terminated");
                    }
                    break;
                }
            }
            if (j == numChildren) {
                // Child not found, create it
                child = new UriMatcher();
                if (token.equals("#")) {
                    child.mWhich = NUMBER;
                } else if (token.equals("*")) {
                    child.mWhich = TEXT;
                } else if (token.equals("%")) {
                    child.mWhich = REMAINING;
                    node.mTerminated = true;
                    if (i != (numTokens - 1)) {
                        throw new IllegalArgumentException("Token % must be the last token");
                    }
                } else {
                    child.mWhich = EXACT;
                }

                child.mText = token;
                node.mChildren.add(child);
                node = child;
            }
        }
        node.mCode = code;
    }

    static final Pattern PATH_SPLIT_PATTERN = Pattern.compile("/");

    public int match(Uri uri)
    {
        final List<String> pathSegments = uri.getPathSegments();
        final int li = pathSegments.size();

        UriMatcher node = this;

        if (li == 0 && uri.getAuthority() == null) {
            return this.mCode;
        }

        for (int i=-1; i<li; i++) {
            String u = i < 0 ? uri.getAuthority() : pathSegments.get(i);
            ArrayList<UriMatcher> list = node.mChildren;
            if (list == null) {
                break;
            }

            node = null;
            int lj = list.size();
            for (int j=0; j<lj; j++) {
                UriMatcher n = list.get(j);
                which_switch:
                switch (n.mWhich) {
                    case EXACT:
                        if (n.mText.equals(u)) {
                            node = n;
                        }
                        break;
                    case NUMBER:
                        int lk = u.length();
                        for (int k=0; k<lk; k++) {
                            char c = u.charAt(k);
                            if (c < '0' || c > '9') {
                                break which_switch;
                            }
                        }
                        node = n;
                        break;
                    case TEXT:
                        node = n;
                        break;
                    case REMAINING:
                       return n.mCode;
                }
                if (node != null) {
                    break;
                }
            }
            if (node == null) {
                return NO_MATCH;
            }
        }

        return node.mCode;
    }

    private static final int EXACT = 0;
    private static final int NUMBER = 1;
    private static final int TEXT = 2;
    private static final int REMAINING = 3;

    @Override
    public String toString() {
        return "UriMatcher{" +
                "mCode=" + mCode +
                ", mWhich=" + mWhich +
                ", mText='" + mText + '\'' +
                ", mTerminated=" + mTerminated +
                ", mChildren=[" + toStringChildren() + "]" +
                '}';
    }

     String toStringChildren() {
        StringBuilder builder = new StringBuilder();
       for (UriMatcher child: mChildren) {
           if (builder.length() > 0) {
               builder.append(", ");
           }
           builder.append(child.toString());
       }
       return builder.toString();
    }

    private int mCode;
    private int mWhich;
    private String mText;
    private ArrayList<UriMatcher> mChildren;
    private boolean mTerminated;
}

