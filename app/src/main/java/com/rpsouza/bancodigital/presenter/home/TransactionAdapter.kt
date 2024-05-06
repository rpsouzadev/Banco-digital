package com.rpsouza.bancodigital.presenter.home

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
import com.rpsouza.bancodigital.databinding.TransactionItemBinding
import com.rpsouza.bancodigital.utils.GetMask

class TransactionAdapter(
  private val context: Context,
  private val transactionSelected: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.ViewHolder>(DIFF_CALLBACK) {

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
      TransactionItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: TransactionAdapter.ViewHolder, position: Int) {
    val transaction = getItem(position)

    transaction.operation?.let {
      val setBackgroundColor = if (transaction.type == TransactionType.CASH_IN) {
        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_cash_in))
      } else {
        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_cash_out))
      }

      holder.binding.textTransactionDescription.text = TransactionOperation.getOperation(it)
      holder.binding.textTransactionType.text = TransactionType.getType(it).toString()
      holder.binding.textTransactionType.backgroundTintList = setBackgroundColor
    }


    holder.binding.textTransactionDate.text =
      GetMask.getFormatedDate(transaction.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
    holder.binding.textTransactionValue.text =
      context.getString(R.string.text_formated_value, GetMask.getFormatedValue(transaction.amount))


    holder.itemView.setOnClickListener {
      transactionSelected(transaction)
    }
  }

  inner class ViewHolder(val binding: TransactionItemBinding) :
    RecyclerView.ViewHolder(binding.root)
}