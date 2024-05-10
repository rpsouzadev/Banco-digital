package com.rpsouza.bancodigital.presenter.features.transfer

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rpsouza.bancodigital.R
import com.rpsouza.bancodigital.data.enum.TransactionOperation
import com.rpsouza.bancodigital.data.enum.TransactionType
import com.rpsouza.bancodigital.data.model.Transaction
import com.rpsouza.bancodigital.data.model.User
import com.rpsouza.bancodigital.databinding.TransactionItemBinding
import com.rpsouza.bancodigital.databinding.TransferUserItemBinding
import com.rpsouza.bancodigital.utils.GetMask
import com.squareup.picasso.Picasso

class TransferUserAdapter(
  private val userSelected: (User) -> Unit
) : ListAdapter<User, TransferUserAdapter.ViewHolder>(DIFF_CALLBACK) {

  companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
      override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
      }
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(
      TransferUserItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val user = getItem(position)

    loadImageUser(holder, user)
    holder.binding.textUsername.text = user.name

    holder.itemView.setOnClickListener {
      userSelected(user)
    }
  }

  private fun loadImageUser(holder: ViewHolder, user: User) {
    if (user.image.isNotEmpty()) {
      Picasso.get()
        .load(user.image)
        .centerCrop()
        .into(holder.binding.userImage)
    }
  }

  inner class ViewHolder(val binding: TransferUserItemBinding) :
    RecyclerView.ViewHolder(binding.root)
}