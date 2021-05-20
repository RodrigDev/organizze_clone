package com.example.organizze.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.configuracaoFirebase;
import com.example.organizze.helper.DateCustom;
import com.example.organizze.helper.base64Custom;
import com.example.organizze.model.Movimentacao;
import com.example.organizze.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitaActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = configuracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = configuracaoFirebase.getFirebaseAutenticacao();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoDescricao = findViewById(R.id.editDescricao);
        campoCategoria = findViewById(R.id.editCategoria);

        //Preenche o campo data com a data atual
        campoData.setText(DateCustom.dataAtual());
       recuperarReceitaTotal();

    }

    public void salvarReceita(View view) {

        if (validarCamposReceita()) {
            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("r");

            Double receitaAtualizada = receitaTotal + valorRecuperado;
            atualizarReceita( receitaAtualizada );

            movimentacao.salvar(data);
            finish();
        }
    }

    public Boolean validarCamposReceita() {

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if (!textoValor.isEmpty()) {
            if (!textoData.isEmpty()) {
                if (!textoCategoria.isEmpty()) {
                    if (!textoDescricao.isEmpty()) {
                        return true;
                    } else {
                        Toast.makeText(ReceitaActivity.this,
                                "Descrição não foi preenchida!",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                } else {
                    Toast.makeText(ReceitaActivity.this,
                            "Categoria não foi preenchido!",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(ReceitaActivity.this,
                        "Data não foi preenchida!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }

        } else {
            Toast.makeText(ReceitaActivity.this,
                    "Valor não foi preenchido!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void recuperarReceitaTotal(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = base64Custom.codificarBase64( emailUsuario );
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child( idUsuario );

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario =  snapshot.getValue( Usuario.class );
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarReceita(Double receita){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = base64Custom.codificarBase64( emailUsuario );
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child( idUsuario );

        usuarioRef.child("receitaTotal").setValue(receita);

    }

}