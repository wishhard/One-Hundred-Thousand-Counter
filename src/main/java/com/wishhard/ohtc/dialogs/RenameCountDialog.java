package com.wishhard.ohtc.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wishhard.ohtc.R;

import static android.view.KeyEvent.*;


public class RenameCountDialog extends DialogFragment {
    public static final String RENAME_COUNT_KEY = "for_rename_dialog_key";

    private EditText renameEt;
    private TextView errTv;
    private String forChange;

    private RenameDialogListener mListener;

    public static RenameCountDialog newInstance(String countTitle) {

        Bundle args = new Bundle();
        args.putString(RENAME_COUNT_KEY,countTitle);
        RenameCountDialog fragment = new RenameCountDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        setCancelable(false);



        View view = inflater.inflate(R.layout.rename_count_dialog,container,false);


        errTv =  view.findViewById(R.id.renameErrTv);
        renameEt = view.findViewById(R.id.renameEt);



        forChange = getArguments().getString(RENAME_COUNT_KEY);
        renameEt.setText(forChange);
        renameEt.setSelection(0,forChange.length());






        Button cancel = (Button) view.findViewById(R.id.renameBtnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button save = (Button) view.findViewById(R.id.renameBtnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forChange = renameEt.getText().toString();
                if(isCountTitleRenameValid()) {
                    mListener.changedName(forChange);
                    dismiss();
                } else {
                    errTv.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
         final Dialog dialog = super.onCreateDialog(savedInstanceState);
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
             @Override
             public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                 if(i == KEYCODE_BACK) {
                     dismiss();
                 }

                 return false;
             }
         });
        return dialog;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        renameEt.post(new Runnable()
        {
            @Override
            public void run()
            {
                renameEt.requestFocus();
                InputMethodManager imm =
                        (InputMethodManager)renameEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.showSoftInput(renameEt, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mListener = (RenameDialogListener) activity;
    }





    private boolean isCountTitleRenameValid() {
        String rename = forChange;

        rename.trim();
        rename.replaceAll("\\s{2,}"," ");

        if (rename.length() < 3 || rename.length() > 60 || rename.isEmpty()) {
            return false;
        }

        forChange = rename;
        return true;
    }



    public interface RenameDialogListener {
        void changedName(String forChange);
    }



}
