package com.example.fivecontacts.main.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class WarringAboutPermissions extends DialogFragment {

    String menssage;
    String title;
    int code;

    public WarringAboutPermissions(String menssage, String title, int code) {
        this.menssage = menssage;
        this.title = title;
        this.code = code;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(getActivity());
        builderDialog.setMessage(this.menssage)
                .setTitle(this.title);
        builderDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                listener.onDialogPositiveClick(code);
            }
        });
        AlertDialog adialog = builderDialog.create();
        return adialog;
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(int codigo);

    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
