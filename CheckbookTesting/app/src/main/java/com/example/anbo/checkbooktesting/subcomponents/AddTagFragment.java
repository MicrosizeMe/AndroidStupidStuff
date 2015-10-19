package com.example.anbo.checkbooktesting.subcomponents;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.anbo.checkbooktesting.R;

import java.util.List;

/**
 * Created by Anbo on 10/15/2015.
 */
public class AddTagFragment extends DialogFragment {

    public interface AddTagFragmentListener {
        String[] getTagStrings();

        void receiveString(String string);
    }

    AddTagFragmentListener activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (AddTagFragmentListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement AddTagFragmentListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.tag_entry_dialogue_title);

        final AutoCompleteTextView textView = new AutoCompleteTextView(getActivity());
        textView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item,
                activity.getTagStrings()));
        textView.setThreshold(0);
        textView.setHint(R.string.tag_entry_or_search_enter_tag_hint);
        textView.requestFocus();
        builder.setView(textView);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = textView.getText().toString().trim().toLowerCase();
                if (input.isEmpty()) {
                    Toast toast = Toast.makeText(getActivity(), "Actually include a tag, dipshit."
                        , Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                activity.receiveString(textView.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });

        return builder.create();
    }
}
