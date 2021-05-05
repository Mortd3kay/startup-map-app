package com.skyletto.startappfrontend.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false )
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