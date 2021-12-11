package com.iasoftwares.sharelist.config;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsFirebase {
    private static FirebaseAuth autenticacao;

    public static FirebaseAuth getFirebaseAutenticacao() {
        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

}
