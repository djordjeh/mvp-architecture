package djordjeh.architecture.mvp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(var id: Long = 0, var title: String? = null, var description: String? = null, var completed: Boolean = false) : Parcelable
