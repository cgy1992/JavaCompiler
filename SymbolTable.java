package cop5556sp17;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;

import cop5556sp17.AST.Dec;
import cop5556sp17.TypeCheckVisitor.TypeCheckException;

public class SymbolTable {

	// TODO add fields
	int current_scope, next_scope;

	LinkedHashMap<Integer, Dec> mySymbolTableEntry;
	Stack<Integer> scope_stack;
	HashMap<String, LinkedHashMap<Integer,Dec>> mySymboltable;

	/**
	 * to be called when block entered
	 */
	public void enterScope() {
		current_scope = next_scope++;
		scope_stack.push(current_scope);
	}

	/**
	 * leaves scope
	 */
	public void leaveScope() {
		scope_stack.pop();
		if(!scope_stack.empty()){
		current_scope = scope_stack.peek();
		}
	}

	public boolean insert(String ident, Dec dec) {
		if (lookup(ident) == null) {
			mySymbolTableEntry = new LinkedHashMap<>();
			mySymbolTableEntry.put(current_scope, dec);
			mySymboltable.put(ident, mySymbolTableEntry);
		} else if (mySymboltable.containsKey(ident) && !mySymboltable.get(ident).containsKey(current_scope)
				&& !mySymbolTableEntry.containsValue(dec)) {
			mySymbolTableEntry = mySymboltable.get(ident);
			mySymbolTableEntry.put(current_scope, dec);
			mySymboltable.put(ident, mySymbolTableEntry);
		} else {
			return false;
		}

		return true;
	}

	public Dec lookup(String ident) {
		Dec dec = null;
		int temp_scope = scope_stack.peek();
		if (mySymboltable.containsKey(ident)) {
			mySymbolTableEntry = mySymboltable.get(ident);
			while (temp_scope > -1) {
				if (scope_stack.contains(temp_scope) && mySymbolTableEntry.containsKey(temp_scope)) {
					dec = mySymbolTableEntry.get(temp_scope);
					break;
				} else {
					temp_scope--;
					continue;
				}
			}
		}
		return dec;
	}

	public SymbolTable() {
		scope_stack = new Stack<Integer>();
		mySymboltable = new HashMap<String, LinkedHashMap<Integer,Dec>>();
		current_scope = 0;
		next_scope = 0;
		scope_stack = new Stack<Integer>();
	}

	@Override
	public String toString() {
		return mySymboltable.toString();
	}

}
