package frc.robot.util.stateMachine;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.HashMap;

public class StateMachine {

  private HashMap<String, Method> states;
  private HashMap<String, Method> runStates;
  private String state = "";

  public StateMachine(String defaultState) {
    states = new HashMap<String, Method>();
    runStates = new HashMap<String, Method>();
    Method[] methods= this.getClass().getMethods(); //obtain all method objects
    for(Method method : methods){
      Annotation[] annotations = method.getDeclaredAnnotations();
      for(Annotation annotation : annotations){
        // Gathers each method that has InitState annotations
        if(annotation instanceof InitState){
          InitState myAnnotation = (InitState) annotation;
          //System.out.println("state name: " + myAnnotation.name());
          states.put(myAnnotation.name(), method);
        }
        // Gathers each method that has RunState annotations
        if(annotation instanceof RunState){
          RunState myAnnotation = (RunState) annotation;
          //System.out.println("endState name: " + myAnnotation.name());
          runStates.put(myAnnotation.name(), method);
        }
      }
    }
    setState(defaultState);
  }

  /**
  * Calls the function associated with the current state.
  * <p>Run this method periodically.</p>
  */
  public void run(){
    String name = this.state;
    runState(name);
  }

  /**
  * Sets the current state.
  * @param name : String <i>name of the state</i>
  */
  public void setState(String name){
    state = name;
    initState(name);
  }

  public void initState(String name){
    try {
      Object returnObject = states.get(name).invoke(this);
    } catch (Exception e){
      System.out.println(e + ": state-> " + name);
    }
  }

  public void runState(String name){
    try {
      Object returnObject = runStates.get(name).invoke(this);
    } catch (Exception e){
      System.out.println(e + ": runState-> " + name);
    }
  }
}