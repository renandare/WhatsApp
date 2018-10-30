package br.com.whatsappandroid.cursoandroid.whatsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.whatsappandroid.cursoandroid.whatsapp.adapter.TabAdapter;
import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.SlidingTabLayout;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Contato;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private Button botaoSair;
    private FirebaseAuth autenticacao;
    private Toolbar toolbar;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String identificadorContato;
    private DatabaseReference referenciaFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("NoivasApp");
        setSupportActionBar(toolbar); //Metodo de suporte para evitar erro com toolbar

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //configurar o sliding tabs pra ficar bonito
        slidingTabLayout.setDistributeEvenly(true); //Distribui a tab pela largura do layout
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent)); //altera a cor do item selecionado

        //configurar o adpater
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    //evento que pega ql item do menu de ... foi clicado
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            case R.id.item_adicionar:
                abrirCadastroContato();
            case R.id.item_configuracoes:
                return true;
            default:
                return super.onOptionsItemSelected(item);
            }
    }

    private void abrirCadastroContato(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("E-mail do usuário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String emailContato = editText.getText().toString();
                if(emailContato.isEmpty()){
                    Toast.makeText(MainActivity.this, "Preencha o e-mail do usuário.", Toast.LENGTH_SHORT).show();
                }else{
                    //verifica se o usuario esta cadastrado
                    identificadorContato = Base64Custom.CodificarBase64(emailContato);
                    Log.i("identificadorContato", "identificadorContato: "+identificadorContato);

                    //Recupera a instancia do firebase
                    referenciaFirebase = ConfiguracaoFirebase.getFirebase().child("Usuarios").child(identificadorContato);
                    referenciaFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() != null){



                                    //Recuperar dados do contato a ser adicionado
                                    Usuario usuarioContato = dataSnapshot.getValue(Usuario.class);

                                    //Pega o identificador do usuario logado
                                    Preferencias preferencias = new Preferencias(MainActivity.this);
                                    String identificadorUsuarioLogado = preferencias.getIdentificador();

                                    //Pega uma nova instancia do firebase
                                    referenciaFirebase = ConfiguracaoFirebase.getFirebase();
                                    referenciaFirebase = referenciaFirebase.child("Contatos")
                                                                            .child(identificadorUsuarioLogado)
                                                                            .child(identificadorContato);

                                    Contato contato = new Contato();
                                    contato.setIdentificadorUsuario( identificadorContato );
                                    contato.setEmail( usuarioContato.getEmail() );
                                    contato.setNome( usuarioContato.getNome() );

                                    referenciaFirebase.setValue( contato );

                            }else{
                                Toast.makeText(MainActivity.this, "E-mail nao cadastrado.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }); //consulta apenas 1 vez igual query

                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();

    }

    private void deslogarUsuario(){
        autenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginEmail.class);
        startActivity(intent);
        finish();
    }
}
