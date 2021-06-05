package com.skyletto.startappfrontend.ui.main.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.models.AlertModel
import com.skyletto.startappfrontend.common.models.ProjectWithTagsAndRoles
import com.skyletto.startappfrontend.common.models.UserWithTags
import com.skyletto.startappfrontend.data.database.AppDatabase
import com.skyletto.startappfrontend.databinding.MapAlertDialogBinding
import com.skyletto.startappfrontend.domain.entities.Tag
import com.skyletto.startappfrontend.ui.chat.ChatActivity

class MapDialog(val model: AlertModel) : DialogFragment() {
    private val mModel = MutableLiveData(model)
    private lateinit var db: AppDatabase
    private var binding: MapAlertDialogBinding? = null
    private lateinit var project: LiveData<ProjectWithTagsAndRoles>
    private lateinit var user: LiveData<UserWithTags>

    init {
        mModel.observeForever {
            binding?.model = it
            binding?.click = View.OnClickListener { _ ->
                if (it.chatId != null) {
                    val intent = Intent(context, ChatActivity::class.java)
                    intent.putExtra("id", it.chatId)
                    context?.startActivity(intent)
                }
            }
            binding?.mapDialogImg?.setImageResource(
                    if (it.isProject) {
                        R.drawable.ic_idea_image
                    } else {
                        R.drawable.ic_profile_image
                    }
            )
            binding?.mapDialogChipGroup?.let { iit->
                it.tags?.let { it2-> inflateChipGroup(iit, it2) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = activity?.application as MainApplication
        db = app.db
        if (model.isProject) {
            project = db.projectDao().getById(model.id)
            observeProject()
        } else {
            user = db.userDao().getById(model.id)
            observeUser()
        }
    }

    private fun observeUser() {
        user.observe(this) { oit ->
            mModel.value?.let {
                it.title = oit.user.title ?: ""
                it.tags = oit.tags
                it.subtitle = oit.user.firstName + " " + oit.user.secondName
                if (!oit.user.experience.isNullOrBlank()) {
                    it.subsubtitle = getString(R.string.exp_of_work) + oit.user.experience?.toInt()?.let { it1 -> resources.getQuantityString(R.plurals.years, it1, it1) }
                } else it.subsubtitle = getString(R.string.exp_of_work) + "нет"

                it.description = oit.user.description ?: ""
                it.chatId = oit.user.id
            }
            mModel.postValue(mModel.value)
        }
    }

    private fun observeProject() {
        project.observe(this) { oit ->
            mModel.value?.let {
                it.title = oit.project.title
                it.subtitle = ""
                val str = getString(R.string.needed) + oit.roles?.joinToString { iit ->
                    if (iit.user == null) {
                        return@joinToString iit.role?.name!!
                    }
                    return@joinToString ""
                }?.replace(" ,", "")?.trim(',')?.toLowerCase()
                if (str != null) {
                    it.subsubtitle = str
                }
                it.description = oit.project.description
                it.tags = oit.tags
                it.chatId = oit.user?.id
            }
            mModel.postValue(mModel.value)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.map_alert_dialog, container);
        binding = DataBindingUtil.bind(v)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        setStyle(STYLE_NO_FRAME, android.R.style.Theme);
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
                resources.displayMetrics.widthPixels * 85 / 100,
                resources.displayMetrics.heightPixels * 50 / 100
        )
    }

    private fun inflateChipGroup(group: ChipGroup, tags: Set<Tag>) {
        group.removeAllViews()
        for (t in tags) {
            val chip = (Chip(requireContext()))
            chip.text = t.name
            chip.chipBackgroundColor = ResourcesCompat.getColorStateList(resources, R.color.skin2, null)
            group.addView(chip)
        }
    }

    companion object {
        private const val TAG = "MAP_DIALOG"
    }
}