package com.iasoftwares.sharelist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.iasoftwares.sharelist.activity.MainActivity;


public class DialogQuestionNameList extends AppCompatDialogFragment {
    private EditText edtTextName;
    private DialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle("Qual lista você deseja criar?")
                .setIcon(R.drawable.ic_baseline_playlist_add)
                .setNeutralButton("Voltar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("Gravar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edtTextName = view.findViewById(R.id.edtTextNameList);
                        String nameList = edtTextName.getText().toString();
                        if(!nameList.isEmpty()){
                            listener.appyText(nameList);
                        }else{
                            listener.appyText("Lista de Compras");
                        }
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface DialogListener{
        void appyText(String nameList);
}

}
