package sampleKotlin

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.functions.Function3
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class SampleKotlin5 {
    fun run1() {
        val o0 = Observable.range(0, 20)
            .map {
                Thread.sleep(500)
                println("${Thread.currentThread()} :: $it")
                it
            }

        o0.subscribeOn(Schedulers.computation())
            .subscribe {
                println("${Thread.currentThread()} :: $it")
            }

        Thread.sleep(10000)

        val o1 = Observable.range(0, 20)
            .map {
                Thread.sleep(500)
                println("${Thread.currentThread()} :: $it")
                it
            }

        o1.subscribeOn(Schedulers.newThread())
            .subscribe {
                println("${Thread.currentThread()} :: $it")
            }
        o1.subscribeOn(Schedulers.newThread())
            .subscribe {
            println("${Thread.currentThread()} :: $it")
        }
        o1.subscribeOn(Schedulers.newThread())
            .subscribe {
            println("${Thread.currentThread()} :: $it")
        }


        Thread.sleep(10000)
    }

    fun run2() {
        val o1 = Observable.just(1, 2, 3).subscribeOn(Schedulers.newThread())
        val o2 = Observable.just(1, 2, 3).subscribeOn(Schedulers.newThread())
        val o3 = Observable.just(1, 2, 3).subscribeOn(Schedulers.newThread())
        Observable.combineLatest(o1, o2, o3,
            Function3 { i1: Int, i2: Int, i3: Int ->
                "$i1 $i2 $i3"
            })
//            .observeOn(Schedulers.trampoline())
//            .subscribeOn(Schedulers.trampoline())
//            .subscribeOn(Schedulers.newThread())
            .subscribe { println(it) }
        Thread.sleep(1000)
    }

    fun run3() {
        val o1 = Observable.create<String> {
            println("Observable1 thread : ${Thread.currentThread().name}")
            it.onNext("")
        }
        o1.subscribe { println("Subscribe1 thread : ${Thread.currentThread().name}") }

        val o2 = Observable.create<String> {
            println("Observable2 thread : ${Thread.currentThread().name}")
            it.onNext("")
        }

        o2.observeOn(Schedulers.io())
            .subscribe { println("Subscribe2 thread : ${Thread.currentThread().name}") }


        val o3 = Observable.create<String> {
            println("Observable3 thread : ${Thread.currentThread().name}")
            it.onNext("")
        }.subscribeOn(Schedulers.io())
        o3.subscribe { println("Subscribe3 thread : ${Thread.currentThread().name}") }

        val o4 = Observable.create<String> {
            println("Observable4 thread : ${Thread.currentThread().name}")
            it.onNext("")
        }.observeOn(Schedulers.io())
        o4.subscribe { println("Subscribe4 thread : ${Thread.currentThread().name}") }

        val o5 = Observable.create<String> {
            println("Observable5 thread : ${Thread.currentThread().name}")
            it.onNext("")
        }.observeOn(Schedulers.io())
        o5.subscribeOn(Schedulers.trampoline())
            .subscribe { println("Subscribe5 thread : ${Thread.currentThread().name}") }

        val o6 = Observable.create<String> {
            println("Observable6 thread : ${Thread.currentThread().name}")
            it.onNext("")
        }.subscribeOn(Schedulers.trampoline())
        o6.observeOn(Schedulers.io())
            .subscribe { println("Subscribe6 thread : ${Thread.currentThread().name}") }


        Thread.sleep(1000)

    }

    fun run4() {
        val simple: (Int) -> Observable<String> = { num ->
            Observable.create {
                println("start")
                Thread.sleep(1000)
                it.onNext("Data $num")
                it.onComplete()
                println("finish")
            }
        }

        // 이건 순차적으로 동작한다.
        Observable.range(1, 5)
            .flatMap { it -> simple(it) }
            .subscribeOn(Schedulers.newThread())
            .subscribe { println(it) }

        Thread.sleep(6000)

        println("==============================")

        // 이건 병렬적으로 동작한다.
        Observable.range(1, 5)
            .flatMap { it -> simple(it).subscribeOn(Schedulers.newThread()) }
            .subscribe { println(it) }

        Thread.sleep(3000)
    }

    fun run5() {
        log("Starting")
        val o = simple()
        log("create")
        o
            .doOnNext { log("Found 1: $it") }
            .observeOn(Schedulers.io())
            .doOnNext { log("Found 2: $it") }
            .observeOn(Schedulers.computation())
            .doOnNext { log("Found 3: $it") }
            .subscribeOn(Schedulers.newThread())
            .subscribe {
                log("Got 1: $it")
            }

        Thread.sleep(1000)
    }

    fun simple() : Observable<String> {
        return Observable.create {
            log("Subscribed")
            it.onNext("A")
            it.onNext("B")
            it.onComplete()
        }
    }

    var offset = System.currentTimeMillis()
    fun log(message : String) {
        println("${String.format("%-4d", System.currentTimeMillis() - offset)}|" +
                " ${Thread.currentThread().name} |" +
                " $message")
    }
}