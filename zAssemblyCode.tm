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
13: *************OFFSET CHECK  -3,0(0) SIMPLEVAR
14: LDA  0,-2(5) loading simpleVar
15: ST  0,-3(5) store value
16: *******************CALLEXP  1,-4(5)  
17: *************OFFSET CHECK  -4,0(0) frameoffset AssignEXP
18: LDA  0,-3(5) loading varExp
19: ST  0,-4(5) store value
20: *************OFFSET CHECK  -4,0(0) frameoffset INTEXP
21: LDC  0,1(0) loading int
22: ST  0,0(1) assign: store value
24: *************OFFSET CHECK  -5,0(0) SIMPLEVAR
25: LDA  0,-2(5) loading simpleVar
26: ST  0,-5(5) store value
27: *************OFFSET CHECK  -6,0(0) frameoffset INTEXP
28: LDC  0,1(0) loading int
29: LD  0,-7(5) load operand in register 0
30: LD  1,-7(5) load operand in register 1
31:   0,1,0 perform operation 
33: *************OFFSET CHECK  -7,0(0) frameoffset AssignEXP
34: LDA  0,-3(5) loading varExp
35: ST  0,-7(5) store value
36: *************OFFSET CHECK  -7,0(0) SIMPLEVAR
37: LDA  0,-3(5) loading simpleVar
38: ST  0,-7(5) store value
39: *************OFFSET CHECK  -8,0(0) SIMPLEVAR
40: LDA  0,-2(5) loading simpleVar
41: ST  0,-8(5) store value
42: LD  0,-9(5) load operand in register 0
43: LD  1,-9(5) load operand in register 1
44: MUL  0,1,0 perform operation MUL
45: ST  0,0(1) assign: store value
46: *************OFFSET CHECK  -8,0(0) frameoffset AssignEXP
47: LDA  0,-2(5) loading varExp
48: ST  0,-8(5) store value
49: *************OFFSET CHECK  -8,0(0) SIMPLEVAR
50: LDA  0,-2(5) loading simpleVar
51: ST  0,-8(5) store value
52: *************OFFSET CHECK  -9,0(0) frameoffset INTEXP
53: LDC  0,1(0) loading int
54: LD  0,-10(5) load operand in register 0
55: LD  1,-10(5) load operand in register 1
56: SUB  0,1,0 perform operation SUB
57: ST  0,0(1) assign: store value
58: JGT  0,-35(7) br if true
59: LDC  0,0(0) false case
60: LDA  7,-28(7) jump
61: LDC  0,1(0) true case
62: LD  7,-1(5) return back to caller
11:	LDA 7, 51(7) jump forward
63: ST  5,0(5) push ofp
64: LDA  5,0(5) push frame
65: LDA  0,1(7) load ac with ret ptr
66:	LDA  7,-56(7)
67: LD  5,0(5) pop frame
68: HALT  0,0,0 
