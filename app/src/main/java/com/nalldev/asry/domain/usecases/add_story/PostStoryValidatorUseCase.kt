package com.nalldev.asry.domain.usecases.add_story

import com.google.android.gms.maps.model.LatLng
import com.nalldev.asry.util.Optional
import io.reactivex.rxjava3.core.Observable

class PostStoryValidatorUseCase {
    operator fun invoke(
        descriptionSubject: Observable<String>,
        useLocationSubject: Observable<Boolean>,
        coordinateSubject: Observable<Optional<LatLng>>
    ): Observable<Boolean> {
        return Observable.combineLatest(
            descriptionSubject.map { it.isNotEmpty() },
            useLocationSubject,
            coordinateSubject
        ) { isDescriptionValid, isUseLocation, coordinateOptional ->
            if (isUseLocation) {
                isDescriptionValid && coordinateOptional.value != null
            } else {
                isDescriptionValid
            }
        }
            .distinctUntilChanged()
    }
}