package glory.hearing.tool.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.pesonal.adsdk.AppManage
import glory.hearing.tool.R
import glory.hearing.tool.databinding.ActivityMainBinding
import glory.hearing.tool.repository.DataStoreRepository
import glory.hearing.tool.service.AudioService
import glory.hearing.tool.service.VolumeChangeReceiver
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.ceil


class MainActivity : AppCompatActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    //Audio
    private var isOn = false
    private lateinit var broadCast: VolumeChangeReceiver
    lateinit var mAudio: AudioManager


    //private var audioTrack: AudioTrack? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var dataStoreRepo: DataStoreRepository
    private lateinit var manager: ReviewManager
    private lateinit var reviewInfo: ReviewInfo

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        //loadInterstitialAd
        AppManage.getInstance(this).loadInterstitialAd(
            this,
            AppManage.ADMOB_I[0],
            AppManage.FACEBOOK_I[0]
        )

        //Native Ads
        AppManage.getInstance(this).showNative(
            findViewById<View>(R.id.native_container) as ViewGroup,
            AppManage.ADMOB_N[0],
            AppManage.FACEBOOK_N[0]
        )



        dataStoreRepo = DataStoreRepository(this)
        val serviceIntent = Intent(this, AudioService::class.java)
        val audioFile = serviceIntent.getBundleExtra("audioFile")
        Log.d("TAG", "audioFile: $audioFile")

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {}

        lifecycleScope.launch {
            val isRecordingOn: Boolean = dataStoreRepo.readFromDataStore.first()
            //binding.isRecordingOnSwitch.isChecked = isRecordingOn
        }
//        binding.isRecordingOnSwitch.setOnCheckedChangeListener { _, isChecked ->
//            lifecycleScope.launch {
//                dataStoreRepo.saveToDataStore(isChecked)
//            }
//        }

        binding.toolbarInclude.crown.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }

        binding.toolbarInclude.lightBulb.setOnClickListener {
            startActivity(Intent(this@MainActivity, HearingFactsActivity::class.java))
        }

        isOn = false

        isOn = isMyServiceRunning(AudioService::class.java)

        binding.button.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                AppManage.getInstance(this).showInterstitialAd(
                    this,
                    {
                        binding.button.setImageResource(if (!isOn) R.drawable.stop else R.drawable.start)

                        isOn = !isOn
                        if (isOn) {
                            if (!isMyServiceRunning(AudioService::class.java)) {
                                ContextCompat.startForegroundService(
                                    this@MainActivity,
                                    serviceIntent
                                )
                            } else {
                                Toast.makeText(this@MainActivity, "Running", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            stopService(serviceIntent)
                        }
                    },
                    "",
                    AppManage.app_mainClickCntSwAd
                )


            }
        }

        mAudio = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
        (AudioManager.STREAM_RING)

        initControls()

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }

    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun initControls() {

        broadCast = VolumeChangeReceiver { volume ->
            setProgress()
        }
        registerReceiver(broadCast, IntentFilter("android.media.VOLUME_CHANGED_ACTION"))
        binding.setVolumePercentage.max = mAudio
            .getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        setProgress()
        binding.setVolumePercentage.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mAudio.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    private fun setProgress() {
        val mediaVolume: Int = mAudio.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVol: Int = mAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val volPercentage = ceil(mediaVolume.toDouble() / maxVol.toDouble() * 100)
        binding.setVolumePercentage.progress = mediaVolume
        //binding.percentage.text = "${volPercentage.roundToInt()} %"
    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)

        builder.setMessage("Would you like to rate our app ?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes,I Love it"
            ) { dialogInterFace, p1 ->

                manager = ReviewManagerFactory.create(this)
                val request: Task<ReviewInfo> = manager.requestReviewFlow()

                request.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        reviewInfo = task.result!!
                        val flow = manager.launchReviewFlow(this@MainActivity, reviewInfo)

                        flow.addOnSuccessListener {
                            //Toast.makeText(this, "Listener", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // @ReviewErrorCode val reviewErrorCode = (task.exception ).errorCode
                    }
                }
            }
            .setNegativeButton(
                "No,Sure "
            ) { _, _ ->

                AppManage.getInstance(this).showInterstitialAd(this, {
                    super.onBackPressed()
                }, "", AppManage.app_mainClickCntSwAd)

            }
        val alertDialog = builder.create()
        alertDialog.show()

    }

}


//    private fun playRecord(file: File) {
//        //Read the file
//        val musicLength: Int = (file.length() / 2).toInt()
//        val music = ShortArray(musicLength)
//        try {
//            val inputStream: InputStream = FileInputStream(file)
//            val dis = DataInputStream(BufferedInputStream(inputStream))
//            var i = 0
//            while (dis.available() > 0) {
//                music[i] = dis.readShort()
//                i++
//            }
//            dis.close()
//            val sampleRate = getSampleRate()
//            audioTrack = AudioTrack(
//                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build(),
//                AudioFormat.Builder().setEncoding(format).setSampleRate(sampleRate)
//                    .setChannelMask(channelOut)
//                    .build(),
//                musicLength * 2,
//                AudioTrack.MODE_STREAM,
//                AudioManager.AUDIO_SESSION_ID_GENERATE
//            )
//            audioTrack?.play()
//            audioTrack?.write(music, 0, musicLength)
////            audioTrack.stop()
//        } catch (t: Throwable) {
//            Log.e("TAG", "Play failed")
//        }
//    }
//
//    private val channelIn: Int = AudioFormat.CHANNEL_IN_MONO
//    private val channelOut: Int = AudioFormat.CHANNEL_OUT_MONO
//    private val format: Int = AudioFormat.ENCODING_PCM_16BIT
//    private fun getSampleRate(): Int {
//        //Find a sample rate that works with the device
//        for (rate in intArrayOf(8000, 11025, 16000, 22050, 44100, 48000)) {
//            val buffer = AudioRecord.getMinBufferSize(rate, channelIn, format)
//            if (buffer > 0) return rate
//        }
//        return -1
//    }
