package dev.m13d.rickandmorty.features.locations.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import dev.m13d.rickandmorty.R

class LocationsDialogFragment : DialogFragment() {

    private lateinit var nameF: EditText
    private lateinit var typeF: Spinner
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
        return inflater.inflate(R.layout.dialog_filter_locations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        nameF = view.findViewById(R.id.name)
        typeF = view.findViewById(R.id.type)

        nameF.setText(LocationsFragment.filter.name)
        typeF.setSelection(
            requireContext().resources.getStringArray(R.array.location_types)
                .indexOf(LocationsFragment.filter.type)
        )

        buttonOk = view.findViewById(R.id.buttonOk)
        buttonOk.setOnClickListener {
            LocationsFragment.filter.apply {
                name = if (nameF.text.toString().isNotEmpty())
                    nameF.text.toString()
                else null
                type = if (typeF.selectedItem.toString() != "Any") {
                    typeF.selectedItem as String
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
