package sample1;

import io.reactivex.rxjava3.core.Observable;

public class Sample2 {
    public void run1() {


        // 비동기 Rx 예시 실행결과 :
//        Number 300
//        Number 200
//        Number 100

        Observable<Integer> o = Observable.create(s->{
            Callback c = i -> {
                try {  Thread.sleep(500 - i); } catch (InterruptedException e) { }
                s.onNext(i);
            };
            new Thread(() -> c.onCallback(100) ).start();
            new Thread(() -> c.onCallback(200) ).start();
            new Thread(() -> c.onCallback(300) ).start();
        });
        o.map(i -> "Number " + i)
                .subscribe(s -> System.out.println(s));
        // 이렇게 하지 말 것
    }

    public void run2() {
        Observable<String> a = Observable.create(s -> {
            new Thread(() -> {
                s.onNext("one");
                s.onNext("two");
                s.onComplete();
            }).start();
        });

        Observable<String> b = Observable.create(s->{
            new Thread(()-> {
                s.onNext("three");
                s.onNext("four");
                s.onComplete();
            }).start();
        });

        // 동시에 a와 b를 구독하여 제3의 순차적인 스트림으로 병합한다.
        Observable<String> c = Observable.merge(a, b);
        c.subscribe(System.out::println);
    }
}


