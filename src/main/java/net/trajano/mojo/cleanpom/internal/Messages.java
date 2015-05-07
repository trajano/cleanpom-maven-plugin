package net.trajano.mojo.cleanpom.internal;

import java.util.ResourceBundle;

/**
 * Message constants.
 */
public final class Messages {

    /**
     * Resource bundle.
     */
    private static final ResourceBundle R = ResourceBundle.getBundle("META-INF/Messages");

    /**
     * Transformation failure.
     */
    public static final String TRANSFORM_FAIL = R.getString("transformfail");

    /**
     * Transformation failure due to I/O.
     */
    public static final String TRANSFORM_FAIL_IO = R.getString("transformfailio");

    /**
     * Prevent instantiation of messages class.
     */
    private Messages() {

    }
}
