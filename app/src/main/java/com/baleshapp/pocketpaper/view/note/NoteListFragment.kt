package com.baleshapp.pocketpaper.view.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.repository.NoteRepository
import com.baleshapp.pocketpaper.databinding.FragmentNoteListBinding
import com.baleshapp.pocketpaper.view.note.adapters.NoteAdapter
import com.baleshapp.pocketpaper.view.note.dialogs.NoteDialog
import com.baleshapp.pocketpaper.viewmodel.note.NoteViewModelFactory
import com.baleshapp.pocketpaper.viewmodel.note.NoteViewModel as NoteViewModel1

class NoteListFragment : Fragment() {

    lateinit var binding: FragmentNoteListBinding
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var noteViewModel: NoteViewModel1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_list, container, false)
        binding.notesFragment = this
        gridLayoutManager = GridLayoutManager(context, 2)
        binding.notesRecyclerView.layoutManager = gridLayoutManager

        val viewModelFactory = NoteViewModelFactory(
            NoteRepository(binding.root.context)
        )

        noteViewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel1::class.java]
        val adapter = NoteAdapter({ noteViewModel.delete(it) }, { noteViewModel.update(it) })

        noteViewModel.getNotes().observe(viewLifecycleOwner) {
            adapter.setItems(it)
            binding.invalidateAll()
        }

        binding.notesRecyclerView.layoutManager = gridLayoutManager
        binding.notesRecyclerView.adapter = adapter
        return binding.root
    }

    fun createNewNote() {
        NoteDialog(binding.root.context, true, null, {
            noteViewModel.insert(it)
        },
            {
                noteViewModel.update(it)
            })
    }

    fun onBack() {
        val fragmentManager = parentFragmentManager
        fragmentManager.popBackStack()
    }

}