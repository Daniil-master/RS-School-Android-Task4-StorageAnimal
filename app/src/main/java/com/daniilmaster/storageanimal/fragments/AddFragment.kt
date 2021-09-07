package com.daniilmaster.storageanimal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.daniilmaster.storageanimal.R
import com.daniilmaster.storageanimal.databinding.FragmentAddBinding
import com.daniilmaster.storageanimal.db.AnimalEntity
import com.daniilmaster.storageanimal.main.AppViewModel

class AddFragment : Fragment() {
    private lateinit var viewModel: AppViewModel

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        binding.btnAdd.setOnClickListener {
            addToDatabase()
        }

        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
        return binding.root
    }

    private fun addToDatabase() {
        val name = binding.etAddName.text.toString()
        val age = binding.etAddAge.text.toString()
        val breed = binding.etAddBreed.text.toString()

        if (inputCheck(name, age, breed)) {
            // Create object, add to database and in view list
            val animal = AnimalEntity(0, name, age.toInt(), breed)
            viewModel.addAnimal(animal)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
        else{
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun inputCheck(name: String, age: String, breed: String): Boolean {
        return name.isNotEmpty() && age.isNotEmpty() && breed.isNotEmpty()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}