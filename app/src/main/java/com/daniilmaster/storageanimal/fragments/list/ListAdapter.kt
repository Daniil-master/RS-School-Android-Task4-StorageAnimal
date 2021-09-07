package com.daniilmaster.storageanimal.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.daniilmaster.storageanimal.databinding.ListItemBinding
import com.daniilmaster.storageanimal.db.AnimalEntity

class ListAdapter(private val onDeleteFragment: OnDeleteFragment) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    private var animals = emptyList<AnimalEntity>()
    private lateinit var binding: ListItemBinding

    class ListViewHolder(
        private val binding: ListItemBinding,
        private val onDeleteFragment: OnDeleteFragment
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AnimalEntity) {
            binding.txtId.text = "id: ${item.id}"
            binding.txtName.text = item.name
            binding.txtAge.text =  item.age.toString()
            binding.txtBreed.text = item.breed

            binding.listItemLayout.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(item)
                itemView.findNavController().navigate(action)
            }

            binding.imgDelete.setOnClickListener {
                onDeleteFragment.delete(item)
            }

        }
    }

    fun setData(animals: List<AnimalEntity>) {
        this.animals = animals
        notifyDataSetChanged() // можно было и DiffUtil
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ListItemBinding.inflate(layoutInflater, parent, false)
        return ListViewHolder(binding, onDeleteFragment)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(animals[position])
    }

    override fun getItemCount(): Int = animals.size


}