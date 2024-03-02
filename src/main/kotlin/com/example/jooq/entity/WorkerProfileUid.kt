package com.example.jooq.entity

class PersonUid: Uid() {
    override fun getPrefix(): UidPrefix = UidPrefix.PERSON
}
