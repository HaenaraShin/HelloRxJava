package sample1;

import io.reactivex.rxjava3.core.Observable;

import java.util.ArrayList;
import java.util.List;

public class Sample4 {
    public void run1() {
        Observable.range(0, 10)
                .filter(i -> i % 2 == 0)
                .subscribe(System.out::println);
    }

    public void run2(){
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

    private List<Integer> createList(int start, int size) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(start + i);
        }
        return list;
    }
}
