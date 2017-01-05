package com.script.engine.controller;

import com.script.engine.entity.Script;
import com.script.engine.service.ScriptEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class ScriptController {
  @Autowired
  private ScriptEngineService scriptEngineService;

  @RequestMapping(value = "/scripts", method = RequestMethod.POST)
  public Script postScript(@RequestBody String scriptBody,
                           @RequestParam(value = "blocking", defaultValue = "true") boolean blocking,
                           HttpServletResponse response,
                           HttpServletRequest request) {
    if (!blocking) {
      response.setStatus(202);
    }

    return scriptEngineService.execute(scriptBody, request.getHeader("UserID"), blocking);
  }

  @RequestMapping(value = "/scripts", method = RequestMethod.GET)
  public List<Script> getScripts() {
    return scriptEngineService.getScripts();
  }

  @RequestMapping(value = "/scripts/{id}", method = RequestMethod.GET)
  public Script getScript(@PathVariable("id") long id) {
    return  scriptEngineService.getScript(id);
  }

  @RequestMapping(value = "/scripts/{id}", method = RequestMethod.DELETE)
  public Script cancelScript(@PathVariable("id") long id) {
    return scriptEngineService.cancel(id);
  }

  @RequestMapping(value = "/disconnect", method = RequestMethod.DELETE)
  public List<Script> disconnect(HttpServletRequest request) {
    return scriptEngineService.cancelAllByUser(request.getHeader("UserID"));
  }



}