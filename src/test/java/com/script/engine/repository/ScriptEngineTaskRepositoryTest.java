package com.script.engine.repository;

import com.script.engine.entity.Script;
import com.script.engine.entity.ScriptEngineTask;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScriptEngineTaskRepositoryTest {
  Map<Long, ScriptEngineTask> mockmap;
  private ScriptEngineTaskRepository scriptEngineTaskRepository = new ScriptEngineTaskRepository();

  @Before
  public void setUp() throws Exception {
    String localHost = "http://localhost:8080";

    HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
    when(httpServletRequestMock.getRequestURL()).thenReturn(new StringBuffer(localHost));
    when(httpServletRequestMock.getHeaderNames()).thenReturn(Collections.emptyEnumeration());
    when(httpServletRequestMock.getRequestURI()).thenReturn(localHost);
    when(httpServletRequestMock.getContextPath()).thenReturn(StringUtils.EMPTY);
    when(httpServletRequestMock.getServletPath()).thenReturn(StringUtils.EMPTY);
    ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(httpServletRequestMock);
    RequestContextHolder.setRequestAttributes(servletRequestAttributes);

    mockmap = new ConcurrentHashMap<>();
    mockmap.put(1L, new ScriptEngineTask(new Script(1L, ""), "User1", null));
    mockmap.put(2L, new ScriptEngineTask(new Script(2L, ""), "User1", null));
    mockmap.put(3L, new ScriptEngineTask(new Script(3L, ""), "User2", null));

    ReflectionTestUtils.setField(scriptEngineTaskRepository, "map", mockmap);
  }

  @Test
  public void shouldFindAllTasks() throws Exception {
    List<ScriptEngineTask> scriptEngineTasks = scriptEngineTaskRepository.findAll();
    assertEquals("Should find 3 tasks", 3, scriptEngineTasks.size());
  }

  @Test
  public void shouldFindAllTasksByUser() throws Exception {
    List<ScriptEngineTask> scriptEngineTasks = scriptEngineTaskRepository.findAllByUser("User1");
    assertEquals("Should find 2 tasks", 2, scriptEngineTasks.size());
  }

  @Test
  public void shouldFindTaskById() throws Exception {
    ScriptEngineTask scriptEngineTask = scriptEngineTaskRepository.find(3L);
    assertEquals("Should find 1 task", "User2", scriptEngineTask.getUserId());
  }

  @Test
  public void shouldSaveTask() throws Exception {
    ScriptEngineTask task = new ScriptEngineTask(new Script(4L, ""), "User4", null);
    ScriptEngineTask scriptEngineTask = scriptEngineTaskRepository.save(task);
    assertEquals("Should save 1 task", 4, mockmap.size());
  }

  @Test
  public void shouldDeleteTask() throws Exception {
    ScriptEngineTask scriptEngineTask = scriptEngineTaskRepository.delete(3L);
    assertEquals("Should delete 1 task", 2, mockmap.size());
  }

}