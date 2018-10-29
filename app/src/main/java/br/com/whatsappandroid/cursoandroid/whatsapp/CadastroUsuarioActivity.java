package br.com.whatsappandroid.cursoandroid.whatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;

import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    //Atributos dos campos
    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button botaoCadastrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        //Recuperar os componentes
        nome = (EditText) findViewById(R.id.edit_cadastro_nome);
        email = (EditText) findViewById(R.id.edit_cadastro_email);
        senha = (EditText) findViewById(R.id.edit_cadastro_senha);
        botaoCadastrar = (Button) findViewById(R.id.bt_cadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //instancia a classe usuario

                usuario = new Usuario();
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());

                cadastrarUsuario();
            }
        });
    }

    private void cadastrarUsuario(){
        //objeto que faz a autenticacao com o firebase
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //testa se deu certo o cadastro
                if(task.isSuccessful()){

                    String identificadorUsuario = Base64Custom.CodificarBase64(usuario.getEmail());
                    abrirLoginUsuario();


                    //Intent intent = new Intent(CadastroUsuarioActivity.this, MainActivity.class);
                    //startActivity(intent);

                }else{
                    String erroExcecao = "";

                    try{
                        throw  task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, que contenha caracteres e letras.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail.";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Este e-mail já está cadastrado.";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao cadastrar o usuário";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuarioActivity.this, erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginEmail.class);
        startActivity(intent);
        finish();
    }
}
