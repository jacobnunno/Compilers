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
13: LDA  0,-2(5) loading simpleVar
14: ST  0,-4(5) store value
15: ST  5,-6(5) push ofp
16: LDA  5,-6(5) push frame
17: LDA  0,1(7) load ac with ret ptr
18:	LDA  7,-15(7)	jump to main loc
19: LD  5,0(5) pop frame
20: LD  1,-4(5)  loading result
21: ST  0,0(1) assign: store value
22: LDA  0,-3(5) loading simpleVar
23: ST  0,-5(5) store value
24: LDC  0,1(0) loading int
25: ST  0,-6(5) store value
26: LD  1,-5(5)  loading result
27: ST  0,0(1) assign: store value
28: LD  0,-2(5) loading simpleVar
29: ST  0,-7(5) store value
30: LDC  0,1(0) loading int
31: ST  0,-8(5) store value
32: LD  1,-4(5) load 
33: SUB  0,1,0 perform operation 8
34: JGT  0,2(7) br if true
35: LDC  0,0(0) false case
36: LDA  7,1(7) jump
37: LDC  0,1(0) true case
39: LDA  0,-3(5) loading simpleVar
40: ST  0,-8(5) store value
41: LD  0,-3(5) loading simpleVar
42: ST  0,-10(5) store value
43: LD  0,-2(5) loading simpleVar
44: ST  0,-11(5) store value
45: LD  0,-9(5) load operand in register 0
46: LD  1,-9(5) load operand in register 1
47: MUL  0,1,0 perform operation MUL
48: LD  0,-8(5)  loading result
49: LD  1,-9(5) loading result
50: ST  1,0(0) assign: store value
51: ST  1,-7(5) 
52: LDA  0,-2(5) loading simpleVar
53: ST  0,-9(5) store value
54: LD  0,-2(5) loading simpleVar
55: ST  0,-11(5) store value
56: LDC  0,1(0) loading int
57: ST  0,-12(5) store value
58: LD  0,-10(5) load operand in register 0
59: LD  1,-10(5) load operand in register 1
60: SUB  0,1,0 perform operation SUB
61: LD  0,-9(5)  loading result
62: LD  1,-10(5) loading result
63: ST  1,0(0) assign: store value
64: ST  1,-8(5) 
65:	LDA  7,-27(7)	while jump
39: JEQ 7,26(7) jump forward
66: LD  0,-3(5) loading simpleVar
67: ST  0,-6(5) store value
68: ST  5,-7(5) push ofp
69: LDA  5,-7(5) push frame
70: LDA  0,1(7) load ac with ret ptr
71:	LDA  7,-68(7)	jump to main loc
72: LD  5,0(5) pop frame
73: LD  7,-1(5) return back to caller
11: LDA 7,62(7) jump forward
74: ST  5,0(5) push ofp
75: LDA  5,0(5) push frame
76: LDA  0,1(7) load ac with ret ptr
77:	LDA  7,-66(7)	jump to main loc
78: LD  5,0(5) pop frame
79: HALT  0,0,0 
