package an.xuzaixiang.camera.demo

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.xuzaixiang.camera.demo.R

class MainActivity : AppCompatActivity() {
    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context,MainActivity::class.java))
        }
    }

    private val permission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            val granted = it.all { it.value }
            if (granted) {
                setContentView(R.layout.activity_main)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permission.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO))
    }
}