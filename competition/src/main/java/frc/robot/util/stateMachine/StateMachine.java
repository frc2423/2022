package frc.robot.util.stateMachine;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.HashMap;

public class StateMachine {

  private HashMap<String, Method> initStates;
  private HashMap<String, Method> runStates;
  protected String state = "";

  public StateMachine(String defaultState) {
    initStates = new HashMap<String, Method>();
    runStates = new HashMap<String, Method>();
    Method[] methods= this.getClass().getMethods(); //obtain all method objects
    for(Method method : methods){
      Annotation[] annotations = method.getDeclaredAnnotations();
      for(Annotation annotation : annotations){
        // Gathers each method that has InitState annotations
        if(annotation instanceof InitState){
          InitState myAnnotation = (InitState) annotation;
          //System.out.println("state name: " + myAnnotation.name());
          initStates.put(myAnnotation.name(), method);
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
      var initFunction = initStates.get(name);
      if (initFunction != null) {
        initFunction.invoke(this);
      }
    } catch (Exception e){
      System.out.println(e + ": initState-> " + name);
    }
  }

  public void runState(String name){
    try {
      Object returnObject = runStates.get(name).invoke(this);
    } catch (Exception e){
      System.out.println(e + ": runState-> " + name + " cause:"+ e.getCause());
    }
  }
}