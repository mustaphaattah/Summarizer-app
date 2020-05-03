package com.mtah.summerizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SaveDialog extends AppCompatDialogFragment {
    private EditText summaryName;
    private SaveDialogListener saveDialogListener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Save Summary")
                .setCancelable(true)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Dismiss dialog
                    }
                })
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (summaryName.getText().toString() != null) {
                            String saveName = summaryName.getText().toString();
                            saveDialogListener.applyText(saveName);
                        } else {
                            Toast.makeText(getActivity(), "Enter summary name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        summaryName = view.findViewById(R.id.saveEditText);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            saveDialogListener = (SaveDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " must implement SaveDialogListener");
        }
    }

    public interface SaveDialogListener{
        void applyText(String name);
    }
}
