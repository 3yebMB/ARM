package dev.m13d.rickandmorty.features.characters.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import dev.m13d.rickandmorty.R

class CharactersDialogFragment : DialogFragment() {

    private lateinit var nameF: EditText
    private lateinit var statusF: Spinner
    private lateinit var genderF: Spinner
    private lateinit var speciesF: Spinner
    private lateinit var buttonOk: Button

    interface DialogCallback {
        fun onFilter()
    }

    private var callback: DialogCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        callback = targetFragment as DialogCallback?
        return inflater.inflate(R.layout.dialog_filter_characters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        nameF = view.findViewById(R.id.name)
        statusF = view.findViewById(R.id.status)
        genderF = view.findViewById(R.id.gender)
        speciesF = view.findViewById(R.id.species)

        nameF.setText(CharactersFragment.filter.name)
        statusF.setSelection(
            requireContext().resources.getStringArray(R.array.statuses)
                .indexOf(CharactersFragment.filter.status)
        )
        genderF.setSelection(
            requireContext().resources.getStringArray(R.array.genders)
                .indexOf(CharactersFragment.filter.gender)
        )
        speciesF.setSelection(
            requireContext().resources.getStringArray(R.array.species)
                .indexOf(CharactersFragment.filter.species)
        )

        buttonOk = view.findViewById(R.id.buttonOk)
        buttonOk.setOnClickListener {
            CharactersFragment.filter.apply {
                name = if (nameF.text.toString().isNotEmpty())
                    nameF.text.toString()
                else null
                status = if (statusF.selectedItem.toString() != "Any") {
                    statusF.selectedItem as String
                } else null
                gender = if (genderF.selectedItem.toString() != "Any") {
                    genderF.selectedItem as String
                } else null
                species = if (speciesF.selectedItem.toString() != "Any") {
                    speciesF.selectedItem as String
                } else null
            }
            callback?.onFilter()
            dialog?.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }
}
