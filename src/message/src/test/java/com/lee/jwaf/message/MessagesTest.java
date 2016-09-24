/**
 * Project Name : jwaf-message <br>
 * File Name : MessagesTest.java <br>
 * Package Name : com.lee.jwaf.message <br>
 * Create Time : 2016-09-24 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.message;

import static com.lee.jwaf.message.Messages.Msg;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;

import com.lee.jwaf.context.ActionContext;

/**
 * ClassName : MessagesTest <br>
 * Description : Unit test for Messages <br>
 * Create Time : 2016-09-24 <br>
 * Create by : jimmyblylee@126.com
 */
public class MessagesTest {

    @Before
    public void init() {
        // clear system properties
        System.getProperties().remove(Messages.CNS_KEY_COUNTRY);
        System.getProperties().remove(Messages.CNS_KEY_BASE_NAME);
        System.getProperties().remove(Messages.CNS_KEY_LANGUAGE);
        ActionContext.setContext(null);
    }

    @Test
    public void testGetLocaleByActionContext() {
        try {
            assertThat(Executors.newCachedThreadPool().submit(new FutureCallable(new Locale("zh", "CN"))).get()
                    .toLanguageTag(), is("zh-CN"));
            assertThat(Executors.newCachedThreadPool().submit(new FutureCallable(new Locale("zh", "TW"))).get()
                    .toLanguageTag(), is("zh-TW"));
            assertThat(Executors.newCachedThreadPool().submit(new FutureCallable(new Locale("en", "US"))).get()
                    .toLanguageTag(), is("en-US"));
        } catch (InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void testGetLocaleBySystemProperties() {
        System.setProperty(Messages.CNS_KEY_LANGUAGE, "zh");
        System.setProperty(Messages.CNS_KEY_COUNTRY, "CN");
        assertThat(Messages.getLocale().toLanguageTag(), is("zh-CN"));
    }

    @Test
    public void testGetLocaleByDefault() {
        Locale.setDefault(Locale.GERMANY);
        assertThat(Messages.getLocale().toLanguageTag(), is("de-DE"));
    }

    @Test
    public void testMsg1Param() {
        /*********
         * by context
         *********/
        System.setProperty(Messages.CNS_KEY_BASE_NAME, "test");
        ActionContext ctx = new ActionContext();
        ctx.setLocale(new Locale("zh", "CN"));
        ActionContext.setContext(ctx);
        assertThat(Msg.msg("test.msg"), is("{0} 你好，现在时间是 {1,time,H-m-s}"));
        ActionContext.getContext().setLocale(new Locale("en", "US"));
        assertThat(Msg.msg("test.msg"), is("hello {0} the time is {1,time,H-m-s}"));

        init();
        /*********
         * by system properties
         *********/
        System.setProperty(Messages.CNS_KEY_BASE_NAME, "test");
        System.setProperty(Messages.CNS_KEY_LANGUAGE, "zh");
        System.setProperty(Messages.CNS_KEY_COUNTRY, "CN");
        assertThat(Msg.msg("test.msg"), is("{0} 你好，现在时间是 {1,time,H-m-s}"));
        System.setProperty(Messages.CNS_KEY_LANGUAGE, "en");
        System.setProperty(Messages.CNS_KEY_COUNTRY, "US");
        assertThat(Msg.msg("test.msg"), is("hello {0} the time is {1,time,H-m-s}"));

        init();
        /*********
         * by default
         *********/
        System.setProperty(Messages.CNS_KEY_BASE_NAME, "test");
        Locale.setDefault(new Locale("zh", "CN"));
        assertThat(Msg.msg("test.msg"), is("{0} 你好，现在时间是 {1,time,H-m-s}"));
        Locale.setDefault(new Locale("en", "US"));
        assertThat(Msg.msg("test.msg"), is("hello {0} the time is {1,time,H-m-s}"));
    }

    @Test
    public void testMsg2Params() {
        Date d = new Date();
        /*********
         * by context
         *********/
        System.setProperty(Messages.CNS_KEY_BASE_NAME, "test");
        ActionContext ctx = new ActionContext();
        ctx.setLocale(new Locale("zh", "CN"));
        ActionContext.setContext(ctx);
        assertThat(Msg.msg("test.msg", new Object[] { "jimmy", d }),
                is("jimmy 你好，现在时间是 " + MessageFormat.format("{0,time,H-m-s}", d)));
        ActionContext.getContext().setLocale(new Locale("en", "US"));
        assertThat(Msg.msg("test.msg", new Object[] { "jimmy", d }),
                is("hello jimmy the time is " + MessageFormat.format("{0,time,H-m-s}", d)));

        init();
        /*********
         * by system properties
         *********/
        System.setProperty(Messages.CNS_KEY_BASE_NAME, "test");
        System.setProperty(Messages.CNS_KEY_LANGUAGE, "zh");
        System.setProperty(Messages.CNS_KEY_COUNTRY, "CN");
        assertThat(Msg.msg("test.msg", new Object[] { "jimmy", d }),
                is("jimmy 你好，现在时间是 " + MessageFormat.format("{0,time,H-m-s}", d)));
        System.setProperty(Messages.CNS_KEY_LANGUAGE, "en");
        System.setProperty(Messages.CNS_KEY_COUNTRY, "US");
        assertThat(Msg.msg("test.msg", new Object[] { "jimmy", d }),
                is("hello jimmy the time is " + MessageFormat.format("{0,time,H-m-s}", d)));

        init();
        /*********
         * by default
         *********/
        System.setProperty(Messages.CNS_KEY_BASE_NAME, "test");
        Locale.setDefault(new Locale("zh", "CN"));
        assertThat(Msg.msg("test.msg", new Object[] { "jimmy", d }),
                is("jimmy 你好，现在时间是 " + MessageFormat.format("{0,time,H-m-s}", d)));
        Locale.setDefault(new Locale("en", "US"));
        assertThat(Msg.msg("test.msg", new Object[] { "jimmy", d }),
                is("hello jimmy the time is " + MessageFormat.format("{0,time,H-m-s}", d)));
    }

    @Test
    public void testMsg3Params() {
        Date d = new Date();
        /*********
         * by context
         *********/
        ActionContext ctx = new ActionContext();
        ctx.setLocale(new Locale("zh", "CN"));
        ActionContext.setContext(ctx);
        assertThat(Msg.msg("test", "test.msg", new Object[] { "jimmy", d }),
                is("jimmy 你好，现在时间是 " + MessageFormat.format("{0,time,H-m-s}", d)));
        ActionContext.getContext().setLocale(new Locale("en", "US"));
        assertThat(Msg.msg("test", "test.msg", new Object[] { "jimmy", d }),
                is("hello jimmy the time is " + MessageFormat.format("{0,time,H-m-s}", d)));

        init();
        /*********
         * by system properties
         *********/
        System.setProperty(Messages.CNS_KEY_BASE_NAME, "test");
        System.setProperty(Messages.CNS_KEY_LANGUAGE, "zh");
        System.setProperty(Messages.CNS_KEY_COUNTRY, "CN");
        assertThat(Msg.msg("test", "test.msg", new Object[] { "jimmy", d }),
                is("jimmy 你好，现在时间是 " + MessageFormat.format("{0,time,H-m-s}", d)));
        System.setProperty(Messages.CNS_KEY_LANGUAGE, "en");
        System.setProperty(Messages.CNS_KEY_COUNTRY, "US");
        assertThat(Msg.msg("test", "test.msg", new Object[] { "jimmy", d }),
                is("hello jimmy the time is " + MessageFormat.format("{0,time,H-m-s}", d)));

        init();
        /*********
         * by default
         *********/
        System.setProperty(Messages.CNS_KEY_BASE_NAME, "test");
        Locale.setDefault(new Locale("zh", "CN"));
        assertThat(Msg.msg("test", "test.msg", new Object[] { "jimmy", d }),
                is("jimmy 你好，现在时间是 " + MessageFormat.format("{0,time,H-m-s}", d)));
        Locale.setDefault(new Locale("en", "US"));
        assertThat(Msg.msg("test", "test.msg", new Object[] { "jimmy", d }),
                is("hello jimmy the time is " + MessageFormat.format("{0,time,H-m-s}", d)));
    }

    @Test
    public void testMsg4Params() {
        Date d = new Date();
        assertThat(Msg.msg("test", "test.msg", new Object[] { "jimmy", d }, new Locale("zh", "CN")),
                is("jimmy 你好，现在时间是 " + MessageFormat.format("{0,time,H-m-s}", d)));
        assertThat(Msg.msg("test", "test.msg", new Object[] { "jimmy", d }, new Locale("en", "US")),
                is("hello jimmy the time is " + MessageFormat.format("{0,time,H-m-s}", d)));
    }

    @Test
    public void testMsg5Params() {
        assertThat(Msg.msg("test", "foo", null, "no message found", new Locale("zh", "CN")), is("no message found"));
    }
}
