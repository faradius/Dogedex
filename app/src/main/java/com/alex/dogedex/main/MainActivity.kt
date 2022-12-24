package com.alex.dogedex.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.alex.dogedex.R
import com.alex.dogedex.api.ApiResponseStatus
import com.alex.dogedex.api.ApiServiceInterceptor
import com.alex.dogedex.auth.LoginActivity
import com.alex.dogedex.databinding.ActivityMainBinding
import com.alex.dogedex.dogdetail.DogDetailActivity
import com.alex.dogedex.dogdetail.DogDetailActivity.Companion.DOG_KEY
import com.alex.dogedex.doglist.DogListActivity
import com.alex.dogedex.machinelearning.Classifier
import com.alex.dogedex.model.Dog
import com.alex.dogedex.model.User
import com.alex.dogedex.settings.SettingsActivity
import com.alex.dogedex.utils.LABEL_PATH
import com.alex.dogedex.utils.MODEL_PATH
import org.tensorflow.lite.support.common.FileUtil
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private var isCameraReady = false
    private lateinit var classifier: Classifier
    private val viewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                //Open Camera
                setupCamera()
            } else {
                Toast.makeText(
                    this,
                    "You need to accept camera permission to use camera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = User.getLoggedInUser(this)
        if (user == null) {
            openLoginActivity()
            //Para que no haga mas de lo que haya abajo de esto
            return
        } else {
            ApiServiceInterceptor.setSessionToken(user.authenticationToken)
        }

        binding.settingsFab.setOnClickListener {
            openSettingsActivity()
        }

        binding.dogListFab.setOnClickListener {
            openDogListActivity()
        }

        binding.takePhotoFab.setOnClickListener {
            if (isCameraReady){
                takePhoto()
            }
        }

        viewModel.status.observe(this){ status->

            when(status){
                is ApiResponseStatus.Error -> {
                    binding.loadingWheel.visibility = View.GONE
                    Toast.makeText(this@MainActivity, status.messageId, Toast.LENGTH_SHORT).show()
                }
                is ApiResponseStatus.Loading -> binding.loadingWheel.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> binding.loadingWheel.visibility = View.GONE
                else -> {Toast.makeText(this@MainActivity, R.string.unknown_error, Toast.LENGTH_SHORT).show()}
            }
        }

        viewModel.dog.observe(this){ dog->
            if (dog != null){
                openDogDetailActivity(dog)
            }
        }

        requestCameraPermission()
    }

    private fun openDogDetailActivity(dog: Dog) {
        val intent = Intent(this, DogDetailActivity::class.java)
        intent.putExtra(DOG_KEY, dog)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        classifier = Classifier(
            FileUtil.loadMappedFile(this@MainActivity, MODEL_PATH),
            FileUtil.loadLabels(this@MainActivity, LABEL_PATH)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraExecutor.isInitialized){
            cameraExecutor.shutdown()
        }

    }

    private fun setupCamera() {
        //Con el post hace que se espere a que se ejecute la camara para poder
        //proceder con el bloque de codigo que esta en su interior
        binding.cameraPreview.post {
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.cameraPreview.display.rotation)
                .build()
            cameraExecutor = Executors.newSingleThreadExecutor()
            startCamera()
            isCameraReady = true
        }
    }

    private fun requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    setupCamera()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    AlertDialog.Builder(this)
                        .setTitle("Accept please")
                        .setMessage("It necesary these permissions, accept please")
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            requestPermissionLauncher.launch(
                                Manifest.permission.CAMERA
                            )
                        }
                        .setNegativeButton(android.R.string.cancel) { _, _ ->

                        }.show()
                }
                else -> {
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            }
        } else {
            setupCamera()
        }
    }

    private fun takePhoto() {
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(getOutputPhotoFile()).build()
        imageCapture.takePicture(outputFileOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    Toast.makeText(this@MainActivity, "Error taking photo ${error.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val photoUri = outputFileResults.savedUri

                    val bitmap = BitmapFactory.decodeFile(photoUri?.path)
                    val dogRecognition = classifier.recognizeImage(bitmap).first()
                    viewModel.getDogByMlId(dogRecognition.id)
                }
            })
    }

    private fun getOutputPhotoFile(): File {
        //mediaDir tiene un arreglo de directorios y toma el primero que no sea null
        val mediaDir = externalMediaDirs.firstOrNull()?.let{
            //Crea ese directorio como padre y como hijo el nombre de la aplicación, para que tenga
            //un nombre correcto ese archivo se le agrega la extenxión en .jpg
            File(it, resources.getString(R.string.app_name) + ".jpg").apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()){
            mediaDir
        }else{
            filesDir
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                imageProxy.close()
            }

            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis)


        }, ContextCompat.getMainExecutor(this))
    }

    private fun openDogListActivity() {
        startActivity(Intent(this, DogListActivity::class.java))
    }

    private fun openSettingsActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun openLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}