package glory.hearing.tool.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            },
            3000,
        )

//        ADSinit(this, getCurrentVersionCode(), object : getDataListner {
//            override fun onSuccess() {
//                CoroutineScope(Dispatchers.IO).launch {
//                        startActivity(
//                            Intent(
//                                this@SplashActivity,
//                                MainActivity::class.java
//                            )
//                        )
//                        finish()
//
//
//                }
//            }
//
//            override fun onUpdate(url: String?) {
//                Log.e("my_log", "onUpdate: $url")
//                showUpdateDialog(url)
//            }
//
//            override fun onRedirect(url: String?) {
//                Log.d("my_log", "onRedirect: $url")
//                showRedirectDialog(url)
//            }
//
//            override fun onReload() {
//                startActivity(
//                    Intent(
//                        this@SplashActivity,
//                       SplashActivity::class.java
//                    )
//                )
//                finish()
//            }
//
//            override fun onGetExtradata(extraData: JSONObject?) {
//                Log.d("my_log", "onsetExtradite: " + extraData.toString())
//            }
//        })

    }

//    fun showRedirectDialog(url: String?) {
//        val dialog = Dialog(this)
//        dialog.setCancelable(false)
//        val view: View =
//            layoutInflater.inflate(com.pesonal.adsdk.R.layout.installnewappdialog, null)
//        dialog.setContentView(view)
//        val update = view.findViewById<TextView>(com.pesonal.adsdk.R.id.update)
//        val title = view.findViewById<TextView>(com.pesonal.adsdk.R.id.txt_title)
//        val decription = view.findViewById<TextView>(com.pesonal.adsdk.R.id.txt_decription)
//        update.text = "Install Now"
//        title.text = "Install our new app now and enjoy"
//        decription.text =
//            "We have transferred our server, so install our new app by clicking the button below to enjoy the new features of app."
//        update.setOnClickListener {
//            try {
//                val marketUri = Uri.parse(url)
//                val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
//                startActivity(marketIntent)
//            } catch (ignored1: ActivityNotFoundException) {
//            }
//        }
//        dialog.create()
//        dialog.show()
//        val window = dialog.window
//        window!!.setLayout(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//    }
//
//    @SuppressLint("InflateParams", "SetTextI18n")
//    fun showUpdateDialog(url: String?) {
//        val dialog = Dialog(this)
//        dialog.setCancelable(false)
//        val view: View =
//            layoutInflater.inflate(com.pesonal.adsdk.R.layout.installnewappdialog, null)
//        dialog.setContentView(view)
//        val update = view.findViewById<TextView>(com.pesonal.adsdk.R.id.update)
//        val title = view.findViewById<TextView>(com.pesonal.adsdk.R.id.txt_title)
//        val decription = view.findViewById<TextView>(com.pesonal.adsdk.R.id.txt_decription)
//        update.text = "Update Now"
//        title.text = "Update our new app now and enjoy"
//        decription.text = ""
//        decription.visibility = View.GONE
//        update.setOnClickListener {
//            try {
//                val marketUri = Uri.parse(url)
//                val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
//                startActivity(marketIntent)
//            } catch (ignored1: ActivityNotFoundException) {
//            }
//        }
//        dialog.create()
//        dialog.show()
//        val window = dialog.window
//        window!!.setLayout(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//    }
//
//    private fun getCurrentVersionCode(): Int {
//        val manager = packageManager
//        var info: PackageInfo? = null
//        try {
//            info = manager.getPackageInfo(
//                packageName, 0
//            )
//            return info.versionCode
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        }
//        return 0
//    }
}