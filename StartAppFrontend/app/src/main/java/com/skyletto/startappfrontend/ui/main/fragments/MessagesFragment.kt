package com.skyletto.startappfrontend.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.adapters.ChatAdapter
import com.skyletto.startappfrontend.databinding.FragmentMessagesBinding
import com.skyletto.startappfrontend.ui.main.ActivityFragmentWorker
import com.skyletto.startappfrontend.ui.main.viewmodels.MessagesViewModel

class MessagesFragment : Fragment() {
    var mActivity: ActivityFragmentWorker? = null
    private var viewModel: MessagesViewModel? = null
    private var adapter: ChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let { ViewModelProvider(it).get(MessagesViewModel::class.java) }
        viewModel?.activity = mActivity
        viewModel?.loadChats()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentMessagesBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_messages, container, false)
        val settingsBtn = binding.messageTbSettings
        settingsBtn.setOnClickListener { viewModel?.goToSettings()}
        val rv = binding.chatsRv
        adapter = context?.let { ChatAdapter(it) }
        viewModel?.chats?.observe(viewLifecycleOwner){
            adapter?.chats = it
            adapter?.notifyDataSetChanged()
        }
        val llm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false )
        val divider = DividerItemDecoration(context, llm.orientation)
        divider.setDrawable(resources.getDrawable(R.drawable.rv_divider, null))
        rv.layoutManager = llm
        rv.addItemDecoration(divider)
        rv.adapter = adapter
        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance():MessagesFragment{
            return MessagesFragment()
        }
    }
}