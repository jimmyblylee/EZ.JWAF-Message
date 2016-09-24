/**
 * Project Name : jwaf-message <br>
 * File Name : NoSuchMessageException.java <br>
 * Package Name : com.lee.jwaf.message <br>
 * Create Time : 2016-09-23 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.message;

import java.util.Locale;

/**
 * ClassName : NoSuchMessageException <br>
 * Description : Throw up when a message can not be resolved <br>
 * Create Time : 2016-09-23 <br>
 * Create by : jimmyblylee@126.com
 */
public class NoSuchMessageException extends RuntimeException {

    private static final long serialVersionUID = 3303397835324846454L;

    public NoSuchMessageException(String code, Locale locale) {
        super("No message found under code '" + code + "' for locale '" + locale + "'.");
    }

    public NoSuchMessageException(String code) {
        this(code,
                System.getProperties().contains("app.language") && System.getProperties().contains("app.country")
                        ? new Locale(System.getProperty("app.language"), System.getProperty("app.country"))
                        : Locale.getDefault());
    }
}
