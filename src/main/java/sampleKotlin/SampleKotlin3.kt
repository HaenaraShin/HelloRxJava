package sampleKotlin

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
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

        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
        }
    }

    fun run2() {
        Observable.range(2, 8)
            .flatMap(
                { Observable.range(1, 9) },
                { num, i -> "$num x $i = ${num * i}" })
            .subscribe(System.out::println)
    }

    fun run3() {
        Observable.create<String> { s ->
            s.onNext("Hello")
            s.onNext("Rx")
            s.onNext("Hello")
            s.onNext("Kidsnote")
            s.onComplete()
        }.subscribe { hello: String -> println(hello) }
    }
}