package com.iasoftwares.sharelist.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.iasoftwares.sharelist.config.SettingsFirebase;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String idUsuario;

    public Usuario() {

    }

    public String getNome() {
        return nome;
    }


    public void salvar(){
        DatabaseReference firebase = SettingsFirebase.getFirebaseDatabase();
         firebase.child("usuarios").child(this.idUsuario).setValue(this);
    }
@Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

@Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }



}
