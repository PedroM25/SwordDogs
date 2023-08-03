package com.example.sworddogs

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.sworddogs.model.BreedResponse
import java.util.concurrent.atomic.AtomicBoolean

typealias ListOfBreeds = List<BreedResponse>

const val SPAN = 3
const val LOAD_THRESHOLD  = SPAN * 4

const val BUNDLE_BREED_NAME = "breedName"
const val BUNDLE_BREED_ORIGIN = "breedOrigin"
const val BUNDLE_BREED_TEMPERAMENT = "breedTemperament"
const val BUNDLE_BREED_GROUP = "breedGroup"

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer<T> { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    fun call() {
        postValue(null)
    }

}