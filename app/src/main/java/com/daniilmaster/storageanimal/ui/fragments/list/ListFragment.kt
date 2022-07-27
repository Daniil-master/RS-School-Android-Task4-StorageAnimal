package com.daniilmaster.storageanimal.ui.fragments.list

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniilmaster.storageanimal.R
import com.daniilmaster.storageanimal.databinding.FragmentListBinding
import com.daniilmaster.storageanimal.db.AnimalEntity
import com.daniilmaster.storageanimal.ui.AppViewModel
import com.daniilmaster.storageanimal.ui.fragments.showToast

class ListFragment : Fragment(), OnDeleteFragment {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AppViewModel
    private var adapter: ListAdapter? = null
    private var _pref: SharedPreferences? = null
    private val pref get() = _pref!!

    private var selectedFilter = "id"
    private var isRoom = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(layoutInflater, container, false)

        // RecyclerView
        adapter = ListAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ViewModel
        viewModel = ViewModelProvider(this).get(AppViewModel::class.java)


        _pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
//        viewModel.animals.observe(viewLifecycleOwner, { listAnimals ->
//            adapter!!.setData(user)
//        }) // обновление при изменений

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        binding.imgAllDelete.setOnClickListener {
            deleteAll()
        }

        binding.imgFilter.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_filterPrefFragment)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val newFilter = pref.getString("list_preference_filter", "id").toString()
        val newIsRoom = pref.getBoolean("switch_room_or_cursor", true)

        if (selectedFilter != newFilter || isRoom != newIsRoom) {
            selectedFilter = newFilter
            isRoom = newIsRoom
            var str =
                getString(R.string.toast_sort) + selectedFilter + getString(R.string.toast_lib)
            str += if (isRoom) "Room" else "Cursor"
            showToast(str)
        }

//        val observer: Observer<List<AnimalEntity>> = Observer { adapter!!.setData(it) }
//        viewModel.animals.removeObserver(observer)

        // Обновление строения
        viewModel.allAnimals()
        viewModel.animals.observe(viewLifecycleOwner) { listAnimals ->
            adapter!!.setData(listAnimals)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun delete(animalEntity: AnimalEntity) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->

            // ViewModel and Navigate
            viewModel.deleteAnimal(animalEntity)

            showToast(getString(R.string.toast_delete_success) + animalEntity.name)

            viewModel.allAnimals()
            viewModel.animals.observe(viewLifecycleOwner) { listAnimals ->
                adapter!!.setData(listAnimals)
            }

        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ ->

        }
        builder.setTitle(getString(R.string.dialog_delete_title) + " ${animalEntity.name}?")
        builder.setMessage(getString(R.string.dialog_delete_message) + " ${animalEntity.name}?")
        builder.create().show()
    }

    private fun deleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->

            // ViewModel and Navigate
            viewModel.deleteAllAnimals()

            showToast(R.string.toast_delete_all_success)

            viewModel.allAnimals()
            viewModel.animals.observe(viewLifecycleOwner) { listAnimals ->
                adapter!!.setData(listAnimals)
            }

        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ ->

        }
        builder.setTitle(R.string.dialog_delete_all_title)
        builder.setMessage(R.string.dialog_delete_all_message)
        builder.create().show()
    }
}