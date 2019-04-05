* C-Minus Compilation to TM Code
* File: cMinusTestFiles/test.cm
* Standard prelude
0: LD  6,0(0)
1: LDA  5,0(6)
2: ST  0,0(0)
4: ST  0,-1(5)
5: IN  0,0,0
6: LD  7,-1(5)
7: ST  0,-1(5)
8: LD  0,-2(5)
9: OUT  0,0,0
10: LD  7,-1(5)
3: LDA  7,7(7)
12: ST  0,-1(5) store return address
*************OFFSET CHECK -3 frameoffset AssignEXP
*************OFFSET CHECK -4SIMPLEVAR
13: LDA  0,-2(5) loading simpleVar
14: ST  0,-4(5) store value
*************OFFSET CHECK -5OPEXP
*************OFFSET CHECK -6SIMPLEVAR
15: LD  0,-2(5) loading simpleVar
16: ST  0,-6(5) store value
*************OFFSET CHECK -7frameoffset INTEXP
17: LDC  0,3(0) loading int
18: ST  0,-7(5) store value
19: LD  0,-6(5) load operand in register 0
20: LD  1,-7(5) load operand in register 1
21: ADD  0,0,1 perform operation ADD
22: ST  0,-5(5) store OpExp
23: LD  0,-4(5)  loading result
24: LD  1,-5(5) loading result
25: ST  1,0(0) assign: store value
26: ST  1,-3(5) 
27: LD  7,-1(5) return back to caller
11:	LDA 7, 16(7) jump forward
28: ST  5,0(5) push ofp
29: LDA  5,0(5) push frame
30: LDA  0,1(7) load ac with ret ptr
31:	LDA  7,-20(7)
32: LD  5,0(5) pop frame
33: HALT  0,0,0 
