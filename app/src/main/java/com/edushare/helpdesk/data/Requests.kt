package com.edushare.helpdesk.data

data class Requests(
    var id: String ?= null,
    var title:String ?= null,
    var subject:String ?= null,
    var response: String ?= null
)
