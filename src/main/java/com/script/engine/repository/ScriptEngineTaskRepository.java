package com.script.engine.repository;

import com.script.engine.entity.ScriptEngineTask;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class ScriptEngineTaskRepository {
  private final Map<Long, ScriptEngineTask> map = new ConcurrentHashMap<>();

  public List<ScriptEngineTask> findAll() {
    return new ArrayList<>(map.values());
  }

  public List<ScriptEngineTask> findAllByUser(String userId) {
    return findAll().stream()
        .filter(task -> userId.equals(task.getUserId()))
        .collect(Collectors.toList());
  }

  public ScriptEngineTask find(long id) {
    return map.get(id);
  }

  public ScriptEngineTask save(ScriptEngineTask scriptEngineTask) {
    return map.put(scriptEngineTask.getId(), scriptEngineTask);
  }

  public ScriptEngineTask delete(long id) {
    return map.remove(id);
  }

}
