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

package com.noveogroup.android.reporter.library.sender;

import com.noveogroup.android.reporter.library.events.Message;

import java.io.IOException;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Query;

public class RestSender implements Sender {

    private static interface SenderService {

        @POST("/send")
        public String send(@Query("application_id") String applicationId, @Query("device_id") String deviceId, @Body List<Message<?>> messages);

    }

    private final SenderService senderService;

    public RestSender(String endpoint) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .build();
        this.senderService = restAdapter.create(SenderService.class);
    }

    @Override
    public void send(String applicationId, String deviceId, List<Message<?>> messages) throws IOException {
        try {
            senderService.send(applicationId, deviceId, messages);
        } catch (Exception e) {
            IOException ioException = new IOException();
            ioException.initCause(e);
            throw ioException;
        }
    }

}
