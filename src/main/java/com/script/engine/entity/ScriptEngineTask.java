package com.script.engine.entity;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ScriptEngineTask {
  private final StringWriter stringWriter = new StringWriter();
  private final StringWriter stringErrorWriter = new StringWriter();
  private final ScriptContext scriptContext = new SimpleScriptContext();

  private final Script script;
  private final String userId;
  private final ScriptEngine scriptEngine;
  private Thread thread;

  public ScriptEngineTask(Script script, String userId, ScriptEngine scriptEngine) {
    this.script = script;
    this.userId = userId;
    this.scriptEngine = scriptEngine;
  }

  public Thread execute() {
    thread = new Thread(() -> {
      try (PrintWriter printWriter = new PrintWriter(stringWriter);
           PrintWriter printErrorWriter = new PrintWriter(stringErrorWriter);) {
        scriptContext.setWriter(printWriter);
        scriptContext.setErrorWriter(printErrorWriter);
        scriptEngine.eval(script.getBody(), scriptContext);
        script.setResult(getResult());
        script.setStatus(Status.COMPLETED);
      } catch (ScriptException e) {
        script.setError(getError());
        script.setStatus(Status.ERROR);
        e.printStackTrace();
      }
    });
    thread.start();
    return thread;
  }

  public void stop() {
    script.setResult(getResult());
    script.setError(getError());
    if (thread != null && script.getStatus() == Status.IN_PROCESS) {
      thread.stop();
      script.setStatus(Status.CANCELLED);
    }
  }

  public Script getScript() {
    return script;
  }

  public String getResult() {
    return stringWriter.toString();
  }

  public String getError() {
    return stringErrorWriter.toString();
  }

  public Long getId() {
    return script.getId();
  }

  public String getUserId() {
    return userId;
  }

}
