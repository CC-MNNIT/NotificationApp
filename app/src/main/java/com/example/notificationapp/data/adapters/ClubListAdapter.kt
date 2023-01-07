package com.example.notificationapp.data.adapters

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notificationapp.R
import com.example.notificationapp.data.adapters.ClubListAdapter.CustomVH
import com.example.notificationapp.data.network.ClubModel
import com.example.notificationapp.utils.Constants
import com.example.notificationapp.view.activities.ClubActivity

class ClubListAdapter(private val mClubs: List<ClubModel>, private val mContext: Context) : RecyclerView.Adapter<CustomVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CustomVH(View.inflate(parent.context, R.layout.club_list_item, null))

    override fun onBindViewHolder(holder: CustomVH, position: Int) {
        holder.setView(mClubs[position])
    }

    override fun getItemCount(): Int = mClubs.size

    inner class CustomVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: AppCompatTextView
        private val description: AppCompatTextView

        init {
            name = itemView.findViewById(R.id.club_name)
            description = itemView.findViewById(R.id.textDescription)

            itemView.setOnClickListener {
                val intent = Intent(mContext, ClubActivity::class.java)
                intent.putExtra(Constants.CLUB_NAME, mClubs[adapterPosition].name)
                intent.putExtra(Constants.CLUB_ID, mClubs[adapterPosition].id)
                intent.putExtra(Constants.CLUB_DESC, mClubs[adapterPosition].description)
                mContext.startActivity(intent)
            }
        }

        fun setView(clubModel: ClubModel) {
            name.text = clubModel.name
            description.text = clubModel.description
        }
    }
}