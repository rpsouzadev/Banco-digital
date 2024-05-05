package com.rpsouza.bancodigital.presenter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rpsouza.bancodigital.data.enum.TransactionOperation
import com.rpsouza.bancodigital.data.enum.TransactionType
import com.rpsouza.bancodigital.data.model.Transaction
import com.rpsouza.bancodigital.databinding.LastTransactionItemBinding
import com.rpsouza.bancodigital.utils.GetMask

class LastTransactionAdapter(
  private val transactionSelected: (Transaction) -> Unit
) : ListAdapter<Transaction, LastTransactionAdapter.ViewHolder>(DIFF_CALLBACK) {

  companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Transaction>() {
      override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
      }
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(
      LastTransactionItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: LastTransactionAdapter.ViewHolder, position: Int) {
    val transaction = getItem(position)

    transaction.operation?.let {
      holder.binding.textTransactionDescription.text = TransactionOperation.getOperation(it)

      holder.binding.textTransactionType.text = TransactionType.getType(it).toString()
    }


    holder.binding.textTransactionDate.text =
      GetMask.getFormatedDate(transaction.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
    holder.binding.textTransactionValue.text = GetMask.getFormatedValue(transaction.amount)
  }

  inner class ViewHolder(val binding: LastTransactionItemBinding) :
    RecyclerView.ViewHolder(binding.root)

}