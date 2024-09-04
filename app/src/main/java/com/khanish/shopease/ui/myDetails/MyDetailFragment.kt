package com.khanish.shopease.ui.myDetails

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.khanish.shopease.R
import com.khanish.shopease.base.BaseFragment
import com.khanish.shopease.databinding.FragmentMyDetailBinding
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class MyDetailFragment : BaseFragment<FragmentMyDetailBinding>(
    FragmentMyDetailBinding::inflate
) {
    private val viewModel by viewModels<MyDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.dateContainer.setEndIconOnClickListener {
            showDatePickerDialog()
        }
        viewModel.getUserById()
        observeData()
        val textInputLayout = binding.phoneContainer
        val editText = textInputLayout.editText

        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                s?.let {
                    val formattedNumber = formatPhoneNumber(it.toString())
                    if (editText.text.toString() != formattedNumber) {
                        editText.removeTextChangedListener(this)
                        editText.setText(formattedNumber)
                        editText.setSelection(formattedNumber.length)
                        editText.addTextChangedListener(this)
                    }
                }


            }

            override fun afterTextChanged(s: Editable?) {


            }

            private fun formatPhoneNumber(phoneNumber: String): String {
                return phoneNumber.replace(Regex("[^\\d]"), "").let {
                    when {
                        it.length <= 3 -> it
                        it.length <= 6 -> "${it.substring(0, 3)} ${it.substring(3)}"
                        it.length <= 8 -> "${it.substring(0, 3)} ${
                            it.substring(
                                3,
                                6
                            )
                        } ${it.substring(6)}"

                        else -> "${it.substring(0, 3)} ${it.substring(3, 6)} ${
                            it.substring(
                                6,
                                8
                            )
                        } ${it.substring(8)}"

                    }
                }
            }


        })

        setSubmitButton()
    }

    private fun setSubmitButton() {
        binding.buttonSubmit.setOnClickListener {
            val fullNameInputValue = binding.fullNameInput.text.toString().trim()
            val emailInputValue = binding.emailAddressInput.text.toString().trim()
            val dateInputValue = binding.dateInput.text.toString().trim()
            val phoneInputValue = binding.phoneInput.text.toString().trim()

            viewModel.updateUserValue(
                fullNameInputValue,
                phoneInputValue,
                emailInputValue,
                dateInputValue
            )

            FancyToast.makeText(
                requireContext(),
                "Information has been changed",
                FancyToast.LENGTH_LONG,
                FancyToast.SUCCESS,
                false
            ).show()

        }
    }

    private fun observeData() {
        with(viewModel) {

            user.observe(viewLifecycleOwner) {

                binding.fullNameInput.setText(it.fullName)
                binding.phoneInput.setText(it.phoneNumber)
                binding.dateInput.setText(it.dateOfBirth)
                binding.emailAddressInput.setText(it.email)

            }
        }
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Seçilen tarihi formatla ve TextInputEditText içine yaz
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.dateInput.setText(selectedDate)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

}