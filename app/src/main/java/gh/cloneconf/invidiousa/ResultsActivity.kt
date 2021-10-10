package gh.cloneconf.invidiousa

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gh.cloneconf.invidiousa.api.Invidious
import gh.cloneconf.invidiousa.databinding.ActivityResultsBinding
import gh.cloneconf.invidiousa.databinding.ItemResultBinding
import gh.cloneconf.invidiousa.model.Video
import gh.cloneconf.invidiousa.player.PlaybackActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ResultsActivity : AppCompatActivity() {


    private lateinit var binds : ActivityResultsBinding

    private lateinit var q : String

    private val results = ArrayList<Any>().apply { add(1) }

    var pos = 0

    private val adapter by lazy { Adapter() }


    private val videoPlaceHolder by lazy {
        val bm = Bitmap.createBitmap(480, 360, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        canvas.drawColor(Color.argb(255, 255, 255, 255))
        BitmapDrawable(resources, bm)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        q = intent.getStringExtra("q")!!

        binds = ActivityResultsBinding.inflate(layoutInflater).apply {
            setContentView(root)

            titleTv.text = q

            resultsRv.layoutManager = LinearLayoutManager(this@ResultsActivity, LinearLayoutManager.HORIZONTAL, false)
            resultsRv.adapter = adapter



            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val data = Invidious.search(q)

                    withContext(Dispatchers.Main) {
                        results.clear()
                        results.addAll(data)
                        adapter.notifyDataSetChanged()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }


    companion object {
        const val TYPE_PROGRESS = 0
        const val TYPE_VIDEO = 10
    }

    inner class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        var defaultBg : Drawable? = null
        val selectedBg by lazy { ContextCompat.getColor(this@ResultsActivity, R.color.background_gradient_end) }

        inner class VideoVHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val binds = ItemResultBinding.bind(itemView)
        }
        inner class ProgressVHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                TYPE_VIDEO ->
                    VideoVHolder(layoutInflater.inflate(R.layout.item_result, parent, false))

                else -> ProgressVHolder(layoutInflater.inflate(R.layout.item_progress, parent, false))

            }



        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (defaultBg == null) defaultBg = binds.root.background


            when (holder) {

                is Adapter.VideoVHolder -> {
                    holder.binds.apply {
                        val video = results[position] as Video


                        root.setOnClickListener { open(video) }

                        if (position == pos) {
                            root.setBackgroundColor(selectedBg)

                            this@ResultsActivity.binds.titleTv.text = video.title
                        } else {
                            root.background = defaultBg
                        }


                        Glide.with(backgroundIv)
                            .load(video.thumbnail.src)
                            .override(video.thumbnail.width, video.thumbnail.height)
                            .placeholder(videoPlaceHolder)
                            .into(backgroundIv)

                    }
                }
            }
        }


        override fun getItemViewType(position: Int) =
            if (results[position] is Video) TYPE_VIDEO
            else TYPE_PROGRESS


        override fun getItemCount() = results.size
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {


        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (pos <= 0) return false

                val oldPos = pos
                binds.resultsRv.smoothScrollToPosition(--pos)
                adapter.notifyItemChanged(oldPos)
                adapter.notifyItemChanged(pos)
                true
            }

            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (pos >= results.size - 1) return true

                val oldPos = pos
                binds.resultsRv.smoothScrollToPosition(++pos)
                adapter.notifyItemChanged(oldPos)
                adapter.notifyItemChanged(pos)
                true
            }

            KeyEvent.KEYCODE_DPAD_CENTER.or(KeyEvent.KEYCODE_ENTER) -> {
                open(results[pos] as Video)
                true
            }

            else -> super.onKeyDown(keyCode, event)
        }

    }



    private fun open(video: Video) {
        startActivity(Intent(this, PlaybackActivity::class.java).apply {
            putExtra("id", video.id)
        })
    }




}