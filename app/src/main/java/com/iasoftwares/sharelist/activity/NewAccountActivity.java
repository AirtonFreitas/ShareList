package com.iasoftwares.sharelist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.helper.Base64Custom;
import com.iasoftwares.sharelist.model.Usuario;

public class NewAccountActivity extends AppCompatActivity {
    private EditText campoNome, campoEmail, campoSenha, campoConfirmaSenha;
    private Button botaoCadastrar, botaoLogin;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private TextView textLogin;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        final boolean[] VISIBLE_PASSWORD = {false};


        campoNome = findViewById(R.id.edtNewAccountNomeID);
        campoEmail = findViewById(R.id.edtNewAccountEmailID);
        campoSenha = findViewById(R.id.edtNewAccountSenhaID);
        campoConfirmaSenha = findViewById(R.id.edtNewAccountSenhaBID);
        botaoCadastrar = findViewById(R.id.btnNewAccountEntrarID);
        textLogin = findViewById(R.id.textLoginID);

        textLogin.setOnClickListener(new View.OnClickListener() {
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

        campoSenha.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;


                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (campoSenha.getRight() - campoSenha.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (VISIBLE_PASSWORD[0]) {
                            VISIBLE_PASSWORD[0] = false;
                            campoSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            campoSenha.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_baseline_lock_gray, 0, R.drawable.ic_baseline_visibility_off, 0);
                        } else {
                            VISIBLE_PASSWORD[0] = true;
                            campoSenha.setInputType(InputType.TYPE_CLASS_TEXT);
                            campoSenha.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_baseline_lock_gray, 0, R.drawable.ic_baseline_visibility, 0);
                        }
                        return false;
                    }
                }
                return false;
            }
        });

        campoConfirmaSenha.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;


                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (campoConfirmaSenha.getRight() - campoConfirmaSenha.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (VISIBLE_PASSWORD[0]) {
                            VISIBLE_PASSWORD[0] = false;
                            campoConfirmaSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            campoConfirmaSenha.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_baseline_lock_gray, 0, R.drawable.ic_baseline_visibility_off, 0);
                        } else {
                            VISIBLE_PASSWORD[0] = true;
                            campoConfirmaSenha.setInputType(InputType.TYPE_CLASS_TEXT);
                            campoConfirmaSenha.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_baseline_lock_gray, 0, R.drawable.ic_baseline_visibility, 0);
                        }
                        return false;
                    }
                }
                return false;
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
                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();
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