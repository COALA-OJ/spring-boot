package org.coala.domain;

import java.util.LinkedList;
import java.util.Queue;

import com.google.gson.JsonObject;

import lombok.Data;

@Data
public class Engine {
	Boolean server[] = new Boolean[6];
	Queue<JsonObject> waitQueue = new LinkedList<>();

}
