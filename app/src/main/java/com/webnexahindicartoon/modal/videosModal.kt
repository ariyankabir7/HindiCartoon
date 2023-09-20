package com.webnexahindicartoon.modal

import android.os.Parcel
import android.os.Parcelable

data class videosModal(
    val c_id: String,
    val c_image: String,
    val category_id: String,
    val category_name: String,
    val id: String,
    val image: String,
    val lan: String,
    val status: String,
    val tags: String,
    val title: String,
    val type: String,
    val upload_by: String,
    val video_link: String,
    val views: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(c_id)
        parcel.writeString(c_image)
        parcel.writeString(category_id)
        parcel.writeString(category_name)
        parcel.writeString(id)
        parcel.writeString(image)
        parcel.writeString(lan)
        parcel.writeString(status)
        parcel.writeString(tags)
        parcel.writeString(title)
        parcel.writeString(type)
        parcel.writeString(upload_by)
        parcel.writeString(video_link)
        parcel.writeString(views)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<videosModal> {
        override fun createFromParcel(parcel: Parcel): videosModal {
            return videosModal(parcel)
        }

        override fun newArray(size: Int): Array<videosModal?> {
            return arrayOfNulls(size)
        }
    }
}