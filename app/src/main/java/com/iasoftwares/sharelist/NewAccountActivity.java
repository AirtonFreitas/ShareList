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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.model.Usuario;

public class NewAccountActivity extends AppCompatActivity {
    private EditText campoNome, campoEmail, campoSenha, campoConfirmaSenha;
    private Button botaoCadastrar, botaoLogin;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        campoNome = findViewById(R.id.edtNewAccountNomeID);
        campoEmail = findViewById(R.id.edtNewAccountEmailID);
        campoSenha = findViewById(R.id.edtNewAccountSenhaID);
        campoConfirmaSenha = findViewById(R.id.edtNewAccountSenhaBID);
        botaoCadastrar = findViewById(R.id.btnNewAccountEntrarID);
        botaoLogin = findViewById(R.id.btntLoginCadastroID);

        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewAccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textNome = campoNome.getText().toString();
                String textEmail = campoEmail.getText().toString();
                String textSenha = campoSenha.getText().toString();
                String textConfirmaSenha = campoConfirmaSenha.getText().toString();

                if (!textNome.isEmpty()) {
                    if (!textEmail.isEmpty()) {
                        if (textSenha.equals(textConfirmaSenha)) {
                            usuario = new Usuario();
                            usuario.setNome(textNome);
                            usuario.setEmail(textEmail);
                            usuario.setSenha(textSenha);
                            createNewAccount();
                        } else {
                            campoSenha.setError("As senhas não conferem.");
                            campoSenha.requestFocus();
                            campoConfirmaSenha.setError("As senhas não conferem.");
                        }
                    } else {
                        campoEmail.setError("Campo vazio!");
                        campoEmail.requestFocus();
                    }
                } else {
                    campoNome.setError("Campo vazio!");
                    campoNome.requestFocus();
                }
            }
        });

    }

    private void createNewAccount() {
        autenticacao = SettingsFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String execao
                    abrirTelaprincipal();
                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma senha mais forte.";
                        campoSenha.setError(excecao);
                        campoSenha.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Email inválido";
                        campoEmail.setError(excecao);
                        campoEmail.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Você já possui cadastro.";
                        campoEmail.setError(excecao);
                        campoEmail.requestFocus();
                        botaoLogin.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        excecao = "Tivemos um erro no seu cadastro. Detalhamento: " + e.getMessage();
                        e.printStackTrace();
                        Toast.makeText(NewAccountActivity.this, excecao, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void abrirTelaprincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}