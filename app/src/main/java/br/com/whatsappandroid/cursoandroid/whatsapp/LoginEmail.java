package br.com.whatsappandroid.cursoandroid.whatsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Usuario;

public class LoginEmail extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        verificaUsuarioLogado();

        email = (EditText) findViewById(R.id.edit_login_email);
        senha = (EditText) findViewById(R.id.edit_login_senha);
        botaoLogar = (Button) findViewById(R.id.bt_logar);

       botaoLogar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               usuario = new Usuario();
               usuario.setEmail(email.getText().toString());
               usuario.setSenha(senha.getText().toString());
               validarLogin();
           }
       });
    }

    private void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Preferencias preferencias = new Preferencias(LoginEmail.this);
                    //Pega o e-mail do cara que fez o login
                    String identificadorUsuarioLogado = Base64Custom.CodificarBase64(usuario.getEmail());
                    preferencias.salvarDados(identificadorUsuarioLogado);

                    abrirTelaPrincipal();
                    Toast.makeText(LoginEmail.this, "Sucesso ao fazer login", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(LoginEmail.this, "erro ao fazer login", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    //Verifica usuario logado
    private void verificaUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }

    }

    //abre a tela inicial do app
    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginEmail.this, MainActivity.class);
        startActivity(intent);
    }

    public void abrirCadastroUsuario(View view){

        Intent intent = new Intent(LoginEmail.this, CadastroUsuarioActivity.class);
        startActivity(intent);
        finish();

    }


}
