package sampleKotlin

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class SampleKotlin3 {
    fun run1() {
        val o = Observable.range(0, 10)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.newThread())

        o.subscribe { println(it) }
        o.subscribe { println(it) }
        o.subscribe { println(it) }
        o.subscribe { println(it) }
        o.subscribe { println(it) }

        try { Thread.sleep(1000) } catch (e :InterruptedException) { }
    }
}