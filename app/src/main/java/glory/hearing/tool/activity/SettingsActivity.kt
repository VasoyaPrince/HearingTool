package glory.hearing.tool.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import glory.hearing.tool.repository.DataStoreRepository
import glory.hearing.tool.BuildConfig
import glory.hearing.tool.R
import glory.hearing.tool.databinding.ActivitySettingsBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    private lateinit var dataStoreRepo: DataStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

//        //loadInterstitialAd
//        AppManage.getInstance(this).loadInterstitialAd(
//            this, AppManage.ADMOB_I[0], AppManage.FACEBOOK_I[0]
//        )
//
//        //Native Ads
//        AppManage.getInstance(this).showNative(
//            findViewById<View>(R.id.native_container) as ViewGroup,
//            AppManage.ADMOB_N[0],
//            AppManage.FACEBOOK_N[0]
//        )



        binding.toolbarInclude.back.setOnClickListener {
//            AppManage.getInstance(this@SettingsActivity).showInterstitialAd(
//                this@SettingsActivity, {
//                }, "", AppManage.app_mainClickCntSwAd
//            )
                    startActivity(Intent(this@SettingsActivity, MainActivity::class.java))
                    finish()
        }

        dataStoreRepo = DataStoreRepository(this)

        binding.recording.setOnClickListener {
//            AppManage.getInstance(this@SettingsActivity).showInterstitialAd(
//                this@SettingsActivity, {
//
//                }, "", AppManage.app_mainClickCntSwAd
//            )
            startActivity(Intent(this@SettingsActivity, AllFileActivity::class.java))
        }

        binding.shareApp.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hearing Tool")
            var shareMessage = "\nHearing Tool\n\n"
            shareMessage = """
                ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                
                """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Share"))
        }

        binding.permissionApp.setOnClickListener {
            val builder: android.app.AlertDialog.Builder =
                android.app.AlertDialog.Builder(this@SettingsActivity)

            builder.setMessage("Audio Permission :- Require to use device's mic \n(Note:- Application will use mic in background as well when start button is turned on along with auto recording feature.)")
            // Set Alert Title
            builder.setTitle("Permission")

            builder.setPositiveButton(
                "ok",
                DialogInterface.OnClickListener { dialog: DialogInterface?, _: Int ->
                    dialog?.dismiss()
                })

            val alertDialog: android.app.AlertDialog? = builder.create()
            alertDialog?.show()
        }

        lifecycleScope.launch {
            val isRecordingOn: Boolean = dataStoreRepo.readFromDataStore.first()
            binding.isRecordingOnSwitch.isChecked = isRecordingOn
        }
//        AppManage.getInstance(this@SettingsActivity).showInterstitialAd(
//            this@SettingsActivity, {
//            }, "", AppManage.app_mainClickCntSwAd
//        )
                binding.isRecordingOnSwitch.setOnCheckedChangeListener { _, isChecked ->
                    lifecycleScope.launch {
                        dataStoreRepo.saveToDataStore(isChecked)
                    }
                }


    }

    override fun onBackPressed() {
//        AppManage.getInstance(this@SettingsActivity).showInterstitialAd(
//            this@SettingsActivity, {
//            }, "", AppManage.app_innerClickCntSwAd
//        )
                super.onBackPressed()
    }
}