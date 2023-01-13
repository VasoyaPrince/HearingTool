package glory.hearing.tool.adepter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import glory.hearing.tool.model.Item
import glory.hearing.tool.databinding.ItemsLayoutBinding

class ItemAdapter(private var context: Context, private val photosList: ArrayList<Item>, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    class ItemHolder(private val itemLayoutBinding: ItemsLayoutBinding) :
        RecyclerView.ViewHolder(itemLayoutBinding.root) {
        val binding = itemLayoutBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = ItemsLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = photosList[position]
        holder.binding.imageView6.setImageResource(item.Image)
        holder.binding.textView4.text = item.Title
        holder.binding.textView3.text = item.Description
        if (position == photosList.size-1){
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return photosList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    private val runnable= Runnable {
        photosList.addAll(photosList)
        notifyDataSetChanged()
    }
 }