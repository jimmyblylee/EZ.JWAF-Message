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

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import com.lee.util.ObjectUtils;
import com.lee.util.StringUtils;

/**
 * ClassName : Messages <br>
 * Description : International Message manager <br>
 * get the Locale by steps as below:
 * <ol>
 * <li>find the local by class com.lee.jwaf.context.ActionContext.getContext().getLocale(), not found then go to the
 * next step</li>
 * <li>find the local by system properties "application.language" and "application.country", not found then go to the
 * next step</li>
 * <li>find the local by {@code Local.getDefault()}</li>
 * </ol>
 * Create Time : 2016-09-23 <br>
 *
 * @author jimmyblylee@126.com
 */
public enum Messages {
    /** Msg singleton. */
    Msg;

    /** Key of language. */
    protected static final String CNS_KEY_LANGUAGE = "application.locale.language";
    /** Key of country.. */
    protected static final String CNS_KEY_COUNTRY = "application.locale.country";
    /** Key of base name. */
    protected static final String CNS_KEY_BASE_NAME = "application.locale.basenames";

    /** Map&lt;baseName, Map&lt;locale.tolanguage(), ResourceBundle&gt;&gt;. */
    private Map<String, Map<String, ResourceBundle>> resources = new HashMap<>();

    /**
     * Description : get the Locale by steps as below:
     * <ol>
     * <li>find the local by class com.lee.jwaf.context.ActionContext.getContext().getLocale(), not found then go to the
     * next step</li>
     * <li>find the local by system properties "application.language" and "application.country", not found then go to
     * the next step</li>
     * <li>find the local by {@code Local.getDefault()}</li>
     * </ol>
     * Create Time: 2016-09-24 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return the Locale
     */
    protected static Locale getLocale() {
        Locale result;
        try {
            Class.forName("javax.servlet.http.HttpServletResponse");
            final Class<?> context = Class.forName("com.lee.jwaf.context.ActionContext");
            final Object instance = context.getMethod("getContext", new Class<?>[]{}).invoke(null);
            result = (Locale) context.getMethod("getLocale", new Class<?>[]{}).invoke(instance);
            if (ObjectUtils.isEmpty(result)) {
                throw new Exception();
            }
            // CSOFF: IllegalCatch
        } catch (Exception ex) {
            // CSON: IllegalCatch
            final String language = System.getProperty(CNS_KEY_LANGUAGE);
            final String country = System.getProperty(CNS_KEY_COUNTRY);
            if (language != null && !"".equalsIgnoreCase(language) && country != null
                && !"".equalsIgnoreCase(country)) {
                result = new Locale(language, country);
            } else {
                result = Locale.getDefault();
            }
        }
        return result;
    }

    /**
     * Description : get base name from sytem properties, by key {@link #CNS_KEY_BASE_NAME} <br>
     * Create Time: 2016-09-24 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @return baseName set
     */
    protected static Set<String> getBaseNames() {
        return System.getProperties().containsKey(CNS_KEY_BASE_NAME)
            ? new HashSet<>(Arrays.asList(System.getProperty(CNS_KEY_BASE_NAME).split(","))) : new HashSet<>();
    }

    /**
     * Description : create a bundle and insert it into current cached bundles, if it's new for the cached bundle
     * resource <br>
     * Create Time: 2016-09-24 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param baseName bundle baseName
     * @param locale   the locale
     * @throws NullPointerException     if the baseName or the locale is empty
     * @throws MissingResourceException if no resource bundle for the specified base name can be found
     */
    private void createBundleIfNessary(String baseName, Locale locale) throws NullPointerException,
            MissingResourceException {
        if (ObjectUtils.isEmpty(locale) || StringUtils.isEmpty(baseName)) {
            throw new NullPointerException(
                "illegal argument for createBundleIfNessary, baseName, locale should not be null");
        }
        if (!resources.containsKey(baseName) || !resources.get(baseName).containsKey(locale.toLanguageTag())) {
            synchronized (Msg) {
                if (!resources.containsKey(baseName) || !resources.get(baseName).containsKey(locale.toLanguageTag())) {
                    if (!resources.containsKey(baseName)) {
                        resources.put(baseName, new HashMap<>());
                    }
                    if (!resources.get(baseName).containsKey(locale.toLanguageTag())) {
                        resources.get(baseName).put(locale.toLanguageTag(),
                            ResourceBundle.getBundle(baseName + "-messages", locale));
                    }
                }
            }
        }
    }

    /**
     * Description : get message from bundles <br>
     * Create Time: 2016-09-24 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param baseName       the bundle base name
     * @param code           the code in the bundle
     * @param args           the arguments for the formated string
     *                       (wich will formated by {@link MessageFormat#format(Object)}
     * @param defaultMessage the default message if the message can not be found by given code
     * @param locale         the locale
     * @return the formated message
     * @throws NullPointerException if the baseNmae, code, defaultMessage or the locale is empty
     * @throws NoSuchMessageException if there is no such messages.
     */
    @SuppressWarnings("SameParameterValue")
    public String msg(final String baseName, final String code, final Object[] args, final String defaultMessage,
                      final Locale locale) throws NullPointerException, NoSuchMessageException {
        final String result;
        if (ObjectUtils.isEmpty(locale) || StringUtils.isEmpty(baseName) || StringUtils.isEmpty(code)
            || StringUtils.isEmpty(defaultMessage)) {
            throw new NullPointerException(
                "illegal argument for msg, code, baseName, defaultMessage, locale should not be null");
        }
        createBundleIfNessary(baseName, locale);
        if (!resources.get(baseName).get(locale.toLanguageTag()).containsKey(code)) {
            if (ObjectUtils.isEmpty(args)) {
                result = defaultMessage;
            } else {
                result = MessageFormat.format(defaultMessage, args);
            }
        } else {
            if (ObjectUtils.isEmpty(args)) {
                result = resources.get(baseName).get(locale.toLanguageTag()).getString(code);
            } else {
                result = MessageFormat.format(resources.get(baseName).get(locale.toLanguageTag()).getString(code),
                    args);
            }
        }
        return result;
    }

    /**
     * Description : get message from bundles <br>
     * Create Time: 2016-09-24 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param baseName the bundle base name
     * @param code     the code in the bundle
     * @param args     the arguments for the formated string (wich will formated by {@link MessageFormat#format(Object)}
     * @param locale   the locale
     * @return the formated message
     * @throws NullPointerException   if the baseNmae, code, defaultMessage or the locale is empty
     * @throws NoSuchMessageException if it can not find the message by given code in target bundle
     */
    public String msg(String baseName, String code, Object[] args, Locale locale)
            throws NullPointerException, NoSuchMessageException {
        if (ObjectUtils.isEmpty(locale) || StringUtils.isEmpty(baseName)
            || StringUtils.isEmpty(code)) {
            throw new NullPointerException(
                "illegal argument for msg, code, baseName, defaultMessage, locale should not be null");
        }
        createBundleIfNessary(baseName, locale);
        if (!resources.get(baseName).get(locale.toLanguageTag()).containsKey(code)) {
            throw new NoSuchMessageException(code);
        } else {
            if (ObjectUtils.isEmpty(args)) {
                return resources.get(baseName).get(locale.toLanguageTag()).getString(code);
            } else {
                return MessageFormat.format(resources.get(baseName).get(locale.toLanguageTag()).getString(code), args);
            }
        }
    }

    /**
     * Description : get message from bundles by default locale logic with {@link #getLocale()} <br>
     * Create Time: 2016-09-24 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param baseName the bundle base name
     * @param code     the code in the bundle
     * @param args     the arguments for the formated string (wich will formated by {@link MessageFormat#format(Object)}
     * @return the formated message
     * @throws NullPointerException   if the baseNmae, code, defaultMessage or the locale is empty
     * @throws NoSuchMessageException if it can not find the message by given code in target bundle
     */
    @SuppressWarnings("SameParameterValue")
    public String msg(String baseName, String code, Object[] args) throws NullPointerException, NoSuchMessageException {
        return msg(baseName, code, args, getLocale());
    }

    /**
     * Description : get message from bundles by default locale logic with {@link #getLocale()}, and default bundle base
     * name logic with {@link Messages#getBaseNames()} <br>
     * Create Time: 2016-09-24 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param code the code in the bundle
     * @param args the arguments for the formated string (wich will formated by {@link MessageFormat#format(Object)}
     * @return the formated message
     * @throws NullPointerException   if the baseNmae, code, defaultMessage or the locale is empty
     * @throws NoSuchMessageException if it can not find the message by given code in target bundle
     */
    public String msg(String code, Object[] args) throws NullPointerException, NoSuchMessageException {
        for (String baseName : getBaseNames()) {
            try {
                return msg(baseName, code, args, getLocale());
            } catch (NoSuchMessageException ex) {
                // ignore
            }
        }
        throw new NoSuchMessageException(code);
    }

    /**
     * Description : get message from bundles by default locale logic with {@link #getLocale()}, and default bundle base
     * name logic with {@link Messages#getBaseNames()} <br>
     * Create Time: 2016-09-24 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param code the code in the bundle
     * @return the formated message
     * @throws NullPointerException   if the baseNmae, code, defaultMessage or the locale is empty
     * @throws NoSuchMessageException if it can not find the message by given code in target bundle
     */
    @SuppressWarnings("SameParameterValue")
    public String msg(String code) throws NullPointerException, NoSuchMessageException {
        return msg(code, null);
    }
}
