package com.nyanm.rightknob

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SingleMusic(
    val jkRes: Int,
    val mid: Int,
    val name: String,
    val artist: String,
    val firstKana: Char,
    val date: Int,
    val gameVer: Int,
    val infVer: Int,
    val nov: Int,
    val adv: Int,
    val exh: Int,
    val inf: Int,
    val mxm: Int
)

class MusicBoxAdapter(private val dataList: List<SingleMusic>, private val mContext: Context) :
    RecyclerView.Adapter<MusicBoxAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewJKRes: ImageView = view.findViewById(R.id.img_jacket)
        val viewMid: TextView = view.findViewById(R.id.text_mid)
        val viewName: TextView = view.findViewById(R.id.text_name)
        val viewArtist: TextView = view.findViewById(R.id.text_artist)
        val viewDate: TextView = view.findViewById(R.id.text_date)
        val view1stKana: TextView = view.findViewById(R.id.text_1st_kana)
        val viewNOV: TextView = view.findViewById(R.id.text_nov)
        val viewADV: TextView = view.findViewById(R.id.text_adv)
        val viewEXH: TextView = view.findViewById(R.id.text_exh)
        val viewINF: TextView = view.findViewById(R.id.text_inf)
        val viewGRV: TextView = view.findViewById(R.id.text_grv)
        val viewHVN: TextView = view.findViewById(R.id.text_hvn)
        val viewVVD: TextView = view.findViewById(R.id.text_vvd)
        val viewMXM: TextView = view.findViewById(R.id.text_mxm)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.music_box, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val musicItem = dataList[position]
        // set unchangeable data
        holder.viewJKRes.setImageResource(musicItem.jkRes)
        holder.viewMid.text = musicItem.mid.toString()
        holder.viewName.text = musicItem.name
        holder.viewArtist.text = musicItem.artist
        holder.view1stKana.text = musicItem.firstKana.toString()
        val intYear: Int = musicItem.date / 10000
        val intMonth: Int = (musicItem.date / 100) % 100
        val intDay: Int = musicItem.date % 100
        holder.viewDate.text =
            String.format(mContext.getString(R.string.music_box_date), intYear, intMonth, intDay)

        // set level information
        if (musicItem.nov == 0) {
            holder.viewNOV.visibility = View.INVISIBLE
        } else holder.viewNOV.text = musicItem.nov.toString()
        if (musicItem.adv == 0) {
            holder.viewADV.visibility = View.INVISIBLE
        } else holder.viewADV.text = musicItem.adv.toString()
        if (musicItem.exh == 0) {
            holder.viewEXH.visibility = View.INVISIBLE
        } else holder.viewEXH.text = musicItem.exh.toString()
        // set 4th level
        if (musicItem.mxm != 0) {
            holder.viewMXM.visibility = View.VISIBLE
            holder.viewMXM.text = musicItem.mxm.toString()
        } else {
            if (musicItem.inf != 0) {
                when (musicItem.infVer) {
                    2 -> {
                        holder.viewINF.visibility = View.VISIBLE
                        holder.viewINF.text = musicItem.inf.toString()
                    }
                    3 -> {
                        holder.viewGRV.visibility = View.VISIBLE
                        holder.viewGRV.text = musicItem.inf.toString()
                    }
                    4 -> {
                        holder.viewHVN.visibility = View.VISIBLE
                        holder.viewHVN.text = musicItem.inf.toString()
                    }
                    5 -> {
                        holder.viewVVD.visibility = View.VISIBLE
                        holder.viewVVD.text = musicItem.inf.toString()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = dataList.size
}
