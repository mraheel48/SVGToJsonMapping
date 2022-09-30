package com.example.svgtojsonmapping.dataModel

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()

data class SVGModel(
    val svg: SVG? = null
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<SVGModel>(json)
    }
}

data class SVG(
    val rect: Rect? = null,
    val text: Text? = null,
    val path: List<Path>? = null,

    @Json(name = "_xmlns")
    val xmlns: String? = null,

    @Json(name = "_id")
    val id: String? = null,

    @Json(name = "_data-name")
    val dataName: String? = null,

    @Json(name = "_viewBox")
    val viewBox: String? = null
)

data class Path(
    @Json(name = "_d")
    val d: String? = null,

    @Json(name = "_style")
    val style: String? = null
)

data class Rect(
    @Json(name = "_width")
    val width: String? = null,

    @Json(name = "_height")
    val height: String? = null,

    @Json(name = "_style")
    val style: String? = null
)

data class Text(
    @Json(name = "_transform")
    val transform: String? = null,

    @Json(name = "_style")
    val style: String? = null,

    @Json(name = "__text")
    val text: String? = null
)
