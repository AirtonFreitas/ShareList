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
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.model.Usuario;

public class LoginActivity extends AppCompatActivity {
    private EditText campoSenha, campoEmail;
    private Button btnLogin, btnNewAccount;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private TextView newAccount;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final boolean[] VISIBLE_PASSWORD = {false};
        campoEmail = findViewById(R.id.edtLoginEmailID);
        btnLogin = findViewById(R.id.btnLoginEntrarID);
        campoSenha = findViewById(R.id.edtLoginSenhaID);
        newAccount = findViewById(R.id.newAccountID);

        campoSenha.setOnTouchListener(new View.OnTouchListener() {
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
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, NewAccountActivity.class);
                startActivity(intent);
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
                    }catch (FirebaseTooManyRequestsException e){
                        excecao = "Devido ao número frequente de tentativas de Login, sua conexão foi bloqueada temporariamente.\nTente novamente mais tarde.";
                    Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_LONG).show();
                    }catch (Exception e) {
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