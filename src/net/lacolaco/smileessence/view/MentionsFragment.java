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

package net.lacolaco.smileessence.view;

import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.MentionsTimelineTask;
import net.lacolaco.smileessence.view.adapter.StatusListAdapter;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import twitter4j.Paging;
import twitter4j.Twitter;

public class MentionsFragment extends CustomListFragment
{

    // --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    protected PullToRefreshBase.Mode getRefreshMode()
    {
        return PullToRefreshBase.Mode.BOTH;
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface OnRefreshListener2 ---------------------

    @Override
    public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView)
    {
        final MainActivity activity = (MainActivity) getActivity();
        final Account currentAccount = activity.getCurrentAccount();
        Twitter twitter = TwitterApi.getTwitter(currentAccount);
        final StatusListAdapter adapter = getListAdapter(activity);
        Paging paging = getPaging(getPagingCount(activity));
        if(adapter.getCount() > 0)
        {
            paging.setSinceId(getTopID(adapter));
        }
        new MentionsTimelineTask(twitter, activity, paging)
        {
            @Override
            protected void onPostExecute(twitter4j.Status[] statuses)
            {
                super.onPostExecute(statuses);
                for(int i = statuses.length - 1; i >= 0; i--)
                {
                    twitter4j.Status status = statuses[i];
                    adapter.addToTop(new StatusViewModel(status, currentAccount));
                }
                updateListViewWithNotice(refreshView.getRefreshableView(), adapter, true);
                refreshView.onRefreshComplete();
            }
        }.execute();
    }

    @Override
    public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView)
    {
        final MainActivity activity = (MainActivity) getActivity();
        final Account currentAccount = activity.getCurrentAccount();
        Twitter twitter = TwitterApi.getTwitter(currentAccount);
        final StatusListAdapter adapter = getListAdapter(activity);
        Paging paging = getPaging(getPagingCount(activity));
        if(adapter.getCount() > 0)
        {
            paging.setMaxId(getLastID(adapter) - 1);
        }
        new MentionsTimelineTask(twitter, activity, paging)
        {
            @Override
            protected void onPostExecute(twitter4j.Status[] statuses)
            {
                super.onPostExecute(statuses);
                for(twitter4j.Status status : statuses)
                {
                    adapter.addToBottom(new StatusViewModel(status, currentAccount));
                }
                updateListViewWithNotice(refreshView.getRefreshableView(), adapter, false);
                refreshView.onRefreshComplete();
            }
        }.execute();
    }

    private long getLastID(StatusListAdapter adapter)
    {
        return ((StatusViewModel) adapter.getItem(adapter.getCount() - 1)).getID();
    }

    private StatusListAdapter getListAdapter(MainActivity activity)
    {
        return (StatusListAdapter) activity.getListAdapter(MainActivity.PAGE_MENTIONS);
    }

    private Paging getPaging(int count)
    {
        return new Paging(1).count(count);
    }

    private int getPagingCount(MainActivity activity)
    {
        return activity.getUserPreferenceHelper().getValue(R.string.key_setting_timelines, 20);
    }

    private long getTopID(StatusListAdapter adapter)
    {
        return ((StatusViewModel) adapter.getItem(0)).getID();
    }
}
