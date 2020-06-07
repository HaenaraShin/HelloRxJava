package sample1;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sample5 {
    public void run1() {
        Observable<BigInteger> factorials = Observable
                .range(2, 100)
                .scan(BigInteger.ONE, (big, cur) ->
                        big.multiply(BigInteger.valueOf(cur)));

        factorials.subscribe(System.out::println);
    }

    public void run2() {
        ArrayList<Integer> list = new ArrayList<>();
        Single<ArrayList<Integer>> all = Observable
                .range(10, 20)
                .collect(() -> list, (l, p) -> l.add(p));

        all.subscribe(System.out::println);
    }

    public void run3() {
        // scan은 누진계산시에 사용한다.
        Observable.range(0, 10)
                .doOnNext(i -> System.out.println("onNext :: " + i))
                .scan((sum, i) -> sum + i)
                .subscribe(System.out::println);

        // reduce는 누진계산하되 마지막만 뱉는다.
        Observable.range(0, 10)
                .doOnNext(i -> System.out.println("onNext :: " + i))
                .reduce((sum, i) -> sum + i)
                .subscribe(System.out::println);
    }

    public void run4() {
        // distinct는 이전까지 중복되지 않던 값만 받는다.
        Observable.create(s -> {
            Random random = new Random();
            while (!s.isDisposed()) {
                s.onNext(random.nextInt(100));
            }
        })
//                .doOnNext(i -> System.out.println("onNext :: " + i))
                .distinct()
                .take(10)
                .subscribe(System.out::println);

        Observable<Foo> o = Observable.create(s -> {
            s.onNext(new Foo("123", 123));
            s.onNext(new Foo("123", 456));
            s.onNext(new Foo("111", 123));
            s.onNext(new Foo("112", 456));
            s.onComplete();
        });
//                .distinct()
                o.distinct(f -> f.id)
                        .subscribe(f -> System.out.println(f.id + " " + f.value));
    }



    class Foo {
        public Foo(String id, int value){
            this.id = id;
            this.value = value;
        }
        public String id;
        public int value;
    }

    public void run5() {
        Observable<Integer> numbers = Observable.range(1, 5);
        numbers.all(x -> x != 4).subscribe(System.out::println); // all() 모두가 조건 성립?
        numbers.any(x -> x != 4).subscribe(System.out::println); // any() 하나라도 조건 성립? ==> exist 에서 변경됨.
        numbers.contains(4).subscribe(System.out::println); // contains() 하나라도 동일값?
    }

}
