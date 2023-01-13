package glory.hearing.tool.adepter

import android.content.Context
import android.content.Intent
import android.media.*
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import glory.hearing.tool.databinding.RecordingviewBinding
import java.io.*


class RecordingAdepter(private val context: Context, private val List: MutableList<File>) :
    RecyclerView.Adapter<RecordingAdepter.ViewHolder>() {

    private val channelIn: Int = AudioFormat.CHANNEL_IN_MONO
    private val channelOut: Int = AudioFormat.CHANNEL_OUT_MONO
    private val format: Int = AudioFormat.ENCODING_PCM_16BIT
    private var audioTrack: AudioTrack? = null
    private var indexOfItem: Int = 0

    class ViewHolder(itemView: RecordingviewBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecordingviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recordingName = List[position]
        holder.binding.recordingName.text = recordingName.name
        holder.binding.ivShare.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            val screenshotUri = Uri.parse(recordingName.path)
            sharingIntent.type = "*/File"
            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
            context.startActivity(Intent.createChooser(sharingIntent, "Share using"))
        }
        checkStatus(holder, position)
        holder.binding.ivPlay.setOnClickListener {
            playRecord(
                File("${context.externalMediaDirs.first().absoluteFile}/${recordingName.name}"),
                position
            )
            checkStatus(holder, position)
        }

        holder.binding.ivStop.setOnClickListener {
            audioTrack?.pause()
            checkStatus(holder, position)
        }
    }

    private fun checkStatus(holder: ViewHolder, position: Int) {
        if (indexOfItem == position) {
            if (audioTrack != null) {
                when (audioTrack!!.playState) {

                    AudioTrack.PLAYSTATE_PAUSED -> {
                        audioTrack = null
                        holder.binding.ivPlay.visibility = View.VISIBLE
                        holder.binding.ivStop.visibility = View.GONE
                    }
                    else -> {
                        holder.binding.ivPlay.visibility = View.GONE
                        holder.binding.ivStop.visibility = View.VISIBLE
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return List.size
    }

    private fun playRecord(file: File, index: Int) {
       /* if (audioTrack != null && audioTrack!!.playState == AudioTrack.PLAYSTATE_PAUSED) {
            audioTrack?.play()
            return
        }*/
        //Read the file
        val musicLength: Int = (file.length() / 2).toInt()
        val music = ShortArray(musicLength)
        try {
            val inputStream: InputStream = FileInputStream(file)
            val dis = DataInputStream(BufferedInputStream(inputStream))
            var i = 0
            while (dis.available() > 0) {
                music[i] = dis.readShort()
                i++
            }
            dis.close()
            val sampleRate = getSampleRate()
            audioTrack = AudioTrack(
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build(),
                AudioFormat.Builder().setEncoding(format).setSampleRate(sampleRate)
                    .setChannelMask(channelOut).build(),
                musicLength * 2,
                AudioTrack.MODE_STREAM,
                AudioManager.AUDIO_SESSION_ID_GENERATE
            )

            audioTrack?.play()
            audioTrack?.write(music, 0, musicLength)
            indexOfItem = index
        } catch (t: Throwable) {
            Log.e("TAG", "Play failed")
        }
    }

    private fun getSampleRate(): Int {
        //Find a sample rate that works with the device
        for (rate in intArrayOf(8000, 11025, 16000, 22050, 44100, 48000)) {
            val buffer = AudioRecord.getMinBufferSize(rate, channelIn, format)
            if (buffer > 0) return rate
        }
        return -1
    }

}