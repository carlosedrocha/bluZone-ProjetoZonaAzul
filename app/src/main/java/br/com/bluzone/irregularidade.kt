package br.com.bluzone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class irregularidade : AppCompatActivity() {
    private lateinit var backBtn: AppCompatButton
    private lateinit var homeBtn: AppCompatButton
    private lateinit var enviarBtn: AppCompatButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_irregularidade)

        //botão para voltar para a home
        homeBtn = findViewById(R.id.homeBtn)

        //Botão para voltar a tela
        backBtn = findViewById(R.id.backBtn)

        //Botão para enviar a irregularide
        enviarBtn = findViewById(R.id.enviarBtn)
    }

    //Função para Retornar a home
    private fun IrParaHome() {
        val telaHome = Intent(this, MainActivity::class.java)
        startActivity(telaHome)
    }
}