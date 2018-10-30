package br.com.whatsappandroid.cursoandroid.whatsapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

import java.util.HashMap;
import java.util.Random;

import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Permissao;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;

public class LoginActivity extends AppCompatActivity {
/*
    private EditText nomeUsuario;
    private EditText telefone;
    private EditText codPais;
    private Button btnCadastrar;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.validaPermissoes(1,this, permissoesNecessarias);

        nomeUsuario     = (EditText) findViewById(R.id.edit_nome);
        telefone        = (EditText) findViewById(R.id.edit_telefone);
        codPais         = (EditText) findViewById(R.id.edit_cod_pais);
        btnCadastrar    = (Button) findViewById(R.id.bt_cadastrar);

        //Máscaras de número de telefone
        SimpleMaskFormatter simpleMaskTelefone =  new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        SimpleMaskFormatter simpleMaskPais = new SimpleMaskFormatter("NN");

        MaskTextWatcher maskTelefone = new SimpleMaskTextWatcher(telefone, simpleMaskTelefone);
        MaskTextWatcher maskCodPais = new SimpleMaskTextWatcher(codPais, simpleMaskPais);

        codPais.addTextChangedListener(maskCodPais);
        telefone.addTextChangedListener(maskTelefone);


        //Geracao de token
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomeDoUsuario = nomeUsuario.getText().toString();
                String telefoneCompleto = codPais.getText().toString() + telefone.getText().toString();
                String telefoneSemFormatacao = telefoneCompleto.replace("+","");
                telefoneSemFormatacao = telefoneSemFormatacao.replace("-","");
                telefoneSemFormatacao = telefoneSemFormatacao.replace("(","");
                telefoneSemFormatacao = telefoneSemFormatacao.replace(") ","");


                Random randomico = new Random();
                int numeroRandomico = randomico.nextInt(9999-1000) + 1000; // Gera um número randon entre 1000 e 9999

                String token = String.valueOf(numeroRandomico);
                String mensagemEnvio = "WhatsApp código de validação: "+ token;

                Preferencias preferencias = new Preferencias(LoginActivity.this);
                preferencias.SalvarUsuarioPreferencias(nomeDoUsuario, telefoneSemFormatacao, token);

                boolean enviadoSMS = enviaSMS("+"+telefoneSemFormatacao,mensagemEnvio);

                //Manda o caboco pra activity de validacao
                if(enviadoSMS){
                    Intent intent = new Intent(LoginActivity.this, ValidadorActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,"Problema ao enviar o SMS tente novamente",Toast.LENGTH_LONG).show();
                }

               /* HashMap<String, String> usuario = preferencias.getDadosUsuario();
                Log.i("Token", "token:" + usuario.get("token"));
                */
/*
            }
        });


    }
    private boolean enviaSMS(String telefone, String mensagem){

        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone,null, mensagem, null, null);
            return true;
        }catch (Exception e ){
            e.printStackTrace();;
            return false;
        }
    }
    public void  onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult){
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        for(int resultado : grantResult){

            if(resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }

        }

    }
    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para usar este APP é necessário aceitar as permissões");
        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }*/
}
