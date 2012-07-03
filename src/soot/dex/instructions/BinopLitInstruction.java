/* Soot - a Java Optimization Framework
 * Copyright (C) 2012 Michael Markert, Frank Hartmann
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package soot.dex.instructions;

import org.jf.dexlib.Code.Instruction;
import org.jf.dexlib.Code.LiteralInstruction;
import org.jf.dexlib.Code.TwoRegisterInstruction;
import org.jf.dexlib.Code.Format.Instruction22b;
import org.jf.dexlib.Code.Format.Instruction22s;

import soot.Local;
import soot.Value;
import soot.dex.DexBody;
import soot.dex.tags.IntOpTag;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;

public class BinopLitInstruction extends TaggedInstruction {

    public BinopLitInstruction (Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    public void jimplify (DexBody body) {
        if(!(instruction instanceof Instruction22s) && !(instruction instanceof Instruction22b))
            throw new IllegalArgumentException("Expected Instruction22s or Instruction22b but got: "+instruction.getClass());

        LiteralInstruction binOpLitInstr = (LiteralInstruction) this.instruction;
        int dest = ((TwoRegisterInstruction) instruction).getRegisterA();
        int source = ((TwoRegisterInstruction) instruction).getRegisterB();

        Local source1 = body.getRegisterLocal(source);

        IntConstant constant = IntConstant.v((int)binOpLitInstr.getLiteral());

        Value expr = getExpression(source1, constant);

        AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), expr);
        assign.addTag(getTag());

        defineBlock(assign);
        tagWithLineNumber(assign);
        body.add(assign);
    }

    private Value getExpression(Local source1, Value source2) {
        switch(instruction.opcode) {
        case ADD_INT_LIT16:
          setTag (new IntOpTag());
        case ADD_INT_LIT8:
          setTag (new IntOpTag());
            return Jimple.v().newAddExpr(source1, source2);

        case RSUB_INT:
          setTag (new IntOpTag());
        case RSUB_INT_LIT8:
          setTag (new IntOpTag());
            return Jimple.v().newSubExpr(source1, source2);

        case MUL_INT_LIT16:
          setTag (new IntOpTag());
        case MUL_INT_LIT8:
          setTag (new IntOpTag());
            return Jimple.v().newMulExpr(source1, source2);

        case DIV_INT_LIT16:
          setTag (new IntOpTag());
        case DIV_INT_LIT8:
          setTag (new IntOpTag());
            return Jimple.v().newDivExpr(source1, source2);

        case REM_INT_LIT16:
          setTag (new IntOpTag());
        case REM_INT_LIT8:
          setTag (new IntOpTag());
            return Jimple.v().newRemExpr(source1, source2);

        case AND_INT_LIT8:
          setTag (new IntOpTag());
        case AND_INT_LIT16:
          setTag (new IntOpTag());
            return Jimple.v().newAndExpr(source1, source2);

        case OR_INT_LIT16:
          setTag (new IntOpTag());
        case OR_INT_LIT8:
          setTag (new IntOpTag());
            return Jimple.v().newOrExpr(source1, source2);

        case XOR_INT_LIT16:
          setTag (new IntOpTag());
        case XOR_INT_LIT8:
          setTag (new IntOpTag());
            return Jimple.v().newXorExpr(source1, source2);

        case SHL_INT_LIT8:
          setTag (new IntOpTag());
            return Jimple.v().newShlExpr(source1, source2);

        case SHR_INT_LIT8:
          setTag (new IntOpTag());
            return Jimple.v().newShrExpr(source1, source2);

        case USHR_INT_LIT8:
          setTag (new IntOpTag());
            return Jimple.v().newUshrExpr(source1, source2);

        default :
            throw new RuntimeException("Invalid Opcode: " + instruction.opcode);
        }
    }

    @Override
    boolean overridesRegister(int register) {
        TwoRegisterInstruction i = (TwoRegisterInstruction) instruction;
        int dest = i.getRegisterA();
        return register == dest;
    }
}
