package br.com.whatsappandroid.cursoandroid.whatsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;

public class ValidadorActivity extends AppCompatActivity {

    private EditText codValidacao;
    private Button validar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        codValidacao = (EditText) findViewById(R.id.edit_cod_validacao);
        validar = (Button) findViewById(R.id.bt_validar);

        SimpleMaskFormatter simpleMaskValidacao = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher mascaraCodigoValidacao = new MaskTextWatcher(codValidacao, simpleMaskValidacao);

        codValidacao.addTextChangedListener(mascaraCodigoValidacao);

        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recuperar dados do usuario
                Preferencias preferencias = new Preferencias(ValidadorActivity.this);
                HashMap<String,String> usuario = preferencias.getDadosUsuario();

                //recupera o token
                String TokenGerado = usuario.get("token");
                String tokenDigitado = codValidacao.getText().toString();

                //valida o token
                if( tokenDigitado.equals(TokenGerado)){
                    Toast.makeText(ValidadorActivity.this, "Token valiudado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ValidadorActivity.this, "Token inválido", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
