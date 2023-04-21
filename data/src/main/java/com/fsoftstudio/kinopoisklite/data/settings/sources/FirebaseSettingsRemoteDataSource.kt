/**
 * Copyright (C) 2023 Anatoliy Ferin - Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fsoftstudio.kinopoisklite.data.settings.sources

import com.fsoftstudio.kinopoisklite.common.entity.Const.APP_DATA_NODE
import com.fsoftstudio.kinopoisklite.data.settings.entity.FirebaseSettingsDataEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseSettingsRemoteDataSource @Inject constructor(
    private val db: DatabaseReference
) : SettingsRemoteDataSource {

    override fun getAppData(): Flow<FirebaseSettingsDataEntity?> = callbackFlow {

        db.child(APP_DATA_NODE).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(FirebaseSettingsDataEntity::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(FirebaseSettingsDataEntity(errorMessage = error.message))
            }
        })
        awaitClose()
    }
}