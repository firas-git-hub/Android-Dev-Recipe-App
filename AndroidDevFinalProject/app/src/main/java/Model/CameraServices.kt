package Model

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

object CameraServices {
    fun checkPermission(activity: AppCompatActivity): Boolean{
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(activity: AppCompatActivity, requestCode: Int){
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            requestCode
        )
    }

    fun loadCamera(activity: AppCompatActivity, requestCode: Int){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        ActivityCompat.startActivityForResult(activity, cameraIntent, requestCode, null)
    }
}