package com.daniilmaster.storageanimal.fragments.list

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniilmaster.storageanimal.R
import com.daniilmaster.storageanimal.databinding.FragmentListBinding
import com.daniilmaster.storageanimal.db.AnimalEntity
import com.daniilmaster.storageanimal.main.AppViewModel

class ListFragment : Fragment(), OnDeleteFragment {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AppViewModel
    private var adapter: ListAdapter? = null
//    private var _pref: SharedPreferences? = null
//    private val pref get() = _pref!!

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

//        _pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
//        viewModel.animals.observe(viewLifecycleOwner, { user ->
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

        // Обновление строения
        viewModel.allAnimals()
        viewModel.animals.observe(viewLifecycleOwner, { user ->
            adapter!!.setData(user)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun delete(animalEntity: AnimalEntity) {
//        val isRoom = pref.getBoolean("switch_room_or_cursor", true)

        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->

            // ViewModel and Navigate
            viewModel.deleteAnimal(animalEntity)
            Toast.makeText(
                requireContext(),
                "Successfully removed: ${animalEntity.name}",
                Toast.LENGTH_SHORT
            ).show()


                viewModel.allAnimals()
                viewModel.animals.observe(viewLifecycleOwner, { user ->
                    adapter!!.setData(user)
                })

        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Delete ${animalEntity.name}?")
        builder.setMessage("Are you sure you want to delete ${animalEntity.name}?")
        builder.create().show()
    }

    private fun deleteAll() {


        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->

            // ViewModel and Navigate
            viewModel.deleteAllAnimals()

            Toast.makeText(
                requireContext(),
                "Successfully removed all animals!",
                Toast.LENGTH_SHORT
            ).show()


                viewModel.allAnimals()
                viewModel.animals.observe(viewLifecycleOwner, { user ->
                    adapter!!.setData(user)
                })

        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Delete all animals?")
        builder.setMessage("Are you sure you want to delete all animals?")
        builder.create().show()
    }
}