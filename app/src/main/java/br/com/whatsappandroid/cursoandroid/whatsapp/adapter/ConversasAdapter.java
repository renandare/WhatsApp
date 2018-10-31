package br.com.whatsappandroid.cursoandroid.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Contato;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Conversa;

public class ConversasAdapter extends ArrayAdapter<Conversa> {

    private ArrayList<Conversa> conversas;
    private Context context;

    public ConversasAdapter(Context c, ArrayList<Conversa> objects) {
        super(c, 0, objects);
        this.conversas = objects;
        this.context = c;
    }

    //Método que é chamado para cada item da lista.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View view = null;

        //verifica se a lista de contatos esta vazia
        if(conversas != null){

            //Iniciar o objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //monta a view a partir do XML
            view = inflater.inflate(R.layout.lista_conversa, parent, false);

            //Recupera o elemento para exibicao
            TextView nome = (TextView) view.findViewById(R.id.tv_titulo);
            TextView ultimaConversa = (TextView) view.findViewById(R.id.tv_subtitulo);

            Conversa conversa = conversas.get(position);
            nome.setText(conversa.getNome());
            ultimaConversa.setText(conversa.getMensagem());
        }
        return view;
    }
}
