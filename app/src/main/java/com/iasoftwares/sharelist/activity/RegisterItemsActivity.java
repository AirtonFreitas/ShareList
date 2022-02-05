package com.iasoftwares.sharelist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iasoftwares.sharelist.DialogQuestionNameList;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.model.ProdutosLista;

public class RegisterItemsActivity extends AppCompatActivity implements DialogQuestionNameList.DialogListener {
    private Spinner spinnerCategories, spinnerUN;
    private EditText descricao, observacao, quantidade;
    private ProdutosLista produtosLista;
    private Button btnSave, btnBack;
    private TextView textViewNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_items);
        spinnerCategories = findViewById(R.id.spinnerCateg);
        spinnerUN = findViewById(R.id.spinnerUN);
        descricao = findViewById(R.id.descricaoItemID);
        observacao = findViewById(R.id.observacaoItemID);
        quantidade = findViewById(R.id.quantidadeItemID);
        btnSave = findViewById(R.id.btnSaveID);
        btnBack = findViewById(R.id.btnBackID);
        textViewNameList = findViewById(R.id.textViewNameList);
        openDialog();


        AtivarSpinnerCateg();
        AtivarSpinnerUn();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GravarItem();
            }

        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterItemsActivity.this, ListsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void GravarItem() {
        String textDescricao = descricao.getText().toString();
        if (!textDescricao.isEmpty()) {
            salvarLista();
            Toast.makeText(RegisterItemsActivity.this, "Item gravado com sucesso", Toast.LENGTH_SHORT).show();
            descricao.setText("");
            observacao.setText("");
            quantidade.setText("");
            descricao.requestFocus();
            AtivarSpinnerCateg();
            AtivarSpinnerUn();
        } else {
            descricao.setError("Campo vazio!");
            descricao.requestFocus();
        }
    }

    private void AtivarSpinnerUn() {
        String[] arraySpinnerUn = new String[]{
                "UN", "KG", "LT", "CX", "PCT", "RL"
        };
        Spinner spinnerUnid = (Spinner) findViewById(R.id.spinnerUN);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerUn);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnid.setAdapter(adapter);
    }

    private void openDialog() {
        DialogQuestionNameList dialogQuestionNameList = new DialogQuestionNameList();
        dialogQuestionNameList.show(getSupportFragmentManager(), "Dialog");

    }

    private void AtivarSpinnerCateg() {
        String[] arraySpinner = new String[]{
                "Escolha a Categoria", "Alimentos", "Bebidas", "Bebidas Alcoólicas", "Congelados e frios", "Higiene Pessoal", "Hortifruti", "Padaria", "Produtos de Limpeza", "Outros"
        };
        Spinner spinnerCategories = (Spinner) findViewById(R.id.spinnerCateg);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);
    }

    public void salvarLista() {
        produtosLista = new ProdutosLista();
        produtosLista.setNomeLista(textViewNameList.getText().toString());
        produtosLista.setDescricao(descricao.getText().toString());
        produtosLista.setCategoria(spinnerCategories.getSelectedItem().toString());
        produtosLista.setUnd(spinnerUN.getSelectedItem().toString());
        produtosLista.setObservacao(observacao.getText().toString());
        produtosLista.setQuantidade(quantidade.getText().toString());
        produtosLista.setStatus("N");
        produtosLista.salvar();
    }

    @Override
    public void appyText(String nameList) {
        textViewNameList.setText(nameList);
    }
}