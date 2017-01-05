package com.script.engine.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Script {

  private long id;
  private Status status;
  private String body;
  private String result;
  private String error;

  public Script() {}

  public Script(long id, String body) {
    this.id = id;
    this.body = body;
    this.status = Status.IN_PROCESS;
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

  }

  public Script(Script script, String result, String error) {
    this.id = script.id;
    this.body = script.body;
    this.status = script.status;
    this.result = result;
    this.error = error;
  }

  public long getId() {
    return id;
  }

  public String getBody() {
    return body;
  }

  public void setId(long id) {
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
}