/**
 * Copyright 2011-2013 FoundationDB, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* The original from which this derives bore the following: */

/*

   Derby - Class org.apache.derby.impl.sql.compile.UnaryOperatorNode

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package com.foundationdb.sql.parser;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.types.ValueClassName;

/**
 * A UnaryOperatorNode represents a built-in unary operator as defined by
 * the ANSI/ISO SQL standard.    This covers operators like +, -, NOT, and IS NULL.
 * Java operators are not represented here: the JSQL language allows Java
 * methods to be called from expressions, but not Java operators.
 *
 */

public class UnaryOperatorNode extends ValueNode
{
    protected String operator;
    protected String methodName;

    protected String resultInterfaceType;
    protected String receiverInterfaceType;

    /**
     * WARNING: operand may be NULL for COUNT(*).    
     */
    ValueNode operand;

    /**
     * Initializer for a UnaryOperatorNode.
     *
     * <ul>
     * @param operand The operand of the node
     * @param operator The name of the operator,
     * @param methodName Name of the method
     */

    public void init(Object operand,
                     Object operatorOrOpType,
                     Object methodNameOrAddedArgs) 
            throws StandardException {
        this.operand = (ValueNode)operand;
        this.operator = (String)operatorOrOpType;
        this.methodName = (String)methodNameOrAddedArgs;
    }

    /**
     * Initializer for a UnaryOperatorNode
     *
     * @param operand The operand of the node
     */
    public void init(Object operand) throws StandardException {
        this.operand = (ValueNode)operand;
    }

    /**
     * Fill this node with a deep copy of the given node.
     */
    public void copyFrom(QueryTreeNode node) throws StandardException {
        super.copyFrom(node);

        UnaryOperatorNode other = (UnaryOperatorNode)node;
        this.operator = other.operator;
        this.methodName = other.methodName;
        this.resultInterfaceType = other.resultInterfaceType;
        this.receiverInterfaceType = other.receiverInterfaceType;
        this.operand = (ValueNode)getNodeFactory().copyNode(other.operand,
                                                            getParserContext());
    }

    /**
     * Set the operator.
     *
     * @param operator The operator.
     */
    void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * Get the operator of this unary operator.
     *
     * @return The operator of this unary operator.
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Set the methodName.
     *
     * @param methodName The methodName.
     */
    void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    /**
     * Convert this object to a String.  See comments in QueryTreeNode.java
     * for how this should be done for tree printing.
     *
     * @return This object as a String
     */

    public String toString() {
        return "operator: " + operator + "\n" +
            "methodName: " + methodName + "\n" +
            super.toString();
    }

    /**
     * Prints the sub-nodes of this object.  See QueryTreeNode.java for
     * how tree printing is supposed to work.
     *
     * @param depth The depth of this node in the tree
     */

    public void printSubNodes(int depth) {
        super.printSubNodes(depth);

        if (operand != null) {
            printLabel(depth, "operand: ");
            operand.treePrint(depth + 1);
        }
    }

    /**
     * Get the operand of this unary operator.
     *
     * @return The operand of this unary operator.
     */
    public ValueNode getOperand() {
        return operand;
    }

    public void setOperand(ValueNode operand) {
        this.operand = operand;
    }

    /**
     * Accept the visitor for all visitable children of this node.
     * 
     * @param v the visitor
     *
     * @exception StandardException on error
     */
    void acceptChildren(Visitor v) throws StandardException {
        super.acceptChildren(v);

        if (operand != null) {
            operand = (ValueNode)operand.accept(v);
        }
    }
        
    /**
     * @throws StandardException 
     * {@inheritDoc}
     */
    protected boolean isEquivalent(ValueNode o) throws StandardException {
        if (isSameNodeType(o)) {
            // the first condition in the || covers the case when 
            // both operands are null.
            UnaryOperatorNode other = (UnaryOperatorNode)o;
            return (operator.equals(other.operator) && 
                    ((operand == other.operand)|| 
                     ((operand != null) && operand.isEquivalent(other.operand))));
        }
        return false;
    }

}
