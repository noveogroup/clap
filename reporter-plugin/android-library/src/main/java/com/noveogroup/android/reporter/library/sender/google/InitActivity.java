/*
 * Copyright (c) 2015 Noveo
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

package com.noveogroup.android.reporter.library.sender.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.noveogroup.android.reporter.library.R;

import java.util.Collections;
import java.util.Set;

public class InitActivity extends Activity {

    private static final String PREFERENCES_NAME = "--com_noveogroup_reporter_google.preferences";
    private static final String KEY_AUTH_CODE = "auth-code";
    private static final String KEY_NEVER = "auth-code";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, MODE_MULTI_PROCESS);
    }

    public static String getAuthCode(Context context) {
        return getPreferences(context).getString(KEY_AUTH_CODE, null);
    }

    private static void setAuthCode(Context context, String authCode) {
        getPreferences(context).edit().putString(KEY_AUTH_CODE, authCode).commit();
    }

    private static boolean getNever(Context context) {
        return getPreferences(context).getBoolean(KEY_NEVER, false);
    }

    private static void setNever(Context context, boolean never) {
        getPreferences(context).edit().putBoolean(KEY_NEVER, never).commit();
    }

    public static void start(Context context, Bundle meta) {
        context.startActivity(
                new Intent(context, InitActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtras(meta));
    }

    private static final Set<String> AUTH_SCOPES = Collections.unmodifiableSet(Collections.singleton("https://spreadsheets.google.com/feeds"));

    private String getAuthClientId() {
        return getIntent().getStringExtra(GoogleSender.META_CLIENT_ID);
    }

    private String getAuthRedirectUri() {
        return getIntent().getStringExtra(GoogleSender.META_REDIRECT_URI);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getNever(this)) {
            finish();
        } else {
            setContentView(R.layout.com_noveogroup_reporter_google_init);

            WebView webView = (WebView) findViewById(R.id.com_noveogroup_reporter_google_init_web);
            Button laterView = (Button) findViewById(R.id.com_noveogroup_reporter_google_init_later);
            Button neverView = (Button) findViewById(R.id.com_noveogroup_reporter_google_init_never);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith(getAuthRedirectUri())) {
                        String code = Uri.parse(url).getQueryParameter("code");
                        setAuthCode(InitActivity.this, code);
                        finish();
                    }
                    return false;
                }
            });
            webView.loadUrl(new GoogleAuthorizationCodeRequestUrl(getAuthClientId(), getAuthRedirectUri(), AUTH_SCOPES).build());

            laterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            neverView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNever(InitActivity.this, true);
                    finish();
                }
            });
        }
    }

}
