package com.iasoftwares.sharelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.iasoftwares.sharelist.model.ProdutosLista;

public class RegisterItemsActivity extends AppCompatActivity {
    private Spinner spinnerCategories;
    private Object ViewGroup;
    private EditText descricao, observacao, quantidade;
    private ProdutosLista produtosLista;
    private Button btnSave, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_items);
        spinnerCategories = findViewById(R.id.spinnerCateg);
        descricao = findViewById(R.id.descricaoItemID);
        observacao = findViewById(R.id.observacaoItemID);
        quantidade = findViewById(R.id.quantidadeItemID);
        btnSave = findViewById(R.id.btnSaveID);
        btnBack = findViewById(R.id.btnBackID);

        AtivarSpinner();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textDescricao = descricao.getText().toString();
                if (!textDescricao.isEmpty()) {
                    salvarLista();
                    Toast.makeText(RegisterItemsActivity.this, "Item Gravado com sucesso", Toast.LENGTH_SHORT).show();
                    descricao.setText("");
                    observacao.setText("");
                    descricao.requestFocus();
                    AtivarSpinner();
                } else {
                    descricao.setError("Campo vazio!");
                    descricao.requestFocus();
                }
            }

        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        Intent intent = new Intent(RegisterItemsActivity.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
            }
        });
    }

    private void AtivarSpinner() {
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
        produtosLista.setNomeLista("Segunda Lista");
        produtosLista.setDescricao(descricao.getText().toString());
        produtosLista.setCategoria(spinnerCategories.getSelectedItem().toString());
        produtosLista.setObservacao(observacao.getText().toString());
        produtosLista.setQuantidade(quantidade.getText().toString());
        produtosLista.setStatus("N");
        produtosLista.salvar();
    }
}