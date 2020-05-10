package sample1;

import io.reactivex.rxjava3.core.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.reactivex.rxjava3.core.Observable.just;

public class Sample4 {
    public void run1() {
        Observable.range(0, 10)
                .filter(i -> i % 2 == 0)
                .subscribe(System.out::println);
    }

    public void run2() {
        Observable<List<Integer>> o = Observable.create(it -> {
                    it.onNext(createList(0, 3));
                    it.onNext(createList(3, 3));
                    it.onNext(createList(6, 3));
                    it.onComplete();
                }
        );
        Observable o2 = o.flatMapIterable(list -> {
            return list;
        });
        o2.subscribe(System.out::println);

    }

    public void run3() {
        System.out.println("순서 상관없이 출력");
        just(10L, 1L)
                .flatMap(x ->
                        just(x).delay(x, TimeUnit.SECONDS))
                .subscribe(System.out::println);

        try {
            Thread.sleep(11 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("순서 대로 출력");
        just(10L, 1L)
                .concatMap(x ->
                        just(x).delay(x, TimeUnit.SECONDS))
                .subscribe(System.out::println);

        try {
            Thread.sleep(11 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run4() {
        Observable<Integer> o1 = Observable.create(i -> {
            i.onNext(1);
            i.onNext(2);
            i.onComplete();
        });

        Observable<Integer> o2 = Observable.create(i -> {
            i.onNext(3);
            i.onComplete();
        });

        Observable<Integer> o3 = Observable.create(i -> {
            i.onNext(4);
            i.onNext(5);
            i.onNext(6);
            i.onComplete();
        });

        Observable<Integer> o = Observable.merge(o1, o2, o3);
        o.subscribe(i -> {
                    System.out.println("onNext " + i);
                },
                err -> {
                    System.err.println("onError " + err.getLocalizedMessage());
                },
                () -> {
                    System.out.println("COMPLETE!!");
                }
        );
    }

    public void run5() {
        Observable<Integer> o1 = Observable.create(i -> {
            i.onNext(1);
            i.onNext(2);
            i.onError(new Exception("error")); // 여기서 멈추고 이후 onNext는 merge된 곳에서 무시한다.
            i.onComplete();
        });

        Observable<Integer> o2 = Observable.create(i -> {
            i.onNext(3);
            i.onComplete();
        });

        Observable<Integer> o3 = Observable.create(i -> {
            i.onNext(4);
            i.onNext(5);
            i.onNext(6);
            i.onComplete();
        });

        Observable<Integer> o = Observable.merge(o1, o2, o3);
        o.subscribe(i -> {
                    System.out.println("onNext " + i);
                },
                err -> {
                    System.err.println("onError " + err.getLocalizedMessage());
                },
                () -> {
                    System.out.println("COMPLETE!!");
                }
        );
    }

    public void run6() {
        Observable<Integer> o1 = Observable.create(i -> {
            i.onNext(1);
            i.onNext(2);
            i.onComplete();
        });

        Observable<String> o2 = Observable.create(i -> {
            i.onNext("3");
            i.onComplete();
        });

        Observable<Integer> o3 = Observable.create(i -> {
            i.onNext(4);
            i.onNext(5);
            i.onNext(6);
            i.onComplete();
        });

        Observable<Integer> o = Observable.merge(o1, o2.map(Integer::parseInt), o3);
        o.subscribe(i -> {
                    System.out.println("onNext " + i);
                },
                err -> {
                    System.err.println("onError " + err.getLocalizedMessage());
                },
                () -> {
                    System.out.println("COMPLETE!!");
                }
        );
    }

    public void run7() {
        // Single 이래서 안된댄다.. 대체 뭐지
//        Observable<List<Integer>> all = Observable
//                .range(10, 20)
//                .collect(ArrayList::new, List::add);
//        all.subscribe(System.out::println);

    }

    private List<Integer> createList(int start, int size) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(start + i);
        }
        return list;
    }
}
