package com.iasoftwares.sharelist.model;

import android.app.BackgroundServiceStartNotAllowedException;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.helper.Base64Custom;

public class ProdutosLista {
    private String nomeLista;
    private String descricao;
    private String categoria;
    private String observacao;

    public ProdutosLista() {

    }
    public void salvar(){
        FirebaseAuth autentic = SettingsFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(autentic.getCurrentUser().getEmail());
        DatabaseReference firebase = SettingsFirebase.getFirebaseDatabase();

        firebase.child("Listas")
                .child(idUsuario)
                .child(nomeLista)
                .push().setValue(this);
    }
    @Exclude
    public String getNomeLista() {
        return nomeLista;
    }

    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }



}
