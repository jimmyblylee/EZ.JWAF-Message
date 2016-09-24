/**
 * Project Name : jwaf-message <br>
 * File Name : FutureCallable.java <br>
 * Package Name : com.lee.jwaf.message <br>
 * Create Time : 2016-09-24 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.message;

import java.util.Locale;
import java.util.concurrent.Callable;

import com.lee.jwaf.context.ActionContext;

/**
 * ClassName : FutureCallable <br>
 * Description : simulation of standard thread <br>
 * Create Time : 2016-09-24 <br>
 * Create by : jimmyblylee@126.com
 */
public class FutureCallable implements Callable<Locale> {

    private Locale locale;

    public FutureCallable(Locale locale) {
        this.locale = locale;
    }

    @Override
    public Locale call() throws Exception {
        ActionContext ctx = new ActionContext();
        ctx.setLocale(locale);
        ActionContext.setContext(ctx);
        return Messages.getLocale();
    }

}
