package com.script.engine.entity;

import com.script.engine.controller.ScriptController;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.Link;
import org.springframework.util.Assert;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@XmlRootElement
public class Script implements Identifiable<Long> {

  private Long id;
  private Status status;
  private String body;
  private String result;
  private String error;
  private final List<Link> links = new ArrayList<>();

  public Script() {}

  public Script(Long id, String body) {
    this.id = id;
    this.body = body;
    this.status = Status.IN_PROCESS;
    links.add(linkTo(ScriptController.class).slash(id).withSelfRel());
  }

  public Script(String result, String error) {
    this.result = result;
    this.error = error;
  }

  public Script(Script script) {
    this.id = script.id;
    this.body = script.body;
    this.status = script.status;
    this.result = script.result;
    this.error = script.error;
    links.add(linkTo(ScriptController.class).slash(id).withSelfRel());

  }

  public Script(Script script, String result, String error) {
    this.id = script.id;
    this.body = script.body;
    this.status = script.status;
    this.result = result;
    this.error = error;
    links.add(linkTo(ScriptController.class).slash(id).withSelfRel());
  }

  public Long getId() {
    return id;
  }

  public String getBody() {
    return body;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public void add(Link link) {
    Assert.notNull(link, "Link must not be null!");
    this.links.add(link);
  }

  public List<Link> getLinks() {
    return links;
  }
}