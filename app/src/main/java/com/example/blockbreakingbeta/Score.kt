package com.example.blockbreakingbeta

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

class Score {
    open class Score(
        @PrimaryKey var id: Long = 0,
        var name: String = "",
        var age :Int = 0
    ) : RealmObject()
    }
