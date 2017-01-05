package com.script.engine.service;

import com.script.engine.entity.Script;

import java.util.List;

public interface ScriptEngineService {
  Script execute(String scriptBody, String userId, boolean blocking);
  Script cancel(long id);
  Script delete(long id);
  List<Script> cancelAllByUser(String userId);
  List<Script> deleteAllByUser(String userId);
  Script getScript(long id);
  List<Script> getScripts();

}
