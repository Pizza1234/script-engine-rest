package com.script.engine.controller;

import com.script.engine.entity.Script;
import com.script.engine.service.ScriptEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping(value = "/scripts")
public class ScriptController {
  @Autowired
  private ScriptEngineService scriptEngineService;

  @RequestMapping(method = RequestMethod.POST)
  public Script postScript(@RequestBody String scriptBody,
                           @RequestParam(value = "blocking", defaultValue = "true") boolean blocking,
                           HttpServletResponse response,
                           HttpServletRequest request) {
    if (!blocking) {
      response.setStatus(HttpStatus.ACCEPTED.value());
    }

    return scriptEngineService.execute(scriptBody, request.getHeader("UserID"), blocking);
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<Script> getScripts() {
    return scriptEngineService.getScripts();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public Script getScript(@PathVariable("id") Long id) {
    return  scriptEngineService.getScript(id);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public Script cancelScript(@PathVariable("id") Long id,
                             @RequestParam(value = "remove", defaultValue = "false") boolean remove) {
    if (!remove) {
      return scriptEngineService.cancel(id);
    }

    return scriptEngineService.delete(id);
  }

  @RequestMapping(method = RequestMethod.DELETE)
  public List<Script> disconnect(@RequestParam(value = "remove", defaultValue = "false") boolean remove,
                                 HttpServletRequest request) {
    if (!remove) {
      return scriptEngineService.cancelAllByUser(request.getHeader("UserID"));
    }

    return scriptEngineService.deleteAllByUser(request.getHeader("UserID"));
  }

}