package br.com.bluzone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import br.com.bluzone.databinding.ActivityTelaCapturaBinding
import com.google.android.material.snackbar.Snackbar

class TelaCaptura : AppCompatActivity() {

    private lateinit var binding: ActivityTelaCapturaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inflar a activity
        binding = ActivityTelaCapturaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAbrirCamera.setOnClickListener{

            //solicitar permissao
            cameraProviderResult.launch(android.Manifest.permission.CAMERA)

        }

    }

    private val cameraProviderResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                abrirTelaPreview()
            }else{
                Snackbar.make(binding.root,"Permissao de camera negada",Snackbar.LENGTH_INDEFINITE).show()
            }
        }

    private fun abrirTelaPreview(){
        //navegar pra activity de foto
        val intentCameraPreview = Intent(this, CameraPreviewActivity::class.java)
        startActivity(intentCameraPreview)

    }
}