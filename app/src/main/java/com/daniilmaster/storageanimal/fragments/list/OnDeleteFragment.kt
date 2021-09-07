package com.daniilmaster.storageanimal.fragments.list

import com.daniilmaster.storageanimal.db.AnimalEntity

interface OnDeleteFragment {
    fun delete(animalEntity: AnimalEntity)
}