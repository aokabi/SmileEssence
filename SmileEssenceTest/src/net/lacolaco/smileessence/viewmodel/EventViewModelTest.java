/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2014 lacolaco.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.lacolaco.smileessence.viewmodel;

import android.content.Context;
import android.content.res.Configuration;
import android.test.InstrumentationTestCase;
import net.lacolaco.smileessence.util.TwitterMock;
import twitter4j.Status;
import twitter4j.User;

import java.util.Locale;

public class EventViewModelTest extends InstrumentationTestCase
{

    TwitterMock mock;

    @Override
    public void setUp() throws Exception
    {
        mock = new TwitterMock(getInstrumentation().getContext());
    }

    public void testNewInstance() throws Exception
    {
        Status status = mock.getStatusMock();
        User source = mock.getUserMock();
        EventViewModel history = new EventViewModel(EnumEvent.FAVORITED, source, status);
        assertEquals(source.getId(), history.getSourceUserID());
        assertEquals(status.getId(), history.getTargetStatusID());
        assertEquals(String.format("Favorited by %s", source.getScreenName()), history.getFormatString(getInstrumentation().getTargetContext()));
        history = new EventViewModel(EnumEvent.RECEIVE_MESSAGE, source);
        Context context = getInstrumentation().getTargetContext();
        Configuration config = context.getResources().getConfiguration();
        config.locale = Locale.ENGLISH;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        assertEquals(String.format("Received %s's message", source.getScreenName()), history.getFormatString(context));
        config.locale = Locale.JAPANESE;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        assertEquals(String.format("%sからDMを受信", source.getScreenName()), history.getFormatString(context));
    }
}
