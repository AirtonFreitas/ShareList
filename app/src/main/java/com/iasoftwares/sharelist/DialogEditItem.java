package com.iasoftwares.sharelist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


public class DialogEditItem extends AppCompatDialogFragment {

    private EditText edtTextDesc;
    private EditText edtTextQtd;
    private DialogEditItem.DialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_edit_item, null);
        builder.setView(view)
                .setTitle("Digite os novos dados")
                .setIcon(R.drawable.ic_baseline_playlist_add)
                .setNeutralButton("Voltar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Gravar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edtTextDesc = view.findViewById(R.id.editNewDescription);
                        edtTextQtd = view.findViewById(R.id.editNewQtd);
                        String descriptionNewText = edtTextDesc.getText().toString();
                        String qtdString = edtTextQtd.getText().toString();

                        if(!descriptionNewText.isEmpty()){
                            if(!qtdString.isEmpty()){
                                int qtdNewText = Integer.parseInt(qtdString);
                                listener.appyText(descriptionNewText, qtdNewText);

                            }else{
                                Toast.makeText(getContext(),"Quantidade Vazia, Item não editado", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getContext(),"Descrição Vazia, Item não editado", Toast.LENGTH_LONG).show();
                            }
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogEditItem.DialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface DialogListener{
        void appyText(String newDescription, int newQtd);
    }
}
