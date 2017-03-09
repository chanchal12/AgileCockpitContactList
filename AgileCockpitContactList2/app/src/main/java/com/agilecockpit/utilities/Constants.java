package com.agilecockpit.utilities;



public class Constants {

    public static final String SERVER_URL = "http://139.162.152.157/contactlist.php";
    public static final String TOKEN ="token";
    public static final String TOKEN_VALUE ="c149c4fac72d3a3678eefab5b0d3a85a";
    /**
     * Default character set used is "utf-8"
     */
    public static final String PROTOCOL_CHARSET_DEFAULT = "UTF-8";

    /**
     * Request time out 50s
     */
    public static final int REQUEST_TIMEOUT_MS = 50000;

    public static final String CONTENT_TYPE = "Content-Type";

    /**
     * Content-Type for "application/x-www-form-urlencoded"
     */
    public static final String PROTOCOL_CONTENT_TYPE_APPLICATION_URL_ENCODED = String
            .format("application/x-www-form-urlencoded; charset=%s",
                    PROTOCOL_CHARSET_DEFAULT);

}
