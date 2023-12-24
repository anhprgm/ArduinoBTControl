package com.arduno.remotebt.dialogs;

import android.content.Context;
import android.view.LayoutInflater;

import com.arduno.remotebt.databinding.DialogEditRemoteBinding;

public class DialogData extends BaseDialog<DialogEditRemoteBinding, DialogData.ExtendBuilder> {


    public DialogData(Context context, ExtendBuilder builder) {
        super(context, builder);
    }

    @Override
    protected DialogEditRemoteBinding getViewBinding() {
        return DialogEditRemoteBinding.inflate(LayoutInflater.from(getContext()));
    }

    @Override
    protected void initListener() {

    }

    public static class ExtendBuilder extends BuilderDialog {
        public ExtendBuilder(Context context) {
            super(context);
        }

        @Override
        public BaseDialog build() {
            return new DialogData(context, this);
        }

    }
}

interface onItemClickListener {
    void onItemClick(String s);
}
