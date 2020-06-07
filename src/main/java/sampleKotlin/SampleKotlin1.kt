package sampleKotlin

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

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

    fun run3() {
        val o = object : Observer<String>{
            override fun onComplete() { println("onComplete") }
            override fun onSubscribe(d: Disposable?) { println("onSubscribe") }
            override fun onNext(t: String?) { println(t) }
            override fun onError(e: Throwable?) { }
        }

        o.onNext("Hello")
        o.onNext("RxKotlin")
        o.onNext("Observer with manually.")
        o.onComplete()
    }
}