package com.example.anbo.checkbooktesting.subcomponents;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.anbo.checkbooktesting.R;

/**
 * Created by Anbo on 11/9/2015.
 */
public class DeletionConfirmationFragment extends DialogFragment {

    public abstract interface DeletionConfirmationReceiver {
        void onDeletionConfirm();
    }

    DeletionConfirmationReceiver activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (DeletionConfirmationReceiver) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement DeletionConfirmationReciever");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.deletion_confirmation_fragment_deletion_confirm);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.onDeletionConfirm();
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
