package br.com.whatsappandroid.cursoandroid.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferencias {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context contexto;
    private final String NOME_ARQUIVO = "whatsapp.preferencias";
   // private final String CHAVE_NOME = "nome";
   private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private final String CHAVE_NOME = "nomeUsuarioLogado";
   // private final String CHAVE_TELEFONE = "telefone";
    //private final String CHAVE_TOKEN = "token";
    private final int MODE = 0;


    public Preferencias(Context contexto_parametro){

        contexto = contexto_parametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE );
        editor = preferences.edit(); // Editor é uma interface pra conseguir alterar as preferencias
    }

    //public void SalvarUsuarioPreferencias(String nome, String telefone, String token){
    public void salvarDados(String identificadorUsuario, String nomeUsuario){

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_NOME, nomeUsuario);
        //editor.putString(CHAVE_NOME,nome);
        //editor.putString(CHAVE_TELEFONE,telefone);
        //editor.putString(CHAVE_TOKEN,token);
        editor.commit();
    }

    //Permite criar uma lista com chave e valor do tipo string
   /* public HashMap<String, String> getDadosUsuario(){

        HashMap<String, String> dadosUsuario = new HashMap<>();
        dadosUsuario.put(CHAVE_NOME, preferences.getString(CHAVE_NOME, null));
        dadosUsuario.put(CHAVE_TELEFONE, preferences.getString(CHAVE_TELEFONE, null));
        dadosUsuario.put(CHAVE_TOKEN, preferences.getString(CHAVE_TOKEN, null));
        return dadosUsuario;

    }*/

   public String getIdentificador(){

       return preferences.getString(CHAVE_IDENTIFICADOR, null);
   }

    public String getNome(){

        return preferences.getString(CHAVE_NOME, null);
    }

}
