package br.com.whatsappandroid.cursoandroid.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfiguracaoFirebase {

    //Atributo statico é o mesmo independente das instancias que crie dessa classe
    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth firebaseAuth;

    //Recupera a instancia do firebase
    //Tipo statico nao necessita instanciar colocando "new..."
    public static DatabaseReference getFirebase(){

        if(referenciaFirebase == null) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return referenciaFirebase;
    }

    //Retorna a instancia da Auth do Firebase.
    public static FirebaseAuth getFirebaseAuth(){
        if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }
}
