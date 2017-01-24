/* ***************************************************************************
 * EZ.JWAF/EZ.JCWAP: Easy series Production.
 * Including JWAF(Java-based Web Application Framework)
 * and JCWAP(Java-based Customized Web Application Platform).
 * Copyright (C) 2016-2017 the original author or authors.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of MIT License as published by
 * the Free Software Foundation;
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the MIT License for more details.
 *
 * You should have received a copy of the MIT License along
 * with this library; if not, write to the Free Software Foundation.
 * ***************************************************************************/

package com.lee.jwaf.message;

import java.util.Locale;

/**
 * ClassName : NoSuchMessageException <br>
 * Description : Throw up when a message can not be resolved <br>
 * Create Time : 2016-09-23 <br>
 * @author jimmyblylee@126.com
 */
@SuppressWarnings("WeakerAccess")
public class NoSuchMessageException extends RuntimeException {

    private static final long serialVersionUID = 3303397835324846454L;

    /**
     * Default Constructor.
     * @param code the message code
     * @param locale the locale of the message
     */
    public NoSuchMessageException(String code, Locale locale) {
        super("No message found under code '" + code + "' for locale '" + locale + "'.");
    }

    /**
     * Default constructor.
     * @param code the message code
     */
    public NoSuchMessageException(String code) {
        this(code,
                System.getProperties().contains("app.language") && System.getProperties().contains("app.country")
                        ? new Locale(System.getProperty("app.language"), System.getProperty("app.country"))
                        : Locale.getDefault());
    }
}
