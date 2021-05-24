package com.skyletto.startappfrontend.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.models.AlertModel
import com.skyletto.startappfrontend.common.models.ProjectWithTagsAndRoles
import com.skyletto.startappfrontend.common.models.UserWithTags
import com.skyletto.startappfrontend.data.database.AppDatabase
import com.skyletto.startappfrontend.databinding.MapAlertDialogBinding
import com.skyletto.startappfrontend.ui.chat.ChatActivity

class MapDialog(val model : AlertModel) : DialogFragment() {
    private val mModel = MutableLiveData(model)
    private lateinit var db: AppDatabase
    private var binding : MapAlertDialogBinding?=  null
    private lateinit var project: LiveData<ProjectWithTagsAndRoles>
    private lateinit var user: LiveData<UserWithTags>

    init {
        mModel.observeForever {
            binding?.model = it
            binding?.click = View.OnClickListener { iit->
                if (it.chatId!=null){
                    val intent = Intent(context, ChatActivity::class.java)
                    intent.putExtra("id", it.chatId)
                    context?.startActivity(intent)
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = activity?.application as MainApplication
        db = app.db
        if (model.isProject){
            project = db.projectDao().getById(model.id)
            observeProject()
        } else {
            user = db.userDao().getById(model.id)
            observeUser()
        }
    }

    private fun observeUser() {
        user.observe(this){ oit->
            mModel.value?.let {
                it.title = oit.user.title?:""
                it.tags = oit.tags
                it.subtitle = oit.user.firstName +" "+ oit.user.secondName
                it.subsubtitle = getString(R.string.exp_of_work)+oit.user.experience
                it.description = oit.user.description?:""
                it.chatId = oit.user.id
            }
            mModel.postValue(mModel.value)
        }
    }

    private fun observeProject(){
        project.observe(this){ oit->
            mModel.value?.let {
                it.title = oit.project.title
                it.subtitle = ""
                val str = getString(R.string.needed) + oit.roles?.joinToString { iit ->
                    if (iit.user==null){
                        return@joinToString iit.role?.name!!
                    }
                    return@joinToString ""
                }
                Log.d(TAG, "onCreate: subtitle $str")
                if (str != null) {
                    it.subsubtitle =str
                }
                it.description = oit.project.description
                it.tags = oit.tags
                it.chatId = oit.user?.id
            }
            mModel.postValue(mModel.value)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.map_alert_dialog, container, false)
        return binding?.root
    }

    companion object{
        private const val TAG = "MAP_DIALOG"
    }
}