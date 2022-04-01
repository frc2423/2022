package frc.robot.util.stateMachine;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.HashMap;

public class StateMachine {

  private HashMap<String, Method> states;
  private String state = "";
  private String defaultState;
  private StateContext ctx;

  public StateMachine(String defaultState) {
    this.defaultState = defaultState;
    states = new HashMap<String, Method>();
    Method[] methods= this.getClass().getMethods(); //obtain all method objects
    for(Method method : methods){
      Annotation[] annotations = method.getDeclaredAnnotations();
      for(Annotation annotation : annotations){
        // Gathers each method that has State annotations
        if(annotation instanceof State){
          State myAnnotation = (State) annotation;
          //System.out.println("endState name: " + myAnnotation.name());
          states.put(myAnnotation.name(), method);
        }
      }
    }
    setState(defaultState);
  }

  /**
   * Gets the current state of the state machine.
   */
  public String getState(){
    return this.state;
  }


  /**
  * Calls the function associated with the current state.
  * <p>Run this method periodically.</p>
  */
  public void run(){
    String name = this.state;
    runState(name);
    ctx.initialize();
  }

  /**
  * Sets the current state.
  * @param name : String <i>name of the state</i>
  */
  public void setState(String name){
    ctx = new StateContext();
    state = name;
  }

  public void runState(String name){
    try {
      Object returnObject = states.get(name).invoke(this);
    } catch (Exception e){
      System.out.println(e + ": State -> " + name + " cause:"+ e.getCause());
    }
  }

  public String getDefaultState(){
    return defaultState;
  }
}