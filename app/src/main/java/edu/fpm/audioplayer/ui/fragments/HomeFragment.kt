package edu.fpm.audioplayer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.fpm.audioplayer.R
import edu.fpm.audioplayer.adapters.SongAdapter
import edu.fpm.audioplayer.databinding.FragmentHomeBinding
import edu.fpm.audioplayer.other.Status
import edu.fpm.audioplayer.ui.viewmodels.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var mainViewModel: MainViewModel
    lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var songAdapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        setupRecyclerView()
        subscribeToObservers()

        songAdapter.setOnItemClickListener {
            mainViewModel.playOrToggleSong(it)
        }
    }

    private fun setupRecyclerView() = binding.rvAllSongs.apply {
        adapter = songAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    binding.pbAllSongs.isVisible = false
                    result.data?.let { songs ->
                        songAdapter.songs = songs
                    }
                }

                Status.LOADING -> binding.pbAllSongs.isVisible = true
                Status.ERROR -> Unit
            }
        }
    }
}