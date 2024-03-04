package com.example.jooq.entity.uid

class PersonUid: Uid {
    constructor(uid: String) : super(uid)

    constructor() : super()

    override fun getPrefix(): UidPrefix = UidPrefix.PERSON
}
