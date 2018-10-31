package br.com.whatsappandroid.cursoandroid.whatsapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.whatsappandroid.cursoandroid.whatsapp.adapter.MensagemAdapter;
import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Conversa;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editMensagem;
    private ImageButton btMensagem;
    private DatabaseReference firebase;
    private ListView listview;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ValueEventListener valueEventListenerMensagem;

    //Dados do destinatário da mensagem
    private String nomeUsuarioDestinario;
    private String idUsuarioDestinatario;

    //Dados do remetente da mensagem
    private String idUsuarioRemetente;
    private String nomeUsuarioRemetente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = (Toolbar) findViewById(R.id.tb_conversa);
        editMensagem = (EditText) findViewById(R.id.edit_mensagem);
        btMensagem = (ImageButton) findViewById(R.id.bt_enviar);
        listview = (ListView) findViewById(R.id.lv_conversas);

        //Dados do usuario logado
        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioRemetente = preferencias.getIdentificador();
        nomeUsuarioRemetente = preferencias.getNome();


        //Bundle: Utilizado para passar dados entre activities
        Bundle extra = getIntent().getExtras();

        if(extra != null){
            nomeUsuarioDestinario = extra.getString("nome");
            String emailDestinatario = extra.getString("email");
            idUsuarioDestinatario = Base64Custom.CodificarBase64(emailDestinatario);
        }

        //Configura a toolbar
        toolbar.setTitle(nomeUsuarioDestinario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Monta listView e Adapter
        mensagens = new ArrayList<>();
        adapter = new MensagemAdapter(ConversaActivity.this, mensagens);
        listview.setAdapter(adapter);

        //Recuperar as mensagens que estao no firebase (anteriores)
        firebase = ConfiguracaoFirebase.getFirebase()
        .child("Mensagens")
        .child(idUsuarioRemetente)
        .child(idUsuarioDestinatario);

        //Cria listner para mensagem
        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Limpa as mensagens para não exibir duplicado
                mensagens.clear();

                //busca as mensagens desse nó
                for(DataSnapshot dados : dataSnapshot.getChildren() ){

                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        //Adiciona o listner ao firebase
        firebase.addValueEventListener(valueEventListenerMensagem);


        //Enviar mensagem
        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoMensagem = editMensagem.getText().toString();

                if(textoMensagem.isEmpty()){
                    Toast.makeText(ConversaActivity.this,"Digite uma mensagem",Toast.LENGTH_SHORT).show();
                }else{

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRemetente);
                    mensagem.setMensagem(textoMensagem);

                    //Salva a mensagem para o remetente
                   Boolean retornoMensagemRemetente =  SalvarMensagem(idUsuarioRemetente, idUsuarioDestinatario,mensagem );


                   if(!retornoMensagemRemetente){
                       Toast.makeText(ConversaActivity.this, "Não foi possível enviar a mensagem", Toast.LENGTH_SHORT).show();
                   }else{

                       //Salva a mensagem para o destinatario
                       Boolean retornoMensagemDestinatario = SalvarMensagem(idUsuarioDestinatario, idUsuarioRemetente,mensagem );
                       if(!retornoMensagemDestinatario){
                           Toast.makeText(ConversaActivity.this, "Não foi possível enviar a mensagem ao destinatário", Toast.LENGTH_SHORT).show();
                       }
                   }

                   //Cria objeto do tipo Conversa
                   Conversa conversa = new Conversa();
                   conversa.setIdUsuario(idUsuarioDestinatario);
                   conversa.setNome(nomeUsuarioDestinario);
                   conversa.setMensagem( textoMensagem );

                   //Salva a conversa para o remetente
                   Boolean retornoConversaRemetente = SalvarConversa(idUsuarioRemetente, idUsuarioDestinatario, conversa );

                   if(!retornoConversaRemetente){
                       Toast.makeText(ConversaActivity.this, "Não foi salvar a conversa", Toast.LENGTH_SHORT).show();
                   }else{

                       //Salva a conversa para o destinatario
                       //Conversa conversa2 = new Conversa();
                       conversa.setIdUsuario(idUsuarioRemetente);
                       conversa.setNome(nomeUsuarioRemetente);
                       conversa.setMensagem(textoMensagem);


                       Boolean retornoConversaDestinatario = SalvarConversa(idUsuarioDestinatario, idUsuarioRemetente, conversa );

                       if(!retornoConversaDestinatario){
                           Toast.makeText(ConversaActivity.this, "Não foi salvar a conversa para o destinatário", Toast.LENGTH_SHORT).show();

                       }
                   }


                    editMensagem.setText("");
                }
            }
        });

    }

    private Boolean SalvarConversa(String idRemetente, String idDestinatario, Conversa conversa){
        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("Conversas");
                    firebase.child(idRemetente)
                    .child(idDestinatario)
                    .setValue(conversa);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }
    private boolean SalvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){

        try{
            //Pega a instancia do firebase
            firebase = ConfiguracaoFirebase.getFirebase().child("Mensagens");
            firebase.child(idRemetente)
                    .child(idDestinatario)
                    .push()
                    .setValue(mensagem);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    //Quando a activity for fechada nao precisa mais do listener entao cancela ele pra evitar consumo
    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagem);
    }
}
