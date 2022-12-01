/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.teis.ud2.exceptions;

/**
 *
 * @author maria
 */
public class InstanceNotFoundException extends Exception {

  
    private Object key;
    private String className;
    
    private static final String DEFAULT_MSG = "Instance not found";

    public InstanceNotFoundException( Object key,
            String className) {

        super(DEFAULT_MSG + " (key = '" + key + "' - className = '"
                + className + "')");
        this.key = key;
        this.className = className;

    }

    public Object getKey() {
        return key;
    }

    public String getClassName() {
        return className;
    }
}
