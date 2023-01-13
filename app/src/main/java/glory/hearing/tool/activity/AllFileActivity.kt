package glory.hearing.tool.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import glory.hearing.tool.adepter.RecordingAdepter
import com.pesonal.adsdk.AppManage
import glory.hearing.tool.databinding.ActivityAllFileBinding
import java.io.File

class AllFileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllFileBinding
    private lateinit var recordingAdapter: RecordingAdepter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        //loadInterstitialAd
        AppManage.getInstance(this).loadInterstitialAd(
            this, AppManage.ADMOB_I[0], AppManage.FACEBOOK_I[0]
        )



        //banner ads
        AppManage.getInstance(this).showNativeBanner(
            binding.linearLayout, AppManage.ADMOB_I[0], AppManage.FACEBOOK_I[0]
        )
        AppManage.getInstance(this@AllFileActivity).showNativeBanner(binding.linearLayout)

        binding.toolbarInclude.back.setOnClickListener {
            AppManage.getInstance(this@AllFileActivity).showInterstitialAd(
                this@AllFileActivity, {
                    startActivity(Intent(this@AllFileActivity, SettingsActivity::class.java))
                    finish()
                }, "", AppManage.app_mainClickCntSwAd
            )
        }


        val pathView: File = externalMediaDirs.first().absoluteFile
        val files: MutableList<File> = arrayListOf()
        if (pathView.isDirectory) {
            for (i in pathView.listFiles()!!) {
                if (i.isFile) {
                    files.add(i)
                }
            }
        }
        recordingAdapter = RecordingAdepter(this@AllFileActivity, files)

        binding.recyclerView.adapter = recordingAdapter


    }

    override fun onBackPressed() {
        AppManage.getInstance(this@AllFileActivity).showInterstitialAd(
            this@AllFileActivity, {
                super.onBackPressed()
            }, "", AppManage.app_innerClickCntSwAd
        )
    }
}