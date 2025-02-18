package fractal.semantics;

import fractal.sys.FractalUnboundException;
import java.util.*;
import fractal.values.FractalValue;

/**
 * An instance of class <code>Environment<T></code> maintains a
 * collection of bindings from valid identifiers to values of type T.
 * It supports storing and retrieving bindings, just as would
 * be expected in any dictionary.
 *
 * @author <a href="mailto:daniel.coore@uwimona.edu.jm">Daniel Coore</a>
 * @version 1.0
 * @param <T> The type of values stored in this environment.
 */
public class Environment<T> {

    HashMap<String, T> dictionary;		// local bindings
    Environment<T> parent;		// parent environment
    // HashMap<String, Function> functionTable;
    
    /**
     * Create a new (empty) top level Environment.
     *
     */
    public Environment() {
        this(null);
    }

    /**
     * Create a new environment with an empty first frame, inheriting the
     * bindings of the given environment.
     *
     * @param p The parent environment of the new environment
     */
    public Environment(Environment<T> p) {
	dictionary = new HashMap<>();
	parent = p;
        // functionTable = new HashMap<>();
    }

    /**
     * Creates a new <code>Environment</code> instance that is initialised with
     * the given collection of bindings (presented as separate arrays of names 
     * and values).
     *
     * @param ids the collection of identifiers to be bound.
     * @param values the corresponding collection of values
     * for the identifiers.  Note that the two arrays must
     * have the same length.
     * @param parent The parent of this environment 
     */
    public Environment(String[] ids, T[] values, Environment<T> parent) {
	this(parent);
	for (int i = 0; i < ids.length; i++) {
	    dictionary.put(ids[i], values[i]);
	}
    }
    
    /**
     * Creates a new <code>Environment</code> instance that is initialised with
     * the given collection of bindings (presented as separate arrays of names 
     * and values).
     *
     * @param ids the collection of identifiers to be bound.
     * @param values the corresponding collection of values for the identifiers.
     * Note that the two array lists must have the same size.
     * @param parent The parent of this environment 
     */
    public Environment(ArrayList<String> ids, ArrayList<T> values, 
            Environment<T> parent) {
        this(parent);
	
	for (int i = 0; i < ids.size(); i++) {
	    dictionary.put(ids.get(i), values.get(i));
	}
    }

    /*
    public void putFun(String name, Function f) {
        functionTable.put(name, f);
    }

    public Function getFun(String name) throws FractalUnboundException {
        Function result = functionTable.get(name);
        if (result == null) {
            if (parent == null) {
                throw new FractalUnboundException("Undefined function " + name);
            } else {
                return parent.getFun(name);
            }
        } else {
            return result;
        }
    }
    */

    /**
     * Store a binding for the given identifier to the given
     * int within this environment.
     *
     * @param id the name to be bound
     * @param value the value to which the name is bound.
     */
    public void put(String id, T value) {
	dictionary.put(id, value);
    }

    /**
     * Return the int associated with the given identifier.
     *
     * @param id the identifier.
     * @return the int associated with the identifier in
     * this environment.
     * @exception FractalUnboundException if <code>id</code> is unbound
     */
    public T get(String id) throws FractalUnboundException {
	T result = dictionary.get(id);
	if (result == null)
	    if (parent == null)
		throw new FractalUnboundException("Unbound variable " + id);
	    else
		return parent.get(id);
	else
	    return result;
    }

    /**
     * Create a string representation of this environment.
     *
     * @return a string of all the names bound in this
     *         environment.
     */
    @Override
    public String toString() {
	StringBuffer result = new StringBuffer();
        result.append("[");

        for (String key : dictionary.keySet()) {
            result = result.append(key);
            result.append(" ");
        }
        
        result.append("]");
	return result.toString();
    }

}
