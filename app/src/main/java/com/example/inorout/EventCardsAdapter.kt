package com.example.inorout

import android.content.Context;
import android.icu.util.UniversalTimeScale.toLong
import android.os.Build
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class EventCardsAdapter(context: Context,events: ArrayList<EventCard>) : BaseAdapter() {
    private val context = context
    private val events = events
    private lateinit var name: TextView
    private lateinit var type: TextView
    private lateinit var description: TextView
    private lateinit var imgResource: ImageView
    private lateinit var date: TextView
    override fun getCount(): Int {
        return events.count()
    }

    override fun getItem(p0: Int): EventCard {
        return events[p0]
    }

    override fun getItemId(p0: Int): Long {
        return events[p0].Id!!.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.card_item,parent,false)
        name = convertView.findViewById(R.id.info_Title)
        type = convertView.findViewById(R.id.info_Type)
        description = convertView.findViewById(R.id.info_Description)
        imgResource = convertView.findViewById(R.id.imageView)
        date = convertView.findViewById(R.id.info_Date)


        var givenEventCard = getItem(position)

        name.text = givenEventCard.name
        type.text = givenEventCard.type
        description.text = givenEventCard.description
        date.text = givenEventCard.occurenceFormat(false)
        bindImage(imgResource, givenEventCard.images?.get(0)?.url)

        return convertView
    }
    @BindingAdapter("imageUrl")
    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            Glide.with(imgView)
                .load(imgUri)
                .apply(RequestOptions().placeholder(R.drawable.loading).error(R.drawable.loading))
                .into(imgView)
        }
    }

}