package top.kwseeker.developkit.reactive.callback;

public interface Callback<T> {

    void onSuccess(T data);

    void onError(Throwable error);
}
