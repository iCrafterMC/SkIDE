package com.skide

object Info {
    const val version = "2026.8"
    var prodMode = true
    var indpendentInstall = false
}

fun main(args: Array<String>) {
    CoreManager().bootstrap(args)
}
