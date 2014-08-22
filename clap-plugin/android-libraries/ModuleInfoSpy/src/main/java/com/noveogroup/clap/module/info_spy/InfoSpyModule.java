/*
 * Copyright (c) 2014 Noveo Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Except as contained in this notice, the name(s) of the above copyright holders
 * shall not be used in advertising or otherwise to promote the sale, use or
 * other dealings in this Software without prior written authorization.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.noveogroup.clap.module.info_spy;

import com.noveogroup.clap.library.api.ClapApi;
import com.noveogroup.clap.library.common.AndroidContext;
import com.noveogroup.clap.library.common.Module;
import com.noveogroup.clap.library.common.StaticContext;

public final class InfoSpyModule implements Module {

    private static final String KEY_INFO_SENT = "info-sent";

    @Override
    public void initStatic(StaticContext context) {
        // do nothing
    }

    @Override
    public void initContext(final AndroidContext context) {
        if (context.getPreferences().getBoolean(KEY_INFO_SENT, false)) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        ClapApi clapApi = new ClapApi(context.getContext());
                        String token = clapApi.retrieveToken();
                        clapApi.sendInfo(clapApi.prepareInfoRequest(token));
                        context.getPreferences().edit().putBoolean(KEY_INFO_SENT, true).commit();
                    } catch (Exception ignored) {
                    }
                }
            }.start();
        }
    }

}
