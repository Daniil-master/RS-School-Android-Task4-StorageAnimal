package com.daniilmaster.storageanimal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daniilmaster.storageanimal.R
import com.daniilmaster.storageanimal.databinding.FragmentUpdateBinding
import com.daniilmaster.storageanimal.db.AnimalEntity
import com.daniilmaster.storageanimal.main.AppViewModel

class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        binding.etUpadateName.setText(args.currentAnimal.name)
        binding.etUpdateAge.setText(args.currentAnimal.age.toString())
        binding.etUpdateBreed.setText(args.currentAnimal.breed)

        binding.btnUpdate.setOnClickListener {
            updateItem()
        }

        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        return binding.root
    }

    private fun updateItem() {
        val id = args.currentAnimal.id
        val name = binding.etUpadateName.text.toString()
        val age = binding.etUpdateAge.text.toString()
        val breed = binding.etUpdateBreed.text.toString()

        if (inputCheck(name, age, breed)) {
            val updateAnimal = AnimalEntity(id, name, age.toInt(), breed)
            viewModel.updateAnimal(updateAnimal)

            Toast.makeText(requireContext(), "Update Successfully!", Toast.LENGTH_SHORT).show()
            // Navigation back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun inputCheck(name: String, age: String, breed: String): Boolean {
        return name.isNotEmpty() && age.isNotEmpty() && breed.isNotEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}