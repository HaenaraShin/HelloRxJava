package sampleKotlin

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.RuntimeException

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

    fun run4() {
        val o1 = Observable.create<String> {
            it.onNext("hello")
            it.onNext("Rx")
            it.onComplete()
        }

        val o2 = Observable.create<String> {
            it.onNext("hello")
            it.onNext("Error")
            it.onError(RuntimeException("Runtime Exception"))
        }

        o1.subscribe(
            {it -> println(it)},
            {e -> println(e.localizedMessage)},
            { println("complete")})

        o2.subscribe(
            {it -> println(it)},
            {e -> println(e.message)},
            { println("complete")})
    }

    fun run5() {
        // fromCallabe 사용하는 방법
        Observable.fromCallable { 1 / 0 }
            .subscribe(
                { println(it) },
                { e -> println(e.message) }
            )

    }
}