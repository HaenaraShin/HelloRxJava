package sampleKotlin

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class SampleKotlin1 {
    fun run1() {
        Observable.create { s: ObservableEmitter<String> ->
            s.onNext("Hello RxJava!")
            s.onComplete()
        }.subscribe { hello: String -> println(hello) }
    }

    fun run2() {
        // let's make it more simple
        Observable.just("Hello RxJava!!")
            .subscribe { println(it) }
    }
}