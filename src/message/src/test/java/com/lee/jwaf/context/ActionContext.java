/**
 * Project Name : jwaf-message <br>
 * File Name : ActionContext.java <br>
 * Package Name : com.lee.jwaf.context <br>
 * Create Time : 2016-09-24 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.context;

import java.util.Locale;

/**
 * ClassName : ActionContext <br>
 * Description : For Message get Locale test <br>
 * Create Time : 2016-09-24 <br>
 * Create by : jimmyblylee@126.com
 */
public class ActionContext {

    private static ThreadLocal<ActionContext> ctx = new ThreadLocal<ActionContext>();

    public static void setContext(ActionContext ctx) {
        ActionContext.ctx.set(ctx);
    }

    public static ActionContext getContext() {
        return ctx.get();
    }

    private Locale locale;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
