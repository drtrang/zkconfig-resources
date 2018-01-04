package com.github.phantomthief.zookeeper;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.zookeeper.data.Stat;

import com.github.phantomthief.util.ThrowableBiFunction;
import com.github.phantomthief.util.ThrowableConsumer;
import com.github.phantomthief.util.ThrowableFunction;
import com.github.phantomthief.zookeeper.ZkBasedNodeResource.Builder;
import com.google.common.util.concurrent.ListeningExecutorService;

/**
 * @author w.vela
 * Created on 16/5/13.
 */
public class GenericZkBasedNodeBuilder<T> {

    private final Builder<Object> builder;

    GenericZkBasedNodeBuilder(Builder<Object> builder) {
        this.builder = builder;
    }

    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T>
            addFactoryFailedListener(@Nonnull ThrowableConsumer<Throwable, ?> listener) {
        builder.addFactoryFailedListener(listener);
        return this;
    }

    /**
     * use {@link #withFactoryEx}
     */
    @Deprecated
    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T> withFactory(BiFunction<byte[], Stat, ? extends T> factory) {
        return withFactoryEx(factory::apply);
    }

    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T>
            withFactoryEx(ThrowableBiFunction<byte[], Stat, ? extends T, Exception> factory) {
        builder.withFactoryEx(factory);
        return this;
    }

    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T> asyncRefresh(@Nonnull ListeningExecutorService executor) {
        builder.asyncRefresh(executor);
        return this;
    }

    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T>
            onResourceChange(BiConsumer<? super T, ? super T> callback) {
        builder.onResourceChange(callback);
        return this;
    }

    /**
     * use {@link #withFactoryEx}
     */
    @Deprecated
    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T> withFactory(Function<byte[], ? extends T> factory) {
        return withFactoryEx(factory::apply);
    }

    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T>
            withFactoryEx(ThrowableFunction<byte[], ? extends T, Exception> factory) {
        return withFactoryEx((b, s) -> factory.apply(b));
    }

    /**
     * use {@link #withStringFactoryEx}
     */
    @Deprecated
    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T>
            withStringFactory(BiFunction<String, Stat, ? extends T> factory) {
        return withStringFactoryEx(factory::apply);
    }

    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T>
            withStringFactoryEx(ThrowableBiFunction<String, Stat, ? extends T, Exception> factory) {
        return withFactoryEx((b, s) -> factory.apply(b == null ? null : new String(b), s));
    }

    /**
     * use {@link #withStringFactoryEx}
     */
    @Deprecated
    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T> withStringFactory(Function<String, ? extends T> factory) {
        return withStringFactoryEx(factory::apply);
    }

    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T>
            withStringFactoryEx(ThrowableFunction<String, ? extends T, Exception> factory) {
        return withStringFactoryEx((b, s) -> factory.apply(b));
    }

    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T> withCacheFactory(Supplier<NodeCache> cacheFactory) {
        builder.withCacheFactory(cacheFactory);
        return this;
    }

    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T> withCacheFactory(String path, CuratorFramework curator) {
        builder.withCacheFactory(path, curator);
        return this;
    }

    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T> withCacheFactory(String path,
            Supplier<CuratorFramework> curatorFactory) {
        builder.withCacheFactory(path, curatorFactory);
        return this;
    }

    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T>
            withCleanupConsumer(ThrowableConsumer<? super T, Throwable> cleanup) {
        builder.withCleanupConsumer(cleanup);
        return this;
    }

    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T> withCleanupPredicate(Predicate<? super T> cleanup) {
        builder.withCleanupPredicate(cleanup);
        return this;
    }

    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T> withWaitStopPeriod(long waitStopPeriod) {
        builder.withWaitStopPeriod(waitStopPeriod);
        return this;
    }

    @CheckReturnValue
    public GenericZkBasedNodeBuilder<T> withEmptyObject(T emptyObject) {
        builder.withEmptyObject(emptyObject);
        return this;
    }

    public ZkBasedNodeResource<T> build() {
        return builder.build();
    }
}
