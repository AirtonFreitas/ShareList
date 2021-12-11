package com.iasoftwares.sharelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.model.Usuario;

public class LoginActivity extends AppCompatActivity {
    private EditText campoSenha, campoEmail;
    private Button btnLogin, btnNewAccount;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        campoEmail = findViewById(R.id.edtLoginEmailID);
        campoSenha = findViewById(R.id.edtLoginSenhaID);
        btnLogin = findViewById(R.id.btnLoginEntrarID);
        btnNewAccount = findViewById(R.id.btntLoginCadastroID);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();
                if (!textoEmail.isEmpty()) {
                    if (!textoSenha.isEmpty()) {
                        usuario = new Usuario();
                        usuario.setEmail(textoEmail);
                        usuario.setSenha(textoSenha);
                        validarLogin();
                    } else {
                        campoSenha.setError("Campo vazio!");
                        campoSenha.requestFocus();
                    }
                } else {
                    campoEmail.setError("Campo vazio!");
                    campoEmail.requestFocus();
                }
            }
        });
        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, NewAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void validarLogin(){
        autenticacao = SettingsFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaprincipal();
                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Senha incorreta.";
                        campoSenha.setError(excecao);
                        campoSenha.requestFocus();
                    } catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Usuário não existe";
                        campoEmail.setError(excecao);
                        campoEmail.requestFocus();
                    } catch (Exception e) {
                        excecao = "Tivemos um erro no seu Login. Detalhamento: " + e.getMessage();
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

}
public void abrirTelaprincipal(){
    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
}
}