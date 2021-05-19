package com.skyletto.startappfrontend.common.adapters

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.skyletto.startappfrontend.common.models.UserWithTags
import com.skyletto.startappfrontend.domain.entities.User


class UserArrayAdapter(context: Context, private val users: List<UserWithTags>) : ArrayAdapter<UserWithTags>(context, android.R.layout.simple_dropdown_item_1line, users), Filterable {

    private val customFilter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            Log.d(TAG, "performFiltering: users $users \n filter = $constraint ")
            constraint?.let {
                val results = FilterResults()
                results.values = users.filter { it.user.firstName.toLowerCase().startsWith(constraint.toString().toLowerCase()) || it.user.secondName.toLowerCase().startsWith(constraint.toString().toLowerCase()) }
                results.count = (results.values as List<*>).size
                Log.d(TAG, "performFiltering: users $users \n filter = $constraint ")
                return results
            }
            return FilterResults()
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            Log.d(TAG, "performFiltering: users $users \n filter = $constraint ")
            results?.values?.let {
                val filterList = it as ArrayList<UserWithTags>
                if (results.count > 0) {
                    clear()
                    for (people in filterList) {
                        add(people)
                        notifyDataSetChanged()
                    }
                }
            }
        }

    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val user: User = getItem(position).user
        user?.let {
            (convertView?.findViewById(android.R.id.text1) as TextView).text = "${it.firstName} ${it.secondName}"
            //(convertView.findViewById(android.R.id.text2) as TextView).text = "${it.title}"
        }

        return convertView!!
    }

    override fun getItem(position: Int): UserWithTags {
        return users[position]
    }

    override fun getCount(): Int {
        return users.size
    }

    override fun getFilter() :Filter{
        Log.d(TAG, "getFilter: $customFilter")
        return customFilter
    }

    companion object{
        private const val TAG = "USER_ARRAY_ADAPTER"
    }
}