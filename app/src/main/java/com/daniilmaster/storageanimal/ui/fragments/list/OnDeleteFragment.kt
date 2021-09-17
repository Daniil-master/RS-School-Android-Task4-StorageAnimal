package com.daniilmaster.storageanimal.ui.fragments.list

import com.daniilmaster.storageanimal.db.AnimalEntity

interface OnDeleteFragment {
    fun delete(animalEntity: AnimalEntity)
}