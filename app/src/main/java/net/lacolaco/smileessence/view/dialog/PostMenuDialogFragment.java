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

package net.lacolaco.smileessence.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.command.CommandOpenTemplateList;
import net.lacolaco.smileessence.command.post.PostCommandMakeAnonymous;
import net.lacolaco.smileessence.command.post.PostCommandMorse;
import net.lacolaco.smileessence.command.post.PostCommandZekamashi;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;

import java.util.ArrayList;
import java.util.List;

public class PostMenuDialogFragment extends MenuDialogFragment {

    public static final String TAG = "postMenu";

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        Account account = activity.getCurrentAccount();
        List<Command> commands = getCommands(activity);
        Command.filter(commands);
        if (commands.isEmpty()) {
            Notificator.publish(getActivity(), R.string.notice_no_command_exists);
            return new DisposeDialog(getActivity());
        }
        View body = activity.getLayoutInflater().inflate(R.layout.dialog_menu_list, null);
        ListView listView = (ListView) body.findViewById(R.id.listview_dialog_menu_list);
        CustomListAdapter<Command> adapter = new CustomListAdapter<>(activity, Command.class);
        listView.setAdapter(adapter);
        for (Command command : commands) {
            adapter.addToBottom(command);
        }
        adapter.update();
        listView.setOnItemClickListener(onItemClickListener);

        return new AlertDialog.Builder(activity)
                .setView(body)
                .setCancelable(true)
                .create();
    }

    @Override
    protected void executeCommand(Command command) {
        if (command.execute()) {
            dismiss();
            DialogHelper.close(getActivity(), TAG);
        }
    }

    // -------------------------- OTHER METHODS --------------------------

    public List<Command> getCommands(Activity activity) {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new CommandOpenTemplateList(activity));
        commands.add(new PostCommandMorse(activity));
        commands.add(new PostCommandMakeAnonymous(activity));
        commands.add(new PostCommandZekamashi(activity));
        return commands;
    }
}
