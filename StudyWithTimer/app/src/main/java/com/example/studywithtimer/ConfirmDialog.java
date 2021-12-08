package com.example.studywithtimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class ConfirmDialog extends DialogFragment {

    private Button positiveButton;
    private Button negativeButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.fragment_confirm_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        positiveButton = (Button) v.findViewById(R.id.positive_button);
        negativeButton = (Button) v.findViewById(R.id.negative_button);

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPositiveButtonClick();
                dismiss();
            }
        });
        builder.setView(v);
        return builder.create();
    }

    private onButtonClickListener listener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener = (onButtonClickListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public interface onButtonClickListener{
        public void onPositiveButtonClick();
    }
}