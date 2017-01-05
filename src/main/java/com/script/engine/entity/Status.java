package com.script.engine.entity;

public enum Status {
  IN_PROCESS(1, "In Process"),
  INTERRUPTED(2, "Interrupted"),
  COMPLETED(3, "Completed"),
  CANCELLED(4, "Cancelled"),
  DELETED(5, "Deleted"),
  ERROR(6, "Error");

  private final int id;
  private final String description;

  Status(int id, String description) {
      this.id = id;
      this.description = description;
  }
}
