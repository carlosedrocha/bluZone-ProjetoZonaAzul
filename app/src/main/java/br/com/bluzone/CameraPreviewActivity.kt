package br.com.bluzone

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import br.com.bluzone.databinding.ActivityCameraPreviewBinding
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.lang.Exception
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraPreviewBinding

    //processamento de imagem - Controle do driver da camera
    private lateinit var cameraProviderFeature: ListenableFuture<ProcessCameraProvider>

    //Selecionar a camera desejada
    private lateinit var cameraSelector: CameraSelector

    //imagem capturada
    private var imageCapture: ImageCapture? = null

    //executor de thread separada
    private lateinit var imgCaptureExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_preview)

        binding = ActivityCameraPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraProviderFeature = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        imgCaptureExecutor = Executors.newSingleThreadExecutor()

        //Metodo start camera
        startCamera()

        //evento do clique para chamar o metodo de tirar foto
        binding.btnTirarFoto.setOnClickListener {
            takePhoto()
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                blinkPreview()
            }
        }
    }

    private fun startCamera() {
        cameraProviderFeature.addListener({

            imageCapture = ImageCapture.Builder().build()

            val cameraProvider = cameraProviderFeature.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            }
            try {
                //abrir o preview
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            } catch (e: Exception) {
                Log.e("CameraPreview", "Falha ao abrir a camera")
            }


        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        //codigo para tirar a foto
        imageCapture?.let {

            //nome do arquivo
            val filename = "FOTO_JPEG_${System.currentTimeMillis()}"
            val file = File(externalMediaDirs[0], filename)

            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        Log.i("CameraPreview", "A imagem foi salva no diretorio: ${file.toURI()}")
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(binding.root.context,
                            "Erro ao salvar a foto",
                            Toast.LENGTH_LONG).show()
                        Log.e("CameraPreview", "Excecao ao gravar arquivo da foto: ${exception}")
                    }
                }
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun blinkPreview(){
        binding.root.postDelayed({
            binding.root.foreground = ColorDrawable(Color.WHITE)
            binding.root.postDelayed({
                binding.root.foreground = null
            }, 50)
        }, 100)
    }

}