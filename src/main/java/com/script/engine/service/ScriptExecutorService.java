package com.script.engine.service;

import com.script.engine.entity.Script;
import com.script.engine.entity.Status;
import com.script.engine.repository.ScriptEngineTaskRepository;
import com.script.engine.entity.ScriptEngineTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ScriptExecutorService implements ScriptEngineService{
  private static final Logger LOGGER = LoggerFactory.getLogger(ScriptExecutorService.class);
  private final ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
  private final AtomicLong counter = new AtomicLong();
  private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

  @Autowired
  private ScriptEngineTaskRepository scriptEngineTaskRepository;

  public Script execute(String scriptBody, String userId, boolean blocking) {
    Script script = new Script(counter.incrementAndGet(), scriptBody);
    ScriptEngineTask scriptEngineTask = new ScriptEngineTask(script, userId, scriptEngine);
    Thread t = scriptEngineTask.execute();
    scriptEngineTaskRepository.save(scriptEngineTask);
    cachedThreadPool.execute(runLogging(scriptEngineTask));

    if (!blocking) {
      return script;
    }

    try {
      t.join();
    } catch (InterruptedException e) {
      script.setStatus(Status.INTERRUPTED);
      script.setResult(script.getResult());
      script.setError(script.getError());
      e.printStackTrace();
    }

    return script;
  }

  private Runnable runLogging(ScriptEngineTask scriptEngineTask) {
    return () -> {
      while(scriptEngineTask.getScript().getStatus() == Status.IN_PROCESS) {
        try {
          LOGGER.info("Task id: " + scriptEngineTask.getId() + " " + scriptEngineTask.getResult());
          Thread.sleep(2000);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      LOGGER.info("Task id: " + scriptEngineTask.getId() + " " + scriptEngineTask.getResult());
    };
  }

  public Script delete(long id) {
    Script script = cancel(id);
    scriptEngineTaskRepository.delete(id);
    script.setStatus(Status.DELETED);
    return script;
  }

  public List<Script> deleteAllByUser(String userId) {
    return scriptEngineTaskRepository.findAllByUser(userId).stream()
        .map(task -> delete(task.getId()))
        .collect(Collectors.toList());
  }

  public Script cancel(long id) {
    ScriptEngineTask scriptEngineTask = scriptEngineTaskRepository.find(id);
    Optional.ofNullable(scriptEngineTask)
        .ifPresent(task -> task.stop());
    return Optional.ofNullable(scriptEngineTask)
        .map(task -> new Script(task.getScript(), task.getResult(), task.getError()))
        .orElse(new Script("", "No script with id: " + id));
  }

  @Override
  public List<Script> cancelAllByUser(String userId) {
    return scriptEngineTaskRepository.findAllByUser(userId).stream()
        .filter(task -> task.getScript().getStatus() == Status.IN_PROCESS)
        .map(task -> cancel(task.getId()))
        .collect(Collectors.toList());
  }

  public Script getScript(long id) {
    return Optional.ofNullable(scriptEngineTaskRepository.find(id))
        .map(task -> new Script(task.getScript(), task.getResult(), task.getError()))
        .orElse(new Script("", "No script with id: " + id));
  }

  public List<Script> getScripts() {
    return scriptEngineTaskRepository.findAll().stream()
        .map(task -> new Script(task.getScript(), task.getResult(), task.getError()))
        .collect(Collectors.toList());
  }

}
