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

package net.lacolaco.smileessence.data;

import android.test.ActivityInstrumentationTestCase2;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import net.lacolaco.smileessence.activity.MainActivity;

public class ImageCacheTest extends ActivityInstrumentationTestCase2<MainActivity>
{

    String imageURL = "http:\\/\\/pbs.twimg.com\\/media\\/BjLX2JRCQAE27mw.png";

    public ImageCacheTest()
    {
        super(MainActivity.class);
    }

    public void testGetInstance() throws Exception
    {
        assertNotNull(ImageCache.getInstance());
    }

    public void testRequest() throws Exception
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                ImageLoader.ImageContainer image = ImageCache.getInstance().requestBitmap(imageURL);
                assertEquals(imageURL, image.getRequestUrl());
            }
        });
    }

    public void testSetImageToView() throws Exception
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                NetworkImageView view = new NetworkImageView(getActivity());
                ImageCache.getInstance().setImageToView(imageURL, view);
            }
        });

    }
}
