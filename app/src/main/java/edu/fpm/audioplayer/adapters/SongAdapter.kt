package edu.fpm.audioplayer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import edu.fpm.audioplayer.R
import edu.fpm.audioplayer.data.entities.Song
import edu.fpm.audioplayer.databinding.ListItemBinding
import javax.inject.Inject

class SongAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(
        private val binding: ListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song, glide: RequestManager) {
            with(binding) {
                tvPrimary.text = song.title
                tvSecondary.text = song.subtitle
                glide.load(song.imageUrl).into(ivItemImage)
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.mediaId == newItem.mediaId
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var songs: List<Song>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private var onItemClickListener: ((Song) -> Unit)? = null
    fun setOnItemClickListener(listener: (Song) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song, glide)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(song)
            }
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}