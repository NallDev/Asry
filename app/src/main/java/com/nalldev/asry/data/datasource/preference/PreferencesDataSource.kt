package com.nalldev.asry.data.datasource.preference

import android.content.SharedPreferences
import com.google.gson.Gson
import com.nalldev.asry.domain.models.UserModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {

    companion object {
        private const val KEY_SESSION = "userSession"
    }

    fun putUserSession(user: UserModel): Completable {
        return Completable.fromAction {
            val jsonString = gson.toJson(user)
            sharedPreferences.edit().putString(KEY_SESSION, jsonString).apply()
        }
    }

    fun getUserSession(): Maybe<UserModel> {
        return Maybe.create { emitter ->
            val jsonString = sharedPreferences.getString(KEY_SESSION, null)
            if (jsonString != null) {
                val userModel = gson.fromJson(jsonString, UserModel::class.java)
                emitter.onSuccess(userModel)
            } else {
                emitter.onComplete()
            }
        }
    }

    fun removeUserSession(): Completable {
        return Completable.fromAction {
            sharedPreferences.edit().remove(KEY_SESSION).apply()
        }
    }
}