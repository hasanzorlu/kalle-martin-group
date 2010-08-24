/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sse2project.AbstractSyntaxTrees;

/**
 *
 * @author Kalle
 */
import sse2project.SourcePosition;

public class Operations extends OperationList{
    public Operations(Identifier2 ID2AST, IntegerLiteral INTAST, SourcePosition thePosition){
        super (thePosition);
        ID2 = ID2AST;
        INTLIT = INTAST;
    }

    public Operations(Synchronization sync, SourcePosition thePosition){
        super (thePosition);
        this.sync = sync;
    }

    public Object visit(Visitor v, Object o) {
    return v.visitOperations(this, o);
    }

    public Identifier2 ID2;
    public IntegerLiteral INTLIT;
    public Synchronization sync;

}