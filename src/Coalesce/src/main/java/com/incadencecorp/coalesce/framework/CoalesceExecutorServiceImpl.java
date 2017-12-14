/*-----------------------------------------------------------------------------'
 Copyright 2017 - InCadence Strategic Solutions Inc., All Rights Reserved

 Notwithstanding any contractor copyright notice, the Government has Unlimited
 Rights in this work as defined by DFARS 252.227-7013 and 252.227-7014.  Use
 of this work other than as specifically authorized by these DFARS Clauses may
 violate Government rights in this work.

 DFARS Clause reference: 252.227-7013 (a)(16) and 252.227-7014 (a)(16)
 Unlimited Rights. The Government has the right to use, modify, reproduce,
 perform, display, release or disclose this computer software and to have or
 authorize others to do so.

 Distribution Statement D. Distribution authorized to the Department of
 Defense and U.S. DoD contractors only in support of U.S. DoD efforts.
 -----------------------------------------------------------------------------*/

package com.incadencecorp.coalesce.framework;

import com.incadencecorp.coalesce.api.ICoalesceComponent;
import com.incadencecorp.coalesce.api.persistance.ICoalesceExecutorService;
import com.incadencecorp.coalesce.common.exceptions.CoalescePersistorException;
import com.incadencecorp.coalesce.framework.jobs.AbstractCoalesceJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * This base implementation manages the {@link ExecutorService} used by extending classes.
 *
 * @author Derek Clemenzi
 */
public class CoalesceExecutorServiceImpl implements ICoalesceExecutorService, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoalesceExecutorServiceImpl.class);

    private ExecutorService _pool;

    public CoalesceExecutorServiceImpl(ExecutorService service)
    {
        if (service == null)
        {
            LOGGER.info("Using Default ExecutorService; Cores: {} Max Threads / Core: {}",
                        CoalesceSettings.getNumberOfCores(),
                        CoalesceSettings.getMaxThreadsPerCore());

            service = new ThreadPoolExecutor(CoalesceSettings.getMinThreads(),
                                             CoalesceSettings.getMaxThreads(),
                                             CoalesceSettings.getKeepAliveTime(),
                                             TimeUnit.SECONDS,
                                             new SynchronousQueue<>(),
                                             new CoalesceThreadFactoryImpl(),
                                             new ThreadPoolExecutor.CallerRunsPolicy());
        }

        _pool = service;

        if (LOGGER.isInfoEnabled())
        {
            LOGGER.info("Executor Service ({})", service.getClass().getName());
        }

        ShutdownAutoCloseable.createShutdownHook(this);
    }

    @Override
    public final void execute(Runnable command)
    {
        _pool.execute(command);
    }

    @Override
    public final boolean isShutdown()
    {
        return _pool.isShutdown();
    }

    @Override
    public final boolean isTerminated()
    {
        return _pool.isTerminated();
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) throws CoalescePersistorException
    {
        return _pool.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException
    {
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("Invoking ({}) Tasks", tasks.size());
        }

        if (_pool.isShutdown())
        {
            LOGGER.warn("(FAILED) Invoking Tasks: Pool is Shutdown");
            return null;
        }
        else
        {
            return _pool.invokeAll(tasks);
        }
    }

    @Override
    public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException
    {
        return _pool.invokeAll(tasks, timeout, unit);
    }

    @Override
    public final <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
    {
        return _pool.invokeAny(tasks);
    }

    @Override
    public final <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException
    {
        return _pool.invokeAny(tasks, timeout, unit);
    }

    @Override
    public void close() throws Exception
    {
        if (!_pool.isShutdown())
        {
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("Closing {}", this.getClass().getSimpleName());
            }

            try
            {
                // Graceful shutdown (Allow 1 minute for jobs to finish)
                _pool.shutdown();

                if (!_pool.awaitTermination(1, TimeUnit.MINUTES))
                {

                    LOGGER.warn("(FAILED) Graceful Pool Termination");

                    logFailedJobs(_pool.shutdownNow());

                    if (!_pool.awaitTermination(1, TimeUnit.MINUTES))
                    {
                        LOGGER.error(" (FAILED) Pool Terminate");
                    }
                }
            }
            catch (InterruptedException e)
            {

                LOGGER.warn("(FAILED) Graceful Pool Termination", e);

                logFailedJobs(_pool.shutdownNow());

                // Preserve Interrupt Status
                Thread.currentThread().interrupt();
            }
        }
    }

    private void logFailedJobs(List<Runnable> jobList)
    {
        for (Runnable runnable : jobList)
        {
            if (runnable instanceof AbstractCoalesceJob<?, ?, ?>)
            {
                LOGGER.warn("Job ({}) Expired ({})",
                            ((AbstractCoalesceJob) runnable).getName(),
                            ((AbstractCoalesceJob) runnable).getJobId());
            }
            else if (runnable instanceof ICoalesceComponent)
            {
                LOGGER.warn("Component Expired ({})", ((ICoalesceComponent) runnable).getName());
            }
            else
            {
                LOGGER.warn("Runnable Expired ({})", runnable.getClass().getName());
            }
        }
    }

}
