package sample1;

import io.reactivex.rxjava3.core.Observable;

public class Sample1 {

    public void run1() {
//                 Observable.just("Hello RxJava!").subscribe(System.out::println);

        Observable.create(s -> {
            s.onNext("Hello RxJava!");
            s.onComplete();
        }).subscribe(hello -> System.out.println(hello));
    }

    public void run2() {
        Observable<Integer> o = Observable.create(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onComplete();
        });
        o.map(i -> "Number " + i)
                .subscribe(s -> System.out.println(s));
    }

}
