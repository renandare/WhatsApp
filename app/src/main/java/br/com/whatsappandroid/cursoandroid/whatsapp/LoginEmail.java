package br.com.whatsappandroid.cursoandroid.whatsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;

public class LoginEmail extends AppCompatActivity {

    private DatabaseReference referenciaFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
//        referenciaFirebase = ConfiguracaoFirebase.getFirebase();

    }
    public void abrirCadastroUsuario(View view){

        Intent intent = new Intent(LoginEmail.this, CadastroUsuarioActivity.class);
        startActivity(intent);

    }


}
